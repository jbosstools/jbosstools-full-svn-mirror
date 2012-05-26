package org.jboss.tools.vpe.resref.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.common.resref.core.ResourceReference;

public abstract class ReferenceWizardPage extends WizardPage 
implements SelectionListener, Listener {

	protected final String BROWSE_BUTTON_NAME = "&Browse...";//$NON-NLS-1$
	protected ResourceReference resref = null;
	protected Object fileLocation = null;
	protected ResourceReferenceValidator validator = null;
	
	private Button pageRadioButton;
	private Button folderRadioButton;
	private Button projectRadioButton;
	private int scope = ResourceReference.FOLDER_SCOPE;
	
	public ReferenceWizardPage(String pageName, String title,
			ImageDescriptor titleImage, Object fileLocation) {
		super(pageName, title, titleImage);
		this.fileLocation = fileLocation; 
		setPageComplete(false);
	}
	
	public static ImageDescriptor getImageDescriptor() {
		return ModelUIImages.getImageDescriptor(ModelUIImages.WIZARD_DEFAULT);
	}
	
	/**
	 * Creates a group of radio buttons to select the scope. 
	 * 
	 * @param parent the parent composite
	 * @return the group control
	 */
	protected Group createScopeGroup(Composite parent) {
		Group groupControl = new Group(parent, SWT.SHADOW_ETCHED_IN);
		groupControl.setText(Messages.SCOPE_GROUP_NAME);
		Layout layout = new GridLayout(1, false);
		groupControl.setLayout(layout);
		
		pageRadioButton = new Button(groupControl, SWT.RADIO);
		pageRadioButton.setText(Messages.SCOPE_PAGE);
		pageRadioButton.addSelectionListener(this);
		
		folderRadioButton = new Button(groupControl, SWT.RADIO);
		folderRadioButton.setText(Messages.SCOPE_FOLDER);
		folderRadioButton.addSelectionListener(this);
		
		projectRadioButton = new Button(groupControl, SWT.RADIO);
		projectRadioButton.setText(Messages.SCOPE_PROJECT);
		projectRadioButton.addSelectionListener(this);
		
		setScopeRadioButtonSelection();
		
		return groupControl;
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		
	}

	public void widgetSelected(SelectionEvent e) {
		validatePage();
	}

	public void handleEvent(Event event) {
		validatePage();
	}
	
	protected int getSelectedScope() {
		int scope = ResourceReference.FILE_SCOPE;
		if ((null != pageRadioButton) && pageRadioButton.getSelection()) {
			scope = ResourceReference.FILE_SCOPE;
		} else if ((null != folderRadioButton) && folderRadioButton.getSelection()) {
			scope = ResourceReference.FOLDER_SCOPE;
		} else if ((null != projectRadioButton) && projectRadioButton.getSelection()) {
			scope = ResourceReference.PROJECT_SCOPE;
		}
		return scope;
	}
	
	protected void setScope(int scope) {
		this.scope = scope;
	}
	
	private void setScopeRadioButtonSelection() {
		switch (scope) {
		case ResourceReference.FILE_SCOPE:
			pageRadioButton.setSelection(true);
			break;
		case ResourceReference.FOLDER_SCOPE:
			folderRadioButton.setSelection(true);
			break;
		case ResourceReference.PROJECT_SCOPE:
			projectRadioButton.setSelection(true);
			break;
		default:
			folderRadioButton.setSelection(true);
			break;
		}
	}
	
	protected void validatePage() {
		validator = getUpdatedValidator();
		setPageComplete(validator.validate());
		setErrorMessage(validator.getErrorMessage());
	}
	
	public ResourceReference getResref() {
		return resref;
	}

	public void setResref(ResourceReference resref) {
		this.resref = resref;
	}
	
	abstract protected String getLocation(); 	
	abstract protected String getProperties(); 	
	abstract protected void setLocation(String location); 	
	abstract protected void setProperties(String properties); 	
	abstract protected ResourceReferenceValidator getUpdatedValidator(); 	

}