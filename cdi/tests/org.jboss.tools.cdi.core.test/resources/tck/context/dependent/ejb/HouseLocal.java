package org.jboss.jsr299.tck.tests.context.dependent.ejb;

import javax.ejb.Local;

@Local
public interface HouseLocal
{
   
   public RoomLocal open();
   
}
