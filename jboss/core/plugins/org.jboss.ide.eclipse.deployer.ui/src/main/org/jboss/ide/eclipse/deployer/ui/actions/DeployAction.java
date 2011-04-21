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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.DeployerCorePlugin;
import org.jboss.ide.eclipse.deployer.core.target.DeploymentException;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIMessages;
import org.jboss.ide.eclipse.deployer.ui.DeployerUIPlugin;
import org.jboss.ide.eclipse.deployer.ui.dialogs.TargetChoiceDialog;
import org.jboss.ide.eclipse.deployer.ui.util.DeployedResourceUtil;

/**
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DeployAction extends AbstractDeployAction
{
   /**Constructor for the XDocletRunAction object */
   public DeployAction()
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

      DeployerCorePlugin.getDefault().refreshDebugTargets();
      DeployerCorePlugin.getDefault().refreshTargets();

      Collection choices = new ArrayList();
      choices.addAll(DeployerCorePlugin.getDefault().getDebugTargets());
      choices.addAll(DeployerCorePlugin.getDefault().getTargets());

      TargetChoiceDialog dialog = new TargetChoiceDialog(AbstractPlugin.getShell(), choices);
      if (dialog.open() == IDialogConstants.OK_ID)
      {
         target = dialog.getTarget();
      }

      return target;
   }


   /**
    * Gets the title attribute of the DeployAction object
    *
    * @param resource  Description of the Parameter
    * @return          The title value
    */
   protected String getTitle(IResource resource)
   {
      String title = DeployerUIMessages.getString("DeployAction.title") + resource.getProjectRelativePath().toString();//$NON-NLS-1$
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
         target.deploy(resource);
         DeployedResourceUtil.linkTarget(resource, target);
         // If the deployment succeeds, no information is provided
         // DeployerUIPlugin.getDefault().showInfoMessage(DeployerUIMessages.getString("DeployAction.action.deploy.succeed.text")); //$NON-NLS-1$
      }
      catch (DeploymentException de)
      {
         DeployerUIPlugin.getDefault().showErrorMessage(DeployerUIMessages.getString("DeployAction.action.deploy.failed.text") + de.getMessage());//$NON-NLS-1$
      }
   }
}
