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
package org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.fields;

import org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.AddEJBFragmentAction;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.fields.NewCMPFieldWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassFragmentWizard;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AddCMPFieldAction extends AddEJBFragmentAction
{
   /**Constructor for the AddCMPFieldAction object */
   public AddCMPFieldAction()
   {
   }

   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected ClassFragmentWizard createFieldWizard()
   {
      return new NewCMPFieldWizard();
   }

   /**
    * Gets the requiredInterfaces attribute of the AddCMPFieldAction object
    *
    * @return   The requiredInterfaces value
    */
   protected String[] getRequiredInterfaces()
   {
      return new String[]
      {"javax.ejb.EntityBean"};//$NON-NLS-1$
   }
}
