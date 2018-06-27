package com.github.cmcrobotics.shadowtheater.daemon;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.chibyhq.playar.model.ApplicationTypeEnum;
import com.github.chibyhq.playar.model.RunSession;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.ApplicationEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.LogEntryEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.RunSessionEntity;

import uk.co.blackpepper.bowman.Client;
import uk.co.blackpepper.bowman.ClientFactory;
import uk.co.blackpepper.bowman.Configuration;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Test
	public void contextLoads() {
	    ClientFactory factory = Configuration.builder().setBaseUri("http://localhost:8080/data").build()
	            .buildClientFactory();

	    Client<ApplicationEntity> applicationClient = factory.create(ApplicationEntity.class);
	    Client<RunSessionEntity> runSessionClient = factory.create(RunSessionEntity.class);
	    Client<LogEntryEntity> logEntryClient = factory.create(LogEntryEntity.class);
        
	    ApplicationEntity app = new ApplicationEntity();
	    app.setAvatar("star");
	    app.setDescription("test");
	    app.setType(ApplicationTypeEnum.PYTHON);
	    URI appURIId = applicationClient.post(app);
	    app = applicationClient.get(appURIId);
	    
	    RunSessionEntity runSession = new RunSessionEntity();
	    runSession.setApplicationUUID(app.getUuid());
	    runSession.setExitCode(-1);
	    URI runSessionId = runSessionClient.post(runSession);
	    
	    
	    LogEntryEntity logEntry = new LogEntryEntity(runSession);
	    URI logEntryId = logEntryClient.post(logEntry);
	    
	}

}
