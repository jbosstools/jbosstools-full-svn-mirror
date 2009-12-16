package org.jboss.jsr299.tck.tests.definition.stereotype.broken.nonEmptyNamed;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.testng.annotations.Test;

@Artifact
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
public class NonEmptyNamedTest extends AbstractJSR299Test
{
   @Test
   @SpecAssertions({
      @SpecAssertion(section = "2.7.1.3", id = "aab"),
      @SpecAssertion(section = "2.5.2", id = "e")
   })
   public void testStereotypeWithNonEmptyNamed()
   {
      assert false;
   }
   
}
