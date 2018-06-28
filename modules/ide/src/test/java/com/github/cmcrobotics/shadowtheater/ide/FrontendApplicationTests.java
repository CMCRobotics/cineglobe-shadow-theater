package com.github.cmcrobotics.shadowtheater.ide;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.chibyhq.playar.model.Application;
import com.github.chibyhq.playar.model.LogEntry;
import com.github.chibyhq.playar.model.RunSession;
import com.github.chibyhq.store.model.repositories.ApplicationRepository;
import com.github.chibyhq.store.model.repositories.RunSessionRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FrontendApplicationTests {

	@Autowired
	ApplicationRepository applicationRepository;
	
	@Autowired
	RunSessionRepository runSessionRepository;
		
	@Test
	public void contextLoads() {
	    assertNotNull(applicationRepository);
	    assertNotNull(runSessionRepository);
	    Application app = Application.builder().environment(new HashMap<>()).build();
	    applicationRepository.save(app);
	    
	    RunSession session = new RunSession();
        session.setApplicationUUID(app.getUuid());
        
        LogEntry logEntry = LogEntry.builder().line("Log Entry Line").error(true).startedAt(new Date()).build();
        session.getLogEntries().add(logEntry);
        runSessionRepository.save(session);
        
        Application qApp = applicationRepository.findOne(app.getUuid());
        assertNotNull(qApp);
        
        RunSession qSession = runSessionRepository.findOne(session.getUuid());
        assertNotNull(qSession);
        assertEquals(1, qSession.getLogEntries().size());
	}

}
