package org.jboss.ide.eclipse.archives.ui.wizards.pages;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jboss.ide.eclipse.archives.ui.PackagesSharedImages;
import org.jboss.ide.eclipse.archives.ui.providers.PackagesContentProvider;
import org.jboss.ide.eclipse.archives.ui.providers.PackagesLabelProvider;
import org.jboss.ide.eclipse.archives.ui.wizards.AbstractPackageWizard;
import org.jboss.ide.eclipse.archives.ui.wizards.WizardPageWithNotification;

public class DefaultJARConfigWizardPage extends WizardPageWithNotification {

	private AbstractPackageWizard wizard;
	
	public DefaultJARConfigWizardPage (AbstractPackageWizard wizard) {
		super ("Default JAR Configuration", "Default JAR Configuration",
				PackagesSharedImages.getImageDescriptor(PackagesSharedImages.IMG_NEW_JAR_WIZARD));
		
		this.wizard = wizard;
	}
	
	public void createControl(Composite parent) {

		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		Button createDefaultConfig = new Button(main, SWT.CHECK);
		createDefaultConfig.setText("Use default JAR configuration");
		
		new Label(main, SWT.NONE).setText("Preview");
		
		TreeViewer previewTree = new TreeViewer(main);
		previewTree.setContentProvider(new PackagesContentProvider());
		previewTree.setLabelProvider(new PackagesLabelProvider());
		previewTree.setInput(wizard.getPackage());
		
		setControl(main);
	}

}
