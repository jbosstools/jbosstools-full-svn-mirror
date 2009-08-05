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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;
import org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart;

/**
 * 
 * @author Dart Peng (dpeng@redhat.com) Date Apr 1, 2009
 */
public class SmooksMultiFormEditor extends AbstractSmooksFormEditor {

	public static final String EDITOR_ID = "org.jboss.tools.smooks.configuration.editors.MultiPageEditor";

	private SmooksConfigurationFormPage configurationPage;

	private SmooksGraphicalEditorPart graphicalPage;

	private SmooksConfigurationOverviewPage overViewPage;

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
		try {
			int index = this.addPage(overViewPage);
			setPageText(index, "Overview");
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		configurationPage = createSmooksConfigurationFormPage();
		addValidateListener(configurationPage);
		addSourceSynchronizeListener(configurationPage);
		try {
			int index = this.addPage(configurationPage);
			setPageText(index, "Design");
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		// graphicalPage = new SmooksGraphicalEditorPart(this);
		// try {
		// int index = this.addPage(graphicalPage, getEditorInput());
		// setPageText(index, "Graph");
		// } catch (PartInitException e) {
		// e.printStackTrace();
		// }

		super.addPages();
	}

	private SmooksConfigurationOverviewPage createSmooksConfigurationOverviewPage() {
		return new SmooksConfigurationOverviewPage(this, "Overview", "Overview", this);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		super.doSave(monitor);
		try {
			getSmooksGraphicsExt().eResource().save(Collections.emptyMap());
			if (graphicalPage != null) {
				if (graphicalPage.getEditDomain() != null) {
					graphicalPage.getEditDomain().getCommandStack().flush();
				}
			}
			firePropertyChange(PROP_DIRTY);
		} catch (Throwable t) {
		}
	}

	protected SmooksConfigurationFormPage createSmooksConfigurationFormPage() {
		return new SmooksConfigurationFormPage(this, "DesignPage", "Design Page");
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
}
