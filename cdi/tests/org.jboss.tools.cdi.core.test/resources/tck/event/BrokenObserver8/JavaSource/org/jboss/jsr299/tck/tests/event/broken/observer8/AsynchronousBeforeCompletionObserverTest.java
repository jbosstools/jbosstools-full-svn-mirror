/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jsr299.tck.tests.event.broken.observer8;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;

/**
 * Tests an asynchronous observer method that is also a
 * transactional observer for the before completion phase.
 * 
 * Spec version: 20090519
 * 
 * @author David Allen
 */
@Artifact
@ExpectedDeploymentException(DeploymentFailure.class)
public class AsynchronousBeforeCompletionObserverTest extends AbstractJSR299Test
{
//   @Test(groups = { "events" })
//   @SpecAssertion(section = "10.5.6", id = "b")
// Asynchronous events are not specified now
   public void testAsynchronousObserverAsBeforeCompletionObserverFails()
   {
      assert false;
   }
}