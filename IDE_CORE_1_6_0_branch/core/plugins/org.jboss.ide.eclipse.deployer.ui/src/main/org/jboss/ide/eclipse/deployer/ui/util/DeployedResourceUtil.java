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
package org.jboss.ide.eclipse.deployer.ui.util;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.IDeployerUIConstants;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DeployedResourceUtil
{
   /** Constructor for the DeployedResourceUtil object */
   private DeployedResourceUtil()
   {
   }

   /**
    * Gets the linkedTarget attribute of the DeployedResourceUtil class
    *
    * @param resource  Description of the Parameter
    * @return          The linkedTarget value
    */
   public static ITarget getLinkedTarget(IResource resource)
   {
      try
      {
         return (ITarget) resource.getSessionProperty(IDeployerUIConstants.QNAME_TARGET);
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to get target session property ", ce);//$NON-NLS-1$
      }
      return null;
   }

   /**
    * Gets the linkedTarget attribute of the DeployedResourceUtil class
    *
    * @param resource  Description of the Parameter
    * @return          The linkedTarget value
    */
   public static boolean isLinkedTarget(IResource resource)
   {
      try
      {
         return (resource.getSessionProperty(IDeployerUIConstants.QNAME_TARGET) != null);
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to get target session property ", ce);//$NON-NLS-1$
      }
      return false;
   }

   /**
    * Description of the Method
    *
    * @param resource  Description of the Parameter
    * @param target    Description of the Parameter
    */
   public static void linkTarget(IResource resource, ITarget target)
   {
      try
      {
         resource.setSessionProperty(IDeployerUIConstants.QNAME_TARGET, target);
         resource.setSessionProperty(IDeployerUIConstants.QNAME_DEPLOYED, "true");
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to set target session property ", ce);//$NON-NLS-1$
      }
   }

   /**
    * Description of the Method
    *
    * @param resource  Description of the Parameter
    */
   public static void unlinkTarget(IResource resource)
   {
      try
      {
         resource.setSessionProperty(IDeployerUIConstants.QNAME_TARGET, null);
         resource.setSessionProperty(IDeployerUIConstants.QNAME_DEPLOYED, null);
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to set target session property ", ce);//$NON-NLS-1$
      }
   }
}
