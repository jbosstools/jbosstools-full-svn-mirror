package org.jboss.jsr299.tck.tests.implementation.simple.lifecycle;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;

@RequestScoped
class Cod
{
   @PreDestroy
   public void destroyWithProblem()
   {
      throw new RuntimeException("Some error");
   }

   public void ping()
   {
      
   }
}
