/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.fields;

import org.jboss.ide.eclipse.jdt.j2ee.ui.actions.ejb.AddEJBFragmentAction;
import org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.fields.NewCMRRelationshipWizard;
import org.jboss.ide.eclipse.jdt.ui.wizards.ClassFragmentWizard;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class AddCMRRelationshipAction extends AddEJBFragmentAction
{
   /**Constructor for the AddCMRRelationshipAction object */
   public AddCMRRelationshipAction() { }


   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   protected ClassFragmentWizard createFieldWizard()
   {
      return new NewCMRRelationshipWizard();
   }


   /**
    * Gets the requiredInterfaces attribute of the AddCMRRelationshipAction object
    *
    * @return   The requiredInterfaces value
    */
   protected String[] getRequiredInterfaces()
   {
      return new String[]{"javax.ejb.EntityBean"};//$NON-NLS-1$
   }
}
