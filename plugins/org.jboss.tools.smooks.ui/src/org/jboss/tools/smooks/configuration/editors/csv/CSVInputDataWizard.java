/**
 * 
 */
package org.jboss.tools.smooks.configuration.editors.csv;

import java.util.Properties;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;

/**
 * @author Dart
 *
 */
public class CSVInputDataWizard extends Wizard implements IStructuredDataSelectionWizard, INewWizard {
	
	private SmooksResourceListType resourceList ;
	
	private EditingDomain editingDomain;
	
	private CSVDataConfigurationWizardPage configPage;
	
	private CSVDataPathWizardPage pathPage;
	
	
	public CSVInputDataWizard() {
		super();
		this.setWindowTitle("CSV Input Data Wizard");
	}
	
	

	@Override
	public void addPages() {
		if(pathPage == null){
			pathPage = new CSVDataPathWizardPage("CSV Path Page", new String[]{});
		}
		if(configPage == null){
			configPage = new CSVDataConfigurationWizardPage("CSV Configurations Page");
			configPage.setSmooksResourceList(resourceList);
		}
		this.addPage(pathPage);
		this.addPage(configPage);
		super.addPages();
	}



	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if(configPage != null){
			
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard#complate(org.jboss.tools.smooks.configuration.editors.SmooksMultiFormEditor)
	 */
	public void complate(SmooksMultiFormEditor formEditor) {

	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard#getInputDataTypeID()
	 */
	public String getInputDataTypeID() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard#getProperties()
	 */
	public Properties getProperties() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard#getReturnData()
	 */
	public Object getReturnData() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard#getStructuredDataSourcePath()
	 */
	public String getStructuredDataSourcePath() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) {
		IEditorPart editorPart = site.getWorkbenchWindow().getActivePage().findEditor(input);
		if (editorPart != null && editorPart instanceof SmooksMultiFormEditor) {
			SmooksMultiFormEditor formEditor = (SmooksMultiFormEditor) editorPart;
			Object smooksModel = formEditor.getSmooksModel();
			if (smooksModel instanceof DocumentRoot) {
				resourceList = ((DocumentRoot) smooksModel).getSmooksResourceList();
			}
			editingDomain = formEditor.getEditingDomain();
		}
		if(configPage != null){
			configPage.setSmooksResourceList(resourceList);
		}

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

}
