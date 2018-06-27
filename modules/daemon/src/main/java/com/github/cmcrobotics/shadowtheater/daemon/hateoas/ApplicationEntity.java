package com.github.cmcrobotics.shadowtheater.daemon.hateoas;

import java.net.URI;

import uk.co.blackpepper.bowman.annotation.RemoteResource;
import uk.co.blackpepper.bowman.annotation.ResourceId;

@RemoteResource("/applications")
public class ApplicationEntity extends com.github.chibyhq.playar.model.Application {
    private URI id;
    @ResourceId public URI getId() { return id; }
}
