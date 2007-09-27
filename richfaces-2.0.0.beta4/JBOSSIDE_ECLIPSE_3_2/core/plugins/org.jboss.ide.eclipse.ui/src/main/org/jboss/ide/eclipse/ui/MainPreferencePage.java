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
package org.jboss.ide.eclipse.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Empty preference page for JBoss-IDE.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */

public class MainPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
   private Image image;

   /** Default constructor */
   public MainPreferencePage()
   {
      this.setDescription(UIMessages.getString("MainPreferencePage.description"));//$NON-NLS-1$
   }

   /** Description of the Method */
   public void dispose()
   {
      if (this.image != null)
      {
         this.image.dispose();
      }
      super.dispose();
   }

   /**
    * Initialization
    *
    * @param workbench  Workbench
    */
   public void init(IWorkbench workbench)
   {
   }

   /**
    * Create the content of the preference page
    *
    * @param parent  Parent
    * @return        The content of the preference page
    */
   protected Control createContents(Composite parent)
   {
      GridData layoutData;
      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(new GridLayout(1, false));
      layoutData = new GridData(GridData.FILL_BOTH);
      composite.setLayoutData(layoutData);

      try
      {
         URL url = new URL(UIPlugin.getDefault().getBundle().getEntry("/"), "resources/jbosside-logo.png");//$NON-NLS-1$ //$NON-NLS-2$
         ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
         this.image = descriptor.createImage();

         if (this.image != null)
         {
            Label lbl = new Label(composite, SWT.NONE);
            lbl.setImage(this.image);
            layoutData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
            lbl.setLayoutData(layoutData);

            lbl = new Label(composite, SWT.NONE);
            layoutData = new GridData(GridData.FILL_BOTH);
            lbl.setLayoutData(layoutData);
         }
      }
      catch (MalformedURLException mfue)
      {
         // Do nothing
      }

      return composite;
   }
}
