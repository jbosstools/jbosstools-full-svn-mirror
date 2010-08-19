/**
 * 
 */
package org.jboss.tools.smooks.configuration.editors.file;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jboss.tools.smooks.configuration.editors.xml.Messages;

/**
 * 
 * @author Dart Peng Date : 2008-8-13
 */
public abstract class AbstractFileSelectionWizardPage extends WizardPage {
	
	protected FileSelectionPageComponent fileSelectionPageComponent;
	
	protected IStructuredSelection selection;
	protected Object returnObject = null;
	protected Object[] initSelections;
	protected List<ViewerFilter> filters = new ArrayList<ViewerFilter>();
	protected boolean multiSelect = false;
	protected boolean reasourceLoaded = false;

	protected String[] fileExtensionNames;
	
	public AbstractFileSelectionWizardPage(String pageName, boolean multiSelect, Object[] initSelections, List<ViewerFilter> filters,String[] fileExtensionNames) {
		super(pageName);
		fileSelectionPageComponent = new FileSelectionPageComponent(getShell(), multiSelect, initSelections, filters, fileExtensionNames);
	}

	public AbstractFileSelectionWizardPage(String pageName, boolean multiSelect, Object[] initSelections, List<ViewerFilter> filters) {
		super(pageName);
		fileSelectionPageComponent = new FileSelectionPageComponent(getShell(), multiSelect, initSelections, filters);
	}

	public AbstractFileSelectionWizardPage(String pageName, String[] fileExtensionNames) {
		super(pageName);
		fileSelectionPageComponent = new FileSelectionPageComponent(getShell(), fileExtensionNames);
	}

	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);

		fileSelectionPageComponent.createControl(mainComposite);
		
		this.setControl(mainComposite);
		
		changeWizardPageStatus();
		// don't show the error message when first open the dialog
		this.setErrorMessage(null);
		parent.getShell().setText(Messages.AbstractFileSelectionWizardPage_WizardTitle);
		
		fileSelectionPageComponent.addFileSelectionListener(new FileSelectionListener() {
			public void fileSelected(String fileName) {
				changeWizardPageStatus();
			}
		});
	}

	protected void changeWizardPageStatus() {
		String text = getFilePath();
		String error = null;
		
		if (text == null || "".equals(text)) { //$NON-NLS-1$
			error = "File name cannot be null"; //$NON-NLS-1$
		}

		this.setErrorMessage(error);
		this.setPageComplete(error == null);
	}

	public String getFilePath() {
		return fileSelectionPageComponent.getFilePath();
	}

	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}

	public Object[] getInitSelections() {
		return initSelections;
	}

	public void setInitSelections(Object[] initSelections) {
		this.initSelections = initSelections;
	}

	public Object getReturnValue() {
		return null;
	}

	public void setFileExtensionNames(String[] fileExtensionNames) {
		fileSelectionPageComponent.setFileExtensionNames(fileExtensionNames);
	}
}
