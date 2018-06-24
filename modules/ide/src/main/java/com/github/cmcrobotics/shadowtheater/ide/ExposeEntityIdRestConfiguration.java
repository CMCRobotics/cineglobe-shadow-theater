package com.github.cmcrobotics.shadowtheater.ide;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.github.chibyhq.playar.model.Application;
import com.github.chibyhq.playar.model.LogEntry;
import com.github.chibyhq.playar.model.RunSession;


@Configuration
public class ExposeEntityIdRestConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Application.class, RunSession.class, LogEntry.class);
    }
}