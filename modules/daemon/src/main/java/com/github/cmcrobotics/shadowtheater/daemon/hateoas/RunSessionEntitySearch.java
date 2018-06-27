package com.github.cmcrobotics.shadowtheater.daemon.hateoas;

import uk.co.blackpepper.bowman.annotation.LinkedResource;
import uk.co.blackpepper.bowman.annotation.RemoteResource;

@RemoteResource("/runSessions/search")
public interface RunSessionEntitySearch {
    
   @LinkedResource
   RunSessionEntity findOneByExecutionIdAndRunning(String executionId, Boolean running);
}
