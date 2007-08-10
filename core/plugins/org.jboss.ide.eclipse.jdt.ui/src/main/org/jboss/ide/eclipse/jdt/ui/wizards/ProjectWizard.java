/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * Base class for project wizards.
 * Derived from org.eclipse.jdt.internal.ui.wizards.NewProjectCreationWizard
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class ProjectWizard extends BaseWizard implements IExecutableExtension
{
   /** Description of the Field */
   protected IConfigurationElement fConfigElement;

   /** Description of the Field */
   protected ProjectWizardPage fJavaPage;

   /** Description of the Field */
   protected WizardNewProjectCreationPage fMainPage;

   /**Constructor for the ProjectWizard object */
   public ProjectWizard()
   {
      super();
      this.setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWJPRJ);
      this.setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
      this.setWindowTitle(NewWizardMessages.JavaProjectWizard_title);//$NON-NLS-1$
   }

   /** Adds a feature to the Pages attribute of the ProjectWizard object */
   public void addPages()
   {
      super.addPages();
      this.fMainPage = new WizardNewProjectCreationPage("NewProjectCreationWizard");//$NON-NLS-1$
      this.fMainPage.setTitle(NewWizardMessages.JavaProjectWizardFirstPage_page_title);//$NON-NLS-1$
      this.fMainPage.setDescription(NewWizardMessages.JavaProjectWizardFirstPage_page_description);//$NON-NLS-1$
      this.addPage(fMainPage);

      this.fJavaPage = this.createProjectWizardPage(fMainPage);
      this.addPage(fJavaPage);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean performCancel()
   {
      this.fJavaPage.performCancel();
      return super.performCancel();
   }

   /**
    * Sets the initializationData attribute of the ProjectWizard object
    *
    * @param cfig          The new initializationData value
    * @param propertyName  The new initializationData value
    * @param data          The new initializationData value
    */
   public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data)
   {
      this.fConfigElement = cfig;
   }

   /**
    * Description of the Method
    *
    * @param mainPage  Description of the Parameter
    * @return          Description of the Return Value
    */
   protected abstract ProjectWizardPage createProjectWizardPage(WizardNewProjectCreationPage mainPage);

   /**
    * Description of the Method
    *
    * @param monitor                   Description of the Parameter
    * @exception InterruptedException  Description of the Exception
    * @exception CoreException         Description of the Exception
    */
   protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException
   {
      this.fJavaPage.performFinish(monitor);// use the full progress monitor
      BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
      this.selectAndReveal(fJavaPage.getJavaProject().getProject());
   }

   /**
    * Description of the Method
    *
    * @param shell  Description of the Parameter
    * @param e      Description of the Parameter
    */
   protected void handleFinishException(Shell shell, InvocationTargetException e)
   {
      String title = NewWizardMessages.JavaProjectWizard_op_error_title;//$NON-NLS-1$
      String message = NewWizardMessages.JavaProjectWizard_op_error_create_message;//$NON-NLS-1$
      ExceptionHandler.handle(e, getShell(), title, message);
   }
}
