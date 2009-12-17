package org.jboss.jsr299.tck.tests.implementation.enterprise.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.inject.Inject;

@Stateful
@Important
public class Heidelburg implements KleinStadt, SchoeneStadt
{
   private String name;

   @Inject
   private GrossStadt grossStadt;
   
   @PostConstruct
   public void begruendet()
   {
      grossStadt.kleinStadtCreated();
      name = "Heidelburg";
   }

   @Remove
   public void zustandVergessen()
   {
   }

   @PreDestroy
   public void zustandVerloren()
   {
      grossStadt.kleinStadtDestroyed();
   }
   
   public void ping() {}

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

}
