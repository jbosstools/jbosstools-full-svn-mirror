package org.jboss.tools.portlet.ui.internal.wizard;

import static org.eclipse.jst.j2ee.internal.common.operations.INewJavaClassDataModelProperties.PROJECT;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.NEW_JAVA_CLASS_DESTINATION_WIZARD_PAGE_DESC;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.NEW_JAVA_CLASS_OPTIONS_WIZARD_PAGE_DESC;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jst.servlet.ui.IWebUIContextIds;
import org.eclipse.jst.servlet.ui.internal.wizard.NewServletClassOptionsWizardPage;
import org.eclipse.jst.servlet.ui.internal.wizard.NewWebArtifactWizard;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelProvider;
import org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties;
import org.jboss.tools.portlet.ui.IPortletUIConstants;
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 * New portlet wizard
 */
public class NewSeamPortletWizard extends NewWebArtifactWizard {
	
	public NewSeamPortletWizard() {
	    this(null);
	}
	
	public NewSeamPortletWizard(IDataModel model) {
		super(model);
	}

	@Override
	protected String getTitle() {
		return IPortletUIConstants.NEW_PORTLET_WIZARD_PAGE_TITLE;
	}

	@Override
	protected ImageDescriptor getImage() {
		return PortletUIActivator.imageDescriptorFromPlugin(PortletUIActivator.PLUGIN_ID, "/icons/portlet_wiz.gif"); //$NON-NLS-1$
	}
	
	@Override
	public void doAddPages() {
		NewJSFPortletClassWizardPage page1 = new NewJSFPortletClassWizardPage(
				getDataModel(), 
				PAGE_ONE,NEW_JAVA_CLASS_DESTINATION_WIZARD_PAGE_DESC,
				IPortletUIConstants.NEW_PORTLET_WIZARD_PAGE_TITLE, IModuleConstants.JST_WEB_MODULE);
		//page1.setInfopopID(IWebUIContextIds.WEBEDITOR_SERVLET_PAGE_ADD_SERVLET_WIZARD_1);
		addPage(page1);
		AddPortletWizardPage page2 = new AddPortletWizardPage(getDataModel(), PAGE_TWO);
		//page2.setInfopopID(IWebUIContextIds.WEBEDITOR_SERVLET_PAGE_ADD_SERVLET_WIZARD_2);
		addPage(page2);
		AddJBossPortletWizardPage page3 = new AddJBossPortletWizardPage(getDataModel(), PAGE_THREE);
		addPage(page3);
	}
	
	@Override
	protected void postPerformFinish() throws InvocationTargetException {
		// open the portlet.xml file
		IProject project = (IProject) getDataModel().getProperty(PROJECT);
		IFile portletFile = PortletUIActivator.getPortletXmlFile(project);
		if (portletFile == null) {
			return;
		}
		openEditor(portletFile);
	}

	@Override
	protected IDataModelProvider getDefaultProvider() {
		return new NewPortletClassDataModelProvider(false,true);
	}
	
}
