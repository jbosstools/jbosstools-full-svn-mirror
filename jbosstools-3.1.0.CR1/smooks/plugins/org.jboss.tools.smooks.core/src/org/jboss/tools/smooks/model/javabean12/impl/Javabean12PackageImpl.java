/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean12.impl;




import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.common.CommonPackage;
import org.jboss.tools.smooks.model.common.impl.CommonPackageImpl;
import org.jboss.tools.smooks.model.javabean12.BeanType;
import org.jboss.tools.smooks.model.javabean12.DecodeParamType;
import org.jboss.tools.smooks.model.javabean12.ExpressionType;
import org.jboss.tools.smooks.model.javabean12.Javabean12DocumentRoot;
import org.jboss.tools.smooks.model.javabean12.Javabean12Factory;
import org.jboss.tools.smooks.model.javabean12.Javabean12Package;
import org.jboss.tools.smooks.model.javabean12.ResultType;
import org.jboss.tools.smooks.model.javabean12.ValueType;
import org.jboss.tools.smooks.model.javabean12.WiringType;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.impl.SmooksPackageImpl;



/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Javabean12PackageImpl extends EPackageImpl implements Javabean12Package {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass beanTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass decodeParamTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass javabean12DocumentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass expressionTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resultTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass valueTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass wiringTypeEClass = null;

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
	 * @see org.jboss.tools.smooks.model.javabean12.Javabean12Package#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Javabean12PackageImpl() {
		super(eNS_URI, Javabean12Factory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link Javabean12Package#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Javabean12Package init() {
		if (isInited) return (Javabean12Package)EPackage.Registry.INSTANCE.getEPackage(Javabean12Package.eNS_URI);

		// Obtain or create and register package
		Javabean12PackageImpl theJavabean12Package = (Javabean12PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Javabean12PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Javabean12PackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		SmooksPackageImpl theSmooksPackage = (SmooksPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) instanceof SmooksPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) : SmooksPackage.eINSTANCE);
		CommonPackageImpl theCommonPackage = (CommonPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) instanceof CommonPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) : CommonPackage.eINSTANCE);

		// Create package meta-data objects
		theJavabean12Package.createPackageContents();
		theSmooksPackage.createPackageContents();
		theCommonPackage.createPackageContents();

		// Initialize created meta-data
		theJavabean12Package.initializePackageContents();
		theSmooksPackage.initializePackageContents();
		theCommonPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJavabean12Package.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Javabean12Package.eNS_URI, theJavabean12Package);
		return theJavabean12Package;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBeanType() {
		return beanTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanType_Group() {
		return (EAttribute)beanTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBeanType_Value() {
		return (EReference)beanTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBeanType_Wiring() {
		return (EReference)beanTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBeanType_Expression() {
		return (EReference)beanTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanType_BeanId() {
		return (EAttribute)beanTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanType_Class() {
		return (EAttribute)beanTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanType_CreateOnElement() {
		return (EAttribute)beanTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanType_CreateOnElementNS() {
		return (EAttribute)beanTypeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBeanType_ExtendLifecycle() {
		return (EAttribute)beanTypeEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDecodeParamType() {
		return decodeParamTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDecodeParamType_Value() {
		return (EAttribute)decodeParamTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDecodeParamType_Name() {
		return (EAttribute)decodeParamTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJavabean12DocumentRoot() {
		return javabean12DocumentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJavabean12DocumentRoot_Mixed() {
		return (EAttribute)javabean12DocumentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJavabean12DocumentRoot_XMLNSPrefixMap() {
		return (EReference)javabean12DocumentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJavabean12DocumentRoot_XSISchemaLocation() {
		return (EReference)javabean12DocumentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJavabean12DocumentRoot_Bean() {
		return (EReference)javabean12DocumentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJavabean12DocumentRoot_DecodeParam() {
		return (EReference)javabean12DocumentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJavabean12DocumentRoot_Expression() {
		return (EReference)javabean12DocumentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJavabean12DocumentRoot_Result() {
		return (EReference)javabean12DocumentRootEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJavabean12DocumentRoot_Value() {
		return (EReference)javabean12DocumentRootEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJavabean12DocumentRoot_Wiring() {
		return (EReference)javabean12DocumentRootEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExpressionType() {
		return expressionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionType_Value() {
		return (EAttribute)expressionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionType_ExecOnElement() {
		return (EAttribute)expressionTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionType_ExecOnElementNS() {
		return (EAttribute)expressionTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionType_InitVal() {
		return (EAttribute)expressionTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionType_Property() {
		return (EAttribute)expressionTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionType_SetterMethod() {
		return (EAttribute)expressionTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getResultType() {
		return resultTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getResultType_RetainBeans() {
		return (EAttribute)resultTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getValueType() {
		return valueTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getValueType_DecodeParam() {
		return (EReference)valueTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueType_Data() {
		return (EAttribute)valueTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueType_DataNS() {
		return (EAttribute)valueTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueType_Decoder() {
		return (EAttribute)valueTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueType_Default() {
		return (EAttribute)valueTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueType_Property() {
		return (EAttribute)valueTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueType_SetterMethod() {
		return (EAttribute)valueTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWiringType() {
		return wiringTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiringType_BeanIdRef() {
		return (EAttribute)wiringTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiringType_Property() {
		return (EAttribute)wiringTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiringType_SetterMethod() {
		return (EAttribute)wiringTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiringType_WireOnElement() {
		return (EAttribute)wiringTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiringType_WireOnElementNS() {
		return (EAttribute)wiringTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Javabean12Factory getJavabean12Factory() {
		return (Javabean12Factory)getEFactoryInstance();
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
		beanTypeEClass = createEClass(BEAN_TYPE);
		createEAttribute(beanTypeEClass, BEAN_TYPE__GROUP);
		createEReference(beanTypeEClass, BEAN_TYPE__VALUE);
		createEReference(beanTypeEClass, BEAN_TYPE__WIRING);
		createEReference(beanTypeEClass, BEAN_TYPE__EXPRESSION);
		createEAttribute(beanTypeEClass, BEAN_TYPE__BEAN_ID);
		createEAttribute(beanTypeEClass, BEAN_TYPE__CLASS);
		createEAttribute(beanTypeEClass, BEAN_TYPE__CREATE_ON_ELEMENT);
		createEAttribute(beanTypeEClass, BEAN_TYPE__CREATE_ON_ELEMENT_NS);
		createEAttribute(beanTypeEClass, BEAN_TYPE__EXTEND_LIFECYCLE);

		decodeParamTypeEClass = createEClass(DECODE_PARAM_TYPE);
		createEAttribute(decodeParamTypeEClass, DECODE_PARAM_TYPE__VALUE);
		createEAttribute(decodeParamTypeEClass, DECODE_PARAM_TYPE__NAME);

		javabean12DocumentRootEClass = createEClass(JAVABEAN12_DOCUMENT_ROOT);
		createEAttribute(javabean12DocumentRootEClass, JAVABEAN12_DOCUMENT_ROOT__MIXED);
		createEReference(javabean12DocumentRootEClass, JAVABEAN12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(javabean12DocumentRootEClass, JAVABEAN12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(javabean12DocumentRootEClass, JAVABEAN12_DOCUMENT_ROOT__BEAN);
		createEReference(javabean12DocumentRootEClass, JAVABEAN12_DOCUMENT_ROOT__DECODE_PARAM);
		createEReference(javabean12DocumentRootEClass, JAVABEAN12_DOCUMENT_ROOT__EXPRESSION);
		createEReference(javabean12DocumentRootEClass, JAVABEAN12_DOCUMENT_ROOT__RESULT);
		createEReference(javabean12DocumentRootEClass, JAVABEAN12_DOCUMENT_ROOT__VALUE);
		createEReference(javabean12DocumentRootEClass, JAVABEAN12_DOCUMENT_ROOT__WIRING);

		expressionTypeEClass = createEClass(EXPRESSION_TYPE);
		createEAttribute(expressionTypeEClass, EXPRESSION_TYPE__VALUE);
		createEAttribute(expressionTypeEClass, EXPRESSION_TYPE__EXEC_ON_ELEMENT);
		createEAttribute(expressionTypeEClass, EXPRESSION_TYPE__EXEC_ON_ELEMENT_NS);
		createEAttribute(expressionTypeEClass, EXPRESSION_TYPE__INIT_VAL);
		createEAttribute(expressionTypeEClass, EXPRESSION_TYPE__PROPERTY);
		createEAttribute(expressionTypeEClass, EXPRESSION_TYPE__SETTER_METHOD);

		resultTypeEClass = createEClass(RESULT_TYPE);
		createEAttribute(resultTypeEClass, RESULT_TYPE__RETAIN_BEANS);

		valueTypeEClass = createEClass(VALUE_TYPE);
		createEReference(valueTypeEClass, VALUE_TYPE__DECODE_PARAM);
		createEAttribute(valueTypeEClass, VALUE_TYPE__DATA);
		createEAttribute(valueTypeEClass, VALUE_TYPE__DATA_NS);
		createEAttribute(valueTypeEClass, VALUE_TYPE__DECODER);
		createEAttribute(valueTypeEClass, VALUE_TYPE__DEFAULT);
		createEAttribute(valueTypeEClass, VALUE_TYPE__PROPERTY);
		createEAttribute(valueTypeEClass, VALUE_TYPE__SETTER_METHOD);

		wiringTypeEClass = createEClass(WIRING_TYPE);
		createEAttribute(wiringTypeEClass, WIRING_TYPE__BEAN_ID_REF);
		createEAttribute(wiringTypeEClass, WIRING_TYPE__PROPERTY);
		createEAttribute(wiringTypeEClass, WIRING_TYPE__SETTER_METHOD);
		createEAttribute(wiringTypeEClass, WIRING_TYPE__WIRE_ON_ELEMENT);
		createEAttribute(wiringTypeEClass, WIRING_TYPE__WIRE_ON_ELEMENT_NS);
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
		CommonPackage theCommonPackage = (CommonPackage)EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI);

		// Add supertypes to classes
		beanTypeEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());
		decodeParamTypeEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		expressionTypeEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		resultTypeEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());
		valueTypeEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		wiringTypeEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());

		// Initialize classes and features; add operations and parameters
		initEClass(beanTypeEClass, BeanType.class, "BeanType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBeanType_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, BeanType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBeanType_Value(), this.getValueType(), null, "value", null, 0, -1, BeanType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getBeanType_Wiring(), this.getWiringType(), null, "wiring", null, 0, -1, BeanType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getBeanType_Expression(), this.getExpressionType(), null, "expression", null, 0, -1, BeanType.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getBeanType_BeanId(), theXMLTypePackage.getString(), "beanId", null, 1, 1, BeanType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBeanType_Class(), theXMLTypePackage.getString(), "class", null, 1, 1, BeanType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBeanType_CreateOnElement(), theXMLTypePackage.getString(), "createOnElement", null, 0, 1, BeanType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBeanType_CreateOnElementNS(), theXMLTypePackage.getAnyURI(), "createOnElementNS", null, 0, 1, BeanType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBeanType_ExtendLifecycle(), theXMLTypePackage.getBoolean(), "extendLifecycle", null, 0, 1, BeanType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(decodeParamTypeEClass, DecodeParamType.class, "DecodeParamType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDecodeParamType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, DecodeParamType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDecodeParamType_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, DecodeParamType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(javabean12DocumentRootEClass, Javabean12DocumentRoot.class, "Javabean12DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJavabean12DocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJavabean12DocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJavabean12DocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJavabean12DocumentRoot_Bean(), this.getBeanType(), null, "bean", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getJavabean12DocumentRoot_DecodeParam(), this.getDecodeParamType(), null, "decodeParam", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getJavabean12DocumentRoot_Expression(), this.getExpressionType(), null, "expression", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getJavabean12DocumentRoot_Result(), this.getResultType(), null, "result", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getJavabean12DocumentRoot_Value(), this.getValueType(), null, "value", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getJavabean12DocumentRoot_Wiring(), this.getWiringType(), null, "wiring", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(expressionTypeEClass, ExpressionType.class, "ExpressionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExpressionType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpressionType_ExecOnElement(), theXMLTypePackage.getString(), "execOnElement", null, 0, 1, ExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpressionType_ExecOnElementNS(), theXMLTypePackage.getAnyURI(), "execOnElementNS", null, 0, 1, ExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpressionType_InitVal(), theXMLTypePackage.getString(), "initVal", null, 0, 1, ExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpressionType_Property(), theXMLTypePackage.getString(), "property", null, 0, 1, ExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpressionType_SetterMethod(), theXMLTypePackage.getString(), "setterMethod", null, 0, 1, ExpressionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resultTypeEClass, ResultType.class, "ResultType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getResultType_RetainBeans(), theXMLTypePackage.getString(), "retainBeans", null, 1, 1, ResultType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(valueTypeEClass, ValueType.class, "ValueType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getValueType_DecodeParam(), this.getDecodeParamType(), null, "decodeParam", null, 0, -1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueType_Data(), theXMLTypePackage.getString(), "data", null, 1, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueType_DataNS(), theXMLTypePackage.getAnyURI(), "dataNS", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueType_Decoder(), theXMLTypePackage.getString(), "decoder", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueType_Default(), theXMLTypePackage.getString(), "default", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueType_Property(), theXMLTypePackage.getString(), "property", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueType_SetterMethod(), theXMLTypePackage.getString(), "setterMethod", null, 0, 1, ValueType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wiringTypeEClass, WiringType.class, "WiringType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getWiringType_BeanIdRef(), theXMLTypePackage.getString(), "beanIdRef", null, 1, 1, WiringType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiringType_Property(), theXMLTypePackage.getString(), "property", null, 0, 1, WiringType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiringType_SetterMethod(), theXMLTypePackage.getString(), "setterMethod", null, 0, 1, WiringType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiringType_WireOnElement(), theXMLTypePackage.getString(), "wireOnElement", null, 0, 1, WiringType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiringType_WireOnElementNS(), theXMLTypePackage.getAnyURI(), "wireOnElementNS", null, 0, 1, WiringType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		  (beanTypeEClass, 
		   source, 
		   new String[] {
			 "name", "bean_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getBeanType_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:2"
		   });			
		addAnnotation
		  (getBeanType_Value(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "value",
			 "namespace", "##targetNamespace",
			 "group", "#group:2"
		   });			
		addAnnotation
		  (getBeanType_Wiring(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "wiring",
			 "namespace", "##targetNamespace",
			 "group", "#group:2"
		   });			
		addAnnotation
		  (getBeanType_Expression(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "expression",
			 "namespace", "##targetNamespace",
			 "group", "#group:2"
		   });			
		addAnnotation
		  (getBeanType_BeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "beanId"
		   });			
		addAnnotation
		  (getBeanType_Class(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "class"
		   });			
		addAnnotation
		  (getBeanType_CreateOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "createOnElement"
		   });			
		addAnnotation
		  (getBeanType_CreateOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "createOnElementNS"
		   });			
		addAnnotation
		  (getBeanType_ExtendLifecycle(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "extendLifecycle"
		   });		
		addAnnotation
		  (decodeParamTypeEClass, 
		   source, 
		   new String[] {
			 "name", "decodeParam_._type",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getDecodeParamType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });			
		addAnnotation
		  (getDecodeParamType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (javabean12DocumentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getJavabean12DocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getJavabean12DocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getJavabean12DocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });			
		addAnnotation
		  (getJavabean12DocumentRoot_Bean(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "bean",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config"
		   });			
		addAnnotation
		  (getJavabean12DocumentRoot_DecodeParam(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "decodeParam",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getJavabean12DocumentRoot_Expression(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "expression",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getJavabean12DocumentRoot_Result(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "result",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config"
		   });			
		addAnnotation
		  (getJavabean12DocumentRoot_Value(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "value",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getJavabean12DocumentRoot_Wiring(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "wiring",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (expressionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "expression_._type",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getExpressionType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });			
		addAnnotation
		  (getExpressionType_ExecOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "execOnElement"
		   });			
		addAnnotation
		  (getExpressionType_ExecOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "execOnElementNS"
		   });			
		addAnnotation
		  (getExpressionType_InitVal(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "initVal"
		   });			
		addAnnotation
		  (getExpressionType_Property(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "property"
		   });			
		addAnnotation
		  (getExpressionType_SetterMethod(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "setterMethod"
		   });		
		addAnnotation
		  (resultTypeEClass, 
		   source, 
		   new String[] {
			 "name", "result_._type",
			 "kind", "mixed"
		   });			
		addAnnotation
		  (getResultType_RetainBeans(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "retainBeans"
		   });		
		addAnnotation
		  (valueTypeEClass, 
		   source, 
		   new String[] {
			 "name", "value_._type",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getValueType_DecodeParam(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "decodeParam",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getValueType_Data(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "data"
		   });			
		addAnnotation
		  (getValueType_DataNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dataNS"
		   });			
		addAnnotation
		  (getValueType_Decoder(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "decoder"
		   });			
		addAnnotation
		  (getValueType_Default(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "default"
		   });			
		addAnnotation
		  (getValueType_Property(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "property"
		   });			
		addAnnotation
		  (getValueType_SetterMethod(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "setterMethod"
		   });		
		addAnnotation
		  (wiringTypeEClass, 
		   source, 
		   new String[] {
			 "name", "wiring_._type",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getWiringType_BeanIdRef(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "beanIdRef"
		   });			
		addAnnotation
		  (getWiringType_Property(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "property"
		   });			
		addAnnotation
		  (getWiringType_SetterMethod(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "setterMethod"
		   });			
		addAnnotation
		  (getWiringType_WireOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "wireOnElement"
		   });			
		addAnnotation
		  (getWiringType_WireOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "wireOnElementNS"
		   });
	}

} //Javabean12PackageImpl
