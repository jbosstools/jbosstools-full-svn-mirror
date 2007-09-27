/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.methods;

import org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.AddEJBFragmentAction;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods.NewHomeMethodWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassFragmentWizard;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AddHomeMethodAction extends AddEJBFragmentAction
{
   /**Constructor for the AddHomeMethodAction object */
   public AddHomeMethodAction() { }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected ClassFragmentWizard createFieldWizard()
   {
      return new NewHomeMethodWizard();
   }


   /**
    * Gets the requiredInterfaces attribute of the AddHomeMethodAction object
    *
    * @return   The requiredInterfaces value
    */
   protected String[] getRequiredInterfaces()
   {
      return new String[]{"javax.ejb.EntityBean", "javax.ejb.SessionBean"};//$NON-NLS-1$ //$NON-NLS-2$
   }
}
