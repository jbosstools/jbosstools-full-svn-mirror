package org.jboss.tools.portlet.ui.bot.test.core;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageDefaultsFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.WizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.task.wizard.web.jboss.JBossPortletCapabilitiesWizardPageFillingTask;
import org.jboss.tools.portlet.ui.bot.test.entity.FacetDefinition;
import org.jboss.tools.portlet.ui.bot.test.template.CreatePortletProjectTemplate;

/**
 * Creates a new Dynamic Web Project with the specific JBoss Core Portlet facet. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class CreateJavaPortletProject extends CreatePortletProjectTemplate {

	public static final String PROJECT_NAME = "java-portlet";
	
	@Override
	public String getProjectName() {
		return PROJECT_NAME;
	}
	
	@Override
	public List<FacetDefinition> getRequiredFacets() {
		List<FacetDefinition> facets = new ArrayList<FacetDefinition>();
		facets.add(JAVA_FACET);
		facets.add(CORE_PORTLET_FACET);
		return facets;
	}
	
	@Override
	public List<WizardPageFillingTask> getAdditionalWizardPages() {
		List<WizardPageFillingTask> tasks = new ArrayList<WizardPageFillingTask>();
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(new WizardPageDefaultsFillingTask());
		tasks.add(new JBossPortletCapabilitiesWizardPageFillingTask(JBossPortletCapabilitiesWizardPageFillingTask.Type.RUNTIME_PROVIDER));
		return tasks;
	}
}
