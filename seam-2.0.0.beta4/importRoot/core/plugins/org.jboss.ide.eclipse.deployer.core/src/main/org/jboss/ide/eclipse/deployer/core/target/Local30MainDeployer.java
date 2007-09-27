/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.core.target;

import org.jboss.ide.eclipse.deployer.core.DeployerCoreMessages;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class Local30MainDeployer extends LocalMainDeployer
{
   /** Constructor for the DeploymentTarget object */
   public Local30MainDeployer()
   {
      this.name = DeployerCoreMessages.getString("Local30MainDeployer.target.name.default");//$NON-NLS-1$
      this.url = DeployerCoreMessages.getString("Local30MainDeployer.target.url.default");//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public Object clone()
   {
      Local30MainDeployer target = new Local30MainDeployer();

      target.setName(this.name);
      target.setUrl(this.url);

      return target;
   }

   /**
    * Description of the Method
    *
    * @param obj  Description of the Parameter
    * @return     Description of the Return Value
    */
   public boolean equals(Object obj)
   {
      if (obj instanceof Local30MainDeployer)
      {
         Local30MainDeployer target = (Local30MainDeployer) obj;
         boolean result = this.getName().equals(target.getName());
         result = result && this.getUrl().equals(target.getUrl());
         return result;
      }
      return false;
   }
}
