package com.github.cmcrobotics.shadowtheater.daemon.hateoas;

import java.net.URI;

import uk.co.blackpepper.bowman.annotation.LinkedResource;
import uk.co.blackpepper.bowman.annotation.RemoteResource;
import uk.co.blackpepper.bowman.annotation.ResourceId;

@RemoteResource("/logEntries")
public class LogEntryEntity extends com.github.chibyhq.playar.model.LogEntry {
    private URI id;
    private RunSessionEntity runSession;
    public LogEntryEntity(RunSessionEntity runSession){
        this.runSession = runSession;
    }
    @ResourceId public URI getId() { return id; }
    @LinkedResource public RunSessionEntity getRunSession(){ return runSession; }
}
