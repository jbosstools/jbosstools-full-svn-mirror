package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

@Cheap
class TropicalFruit implements Fruit
{
   @Inject
   private InjectionPoint metadata;

   public InjectionPoint getMetadata()
   {
      return metadata;
   }
}
