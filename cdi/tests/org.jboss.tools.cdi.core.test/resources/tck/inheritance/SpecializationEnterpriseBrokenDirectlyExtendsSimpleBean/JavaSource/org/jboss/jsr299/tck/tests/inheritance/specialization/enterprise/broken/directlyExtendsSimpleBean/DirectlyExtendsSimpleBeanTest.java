package org.jboss.jsr299.tck.tests.inheritance.specialization.enterprise.broken.directlyExtendsSimpleBean;


import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.DeploymentFailure;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ExpectedDeploymentException;
import org.jboss.testharness.impl.packaging.Packaging;
import org.jboss.testharness.impl.packaging.PackagingType;
import org.testng.annotations.Test;

@Artifact
@Packaging(PackagingType.EAR)
@ExpectedDeploymentException(DeploymentFailure.class)
@SpecVersion(spec="cdi", version="20091101")
public class DirectlyExtendsSimpleBeanTest extends AbstractJSR299Test
{
   
   
   @Test(groups={"specialization"}) 
   @SpecAssertion(section="3.2.4", id = "da")
   public void testSpecializingClassDirectlyExtendsSimpleBean()
   {
      assert false;
   }
   
}
