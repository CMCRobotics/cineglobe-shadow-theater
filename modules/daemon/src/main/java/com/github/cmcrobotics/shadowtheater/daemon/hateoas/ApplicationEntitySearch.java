package com.github.cmcrobotics.shadowtheater.daemon.hateoas;

import uk.co.blackpepper.bowman.annotation.LinkedResource;
import uk.co.blackpepper.bowman.annotation.RemoteResource;

@RemoteResource("/applications/search")
public interface ApplicationEntitySearch {
    
   @LinkedResource
   HateOASEntity findOneByTitle(String title);
   
   @LinkedResource 
   HateOASEntity findByPublished(String published);
}
