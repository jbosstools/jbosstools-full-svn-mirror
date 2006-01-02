/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.deployer.ui;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.deployer.core.target.ITarget;
import org.jboss.ide.eclipse.deployer.ui.dialogs.TargetEditDialog;

/**
 * @author    letiemble
 * @version   $Revision$
 */
public class TargetUIAdapter
{
   private Map dialogs = new HashMap();
   private Map images = new HashMap();
   private List targets = new ArrayList();
   private static TargetUIAdapter adapter = new TargetUIAdapter();


   /**Constructor for the TargetInfo object */
   private TargetUIAdapter() { }


   /**
    * Adds a feature to the Info attribute of the TargetUIAdapter object
    *
    * @param target  The feature to be added to the Info attribute
    * @param dialog  The feature to be added to the Info attribute
    * @param image   The feature to be added to the Info attribute
    */
   public void addInfo(ITarget target, Class dialog, String image)
   {
      this.targets.add(target);
      this.dialogs.put(target.getClass(), dialog);
      this.images.put(target.getClass(), image);
   }


   /**
    * Gets the targets attribute of the TargetUIAdapter object
    *
    * @return   The targets value
    */
   public List getAvailableTargets()
   {
      return this.targets;
   }


   /**
    * Gets the dialog attribute of the TargetInfo object
    *
    * @param shell   Description of the Parameter
    * @param target  Description of the Parameter
    * @return        The dialog value
    */
   public TargetEditDialog getDialog(Shell shell, ITarget target)
   {
      TargetEditDialog dialog = null;
      try
      {
         Class dialogClass = (Class) this.dialogs.get(target.getClass());
         Constructor constructor = dialogClass.getConstructor(new Class[]{Shell.class, ITarget.class});
         dialog = (TargetEditDialog) constructor.newInstance(new Object[]{shell, target});
      }
      catch (Exception e)
      {
         AbstractPlugin.logError("Cannot create a dialog for target " + target.getClass().getName(), e);//$NON-NLS-1$
      }
      return dialog;
   }


   /**
    * Gets the image attribute of the TargetInfo object
    *
    * @param target  Description of the Parameter
    * @return        The image value
    */
   public Image getImage(ITarget target)
   {
      String name = (String) this.images.get(target.getClass());
      return DeployerUIImages.getImage(name);
   }


   /**
    * Gets the instance attribute of the TargetUIAdapter class
    *
    * @return   The instance value
    */
   public static TargetUIAdapter getInstance()
   {
      return adapter;
   }
}

