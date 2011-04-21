/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.ui.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/*
 * This file contains materials derived from the
 * Solareclipse project. License can be found at :
 * http://solareclipse.sourceforge.net/legal/cpl-v10.html
 */
/**
 * Empty preference page (could be used as a preferences category).
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class EmptyPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
   /**
    * Description of the Method
    *
    * @param workbench  Description of the Parameter
    */
   public void init(IWorkbench workbench) { }


   /**
    * Description of the Method
    *
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    */
   protected Control createContents(Composite parent)
   {
      return new Composite(parent, SWT.NULL);
   }
}
