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
package org.jboss.ide.eclipse.jdt.ws.ui.wizards.webservices;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassWizardPage;
import org.jboss.ide.eclipse.jdt.ws.core.generation.WSDL2JavaGenerationEngine;
import org.jboss.ide.eclipse.jdt.ws.ui.JDTWSUIMessages;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewWebServicesTestClientWizard extends ClassWizard
{
   /**Constructor for the NewWebServicesTestClientWizard object */
   public NewWebServicesTestClientWizard()
   {
      this.setWindowTitle(JDTWSUIMessages.getString("NewWebServicesTestClientWizard.window.title"));//$NON-NLS-1$
   }

   /** Adds a feature to the Pages attribute of the NewWebServicesTestClientWizard object */
   public void addPages()
   {
      super.addPages();

      WSDL2JavaGenerationEngine engine = new WSDL2JavaGenerationEngine();
      this.setEngine(engine);
   }

   /**
    * Description of the Method
    *
    * @param root  Description of the Parameter
    * @return      Description of the Return Value
    */
   protected ClassWizardPage createClassWizardPage(IWorkspaceRoot root)
   {
      return new NewWebServicesTestClientWizardPage(root);
   }

   /**
    * Description of the Method
    *
    * @param monitor                   Description of the Parameter
    * @exception InterruptedException  Description of the Exception
    * @exception CoreException         Description of the Exception
    */
   protected void finishPage(IProgressMonitor monitor) throws InterruptedException, CoreException
   {
      this.page.generate(this.getEngine(), monitor);
   }
}
