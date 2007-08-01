/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.methods;

import org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.AddEJBFragmentAction;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods.NewCreateMethodWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassFragmentWizard;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AddCreateMethodAction extends AddEJBFragmentAction
{
   /**Constructor for the AddCreateMethodAction object */
   public AddCreateMethodAction() { }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected ClassFragmentWizard createFieldWizard()
   {
      return new NewCreateMethodWizard();
   }


   /**
    * Gets the requiredInterfaces attribute of the AddCreateMethodAction object
    *
    * @return   The requiredInterfaces value
    */
   protected String[] getRequiredInterfaces()
   {
      return new String[]{"javax.ejb.EnterpriseBean"};//$NON-NLS-1$
   }
}
