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
package org.jboss.jsr299.tck.tests.decorators.custom.broken.finalBeanClass;

import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class AfterBeanDiscoveryObserver implements Extension
{

   private static CustomDecoratorImplementation decorator;

   @SuppressWarnings("unchecked")
   public void addInterceptors(@Observes AfterBeanDiscovery event,BeanManager beanManager)
   {
      AnnotatedType<VehicleDecorator> type = beanManager.createAnnotatedType(VehicleDecorator.class);
      Set<AnnotatedField<? super VehicleDecorator>> annotatedFields = type.getFields();
      AnnotatedField<? super VehicleDecorator> annotatedField = annotatedFields.iterator().next();
      decorator = new CustomDecoratorImplementation(annotatedField, beanManager);

      event.addBean(decorator);
   }
   
   public void vetoVehicleDecorator(@Observes ProcessAnnotatedType<VehicleDecorator> event)
   {
      event.veto();
   }

   public static CustomDecoratorImplementation getDecorator()
   {
      return decorator;
   }
}
