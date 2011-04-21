/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistMessages;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistPlugin;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class XDocletPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
   /**Constructor for the XDocletPreferencePage object */
   public XDocletPreferencePage()
   {
      super();
      this.setDescription(XDocletAssistMessages.getString("XDocletPreferencePage.Allows_to_refresh_the_XTags_from_the_XDoclet_modules_1"));//$NON-NLS-1$
   }


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
      Composite top = new Composite(parent, SWT.NONE);
      top.setLayout(new GridLayout(1, true));

      Button button = new Button(top, SWT.CENTER);
      button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
      button.setText(XDocletAssistMessages.getString("XDocletPreferencePage.Refresh_XDoclet_Data_1"));//$NON-NLS-1$
      button.addSelectionListener(
         new SelectionAdapter()
         {
            public void widgetSelected(SelectionEvent e)
            {
               XDocletAssistPlugin.getDefault().getRefreshedDocletTree();
            }
         });

      return top;
   }
}
