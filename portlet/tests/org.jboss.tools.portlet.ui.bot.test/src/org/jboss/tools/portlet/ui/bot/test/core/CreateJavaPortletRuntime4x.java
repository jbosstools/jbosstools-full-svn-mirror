package org.jboss.tools.portlet.ui.bot.test.core;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.test.template.CreateJavaPortletTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;

/**
 * Creates a new java portlet and checks if the right files are generated.  
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(clearWorkspace=false, clearProjects=false, server=@Server(version="4.3", state=ServerState.Present))
public class CreateJavaPortletRuntime4x extends CreateJavaPortletTemplate {

	@Override
	protected List<String> getExpectedFiles() {
		return Arrays.asList(
				DEFAULT_OBJECTS_XML,
				PORTLET_INSTANCES_XML,
				CLASS_FILE);
	}
	
	@Override
	protected List<String> getNonExpectedFiles() {
		return Arrays.asList(
				JSF_FOLDER,
				JBOSS_APP_XML,
				JBOSS_PORTLET_XML);
	}
}
