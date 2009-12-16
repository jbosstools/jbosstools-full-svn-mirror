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
package org.jboss.jsr299.tck.tests.lookup.clientProxy.incontainer;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.war.WarArtifactDescriptor;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;

@Artifact
@IntegrationTest(runLocally = true)
@Resource(destination = WarArtifactDescriptor.WEB_XML_DESTINATION, source = "web.xml")
@SpecVersion(spec="cdi", version="20091101")
public class ClientProxyTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section = "5.4", id = "d")
   public void testInvocationIsProcessedOnCurrentInstance() throws Exception {
      WebClient webClient = new WebClient();
      webClient.setThrowExceptionOnFailingStatusCode(true);
      String response;
      response = webClient.getPage(getContextPath() + "Test/Garage?make=Honda").getWebResponse().getContentAsString();
      assert response.contains("Honda");
      response = webClient.getPage(getContextPath() + "Test/Garage?make=Toyota").getWebResponse().getContentAsString();
      assert response.contains("Toyota");
   }
}
