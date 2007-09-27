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
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.jdom.IDOMMethod;
import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.Templates;
import org.jboss.ide.eclipse.jdt.ui.wizards.MethodWizardPage;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class NewSelectMethodWizard extends EJBMethodWizard
{
   /**Constructor for the NewBusinessMethodWizard object */
   public NewSelectMethodWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("NewSelectMethodWizard.window.title"));//$NON-NLS-1$
   }

   /**
    * Adds a feature to the Content attribute of the NewBusinessMethodWizard object
    *
    * @exception Exception  Description of the Exception
    */
   protected void addContent() throws Exception
   {
      Templates manager = new Templates();
      NewSelectMethodWizardPage realPage = (NewSelectMethodWizardPage) page;
      IDOMMethod method = this.buildMethod();
      method.setFlags(method.getFlags() | Flags.AccAbstract);

      String viewType = realPage.getViewType();
      String query = realPage.getSelectQuery();

      String comment = manager.getString("wizards.ejb.method.select.comment", new Object[]{query});//$NON-NLS-1$
      method.setComment(comment);

      method.setBody(null);

      this.getType().addChild(method);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected MethodWizardPage createMethodWizardPage()
   {
      return new NewSelectMethodWizardPage();
   }
}
