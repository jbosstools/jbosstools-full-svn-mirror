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
package org.jboss.tools.common.model.refactoring;

import java.text.MessageFormat;
import java.util.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.internal.corext.refactoring.tagging.*;
import org.eclipse.ltk.core.refactoring.*;
import org.eclipse.ltk.core.refactoring.participants.*;
import org.jboss.tools.common.model.XModelObject;

public abstract class ModelRenameProcessor extends RenameProcessor implements INameUpdating, IReferenceUpdating {
	protected XModelObject object;
	protected String fNewElementName;
	private boolean updateReferences = true;
		
	public ModelRenameProcessor() {}
	
	public void setModelObject(XModelObject object) {
		this.object = object;
		if (object != null) {
			setNewElementName(object);
		}
	}
	
	protected void setNewElementName(XModelObject object) {
		if(object != null) {
			setNewElementName(object.getAttributeValue(getProcessorName()));
		}
	}
	
	protected abstract String getPropertyName();

	public void setNewElementName(String newName) {
		fNewElementName = newName;
	}

	public String getNewElementName() {
		return fNewElementName;
	}
	
	public boolean isApplicable() throws CoreException {
		return true;
	}
	
	public String getProcessorName() {
		String message = MessageFormat.format("Rename {0} to {1}", getCurrentElementName(), getNewElementName());
		return message;
	}
	
	public Object[] getElements() {
		return new Object[] {object};
	}
	
	public String getCurrentElementName() {
		return object.getAttributeValue(getPropertyName());
	}
	
	public String[] getAffectedProjectNatures() throws CoreException {
		return new String[]{};
	}

	public Object getNewElement() {
		return object;
	}

	public RefactoringParticipant[] loadParticipants(RefactoringStatus status, SharableParticipants shared) throws CoreException {
		Object[] elements= getElements();
		String[] natures= getAffectedProjectNatures();
		List<RefactoringParticipant> result = new ArrayList<RefactoringParticipant>();
		RenameArguments arguments= new RenameArguments(getNewElementName(), getUpdateReferences());
		for (int i= 0; i < elements.length; i++) {
			result.addAll(Arrays.asList(ParticipantManager.loadRenameParticipants(status, 
				this, elements[i],
				arguments, natures, shared)));
		}
		return result.toArray(new RefactoringParticipant[result.size()]);
	}
	
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm) throws CoreException {
		return new RefactoringStatus();
	}
	
	public RefactoringStatus checkNewElementName(String newName) throws CoreException {
		XModelObject c = object.getParent();
		if (c == null)
			return RefactoringStatus.createFatalErrorStatus("Object is removed from model."); 
						
		if (c.getChildByPath(newName) != null)
			return RefactoringStatus.createFatalErrorStatus(MessageFormat.format("Object {0} already exists.", newName)); 
			
		return RefactoringStatus.create(Status.OK_STATUS);
	}
	
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm, CheckConditionsContext context) throws CoreException {
		pm.beginTask("", 1); //$NON-NLS-1$
		try{
			return new RefactoringStatus();
		} finally{
			pm.done();
		}	
	}
	
	public Change createChange(IProgressMonitor pm) throws CoreException {
		return null;
	}

	public void setUpdateReferences(boolean update) {
		updateReferences = update;
	}

	public boolean getUpdateReferences() {
		return updateReferences;
	}

}
