package com.github.cmcrobotics.shadowtheater.daemon.hateoas;

import java.net.URI;

import uk.co.blackpepper.bowman.annotation.RemoteResource;
import uk.co.blackpepper.bowman.annotation.ResourceId;

@RemoteResource("/applications")
public class ApplicationEntity extends com.github.chibyhq.playar.model.Application implements HateOASEntity {
    private URI id;
    /* (non-Javadoc)
     * @see com.github.cmcrobotics.shadowtheater.daemon.hateoas.HateOASEntity#getId()
     */
    @Override
    @ResourceId public URI getId() { return id; }
}
