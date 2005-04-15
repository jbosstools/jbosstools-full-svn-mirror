/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.ui.wizards.ejb.methods;

import org.jboss.ide.eclipse.jdt.j2ee.ui.JDTJ2EEUIMessages;
import org.jboss.ide.eclipse.jdt.ui.wizards.MethodWizard;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class EJBMethodWizard extends MethodWizard
{
   /**Constructor for the NewBusinessMethodWizard object */
   public EJBMethodWizard()
   {
      this.setWindowTitle(JDTJ2EEUIMessages.getString("EJBMethodWizard.window.title"));//$NON-NLS-1$
   }
}
