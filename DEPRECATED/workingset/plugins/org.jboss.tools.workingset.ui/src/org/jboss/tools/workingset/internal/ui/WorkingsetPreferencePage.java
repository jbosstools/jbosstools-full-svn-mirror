package org.jboss.tools.workingset.internal.ui;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jboss.tools.workingset.core.PreferenceConstants;
import org.jboss.tools.workingset.ui.Activator;

public class WorkingsetPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public WorkingsetPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Setup automatic grouping of projects into working sets that matches a regular expression.");
	}
	
	public void createFieldEditors() {
		
		addField(new ListEditor(PreferenceConstants.P_PATTERNS,"Grouping patterns", getFieldEditorParent()) {
						
			@Override
			protected String[] parseString(String stringList) {
				if(stringList.trim().length()>0) {
				return stringList.split(";"); 
				} else {
					return new String[0];
				}
			}
			
			@Override
			protected String getNewInputObject() {
				
				InputDialog dialog = new InputDialog(getShell(), "Add new pattern", "Syntax: <pattern>, <replace pattern>, <exclusive:true|false>", "org\\.jboss\\.tools\\.([^\\.]+).*,$1,true", new IInputValidator() {
					
					public String isValid(String newText) {
						String[] split = newText.split(",");
						if(split.length!=3) {
							return "Pattern does not consist 3 parts separated by commas";
						}
						try {
							Pattern.compile(split[0]);
						} catch(PatternSyntaxException pse) {
							return "Pattern does not compile: " + pse.getDescription();
						}						
						if(!split[2].equals("true") && !split[2].equals("false")) {
							return "Exclusive needs to be true or false";
						}
						
						return null;
					}
				});
								
				if(dialog.open()==InputDialog.OK) {
					return dialog.getValue();
				} else {
					return null;
				}
				
			}
			
			@Override
			protected String createList(String[] items) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < items.length; i++) {
					String string = items[i];
					sb.append(string);
					if(i<items.length) sb.append(";");
				}
				return sb.toString();
			}
		});
		
		addField(
				new BooleanFieldEditor(
					PreferenceConstants.P_ENABLE,
					"Automatic grouping of projects",
					getFieldEditorParent()));
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}