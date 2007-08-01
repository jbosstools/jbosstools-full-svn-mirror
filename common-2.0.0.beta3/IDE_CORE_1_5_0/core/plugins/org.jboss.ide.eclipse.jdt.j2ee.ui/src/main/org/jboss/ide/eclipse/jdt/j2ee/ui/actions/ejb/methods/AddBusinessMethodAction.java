/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.methods;

import org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.AddEJBFragmentAction;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods.NewBusinessMethodWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassFragmentWizard;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AddBusinessMethodAction extends AddEJBFragmentAction
{
   /**Constructor for the AddBusinessMethodAction object */
   public AddBusinessMethodAction() { }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected ClassFragmentWizard createFieldWizard()
   {
      return new NewBusinessMethodWizard();
   }


   /**
    * Gets the requiredInterfaces attribute of the AddBusinessMethodAction object
    *
    * @return   The requiredInterfaces value
    */
   protected String[] getRequiredInterfaces()
   {
      return new String[]{"javax.ejb.EntityBean", "javax.ejb.SessionBean"};//$NON-NLS-1$ //$NON-NLS-2$
   }
}
