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
package org.jboss.jsr299.tck.tests.event;

import static javax.enterprise.event.TransactionPhase.AFTER_COMPLETION;
import static javax.enterprise.event.TransactionPhase.AFTER_FAILURE;
import static javax.enterprise.event.TransactionPhase.AFTER_SUCCESS;
import static javax.enterprise.event.TransactionPhase.BEFORE_COMPLETION;

import javax.enterprise.event.Observes;

class TransactionalObservers
{
   public void train(@Observes(during=BEFORE_COMPLETION) DisobedientDog dog)
   {
   }

   public void trainNewTricks(@Observes(during=AFTER_COMPLETION) ShowDog dog)
   {
   }

   public void trainCompanion(@Observes(during=AFTER_FAILURE) SmallDog dog)
   {
   }

   public void trainSightSeeing(@Observes(during=AFTER_SUCCESS) LargeDog dog)
   {
   }

}
