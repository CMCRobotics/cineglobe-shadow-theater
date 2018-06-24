package com.github.cmcrobotics.shadowtheater.ide.datahandlers;

import java.util.Date;

import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.github.chibyhq.playar.model.Application;

@Component
@RepositoryEventHandler(Application.class)
public class ApplicationHandler {
	
	@HandleBeforeCreate
	public void beforeCreate(Application app){
		app.setCreatedOn(new Date());
	}
	@HandleBeforeSave
	public void beforeSave(Application app){
		app.setLastUpdatedOn(new Date());
	}
	
	@HandleAfterCreate
	@HandleAfterSave
	public void persistApplicationToFileSystem(Application app) {
	}

}
