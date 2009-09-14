/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * @author Dart Peng (dpeng@redhat.com)
 * Date Apr 13, 2009
 */
public class FileSelectionWizard extends Wizard implements INewWizard {

	private FileSelectionWizardPage fileSelectionWizardPage = null;
	
	private String filePath = null;
	
	private List<ViewerFilter> viewerFilters = null;
	
	private Object[] initSelections = null;
	
	private boolean multiSelect = false;
	
	private IFilePathProcessor filePathProcessor = null;

	@Override
	public void addPages() {
		fileSelectionWizardPage = new FileSelectionWizardPage("File Selection" ,null);
		fileSelectionWizardPage.setFilters(viewerFilters);
		fileSelectionWizardPage.setInitSelections(getInitSelections());
		fileSelectionWizardPage.setMultiSelect(isMultiSelect());
		fileSelectionWizardPage.setFilePathProcessor(getFilePathProcessor());
		this.addPage(fileSelectionWizardPage);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		filePath = fileSelectionWizardPage.getFilePath();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	public Object[] getInitSelections() {
		return initSelections;
	}

	public void setInitSelections(Object[] initSelections) {
		this.initSelections = initSelections;
	}

	public boolean isMultiSelect() {
		return multiSelect;
	}

	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public List<ViewerFilter> getViewerFilters() {
		return viewerFilters;
	}

	public void setViewerFilters(List<ViewerFilter> viewerFilters) {
		this.viewerFilters = viewerFilters;
	}

	public void setFilePathProcessor(IFilePathProcessor filePathProcessor) {
		this.filePathProcessor = filePathProcessor;
	}

	public IFilePathProcessor getFilePathProcessor() {
		return filePathProcessor;
	}
}
