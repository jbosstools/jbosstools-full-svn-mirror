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
public class Local40MainDeployer extends LocalMainDeployer
{
   /** Constructor for the DeploymentTarget object */
   public Local40MainDeployer()
   {
      this.name = DeployerCoreMessages.getString("Local40MainDeployer.target.name.default");//$NON-NLS-1$
      this.url = DeployerCoreMessages.getString("Local40MainDeployer.target.url.default");//$NON-NLS-1$
   }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public Object clone()
   {
      Local40MainDeployer target = new Local40MainDeployer();

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
      if (obj instanceof Local40MainDeployer)
      {
         Local40MainDeployer target = (Local40MainDeployer) obj;
         boolean result = this.getName().equals(target.getName());
         result = result && this.getUrl().equals(target.getUrl());
         return result;
      }
      return false;
   }
}
