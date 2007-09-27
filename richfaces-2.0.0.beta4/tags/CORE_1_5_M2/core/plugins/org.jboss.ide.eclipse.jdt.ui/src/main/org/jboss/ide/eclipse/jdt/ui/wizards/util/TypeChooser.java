/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.wizards.util;

import org.eclipse.jdt.core.IType;

/**
 * Description of the Interface
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public interface TypeChooser
{
   /**
    * Description of the Method
    *
    * @param filter  Description of the Parameter
    * @return        Description of the Return Value
    */
   public IType chooseType(String filter);
}
