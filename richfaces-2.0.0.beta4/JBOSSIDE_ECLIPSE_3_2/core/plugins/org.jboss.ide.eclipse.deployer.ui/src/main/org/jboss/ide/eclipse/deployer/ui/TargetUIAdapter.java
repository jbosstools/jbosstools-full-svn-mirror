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
   private TargetUIAdapter()
   {
   }

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
         Constructor constructor = dialogClass.getConstructor(new Class[]
         {Shell.class, ITarget.class});
         dialog = (TargetEditDialog) constructor.newInstance(new Object[]
         {shell, target});
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
