/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.smooks.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.jboss.tools.smooks.model.calc.CalcFactory;
import org.jboss.tools.smooks.model.calc.CalcPackage;
import org.jboss.tools.smooks.model.common.provider.AbstractAnyTypeItemProvider;
import org.jboss.tools.smooks.model.csv.CsvFactory;
import org.jboss.tools.smooks.model.csv.CsvPackage;
import org.jboss.tools.smooks.model.csv12.Csv12Factory;
import org.jboss.tools.smooks.model.csv12.Csv12Package;
import org.jboss.tools.smooks.model.datasource.DatasourceFactory;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.dbrouting.DbroutingFactory;
import org.jboss.tools.smooks.model.dbrouting.DbroutingPackage;
import org.jboss.tools.smooks.model.edi.EdiFactory;
import org.jboss.tools.smooks.model.edi.EdiPackage;
import org.jboss.tools.smooks.model.edi12.Edi12Factory;
import org.jboss.tools.smooks.model.edi12.Edi12Package;
import org.jboss.tools.smooks.model.esbrouting.EsbroutingFactory;
import org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage;
import org.jboss.tools.smooks.model.fileRouting.FileRoutingFactory;
import org.jboss.tools.smooks.model.fileRouting.FileRoutingPackage;
import org.jboss.tools.smooks.model.freemarker.FreemarkerFactory;
import org.jboss.tools.smooks.model.freemarker.FreemarkerPackage;
import org.jboss.tools.smooks.model.groovy.GroovyFactory;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;
import org.jboss.tools.smooks.model.iorouting.IoroutingFactory;
import org.jboss.tools.smooks.model.iorouting.IoroutingPackage;
import org.jboss.tools.smooks.model.javabean.JavabeanFactory;
import org.jboss.tools.smooks.model.javabean.JavabeanPackage;
import org.jboss.tools.smooks.model.javabean12.Javabean12Factory;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingFactory;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Factory;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package;
import org.jboss.tools.smooks.model.json.JsonFactory;
import org.jboss.tools.smooks.model.json.JsonPackage;
import org.jboss.tools.smooks.model.json12.Json12Factory;
import org.jboss.tools.smooks.model.json12.Json12Package;
import org.jboss.tools.smooks.model.persistence12.Persistence12Factory;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.rules10.Rules10Factory;
import org.jboss.tools.smooks.model.rules10.Rules10Package;
import org.jboss.tools.smooks.model.smooks.SmooksFactory;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks.model.validation10.Validation10Factory;
import org.jboss.tools.smooks.model.validation10.Validation10Package;
import org.jboss.tools.smooks.model.xsl.XslFactory;
import org.jboss.tools.smooks.model.xsl.XslPackage;
import org.jboss.tools.smooks.model.xsl.provider.Xsl1EditPlugin;

/**
 * This is the item provider adapter for a {@link smooks.SmooksResourceListType}
 * object. <!-- begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class SmooksResourceListTypeItemProvider extends AbstractAnyTypeItemProvider implements
		IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public SmooksResourceListTypeItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addDefaultConditionRefPropertyDescriptor(object);
			addDefaultSelectorPropertyDescriptor(object);
			addDefaultSelectorNamespacePropertyDescriptor(object);
			addDefaultTargetProfilePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Default Condition Ref feature.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDefaultConditionRefPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory)
				.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_SmooksResourceListType_defaultConditionRef_feature"), getString(
						"_UI_PropertyDescriptor_description", "_UI_SmooksResourceListType_defaultConditionRef_feature",
						"_UI_SmooksResourceListType_type"),
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_CONDITION_REF, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Default Selector feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDefaultSelectorPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory)
				.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_SmooksResourceListType_defaultSelector_feature"), getString(
						"_UI_PropertyDescriptor_description", "_UI_SmooksResourceListType_defaultSelector_feature",
						"_UI_SmooksResourceListType_type"),
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Default Selector Namespace
	 * feature. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDefaultSelectorNamespacePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory)
				.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_SmooksResourceListType_defaultSelectorNamespace_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_SmooksResourceListType_defaultSelectorNamespace_feature",
						"_UI_SmooksResourceListType_type"),
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Default Target Profile feature.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDefaultTargetProfilePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory)
				.getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_SmooksResourceListType_defaultTargetProfile_feature"), getString(
						"_UI_PropertyDescriptor_description",
						"_UI_SmooksResourceListType_defaultTargetProfile_feature", "_UI_SmooksResourceListType_type"),
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE, true, false, false,
				ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to
	 * deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand},
	 * {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in
	 * {@link #createCommand}. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__PARAMS);
			childrenFeatures.add(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__CONDITIONS);
			childrenFeatures.add(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__PROFILES);
			childrenFeatures.add(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP);
			childrenFeatures.add(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper
		// feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns SmooksResourceListType.gif. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SmooksResourceListType"));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SmooksResourceListType) object).getDefaultSelectorNamespace();
		return label == null || label.length() == 0 ? getString("_UI_SmooksResourceListType_type")
				: getString("_UI_SmooksResourceListType_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to
	 * update any cached children and by creating a viewer notification, which
	 * it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(SmooksResourceListType.class)) {
		case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_CONDITION_REF:
		case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR:
		case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE:
		case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PARAMS:
		case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__CONDITIONS:
		case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES:
		case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP:
		case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
			return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing the children that can be created under this object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add(createChildParameter(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__PARAMS,
				SmooksFactory.eINSTANCE.createParamsType()));

		newChildDescriptors.add(createChildParameter(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__CONDITIONS,
				SmooksFactory.eINSTANCE.createConditionsType()));

		newChildDescriptors.add(createChildParameter(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__PROFILES,
				SmooksFactory.eINSTANCE.createProfilesType()));

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, FeatureMapUtil.createEntry(
						SmooksPackage.Literals.DOCUMENT_ROOT__READER, SmooksFactory.eINSTANCE.createReaderType())));

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(SmooksPackage.Literals.DOCUMENT_ROOT__IMPORT, SmooksFactory.eINSTANCE
								.createImportType())));

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(SmooksPackage.Literals.DOCUMENT_ROOT__RESOURCE_CONFIG, SmooksFactory.eINSTANCE
								.createResourceConfigType())));
		/***********************************************/
		// for CSV
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, FeatureMapUtil.createEntry(
						CsvPackage.Literals.CSV_DOCUMENT_ROOT__READER, CsvFactory.eINSTANCE.createCsvReader())));
		// for EDI
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, FeatureMapUtil.createEntry(
						EdiPackage.Literals.EDI_DOCUMENT_ROOT__READER, EdiFactory.eINSTANCE.createEDIReader())));

		// for JSON
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, FeatureMapUtil.createEntry(
						JsonPackage.Literals.JSON_DOCUMENT_ROOT__READER, JsonFactory.eINSTANCE.createJsonReader())));

		// for calc
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(CalcPackage.Literals.CALC_DOCUMENT_ROOT__COUNTER, CalcFactory.eINSTANCE
								.createCounter())));

		// for groovy

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(GroovyPackage.Literals.DOCUMENT_ROOT__GROOVY, GroovyFactory.eINSTANCE
								.createGroovy())));

		// for datasource
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(DatasourcePackage.Literals.DATA_SOURCE_DOCUMENT_ROOT__DIRECT,
								DatasourceFactory.eINSTANCE.createDirect())));

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(DatasourcePackage.Literals.DATA_SOURCE_DOCUMENT_ROOT__JNDI,
								DatasourceFactory.eINSTANCE.createDataSourceJndi())));

		// for javabean
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(JavabeanPackage.Literals.DOCUMENT_ROOT__BINDINGS, JavabeanFactory.eINSTANCE
								.createBindingsType())));
		// for xsl
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(XslPackage.Literals.DOCUMENT_ROOT__XSL, XslFactory.eINSTANCE.createXsl())));
		// for freemarker
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(FreemarkerPackage.Literals.DOCUMENT_ROOT__FREEMARKER, FreemarkerFactory.eINSTANCE
								.createFreemarker())));

		// for DB routing
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(DbroutingPackage.Literals.DOCUMENT_ROOT__EXECUTOR, DbroutingFactory.eINSTANCE
								.createExecutor())));

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(DbroutingPackage.Literals.DOCUMENT_ROOT__RESULT_SET_ROW_SELECTOR,
								DbroutingFactory.eINSTANCE.createResultSetRowSelector())));

		// for IO routing
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(IoroutingPackage.Literals.IO_DOCUMENT_ROOT__ROUTER, IoroutingFactory.eINSTANCE
								.createIORouter())));

		// for JMS routing
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(JmsroutingPackage.Literals.JMS_DOCUMENT_ROOT__ROUTER, JmsroutingFactory.eINSTANCE
								.createJmsRouter())));

		// for File routing
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(FileRoutingPackage.Literals.DOCUMENT_ROOT__OUTPUT_STREAM,
								FileRoutingFactory.eINSTANCE.createOutputStream())));

		// for ESB routing
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(EsbroutingPackage.Literals.ESB_ROUTING_DOCUMENT_ROOT__ROUTE_BEAN,
								EsbroutingFactory.eINSTANCE.createRouteBean())));

		/***********************************************/

		/****************** Add Smooks 1.2 items ********/
		// for Rules 1.0
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(Rules10Package.Literals.RULES10_DOCUMENT_ROOT__RULE_BASES,
								Rules10Factory.eINSTANCE.createRuleBasesType())));

		// for Persistence 1.2
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__DELETER,
								Persistence12Factory.eINSTANCE.createDeleter())));

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__FLUSHER,
								Persistence12Factory.eINSTANCE.createFlusher())));

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__INSERTER,
								Persistence12Factory.eINSTANCE.createInserter())));

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__LOCATOR,
								Persistence12Factory.eINSTANCE.createLocator())));

		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__UPDATER,
								Persistence12Factory.eINSTANCE.createUpdater())));

		// for Validation 1.0
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(Validation10Package.Literals.VALIDATION10_DOCUMENT_ROOT__RULE,
								Validation10Factory.eINSTANCE.createRuleType())));

		// for CSV 1.2
		newChildDescriptors
				.add(createChildParameter(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP,
						FeatureMapUtil.createEntry(Csv12Package.Literals.CSV12_DOCUMENT_ROOT__READER,
								Csv12Factory.eINSTANCE.createCSV12Reader())));

		// for EDI 1.2
		newChildDescriptors
				.add(createChildParameter(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP,
						FeatureMapUtil.createEntry(Edi12Package.Literals.EDI12_DOCUMENT_ROOT__READER,
								Edi12Factory.eINSTANCE.createEDI12Reader())));

		// for JSON 1.2
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, FeatureMapUtil.createEntry(
						Json12Package.Literals.JSON12_DOCUMENT_ROOT__READER, Json12Factory.eINSTANCE
								.createJson12Reader())));

		// for JavaBean 1.2
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(Javabean12Package.Literals.JAVABEAN12_DOCUMENT_ROOT__BEAN,
								Javabean12Factory.eINSTANCE.createBeanType())));
		
		// for JMS Router 1.2
		newChildDescriptors.add(createChildParameter(
				SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP, FeatureMapUtil
						.createEntry(Jmsrouting12Package.Literals.JMS_ROUTING12_DOCUMENT_ROOT__ROUTER,
								Jmsrouting12Factory.eINSTANCE.createRouter())));

	}

	/**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return Xsl1EditPlugin.INSTANCE;
	}

}
