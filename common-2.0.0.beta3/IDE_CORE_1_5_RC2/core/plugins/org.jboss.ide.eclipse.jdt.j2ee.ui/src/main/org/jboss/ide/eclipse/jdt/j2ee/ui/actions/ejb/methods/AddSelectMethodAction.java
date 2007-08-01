/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.methods;

import org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.AddEJBFragmentAction;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods.NewSelectMethodWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassFragmentWizard;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AddSelectMethodAction extends AddEJBFragmentAction
{
   /**Constructor for the AddSelectMethodAction object */
   public AddSelectMethodAction() { }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected ClassFragmentWizard createFieldWizard()
   {
      return new NewSelectMethodWizard();
   }


   /**
    * Gets the requiredInterfaces attribute of the AddSelectMethodAction object
    *
    * @return   The requiredInterfaces value
    */
   protected String[] getRequiredInterfaces()
   {
      return new String[]{"javax.ejb.EntityBean"};//$NON-NLS-1$
   }
}
