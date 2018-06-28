package com.github.cmcrobotics.shadowtheater.daemon;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.chibyhq.playar.model.ApplicationTypeConstants;
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
@Component
public class PythonExecutor {

    @Autowired
    Client<RunSessionEntity> runSessionClient;

    @Autowired
    Client<LogEntryEntity> logEntryClient;

    @Autowired
    Client<RunSessionEntitySearch> runSessionSearchClient;

    private Map<String, Process> processesMap = new HashMap<>();
    private Map<String, ExecutorService> executorsMap = new HashMap<>();

    @Getter
    @Setter
    protected String pythonExecutable = "python3";

    public String start(ApplicationEntity application, RunSessionEntity session, Path applicationHome)
            throws IOException {

        ProcessBuilder pBuilder = new ProcessBuilder(getPythonExecutable(),
                ApplicationTypeConstants.PYTHON_APPLICATION_PY);
        pBuilder.directory(applicationHome.toFile());
        if (application.getEnvironment() != null) {
            pBuilder.environment().putAll(application.getEnvironment());
        }

        // Save the application code to a local file inside the application home
        if (application.getGeneratedContents() != null) {
            File appPyFile = applicationHome.resolve(ApplicationTypeConstants.PYTHON_APPLICATION_PY).toFile();
            boolean justCreated = false;
            if (!appPyFile.exists()) {
                appPyFile.createNewFile();
                justCreated = true;
            }
            if (appPyFile.canWrite()) {
                if (justCreated || application.getLastUpdatedOn() == null
                        || application.getLastUpdatedOn().after(new Date(appPyFile.lastModified()))) {
                    log.log(Level.FINE, "Persisting Python application to " + appPyFile.getAbsolutePath());
                    Files.write(applicationHome.resolve(ApplicationTypeConstants.PYTHON_APPLICATION_PY),
                            ImmutableList.of(application.getGeneratedContents()), StandardCharsets.UTF_8);
                }
            } else {
                log.log(Level.FINEST, "Application home or destination file not writeable, cannot write out"
                        + appPyFile.getAbsolutePath());
            }
        }

        ////////////////////////
        // In Java 8 there is no portable way to obtain the process ID
        // allocating a random unique identifier instead.
        String processUUID = UUID.randomUUID().toString();
        session.setExecutionId(processUUID.toString());
        session.setRunning(true);
        session.setStopped(false);
        runSessionClient.patch(session.getId(), session);
        
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
                runSessionClient.patch(session.getId(), session);
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
            StringBuffer lineBuffer = new StringBuffer();
            try {
                int count = 0;
                while (scanner.hasNext()) {
                    lineBuffer.append((stdErr ? "E! " : ""));
                    lineBuffer.append(scanner.nextLine() + "\n");
                    count++;
                    if (count > 20) {
                        // Flush log to runSession
                        flushRunSession(lineBuffer.toString());
                        count = 0;
                        lineBuffer = new StringBuffer();
                    }
                    // LogEntryEntity lee = new LogEntryEntity(session);
                    // lee.setLine(line);
                    // lee.setError(stdErr);
                    // session.getLogEntries().add(lee);
                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Output interrupted for process " + processId, e);
            } finally {
                try {
                    flushRunSession(lineBuffer.toString());
                } catch (Throwable t) {
                    log.log(Level.WARNING, "Could not finally flush run session for process " + processId, t);
                }
                scanner.close();
            }
        }

        public void flushRunSession(String logUpdates) {
            session = runSessionClient.get(session.getId());
            session.setExecutionLog((session.getExecutionLog() == null ? "" : session.getExecutionLog()) + logUpdates);
            runSessionClient.patch(session.getId(), session);
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
                        RunSessionEntity session = runSessionSearchClient.get()
                                .findOneByExecutionIdAndRunning(executionId, true);
                        if (session != null) {
                            log.finest("Marking process " + executionId + " as 'not running'");
                            session.setRunning(false);
                            session.setExitCode(process.exitValue());
                            session.setStoppedAt(new Date());
                            runSessionClient.patch(session.getId(), session);
                        } else {
                            log.warning(String.format(
                                    "Run session %s was already marked as STOPPED - interrupted by user ?",
                                    executionId));
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
