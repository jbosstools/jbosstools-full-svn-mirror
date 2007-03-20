package org.jboss.ide.eclipse.packages.ui.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
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
	protected Text destinationText;
	protected Button destinationBrowseButton;
	protected Object nodeDestination;
	protected boolean editable;
	protected ArrayList listeners;
	protected GridData textLayoutData, buttonLayoutData;
	
	public PackageNodeDestinationComposite(Composite parent, int style, Object destination)
	{
		this (parent, style, null, null, destination);
	}
	
	public PackageNodeDestinationComposite(Composite parent, int style, GridData textLayoutData, GridData buttonLayoutData, Object destination)
	{
		super(parent, style);
		this.parent = parent;
		this.nodeDestination = destination;
		this.editable = true;
		this.listeners = new ArrayList();
		this.textLayoutData = textLayoutData;
		this.buttonLayoutData = buttonLayoutData; 
		
		createComposite();
	}
	
	protected void createComposite()
	{
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginBottom = layout.marginLeft = layout.marginRight = layout.marginTop = 0;
		
		setLayout(layout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		setLayoutData(data);
		
		destinationImage = new Label(this, SWT.NONE);
		destinationText = new Text(this, SWT.BORDER);
		destinationText.setEditable(false);
		
		updateDestinationViewer();
		
		if (textLayoutData == null)
		{
			textLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		}
		destinationText.setLayoutData(textLayoutData);
		
		Composite buttons = new Composite(parent, SWT.NONE);
		
		GridLayout buttonLayout = new GridLayout(2, false);
		//buttonLayout.marginBottom = buttonLayout.marginLeft = buttonLayout.marginRight = buttonLayout.marginTop = 0;
		buttonLayout.marginHeight =  buttonLayout.marginWidth = 0;
		
		buttons.setLayout(buttonLayout);
		if (buttonLayoutData == null) {
			buttonLayoutData = new GridData(GridData.FILL_HORIZONTAL);
			buttonLayoutData.horizontalSpan = 2;
			buttonLayoutData.horizontalAlignment = GridData.END;
			buttonLayoutData.verticalAlignment = GridData.BEGINNING;
		}
		buttons.setLayoutData(buttonLayoutData);
		
		createBrowseButton(buttons);
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
	
	protected void setDestinationImage (Image image)
	{
		destinationImage.setImage(image);
	}
	
	protected void setDestinationText (String text)
	{
		destinationText.setText(text);
	}
	
	protected void updateDestinationViewer ()
	{
		if (nodeDestination == null) return;
		destinationText.setText("");
		
		if (nodeDestination instanceof IPackage)
		{
			IPackage pkg = (IPackage) nodeDestination;
			
			if (pkg.isTopLevel())
			{
				setDestinationText(pkg.getName());
			} else {
				setDestinationText(pkg.getPackageRelativePath().toString());
			}
			if (pkg.isExploded()) {
				setDestinationImage(PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_PACKAGE_EXPLODED));
			} else {
				setDestinationImage(PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_PACKAGE));
			}
		}
		else if (nodeDestination instanceof IPackageFolder)
		{
			IPackageFolder folder = (IPackageFolder) nodeDestination;
			setDestinationText(folder.getPackageRelativePath().toString());
			setDestinationImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
		}
		else if (nodeDestination instanceof IProject)
		{
			IProject project = (IProject) nodeDestination;
			setDestinationText(project.getName());
			setDestinationImage(PlatformUI.getWorkbench().getSharedImages().getImage(IDE.SharedImages.IMG_OBJ_PROJECT));
		}
		else if (nodeDestination instanceof IFolder)
		{
			IFolder folder = (IFolder) nodeDestination;
			setDestinationText("/" + folder.getProject().getName() + "/" + folder.getProjectRelativePath().toString());
			setDestinationImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER));
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
