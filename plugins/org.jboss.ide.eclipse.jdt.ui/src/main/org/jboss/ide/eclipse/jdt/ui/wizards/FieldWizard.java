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
package org.jboss.ide.eclipse.jdt.ui.wizards;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class FieldWizard extends ClassFragmentWizard
{
   /** Description of the Field */
   protected FieldWizardPage page;

   /**Constructor for the FieldWizard object */
   public FieldWizard()
   {
      // Hum, hum, missing banner for field. Replaced with new method one.
      this.setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWMETH);
      this.setWindowTitle(JDTUIMessages.getString("FieldWizard.window.title"));//$NON-NLS-1$
   }

   /** Adds a feature to the Pages attribute of the FieldWizard object */
   public void addPages()
   {
      super.addPages();

      IWorkspace workspace = JavaPlugin.getWorkspace();
      this.page = this.createFieldWizardPage();
      this.addPage(this.page);
      this.page.init(this.getSelection());
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected abstract FieldWizardPage createFieldWizardPage();
}
