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
package org.jboss.tools.vpe.resref;

import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizardView;
import org.jboss.tools.vpe.resref.core.AbsoluteFolderReferenceComposite;
import org.jboss.tools.vpe.resref.core.CssReferencesComposite;
import org.jboss.tools.vpe.resref.core.ElVariablesComposite;
import org.jboss.tools.vpe.resref.core.Messages;
import org.jboss.tools.vpe.resref.core.RelativeFolderReferenceComposite;
import org.jboss.tools.vpe.resref.core.TaglibReferencesComposite;

public class VpeResourcesDialogView extends AbstractQueryWizardView {
    IFile file;
    IPath path;
    CssReferencesComposite css = new CssReferencesComposite();
    // changed by estherbin
    // http://jira.jboss.com/jira/browse/JBIDE-2010
    ElVariablesComposite el = new ElVariablesComposite();
    TaglibReferencesComposite tld = new TaglibReferencesComposite();
    AbsoluteFolderReferenceComposite absFolder = new AbsoluteFolderReferenceComposite();
    RelativeFolderReferenceComposite relFolder = new RelativeFolderReferenceComposite();

    public void setObject(Object object) {
	super.setObject(object);
	Properties p = findProperties(object);
	file = (IFile) p.get("file"); //$NON-NLS-1$
	path = (IPath) p.get("path"); //$NON-NLS-1$

	css.setObject(object);
	// changed by estherbin
	// http://jira.jboss.com/jira/browse/JBIDE-2010
	el.setObject(object);
	tld.setObject(object);
	absFolder.setObject(p);
	relFolder.setObject(p);
    }

    public Control createControl(Composite parent) {

	TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
	tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
	TabItem foldersTab = new TabItem(tabFolder, SWT.NONE);
	TabItem cssTab = new TabItem(tabFolder, SWT.NONE);
	TabItem tldTab = new TabItem(tabFolder, SWT.NONE);
	TabItem elTab = new TabItem(tabFolder, SWT.NONE);
	
	Group groupControl = new Group(tabFolder, SWT.NONE);
	groupControl.setLayout(new GridLayout(1, false));
	groupControl.setText(Messages.ACTUAL_RUN_TIME_FOLDERS);
	Control absControl = absFolder.createControl(groupControl);
	Control relControl = relFolder.createControl(groupControl);
	Control cssControl = css.createControl(tabFolder);
	Control tldControl = tld.createControl(tabFolder);
	Control elControl = el.createControl(tabFolder);

	foldersTab.setText(Messages.ACTUAL_RUN_TIME_FOLDERS);
	foldersTab.setToolTipText(Messages.ACTUAL_RUN_TIME_FOLDERS);
	foldersTab.setControl(groupControl);

	cssTab.setText(Messages.INCLUDED_CSS_FILES);
	cssTab.setToolTipText(Messages.INCLUDED_CSS_FILES);
	cssTab.setControl(cssControl);

	tldTab.setText(Messages.INCLUDED_TAG_LIBS);
	tldTab.setToolTipText(Messages.INCLUDED_TAG_LIBS);
	tldTab.setControl(tldControl);

	elTab.setText(Messages.SUBSTITUTED_EL_EXPRESSIONS);
	elTab.setToolTipText(Messages.SUBSTITUTED_EL_EXPRESSIONS);
	elTab.setControl(elControl);

	return tabFolder;
    }

    public void action(String command) {
	if (OK.equals(command)) {
	    absFolder.commit();
	    relFolder.commit();
	    el.commit();
	    css.commit();
	    tld.commit();
	}
	super.action(command);
    }

    public Point getPreferredSize() {
	// changed by estherbin
	// http://jira.jboss.com/jira/browse/JBIDE-2010
	String os_name = System.getProperty("os.name"); //$NON-NLS-1$
	if (os_name != null && os_name.indexOf("Windows") >= 0) { //$NON-NLS-1$
	    return new Point(800, 300);
	}
	// changed by estherbin
	// http://jira.jboss.com/jira/browse/JBIDE-2010
	return new Point(600, 350);
    }

}
