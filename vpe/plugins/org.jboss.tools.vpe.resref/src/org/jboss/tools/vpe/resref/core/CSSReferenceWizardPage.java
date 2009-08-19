package org.jboss.tools.vpe.resref.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CSSReferenceWizardPage extends ReferenceWizardPage {

	private final String[] FILTER_EXTENSIONS = new String[] {"*.css"}; //$NON-NLS-1$
	private String browseDialogFilterPath = null;
	private Text cssName = null;
	private String cssNameStr = ""; //$NON-NLS-1$
	
	public CSSReferenceWizardPage(String pageName, String title,
			ImageDescriptor titleImage, Object fileLocation) {
		super(pageName, title, titleImage, fileLocation);
	}

	public CSSReferenceWizardPage(String pageName) {
		super(pageName);
	}

	public void createControl(Composite parent) {
		
		Composite pageControl = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(3, false);
		pageControl.setLayout(gridLayout);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		pageControl.setLayoutData(gd);
		
		/*
		 * Create first row
		 * 1) Create label
		 */
		Label pathLabel = new Label(pageControl, SWT.RIGHT);
		pathLabel.setText(Messages.CSS_FILE_PATH);
		gd = new GridData();
		pathLabel.setLayoutData(gd);
		
		/*
		 * 2) Create text field 
		 */
		cssName = new Text(pageControl, SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		cssName.setLayoutData(gd);
		cssName.setText(cssNameStr);

		/*
		 * 3) Create browse control.
		 */
		final Button button = new Button(pageControl, SWT.PUSH);
		button.setText(BROWSE_BUTTON_NAME);
		gd = new GridData();
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				FileDialog dialog = new FileDialog(button.getShell(), SWT.OPEN);
				/*
				 * Add filter path
				 */
				if ((null != browseDialogFilterPath) && (new File(browseDialogFilterPath).exists()) ){
					dialog.setFilterPath(browseDialogFilterPath);
				}
				dialog.setFilterExtensions(FILTER_EXTENSIONS);
				
				String newPath = dialog.open();
				if (newPath != null) {
					newPath = newPath.trim();
					browseDialogFilterPath = newPath; 
					cssName.setText(browseDialogFilterPath);
				}
			}
		});
		
		/*
		 * Create empty label
		 */
		Label emptyLabel1 = new Label(pageControl, SWT.NONE);
		/*
		 * Create scope group
		 */
		Group groupControl = createScopeGroup(pageControl);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1);
		groupControl.setLayoutData(gd); 
		
		cssName.addSelectionListener(this);
		cssName.addListener(SWT.Selection, this);
		cssName.addListener(SWT.Modify, this);

		/*
		 * Dialog's control should be initialized.
		 */
		setControl(pageControl);
		/*
		 * Validate page right after creation.
		 */
		validatePage();
		
	}

	@Override
	protected ResourceReferenceValidator getUpdatedValidator() {
		Map<String, String> fields = new HashMap<String, String>();
		if ((null != cssName) && (null != cssName.getText())){
			fields.put(CSSResourceReferenceValidator.CSS_FILE_PATH, cssName.getText().trim());
		}
		if (null == validator) {
			validator = new CSSResourceReferenceValidator(fields);
		} else {
			validator.setFields(fields);
		}
		return validator;
	}

	public String getCssName() {
		if ((null != cssName) && (null != cssName.getText())) {
			cssNameStr = cssName.getText().trim();
		}
		return cssNameStr;
	}
	
	@Override
	protected String getLocation() {
		return getCssName();
	}

	@Override
	protected String getProperties() {
		return ""; //$NON-NLS-1$
	}	
	
	@Override
	protected void setLocation(String location) {
		cssNameStr = location;
	}

	@Override
	protected void setProperties(String properties) {

	}
	
}
