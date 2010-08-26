/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.jboss.tools.smooks.model.core.CorePackage;
import org.jboss.tools.smooks.model.core.ICorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JavaBeanPackage extends EPackageImpl implements IJavaBeanPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass decodeParamEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass wiringEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass expressionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass valueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass beanEClass = null;

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
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private JavaBeanPackage() {
		super(eNS_URI, IJavaBeanFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link IJavaBeanPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static IJavaBeanPackage init() {
		if (isInited) return (IJavaBeanPackage)EPackage.Registry.INSTANCE.getEPackage(IJavaBeanPackage.eNS_URI);

		// Obtain or create and register package
		JavaBeanPackage theJavaBeanPackage = (JavaBeanPackage)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof JavaBeanPackage ? EPackage.Registry.INSTANCE.get(eNS_URI) : new JavaBeanPackage());

		isInited = true;

		// Obtain or create and register interdependencies
		CorePackage theCorePackage = (CorePackage)(EPackage.Registry.INSTANCE.getEPackage(ICorePackage.eNS_URI) instanceof CorePackage ? EPackage.Registry.INSTANCE.getEPackage(ICorePackage.eNS_URI) : ICorePackage.eINSTANCE);

		// Create package meta-data objects
		theJavaBeanPackage.createPackageContents();
		theCorePackage.createPackageContents();

		// Initialize created meta-data
		theJavaBeanPackage.initializePackageContents();
		theCorePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJavaBeanPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(IJavaBeanPackage.eNS_URI, theJavaBeanPackage);
		return theJavaBeanPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDecodeParam() {
		return decodeParamEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDecodeParam_Name() {
		return (EAttribute)decodeParamEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDecodeParam_Value() {
		return (EAttribute)decodeParamEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWiring() {
		return wiringEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiring_Property() {
		return (EAttribute)wiringEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiring_SetterMethod() {
		return (EAttribute)wiringEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiring_BeanIdRef() {
		return (EAttribute)wiringEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiring_WireOnElement() {
		return (EAttribute)wiringEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiring_WireOnElementNS() {
		return (EAttribute)wiringEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExpression() {
		return expressionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpression_Property() {
		return (EAttribute)expressionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpression_SetterMethod() {
		return (EAttribute)expressionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpression_ExecOnElement() {
		return (EAttribute)expressionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpression_ExecOnElementNS() {
		return (EAttribute)expressionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpression_InitVal() {
		return (EAttribute)expressionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getValue() {
		return valueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValue_Property() {
		return (EAttribute)valueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValue_SetterMethod() {
		return (EAttribute)valueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValue_Data() {
		return (EAttribute)valueEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValue_DataNS() {
		return (EAttribute)valueEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValue_Decoder() {
		return (EAttribute)valueEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValue_DefaultVal() {
		return (EAttribute)valueEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getValue_DecodeParams() {
		return (EReference)valueEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getBean() {
		return beanEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBean_BeanId() {
		return (EAttribute)beanEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBean_BeanClass() {
		return (EAttribute)beanEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBean_CreateOnElement() {
		return (EAttribute)beanEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getBean_CreateOnElementNS() {
		return (EAttribute)beanEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBean_ValueBindings() {
		return (EReference)beanEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBean_WireBindings() {
		return (EReference)beanEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getBean_ExpressionBindings() {
		return (EReference)beanEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IJavaBeanFactory getJavaBeanFactory() {
		return (IJavaBeanFactory)getEFactoryInstance();
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
		decodeParamEClass = createEClass(DECODE_PARAM);
		createEAttribute(decodeParamEClass, DECODE_PARAM__NAME);
		createEAttribute(decodeParamEClass, DECODE_PARAM__VALUE);

		wiringEClass = createEClass(WIRING);
		createEAttribute(wiringEClass, WIRING__PROPERTY);
		createEAttribute(wiringEClass, WIRING__SETTER_METHOD);
		createEAttribute(wiringEClass, WIRING__BEAN_ID_REF);
		createEAttribute(wiringEClass, WIRING__WIRE_ON_ELEMENT);
		createEAttribute(wiringEClass, WIRING__WIRE_ON_ELEMENT_NS);

		expressionEClass = createEClass(EXPRESSION);
		createEAttribute(expressionEClass, EXPRESSION__PROPERTY);
		createEAttribute(expressionEClass, EXPRESSION__SETTER_METHOD);
		createEAttribute(expressionEClass, EXPRESSION__EXEC_ON_ELEMENT);
		createEAttribute(expressionEClass, EXPRESSION__EXEC_ON_ELEMENT_NS);
		createEAttribute(expressionEClass, EXPRESSION__INIT_VAL);

		valueEClass = createEClass(VALUE);
		createEAttribute(valueEClass, VALUE__PROPERTY);
		createEAttribute(valueEClass, VALUE__SETTER_METHOD);
		createEAttribute(valueEClass, VALUE__DATA);
		createEAttribute(valueEClass, VALUE__DATA_NS);
		createEAttribute(valueEClass, VALUE__DECODER);
		createEAttribute(valueEClass, VALUE__DEFAULT_VAL);
		createEReference(valueEClass, VALUE__DECODE_PARAMS);

		beanEClass = createEClass(BEAN);
		createEAttribute(beanEClass, BEAN__BEAN_ID);
		createEAttribute(beanEClass, BEAN__BEAN_CLASS);
		createEAttribute(beanEClass, BEAN__CREATE_ON_ELEMENT);
		createEAttribute(beanEClass, BEAN__CREATE_ON_ELEMENT_NS);
		createEReference(beanEClass, BEAN__VALUE_BINDINGS);
		createEReference(beanEClass, BEAN__WIRE_BINDINGS);
		createEReference(beanEClass, BEAN__EXPRESSION_BINDINGS);
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
		ICorePackage theCorePackage = (ICorePackage)EPackage.Registry.INSTANCE.getEPackage(ICorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		beanEClass.getESuperTypes().add(theCorePackage.getComponent());

		// Initialize classes and features; add operations and parameters
		initEClass(decodeParamEClass, IDecodeParam.class, "DecodeParam", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDecodeParam_Name(), ecorePackage.getEString(), "name", null, 1, 1, IDecodeParam.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDecodeParam_Value(), ecorePackage.getEString(), "value", null, 1, 1, IDecodeParam.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wiringEClass, IWiring.class, "Wiring", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getWiring_Property(), ecorePackage.getEString(), "property", null, 1, 1, IWiring.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiring_SetterMethod(), ecorePackage.getEString(), "setterMethod", null, 1, 1, IWiring.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiring_BeanIdRef(), ecorePackage.getEString(), "beanIdRef", null, 1, 1, IWiring.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiring_WireOnElement(), ecorePackage.getEString(), "wireOnElement", null, 1, 1, IWiring.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiring_WireOnElementNS(), ecorePackage.getEString(), "wireOnElementNS", null, 1, 1, IWiring.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(expressionEClass, IExpression.class, "Expression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExpression_Property(), ecorePackage.getEString(), "property", null, 1, 1, IExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpression_SetterMethod(), ecorePackage.getEString(), "setterMethod", null, 1, 1, IExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpression_ExecOnElement(), ecorePackage.getEString(), "execOnElement", null, 1, 1, IExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpression_ExecOnElementNS(), ecorePackage.getEString(), "execOnElementNS", null, 0, 1, IExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpression_InitVal(), ecorePackage.getEString(), "initVal", null, 1, 1, IExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(valueEClass, IValue.class, "Value", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getValue_Property(), ecorePackage.getEString(), "property", null, 1, 1, IValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValue_SetterMethod(), ecorePackage.getEString(), "setterMethod", null, 1, 1, IValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValue_Data(), ecorePackage.getEString(), "data", null, 1, 1, IValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValue_DataNS(), ecorePackage.getEString(), "dataNS", null, 1, 1, IValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValue_Decoder(), ecorePackage.getEString(), "decoder", null, 1, 1, IValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValue_DefaultVal(), ecorePackage.getEString(), "defaultVal", null, 1, 1, IValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getValue_DecodeParams(), this.getDecodeParam(), null, "decodeParams", null, 0, -1, IValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(beanEClass, IBean.class, "Bean", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBean_BeanId(), ecorePackage.getEString(), "beanId", null, 0, 1, IBean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBean_BeanClass(), ecorePackage.getEString(), "beanClass", null, 0, 1, IBean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBean_CreateOnElement(), ecorePackage.getEString(), "createOnElement", null, 0, 1, IBean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBean_CreateOnElementNS(), ecorePackage.getEString(), "createOnElementNS", null, 0, 1, IBean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBean_ValueBindings(), this.getValue(), null, "valueBindings", null, 0, -1, IBean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBean_WireBindings(), this.getWiring(), null, "wireBindings", null, 0, -1, IBean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBean_ExpressionBindings(), this.getExpression(), null, "expressionBindings", null, 0, -1, IBean.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //JavaBeanPackage
