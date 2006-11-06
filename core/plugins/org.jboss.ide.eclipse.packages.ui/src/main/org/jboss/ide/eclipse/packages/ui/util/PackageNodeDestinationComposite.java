package org.jboss.ide.eclipse.packages.ui.util;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;
import org.jboss.ide.eclipse.packages.ui.dialogs.PackageNodeDestinationDialog;

public class PackageNodeDestinationComposite extends Composite {

	protected Composite parent;
	protected Label destinationImage;
	protected Label destinationText;
	protected Button destinationBrowseButton;
	protected Object packageDestination;
	protected boolean editable;
	
	public PackageNodeDestinationComposite(Composite parent, int style, Object destination)
	{
		super(parent, style);
		this.parent = parent;
		this.packageDestination = destination;
		this.editable = true;
		createComposite();
	}
	
	protected void createComposite()
	{
		setLayout(new GridLayout(2, false));
		setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		destinationImage = new Label(this, SWT.NONE);
		destinationText = new Label(this, SWT.NONE);
		updateDestinationViewer();
		
		destinationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		createBrowseButton(parent);
	}
	
	protected void createBrowseButton (Composite parent)
	{
		destinationBrowseButton = new Button(parent, SWT.PUSH); 
		destinationBrowseButton.setText(PackagesUIMessages.PackageNodeDestinationComposite_destinationBrowseButton_label);
		
		destinationBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				openDestinationDialog();
			}
		});
		destinationBrowseButton.setEnabled(editable);
	}
	
	protected void openDestinationDialog ()
	{
		PackageNodeDestinationDialog dialog = new PackageNodeDestinationDialog(getShell(), packageDestination, true, true);
		if (packageDestination != null)
			dialog.setInitialSelection(packageDestination);
		
		int response = dialog.open();
		if (response == Dialog.OK)
		{
			Object object = dialog.getResult()[0];
			packageDestination = object;
			
			updateDestinationViewer();
		}
	}
	
	protected void updateDestinationViewer ()
	{
		if (packageDestination == null) return;
		
		if (packageDestination instanceof IPackage)
		{
			IPackage pkg = (IPackage) packageDestination;
			
			if (pkg.isExploded()) {
				destinationImage.setImage(PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_PACKAGE_EXPLODED));
			} else {
				destinationImage.setImage(PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_PACKAGE));
			}
			destinationText.setText(pkg.getName());
		}
		else if (packageDestination instanceof IPackageFolder)
		{
			IPackageFolder folder = (IPackageFolder) packageDestination;
			destinationImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
			destinationText.setText(folder.getPackageRelativePath().toString());
		}
		else if (packageDestination instanceof IProject)
		{
			IProject project = (IProject) packageDestination;
			destinationImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT));
			destinationText.setText(project.getName());
		}
		else if (packageDestination instanceof IFolder)
		{
			IFolder folder = (IFolder) packageDestination;
			destinationImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
			destinationText.setText(folder.getProjectRelativePath().toString());
		}
	}
	
	public Object getPackageNodeDestination ()
	{
		return packageDestination;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		if (destinationBrowseButton != null)
		{
			destinationBrowseButton.setEnabled(editable);
		}
	}
}
