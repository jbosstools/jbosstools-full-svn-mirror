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
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizardView;
import org.jboss.tools.vpe.resref.core.AbsoluteFolderReferenceComposite;
import org.jboss.tools.vpe.resref.core.CssReferencesComposite;
import org.jboss.tools.vpe.resref.core.ElVariablesComposite;
import org.jboss.tools.vpe.resref.core.RelativeFolderReferenceComposite;
import org.jboss.tools.vpe.resref.core.TaglibReferencesComposite;

public class VpeResourcesDialogView extends AbstractQueryWizardView {
	IFile file;
	IPath path;
	CssReferencesComposite css = new CssReferencesComposite();
    //changed by estherbin
    //http://jira.jboss.com/jira/browse/JBIDE-2010
	ElVariablesComposite   el  = new ElVariablesComposite();
	TaglibReferencesComposite tld = new TaglibReferencesComposite();
	AbsoluteFolderReferenceComposite absFolder = new AbsoluteFolderReferenceComposite();
	RelativeFolderReferenceComposite relFolder = new RelativeFolderReferenceComposite();

	public void setObject(Object object) {
		super.setObject(object);
		Properties p = findProperties(object);
		file = (IFile)p.get("file");
		path = (IPath)p.get("path");

		css.setObject(object);
	    //changed by estherbin
        //http://jira.jboss.com/jira/browse/JBIDE-2010
		el.setObject(object);
		tld.setObject(object);
		absFolder.setObject(p);
		relFolder.setObject(p);
	}

	public Control createControl(Composite parent) {
		GridData data;
		Composite c = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginWidth = 5;
		layout.marginHeight = 0;
		c.setLayout(layout);
		Control absControl = absFolder.createControl(c);
		Control relControl = relFolder.createControl(c);
		Control cssControl = css.createControl(c);
		Control tldControl = tld.createControl(c);
		Control elControl = el.createControl(c);
		return c;
	}

	public void action(String command) {
		if(OK.equals(command)) {
			absFolder.commit();
			relFolder.commit();
			el.commit();
			css.commit();
			tld.commit();
		}
		super.action(command);
	}

	public Point getPreferredSize() {
		//changed by estherbin
		//http://jira.jboss.com/jira/browse/JBIDE-2010
		String os_name = System.getProperty("os.name");
		if(os_name != null && os_name.indexOf("Windows") >= 0) return new Point(800, 600);
	    //changed by estherbin
        //http://jira.jboss.com/jira/browse/JBIDE-2010
		return new Point(600, 700);
	}

}
