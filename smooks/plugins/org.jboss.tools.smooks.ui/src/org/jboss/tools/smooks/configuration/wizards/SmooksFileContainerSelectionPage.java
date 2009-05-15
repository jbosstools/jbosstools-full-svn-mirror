package org.jboss.tools.smooks.configuration.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 1, 2009
 */
public class SmooksFileContainerSelectionPage extends WizardNewFileCreationPage {

	public SmooksFileContainerSelectionPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
		setTitle("Smooks Configuration File Wizard Page");
		setDescription("Create a new Smooks configuration file.");
		setFileExtension("xml");
		setFileName("smooks-config.xml");
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		validatePage();
	}
}