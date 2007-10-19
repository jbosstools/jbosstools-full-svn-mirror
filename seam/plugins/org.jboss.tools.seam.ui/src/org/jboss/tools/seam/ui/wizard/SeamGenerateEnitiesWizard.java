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
package org.jboss.tools.seam.ui.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.INewWizard;
import org.hibernate.eclipse.launch.HibernateLaunchConstants;
import org.jboss.tools.seam.core.SeamProjectsSet;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.core.project.facet.SeamRuntime;
import org.jboss.tools.seam.core.project.facet.SeamRuntimeManager;
import org.jboss.tools.seam.internal.core.project.facet.ISeamFacetDataModelProperties;
import org.jboss.tools.seam.ui.SeamUIMessages;

/**
 * Seam Generate Entities Wizard.
 * @author Alexey Kazakov
 */
public class SeamGenerateEnitiesWizard extends SeamBaseWizard implements INewWizard {

	public SeamGenerateEnitiesWizard() {
		super(GENERATE_SEAM_ENTITIES);
		setWindowTitle(SeamUIMessages.GENERATE_SEAM_ENTITIES_WIZARD_TITLE);
		setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(SeamGenerateEnitiesWizard.class, "SeamWebProjectWizBan.png"));
		addPage(new SeamGenerateEnitiesWizardPage());
	}

	public static final IUndoableOperation GENERATE_SEAM_ENTITIES = new SeamBaseOperation(SeamUIMessages.SEAM_GENERATE_ENTITIES_WIZARD_ACTION_CREATING_OPERATION) {

		@Override
		public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
			Map<String, String> params = (Map)info.getAdapter(Map.class);	
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(
					params.get(IParameter.SEAM_PROJECT_NAME));

			try {
				ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
				ILaunchConfigurationType launchConfigurationType = 
					launchManager.getLaunchConfigurationType("org.hibernate.eclipse.launch.CodeGenerationLaunchConfigurationType"); //$NON-NLS-1$
				// we put null in location to avoid it being inside the project by default since the launch config currenlty will be machine independent with respect to the paths. 
				ILaunchConfigurationWorkingCopy wc = 
					launchConfigurationType.newInstance(null, project.getName() + "-generate-entities"); //$NON-NLS-1$

				
				//Main
				wc.setAttribute(
					HibernateLaunchConstants.ATTR_CONSOLE_CONFIGURATION_NAME, 
					params.get(IParameter.HIBERNATE_CONFIGURATION_NAME));

				SeamProjectsSet seamProjectsSet = SeamProjectsSet.create(project);

				wc.setAttribute(HibernateLaunchConstants.ATTR_OUTPUT_DIR, 
						seamProjectsSet.getBeansFolder()==null?
								"":seamProjectsSet.getBeansFolder().getFullPath().toString()); //$NON-NLS-1$

				boolean isReverseEngineer = "true".equals(params.get(HibernateLaunchConstants.ATTR_REVERSE_ENGINEER)); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_REVERSE_ENGINEER, isReverseEngineer);

				if(isReverseEngineer) {
					wc.setAttribute(HibernateLaunchConstants.ATTR_PACKAGE_NAME, seamProjectsSet.getEntityPackage());
					wc.setAttribute(HibernateLaunchConstants.ATTR_PREFER_BASIC_COMPOSITE_IDS, true);
					wc.setAttribute(HibernateLaunchConstants.ATTR_AUTOMATIC_MANY_TO_MANY, true);
					wc.setAttribute(HibernateLaunchConstants.ATTR_AUTOMATIC_VERSIONING, true);
				}

				
				
				SeamRuntime seamRt = getRuntime(project);
				if(seamRt==null) {
					seamRt = getRuntime(project);
				}
				if(seamRt == null) {
					throw new CoreException(new Status(IStatus.ERROR, SeamCorePlugin.PLUGIN_ID, SeamUIMessages.SEAM_GENERATE_ENTITIES_WIZARD_CAN_NOT_FIND_SEAM_RUNTIME + project.getName()));
				}
				String seamTemplatesRoot = seamRt.getTemplatesDir();
				
				wc.setAttribute(HibernateLaunchConstants.ATTR_TEMPLATE_DIR, seamTemplatesRoot);
				wc.setAttribute(HibernateLaunchConstants.ATTR_USE_OWN_TEMPLATES, true);
								
				wc.setAttribute(HibernateLaunchConstants.ATTR_ENABLE_JDK5, true);
				wc.setAttribute(HibernateLaunchConstants.ATTR_ENABLE_EJB3_ANNOTATIONS, true);

				// Create exporters
				// TODO Add others exporters
				List<String> exporters = new ArrayList<String>();
				
				exporters.add("hbmtemplate0"); //$NON-NLS-1$
				exporters.add("hbmtemplate1"); //$NON-NLS-1$
				exporters.add("hbmtemplate2"); //$NON-NLS-1$
				exporters.add("hbmtemplate3"); //$NON-NLS-1$
				exporters.add("hbmtemplate4"); //$NON-NLS-1$
				exporters.add("hbmtemplate5"); //$NON-NLS-1$
				exporters.add("hbmtemplate6"); //$NON-NLS-1$
				exporters.add("hbmtemplate7"); //$NON-NLS-1$
				exporters.add("hbmtemplate8"); //$NON-NLS-1$
				exporters.add("hbmtemplate9"); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS, exporters);
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate0", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate0.extension_id", "org.hibernate.tools.hbm2java"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate1", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate1.extension_id", "org.hibernate.tools.hbmtemplate"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate2", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate2.extension_id", "org.hibernate.tools.hbmtemplate"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate3", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate3.extension_id", "org.hibernate.tools.hbmtemplate"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate4", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate4.extension_id", "org.hibernate.tools.hbmtemplate"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate5", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate5.extension_id", "org.hibernate.tools.hbmtemplate"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate6", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate6.extension_id", "org.hibernate.tools.hbmtemplate"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate7", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate7.extension_id", "org.hibernate.tools.hbmtemplate"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate8", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate8.extension_id", "org.hibernate.tools.hbmtemplate"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate9", true); //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate9.extension_id", "org.hibernate.tools.hbmtemplate"); //$NON-NLS-1$ //$NON-NLS-2$
				
				Map<String, String> hbmtemplateAttributes = new HashMap<String, String>();

//	        	<hbmtemplate filepattern="{class-name}List.xhtml"
//	                template="view/list.xhtml.ftl" 
//		             destdir="${project.home}/view"
//	                 foreach="entity"/>

				hbmtemplateAttributes.put("file_pattern", "{class-name}List.xhtml"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("template_name", "view/list.xhtml.ftl"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("outputdir", seamProjectsSet.getViewsFolder().getFullPath().toString()); //$NON-NLS-1$
				hbmtemplateAttributes.put("for_each", "entity");				 //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("hibernatetool.util.toolclass",SeamUIMessages.SEAM_GENERATE_ENTITIES_WIZARD_54);	 //$NON-NLS-1$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate1.properties", hbmtemplateAttributes); //$NON-NLS-1$

//				<hbmtemplate filepattern="{class-name}.xhtml"
//	                template="view/view.xhtml.ftl" 
//		             destdir="${project.home}/view"
//                     foreach="entity"/>

				hbmtemplateAttributes = new HashMap<String, String>();
				hbmtemplateAttributes.put("file_pattern", "{class-name}.xhtml"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("template_name", "view/view.xhtml.ftl"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("outputdir",seamProjectsSet.getViewsFolder().getFullPath().toString()); //$NON-NLS-1$
				hbmtemplateAttributes.put("for_each", "entity"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("hibernatetool.util.toolclass","org.hibernate.eclipse.launch.SeamUtil"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate2.properties", hbmtemplateAttributes); //$NON-NLS-1$

//				<hbmtemplate filepattern="{class-name}.page.xml"
//	                template="view/view.page.xml.ftl" 
//		             destdir="${project.home}/view"
//                     foreach="entity"/>

				hbmtemplateAttributes = new HashMap<String, String>();
				hbmtemplateAttributes.put("file_pattern", "{class-name}.page.xml"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("template_name", "view/view.page.xml.ftl"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("outputdir",seamProjectsSet.getViewsFolder().getFullPath().toString()); //$NON-NLS-1$
				hbmtemplateAttributes.put("for_each", "entity"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("hibernatetool.util.toolclass","org.hibernate.eclipse.launch.SeamUtil"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate3.properties", hbmtemplateAttributes); //$NON-NLS-1$
				
//				<hbmtemplate filepattern="{class-name}Edit.xhtml"
//	                template="view/edit.xhtml.ftl" 
//		             destdir="${project.home}/view"
//                     foreach="entity"/>

				hbmtemplateAttributes = new HashMap<String, String>();
				hbmtemplateAttributes.put("file_pattern", "{class-name}Edit.xhtml"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("template_name", "view/edit.xhtml.ftl"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("outputdir",seamProjectsSet.getViewsFolder().getFullPath().toString()); //$NON-NLS-1$
				hbmtemplateAttributes.put("for_each", "entity"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("hibernatetool.util.toolclass","org.hibernate.eclipse.launch.SeamUtil"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate4.properties", hbmtemplateAttributes); //$NON-NLS-1$

//				<hbmtemplate filepattern="{class-name}Edit.page.xml"
//	                template="view/edit.page.xml.ftl" 
//		             destdir="${project.home}/view"
//                     foreach="entity"/>
				
				
				hbmtemplateAttributes = new HashMap<String, String>();
				hbmtemplateAttributes.put("file_pattern", "{class-name}Edit.page.xml"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("template_name", "view/edit.page.xml.ftl"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("outputdir",seamProjectsSet.getViewsFolder().getFullPath().toString()); //$NON-NLS-1$
				hbmtemplateAttributes.put("for_each", "entity"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("hibernatetool.util.toolclass","org.hibernate.eclipse.launch.SeamUtil"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate5.properties", hbmtemplateAttributes); //$NON-NLS-1$

//				<hbmtemplate filepattern="{package-name}/{class-name}List.java"
//	                template="src/EntityList.java.ftl" 
//		             destdir="${project.home}/src"
//                     foreach="entity"/>

				hbmtemplateAttributes = new HashMap<String, String>();
				hbmtemplateAttributes.put("file_pattern", "{package-name}/{class-name}List.java"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("template_name", "src/EntityList.java.ftl"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("outputdir",seamProjectsSet.getActionsFolder().getFullPath().toString()); //$NON-NLS-1$
				hbmtemplateAttributes.put("for_each", "entity"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("hibernatetool.util.toolclass","org.hibernate.eclipse.launch.SeamUtil"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate6.properties", hbmtemplateAttributes); //$NON-NLS-1$

//				<hbmtemplate filepattern="{class-name}List.page.xml"
//	                template="view/list.page.xml.ftl" 
//		             destdir="${project.home}/view"
//                     foreach="entity"/>

				
				hbmtemplateAttributes = new HashMap<String, String>();
				hbmtemplateAttributes.put("file_pattern", "{class-name}List.page.xml"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("template_name", "view/list.page.xml.ftl"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("outputdir",seamProjectsSet.getViewsFolder().getFullPath().toString()); //$NON-NLS-1$
				hbmtemplateAttributes.put("for_each", "entity"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("hibernatetool.util.toolclass","org.hibernate.eclipse.launch.SeamUtil"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate7.properties", hbmtemplateAttributes); //$NON-NLS-1$
				
//				<hbmtemplate filepattern="{package-name}/{class-name}Home.java"
//	                template="src/EntityHome.java.ftl" 
//		             destdir="${project.home}/src"
//                     foreach="entity"/>

				hbmtemplateAttributes = new HashMap<String, String>();
				hbmtemplateAttributes.put("file_pattern", "{package-name}/{class-name}Home.java"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("template_name", "src/EntityHome.java.ftl"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("outputdir",seamProjectsSet.getActionsFolder().getFullPath().toString()); //$NON-NLS-1$
				hbmtemplateAttributes.put("for_each", "entity"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("hibernatetool.util.toolclass","org.hibernate.eclipse.launch.SeamUtil"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate8.properties", hbmtemplateAttributes); //$NON-NLS-1$
				
//				<hbmtemplate filepattern="menu.xhtml"
//	                template="view/layout/menu.xhtml.ftl" 
//		             destdir="${project.home}/view/layout"
//                     foreach="entity"/>

				hbmtemplateAttributes = new HashMap<String, String>();
				hbmtemplateAttributes.put("file_pattern", "menu.xhtml"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("template_name", "view/layout/menu.xhtml.ftl"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("outputdir",seamProjectsSet.getViewsFolder().getFullPath().toString()+"/layout"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("for_each", "entity"); //$NON-NLS-1$ //$NON-NLS-2$
				hbmtemplateAttributes.put("hibernatetool.util.toolclass","org.hibernate.eclipse.launch.SeamUtil"); //$NON-NLS-1$ //$NON-NLS-2$
				wc.setAttribute(HibernateLaunchConstants.ATTR_EXPORTERS + ".hbmtemplate9.properties", hbmtemplateAttributes); //$NON-NLS-1$
				wc.doSave();
				launchManager.addLaunch(wc.launch(ILaunchManager.RUN_MODE, monitor));
			} catch (CoreException e) {
				SeamCorePlugin.getDefault().showError(SeamUIMessages.SEAM_GENERATE_ENTITIES_WIZARD_CAN_NOT_GENERATE_SEAM_ENTITIES, e);
			}
			return Status.OK_STATUS;
		}

		@Override
		public List<String[]> getFileMappings(Map<String, Object> vars) {
			throw new UnsupportedOperationException(SeamUIMessages.SEAM_GENERATE_ENTITIES_WIZARD_THIS_METHOD_IS_NOT_RELEVANT_IN_GENERATING_SEAM_ENTITIES);
		}
	};

	static SeamRuntime getRuntime(IProject project) {
		if(project==null) {
			return null;
		}
		SeamRuntime seamRt = SeamRuntimeManager.getInstance().getDefaultRuntime();

		String runtimeName = SeamCorePlugin.getSeamPreferences(project)
			.get(ISeamFacetDataModelProperties.SEAM_RUNTIME_NAME,""); //$NON-NLS-1$

		if(!"".equals(runtimeName)) { //$NON-NLS-1$
			seamRt = SeamRuntimeManager.getInstance().findRuntimeByName(runtimeName);
		}
		return seamRt;
	}
}