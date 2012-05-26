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
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.common.resref.core.ResourceReferenceList;

public abstract class FolderReferenceComposite {

	private Text pathText = null;
	/*
	 * Object representing file location.
	 * Can be IFile or IPath.
	 */
	Object fileLocation = null;
	private ResourceReference defaultReference;
	List<ResourceReference> resourceReferences = new ArrayList<ResourceReference>();
	private ResourceReference currentReference;
	private boolean overrideDefaultPath;
	
	private Combo scopeCombo = null;
	private static final String[] scopeNames = { Messages.SCOPE_PAGE_SHORT,
			Messages.SCOPE_FOLDER_SHORT, Messages.SCOPE_PROJECT_SHORT };
	private static final int[] scopeValues = { ResourceReference.FILE_SCOPE,
		 ResourceReference.FOLDER_SCOPE,  ResourceReference.PROJECT_SCOPE };
	private Button browseButton;
	private Button overrideDefaultPathCheckBox;
	private Label pathLabel;
	private Label scopeLabel;
	
	
	public FolderReferenceComposite() {}
	
	public void setObject(Object fileLocation, ResourceReference defaultReference) {
		this.defaultReference = defaultReference;
		
		this.fileLocation = fileLocation;
		
		if (fileLocation instanceof IFile) {
			resourceReferences.addAll(Arrays.asList(getReferenceList().getAllResources((IFile) fileLocation)));
		} else if (fileLocation instanceof IPath) {
			resourceReferences.addAll(Arrays.asList(getReferenceList().getAllResources((IPath) fileLocation)));
		}
		
		if (resourceReferences.size() != 0) {
			overrideDefaultPath = true;
			ResourceReference firstReference = resourceReferences.remove(0);
			currentReference = new ResourceReference(firstReference.getLocation(), firstReference.getScope());
		} else {
			overrideDefaultPath = false;
			currentReference = new ResourceReference(defaultReference.getLocation(), defaultReference.getScope());
		}
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
		
		
		overrideDefaultPathCheckBox = new Button(groupControl, SWT.LEFT | SWT.CHECK);
		overrideDefaultPathCheckBox.setText(Messages.OVERRIDE_DEFAULT_FOLDER);
		gd = new GridData();
		gd.horizontalSpan = 3;
		overrideDefaultPathCheckBox.setLayoutData(gd);
		overrideDefaultPathCheckBox.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				overrideDefaultPath = overrideDefaultPathCheckBox.getSelection();
				updateFieldsForOverrideDefaultPath();
			}
		});
		overrideDefaultPathCheckBox.setSelection(overrideDefaultPath);
		
		pathLabel = new Label(groupControl, SWT.RIGHT);
		pathLabel.setText(Messages.FOLDER_PATH);
		gd = new GridData();
		gd.horizontalIndent = 20;
		pathLabel.setLayoutData(gd);
		
		/*
		 * Create text control.
		 */
		pathText = new Text(groupControl, SWT.BORDER);
		/*
		 * Set location from stored resource reference
		 * if it presents.
		 */
		pathText.setText(currentReference.getLocation());
		gd = new GridData(SWT.FILL, SWT.NONE, true, false);
		pathText.setLayoutData(gd);
		pathText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				currentReference.setLocation(pathText.getText().trim());
			}
		});
		
		browseButton = new Button(groupControl, SWT.PUSH);
		browseButton.setText(Messages.BROWSE_BUTTON_NAME);
		gd = new GridData();
		browseButton.setLayoutData(gd);
		browseButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				DirectoryDialog dialog = new DirectoryDialog(browseButton.getShell());
				dialog.setMessage(Messages.SELECT_FOLDER_DIALOG_TITLE);
				if (new File(currentReference.getLocation()).exists() ){
					dialog.setFilterPath(currentReference.getLocation());
				}
				String newPath = dialog.open();
				/*
				 * When new value is set
				 * store it to current resref, filter, text field.
				 */
				if (newPath != null) {
					currentReference.setLocation(newPath.trim());
					pathText.setText(currentReference.getLocation());
				}
			}
		});
		
		scopeLabel = new Label(groupControl, SWT.RIGHT);
		scopeLabel.setText(Messages.SCOPE_GROUP_NAME);
		gd = new GridData();
		gd.horizontalIndent = 20;
		scopeLabel.setLayoutData(gd);
		
		scopeCombo = new Combo(groupControl, SWT.BORDER | SWT.READ_ONLY);
		scopeCombo.setItems(scopeNames);
		setScopeComboValue(currentReference.getScope());

		gd = new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1);
		scopeCombo.setLayoutData(gd);

		scopeCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentReference.setScope(getScopeComboValue());
			}
		});
		
		updateFieldsForOverrideDefaultPath();
		
		return groupControl;
	}
	
	public void commit() {
		List<ResourceReference> newResourceReferencesList = new ArrayList<ResourceReference>();
		for (int i = resourceReferences.size() - 1; i >= 0; i--) {
			if(!resourceReferences.get(i).getLocation().equals(currentReference.getLocation()) 
					&& resourceReferences.get(i).getScope() != currentReference.getScope()) {
				newResourceReferencesList.add(resourceReferences.get(i));
			}
		}
		if (overrideDefaultPath) {
			newResourceReferencesList.add(currentReference);
		}
		
		ResourceReference[] newResourceReferences = newResourceReferencesList.toArray(new ResourceReference[0]);
		if (fileLocation instanceof IFile) {
			getReferenceList().setAllResources((IFile) fileLocation, newResourceReferences);
		} else if (fileLocation instanceof IPath) {
			getReferenceList().setAllResources((IPath) fileLocation, newResourceReferences);
		}
	}
	
	private int getScopeComboValue() {
		return scopeValues[scopeCombo.getSelectionIndex()];
	}
	
	private void setScopeComboValue(int value) {
		int i = 0;
		for (int scopeValue : scopeValues) {
			if (scopeValue == value) {
				break;
			}
			i++;
		}
		scopeCombo.select(i);
	}
	
	private void updateFieldsForOverrideDefaultPath() {
		for (Control control : new Control[] {pathLabel, pathText, browseButton,
											scopeLabel, scopeCombo}) {
			control.setEnabled(overrideDefaultPath);
		}
		if (!overrideDefaultPath) {
			pathText.setText(defaultReference.getLocation());
			setScopeComboValue(defaultReference.getScope());
		}
	}

}
