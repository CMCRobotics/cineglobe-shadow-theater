package com.github.cmcrobotics.shadowtheater.daemon;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.github.cmcrobotics.shadowtheater.daemon.hateoas.ApplicationEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.LogEntryEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.RunSessionEntity;
import com.github.cmcrobotics.shadowtheater.daemon.hateoas.RunSessionEntitySearch;

import uk.co.blackpepper.bowman.Client;
import uk.co.blackpepper.bowman.ClientFactory;
import uk.co.blackpepper.bowman.Configuration;

@org.springframework.context.annotation.Configuration
public class HATEOASConfig {
    @Autowired
    ApplicationConfigProperties configProperties;
    
    ClientFactory factory;
    
    @PostConstruct
    void setupConnector(){
        factory = Configuration.builder().setBaseUri(configProperties.getBaseUri()).build()
                .buildClientFactory();
    }
    
    @Bean
    Client<ApplicationEntity> getApplicationClient(){
        return factory.create(ApplicationEntity.class);
    }
    
    @Bean
    Client<RunSessionEntity> getRunSessionClient(){
        return factory.create(RunSessionEntity.class);
    }
    
    @Bean
    Client<LogEntryEntity> getLogEntryClient(){
        return factory.create(LogEntryEntity.class);
    }
    
    @Bean
    Client<RunSessionEntitySearch> getRunSessionSearchClient(){
        return factory.create(RunSessionEntitySearch.class);
    }
    

}
