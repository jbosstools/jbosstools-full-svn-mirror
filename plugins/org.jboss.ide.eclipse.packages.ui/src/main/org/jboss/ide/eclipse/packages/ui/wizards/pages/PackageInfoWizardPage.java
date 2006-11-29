package org.jboss.ide.eclipse.packages.ui.wizards.pages;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.util.PackageDestinationComposite;
import org.jboss.ide.eclipse.packages.ui.util.PackageNodeDestinationComposite;
import org.jboss.ide.eclipse.packages.ui.wizards.AbstractPackageWizard;

public class PackageInfoWizardPage extends WizardPage {

	private AbstractPackageWizard wizard;
	private Text packageNameText;
	private Button compressedButton;
	private Button explodedButton;
	private Button customManifestCheck;
	private Text manifestText;
	private Button manifestBrowseButton;
	private String packageName;
	private boolean packageExploded;
	private boolean manifestEnabled;
	private IFile manifestFile;
	private PackageDestinationComposite destinationComposite;

	public PackageInfoWizardPage (AbstractPackageWizard wizard)
	{
		super (PackagesUIMessages.PackageInfoWizardPage_title, PackagesUIMessages.PackageInfoWizardPage_title, wizard.getImageDescriptor());
		setWizard(wizard);
	}
	
	public void createControl(Composite parent) {
		setMessage(PackagesUIMessages.PackageInfoWizardPage_message);		
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		Group infoGroup = new Group(main, SWT.NONE);
		infoGroup.setLayout(new GridLayout(3, false));
		infoGroup.setText(PackagesUIMessages.PackageInfoWizardPage_infoGroup_label);
		expand(infoGroup);
		
		new Label(infoGroup, SWT.NONE).setText(PackagesUIMessages.PackageInfoWizardPage_packageName_label);
		packageNameText = new Text(infoGroup, SWT.BORDER);
		packageName = "Untitled." + wizard.getPackageExtension();
		packageNameText.setText(packageName);
		packageNameText.setSelection(0, "Untitled.".length()-1);
		
		expand(packageNameText);
		packageNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (validate())
				{
					packageName = packageNameText.getText();
				}
			}
		});
		new Label(infoGroup, SWT.NONE);

		new Label(infoGroup, SWT.NONE).setText(PackagesUIMessages.PackageInfoWizardPage_destination_label);
		
		Object destination = wizard.getSelectedDestination();
		destinationComposite = new PackageDestinationComposite(infoGroup, SWT.NONE, destination);
		
		customManifestCheck = new Button(infoGroup , SWT.CHECK);
		customManifestCheck.setText(PackagesUIMessages.PackageInfoWizardPage_customManifest_label);
		customManifestCheck.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				boolean checked = customManifestCheck.getSelection();
				manifestBrowseButton.setEnabled(checked);
				manifestEnabled = checked;
				validate();
			}
		});
		
		manifestText = new Text(infoGroup, SWT.BORDER);
		expand(manifestText);
		manifestText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (validate())
				{
					manifestFile = wizard.getProject().getFile(manifestText.getText()); 
				}
			}
		});
		
		manifestBrowseButton = new Button(infoGroup, SWT.PUSH);
		manifestBrowseButton.setText(PackagesUIMessages.PackageInfoWizardPage_manifestBrowseButton_label);
		manifestText.setEnabled(false);
		manifestBrowseButton.setEnabled(false);
		
		Group packageTypeGroup = new Group(main, SWT.NONE);
		packageTypeGroup.setLayout(new GridLayout(1, false));
		packageTypeGroup.setText(PackagesUIMessages.PackageInfoWizardPage_packageTypeGroup_label);
		expand(packageTypeGroup);
		
		packageExploded = false;
		compressedButton = new Button(packageTypeGroup, SWT.RADIO);
		compressedButton.setText(PackagesUIMessages.PackageInfoWizardPage_compressedButton_label);
		compressedButton.setSelection(true);
		compressedButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				packageExploded = false;
			}
		});
		explodedButton = new Button(packageTypeGroup, SWT.RADIO);
		explodedButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				packageExploded = true;
			}
		});
		explodedButton.setText(PackagesUIMessages.PackageInfoWizardPage_explodedButton_label);
		setControl(main);
	}

	private boolean validate ()
	{
		if (packageNameText.getText() == null || packageNameText.getText().length() == 0)
		{
			setErrorMessage(PackagesUIMessages.PackageInfoWizardPage_error_noPackageName);
			setPageComplete(false);
			return false;
		}
		else {
			setErrorMessage(null);
		}
		
		if (customManifestCheck.getSelection())
		{
			if (manifestText.getText() == null || manifestText.getText().length() == 0)
			{
				setErrorMessage(PackagesUIMessages.PackageInfoWizardPage_error_noManifest);
				setPageComplete(false);
				return false;
			}
			else {
				setErrorMessage(null);
			}
		}
		
		setPageComplete(true);
		return true;
	}
	
	private void expand(Control control)
	{
		control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	public String getPackageName() {
		return packageName;
	}

	public boolean isPackageExploded() {
		return packageExploded;
	}

	public boolean isManifestEnabled() {
		return manifestEnabled;
	}

	public IFile getManifestFile() {
		return manifestFile;
	}

	public Object getPackageDestination() {
		return destinationComposite.getPackageNodeDestination();
	}
	
	private void setWizard(AbstractPackageWizard wizard)
	{
		this.wizard = wizard;
	}
}
