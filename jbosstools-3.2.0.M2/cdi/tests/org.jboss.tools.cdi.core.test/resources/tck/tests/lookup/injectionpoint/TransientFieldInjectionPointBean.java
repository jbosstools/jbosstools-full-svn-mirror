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

package org.jboss.jsr299.tck.tests.lookup.injectionpoint;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Test bean to inject another bean which uses injection point metadata in a
 * field
 * 
 * @author David Allen
 * 
 */
@RequestScoped
class TransientFieldInjectionPointBean
{
   @Inject @AnimalStereotype
   private transient BeanWithInjectionPointMetadata injectedBean;

   public BeanWithInjectionPointMetadata getInjectedBean()
   {
      return injectedBean;
   }
}
