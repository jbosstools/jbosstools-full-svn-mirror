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
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods;

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
public class NewBusinessMethodWizard extends EJBMethodWizard
{
   /**Constructor for the NewBusinessMethodWizard object */
   public NewBusinessMethodWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("NewBusinessMethodWizard.window.title"));//$NON-NLS-1$
   }

   /**
    * Adds a feature to the Content attribute of the NewBusinessMethodWizard object
    *
    * @exception Exception  Description of the Exception
    */
   protected void addContent() throws Exception
   {
      Templates manager = new Templates();
      NewBusinessMethodWizardPage realPage = (NewBusinessMethodWizardPage) page;
      IDOMMethod method = this.buildMethod();

      String viewType = realPage.getViewType();

      String comment = manager.getString("wizards.ejb.method.business.comment", new Object[]{viewType});//$NON-NLS-1$
      method.setComment(comment);

      StringBuffer content = new StringBuffer();
      content.append("{\n");//$NON-NLS-1$
      content.append(manager.getString("wizards.ejb.method.business.body"));//$NON-NLS-1$
      content.append(this.computeReturnClause());
      content.append("}\n");//$NON-NLS-1$
      method.setBody(content.toString());

      this.getType().addChild(method);
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected MethodWizardPage createMethodWizardPage()
   {
      return new NewBusinessMethodWizardPage();
   }
}
