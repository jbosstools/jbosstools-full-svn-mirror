/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public class SmooksFileEditorInput extends FileEditorInput {

	private List sourceModel = new ArrayList();
	
	private List targetModel = new ArrayList();
	
	private Object sourceRoot = null;
	
	private Object targetRoot = null;
	
	private IFile graphicalInformationFile;
	
	public IFile getGraphicalInformationFile() {
		return graphicalInformationFile;
	}

	public void setGraphicalInformationFile(IFile graphicalInformationFile) {
		this.graphicalInformationFile = graphicalInformationFile;
	}

	public Object getSourceTreeViewerInputContents() {
		return sourceRoot;
	}

	public void setSourceTreeViewerInputContents(Object sourceRoot) {
		this.sourceRoot = sourceRoot;
	}

	public Object getTargetTreeViewerInputContents() {
		return targetRoot;
	}

	public void setTargetTreeViewerInputContents(Object targetRoot) {
		this.targetRoot = targetRoot;
	}

	public SmooksFileEditorInput(IFile file) {
		this(file,null);
	}
	
	public SmooksFileEditorInput(IFile smooksConfigFile , IFile graphicalInformationFile) {
		super(smooksConfigFile);
		this.graphicalInformationFile = graphicalInformationFile;
	}

	/**
	 * @return the sourceModel
	 */
	public List getSourceModel() {
		return sourceModel;
	}

	/**
	 * @param sourceModel the sourceModel to set
	 */
	public void setSourceModel(List sourceModel) {
		this.sourceModel = sourceModel;
	}

	/**
	 * @return the targetModel
	 */
	public List getTargetModel() {
		return targetModel;
	}

	/**
	 * @param targetModel the targetModel to set
	 */
	public void setTargetModel(List targetModel) {
		this.targetModel = targetModel;
	}

}
