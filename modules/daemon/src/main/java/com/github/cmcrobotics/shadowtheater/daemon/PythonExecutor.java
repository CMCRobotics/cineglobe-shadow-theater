package com.github.cmcrobotics.shadowtheater.daemon;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.chibyhq.playar.model.Application;
import com.github.chibyhq.playar.model.ApplicationTypeConstants;
import com.github.chibyhq.playar.model.LogEntry;
import com.github.chibyhq.playar.model.RunSession;
import com.github.chibyhq.store.model.repositories.LogEntryRepository;
import com.github.chibyhq.store.model.repositories.RunSessionRepository;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.ApplicationEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.LogEntryEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.RunSessionEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.RunSessionEntitySearch;
import com.google.common.collect.ImmutableList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import uk.co.blackpepper.bowman.Client;

@Log
public class PythonExecutor {

    @Autowired
    Client<RunSessionEntity> hateoasRunSessionClient;
    
    @Autowired
    Client<LogEntryEntity> hateoasLogEntryClient;
    
    @Autowired
    Client<RunSessionEntitySearch> hateoasRunSessionSearchClient;

    private Map<String, Process> processesMap = new HashMap<>();
    private Map<String, ExecutorService> executorsMap = new HashMap<>();

    @Getter
    @Setter
    protected String pythonExecutable = "python3";

    public String start(ApplicationEntity application, RunSessionEntity session, Path applicationHome) throws Exception {

        ProcessBuilder pBuilder = new ProcessBuilder(getPythonExecutable(),
                ApplicationTypeConstants.PYTHON_APPLICATION_PY);
        pBuilder.directory(applicationHome.toFile());
        if (application.getEnvironment() != null) {
            pBuilder.environment().putAll(application.getEnvironment());
        }

        // Save the application code to a local file inside the application home
        if (application.getGeneratedContents() != null) {
            File appPyFile = applicationHome.resolve(ApplicationTypeConstants.PYTHON_APPLICATION_PY).toFile();
            if (appPyFile.canWrite()) {
                if ((!appPyFile.exists()) || application.getLastUpdatedOn().after(new Date(appPyFile.lastModified()))) {
                    log.log(Level.FINE, "Persisting Python application to " + appPyFile.getAbsolutePath());
                    Files.write(applicationHome.resolve(ApplicationTypeConstants.PYTHON_APPLICATION_PY),
                            ImmutableList.of(application.getGeneratedContents()), StandardCharsets.UTF_8);
                }
            } else {
                log.log(Level.FINEST,
                        "Application home or destination file not writeable, cannot write out" + appPyFile.getAbsolutePath());
            }
        }

        ////////////////////////
        // In Java 8 there is no portable way to obtain the process ID
        // allocating a random unique identifier instead.
        String processUUID = UUID.randomUUID().toString();
        session.setExecutionId(processUUID.toString());
        session.setRunning(true);
        session.setStopped(false);
        hateoasRunSessionClient.post(session);
        ////////////////////////

        try {
            Process process = pBuilder.start();
            processesMap.put(processUUID, process);
            ExecutorService executor = Executors.newFixedThreadPool(3);
            executorsMap.put(processUUID, executor);
            executor.execute(new ProcessWatcher(process, processUUID));
            executor.execute(new LineGobbler(process.getInputStream(), session, false, processUUID));
            executor.execute(new LineGobbler(process.getErrorStream(), session, true, processUUID));

        } catch (Exception e) {
            log.log(Level.FINE, "Could not attach to process " + processUUID, e);
        }

        return processUUID;
    }

    public void stop(RunSessionEntity session, Boolean removeContainer) {

        if (session.getExecutionId() != null) {
            String executionId = session.getExecutionId();
            // Double check that the session hasn't yet been marked as stopped
            Process process = processesMap.get(executionId);

            if (process.isAlive()) {
                process.destroyForcibly();
                session.setRunning(false);
                // No need to marked as Stopped, if we made it here it is
                // because the process was actively stopped
                session.setStoppedAt(new Date());
                hateoasRunSessionClient.post(session);
            }

            try {
                // In any case, cancel and remove any left over log flushing
                // executors
                if (executorsMap.containsKey(executionId)) {
                    executorsMap.get(executionId).shutdown();
                }
            } catch (Exception e) {
                log.warning("Could not interrupt all log flushing schedulers for process " + executionId);
            }
        } else {
            log.info("Cannot stop session " + session.uuid + " : No execution id !");
        }

    }

    @AllArgsConstructor
    class LineGobbler implements Runnable {
        InputStream is;
        RunSessionEntity session;
        boolean stdErr;
        String processId;

        public void run() {
            Scanner scanner = new Scanner(is);
            try {
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    LogEntryEntity lee = new LogEntryEntity(session);
                    lee.setLine(line);
                    lee.setError(stdErr);
                    hateoasLogEntryClient.post(lee);
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Output interrupted for process " + processId, e);
            } finally {
                scanner.close();
            }
        }
    }

    @AllArgsConstructor
    class ProcessWatcher implements Runnable {
        Process process;
        String executionId;

        public void run() {
            boolean stop = false;
            try {
                while (!stop) {
                    Thread.sleep(500);
                    if (!process.isAlive()) {
                        RunSessionEntity session = hateoasRunSessionSearchClient.get().findOneByExecutionIdAndRunning(executionId, true);
                        if (session != null) {
                            log.finest("Marking process " + executionId + " as 'not running'");
                            session.setRunning(false);
                            session.setExitCode(process.exitValue());
                            session.setStoppedAt(new Date());
                            hateoasRunSessionClient.post(session);
                        }
                        stop = true;
                    }
                }
            } catch (InterruptedException e) {
                log.warning("Process watcher for " + executionId + " was interrupted");
            }
        }
    }

    public Process getProcessForExecutionId(String executionId) {
        return processesMap.get(executionId);
    }
}
