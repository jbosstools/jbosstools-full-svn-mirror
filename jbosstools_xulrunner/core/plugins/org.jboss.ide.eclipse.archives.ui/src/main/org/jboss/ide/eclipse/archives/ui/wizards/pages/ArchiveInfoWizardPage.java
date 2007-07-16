package org.jboss.ide.eclipse.archives.ui.wizards.pages;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
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
import org.jboss.ide.eclipse.archives.core.model.ArchiveNodeFactory;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;
import org.jboss.ide.eclipse.archives.ui.util.DestinationChangeListener;
import org.jboss.ide.eclipse.archives.ui.util.composites.ArchiveDestinationComposite;
import org.jboss.ide.eclipse.archives.ui.wizards.AbstractArchiveWizard;
import org.jboss.ide.eclipse.archives.ui.wizards.WizardPageWithNotification;
import org.jboss.ide.eclipse.archives.ui.wizards.WizardWithNotification;

public class ArchiveInfoWizardPage extends WizardPageWithNotification {

	private AbstractArchiveWizard wizard;
	private Text packageNameText;
	private Button compressedButton;
	private Button explodedButton;
	private String packageName;
	private boolean packageExploded;
	private ArchiveDestinationComposite destinationComposite;
	private IArchive archive;
	
	public ArchiveInfoWizardPage (AbstractArchiveWizard wizard, IArchive existingPackage) {
		super (ArchivesUIMessages.PackageInfoWizardPage_title, ArchivesUIMessages.PackageInfoWizardPage_title, wizard.getImageDescriptor());
		setWizard(wizard);
		this.archive = existingPackage;
	}
	
	public void createControl(Composite parent) {
		setMessage(ArchivesUIMessages.PackageInfoWizardPage_message);		
		
		Composite main = new Composite(parent, SWT.NONE);
		main.setLayout(new GridLayout(1, false));
		
		Group infoGroup = new Group(main, SWT.NONE);
		infoGroup.setLayout(new GridLayout(3, false));
		infoGroup.setText(ArchivesUIMessages.PackageInfoWizardPage_infoGroup_label);
		expand(infoGroup);
		
		new Label(infoGroup, SWT.NONE).setText(ArchivesUIMessages.PackageInfoWizardPage_packageName_label);
		Composite pkgNameComposite = new Composite(infoGroup, SWT.NONE);
		GridLayout pkgNameLayout = new GridLayout(2, false);
		pkgNameLayout.marginHeight = 0;
		pkgNameLayout.marginWidth = 0;
		pkgNameComposite.setLayout(pkgNameLayout);
		pkgNameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		new Label(pkgNameComposite, SWT.NONE).setImage(ArchivesSharedImages.getImage(ArchivesSharedImages.IMG_PACKAGE));
		
		packageNameText = new Text(pkgNameComposite, SWT.BORDER);
		packageName = wizard.getProject().getName() + "." + wizard.getArchiveExtension();
		packageNameText.setText(packageName);
		packageNameText.setSelection(0, wizard.getProject().getName().length());
		expand(packageNameText);
		
		GridData pkgNameData = new GridData(GridData.FILL_HORIZONTAL);
		pkgNameData.horizontalSpan = 2;
		pkgNameComposite.setLayoutData(pkgNameData);
		
		packageNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (validate())
				{
					packageName = packageNameText.getText();
				}
			}
		});

		new Label(infoGroup, SWT.NONE).setText(ArchivesUIMessages.PackageInfoWizardPage_destination_label);

		GridData destinationTextData = new GridData(GridData.FILL_BOTH);
		destinationTextData.horizontalSpan = 2;
		GridData buttonData = new GridData(GridData.FILL_HORIZONTAL);
		buttonData.horizontalSpan = 3;
		buttonData.horizontalAlignment = SWT.END;
		
		Object destination = wizard.getSelectedDestination();
		destinationComposite = new ArchiveDestinationComposite(
			infoGroup, SWT.NONE, destination);
		destinationComposite.addDestinationChangeListener(new DestinationChangeListener () {
			public void destinationChanged(Object newDestination) {
				validate();
			}
		});
		destinationComposite.setLayoutData(destinationTextData);
		
		Group packageTypeGroup = new Group(main, SWT.NONE);
		packageTypeGroup.setLayout(new GridLayout(1, false));
		packageTypeGroup.setText(ArchivesUIMessages.PackageInfoWizardPage_packageTypeGroup_label);
		expand(packageTypeGroup);
		
		packageExploded = false;
		compressedButton = new Button(packageTypeGroup, SWT.RADIO);
		compressedButton.setText(ArchivesUIMessages.PackageInfoWizardPage_compressedButton_label);
		compressedButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				packageExploded = false;
			}
		});
		compressedButton.setSelection(true);
		explodedButton = new Button(packageTypeGroup, SWT.RADIO);
		explodedButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				packageExploded = true;
			}
		});
		explodedButton.setText(ArchivesUIMessages.PackageInfoWizardPage_explodedButton_label);
		setControl(main);
		
		fillDefaults();
		validate();
	}
	
	private void fillDefaults ()
	{
		if (archive != null)
		{
			compressedButton.setSelection(!archive.isExploded());
			explodedButton.setSelection(archive.isExploded());
			packageNameText.setText(archive.getName());
			packageName = archive.getName();
			
			if (archive.isTopLevel()) {
				
				// TODO:  FIX THIS
				IContainer container = ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(archive.getGlobalDestinationPath());
				if( container != null )
					destinationComposite.setPackageNodeDestination(container);
				else 
					destinationComposite.setPackageNodeDestination(archive.getGlobalDestinationPath());
			} else {
				destinationComposite.setPackageNodeDestination(archive.getParent());
			}
			
			if (archive.isExploded()) {
				explodedButton.setEnabled(true);
			} else {
				compressedButton.setEnabled(true);
			}
		}
	}

	private boolean validate ()
	{
		if (packageNameText.getText() == null || packageNameText.getText().length() == 0)
		{
			setErrorMessage(ArchivesUIMessages.PackageInfoWizardPage_error_noPackageName);
			setPageComplete(false);
			return false;
		}
		else {
			setErrorMessage(null);
		}
		
		Object destination = getPackageDestination();
		if (destination instanceof IArchiveNode)
		{
			IArchiveNode parentNode = (IArchiveNode) destination;
			
			// verify no child has the same name
			IArchiveNode subPackages[] = parentNode.getChildren(IArchiveNode.TYPE_ARCHIVE);
			for (int i = 0; i < subPackages.length; i++)
			{
				IArchive subPackage = (IArchive) subPackages[i];
				if (subPackage.getName().equals(packageNameText.getText())
					&& (!archive.equals(this.archive)))
				{
					setErrorMessage(
						ArchivesUIMessages.bind(
							ArchivesUIMessages.PackageInfoWizardPage_error_packageAlreadyExists, packageNameText.getText()));
					setPageComplete(false);
					return false;
				}
			}
		} else if (destination instanceof IContainer) {
			IContainer container = (IContainer) destination;
			IArchive[] packages = ArchivesModel.instance().getProjectArchives(wizard.getProject().getLocation());
			if (packages != null) {
				for( int i = 0; i < packages.length; i++ ) {
					IArchive pkg = (IArchive) packages[i];
					if (pkg.getName().equals(packageNameText.getText())
						&& (pkg.getGlobalDestinationPath() != null && pkg.getGlobalDestinationPath().equals(container.getFullPath()))
						&& (!pkg.equals(this.archive)))
					{
						setErrorMessage(
								ArchivesUIMessages.bind(
									ArchivesUIMessages.PackageInfoWizardPage_error_packageAlreadyExists, packageNameText.getText()));
							setPageComplete(false);
							return false;
					}
				}
			}
		} else if (destination instanceof IPath) {
			IPath path = (IPath) destination;
			IArchive[] packages = ArchivesModel.instance().getProjectArchives(wizard.getProject().getLocation());
			if (packages != null) {
				for( int i = 0; i < packages.length; i++ ) {
					IArchive pkg = (IArchive) packages[i];
					if (pkg.getName().equals(packageNameText.getText())
						&& (pkg.getGlobalDestinationPath() != null && pkg.getGlobalDestinationPath().equals(path))
						&& (!pkg.equals(this.archive)))
					{
						setErrorMessage(
								ArchivesUIMessages.bind(
									ArchivesUIMessages.PackageInfoWizardPage_error_packageAlreadyExists, packageNameText.getText()));
							setPageComplete(false);
							return false;
					}
				}
			} else if (destination == null) {
				setErrorMessage(ArchivesUIMessages.PackageInfoWizardPage_error_noDestination);
				setPageComplete(false);
				return false;
			}
		}
		
		setPageComplete(true);
		return true;
	}
	
	
	public void pageExited(int button) {
		if (button == WizardWithNotification.NEXT || button == WizardWithNotification.FINISH) {
			createPackage();
		}
	}
	
	private void createPackage () {
		Object destContainer = getPackageDestination();
		
		
		if (archive == null) {
			archive = ArchiveNodeFactory.createArchive();
		}
		
		archive.setName(getPackageName());
		archive.setExploded(isPackageExploded());
		
		if (destContainer instanceof IContainer) {
			archive.setDestinationPath(((IContainer)destContainer).getFullPath());
			archive.setInWorkspace(true);
		} else if (destContainer instanceof IPath) {
			archive.setDestinationPath((IPath) destContainer);
			archive.setInWorkspace(false);
		}
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

	public Object getPackageDestination() {
		return destinationComposite.getPackageNodeDestination();
	}
	
	private void setWizard(AbstractArchiveWizard wizard)
	{
		this.wizard = wizard;
	}
	
	public IArchive getArchive ()
	{
		return archive;
	}
}
