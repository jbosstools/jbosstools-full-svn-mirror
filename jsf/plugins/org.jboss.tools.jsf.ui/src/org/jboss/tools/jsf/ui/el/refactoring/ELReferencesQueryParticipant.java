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
package org.jboss.tools.jsf.ui.el.refactoring;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.jboss.tools.jsf.el.refactoring.ELProjectSetExtension;
import org.jboss.tools.jsf.el.refactoring.ProjectsSet;
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
		
		if(querySpecification instanceof ElementQuerySpecification){
			if (!isSearchForReferences(querySpecification.getLimitTo()))
				return;
			
			ElementQuerySpecification qs = (ElementQuerySpecification)querySpecification;
			if(qs.getElement() instanceof IMethod || qs.getElement() instanceof IType){
				IFile file = (IFile)qs.getElement().getResource();
				if(file == null)
					return;
				
				String name = qs.getElement().getElementName();
				
				searcher = new ELSearcher(requestor, qs.getElement(), file, name);
				searcher.setSearchScope(qs.getScope());
				
				searcher.findELReferences();
			}
		}
	}
	
	public boolean isSearchForReferences(int limitTo) {
    	int maskedLimitTo = limitTo & ~(IJavaSearchConstants.IGNORE_DECLARING_TYPE+IJavaSearchConstants.IGNORE_RETURN_TYPE);
    	if (maskedLimitTo == IJavaSearchConstants.REFERENCES || maskedLimitTo == IJavaSearchConstants.ALL_OCCURRENCES) {
    		return true;
    	}
    
    	return false;
    }
	
	class ELSearcher extends RefactorSearcher{
		ISearchRequestor requestor;
		ProjectsSet projectSet=null;
		
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
			
			IPath path = ProjectHome.getFirstWebContentPath(baseFile.getProject());
			
			if(path != null)
				return project.getFolder(path.removeFirstSegments(1));
			
			return null;
		}
	}
}
