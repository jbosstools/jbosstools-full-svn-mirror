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
package org.jboss.jsr299.tck.tests.lookup.typesafe.resolution;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Typed;

public class PetShop
{
   @Produces
   @Typed(String.class)
   private Dove brokenProducer = new Dove("charlie");

   @Produces @Typed(Dove.class)
   private Dove dove = new Dove("charlie");
   
   @Produces @Typed(Parrot.class)
   public Parrot getParrot()
   {
      return new Parrot("polly");
   }
   
   @Produces @Typed(Cat.class) @Tame
   private DomesticCat felix = new DomesticCat("felix");
   
   @Produces @Typed(Cat.class) @Wild
   public Lion getAslan()
   {
      return new Lion("timmy");
   }

}
