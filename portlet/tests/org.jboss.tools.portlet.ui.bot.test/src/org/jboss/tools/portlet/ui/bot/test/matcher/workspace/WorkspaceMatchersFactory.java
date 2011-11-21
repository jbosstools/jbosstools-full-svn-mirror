package org.jboss.tools.portlet.ui.bot.test.matcher.workspace;

import org.jboss.tools.portlet.ui.bot.test.matcher.SWTMatcher;
import org.jboss.tools.portlet.ui.bot.test.task.facet.FacetDefinition;

/**
 * Factory for workspace specific matchers (projects, files..) 
 * 
 * @author Lucia Jelinkova
 *
 */
public class WorkspaceMatchersFactory {

	private WorkspaceMatchersFactory(){
		// not to be instantiated
	}
	
	public static SWTMatcher<String> isExistingProject(){
		return new ExistingProjectMatcher();
	}
	
	public static SWTMatcher<String> existsInProject(String project){
		return new ExistingFileMatcher(project);
	}
	
	public static SWTMatcher<String> hasFacets(FacetDefinition... facets){
		return new ProjectFacetsMatcher(facets);
	}
}
