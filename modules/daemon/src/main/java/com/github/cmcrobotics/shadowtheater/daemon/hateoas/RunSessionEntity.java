package com.github.cmcrobotics.shadowtheater.daemon.hateoas;

import java.net.URI;

import uk.co.blackpepper.bowman.annotation.RemoteResource;
import uk.co.blackpepper.bowman.annotation.ResourceId;

@RemoteResource("/runSessions")

public class RunSessionEntity extends com.github.chibyhq.playar.model.RunSession implements HateOASEntity {
    private URI id;
    @Override
    @ResourceId public URI getId() { return id; }
}
