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
package org.jboss.tools.jst.jsp.jspeditor;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.INestableKeyBindingService;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.jboss.tools.common.core.resources.XModelObjectEditorInput;

/**
 * 
 */
public abstract class JSPMultiPageEditorPart extends EditorPart {

	private CTabFolder container;

	private ArrayList nestedEditors = new ArrayList(3);

	protected JSPMultiPageEditorPart() {
		super();
	}

	public int addPage(Control control) {
		createItem(control);
		return getPageCount() - 1;
	}

	/**
	 * 
	 */

	Composite ppp = null;

	public int addPage(IEditorPart editor, IEditorInput input)
			throws PartInitException {
		IEditorSite site = createSite(editor);
		editor.init(site, input);
		Composite parent2;
		if (ppp != null) {
			parent2 = ppp;
		} else {
			parent2 = new Composite(getContainer(), SWT.NONE);
			ppp = parent2;
		}
		parent2.setLayout(new FillLayout());
		editor.createPartControl(parent2);
		editor.addPropertyListener(new IPropertyListener() {
			public void propertyChanged(Object source, int propertyId) {
				JSPMultiPageEditorPart.this.handlePropertyChange(propertyId);
			}
		});
		Item item = createItem(parent2);
		item.setData(editor);
		nestedEditors.add(editor);
		return getPageCount() - 1;
	}

	/**
	 * 
	 * @param parent
	 * @return
	 */
	private CTabFolder createContainer(Composite parent) {
		final CTabFolder newContainer = new CTabFolder(parent, SWT.BOTTOM
				| SWT.FLAT);
		newContainer.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int newPageIndex = newContainer.indexOf((CTabItem) e.item);
				pageChange(newPageIndex);
			}
		});
		return newContainer;
	}

	/*
	 * 
	 */
	private CTabItem createItem(Control control) {
		CTabItem item = new CTabItem(getTabFolder(), SWT.NONE);
		item.setControl(control);
		return item;
	}

	protected abstract void createPages();

	public final void createPartControl(Composite parent) {
		this.container = createContainer(parent);
		createPages();
		// set the active page (page 0 by default), unless it has already been
		// done
		if (getActivePage() == -1)
			setActivePage(0);
	}

	protected abstract IEditorSite createSite(IEditorPart editor);

	public void dispose() {
		for (int i = 0; i < nestedEditors.size(); ++i) {
			IEditorPart editor = (IEditorPart) nestedEditors.get(i);
			disposePart(editor);
		}
		nestedEditors.clear();
	}

	protected IEditorPart getActiveEditor() {
		int index = getActivePage();
		if (index != -1)
			return getEditor(0);
		return null;
	}

	protected int getActivePage() {
		CTabFolder tabFolder = getTabFolder();
		if (tabFolder != null && !tabFolder.isDisposed())
			return tabFolder.getSelectionIndex();
		return -1;
	}

	protected Composite getContainer() {
		return container;
	}

	protected Control getControl(int pageIndex) {
		return getItem(0).getControl();
	}

	protected IEditorPart getEditor(int pageIndex) {
		Item item = getItem(pageIndex);
		if (item != null) {
			Object data = item.getData();
			if (data instanceof IEditorPart) {
				return (IEditorPart) data;
			}
		}
		return null;
	}

	private CTabItem getItem(int pageIndex) {
		return getTabFolder().getItem(pageIndex);
	}

	protected int getPageCount() {
		CTabFolder folder = getTabFolder();
		// May not have been created yet, or may have been disposed.
		if (folder != null && !folder.isDisposed())
			return folder.getItemCount();
		return 0;
	}

	protected Image getPageImage(int pageIndex) {
		return getItem(pageIndex).getImage();
	}

	protected String getPageText(int pageIndex) {
		return getItem(pageIndex).getText();
	}

	protected CTabFolder getTabFolder() {
		return container;
	}

	protected void handlePropertyChange(int propertyId) {
		firePropertyChange(propertyId);
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setSite(site);
		setInput(XModelObjectEditorInput.checkInput(input));
		site.setSelectionProvider(new JSPMultiPageSelectionProvider(this));
	}

	public boolean isDirty() {
		// use nestedEditors to avoid SWT requests; see bug 12996
		for (Iterator i = nestedEditors.iterator(); i.hasNext();) {
			IEditorPart editor = (IEditorPart) i.next();
			if (editor.isDirty()) {
				return true;
			}
		}
		return false;
	}

	protected void pageChange(int newPageIndex) {
		Control control = getControl(newPageIndex);
		if (control != null) {
			control.setVisible(true);
		}

		setFocus();
		IEditorPart activeEditor = getEditor(newPageIndex);
		IEditorActionBarContributor contributor = getEditorSite()
				.getActionBarContributor();
		if (contributor != null
				&& contributor instanceof MultiPageEditorActionBarContributor) {
			((MultiPageEditorActionBarContributor) contributor)
					.setActivePage(activeEditor);
		}
		if (activeEditor != null) {
			ISelectionProvider selectionProvider = activeEditor.getSite()
					.getSelectionProvider();
			if (selectionProvider != null) {
				SelectionChangedEvent event = new SelectionChangedEvent(
						selectionProvider, selectionProvider.getSelection());
				((MultiPageSelectionProvider) getSite().getSelectionProvider())
						.fireSelectionChanged(event);
			}
		}
	}

	private void disposePart(final IWorkbenchPart part) {
		SafeRunner.run(new SafeRunnable() {
			public void run() {
				if (part.getSite() instanceof MultiPageEditorSite) {
					MultiPageEditorSite partSite = (MultiPageEditorSite) part
							.getSite();
					partSite.dispose();
				}
				part.dispose();
			}

			public void handleException(Throwable e) {
			}
		});
	}

	public void removePage(int pageIndex) {
		Assert.isTrue(pageIndex >= 0 && pageIndex < getPageCount());
		IEditorPart editor = getEditor(pageIndex);
		getItem(pageIndex).dispose();
		if (editor != null) {
			nestedEditors.remove(editor);
			disposePart(editor);
		}
	}

	protected void setActivePage(int pageIndex) {
		Assert.isTrue(pageIndex >= 0 && pageIndex < getPageCount());
		getTabFolder().setSelection(pageIndex);
	}

	protected void setControl(int pageIndex, Control control) {
		getItem(pageIndex).setControl(control);
	}

	public void setFocus() {
		setFocus(getActivePage());
	}

	private void setFocus(int pageIndex) {
		final IKeyBindingService service = getSite().getKeyBindingService();
		if (pageIndex < 0 || pageIndex >= getPageCount()) {
			if (service instanceof INestableKeyBindingService) {
				final INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
				nestableService.activateKeyBindingService(null);
			} 
			return;
		}

		IEditorPart editor = getEditor(pageIndex);
		if (editor != null) {
			editor.setFocus();
			if (service instanceof INestableKeyBindingService) {
				INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
				if (editor != null) {
					nestableService.activateKeyBindingService(editor
							.getEditorSite());
				} else {
					nestableService.activateKeyBindingService(null);
				}
			}
		} else {
			if (service instanceof INestableKeyBindingService) {
				INestableKeyBindingService nestableService = (INestableKeyBindingService) service;
				nestableService.activateKeyBindingService(null);
			}

			Control control = getControl(pageIndex);
			if (control != null) {
				control.setFocus();
			}
		}
	}

	protected void setPageImage(int pageIndex, Image image) {
		getItem(pageIndex).setImage(image);
	}

	protected void setPageText(int pageIndex, String text) {
		getItem(pageIndex).setText(text);
	}
}
