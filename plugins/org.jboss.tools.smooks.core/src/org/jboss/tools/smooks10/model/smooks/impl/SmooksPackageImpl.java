/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks10.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks10.model.smooks.AbstractType;
import org.jboss.tools.smooks10.model.smooks.ConditionType;
import org.jboss.tools.smooks10.model.smooks.ImportType;
import org.jboss.tools.smooks10.model.smooks.ParamType;
import org.jboss.tools.smooks10.model.smooks.ProfileType;
import org.jboss.tools.smooks10.model.smooks.ProfilesType;
import org.jboss.tools.smooks10.model.smooks.ResourceConfigType;
import org.jboss.tools.smooks10.model.smooks.ResourceType;
import org.jboss.tools.smooks10.model.smooks.DocumentRoot;
import org.jboss.tools.smooks10.model.smooks.SmooksFactory;
import org.jboss.tools.smooks10.model.smooks.SmooksPackage;
import org.jboss.tools.smooks10.model.smooks.SmooksResourceListType;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SmooksPackageImpl extends EPackageImpl implements SmooksPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractResourceConfigEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass conditionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass smooks10DocumentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass importTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass paramTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profilesTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass profileTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resourceConfigTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resourceTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass smooksResourceListTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass abstractTypeEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SmooksPackageImpl() {
		super(eNS_URI, SmooksFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link SmooksPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SmooksPackage init() {
		if (isInited) return (SmooksPackage)EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI);

		// Obtain or create and register package
		SmooksPackageImpl theSmooksPackage = (SmooksPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SmooksPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new SmooksPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theSmooksPackage.createPackageContents();

		// Initialize created meta-data
		theSmooksPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSmooksPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SmooksPackage.eNS_URI, theSmooksPackage);
		return theSmooksPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractResourceConfig() {
		return abstractResourceConfigEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConditionType() {
		return conditionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConditionType_Value() {
		return (EAttribute)conditionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConditionType_Evaluator() {
		return (EAttribute)conditionTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSmooks10DocumentRoot() {
		return smooks10DocumentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooks10DocumentRoot_Mixed() {
		return (EAttribute)smooks10DocumentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_XMLNSPrefixMap() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_XSISchemaLocation() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_AbstractResourceConfig() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_Condition() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_Import() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_Param() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_Profile() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_Profiles() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_Resource() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_ResourceConfig() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooks10DocumentRoot_SmooksResourceList() {
		return (EReference)smooks10DocumentRootEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getImportType() {
		return importTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getImportType_File() {
		return (EAttribute)importTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParamType() {
		return paramTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParamType_Name() {
		return (EAttribute)paramTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParamType_Type() {
		return (EAttribute)paramTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProfilesType() {
		return profilesTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getProfilesType_Profile() {
		return (EReference)profilesTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getProfileType() {
		return profileTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfileType_Value() {
		return (EAttribute)profileTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfileType_BaseProfile() {
		return (EAttribute)profileTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getProfileType_SubProfiles() {
		return (EAttribute)profileTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResourceConfigType() {
		return resourceConfigTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResourceConfigType_Resource() {
		return (EReference)resourceConfigTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResourceConfigType_Condition() {
		return (EReference)resourceConfigTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getResourceConfigType_Param() {
		return (EReference)resourceConfigTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getResourceConfigType_Selector() {
		return (EAttribute)resourceConfigTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getResourceConfigType_SelectorNamespace() {
		return (EAttribute)resourceConfigTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getResourceConfigType_TargetProfile() {
		return (EAttribute)resourceConfigTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResourceType() {
		return resourceTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getResourceType_Type() {
		return (EAttribute)resourceTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSmooksResourceListType() {
		return smooksResourceListTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooksResourceListType_Profiles() {
		return (EReference)smooksResourceListTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooksResourceListType_AbstractResourceConfigGroup() {
		return (EAttribute)smooksResourceListTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooksResourceListType_AbstractResourceConfig() {
		return (EReference)smooksResourceListTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooksResourceListType_DefaultSelector() {
		return (EAttribute)smooksResourceListTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooksResourceListType_DefaultSelectorNamespace() {
		return (EAttribute)smooksResourceListTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooksResourceListType_DefaultTargetProfile() {
		return (EAttribute)smooksResourceListTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAbstractType() {
		return abstractTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksFactory getSmooksFactory() {
		return (SmooksFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		abstractResourceConfigEClass = createEClass(ABSTRACT_RESOURCE_CONFIG);

		conditionTypeEClass = createEClass(CONDITION_TYPE);
		createEAttribute(conditionTypeEClass, CONDITION_TYPE__VALUE);
		createEAttribute(conditionTypeEClass, CONDITION_TYPE__EVALUATOR);

		smooks10DocumentRootEClass = createEClass(SMOOKS10_DOCUMENT_ROOT);
		createEAttribute(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__MIXED);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__ABSTRACT_RESOURCE_CONFIG);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__CONDITION);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__IMPORT);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__PARAM);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__PROFILE);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__PROFILES);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__RESOURCE);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__RESOURCE_CONFIG);
		createEReference(smooks10DocumentRootEClass, SMOOKS10_DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST);

		importTypeEClass = createEClass(IMPORT_TYPE);
		createEAttribute(importTypeEClass, IMPORT_TYPE__FILE);

		paramTypeEClass = createEClass(PARAM_TYPE);
		createEAttribute(paramTypeEClass, PARAM_TYPE__NAME);
		createEAttribute(paramTypeEClass, PARAM_TYPE__TYPE);

		profilesTypeEClass = createEClass(PROFILES_TYPE);
		createEReference(profilesTypeEClass, PROFILES_TYPE__PROFILE);

		profileTypeEClass = createEClass(PROFILE_TYPE);
		createEAttribute(profileTypeEClass, PROFILE_TYPE__VALUE);
		createEAttribute(profileTypeEClass, PROFILE_TYPE__BASE_PROFILE);
		createEAttribute(profileTypeEClass, PROFILE_TYPE__SUB_PROFILES);

		resourceConfigTypeEClass = createEClass(RESOURCE_CONFIG_TYPE);
		createEReference(resourceConfigTypeEClass, RESOURCE_CONFIG_TYPE__RESOURCE);
		createEReference(resourceConfigTypeEClass, RESOURCE_CONFIG_TYPE__CONDITION);
		createEReference(resourceConfigTypeEClass, RESOURCE_CONFIG_TYPE__PARAM);
		createEAttribute(resourceConfigTypeEClass, RESOURCE_CONFIG_TYPE__SELECTOR);
		createEAttribute(resourceConfigTypeEClass, RESOURCE_CONFIG_TYPE__SELECTOR_NAMESPACE);
		createEAttribute(resourceConfigTypeEClass, RESOURCE_CONFIG_TYPE__TARGET_PROFILE);

		resourceTypeEClass = createEClass(RESOURCE_TYPE);
		createEAttribute(resourceTypeEClass, RESOURCE_TYPE__TYPE);

		smooksResourceListTypeEClass = createEClass(SMOOKS_RESOURCE_LIST_TYPE);
		createEReference(smooksResourceListTypeEClass, SMOOKS_RESOURCE_LIST_TYPE__PROFILES);
		createEAttribute(smooksResourceListTypeEClass, SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP);
		createEReference(smooksResourceListTypeEClass, SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG);
		createEAttribute(smooksResourceListTypeEClass, SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR);
		createEAttribute(smooksResourceListTypeEClass, SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE);
		createEAttribute(smooksResourceListTypeEClass, SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE);

		abstractTypeEClass = createEClass(ABSTRACT_TYPE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		abstractResourceConfigEClass.getESuperTypes().add(this.getAbstractType());
		conditionTypeEClass.getESuperTypes().add(this.getAbstractType());
		importTypeEClass.getESuperTypes().add(this.getAbstractResourceConfig());
		paramTypeEClass.getESuperTypes().add(this.getAbstractType());
		profilesTypeEClass.getESuperTypes().add(this.getAbstractType());
		profileTypeEClass.getESuperTypes().add(this.getAbstractType());
		resourceConfigTypeEClass.getESuperTypes().add(this.getAbstractResourceConfig());
		resourceTypeEClass.getESuperTypes().add(this.getAbstractType());
		smooksResourceListTypeEClass.getESuperTypes().add(this.getAbstractType());
		abstractTypeEClass.getESuperTypes().add(theXMLTypePackage.getAnyType());

		// Initialize classes and features; add operations and parameters
		initEClass(abstractResourceConfigEClass, AbstractResourceConfig.class, "AbstractResourceConfig", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(conditionTypeEClass, ConditionType.class, "ConditionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConditionType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ConditionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConditionType_Evaluator(), theXMLTypePackage.getString(), "evaluator", "org.milyn.javabean.expression.BeanMapExpressionEvaluator", 0, 1, ConditionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(smooks10DocumentRootEClass, DocumentRoot.class, "Smooks10DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSmooks10DocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_AbstractResourceConfig(), this.getAbstractResourceConfig(), null, "abstractResourceConfig", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_Condition(), this.getConditionType(), null, "condition", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_Import(), this.getImportType(), null, "import", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_Param(), this.getParamType(), null, "param", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_Profile(), this.getProfileType(), null, "profile", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_Profiles(), this.getProfilesType(), null, "profiles", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_Resource(), this.getResourceType(), null, "resource", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_ResourceConfig(), this.getResourceConfigType(), null, "resourceConfig", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getSmooks10DocumentRoot_SmooksResourceList(), this.getSmooksResourceListType(), null, "smooksResourceList", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(importTypeEClass, ImportType.class, "ImportType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getImportType_File(), theXMLTypePackage.getAnyURI(), "file", null, 1, 1, ImportType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(paramTypeEClass, ParamType.class, "ParamType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParamType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ParamType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParamType_Type(), theXMLTypePackage.getString(), "type", null, 0, 1, ParamType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(profilesTypeEClass, ProfilesType.class, "ProfilesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getProfilesType_Profile(), this.getProfileType(), null, "profile", null, 1, -1, ProfilesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(profileTypeEClass, ProfileType.class, "ProfileType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getProfileType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ProfileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileType_BaseProfile(), theXMLTypePackage.getString(), "baseProfile", null, 1, 1, ProfileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getProfileType_SubProfiles(), theXMLTypePackage.getString(), "subProfiles", null, 0, 1, ProfileType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resourceConfigTypeEClass, ResourceConfigType.class, "ResourceConfigType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getResourceConfigType_Resource(), this.getResourceType(), null, "resource", null, 0, 1, ResourceConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getResourceConfigType_Condition(), this.getConditionType(), null, "condition", null, 0, 1, ResourceConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getResourceConfigType_Param(), this.getParamType(), null, "param", null, 0, -1, ResourceConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResourceConfigType_Selector(), theXMLTypePackage.getString(), "selector", null, 0, 1, ResourceConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResourceConfigType_SelectorNamespace(), theXMLTypePackage.getAnyURI(), "selectorNamespace", null, 0, 1, ResourceConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResourceConfigType_TargetProfile(), theXMLTypePackage.getString(), "targetProfile", null, 0, 1, ResourceConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resourceTypeEClass, ResourceType.class, "ResourceType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getResourceType_Type(), theXMLTypePackage.getString(), "type", null, 0, 1, ResourceType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(smooksResourceListTypeEClass, SmooksResourceListType.class, "SmooksResourceListType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSmooksResourceListType_Profiles(), this.getProfilesType(), null, "profiles", null, 0, 1, SmooksResourceListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSmooksResourceListType_AbstractResourceConfigGroup(), ecorePackage.getEFeatureMapEntry(), "abstractResourceConfigGroup", null, 1, -1, SmooksResourceListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSmooksResourceListType_AbstractResourceConfig(), this.getAbstractResourceConfig(), null, "abstractResourceConfig", null, 1, -1, SmooksResourceListType.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getSmooksResourceListType_DefaultSelector(), theXMLTypePackage.getString(), "defaultSelector", null, 0, 1, SmooksResourceListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSmooksResourceListType_DefaultSelectorNamespace(), theXMLTypePackage.getAnyURI(), "defaultSelectorNamespace", null, 0, 1, SmooksResourceListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSmooksResourceListType_DefaultTargetProfile(), theXMLTypePackage.getString(), "defaultTargetProfile", null, 0, 1, SmooksResourceListType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(abstractTypeEClass, AbstractType.class, "AbstractType", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
		addAnnotation
		  (abstractResourceConfigEClass, 
		   source, 
		   new String[] {
			 "name", "abstract-resource-config",
			 "kind", "empty"
		   });			
		addAnnotation
		  (conditionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "condition_._type",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getConditionType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getConditionType_Evaluator(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "evaluator"
		   });		
		addAnnotation
		  (smooks10DocumentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_AbstractResourceConfig(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "abstract-resource-config",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_Condition(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "condition",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_Import(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "import",
			 "namespace", "##targetNamespace",
			 "affiliation", "abstract-resource-config"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_Param(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "param",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_Profile(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "profile",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_Profiles(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "profiles",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_Resource(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "resource",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_ResourceConfig(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "resource-config",
			 "namespace", "##targetNamespace",
			 "affiliation", "abstract-resource-config"
		   });		
		addAnnotation
		  (getSmooks10DocumentRoot_SmooksResourceList(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "smooks-resource-list",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (importTypeEClass, 
		   source, 
		   new String[] {
			 "name", "import_._type",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getImportType_File(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "file"
		   });			
		addAnnotation
		  (paramTypeEClass, 
		   source, 
		   new String[] {
			 "name", "param_._type",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getParamType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getParamType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type"
		   });			
		addAnnotation
		  (profilesTypeEClass, 
		   source, 
		   new String[] {
			 "name", "profiles_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getProfilesType_Profile(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "profile",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (profileTypeEClass, 
		   source, 
		   new String[] {
			 "name", "profile_._type",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getProfileType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getProfileType_BaseProfile(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "base-profile"
		   });		
		addAnnotation
		  (getProfileType_SubProfiles(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "sub-profiles"
		   });			
		addAnnotation
		  (resourceConfigTypeEClass, 
		   source, 
		   new String[] {
			 "name", "resource-config_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getResourceConfigType_Resource(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "resource",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getResourceConfigType_Condition(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "condition",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getResourceConfigType_Param(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "param",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getResourceConfigType_Selector(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "selector"
		   });		
		addAnnotation
		  (getResourceConfigType_SelectorNamespace(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "selector-namespace"
		   });		
		addAnnotation
		  (getResourceConfigType_TargetProfile(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "target-profile"
		   });			
		addAnnotation
		  (resourceTypeEClass, 
		   source, 
		   new String[] {
			 "name", "resource_._type",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getResourceType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type"
		   });			
		addAnnotation
		  (smooksResourceListTypeEClass, 
		   source, 
		   new String[] {
			 "name", "smooks-resource-list_._type",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getSmooksResourceListType_Profiles(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "profiles",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooksResourceListType_AbstractResourceConfigGroup(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "abstract-resource-config:group",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooksResourceListType_AbstractResourceConfig(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "abstract-resource-config",
			 "namespace", "##targetNamespace",
			 "group", "abstract-resource-config:group"
		   });		
		addAnnotation
		  (getSmooksResourceListType_DefaultSelector(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "default-selector"
		   });		
		addAnnotation
		  (getSmooksResourceListType_DefaultSelectorNamespace(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "default-selector-namespace"
		   });		
		addAnnotation
		  (getSmooksResourceListType_DefaultTargetProfile(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "default-target-profile"
		   });
	}

} //SmooksPackageImpl
