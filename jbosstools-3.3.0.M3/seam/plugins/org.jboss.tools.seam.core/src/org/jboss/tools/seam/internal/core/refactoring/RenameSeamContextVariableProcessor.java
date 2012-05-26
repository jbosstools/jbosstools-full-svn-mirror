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
package org.jboss.tools.seam.internal.core.refactoring;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RefactoringParticipant;
import org.eclipse.ltk.core.refactoring.participants.SharableParticipants;
import org.eclipse.ltk.internal.core.refactoring.Messages;
import org.jboss.tools.seam.core.BijectedAttributeType;
import org.jboss.tools.seam.core.IBijectedAttribute;
import org.jboss.tools.seam.core.ISeamComponent;
import org.jboss.tools.seam.core.ISeamContextShortVariable;
import org.jboss.tools.seam.core.ISeamContextVariable;
import org.jboss.tools.seam.core.ISeamFactory;
import org.jboss.tools.seam.core.ISeamProject;
import org.jboss.tools.seam.core.SeamCoreMessages;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.core.SeamProjectsSet;
import org.jboss.tools.seam.internal.core.scanner.java.SeamAnnotations;

/**
 * @author Daniel Azarov
 */
public class RenameSeamContextVariableProcessor extends SeamRenameProcessor {
	IFile file;
	
	/**
	 * @param file where refactor was called
	 */
	public RenameSeamContextVariableProcessor(IFile file, String oldName) {
		super();
		this.file = file;
		setOldName(oldName);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#checkFinalConditions(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext)
	 */
	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm,
			CheckConditionsContext context) throws CoreException,
			OperationCanceledException {
		status = new RefactoringStatus();
		
		rootChange = new CompositeChange(SeamCoreMessages.RENAME_SEAM_CONTEXT_VARIABLE_PROCESSOR_TITLE);
		if(component != null){
			checkDeclarations(component);
			
			if(status.hasFatalError())
				return status;
			
			renameComponent(pm, component);
		}else{
			renameSeamContextVariable(pm, file);
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#checkInitialConditions(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		RefactoringStatus result = new RefactoringStatus();
		boolean status = false;
		component = checkComponent();
		
		if(component == null){
			status = checkContextVariable();
		}else{
			setOldName(component.getName());
		}
		if(component == null && !status)
			result.addFatalError(Messages.format(SeamCoreMessages.RENAME_SEAM_CONTEXT_VARIABLE_PROCESSOR_CAN_NOT_FIND_CONTEXT_VARIABLE, getOldName()));
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#createChange(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		
		return rootChange;
	}
	
	private ISeamComponent checkComponent(){
		ISeamComponent comp;
		ISeamProject seamProject = SeamCorePlugin.getSeamProject(file.getProject(), true);
		projectsSet = new SeamProjectsSet(file.getProject());
		
		comp = checkComponent(seamProject);
		if(comp != null)
			return comp;
		
		IProject[] projects = projectsSet.getAllProjects();
		for (IProject project : projects) {
			ISeamProject sProject = SeamCorePlugin.getSeamProject(project, true);
			if(sProject != null){
				comp = checkComponent(sProject);
				if(comp != null)
					return comp;
			}
		}
		return null;
	}
	
	private ISeamComponent checkComponent(ISeamProject seamProject){
		if (seamProject != null) {
			ISeamComponent component = seamProject.getComponent(getOldName());
			if(component != null)
				return component;
			
			Set<ISeamContextVariable> variables = seamProject.getVariablesByName(getOldName());
			
			if(variables == null)
				return null;
			
			for(ISeamContextVariable variable : variables){
				if(variable instanceof ISeamContextShortVariable){
					ISeamContextVariable original = ((ISeamContextShortVariable)variable).getOriginal();
					if(original instanceof ISeamComponent)
						return (ISeamComponent)original;
				}
			}
		}
		return null;
	}
	
	private boolean checkContextVariable(){
		boolean status = false;
		ISeamProject seamProject = SeamCorePlugin.getSeamProject(file.getProject(), true);
		
		status = checkFactories(seamProject);
		if(status)
			return status;
		
		status = checkOuts(seamProject);
		if(status)
			return status;
		
		status = checkDataModels(seamProject);
		if(status)
			return status;
		
		IProject[] projects = projectsSet.getAllProjects();
		for (IProject project : projects) {
			ISeamProject sProject = SeamCorePlugin.getSeamProject(project, true);
			if(sProject != null){
				status = checkFactories(sProject);
				if(status)
					return status;
				
				status = checkOuts(sProject);
				if(status)
					return status;
				
				status = checkDataModels(sProject);
				if(status)
					return status;
			}
		}
		return status;
	}
	
	private boolean checkFactories(ISeamProject seamProject){
		if (seamProject != null) {
			Set<ISeamFactory> factories = seamProject.getFactoriesByName(getOldName());
			return factories.size() > 0;
		}
		return false;
	}
	
	private boolean checkOuts(ISeamProject seamProject){
		if (seamProject != null) {
			Set<IBijectedAttribute> variables = seamProject.getBijectedAttributesByName(getOldName(), BijectedAttributeType.OUT);
			
			return variables.size() > 0;
		}
		return false;
	}
	
	private boolean checkDataModels(ISeamProject seamProject){
		if (seamProject != null) {
			Set<IBijectedAttribute> variables = seamProject.getBijectedAttributesByName(getOldName(), BijectedAttributeType.DATA_BINDER);
			
			return variables.size() > 0;
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#getElements()
	 */
	@Override
	public Object[] getElements() {
		return new String[]{getNewName()};
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#getIdentifier()
	 */
	@Override
	public String getIdentifier() {
		return getClass().getName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#getProcessorName()
	 */
	@Override
	public String getProcessorName() {
		return SeamCoreMessages.RENAME_SEAM_CONTEXT_VARIABLE_PROCESSOR_TITLE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#isApplicable()
	 */
	@Override
	public boolean isApplicable() throws CoreException {
		return getNewName()!=null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor#loadParticipants(org.eclipse.ltk.core.refactoring.RefactoringStatus, org.eclipse.ltk.core.refactoring.participants.SharableParticipants)
	 */
	@Override
	public RefactoringParticipant[] loadParticipants(RefactoringStatus status,
			SharableParticipants sharedParticipants) throws CoreException {
		return EMPTY_REF_PARTICIPANT;
	}
}