package org.jboss.tools.smooks.javabean.ui;

import org.eclipse.osgi.util.NLS;

public final class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.jboss.tools.smooks.javabean.ui.messages";//$NON-NLS-1$

	private Messages() {
		// Do not instantiate
	}

	public static String JavaBeanConfigWizardPage_JavaBeanSelectionDialogDesc;
	public static String JavaBeanConfigWizardPage_JavaBeanSelectionDialogErrorMsg;
	public static String JavaBeanConfigWizardPage_JavaBeanSelectionDialogTitle;
//	public static String JavaBeanModelLoadComposite_Browse;
	public static String JavaBeanModelLoadComposite_ClassNameText;
	public static String JavaBeanModelLoadComposite_InitRunnableContextException;
	public static String JavaBeanModelLoadComposite_SearchJavaType;
	public static String JavaBeanModelLoadComposite_SourceJavaBean;
	public static String JavaBeanPropertiesSection_Browse;
	public static String JavaBeanPropertiesSection_ClassBrowse;
	public static String JavaBeanPropertiesSection_CustomTypeBrowse;
	public static String JavaBeanPropertiesSection_ErrorMessageTitle;
	public static String JavaBeanPropertiesSection_JavaBeanProperties;
	public static String JavaBeanPropertiesSection_JavaType;
	public static String JavaBeanPropertiesSection_MappingType;
	public static String JavaBeanPropertiesSection_SearchJavaType;
	public static String JavaBeanPropertiesSection_TargetInstanceClass;
	public static String JavaBeanPropertiesSection_TypeDialogErrorMessage;
	public static String JavaBeanPropertiesSection_TypePropertyName;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}