/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.core.target;

import java.io.IOException;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DeploymentException extends IOException
{
   /**Constructor for the TargetDeploymentException object */
   public DeploymentException()
   {
      super();
   }


   /**
    *Constructor for the TargetDeploymentException object
    *
    * @param message  Description of the Parameter
    */
   public DeploymentException(String message)
   {
      super(message);
   }
}
