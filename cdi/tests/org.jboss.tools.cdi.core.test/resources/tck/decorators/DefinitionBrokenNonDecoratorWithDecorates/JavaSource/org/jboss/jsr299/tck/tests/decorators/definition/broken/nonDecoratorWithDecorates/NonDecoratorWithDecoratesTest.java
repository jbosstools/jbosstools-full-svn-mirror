package org.jboss.jsr299.tck.tests.decorators.definition.broken.nonDecoratorWithDecorates;

import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

/**
 * 
 * @author Shane Bryzak
 */
@Artifact
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
public class NonDecoratorWithDecoratesTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertion(section="8.1.2", id="cg")
   public void testNonDecoratorWithDecoratesAnnotationNotOK()
   {
      assert false;
   }
}
