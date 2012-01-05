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
package org.jboss.tools.jst.web.kb.refactoring;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.ui.search.ElementQuerySpecification;
import org.eclipse.jdt.ui.search.IMatchPresentation;
import org.eclipse.jdt.ui.search.IQueryParticipant;
import org.eclipse.jdt.ui.search.ISearchRequestor;
import org.eclipse.jdt.ui.search.QuerySpecification;
import org.eclipse.search.ui.text.Match;
import org.jboss.tools.common.model.project.ProjectHome;
import org.jboss.tools.jst.web.kb.refactoring.ELProjectSetExtension;
import org.jboss.tools.jst.web.kb.refactoring.IProjectsSet;
import org.jboss.tools.jst.web.kb.refactoring.RefactorSearcher;

public class ELReferencesQueryParticipant implements IQueryParticipant{
	private ELSearcher searcher;
	
	public int estimateTicks(QuerySpecification specification) {
		return 10;
	}

	public IMatchPresentation getUIParticipant() {
		return null;
	}

	public void search(ISearchRequestor requestor,
			QuerySpecification querySpecification, IProgressMonitor monitor)
			throws CoreException {
		
		if(querySpecification instanceof ElementQuerySpecification && isSearchForReferences(querySpecification.getLimitTo())){

			IJavaElement element = ((ElementQuerySpecification)querySpecification).getElement();
			if(element instanceof IField || element instanceof IMethod || element instanceof IType){
				IFile file = (IFile)element.getResource();
				if(file != null) {
					String name = element.getElementName();
					searcher = new ELSearcher(requestor, element, file, name);
					searcher.setSearchScope(querySpecification.getScope());
					searcher.findELReferences();
				}
			}
		}
	}
	
	public boolean isSearchForReferences(int limitTo) {
		int maskedLimitTo = limitTo
				& ~(IJavaSearchConstants.IGNORE_DECLARING_TYPE + IJavaSearchConstants.IGNORE_RETURN_TYPE);
		return maskedLimitTo == IJavaSearchConstants.REFERENCES || maskedLimitTo == IJavaSearchConstants.ALL_OCCURRENCES;	
	}

	class ELSearcher extends RefactorSearcher{
		ISearchRequestor requestor;
		IProjectsSet projectSet=null;
		
		public ELSearcher(ISearchRequestor requestor, IJavaElement element, IFile file, String name){
			super(file, name, element);
			this.requestor = requestor;
			ELProjectSetExtension[] extensions = 	ELProjectSetExtension.getInstances();
			if(extensions.length > 0){
				projectSet = extensions[0].getProjectSet();
				if(projectSet != null)
					projectSet.init(file.getProject());
			}
		}
		
		protected void outOfSynch(IProject file){
			// do nothing
		}

		@Override
		protected void match(IFile file, int offset, int length) {
			Match match = new Match(file, offset, length);
			requestor.reportMatch(match);
		}
		
		protected IProject[] getProjects(){
			if(projectSet != null){
				return projectSet.getLinkedProjects();
			}
			return new IProject[]{baseFile.getProject()};
		}
		
		protected IContainer getViewFolder(IProject project){
			if(projectSet != null){
				return projectSet.getViewFolder(project);
			}
			
			IPath path = ProjectHome.getFirstWebContentPath(project);
			
			if(path != null)
				return project.getFolder(path.removeFirstSegments(1));
			
			return null;
		}
	}
}
