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
package org.jboss.tools.vpe.editor.toolbar;


import org.eclipse.compare.Splitter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jboss.tools.jst.jsp.JspEditorPlugin;
import org.jboss.tools.jst.jsp.preferences.IVpePreferencesPage;
import org.jboss.tools.vpe.VpePlugin;

/**
 * @author Erick Created on 14.07.2005
 * @see IVpeToolBarManager
 */
public class VpeToolBarManager implements IVpeToolBarManager {

	private Splitter splitter;
	private Composite cmpToolBar;

	public VpeToolBarManager() { }

	public Composite createToolBarComposite(Composite parent) {
		splitter = new Splitter(parent, SWT.NONE) {

			// if there are no visual children then return Point(0,0)
			public Point computeSize(int hint, int hint2, boolean changed) {
                
				int countVisibleChild = 0;
				for (Control child : getChildren()) {
					if (child.getVisible())
						countVisibleChild++;
				}

				if (countVisibleChild == 0)
					return new Point(0, 0);
				else
					return super.computeSize(hint, hint2, changed);
			}
		};
		splitter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return splitter;
	}

	public void addToolBar(IVpeToolBar bar) {

		cmpToolBar = new Composite(splitter, SWT.NONE);
		FormData data = new FormData();
		data.left = new FormAttachment(0);
		data.right = new FormAttachment(100);
		data.top = new FormAttachment(0);

		GridLayout layoutTl = new GridLayout(2, false);
		layoutTl.marginBottom = 0;
		layoutTl.marginHeight = 0;
		layoutTl.marginWidth = 0;
		layoutTl.verticalSpacing = 0;
		layoutTl.horizontalSpacing = 0;
		cmpToolBar.setLayout(layoutTl);
		cmpToolBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// create toolbar control
		bar.createToolBarControl(cmpToolBar);

		// create tolbar container
		ToolbarContainer toolbarContainer = new ToolbarContainer(cmpToolBar, bar);

		// show or hide toolbar
		setStateToolbar(cmpToolBar, JspEditorPlugin.getDefault().getPreferenceStore().getBoolean(
				IVpePreferencesPage.SHOW_TEXT_FORMATTING));

	}

	public void setToolbarVisibility(boolean isVisible) {
		if (cmpToolBar != null) {
			setStateToolbar(cmpToolBar, isVisible);
		} else {
			VpePlugin.getDefault().logError("Toolbar control is not initialized.");
		}
	}

	/**
	 * show/hide toolbar
	 * 
	 * @param toolBar
	 * @param show
	 */
	protected void setStateToolbar(Control toolBar, boolean show) {
		splitter.setVisible(toolBar, show);
		splitter.getParent().layout(true, true);
	}

	public void dispose() {
		if (splitter != null) {
			splitter.dispose();
			splitter = null;
		}
	}

	/*
	 * This class describe the container for toolbars
	 */
	private static class ToolbarContainer {

		private Composite parent;
		private IVpeToolBar toolbar;

		public ToolbarContainer(Composite parent, IVpeToolBar toolbar) {
			this.parent = parent;
			this.toolbar = toolbar;
		}

		public Composite getParent() {
			return parent;
		}

		public IVpeToolBar getToolbar() {
			return toolbar;
		}

	}
}