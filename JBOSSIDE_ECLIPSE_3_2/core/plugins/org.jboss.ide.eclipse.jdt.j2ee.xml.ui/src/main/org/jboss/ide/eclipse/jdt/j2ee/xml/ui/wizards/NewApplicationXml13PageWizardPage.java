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
package org.jboss.ide.eclipse.jdt.j2ee.xml.ui.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.j2ee.xml.ui.JDTJ2EEXMLUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.xml.ui.JDTJ2EEXMLUIPlugin;
import org.jboss.ide.eclipse.jdt.ui.wizards.NewFileWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewApplicationXml13PageWizardPage extends NewFileWizardPage
{
   private final static String PAGE_NAME = NewApplicationXml13PageWizardPage.class.getName();

   /**
    *Constructor for the NewApplicationXml13PageWizardPage object
    *
    * @param selection  Description of the Parameter
    */
   public NewApplicationXml13PageWizardPage(IStructuredSelection selection)
   {
      super(PAGE_NAME, selection);
      this.setFileName("application.xml");//$NON-NLS-1$
   }

   /**
    * Gets the plugin attribute of the NewApplicationXml13PageWizardPage object
    *
    * @return   The plugin value
    */
   protected AbstractPlugin getPlugin()
   {
      return JDTJ2EEXMLUIPlugin.getDefault();
   }

   /**
    * Gets the resource attribute of the NewApplicationXml13PageWizardPage object
    *
    * @return   The resource value
    */
   protected String getResource()
   {
      return "application_1.3.xml";//$NON-NLS-1$
   }

   /**
    * Gets the wizardPageDescription attribute of the NewApplicationXml13PageWizardPage object
    *
    * @return   The wizardPageDescription value
    */
   protected String getWizardPageDescription()
   {
      return JDTJ2EEXMLUIMessages.getString("NewApplicationXml13PageWizardPage.description");//$NON-NLS-1$
   }

   /**
    * Gets the wizardPageTitle attribute of the NewApplicationXml13PageWizardPage object
    *
    * @return   The wizardPageTitle value
    */
   protected String getWizardPageTitle()
   {
      return JDTJ2EEXMLUIMessages.getString("NewApplicationXml13PageWizardPage.title");//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param filename  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected boolean validateFileName(String filename)
   {
      if (!filename.equals("application.xml")//$NON-NLS-1$
      )
      {
         this.setMessage(JDTJ2EEXMLUIMessages.getString("NewApplicationXml13PageWizardPage.message.extension"));//$NON-NLS-1$
      }
      return true;
   }
}
