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

package org.jboss.jsr299.tck.tests.deployment.lifecycle.broken.failsDuringValidation;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

/**
 * Tests a failure that occurs during validation of beans, which occurs after
 * the AfterBeanDiscovery event but before the AfterDeploymentValidation event
 * is raised.
 * 
 * @author David Allen
 * @author Dan Allen
 */
@Artifact
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
public class AfterBeanDiscoveryFailureTest extends AbstractJSR299Test
{
   
   // TODO make this an integration test using Extension
   
   @Test(groups={"rewrite"} )
   @SpecAssertions({
      @SpecAssertion(section = "11.5.2", id = "a"),
      @SpecAssertion(section = "12.2", id = "e"),
      @SpecAssertion(section = "12.2", id = "f")
   })
   // WBRI-312
   public void testDeploymentFailsDuringValidation()
   {
      assert false;
   }

   // FIXME need to communicate state of container at the time of failure
   //   @Override
   //   @AfterClass(alwaysRun = true, groups = "scaffold")
   //   public void afterClass() throws Exception
   //   {
   //      super.afterClass();
   //      assert BeanDiscoveryObserver.isManagerInitialized();
   //   }

}
