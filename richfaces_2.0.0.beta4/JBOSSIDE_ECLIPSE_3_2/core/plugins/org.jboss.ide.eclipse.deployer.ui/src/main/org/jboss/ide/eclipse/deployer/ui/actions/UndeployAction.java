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
package org.jboss.ide.eclipse.deployer.ui.actions;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.DeployerCorePlugin;
import org.jboss.ide.eclipse.deployer.core.target.DeploymentException;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIMessages;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIPlugin;
import org.jboss.ide.eclipse.deployer.ui.IDeployerUIConstants;
import org.jboss.ide.eclipse.deployer.ui.util.DeployedResourceUtil;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class UndeployAction extends AbstractDeployAction
{
   /**Constructor for the XDocletRunAction object */
   public UndeployAction()
   {
      super();
   }

   /**
    * Gets the deploymentTarget attribute of the DeployAction object
    *
    * @param resource  Description of the Parameter
    * @return          The deploymentTarget value
    */
   protected ITarget[] getDeploymentTargets(IResource[] resources)
   {
      ITarget[] targets = new ITarget[resources.length];

      try
      {
         DeployerCorePlugin.getDefault().refreshDebugTargets();
         DeployerCorePlugin.getDefault().refreshTargets();

         Collection choices = new ArrayList();
         choices.addAll(DeployerCorePlugin.getDefault().getDebugTargets());
         choices.addAll(DeployerCorePlugin.getDefault().getTargets());

         for (int i = 0; i < resources.length; i++)
         {
            ITarget target = (ITarget) resources[i].getSessionProperty(IDeployerUIConstants.QNAME_TARGET);
            if (target == null)
            {
               DeployerUIPlugin.getDefault().showWarningMessage(
                     DeployerUIMessages.getString("UndeployAction.action.undeploy.not.text"));//$NON-NLS-1$
            }
            targets[i] = target;
         }
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to set session property with target", ce);//$NON-NLS-1$
      }

      return targets;
   }

   /**
    * Gets the title attribute of the UndeployAction object
    *
    * @param resource  Description of the Parameter
    * @return          The title value
    */
   protected String getTitle(IResource resource)
   {
      String title = DeployerUIMessages.getString("UndeployAction.title") + resource.getProjectRelativePath().toString();//$NON-NLS-1$
      return title;
   }

   /**
    * Description of the Method
    *
    * @param target    Description of the Parameter
    * @param resource  Description of the Parameter
    */
   protected void process(ITarget target, IResource resource)
   {
      try
      {
         target.undeploy(resource);
         DeployedResourceUtil.unlinkTarget(resource);
         // If the deployment succeeds, no information is provided
         // DeployerUIPlugin.getDefault().showInfoMessage(DeployerUIMessages.getString("UndeployAction.action.undeploy.succeed.text")); //$NON-NLS-1$
      }
      catch (DeploymentException de)
      {
         DeployerUIPlugin.getDefault().showErrorMessage(
               DeployerUIMessages.getString("UndeployAction.action.undeploy.failed.text") + de.getMessage());//$NON-NLS-1$
      }
   }
}
