/**
 * 
 */
package org.jboss.tools.smooks.xml;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.smooks.ui.IStructuredDataCreationWizard;

/**
 * @author Dart
 * 
 */
public abstract class AbstractStructuredDdataWizard extends Wizard implements
		IStructuredDataCreationWizard ,INewWizard{
	protected IWorkbench workbench;
	
	protected IStructuredSelection selection;
	
	protected AbstractFileSelectionWizardPage page = null;
	protected Object xsdElement  = null;
	protected IWizardNode strucutredDataCreationWizardNode;
	/**
	 * 
	 */
	public AbstractStructuredDdataWizard() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addPages() {
		if(page == null){
			page = createAbstractFileSelectionWizardPage();
			page.setSelection(this.selection);
		}
		this.addPage(page);
	}

	abstract protected AbstractFileSelectionWizardPage createAbstractFileSelectionWizardPage() ;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		xsdElement = this.page.getReturnValue();
		return true;
	}
	public Object getTreeViewerInputContents() {
		return xsdElement;
	}

	public void init(IEditorSite site, IEditorInput input) {
		
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}
	
	
}
