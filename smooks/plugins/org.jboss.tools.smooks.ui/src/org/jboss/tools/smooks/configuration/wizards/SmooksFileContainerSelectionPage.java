package org.jboss.tools.smooks.configuration.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.jboss.tools.smooks.configuration.SmooksConstants;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 1, 2009
 */
public class SmooksFileContainerSelectionPage extends WizardNewFileCreationPage {

	String version = SmooksConstants.VERSION_1_1;

	public SmooksFileContainerSelectionPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle("Smooks Configuration File Wizard Page");
		setDescription("Create a new Smooks configuration file.");
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		Composite buttonComposite = new Composite((Composite) getControl(), SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		buttonComposite.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		buttonComposite.setLayout(layout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		Label speator = new Label(buttonComposite, SWT.SEPARATOR|SWT.HORIZONTAL);
		gd.widthHint = 10;
		speator.setLayoutData(gd);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		Label label = new Label(buttonComposite, SWT.NONE);
		label.setText("Please select Smooks configuration file version");
		
		
		final CCombo combo = new CCombo(buttonComposite,SWT.BORDER);
		combo.setEditable(false);
		
		for(int i = 0 ; i < SmooksConstants.SMOOKS_VERSIONS.length ; i++){
			combo.add(SmooksConstants.SMOOKS_VERSIONS[i]);
		}
		
		int defaultIndex = 0 ;
		for(int i = 0 ; i < SmooksConstants.SMOOKS_VERSIONS.length ; i++){
			if(SmooksConstants.SMOOKS_VERSIONS[i].equals(version)){
				defaultIndex = i;
				break;
			}
		}
		combo.select(defaultIndex);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		combo.setLayoutData(gd);
		
		combo.addModifyListener(new ModifyListener(){

			public void modifyText(ModifyEvent e) {
				version = combo.getText();
			}
			
		});
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}