package org.jboss.ide.eclipse.archives.ui.util.composites;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.ui.ArchivesSharedImages;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;
import org.jboss.ide.eclipse.archives.ui.dialogs.ArchiveNodeDestinationDialog;

public class ArchiveDestinationComposite extends ArchiveNodeDestinationComposite {

	protected boolean inWorkspace;
	protected Button filesystemBrowseButton, workspaceBrowseButton;
	
	public ArchiveDestinationComposite (Composite parent, int style, Object destination) {
		super(parent, style, destination);
	}
	
	protected void fillBrowseComposite(Composite parent) {
		Composite browseComposite = new Composite(parent, SWT.NONE);
		browseComposite.setLayout(new GridLayout(2, false));
		
		workspaceBrowseButton = new Button(browseComposite, SWT.PUSH);
		workspaceBrowseButton.setText(ArchivesUIMessages.PackageDestinationComposite_workspaceBrowseButton_label);
		workspaceBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				openDestinationDialog();
			}
		});
		
		filesystemBrowseButton = new Button(browseComposite, SWT.PUSH);
		filesystemBrowseButton.setText(ArchivesUIMessages.PackageDestinationComposite_filesystemBrowseButton_label);
		filesystemBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				browseFilesystem();
			}
		});
	}
	
	protected void openDestinationDialog() {
		ArchiveNodeDestinationDialog dialog = new ArchiveNodeDestinationDialog(getShell(), nodeDestination, true, true);
		if (nodeDestination != null)
			dialog.setInitialSelection(nodeDestination);
		
		if (dialog.open() == Dialog.OK) 
			setPackageNodeDestination(dialog.getResult()[0]);
	}
	
	protected void browseFilesystem () {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		String currentPath = destinationText.getText();
		if (currentPath != null && currentPath.length() > 0 && !inWorkspace) {
			dialog.setFilterPath(destinationText.getText());
		}
		
		String path = dialog.open();
		if (path != null) 
			setPackageNodeDestination(new Path(path));
	}
	
	protected void updateDestinationViewer() {
		super.updateDestinationViewer();

		if (nodeDestination instanceof IPath) {
			inWorkspace = false;
			IPath path = (IPath) nodeDestination;
			destinationText.setText(path.toString());
			destinationImage.setImage(ArchivesSharedImages.getImage(ArchivesSharedImages.IMG_EXTERNAL_FILE));
		} else if (nodeDestination instanceof IContainer || nodeDestination instanceof IArchiveNode) {
			inWorkspace = true;
		}
	}
}
