/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui.actions;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.DeployerCorePlugin;
import org.jboss.ide.eclipse.deployer.core.target.DeploymentException;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIMessages;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIPlugin;
import org.jboss.ide.eclipse.deployer.ui.IDeployerUIConstants;
import org.jboss.ide.eclipse.deployer.ui.dialogs.TargetChoiceDialog;
import org.jboss.ide.eclipse.deployer.ui.util.DeployedResourceUtil;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class RedeployAction extends AbstractDeployAction
{
   /**Constructor for the XDocletRunAction object */
   public RedeployAction()
   {
      super();
   }


   /**
    * Gets the deploymentTarget attribute of the DeployAction object
    *
    * @param resource  Description of the Parameter
    * @return          The deploymentTarget value
    */
   protected ITarget getDeploymentTarget(IResource resource)
   {
      ITarget target = null;

      try
      {
         DeployerCorePlugin.getDefault().refreshDebugTargets();
         DeployerCorePlugin.getDefault().refreshTargets();

         Collection choices = new ArrayList();
         choices.addAll(DeployerCorePlugin.getDefault().getDebugTargets());
         choices.addAll(DeployerCorePlugin.getDefault().getTargets());

         target = (ITarget) resource.getSessionProperty(IDeployerUIConstants.QNAME_TARGET);
         if (!choices.contains(target))
         {
            TargetChoiceDialog dialog = new TargetChoiceDialog(AbstractPlugin.getShell(), choices);
            if (dialog.open() == IDialogConstants.OK_ID)
            {
               target = dialog.getTarget();
            }
         }
      }
      catch (CoreException ce)
      {
         AbstractPlugin.logError("Unable to get target session property", ce);//$NON-NLS-1$
      }

      return target;
   }


   /**
    * Gets the title attribute of the RedeployAction object
    *
    * @param resource  Description of the Parameter
    * @return          The title value
    */
   protected String getTitle(IResource resource)
   {
      String title = DeployerUIMessages.getString("RedeployAction.title") + resource.getProjectRelativePath().toString();//$NON-NLS-1$
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
         target.redeploy(resource);
         DeployedResourceUtil.linkTarget(resource, target);
         // If the deployment succeeds, no information is provided
         // DeployerUIPlugin.getDefault().showInfoMessage(DeployerUIMessages.getString("RedeployAction.action.redeploy.succeed.text")); //$NON-NLS-1$
      }
      catch (DeploymentException de)
      {
         DeployerUIPlugin.getDefault().showErrorMessage(DeployerUIMessages.getString("RedeployAction.action.redeploy.failed.text") + de.getMessage());//$NON-NLS-1$
      }
   }
}
