/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.resref.core;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jboss.tools.common.model.ui.ModelUIImages;
import org.jboss.tools.common.model.ui.action.CommandBar;

public class VpeResourcesDialog extends TitleAreaDialog {

	Object fileLocation = null;
    CssReferencesComposite css = null;
    ElVariablesComposite el = null;
    TaglibReferencesComposite tld = null;
    AbsoluteFolderReferenceComposite absFolder = null;
    RelativeFolderReferenceComposite relFolder = null;
    CommandBar commandBar = new CommandBar();
    
    private final int DIALOG_WIDTH = 400;
	private final int DIALOG_HEIGHT = 300;
    

	public VpeResourcesDialog(Shell parentShell, Object fileLocation) {
		super(parentShell);
		setHelpAvailable(false);
		this.fileLocation = fileLocation;
		init(fileLocation);
	}

	private void init(Object fileLocation) {
		absFolder = new AbsoluteFolderReferenceComposite();
		relFolder = new RelativeFolderReferenceComposite();
		css = new CssReferencesComposite();
		el = new ElVariablesComposite();
		tld = new TaglibReferencesComposite();
		css.setObject(fileLocation);
    	el.setObject(fileLocation);
    	tld.setObject(fileLocation);
    	absFolder.setObject(fileLocation);
    	relFolder.setObject(fileLocation);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		getShell().setText(Messages.VRD_DEFAULT_WINDOW_TITLE);
		setTitle(Messages.VRD_DEFAULT_TITLE);
		setTitleImage(ModelUIImages.getImageDescriptor(
				ModelUIImages.WIZARD_DEFAULT).createImage(null));
		setMessage(Messages.VRD_PAGE_DESIGN_OPTIONS_ABOUT);
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		
		Label dialogAreaSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		dialogAreaSeparator.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		
		Control pageArea = createTabFolder(composite);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		pageArea.setLayoutData(gd);
		
		dialogAreaSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		dialogAreaSeparator.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
		
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.widthHint = DIALOG_WIDTH;
		gd.heightHint = DIALOG_HEIGHT;
		composite.setLayoutData(gd);
		
		return composite;
	}
	
	public Control createTabFolder(Composite parent) {
		final TabFolder tabFolder = new TabFolder(parent, SWT.FILL);
		
		TabItem foldersTab = new TabItem(tabFolder, SWT.NONE);
		TabItem cssTab = new TabItem(tabFolder, SWT.NONE);
		TabItem tldTab = new TabItem(tabFolder, SWT.NONE);
		TabItem elTab = new TabItem(tabFolder, SWT.NONE);
		
		Composite foldersControl = new Composite(tabFolder, SWT.NONE);
		foldersControl.setLayout(new GridLayout(1, false));
		absFolder.createControl(foldersControl);
		relFolder.createControl(foldersControl);
		
		Control cssControl = css.createControl(tabFolder);
		Control tldControl = tld.createControl(tabFolder);
		Control elControl = el.createControl(tabFolder);

		foldersTab.setText(Messages.VRD_ACTUAL_RUN_TIME_FOLDERS);
		foldersTab.setToolTipText(Messages.VRD_ACTUAL_RUN_TIME_FOLDERS);
		foldersTab.setControl(foldersControl);

		cssTab.setText(Messages.VRD_INCLUDED_CSS_FILES);
		cssTab.setToolTipText(Messages.VRD_INCLUDED_CSS_FILES);
		cssTab.setControl(cssControl);

		tldTab.setText(Messages.VRD_INCLUDED_TAG_LIBS);
		tldTab.setToolTipText(Messages.VRD_INCLUDED_TAG_LIBS);
		tldTab.setControl(tldControl);

		elTab.setText(Messages.VRD_SUBSTITUTED_EL_EXPRESSIONS);
		elTab.setToolTipText(Messages.VRD_SUBSTITUTED_EL_EXPRESSIONS);
		elTab.setControl(elControl);
		
		tabFolder.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {}

			public void widgetSelected(SelectionEvent e) {
				String selectedTabText = tabFolder.getSelection()[0].getText();
				if(Messages.VRD_ACTUAL_RUN_TIME_FOLDERS.equals(selectedTabText)) {
					 setMessage(Messages.VRD_ACTUAL_RUN_TIME_FOLDERS_ABOUT);
				}else if(Messages.VRD_INCLUDED_CSS_FILES.equals(selectedTabText)){
					setMessage(Messages.VRD_INCLUDED_CSS_FILES_ABOUT);
				}else if(Messages.VRD_INCLUDED_TAG_LIBS.equals(selectedTabText)){
					setMessage(Messages.VRD_INCLUDED_TAG_LIBS_ABOUT);
				}else if(Messages.VRD_SUBSTITUTED_EL_EXPRESSIONS.equals(selectedTabText)){
					setMessage(Messages.VRD_SUBSTITUTED_EL_EXPRESSIONS_ABOUT);
				}
			}});
		return tabFolder;
	}
	
	@Override
	protected void okPressed() {
		super.okPressed();
		/*
		 * When dialog OK is pressed - store all resource references
		 * from all tabs as preferences to the selected file.
		 */
		absFolder.commit();
	    relFolder.commit();
	    el.commit();
	    css.commit();
	    tld.commit();
	}

}
