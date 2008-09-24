/**
 * 
 */
package org.jboss.tools.smooks.xml;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.jboss.tools.smooks.ui.IStrucutredDataCreationWizard;

/**
 * @author Dart
 * 
 */
public abstract class AbstractStructuredDdataWizard extends Wizard implements
		IStrucutredDataCreationWizard {
	protected AbstractFileSelectionWizardPage page = null;
	protected Object xsdElement  = null;
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

}
