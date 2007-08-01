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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.jboss.ide.eclipse.core.AbstractPlugin;
import org.jboss.ide.eclipse.jdt.core.wizards.generation.IGenerationEngine;
import org.jboss.ide.eclipse.jdt.ui.JDTUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class ClassWizard extends BaseWizard
{
   /** Description of the Field */
   protected IGenerationEngine engine;

   /** Description of the Field */
   protected ClassWizardPage page;

   /**Constructor for the ClassWizard object */
   public ClassWizard()
   {
      this.setDefaultPageImageDescriptor(JavaPluginImages.DESC_WIZBAN_NEWCLASS);
      this.setDialogSettings(JavaPlugin.getDefault().getDialogSettings());
      this.setWindowTitle(JDTUIMessages.getString("ClassWizard.window.title"));//$NON-NLS-1$
   }

   /** Adds a feature to the Pages attribute of the ClassWizard object */
   public void addPages()
   {
      super.addPages();
      IWorkspace workspace = JavaPlugin.getWorkspace();
      this.page = this.createClassWizardPage(workspace.getRoot());
      this.addPage(page);
      this.page.init(this.getSelection());
   }

   /**
    * Gets the engine attribute of the ClassWizard object
    *
    * @return   The engine value
    */
   public IGenerationEngine getEngine()
   {
      return this.engine;
   }

   /**
    * Sets the engine attribute of the ClassWizard object
    *
    * @param engine  The new engine value
    */
   public void setEngine(IGenerationEngine engine)
   {
      this.engine = engine;
   }

   /**
    * Description of the Method
    *
    * @param root  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected abstract ClassWizardPage createClassWizardPage(IWorkspaceRoot root);

   /**
    * Description of the Method
    *
    * @param monitor                   Description of the Parameter
    * @exception InterruptedException  Description of the Exception
    * @exception CoreException         Description of the Exception
    */
   protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException
   {

      try
      {
         page.createType(monitor);
      }
      catch (CoreException e)
      {
         AbstractPlugin.log(e);
      }
      catch (InterruptedException e)
      {
         AbstractPlugin.log(e);
      }

      IType type = page.getCreatedType();
      if (type != null)
      {
         page.generate(getEngine(), monitor);

         ICompilationUnit cu = type.getCompilationUnit();
         if (cu.isWorkingCopy())
         {
            cu = (ICompilationUnit) cu.getOriginalElement();
         }

         IResource resource = cu.getCorrespondingResource();
         this.selectAndReveal(resource);
         this.openResource((IFile) resource);
      }
   }
}
