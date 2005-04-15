/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistMessages;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistPlugin;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class TemplatesPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
   TemplateListViewer templateListViewer;


   /**Constructor for the TemplatesPreferencePage object */
   public TemplatesPreferencePage()
   {
      super();
      this.setDescription(XDocletAssistMessages.getString("TemplatesPreferencePage.XDoclet_Tags_templates_available_for_completion_1"));//$NON-NLS-1$
   }


   /**
    * @param workbench  Description of the Parameter
    * @see              org.eclipse.ui.IWorkbenchPreferencePage#init(IWorkbench)
    */
   public void init(IWorkbench workbench) { }


   /**
    * @return   Description of the Return Value
    * @see      org.eclipse.jface.preference.IPreferencePage#performOk()
    */
   public boolean performOk()
   {
      XDocletAssistPlugin.getDefault().saveTemplateList();
      return true;
   }


   /**
    * @param parent  Description of the Parameter
    * @return        Description of the Return Value
    * @see           org.eclipse.jface.preference.PreferencePage#createContents(Composite)
    */
   protected Control createContents(Composite parent)
   {
      Composite topComp = new Composite(parent, SWT.NONE);
      topComp.setLayout(new FillLayout());
      templateListViewer =
            new TemplateListViewer(
            XDocletAssistPlugin.getDefault().getTemplateList(),
            topComp,
            SWT.BORDER);
      return topComp;
   }


   /**
    * Create some empty space.
    *
    * @param comp     Description of the Parameter
    * @param colSpan  Description of the Parameter
    */
   protected void createVerticalSpacer(Composite comp, int colSpan)
   {
      Label label = new Label(comp, SWT.NONE);
      GridData gd = new GridData();
      gd.horizontalSpan = colSpan;
      label.setLayoutData(gd);
   }


   /**
    * @see   org.eclipse.jface.preference.PreferencePage#performApply()
    */
   protected void performApply()
   {
      super.performApply();
      templateListViewer.getViewer().refresh();
   }


   /**
    * @see   org.eclipse.jface.preference.PreferencePage#performDefaults()
    */
   protected void performDefaults()
   {
      templateListViewer.getViewer().setInput(XDocletAssistPlugin.getDefault().getAndSetRefreshedTemplateList());
      super.performDefaults();
   }
}
