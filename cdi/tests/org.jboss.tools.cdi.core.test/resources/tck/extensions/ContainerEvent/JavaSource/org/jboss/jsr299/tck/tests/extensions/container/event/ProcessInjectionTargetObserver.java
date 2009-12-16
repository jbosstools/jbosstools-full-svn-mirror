package org.jboss.jsr299.tck.tests.extensions.container.event;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

public class ProcessInjectionTargetObserver implements Extension
{
   private static ProcessInjectionTarget<Sheep> statelessSessionBeanEvent = null;
   private static ProcessInjectionTarget<Cow> statefulSessionBeanEvent = null;
   private static ProcessInjectionTarget<SheepInterceptor> sessionBeanInterceptorEvent = null;
   private static ProcessInjectionTarget<Farm> managedBeanEvent = null;
   private static ProcessInjectionTarget<?> eventWithTypeVariable = null;
   
   private static int event1Observed = 0;
   private static int event2Observed = 0;
   private static int event3Observed = 0;
   private static int event4Observed = 0;
   private static int event5Observed = 0;

   
   public void observeStatelessSessionBean(@Observes ProcessInjectionTarget<Sheep> event) {
      statelessSessionBeanEvent = event;
   }
   
   public void observeStatefulSessionBean(@Observes ProcessInjectionTarget<Cow> event) {
      statefulSessionBeanEvent = event;
   }
   
   public void observeSessionBeanInterceptor(@Observes ProcessInjectionTarget<SheepInterceptor> event) {
      sessionBeanInterceptorEvent = event;
   }
   
   public void observeManagedBean(@Observes ProcessInjectionTarget<Farm> event) {
      managedBeanEvent = event;
   }
   
   public void observeEvent1(@Observes ProcessInjectionTarget<? super String> event) {
      event1Observed++;
   }
   
   public void observeEvent2(@Observes ProcessInjectionTarget<? extends String> event) {
      event2Observed++;
   }
   
   public void observeEvent3(@Observes ProcessInjectionTarget<String> event) {
      event3Observed++;
   }
   
   public void observeEvent4(@Observes ProcessInjectionTarget<? super SheepLocal> event) {
      event4Observed++;
   }
   
   public void observeEvent5(@Observes ProcessInjectionTarget<? extends SheepLocal> event) {
      event5Observed++;
   }
   
   public <X> void processInjectionTarget(@Observes ProcessInjectionTarget<X> event)
   {
      eventWithTypeVariable = event;
   } 
   
   public static ProcessInjectionTarget<Sheep> getStatelessSessionBeanEvent()
   {
      return statelessSessionBeanEvent;
   }

   public static ProcessInjectionTarget<Cow> getStatefulSessionBeanEvent()
   {
      return statefulSessionBeanEvent;
   }

   public static ProcessInjectionTarget<SheepInterceptor> getSessionBeanInterceptorEvent()
   {
      return sessionBeanInterceptorEvent;
   }

   public static ProcessInjectionTarget<Farm> getManagedBeanEvent()
   {
      return managedBeanEvent;
   }
   
   public static ProcessInjectionTarget<?> getEventWithTypeVariable()
   {
      return eventWithTypeVariable;
   }

   public static int getEvent1Observed()
   {
      return event1Observed;
   }

   public static int getEvent2Observed()
   {
      return event2Observed;
   }

   public static int getEvent3Observed()
   {
      return event3Observed;
   }

   public static int getEvent4Observed()
   {
      return event4Observed;
   }

   public static int getEvent5Observed()
   {
      return event5Observed;
   }
}
