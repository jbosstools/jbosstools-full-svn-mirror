/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.deployer.core.target;

import org.jboss.ide.eclipse.deployer.core.DeployerCoreMessages;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class Local32MainDeployer extends LocalMainDeployer
{
   /** Constructor for the DeploymentTarget object */
   public Local32MainDeployer()
   {
      this.name = DeployerCoreMessages.getString("Local32MainDeployer.target.name.default");//$NON-NLS-1$
      this.url = DeployerCoreMessages.getString("Local32MainDeployer.target.url.default");//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public Object clone()
   {
      Local32MainDeployer target = new Local32MainDeployer();

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
      if (obj instanceof Local32MainDeployer)
      {
         Local32MainDeployer target = (Local32MainDeployer) obj;
         boolean result = this.getName().equals(target.getName());
         result = result && this.getUrl().equals(target.getUrl());
         return result;
      }
      return false;
   }
}
