/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.PartInitException;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;
import org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 1, 2009
 */
public class SmooksMultiFormEditor extends AbstractSmooksFormEditor implements ISelectionProvider {

	public static final String EDITOR_ID = "org.jboss.tools.smooks.configuration.editors.MultiPageEditor";

	private SmooksConfigurationFormPage configurationPage;

	private SmooksGraphicalEditorPart graphicalPage;

	private SmooksConfigurationOverviewPage overViewPage;

	private SmooksReaderFormPage readerPage;

	private ISelection selection;

	private Collection<ISelectionChangedListener> selectionChangeListener = new ArrayList<ISelectionChangedListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.editor.AbstractSmooksFormEditor#addPages()
	 */
	@Override
	protected void addPages() {
		overViewPage = createSmooksConfigurationOverviewPage();
		addValidateListener(overViewPage);
		addSourceSynchronizeListener(overViewPage);
		addSmooksGraphExtetionListener(overViewPage);
		try {
			int index = this.addPage(overViewPage);
			setPageText(index, "Overview");
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		readerPage = new SmooksReaderFormPage(this, "reader_page", "Input");
		addValidateListener(readerPage);
		addSourceSynchronizeListener(readerPage);
		addSmooksGraphExtetionListener(readerPage);
		try {
			int index = this.addPage(readerPage);
			setPageText(index, "Input");
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		configurationPage = createSmooksConfigurationFormPage();
		addValidateListener(configurationPage);
		addSourceSynchronizeListener(configurationPage);
		addSmooksGraphExtetionListener(configurationPage);
		try {
			int index = this.addPage(configurationPage);
			setPageText(index, "Message Filter");
		} catch (PartInitException e) {
			e.printStackTrace();
		}

//		graphicalPage = new SmooksGraphicalEditorPart(this);
//		addSourceSynchronizeListener(graphicalPage);
//		addSmooksGraphExtetionListener(graphicalPage);
//		try {
//			int index = this.addPage(graphicalPage, getEditorInput());
//			setPageText(index, "Graph");
//		} catch (PartInitException e) {
//			e.printStackTrace();
//		}

		super.addPages();
	}

//	private SmooksConfigurationReaderPage createSmooksConfigurationReaderPage() {
//		return new SmooksConfigurationReaderPage(this, "reader_page1", "Reader Page");
//	}

	private SmooksConfigurationOverviewPage createSmooksConfigurationOverviewPage() {
		return new SmooksConfigurationOverviewPage(this, "overview_page", "Overview", this);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (graphicalPage != null) {
			if (graphicalPage.getEditDomain() != null) {
				graphicalPage.getEditDomain().getCommandStack().flush();
			}
		}
		super.doSave(monitor);
	}

	protected SmooksConfigurationFormPage createSmooksConfigurationFormPage() {
		return new SmooksConfigurationResourceConfigPage(this, "message_filter_page", "Design Page");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.editor.AbstractSmooksFormEditor#
	 * activeRecentAffectedModel(java.util.Collection)
	 */
	@Override
	public void activeRecentAffectedModel(Collection<?> collection) {
		final Collection<?> theSelection = collection;
		if (theSelection != null && !theSelection.isEmpty() && configurationPage != null) {
			List<Object> newList = new ArrayList<Object>();
			for (Iterator<?> iterator = theSelection.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				// if (object instanceof IWrapperItemProvider) {
				// newList.add(((IWrapperItemProvider) object).getValue());
				// }
				newList.add(object);
			}
			configurationPage.setSelectionToViewer(newList);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.editor.AbstractSmooksFormEditor#
	 * createNewModelViaTextPage()
	 */
	@Override
	protected void createNewModelViaTextPage() {
		super.createNewModelViaTextPage();
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangeListener.add(listener);
	}

	public ISelection getSelection() {
		return this.selection;
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangeListener.remove(listener);
	}

	public void setSelection(ISelection selection) {
		if (selection != null) {
			if (selection.equals(this.selection)) {
				return;
			}
		}
		this.selection = selection;

		for (Iterator<?> iterator = this.selectionChangeListener.iterator(); iterator.hasNext();) {
			ISelectionChangedListener l = (ISelectionChangedListener) iterator.next();
			l.selectionChanged(new SelectionChangedEvent(this, getSelection()));
		}
	}

	public void addSmooksGraphExtetionListener(ISmooksGraphChangeListener listener) {
		SmooksGraphicsExtType ex = getSmooksGraphicsExt();
		if (ex != null) {
			ex.addSmooksGraphChangeListener(listener);
		}
	}

	public void removeSmooksGraphExtetionListener(ISmooksGraphChangeListener listener) {
		SmooksGraphicsExtType ex = getSmooksGraphicsExt();
		if (ex != null) {
			ex.removeSmooksGraphChangeListener(listener);
		}
	}
}
