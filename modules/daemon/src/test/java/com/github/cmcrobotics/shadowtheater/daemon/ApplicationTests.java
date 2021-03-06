package com.github.cmcrobotics.shadowtheater.daemon;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.chibyhq.playar.model.ApplicationTypeEnum;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.ApplicationEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.HateOASEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.LogEntryEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.RunSessionEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.RunSessionEntitySearch;
import com.google.common.collect.Iterables;

import uk.co.blackpepper.bowman.Client;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    Client<ApplicationEntity> applicationClient;
    @Autowired
    Client<RunSessionEntity> runSessionClient;
    @Autowired
    Client<LogEntryEntity> logEntryClient;
    @Autowired
    Client<RunSessionEntitySearch> runSessionEntitySearchClient;
    
    @Autowired
    PythonExecutor pythonExecutor;
    
    @Autowired
    ExecutorService executorService;
    
//	@Test
//	public void testRepository() {
//	    ApplicationEntity app = new ApplicationEntity();
//	    app.setAvatar("star");
//	    app.setDescription("test");
//	    app.setType(ApplicationTypeEnum.PYTHON);
//	    URI appURIId = applicationClient.post(app);
//	    // Refresh to obtain the autogenerated UUID
//	    app = applicationClient.get(appURIId);
//	    assertNotNull(app.getUuid());
//	    
//	    RunSessionEntity runSession = new RunSessionEntity();
//	    runSession.setApplicationUUID(app.getUuid());
//	    runSession.setRunning(true);
//	    runSession.setExecutionId(UUID.randomUUID().toString());
//	    URI runSessionId = runSessionClient.post(runSession);
//	    // Refresh to obtain the autogenerated UUID
//	    runSession = runSessionClient.get(runSessionId);
//	    
//	    assertNotNull(runSession.getUuid());
//	    
//	    LogEntryEntity logEntry = new LogEntryEntity(runSession);
//	    URI logEntryId = logEntryClient.post(logEntry);
//	    logEntry = logEntryClient.get(logEntryId);
//	    assertNotNull(logEntry.getUuid());
//	    
//	    RunSessionEntity queriedRunSession = runSessionEntitySearchClient.get().findOneByExecutionIdAndRunning(runSession.getExecutionId(), true);
//	    
//	    assertEquals(runSession.getUuid(), queriedRunSession.getUuid());
//	    assertEquals(runSession.getExecutionId(), queriedRunSession.getExecutionId());
//	}
	
//	@Test
//	public void testPythonExecutor() throws Exception{
//	    ApplicationEntity app = new ApplicationEntity();
//        app.setAvatar("star");
//        app.setDescription("test");
//        app.setType(ApplicationTypeEnum.PYTHON);
//        app.setGeneratedContents(String.join("\n", "import time", "WIDTH=10", "HEIGHT=10", "def draw():", "  screen.fill((128,0,0))",
//                        "print('OUTPUT',flush=True)", "print('OUTPUT',flush=True)", "print('OUTPUT',flush=True)",
//                        "time.sleep(0.5)", "print('OUTPUT',flush=True)", "print('OUTPUT',flush=True)", "time.sleep(0.5)",
//                        "quit()"));
//        URI appURIId = applicationClient.post(app);
//        // Refresh to obtain the autogenerated UUID
//        app = applicationClient.get(appURIId);
//        assertNotNull(app.getUuid());
//        
//        Path tempDirectory = Files.createTempDirectory("test-python-executor");
//        
//        RunSessionEntity runSession = new RunSessionEntity();
//        runSession.setApplicationUUID(app.getUuid());
//        URI runSessionId = runSessionClient.post(runSession);
//        // Refresh to obtain the autogenerated UUID
//        runSession = runSessionClient.get(runSessionId);
//        
//        pythonExecutor.start(app, runSession, tempDirectory);
//
//        Thread.sleep(3000);
//        
//        final RunSessionEntity qRunSession= runSessionClient.get(runSessionId);
//        
//        // TODO : Replace with a proper bowman search interface.
//        // For now, we query all and filter by RunSession
//        assertEquals("OUTPUT\nOUTPUT\nOUTPUT\nOUTPUT\nOUTPUT\n", qRunSession.getExecutionLog());
//
//	}
	
	@Test
	public void testExecutorService() throws InterruptedException{
	  clearAll(runSessionClient);
	  clearAll(applicationClient);
      ApplicationEntity app = new ApplicationEntity();
      app.setAvatar("Test Executor Service");
      app.setDescription("Test app for the executor service");
      app.setType(ApplicationTypeEnum.PYTHON);
      app.setGeneratedContents(String.join("\n",
                      "print('OUTPUT',flush=True)", "print('OUTPUT',flush=True)", "print('OUTPUT',flush=True)",
                      "print('OUTPUT',flush=True)", "print('OUTPUT',flush=True)",
                      "quit()"));
      URI appURIId = applicationClient.post(app);
      // Refresh to obtain the autogenerated UUID
      app = applicationClient.get(appURIId);
      String runSessionUUID = executorService.start(app.getUuid().toString());
      assertNotNull(runSessionUUID);
      Thread.sleep(500);
      
      assertEquals(1, Iterables.size(runSessionClient.getAll()));
      
//      List<RunSessionEntity> list = StreamSupport.stream(runSessionClient.getAll().spliterator(sess -> sess.), false).filter().collect(Collectors.toList());
	}

private void clearAll(Client<? extends HateOASEntity> client) {
    // TODO Auto-generated method stub
    client.getAll().forEach(obj -> client.delete(obj.getId()));
}

}
