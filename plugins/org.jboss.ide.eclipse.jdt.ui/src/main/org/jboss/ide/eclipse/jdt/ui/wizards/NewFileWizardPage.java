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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.wizards.generation.FileTemplates;
import org.jboss.ide.eclipse.jdt.core.wizards.generation.ITemplates;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class NewFileWizardPage extends WizardNewFileCreationPage
{
   /**
    *Constructor for the NewFilterWizardPage object
    *
    * @param selection  Description of the Parameter
    * @param name       Description of the Parameter
    */
   public NewFileWizardPage(String name, IStructuredSelection selection)
   {
      super(name, selection);
      this.setTitle(this.getWizardPageTitle());
      this.setDescription(this.getWizardPageDescription());
   }

   /**
    * Gets the initialContents attribute of the NewHTMLPageWizardPage object
    *
    * @return   The initialContents value
    */
   protected InputStream getInitialContents()
   {
      return this.getInitialContents(this.getPlugin(), this.getResource());
   }

   /**
    * Gets the initialContents attribute of the NewFileWizardPage object
    *
    * @param plugin    Description of the Parameter
    * @param resource  Description of the Parameter
    * @return          The initialContents value
    */
   protected InputStream getInitialContents(AbstractPlugin plugin, String resource)
   {
      ITemplates templates = new FileTemplates(plugin);
      String content = templates.getString(resource);
      return new ByteArrayInputStream(content.getBytes());
   }

   /**
    * Gets the plugin attribute of the NewFileWizardPage object
    *
    * @return   The plugin value
    */
   protected abstract AbstractPlugin getPlugin();

   /**
    * Gets the resource attribute of the NewFileWizardPage object
    *
    * @return   The resource value
    */
   protected abstract String getResource();

   /**
    * Gets the wizardPageDescription attribute of the NewFileWizardPage object
    *
    * @return   The wizardPageDescription value
    */
   protected abstract String getWizardPageDescription();

   /**
    * Gets the wizardPageTitle attribute of the NewFileWizardPage object
    *
    * @return   The wizardPageTitle value
    */
   protected abstract String getWizardPageTitle();

   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected abstract boolean validateFileName(String filename);

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected boolean validatePage()
   {
      boolean value = super.validatePage();

      if (value)
      {
         String filename = this.getFileName();
         value = this.validateFileName(filename);
      }

      return value;
   }

}
