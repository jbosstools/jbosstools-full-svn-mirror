package org.jboss.jsr299.tck.tests.lookup.injection.enterprise;

import javax.ejb.Local;

@Local
public interface MegaPoorHenHouseLocal
{
   Fox getFox();
   boolean isPostConstructCalledAfterSuperclassInitializer();
   boolean isInitializerCalledAfterSuperclassInjection();
}
