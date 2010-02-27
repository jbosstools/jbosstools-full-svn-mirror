/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.jsr299.tck.tests.context.dependent;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
class InteriorDecorator implements Interior
{
   @Inject @Delegate @Room Interior interior;
   
   private static List<InteriorDecorator> instances = new ArrayList<InteriorDecorator>();
   
   private static boolean destroyed;
   
   public static void reset()
   {
      instances.clear();
      destroyed = false;
   }
   
   public void foo()
   {
      instances.add(this);      
      interior.foo();
   }
   
   @PreDestroy
   public void preDestroy()
   {
      destroyed = true;
   }
   
   /**
    * @return the instances
    */
   public static List<InteriorDecorator> getInstances()
   {
      return instances;
   }
   
   /**
    * @return the destroyed
    */
   public static boolean isDestroyed()
   {
      return destroyed;
   }

}
