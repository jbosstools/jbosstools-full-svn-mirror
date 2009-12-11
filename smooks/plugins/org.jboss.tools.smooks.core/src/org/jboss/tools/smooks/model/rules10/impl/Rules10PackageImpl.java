/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.rules10.impl;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.common.CommonPackage;
import org.jboss.tools.smooks.model.common.impl.CommonPackageImpl;
import org.jboss.tools.smooks.model.rules10.RuleBase;
import org.jboss.tools.smooks.model.rules10.RuleBasesType;
import org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot;
import org.jboss.tools.smooks.model.rules10.Rules10Factory;
import org.jboss.tools.smooks.model.rules10.Rules10Package;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.impl.SmooksPackageImpl;




/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Rules10PackageImpl extends EPackageImpl implements Rules10Package {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass rules10DocumentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ruleBaseEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ruleBasesTypeEClass = null;

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
	 * @see org.jboss.tools.smooks.model.rules10.Rules10Package#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Rules10PackageImpl() {
		super(eNS_URI, Rules10Factory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link Rules10Package#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Rules10Package init() {
		if (isInited) return (Rules10Package)EPackage.Registry.INSTANCE.getEPackage(Rules10Package.eNS_URI);

		// Obtain or create and register package
		Rules10PackageImpl theRules10Package = (Rules10PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Rules10PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Rules10PackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		SmooksPackageImpl theSmooksPackage = (SmooksPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) instanceof SmooksPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) : SmooksPackage.eINSTANCE);
		CommonPackageImpl theCommonPackage = (CommonPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) instanceof CommonPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) : CommonPackage.eINSTANCE);

		// Create package meta-data objects
		theRules10Package.createPackageContents();
		theSmooksPackage.createPackageContents();
		theCommonPackage.createPackageContents();

		// Initialize created meta-data
		theRules10Package.initializePackageContents();
		theSmooksPackage.initializePackageContents();
		theCommonPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theRules10Package.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Rules10Package.eNS_URI, theRules10Package);
		return theRules10Package;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRules10DocumentRoot() {
		return rules10DocumentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRules10DocumentRoot_Mixed() {
		return (EAttribute)rules10DocumentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRules10DocumentRoot_XMLNSPrefixMap() {
		return (EReference)rules10DocumentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRules10DocumentRoot_XSISchemaLocation() {
		return (EReference)rules10DocumentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRules10DocumentRoot_RuleBases() {
		return (EReference)rules10DocumentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRuleBase() {
		return ruleBaseEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRuleBase_Name() {
		return (EAttribute)ruleBaseEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRuleBase_Provider() {
		return (EAttribute)ruleBaseEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRuleBase_Src() {
		return (EAttribute)ruleBaseEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRuleBasesType() {
		return ruleBasesTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRuleBasesType_Group() {
		return (EAttribute)ruleBasesTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRuleBasesType_RuleBase() {
		return (EReference)ruleBasesTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rules10Factory getRules10Factory() {
		return (Rules10Factory)getEFactoryInstance();
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
		rules10DocumentRootEClass = createEClass(RULES10_DOCUMENT_ROOT);
		createEAttribute(rules10DocumentRootEClass, RULES10_DOCUMENT_ROOT__MIXED);
		createEReference(rules10DocumentRootEClass, RULES10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(rules10DocumentRootEClass, RULES10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(rules10DocumentRootEClass, RULES10_DOCUMENT_ROOT__RULE_BASES);

		ruleBaseEClass = createEClass(RULE_BASE);
		createEAttribute(ruleBaseEClass, RULE_BASE__NAME);
		createEAttribute(ruleBaseEClass, RULE_BASE__PROVIDER);
		createEAttribute(ruleBaseEClass, RULE_BASE__SRC);

		ruleBasesTypeEClass = createEClass(RULE_BASES_TYPE);
		createEAttribute(ruleBasesTypeEClass, RULE_BASES_TYPE__GROUP);
		createEReference(ruleBasesTypeEClass, RULE_BASES_TYPE__RULE_BASE);
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
		SmooksPackage theSmooksPackage = (SmooksPackage)EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Add supertypes to classes
		ruleBaseEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());
		ruleBasesTypeEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());

		// Initialize classes and features; add operations and parameters
		initEClass(rules10DocumentRootEClass, Rules10DocumentRoot.class, "Rules10DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getRules10DocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getRules10DocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getRules10DocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getRules10DocumentRoot_RuleBases(), this.getRuleBasesType(), null, "ruleBases", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(ruleBaseEClass, RuleBase.class, "RuleBase", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getRuleBase_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, RuleBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getRuleBase_Provider(), theXMLTypePackage.getString(), "provider", null, 1, 1, RuleBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEAttribute(getRuleBase_Src(), theXMLTypePackage.getString(), "src", null, 0, 1, RuleBase.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

		initEClass(ruleBasesTypeEClass, RuleBasesType.class, "RuleBasesType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
		initEAttribute(getRuleBasesType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, RuleBasesType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED); //$NON-NLS-1$
		initEReference(getRuleBasesType_RuleBase(), this.getRuleBase(), null, "ruleBase", null, 1, -1, RuleBasesType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED); //$NON-NLS-1$

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
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";			 //$NON-NLS-1$
		addAnnotation
		  (rules10DocumentRootEClass, 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_13, Messages.Rules10PackageImpl_14,
			 Messages.Rules10PackageImpl_15, Messages.Rules10PackageImpl_16
		   });		
		addAnnotation
		  (getRules10DocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_17, Messages.Rules10PackageImpl_18,
			 Messages.Rules10PackageImpl_19, Messages.Rules10PackageImpl_20
		   });		
		addAnnotation
		  (getRules10DocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_21, Messages.Rules10PackageImpl_22,
			 Messages.Rules10PackageImpl_23, Messages.Rules10PackageImpl_24
		   });		
		addAnnotation
		  (getRules10DocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_25, Messages.Rules10PackageImpl_26,
			 Messages.Rules10PackageImpl_27, Messages.Rules10PackageImpl_28
		   });		
		addAnnotation
		  (getRules10DocumentRoot_RuleBases(), 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_29, Messages.Rules10PackageImpl_30,
			 Messages.Rules10PackageImpl_31, Messages.Rules10PackageImpl_32,
			 Messages.Rules10PackageImpl_33, Messages.Rules10PackageImpl_34,
			 Messages.Rules10PackageImpl_35, Messages.Rules10PackageImpl_36
		   });		
		addAnnotation
		  (ruleBaseEClass, 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_37, Messages.Rules10PackageImpl_38,
			 Messages.Rules10PackageImpl_39, Messages.Rules10PackageImpl_40
		   });			
		addAnnotation
		  (getRuleBase_Name(), 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_41, Messages.Rules10PackageImpl_42,
			 Messages.Rules10PackageImpl_43, Messages.Rules10PackageImpl_44
		   });			
		addAnnotation
		  (getRuleBase_Provider(), 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_45, Messages.Rules10PackageImpl_46,
			 Messages.Rules10PackageImpl_47, Messages.Rules10PackageImpl_48
		   });			
		addAnnotation
		  (getRuleBase_Src(), 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_49, Messages.Rules10PackageImpl_50,
			 Messages.Rules10PackageImpl_51, Messages.Rules10PackageImpl_52
		   });		
		addAnnotation
		  (ruleBasesTypeEClass, 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_53, Messages.Rules10PackageImpl_54,
			 Messages.Rules10PackageImpl_55, Messages.Rules10PackageImpl_56
		   });		
		addAnnotation
		  (getRuleBasesType_Group(), 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_57, Messages.Rules10PackageImpl_58,
			 Messages.Rules10PackageImpl_59, Messages.Rules10PackageImpl_60
		   });		
		addAnnotation
		  (getRuleBasesType_RuleBase(), 
		   source, 
		   new String[] {
			 Messages.Rules10PackageImpl_61, Messages.Rules10PackageImpl_62,
			 Messages.Rules10PackageImpl_63, Messages.Rules10PackageImpl_64,
			 Messages.Rules10PackageImpl_65, Messages.Rules10PackageImpl_66,
			 Messages.Rules10PackageImpl_67, Messages.Rules10PackageImpl_68
		   });
	}

} //Rules10PackageImpl
