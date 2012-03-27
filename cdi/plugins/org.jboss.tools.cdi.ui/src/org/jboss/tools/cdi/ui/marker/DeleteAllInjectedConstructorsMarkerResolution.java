/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.ui.marker;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.Signature;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
import org.eclipse.ltk.core.refactoring.participants.RefactoringProcessor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution2;
import org.jboss.tools.cdi.core.CDIImages;
import org.jboss.tools.cdi.internal.core.refactoring.DeleteAllInjectedConstructorsProcessor;
import org.jboss.tools.cdi.ui.CDIUIMessages;
import org.jboss.tools.cdi.ui.CDIUIPlugin;
import org.jboss.tools.cdi.ui.wizard.DeletePreviewWizard;
import org.jboss.tools.common.refactoring.MarkerResolutionUtils;
import org.jboss.tools.common.refactoring.TestableResolutionWithRefactoringProcessor;

/**
 * @author Daniel Azarov
 */
public class DeleteAllInjectedConstructorsMarkerResolution implements IMarkerResolution2, TestableResolutionWithRefactoringProcessor {
	private String label;
	private IMethod method;
	private IFile file;
	private String description;
	
	public DeleteAllInjectedConstructorsMarkerResolution(IMethod method, IFile file){
		StringBuffer buffer = new StringBuffer();
		buffer.append(method.getElementName()+"(");
		String[] types = method.getParameterTypes();
		for(int i = 0; i < types.length; i++){
			if(i > 0)
				buffer.append(", ");
			buffer.append(Signature.getSignatureSimpleName(types[i]));
		}
		buffer.append(")");
		this.label = MessageFormat.format(CDIUIMessages.DELETE_ALL_INJECTED_CONSTRUCTORS_MARKER_RESOLUTION_TITLE, new Object[]{buffer.toString()});
		this.method = method;
		this.file = file;
		description = getPreview();
	}

	@Override
	public String getLabel() {
		return label;
	}
	
	private String getPreview(){
		RefactoringProcessor processor = getRefactoringProcessor();
		RefactoringStatus status;
		try {
			status = processor.checkInitialConditions(new NullProgressMonitor());
		
		
			if(status.getEntryMatchingSeverity(RefactoringStatus.FATAL) != null){
				return label;
			}

			status = processor.checkFinalConditions(new NullProgressMonitor(), null);

			if(status.getEntryMatchingSeverity(RefactoringStatus.FATAL) != null){
				return label;
			}

			CompositeChange rootChange = (CompositeChange)processor.createChange(new NullProgressMonitor());
		
			return MarkerResolutionUtils.getPreview(rootChange);
		} catch (OperationCanceledException e) {
			CDIUIPlugin.getDefault().logError(e);
		} catch (CoreException e) {
			CDIUIPlugin.getDefault().logError(e);
		}
		return label;
	}
	
	@Override
	public void run(IMarker marker) {
		DeleteAllInjectedConstructorsProcessor processor = new DeleteAllInjectedConstructorsProcessor(file, method, label);
		ProcessorBasedRefactoring refactoring = new ProcessorBasedRefactoring(processor);
		DeletePreviewWizard wizard = new DeletePreviewWizard(refactoring);
		wizard.showWizard();
	}
	
	@Override
	public RefactoringProcessor getRefactoringProcessor(){
		return new DeleteAllInjectedConstructorsProcessor(file, method, label);
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Image getImage() {
		return CDIImages.QUICKFIX_REMOVE;
	}

}