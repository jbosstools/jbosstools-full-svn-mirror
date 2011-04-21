/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.resref.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.common.resref.core.ResourceReferenceList;

public abstract class FolderReferenceComposite implements ModifyListener {

	private final String BROWSE_BUTTON_NAME = "&Browse...";//$NON-NLS-1$
	private Text text = null;
	/*
	 * Object representing file location.
	 * Can be IFile or IPath.
	 */
	Object fileLocation = null;
	ResourceReference[] rs = null;
	ResourceReference current = null;
	private String browseDialogFilterPath = null;
	
	public FolderReferenceComposite() {}
	
	public void setObject(Object fileLocation) {
		this.fileLocation = fileLocation;
		if (fileLocation instanceof IFile) {
			browseDialogFilterPath = ((IFile)fileLocation).getProject().getLocation().toString(); 
		}
		if (null != fileLocation) {
			if (fileLocation instanceof IFile) {
				rs = getReferenceList().getAllResources((IFile) fileLocation);
			} else if (fileLocation instanceof IPath) {
				rs = getReferenceList().getAllResources((IPath) fileLocation);
			}
		} else {
			rs = new ResourceReference[0];
		}
		
		if(rs.length == 0) {
			rs = new ResourceReference[1];
			rs[0] = new ResourceReference("", ResourceReference.FILE_SCOPE); //$NON-NLS-1$
		}
		current = rs[0];
	}

	protected abstract ResourceReferenceList getReferenceList();
	protected abstract String getTitle();
	
	public Control createControl(Composite parent) {
		/*
		 * Create group control.
		 */
		Group groupControl = new Group(parent, SWT.SHADOW_ETCHED_IN);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		GridLayout layout = new GridLayout(3, false);
		groupControl.setLayout(layout);
		groupControl.setLayoutData(gd);
		groupControl.setText(getTitle());
		
		/*
		 * Create label control
		 */
		Label pathLabel = new Label(groupControl, SWT.RIGHT);
		pathLabel.setText(Messages.FOLDER_PATH);
		gd = new GridData();
		pathLabel.setLayoutData(gd);
		
		/*
		 * Create text control.
		 */
		text = new Text(groupControl, SWT.BORDER);
		/*
		 * Set location from stored resource reference
		 * if it presents.
		 */
		text.setText(current.getLocation());
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		text.setLayoutData(gd);
		text.addModifyListener(this);
		
		/*
		 * Create browse control.
		 */
		final Button button = new Button(groupControl, SWT.PUSH);
		button.setText(BROWSE_BUTTON_NAME);
		gd = new GridData();
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				DirectoryDialog dialog = new DirectoryDialog(button.getShell());
				dialog.setMessage(Messages.SELECT_FOLDER_DIALOG_TITLE);
				if ((null != browseDialogFilterPath) && (new File(browseDialogFilterPath).exists()) ){
					dialog.setFilterPath(browseDialogFilterPath);
				}
				String newPath = dialog.open();
				/*
				 * When new value is set
				 * store it to current resref, filter, text field.
				 */
				if (newPath != null) {
					newPath = newPath.trim();
					browseDialogFilterPath = newPath;
					current.setLocation(newPath);
					text.setText(newPath);
				}
			}
		});
		
		/*
		 * Create scope label.
		 */
		Label comboboxLabel = new Label(groupControl, SWT.RIGHT);
		comboboxLabel.setText(Messages.SCOPE_GROUP_NAME);
		gd = new GridData();
		comboboxLabel.setLayoutData(gd);
		
		/*
		 * Create scope combobox.
		 */
		CCombo combobox = new CCombo(groupControl, SWT.BORDER);
		String[] items = { Messages.SCOPE_PAGE_SHORT,
				Messages.SCOPE_FOLDER_SHORT, Messages.SCOPE_PROJECT_SHORT };
		combobox.setItems(items);
		combobox.setText(Messages.SCOPE_PAGE_SHORT);
		gd = new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1);
		combobox.setLayoutData(gd);
		
		return groupControl;
	}
	
	public void commit() {
		current.setScope(getNewScope());
		List l = new ArrayList();
		for (int i = rs.length - 2; i >= 0; i--) {
			if(rs[i].getLocation().equals(current.getLocation())) {
				continue;
			}
			if(rs[i].getScope() == current.getScope()) {
				continue;
			}
			l.add(rs[i]);
		}
		l.add(current);
		rs = (ResourceReference[])l.toArray(new ResourceReference[0]);
		if (null != fileLocation) {
			if (fileLocation instanceof IFile) {
				getReferenceList().setAllResources((IFile) fileLocation, rs);
			} else if (fileLocation instanceof IPath) {
				getReferenceList().setAllResources((IPath) fileLocation, rs);
			}
		}
	}
	
	int getNewScope() {
		return ResourceReference.FILE_SCOPE;
	}

	public void setFileLocation(Object fileLocation) {
		this.fileLocation = fileLocation;
	}

	public void modifyText(ModifyEvent e) {
		browseDialogFilterPath = text.getText().trim();
		current.setLocation(browseDialogFilterPath);
	}
	
}