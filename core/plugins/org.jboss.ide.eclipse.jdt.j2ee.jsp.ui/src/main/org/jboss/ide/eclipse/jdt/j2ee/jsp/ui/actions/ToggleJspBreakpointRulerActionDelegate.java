/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.jdt.j2ee.jsp.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.source.IVerticalRulerInfo;
import org.eclipse.ui.texteditor.AbstractRulerActionDelegate;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Action delegate that create the ToggleJspBreakpointAction when required.
 *
 * @author    Laurent Etiemble
 * @version   $Revision$
 */
public class ToggleJspBreakpointRulerActionDelegate extends AbstractRulerActionDelegate
{
   /**
    * Description of the Method
    *
    * @param editor     Description of the Parameter
    * @param rulerInfo  Description of the Parameter
    * @return           Description of the Return Value
    */
   protected IAction createAction(ITextEditor editor, IVerticalRulerInfo rulerInfo)
   {
      return new ToggleJspBreakpointAction(editor, rulerInfo);
   }
}
