package org.domain.lamp.session;

import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.jboss.seam.core.FacesMessages;

@Name("ping")
public class Ping {
	
    @Logger private Log log;
	
    @In 
    FacesMessages facesMessages;
    
    //seam-gen method
    public String ping()
    {
        //implement your business logic here
        log.info("ping.ping() action called");
        facesMessages.add("ping");
        return "success";
    }
	
   //add additional action methods
	
}
