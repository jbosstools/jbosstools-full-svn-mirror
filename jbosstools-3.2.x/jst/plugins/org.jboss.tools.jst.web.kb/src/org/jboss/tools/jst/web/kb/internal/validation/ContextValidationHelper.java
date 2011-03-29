/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.wst.validation.internal.operations.WorkbenchContext;
import org.jboss.tools.jst.web.kb.validation.IValidatingProjectTree;
import org.jboss.tools.jst.web.kb.validation.IValidationContextManager;
import org.jboss.tools.jst.web.kb.validation.IValidator;

/**
 * Helper for Validators that use Validator Context. 
 * @author Alexey Kazakov
 */
public class ContextValidationHelper extends WorkbenchContext {

	protected IValidationContextManager validationContextManager;
	protected TextFileDocumentProvider documentProvider = new TextFileDocumentProvider();

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.validation.internal.operations.WorkbenchContext#initialize()
	 */
	@Override
	public void initialize() {
		validationContextManager = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.wst.validation.internal.operations.WorkbenchContext#registerResource(org.eclipse.core.resources.IResource)
	 */
	@Override
	public void registerResource(IResource resource) {
		if(resource instanceof IFile) {
			IFile file = (IFile)resource;
			if(validationContextManager == null) {
				validationContextManager = new ValidationContext(file.getProject());
			}
			validationContextManager.addProject(file.getProject());
			if(!file.exists()) {
				validationContextManager.addRemovedFile(file);
			} else {
				validationContextManager.registerFile(file);
			}
		}
	}

	/**
	 * @return Set of changed resources
	 */
	public Set<IFile> getChangedFiles() {
		Set<IFile> result = new HashSet<IFile>();
		String[] uris = getURIs();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		Set<IProject> projects = getAllProjects();
		for (int i = 0; i < uris.length; i++) {
			IFile currentFile = root.getFile(new Path(uris[i]));
			if(projects.contains(currentFile.getProject())) {
				result.add(currentFile);
			}
		}
		Set<IFile> removedFiles = getValidationContextManager().getRemovedFiles();
		for (IFile file : removedFiles) {
			if(projects.contains(file.getProject())) {
				result.add(file);
			}
		}
		return result;
	}

	public Set<IFile> getProjectSetRegisteredFiles() {
		Set<IFile> result = new HashSet<IFile>();
		Set<IFile> files = validationContextManager.getRegisteredFiles();
		Set<IProject> projects = getAllProjects();
		for (IFile file : files) {
			if(projects.contains(file.getProject())) {
				result.add(file);
			}
		}
		return result;
	}

	private Set<IProject> getAllProjects() {
		List<IValidator> validators = getValidationContextManager().getValidators();
		Set<IProject> projects = new HashSet<IProject>();
		for (IValidator validator : validators) {
			IValidatingProjectTree tree = validator.getValidatingProjects(getProject());
			projects.addAll(tree.getAllProjects());
		}
		return projects;
	}

	public IValidationContextManager getValidationContextManager() {
		if(validationContextManager==null) {
			validationContextManager = new ValidationContext(getProject());
		}
		return validationContextManager;
	}

	public void setValidationContextManager(IValidationContextManager context) {
		validationContextManager = context;
	}

	public TextFileDocumentProvider getDocumentProvider() {
		return documentProvider;
	}
}