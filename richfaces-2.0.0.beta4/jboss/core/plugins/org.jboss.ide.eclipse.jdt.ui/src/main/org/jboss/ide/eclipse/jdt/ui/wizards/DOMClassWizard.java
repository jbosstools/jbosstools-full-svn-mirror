/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards;

import org.jboss.ide.eclipse.jdt.core.wizards.generation.IGenerationEngine;

/**
 * Description of the Class
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public abstract class DOMClassWizard extends ClassWizard
{
   /**Constructor for the DOMClassWizard object */
   public DOMClassWizard() { }


   /** Adds a feature to the Pages attribute of the DOMClassWizard object */
   public void addPages()
   {
      super.addPages();
      this.setEngine((IGenerationEngine) page);
   }
}
