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
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.projects;

import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.ProjectWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ProjectWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class J2EE14ProjectCreationWizard extends ProjectWizard
{
   /**Constructor for the J2EE14ProjectCreationWizard object */
   public J2EE14ProjectCreationWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("J2EE14ProjectCreationWizard.window.title"));//$NON-NLS-1$
   }

   /** Adds a feature to the Pages attribute of the J2EE13ProjectCreationWizard object */
   public void addPages()
   {
      super.addPages();

      this.fMainPage.setTitle(JDTJ2EEUIMessages.getString("J2EE14ProjectCreationWizardPage.title"));//$NON-NLS-1$
      this.fMainPage.setDescription(JDTJ2EEUIMessages.getString("J2EE14ProjectCreationWizardPage.description"));//$NON-NLS-1$
   }

   /**
    * Description of the Method
    *
    * @param mainPage  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected ProjectWizardPage createProjectWizardPage(WizardNewProjectCreationPage mainPage)
   {
      return new J2EE14ProjectCreationWizardPage(mainPage);
   }
}
