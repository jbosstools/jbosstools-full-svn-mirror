package org.jboss.tools.smooks.configuration.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.jboss.tools.smooks.configuration.SmooksConstants;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 1, 2009
 */
public class SmooksFileContainerSelectionPage extends WizardNewFileCreationPage {

	String version = SmooksConstants.VERSION_1_0;

	private Button smook10Button;
	private Button smook11Button;

	public SmooksFileContainerSelectionPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
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
//		Label speator = new Label(buttonComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
//		speator.setLayoutData(gd);
		Label label = new Label(buttonComposite, SWT.NONE);
		label.setText("Please select Smooks configuration file version");
		label.setLayoutData(gd);
		smook10Button = new Button(buttonComposite, SWT.RADIO);
		smook10Button.setText("Smooks 1.0");
		VersionButtonSelectionAdapter selectionAdapter = new VersionButtonSelectionAdapter();
		smook10Button.addSelectionListener(selectionAdapter);
		smook11Button = new Button(buttonComposite, SWT.RADIO);
		smook11Button.setText("Smooks 1.1.1");
		smook11Button.addSelectionListener(selectionAdapter);
		
		smook10Button.setSelection(true);
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	private class VersionButtonSelectionAdapter extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (smook10Button.getSelection()) {
				version = SmooksConstants.VERSION_1_0;
				return;
			}
			if (smook11Button.getSelection()) {
				version = SmooksConstants.VERSION_1_1_1;
				return;
			}
		}
	}
}