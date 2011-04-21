/*
 * JBoss-IDE, Eclipse plugins for JBoss
 *
 * Distributable under LGPL license.
 * See terms of license at www.gnu.org.
 */
package org.jboss.ide.eclipse.xdoclet.assist.ui;

import org.apache.oro.text.regex.Perl5Compiler;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.ide.eclipse.xdoclet.assist.IXDocletConstants;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistMessages;
import org.jboss.ide.eclipse.xdoclet.assist.XDocletAssistPlugin;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class VariablesPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
   Perl5Compiler compiler = new Perl5Compiler();
   /** Description of the Field */
   protected static int WIDTH = 20;


   /**Constructor for the VariablesPreferencePage object */
   public VariablesPreferencePage()
   {
      super(GRID);
      this.setPreferenceStore(XDocletAssistPlugin.getDefault().getPreferenceStore());
      this.setDescription(XDocletAssistMessages.getString("VariablesPreferencePage.Definition_of_the_variables_used_in_templates_1"));//$NON-NLS-1$
   }


   /**
    * @param workbench  Description of the Parameter
    * @see              org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
    */
   public void init(IWorkbench workbench) { }


   /**
    * @see   org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
    */
   protected void createFieldEditors()
   {
      addField(
            new PatternFieldEditor(
            IXDocletConstants.VARIABLE_CLASSNAME,
            XDocletAssistMessages.getString("VariablesPreferencePage.Classname__1"), //$NON-NLS-1$
      getFieldEditorParent()));
      addField(
            new PatternFieldEditor(
            IXDocletConstants.VARIABLE_CLASSNAME_WITHOUT_SUFFIX,
            XDocletAssistMessages.getString("VariablesPreferencePage.Classname_Subset__2"), //$NON-NLS-1$
      getFieldEditorParent()));
      addField(
            new PatternFieldEditor(
            IXDocletConstants.VARIABLE_PACKAGE,
            XDocletAssistMessages.getString("VariablesPreferencePage.Packagename__3"), //$NON-NLS-1$
      getFieldEditorParent()));
      addField(
            new PatternFieldEditor(
            IXDocletConstants.VARIABLE_PARENT_PACKAGE,
            XDocletAssistMessages.getString("VariablesPreferencePage.Parent_Packagename__4"), //$NON-NLS-1$
      getFieldEditorParent()));
   }

}
