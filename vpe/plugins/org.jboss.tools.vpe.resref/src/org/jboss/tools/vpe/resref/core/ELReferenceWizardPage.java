package org.jboss.tools.vpe.resref.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class ELReferenceWizardPage extends ReferenceWizardPage {

	private Text elName = null; 
	private Text elValue = null; 
	private String elNameStr = ""; //$NON-NLS-1$
	private String elValueStr = ""; //$NON-NLS-1$
	
	public ELReferenceWizardPage(String pageName, String title,
			ImageDescriptor titleImage, Object fileLocation) {
		super(pageName, title, titleImage, fileLocation);
	}

	public ELReferenceWizardPage(String pageName) {
		super(pageName);
	}

	public void createControl(Composite parent) {
		Composite pageControl = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		pageControl.setLayout(gridLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		pageControl.setLayoutData(gd);
		
		/*
		 * Create first row
		 * 1) Create label
		 */
		Label pathLabel = new Label(pageControl, SWT.RIGHT);
		pathLabel.setText(Messages.EL_NAME);
		gd = new GridData();
		pathLabel.setLayoutData(gd);
		
		/*
		 * 2) Create text field 
		 */
		elName = new Text(pageControl, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		elName.setLayoutData(gd);
		elName.setText(elNameStr);
		elName.addListener(SWT.Selection, this);
		elName.addListener(SWT.Modify, this);
		
		/*
		 * Create empty label
		 */
		Label emptyLabel1 = new Label(pageControl, SWT.NONE);
		
		/*
		 * Create scope group
		 */
		Control groupControl = createScopeGroup(pageControl);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		groupControl.setLayoutData(gd); 
		
		/*
		 * Create third row
		 */
		Label prefixLabel = new Label(pageControl, SWT.RIGHT);
		prefixLabel.setText(Messages.EL_VALUE);
		gd = new GridData();
		prefixLabel.setLayoutData(gd);
		elValue = new Text(pageControl, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		elValue.setLayoutData(gd);
		elValue.setText(elValueStr);
		
		/*
		 * Dialog's control should be initialized.
		 */
		setControl(pageControl);
	}

	@Override
	protected ResourceReferenceValidator getUpdatedValidator() {
		Map<String, String> fields = new HashMap<String, String>();
		fields.put(ELResourceReferenceValidator.EL_NAME, getElName());
		fields.put(ResourceReferenceValidator.SCOPE, 
				Integer.toString(this.getSelectedScope()));
		if (null == validator) {
			validator = new ELResourceReferenceValidator(fields,
					resref, ((ELReferenceWizard) getWizard()).getResrefList());
		} else {
			validator.setFields(fields);
		}
		return validator;
	}
	
	public String getElName() {
		if ((null != elName) && (null != elName.getText())) {
			elNameStr = elName.getText().trim();
		}
		return elNameStr;
	}
	
	public String getElValue() {
		if ((null != elValue) && (null != elValue.getText())) {
			elValueStr = elValue.getText().trim();
		}
		return elValueStr;
	}

	@Override
	protected String getLocation() {
		return getElName();
	}

	@Override
	protected String getProperties() {
		return getElValue();
	}
	
	@Override
	protected void setLocation(String location) {
		elNameStr = location;
	}

	@Override
	protected void setProperties(String properties) {
		elValueStr = properties;
	}
	
}
