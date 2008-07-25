package org.jboss.tools.portlet.ui.internal.wizard;

import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.NEW_JAVA_CLASS_DESTINATION_WIZARD_PAGE_DESC;
import static org.eclipse.jst.servlet.ui.internal.wizard.IWebWizardConstants.NEW_JAVA_CLASS_OPTIONS_WIZARD_PAGE_DESC;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jst.servlet.ui.IWebUIContextIds;
import org.eclipse.jst.servlet.ui.internal.wizard.NewServletClassOptionsWizardPage;
import org.eclipse.jst.servlet.ui.internal.wizard.NewWebArtifactWizard;
import org.eclipse.wst.common.componentcore.internal.util.IModuleConstants;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelProvider;
import org.jboss.tools.portlet.ui.IPortletUIConstants;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 * New portlet wizard
 */
public class NewPortletWizard extends NewWebArtifactWizard {
	
	protected static final String PAGE_FOUR = "pageFour";
	
	public NewPortletWizard() {
	    this(null);
	}
	
	public NewPortletWizard(IDataModel model) {
		super(model);
	}

	@Override
	protected String getTitle() {
		return IPortletUIConstants.NEW_PORTLET_WIZARD_PAGE_TITLE;
	}

	@Override
	protected ImageDescriptor getImage() {
		return PortletUIActivator.imageDescriptorFromPlugin(PortletUIActivator.PLUGIN_ID, "/icons/portlet_wiz.gif");
	}
	
	@Override
	public void doAddPages() {
		NewPortletClassWizardPage page1 = new NewPortletClassWizardPage(
				getDataModel(), 
				PAGE_ONE,
				NEW_JAVA_CLASS_DESTINATION_WIZARD_PAGE_DESC,
				IPortletUIConstants.NEW_PORTLET_WIZARD_PAGE_TITLE, IModuleConstants.JST_WEB_MODULE);
		page1.setInfopopID(IWebUIContextIds.WEBEDITOR_SERVLET_PAGE_ADD_SERVLET_WIZARD_1);
		addPage(page1);
		AddPortletWizardPage page2 = new AddPortletWizardPage(getDataModel(), PAGE_TWO);
		page2.setInfopopID(IWebUIContextIds.WEBEDITOR_SERVLET_PAGE_ADD_SERVLET_WIZARD_2);
		addPage(page2);
		NewPortletClassOptionsWizardPage page3 = new NewPortletClassOptionsWizardPage(
				getDataModel(), 
				PAGE_THREE,
				"Specify modifiers, interfaces to implement and method stubs to generate.",
				IPortletUIConstants.NEW_PORTLET_WIZARD_PAGE_TITLE);
		page3.setInfopopID(IWebUIContextIds.WEBEDITOR_SERVLET_PAGE_ADD_SERVLET_WIZARD_3);
		addPage(page3);
		AddJBossPortletWizardPage page4 = new AddJBossPortletWizardPage(getDataModel(), PAGE_FOUR);
		addPage(page4);
	}
	
	@Override
	protected void postPerformFinish() throws InvocationTargetException {
		// open new servlet class in java editor
		openJavaClass();
	}

	@Override
	protected IDataModelProvider getDefaultProvider() {
		return new NewPortletClassDataModelProvider();
	}
	
}
