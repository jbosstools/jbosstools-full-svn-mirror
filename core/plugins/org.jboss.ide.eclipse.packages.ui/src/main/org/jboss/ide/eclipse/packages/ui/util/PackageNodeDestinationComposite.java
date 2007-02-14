package org.jboss.ide.eclipse.packages.ui.util;

import java.util.ArrayList;
import java.util.Iterator;

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
	protected Object nodeDestination;
	protected boolean editable;
	protected ArrayList listeners;
	
	public PackageNodeDestinationComposite(Composite parent, int style, Object destination)
	{
		super(parent, style);
		this.parent = parent;
		this.nodeDestination = destination;
		this.editable = true;
		this.listeners = new ArrayList();
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
		PackageNodeDestinationDialog dialog = new PackageNodeDestinationDialog(getShell(), nodeDestination, true, true);
		if (nodeDestination != null)
			dialog.setInitialSelection(nodeDestination);
		
		int response = dialog.open();
		if (response == Dialog.OK)
		{
			Object object = dialog.getResult()[0];
			nodeDestination = object;
			
			updateDestinationViewer();
			fireDestinationChanged();
		}
	}
	
	public void setPackageNodeDestination (Object destination)
	{
		nodeDestination = destination;
		updateDestinationViewer();
	}
	
	protected void updateDestinationViewer ()
	{
		if (nodeDestination == null) return;
		
		if (nodeDestination instanceof IPackage)
		{
			IPackage pkg = (IPackage) nodeDestination;
			
			if (pkg.isExploded()) {
				destinationImage.setImage(PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_PACKAGE_EXPLODED));
			} else {
				destinationImage.setImage(PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_PACKAGE));
			}
			destinationText.setText(pkg.getName());
		}
		else if (nodeDestination instanceof IPackageFolder)
		{
			IPackageFolder folder = (IPackageFolder) nodeDestination;
			destinationImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
			destinationText.setText(folder.getPackageRelativePath().toString());
		}
		else if (nodeDestination instanceof IProject)
		{
			IProject project = (IProject) nodeDestination;
			destinationImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT));
			destinationText.setText(project.getName());
		}
		else if (nodeDestination instanceof IFolder)
		{
			IFolder folder = (IFolder) nodeDestination;
			destinationImage.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
			destinationText.setText(folder.getProjectRelativePath().toString());
		}
	}
	
	public Object getPackageNodeDestination ()
	{
		return nodeDestination;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
		if (destinationBrowseButton != null)
		{
			destinationBrowseButton.setEnabled(editable);
		}
	}
	
	public void addDestinationChangeListener (DestinationChangeListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeDestinationChangeListener (DestinationChangeListener listener)
	{
		listeners.remove(listener);
	}
	
	private void fireDestinationChanged ()
	{
		for (Iterator iter = listeners.iterator(); iter.hasNext(); )
		{
			DestinationChangeListener listener = (DestinationChangeListener) iter.next();
			listener.destinationChanged(nodeDestination);
		}
	}
}
