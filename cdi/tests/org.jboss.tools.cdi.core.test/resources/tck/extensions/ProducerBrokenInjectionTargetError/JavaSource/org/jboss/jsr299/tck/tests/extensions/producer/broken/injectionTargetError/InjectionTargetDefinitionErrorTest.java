package org.jboss.jsr299.tck.tests.extensions.producer.broken.injectionTargetError;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.IntegrationTest;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.Resources;
import org.testng.annotations.Test;

/**
 * Test for adding a definition error via the ProcessInjectionTarget event.
 * 
 * @author David Allen
 *
 */
@Artifact
@Resources({
   @Resource(source="javax.enterprise.inject.spi.Extension", destination="WEB-INF/classes/META-INF/services/javax.enterprise.inject.spi.Extension")
})
// Must be an integration test as it needs a resource copied to a folder
@IntegrationTest
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
public class InjectionTargetDefinitionErrorTest extends AbstractJSR299Test
{

   @Test
   @SpecAssertions({
      @SpecAssertion(section = "11.5.6", id = "da")
   })
   public void testAddingDefinitionError()
   {
      assert false;
   }
}
