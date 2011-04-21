/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui;

import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.target.FileSystemCopy;
import org.jboss.ide.eclipse.deployer.core.target.Local30MainDeployer;
import org.jboss.ide.eclipse.deployer.core.target.Local32MainDeployer;
import org.jboss.ide.eclipse.deployer.core.target.Local40MainDeployer;
import org.jboss.ide.eclipse.deployer.ui.dialogs.FileSystemCopyEditDialog;
import org.jboss.ide.eclipse.deployer.ui.dialogs.LocalMainDeployerEditDialog;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class DeployerUIPlugin extends AbstractPlugin
{
   /** The shared instance */
   private static DeployerUIPlugin plugin;


   /** The constructor. */
   public DeployerUIPlugin()
   {
      plugin = this;
   }


   /**
    * Description of the Method
    *
    * @param context        Description of the Parameter
    * @exception Exception  Description of the Exception
    */
   public void start(BundleContext context)
      throws Exception
   {
      super.start(context);

      // Create the map of the deployment target
      // TODO : maybe it should be change to something
      //        more dynamic.
      TargetUIAdapter.getInstance().addInfo(new FileSystemCopy(), FileSystemCopyEditDialog.class, IDeployerUIConstants.IMG_OBJS_FILESYSTEM);
      TargetUIAdapter.getInstance().addInfo(new Local30MainDeployer(), LocalMainDeployerEditDialog.class, IDeployerUIConstants.IMG_OBJS_LOCAL_DEPLOYER);
      TargetUIAdapter.getInstance().addInfo(new Local32MainDeployer(), LocalMainDeployerEditDialog.class, IDeployerUIConstants.IMG_OBJS_LOCAL_DEPLOYER);
      TargetUIAdapter.getInstance().addInfo(new Local40MainDeployer(), LocalMainDeployerEditDialog.class, IDeployerUIConstants.IMG_OBJS_LOCAL_DEPLOYER);
   }


   /**
    * Returns the shared instance.
    *
    * @return   The default value
    */
   public static DeployerUIPlugin getDefault()
   {
      return plugin;
   }


   /**
    * Convenience method which returns the unique identifier of this plugin.
    *
    * @return   The unique indentifier value
    */
   public static String getUniqueIdentifier()
   {
      if (getDefault() == null)
      {
         // If the default instance is not yet initialized,
         // return a static identifier. This identifier must
         // match the plugin id defined in plugin.xml
         return "org.jboss.ide.eclipse.deployer.ui";//$NON-NLS-1$
      }
      return getDefault().getBundle().getSymbolicName();
   }
}

