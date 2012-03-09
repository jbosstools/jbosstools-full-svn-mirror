/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.viewer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.jboss.tools.common.ui.WizardUtils;
import org.jboss.tools.openshift.express.internal.core.console.UserDelegate;
import org.jboss.tools.openshift.express.internal.core.console.UserModel;
import org.jboss.tools.openshift.express.internal.ui.utils.Logger;

/**
 * @author Xavier Coulon
 * 
 */
public class OpenShiftConsoleExpressView extends ViewPart implements ITabbedPropertySheetPageContributor {

	private static final String VIEWER_ID = "org.jboss.tools.openshift.express.ui.viewer.expressConsoleViewer";

	private Composite loginContainer = null;

	private CommonViewer commonViewer = null;

	private final StackLayout layout = new StackLayout();

	private Composite stackContainer = null;
	
	private TabbedPropertySheetPage tabbedPropertySheetPage;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.navigator.CommonNavigator#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		stackContainer = new Composite(parent, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(stackContainer);
		stackContainer.setLayout(layout);
		loginContainer = new Composite(stackContainer, SWT.BORDER);
		GridLayoutFactory.fillDefaults().margins(6, 6).applyTo(loginContainer);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(loginContainer);
		loginContainer.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		Link loginLink = new Link(loginContainer, SWT.WRAP);
		loginLink.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		loginLink.setText("Click <a>here</a> to connect to your OpenShift account.");
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(loginLink);
		loginLink.addSelectionListener(onLoginLinkClicked());

		commonViewer = new CommonViewer(VIEWER_ID, stackContainer, SWT.BORDER);
		GridLayoutFactory.fillDefaults().applyTo(commonViewer.getTree());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(commonViewer.getTree());
		commonViewer.setContentProvider(new OpenShiftExpressConsoleContentProvider());
		commonViewer.setLabelProvider(new OpenShiftExpressConsoleLabelProvider());
		getSite().setSelectionProvider(commonViewer);
		layout.topControl = loginContainer;
	}

	private SelectionListener onLoginLinkClicked() {
		SelectionListener listener = new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				final Display display = Display.getCurrent();
				final Shell shell = new Shell(display);
				final IWizard connectToOpenShiftWizard = new ConnectToOpenShiftWizard();
				int returnCode = WizardUtils.openWizardDialog(connectToOpenShiftWizard, shell);
				if (returnCode == Window.OK) {
					Logger.debug("OpenShift Auth succeeded.");
					final UserDelegate user = UserModel.getDefault().getRecentUser();
					getCommonViewer().setInput(new OpenShiftExpressConsoleContentCategory(user));
					switchToCommonViewer();
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		return listener;
	}

	@Override
	public void setFocus() {
		layout.topControl.setFocus();
	}

	
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == IPropertySheetPage.class) {
			if (tabbedPropertySheetPage == null) {
				tabbedPropertySheetPage = new TabbedPropertySheetPage(this);
			}
			return tabbedPropertySheetPage;
		}
		return super.getAdapter(adapter);
	}

	public void switchToCommonViewer() {
		layout.topControl = this.commonViewer.getTree();
		stackContainer.layout();
	}

	public CommonViewer getCommonViewer() {
		return this.commonViewer;
	}

	@Override
	public String getContributorId() {
		return VIEWER_ID;
	}

}
