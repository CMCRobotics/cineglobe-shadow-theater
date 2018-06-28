package com.github.cmcrobotics.shadowtheater.daemon;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.JmxException;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import com.github.cmcrobotics.shadowtheater.daemon.hateoas.ApplicationEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.RunSessionEntity;

import ch.qos.logback.classic.Level;
import lombok.extern.java.Log;
import uk.co.blackpepper.bowman.Client;

@Component
@ManagedResource(objectName="bean:name=executorService", description="Remote executor service", log=true,
logFile="jmx.log")
@Log
public class ExecutorService {
    @Autowired
    ApplicationConfigProperties configProperties;
    
    @Autowired
    PythonExecutor pythonExecutor;
    
    @Autowired
    Client<RunSessionEntity> runSessionClient;
    
    @Autowired
    Client<ApplicationEntity> applicationClient;
    
    @ManagedOperation(description="Start running an application by UUID")
    @ManagedOperationParameters({
      @ManagedOperationParameter(name = "applicationUuid", description = "The unique identifier for the application")})
    public String start(String applicationUUID){
        try {
            if(applicationUUID.startsWith("{") && applicationUUID.endsWith("}")){
                // Strip the start and end curly brackets, as HATEOAS does not use those
                applicationUUID.substring(1, applicationUUID.length()-1);
            }
            ApplicationEntity app = applicationClient.get(new URI(configProperties.baseUri+"/applications/"+applicationUUID));
            if(app == null){
                log.warning(String.format("Could not execute %s",applicationUUID));
            }
            RunSessionEntity rse = new RunSessionEntity();
            rse.setApplicationUUID(UUID.fromString(applicationUUID));
            URI rseId = runSessionClient.post(rse);
            
            
            pythonExecutor.start(app, rse, Paths.get(new URI("file://"+configProperties.getHome()+"/"+app.getType())));
            
            // Refresh the run session
            rse = runSessionClient.get(rseId);
            
            return rse.getUuid().toString();
        } catch (URISyntaxException e) {
            String msg =String.format("Could not find application %s : invalid URI",applicationUUID);
            log.warning(msg);
            throw new RuntimeException(msg);
        } catch(IOException ioe){
            String msg =String.format("Could not execute application %s : %s",applicationUUID, ioe.getMessage());
            log.warning(msg);
            throw new RuntimeException(msg);
        }
        
        
    }
}
