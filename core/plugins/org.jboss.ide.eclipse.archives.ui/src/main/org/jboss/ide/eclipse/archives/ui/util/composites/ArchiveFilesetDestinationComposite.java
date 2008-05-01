package org.jboss.ide.eclipse.archives.ui.util.composites;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.jboss.ide.eclipse.archives.ui.ArchivesUIMessages;
import org.jboss.ide.eclipse.archives.ui.dialogs.ArchiveNodeDestinationDialog;

public class ArchiveFilesetDestinationComposite extends ArchiveNodeDestinationComposite {
	protected Button filesystemBrowseButton;
	public ArchiveFilesetDestinationComposite(Composite parent, int style,
			Object destination) {
		super(parent, style, destination);
	}

	protected void fillBrowseComposite(Composite parent) {
		Composite browseComposite = new Composite(parent, SWT.NONE);
		browseComposite.setLayout(new GridLayout(2, false));
		
		filesystemBrowseButton = new Button(browseComposite, SWT.PUSH);
		filesystemBrowseButton.setText(ArchivesUIMessages.PackageDestinationComposite_workspaceBrowseButton_label);
		filesystemBrowseButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				openDestinationDialog();
			}
		});
	}
	
	protected void openDestinationDialog() {
		ArchiveNodeDestinationDialog dialog = new ArchiveNodeDestinationDialog(getShell(), nodeDestination, false, true);
		if (nodeDestination != null)
			dialog.setInitialSelection(nodeDestination);
		
		if (dialog.open() == Dialog.OK) 
			setPackageNodeDestination(dialog.getResult()[0]);
	}
}
