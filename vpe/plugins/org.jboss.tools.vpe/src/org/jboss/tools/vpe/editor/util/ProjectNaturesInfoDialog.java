/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * 
 * @author yzhishko
 * 
 */

public class ProjectNaturesInfoDialog extends MessageDialog {

	private Button button;
	private Link link;
	private boolean isRemember = false;
	private static final String QUESTION = "Don't show this dialog again!"; //$NON-NLS-1$
	private static final String TITLE = "Missing Natures"; //$NON-NLS-1$
	private IProject project;

	public ProjectNaturesInfoDialog(String[] missingNatures, IProject project) {
		super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				TITLE, null, "", INFORMATION, //$NON-NLS-1$
				new String[] { IDialogConstants.OK_LABEL }, 0);
		this.project = project;
		message = getMessageInfo(missingNatures, project);
	}

	@Override
	protected Control createCustomArea(Composite parent) {

		GridLayout gridLayout = (GridLayout) parent.getLayout();
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		parent.setLayout(gridLayout);
		button = new Button(parent, SWT.CHECK);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		button.setLayoutData(gridData);
		button.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				isRemember = !isRemember;
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				isRemember = !isRemember;
			}
		});
		button.setText(QUESTION);
		link = new Link(parent, SWT.NONE);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gridData.grabExcessHorizontalSpace = true;
		link.setLayoutData(gridData);
		link.setText("<A>" + VpeUIMessages.MOZILLA_LOADING_ERROR_LINK_TEXT + "</A>"); //$NON-NLS-1$ //$NON-NLS-2$
		link.setToolTipText(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK);
		link.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				processLink(link);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				processLink(link);
			}

		});
		return parent;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == 0) {
			try {
				project.setPersistentProperty(ProjectNaturesChecker.IS_NATURES_CHECK_NEED, Boolean.toString(!isRemember));
			} catch (CoreException e) {
			}
		}
		super.buttonPressed(buttonId);
	}

	private void processLink(Link link) {
		BusyIndicator.showWhile(link.getDisplay(), new Runnable() {
			public void run() {
				URL theURL = null;
				;
				try {
					theURL = new URL(VpeUIMessages.MOZILLA_LOADING_ERROR_LINK);
				} catch (MalformedURLException e) {
					VpePlugin.reportProblem(e);
				}
				IWorkbenchBrowserSupport support = PlatformUI.getWorkbench()
						.getBrowserSupport();
				try {
					support.getExternalBrowser().openURL(theURL);
				} catch (PartInitException e) {
					VpePlugin.reportProblem(e);
				}
			}
		});
	}

	private String arrayToString(String[] strings){
		StringBuilder builder = new StringBuilder(""); //$NON-NLS-1$
		for (int i = 0; i < strings.length; i++) {
			builder.append(strings[i]+"\n"); //$NON-NLS-1$
		}
		return builder.toString();
	}
	
	private String getMessageInfo(String[] missingNatures, IProject project){
		String dialogMessage = "The project \"" + project.getName() +  //$NON-NLS-1$
		"\" doesn't contain following natures:\n" +  //$NON-NLS-1$
		arrayToString(missingNatures)+"\n"+"Visual Editor may not work properly."; //$NON-NLS-1$ //$NON-NLS-2$
		return dialogMessage;
	}
		
}
