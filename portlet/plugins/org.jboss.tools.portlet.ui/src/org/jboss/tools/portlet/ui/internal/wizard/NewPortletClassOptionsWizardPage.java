package org.jboss.tools.portlet.ui.internal.wizard;

import static org.eclipse.wst.common.componentcore.internal.operation.IArtifactEditOperationDataModelProperties.PROJECT_NAME;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jem.util.emf.workbench.ProjectUtilities;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jst.j2ee.internal.web.operations.INewServletClassDataModelProperties;
import org.eclipse.jst.servlet.ui.internal.wizard.NewWebClassOptionsWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties;
import org.jboss.tools.portlet.ui.PortletUIActivator;

public class NewPortletClassOptionsWizardPage extends
		NewWebClassOptionsWizardPage implements ISelectionChangedListener {
	
	protected Button initButton;
	protected Button destroyButton;
	protected Button getConfigButton;
	protected Button doViewButton;
	protected Button doEditButton;
	protected Button doHelpButton;
	protected Button doDispatchButton;
	protected Button processActionButton;
	protected Button renderButton;
	
	public NewPortletClassOptionsWizardPage(IDataModel model, String pageName, String pageDesc, String pageTitle) {
		super(model, pageName, pageDesc, pageTitle);
	}
	
	/**
	 * Create the composite with all the stubs
	 */
	@Override
	protected void createStubsComposite(Composite parent) {
		super.createStubsComposite(parent);
		
		inheritButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				boolean enable = inheritButton.getSelection();
				enablePortletButtons(enable);
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				//Do nothing
			}
			
			private void enablePortletButtons(boolean enable) {
				initButton.setEnabled(enable);
				destroyButton.setEnabled(enable);
				getConfigButton.setEnabled(enable);
				doDispatchButton.setEnabled(enable);
				doEditButton.setEnabled(enable);
				doViewButton.setEnabled(enable);
				doHelpButton.setEnabled(enable);
				processActionButton.setEnabled(enable);
				renderButton.setEnabled(enable);
			}
			
		});
		
		Composite comp = new Composite(methodStubs, SWT.NULL);
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.makeColumnsEqualWidth = true;
		comp.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(data);
		
		initButton = new Button(comp, SWT.CHECK);
		initButton.setText("&init"); //$NON-NLS-1$
		synchHelper.synchCheckbox(initButton, INewServletClassDataModelProperties.INIT, null);

		destroyButton = new Button(comp, SWT.CHECK);
		destroyButton.setText("destro&y"); //$NON-NLS-1$
		synchHelper.synchCheckbox(destroyButton, INewServletClassDataModelProperties.DESTROY, null);

		getConfigButton = new Button(comp, SWT.CHECK);
		getConfigButton.setText("getPortlet&Config"); //$NON-NLS-1$
		synchHelper.synchCheckbox(getConfigButton, INewPortletClassDataModelProperties.GET_PORTLET_CONFIG, null);

		doViewButton = new Button(comp, SWT.CHECK);
		doViewButton.setText("doView"); //$NON-NLS-1$
		synchHelper.synchCheckbox(doViewButton, INewPortletClassDataModelProperties.DO_VIEW, null);

		doEditButton = new Button(comp, SWT.CHECK);
		doEditButton.setText("doEdit"); //$NON-NLS-1$
		synchHelper.synchCheckbox(doEditButton, INewPortletClassDataModelProperties.DO_EDIT, null);

		doHelpButton = new Button(comp, SWT.CHECK);
		doHelpButton.setText("doHelp"); //$NON-NLS-1$
		synchHelper.synchCheckbox(doHelpButton, INewPortletClassDataModelProperties.DO_HELP, null);
		
		doDispatchButton = new Button(comp, SWT.CHECK);
		doDispatchButton.setText("doDispatch"); //$NON-NLS-1$
		synchHelper.synchCheckbox(doDispatchButton, INewPortletClassDataModelProperties.DO_DISPATCH, null);

		processActionButton = new Button(comp, SWT.CHECK);
		processActionButton.setText("processAction"); //$NON-NLS-1$
		synchHelper.synchCheckbox(processActionButton, INewPortletClassDataModelProperties.PROCESS_ACTION, null);
		
		renderButton = new Button(comp, SWT.CHECK);
		renderButton.setText("render"); //$NON-NLS-1$
		synchHelper.synchCheckbox(renderButton, INewPortletClassDataModelProperties.RENDER, null);

		interfaceViewer.addSelectionChangedListener(this);
		
	    Dialog.applyDialogFont(parent);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		removeButton.setEnabled(canRemoveSelectedInterfaces(selection));
	}
	
	protected KeyListener getInterfaceKeyListener() {
		return new KeyListener() {

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					IStructuredSelection selection = (IStructuredSelection) interfaceViewer.getSelection();
					if (canRemoveSelectedInterfaces(selection)) {
						handleInterfaceRemoveButtonSelected();
					}
				}
			}
			
		};
	}
	
	private boolean canRemoveSelectedInterfaces(IStructuredSelection selection) {
		return true;
	}
	
	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete() && PortletUIActivator.isPortletProject(model);
	}
	
}
