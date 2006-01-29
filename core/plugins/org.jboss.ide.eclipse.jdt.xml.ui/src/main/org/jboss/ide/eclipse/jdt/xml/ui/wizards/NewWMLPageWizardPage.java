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
package org.jboss.ide.eclipse.jdt.xml.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.ui.wizards.NewFileWizardPage;
import org.jboss.ide.eclipse.jdt.xml.core.JDTXMLCorePlugin;
import org.jboss.ide.eclipse.jdt.xml.ui.JDTXMLUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewWMLPageWizardPage extends NewFileWizardPage
{
   private final static String PAGE_NAME = NewWMLPageWizardPage.class.getName();

   /**
    *Constructor for the NewWMLPageWizardPage object
    *
    * @param selection  Description of the Parameter
    */
   public NewWMLPageWizardPage(IStructuredSelection selection)
   {
      super(PAGE_NAME, selection);
   }

   /**
    * Gets the plugin attribute of the NewWMLPageWizardPage object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTXMLCorePlugin.getDefault();
   }

   /**
    * Gets the resource attribute of the NewWMLPageWizardPage object
    *
    * @return   The resource value
    */
   protected String getResource()
   {
      return "template.wml";//$NON-NLS-1$
   }

   /**
    * Gets the wizardPageDescription attribute of the NewWMLPageWizardPage object
    *
    * @return   The wizardPageDescription value
    */
   protected String getWizardPageDescription()
   {
      return JDTXMLUIMessages.getString("NewWMLPageWizardPage.description");//$NON-NLS-1$
   }

   /**
    * Gets the wizardPageTitle attribute of the NewWMLPageWizardPage object
    *
    * @return   The wizardPageTitle value
    */
   protected String getWizardPageTitle()
   {
      return JDTXMLUIMessages.getString("NewWMLPageWizardPage.title");//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected boolean validateFileName(String filename)
   {
      if (!filename.endsWith(".wml")//$NON-NLS-1$
      )
      {
         this.setMessage(JDTXMLUIMessages.getString("NewWMLPageWizardPage.message.extension"));//$NON-NLS-1$
      }
      return true;
   }
}
