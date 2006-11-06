package org.jboss.ide.eclipse.packages.ui.util;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.ui.PackagesUIMessages;
import org.jboss.ide.eclipse.packages.ui.PackagesUIPlugin;

public class PackageDestinationComposite extends PackageNodeDestinationComposite {

	protected boolean inWorkspace;
	protected Button filesystemBrowseButton, workspaceBrowseButton;
	
	public PackageDestinationComposite (Composite parent, int style, Object destination) {
		super(parent, style, destination);
	}
	
	protected void createBrowseButton(Composite parent) {
		Composite browseComposite = new Composite(parent, SWT.NONE);
		browseComposite.setLayout(new GridLayout(2, false));
		
		filesystemBrowseButton = new Button(browseComposite, SWT.PUSH);
		filesystemBrowseButton.setText(PackagesUIMessages.PackageDestinationComposite_filesystemBrowseButton_label);
		filesystemBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				browseFilesystem();
			}
		});
		
		workspaceBrowseButton = new Button(browseComposite, SWT.PUSH);
		workspaceBrowseButton.setText(PackagesUIMessages.PackageDestinationComposite_workspaceBrowseButton_label);
		workspaceBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				openDestinationDialog();
			}
		});
	}
	
	protected void browseFilesystem ()
	{
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		String currentPath = destinationText.getText();
		if (currentPath != null && currentPath.length() > 0 && !inWorkspace)
		{
			dialog.setFilterPath(destinationText.getText());
		}
		
		String path = dialog.open();
		if (path != null)
		{
			packageDestination = new Path(path);
			updateDestinationViewer();
		}	
	}
	
	protected void updateDestinationViewer()
	{
		super.updateDestinationViewer();

		if (packageDestination instanceof IPath)
		{
			inWorkspace = false;
			IPath path = (IPath) packageDestination;
			destinationImage.setImage(PackagesUIPlugin.getImage(PackagesUIPlugin.IMG_EXTERNAL_FILE));
			destinationText.setText(path.toString());
		}
		else if (packageDestination instanceof IContainer || packageDestination instanceof IPackageNode)
		{
			inWorkspace = true;
		}
	}
	
	public void setEditable(boolean editable) {
		super.setEditable(editable);
		
		workspaceBrowseButton.setEnabled(editable);
		filesystemBrowseButton.setEnabled(editable);
	}
}
