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
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.ui.PortletUIActivator;

/**
 * New portlet wizard
 */
public class NewPortletWizard extends NewWebArtifactWizard {
	
	protected static final String PAGE_FOUR = "pageFour"; //$NON-NLS-1$
	
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
		return PortletUIActivator.imageDescriptorFromPlugin(PortletUIActivator.PLUGIN_ID, "/icons/portlet_wiz.gif"); //$NON-NLS-1$
	}
	
	@Override
	public void doAddPages() {
		NewPortletClassWizardPage page1 = new NewPortletClassWizardPage(
				getDataModel(), 
				PAGE_ONE,
				NEW_JAVA_CLASS_DESTINATION_WIZARD_PAGE_DESC,
				IPortletUIConstants.NEW_PORTLET_WIZARD_PAGE_TITLE, IModuleConstants.JST_WEB_MODULE);
		//page1.setInfopopID(IWebUIContextIds.WEBEDITOR_SERVLET_PAGE_ADD_SERVLET_WIZARD_1);
		addPage(page1);
		
		NewPortletClassOptionsWizardPage page2 = new NewPortletClassOptionsWizardPage(
				getDataModel(), 
				PAGE_TWO,
				Messages.NewPortletWizard_Specify_modifiers_interfaces_to_implement_and_method_stubs_to_generate,
				IPortletUIConstants.NEW_PORTLET_WIZARD_PAGE_TITLE);
		//page3.setInfopopID(IWebUIContextIds.WEBEDITOR_SERVLET_PAGE_ADD_SERVLET_WIZARD_3);
		addPage(page2);
		AddPortletWizardPage page3 = new AddPortletWizardPage(getDataModel(), PAGE_THREE);
		//page2.setInfopopID(IWebUIContextIds.WEBEDITOR_SERVLET_PAGE_ADD_SERVLET_WIZARD_2);
		addPage(page3);
		AddJBossPortletWizardPage page4 = new AddJBossPortletWizardPage(getDataModel(), PAGE_FOUR);
		addPage(page4);
	}
	
	@Override
	protected void postPerformFinish() throws InvocationTargetException {
		// open new portlet class in java editor
		openJavaClass();
	}

	@Override
	protected IDataModelProvider getDefaultProvider() {
		return new NewPortletClassDataModelProvider();
	}
	
}
