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

import org.jboss.tools.smooks.model.core.ICorePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanFactory
 * @model kind="package"
 * @generated
 */
public interface IJavaBeanPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "javabean";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/smooks-ui/javabean/1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "javabean";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	IJavaBeanPackage eINSTANCE = org.jboss.tools.smooks.model.javabean.JavaBeanPackage.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.DecodeParam <em>Decode Param</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.DecodeParam
	 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getDecodeParam()
	 * @generated
	 */
	int DECODE_PARAM = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODE_PARAM__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODE_PARAM__VALUE = 1;

	/**
	 * The number of structural features of the '<em>Decode Param</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODE_PARAM_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.Wiring <em>Wiring</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.Wiring
	 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getWiring()
	 * @generated
	 */
	int WIRING = 1;

	/**
	 * The feature id for the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING__PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Setter Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING__SETTER_METHOD = 1;

	/**
	 * The feature id for the '<em><b>Bean Id Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING__BEAN_ID_REF = 2;

	/**
	 * The feature id for the '<em><b>Wire On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING__WIRE_ON_ELEMENT = 3;

	/**
	 * The feature id for the '<em><b>Wire On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING__WIRE_ON_ELEMENT_NS = 4;

	/**
	 * The number of structural features of the '<em>Wiring</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.Expression <em>Expression</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.Expression
	 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getExpression()
	 * @generated
	 */
	int EXPRESSION = 2;

	/**
	 * The feature id for the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION__PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Setter Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION__SETTER_METHOD = 1;

	/**
	 * The feature id for the '<em><b>Exec On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION__EXEC_ON_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Exec On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION__EXEC_ON_ELEMENT_NS = 3;

	/**
	 * The feature id for the '<em><b>Init Val</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION__INIT_VAL = 4;

	/**
	 * The number of structural features of the '<em>Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.Value <em>Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.Value
	 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getValue()
	 * @generated
	 */
	int VALUE = 3;

	/**
	 * The feature id for the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE__PROPERTY = 0;

	/**
	 * The feature id for the '<em><b>Setter Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE__SETTER_METHOD = 1;

	/**
	 * The feature id for the '<em><b>Data</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE__DATA = 2;

	/**
	 * The feature id for the '<em><b>Data NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE__DATA_NS = 3;

	/**
	 * The feature id for the '<em><b>Decoder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE__DECODER = 4;

	/**
	 * The feature id for the '<em><b>Default Val</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE__DEFAULT_VAL = 5;

	/**
	 * The feature id for the '<em><b>Decode Params</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE__DECODE_PARAMS = 6;

	/**
	 * The number of structural features of the '<em>Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.Bean <em>Bean</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.Bean
	 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getBean()
	 * @generated
	 */
	int BEAN = 4;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__BEAN_ID = ICorePackage.COMPONENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Bean Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__BEAN_CLASS = ICorePackage.COMPONENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Create On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__CREATE_ON_ELEMENT = ICorePackage.COMPONENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Create On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__CREATE_ON_ELEMENT_NS = ICorePackage.COMPONENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Value Bindings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__VALUE_BINDINGS = ICorePackage.COMPONENT_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Wire Bindings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__WIRE_BINDINGS = ICorePackage.COMPONENT_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Expression Bindings</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN__EXPRESSION_BINDINGS = ICorePackage.COMPONENT_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Bean</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BEAN_FEATURE_COUNT = ICorePackage.COMPONENT_FEATURE_COUNT + 7;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.IDecodeParam <em>Decode Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Decode Param</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IDecodeParam
	 * @generated
	 */
	EClass getDecodeParam();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IDecodeParam#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IDecodeParam#getName()
	 * @see #getDecodeParam()
	 * @generated
	 */
	EAttribute getDecodeParam_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IDecodeParam#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IDecodeParam#getValue()
	 * @see #getDecodeParam()
	 * @generated
	 */
	EAttribute getDecodeParam_Value();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.IWiring <em>Wiring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Wiring</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IWiring
	 * @generated
	 */
	EClass getWiring();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IWiring#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IWiring#getProperty()
	 * @see #getWiring()
	 * @generated
	 */
	EAttribute getWiring_Property();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IWiring#getSetterMethod <em>Setter Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Setter Method</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IWiring#getSetterMethod()
	 * @see #getWiring()
	 * @generated
	 */
	EAttribute getWiring_SetterMethod();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IWiring#getBeanIdRef <em>Bean Id Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id Ref</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IWiring#getBeanIdRef()
	 * @see #getWiring()
	 * @generated
	 */
	EAttribute getWiring_BeanIdRef();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IWiring#getWireOnElement <em>Wire On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wire On Element</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IWiring#getWireOnElement()
	 * @see #getWiring()
	 * @generated
	 */
	EAttribute getWiring_WireOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IWiring#getWireOnElementNS <em>Wire On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wire On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IWiring#getWireOnElementNS()
	 * @see #getWiring()
	 * @generated
	 */
	EAttribute getWiring_WireOnElementNS();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.IExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Expression</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IExpression
	 * @generated
	 */
	EClass getExpression();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IExpression#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IExpression#getProperty()
	 * @see #getExpression()
	 * @generated
	 */
	EAttribute getExpression_Property();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IExpression#getSetterMethod <em>Setter Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Setter Method</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IExpression#getSetterMethod()
	 * @see #getExpression()
	 * @generated
	 */
	EAttribute getExpression_SetterMethod();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IExpression#getExecOnElement <em>Exec On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exec On Element</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IExpression#getExecOnElement()
	 * @see #getExpression()
	 * @generated
	 */
	EAttribute getExpression_ExecOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IExpression#getExecOnElementNS <em>Exec On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exec On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IExpression#getExecOnElementNS()
	 * @see #getExpression()
	 * @generated
	 */
	EAttribute getExpression_ExecOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IExpression#getInitVal <em>Init Val</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Init Val</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IExpression#getInitVal()
	 * @see #getExpression()
	 * @generated
	 */
	EAttribute getExpression_InitVal();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.IValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IValue
	 * @generated
	 */
	EClass getValue();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IValue#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IValue#getProperty()
	 * @see #getValue()
	 * @generated
	 */
	EAttribute getValue_Property();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IValue#getSetterMethod <em>Setter Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Setter Method</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IValue#getSetterMethod()
	 * @see #getValue()
	 * @generated
	 */
	EAttribute getValue_SetterMethod();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IValue#getData <em>Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IValue#getData()
	 * @see #getValue()
	 * @generated
	 */
	EAttribute getValue_Data();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IValue#getDataNS <em>Data NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data NS</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IValue#getDataNS()
	 * @see #getValue()
	 * @generated
	 */
	EAttribute getValue_DataNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IValue#getDecoder <em>Decoder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Decoder</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IValue#getDecoder()
	 * @see #getValue()
	 * @generated
	 */
	EAttribute getValue_Decoder();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IValue#getDefaultVal <em>Default Val</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Val</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IValue#getDefaultVal()
	 * @see #getValue()
	 * @generated
	 */
	EAttribute getValue_DefaultVal();

	/**
	 * Returns the meta object for the reference list '{@link org.jboss.tools.smooks.model.javabean.IValue#getDecodeParams <em>Decode Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Decode Params</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IValue#getDecodeParams()
	 * @see #getValue()
	 * @generated
	 */
	EReference getValue_DecodeParams();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.IBean <em>Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bean</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IBean
	 * @generated
	 */
	EClass getBean();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IBean#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IBean#getBeanId()
	 * @see #getBean()
	 * @generated
	 */
	EAttribute getBean_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IBean#getBeanClass <em>Bean Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Class</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IBean#getBeanClass()
	 * @see #getBean()
	 * @generated
	 */
	EAttribute getBean_BeanClass();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IBean#getCreateOnElement <em>Create On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Create On Element</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IBean#getCreateOnElement()
	 * @see #getBean()
	 * @generated
	 */
	EAttribute getBean_CreateOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.IBean#getCreateOnElementNS <em>Create On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Create On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IBean#getCreateOnElementNS()
	 * @see #getBean()
	 * @generated
	 */
	EAttribute getBean_CreateOnElementNS();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.javabean.IBean#getValueBindings <em>Value Bindings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Value Bindings</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IBean#getValueBindings()
	 * @see #getBean()
	 * @generated
	 */
	EReference getBean_ValueBindings();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.javabean.IBean#getWireBindings <em>Wire Bindings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Wire Bindings</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IBean#getWireBindings()
	 * @see #getBean()
	 * @generated
	 */
	EReference getBean_WireBindings();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.javabean.IBean#getExpressionBindings <em>Expression Bindings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Expression Bindings</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.IBean#getExpressionBindings()
	 * @see #getBean()
	 * @generated
	 */
	EReference getBean_ExpressionBindings();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	IJavaBeanFactory getJavaBeanFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.DecodeParam <em>Decode Param</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.DecodeParam
		 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getDecodeParam()
		 * @generated
		 */
		EClass DECODE_PARAM = eINSTANCE.getDecodeParam();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DECODE_PARAM__NAME = eINSTANCE.getDecodeParam_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DECODE_PARAM__VALUE = eINSTANCE.getDecodeParam_Value();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.Wiring <em>Wiring</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.Wiring
		 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getWiring()
		 * @generated
		 */
		EClass WIRING = eINSTANCE.getWiring();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING__PROPERTY = eINSTANCE.getWiring_Property();

		/**
		 * The meta object literal for the '<em><b>Setter Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING__SETTER_METHOD = eINSTANCE.getWiring_SetterMethod();

		/**
		 * The meta object literal for the '<em><b>Bean Id Ref</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING__BEAN_ID_REF = eINSTANCE.getWiring_BeanIdRef();

		/**
		 * The meta object literal for the '<em><b>Wire On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING__WIRE_ON_ELEMENT = eINSTANCE.getWiring_WireOnElement();

		/**
		 * The meta object literal for the '<em><b>Wire On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING__WIRE_ON_ELEMENT_NS = eINSTANCE.getWiring_WireOnElementNS();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.Expression <em>Expression</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.Expression
		 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getExpression()
		 * @generated
		 */
		EClass EXPRESSION = eINSTANCE.getExpression();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION__PROPERTY = eINSTANCE.getExpression_Property();

		/**
		 * The meta object literal for the '<em><b>Setter Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION__SETTER_METHOD = eINSTANCE.getExpression_SetterMethod();

		/**
		 * The meta object literal for the '<em><b>Exec On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION__EXEC_ON_ELEMENT = eINSTANCE.getExpression_ExecOnElement();

		/**
		 * The meta object literal for the '<em><b>Exec On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION__EXEC_ON_ELEMENT_NS = eINSTANCE.getExpression_ExecOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Init Val</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION__INIT_VAL = eINSTANCE.getExpression_InitVal();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.Value <em>Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.Value
		 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getValue()
		 * @generated
		 */
		EClass VALUE = eINSTANCE.getValue();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE__PROPERTY = eINSTANCE.getValue_Property();

		/**
		 * The meta object literal for the '<em><b>Setter Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE__SETTER_METHOD = eINSTANCE.getValue_SetterMethod();

		/**
		 * The meta object literal for the '<em><b>Data</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE__DATA = eINSTANCE.getValue_Data();

		/**
		 * The meta object literal for the '<em><b>Data NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE__DATA_NS = eINSTANCE.getValue_DataNS();

		/**
		 * The meta object literal for the '<em><b>Decoder</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE__DECODER = eINSTANCE.getValue_Decoder();

		/**
		 * The meta object literal for the '<em><b>Default Val</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE__DEFAULT_VAL = eINSTANCE.getValue_DefaultVal();

		/**
		 * The meta object literal for the '<em><b>Decode Params</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALUE__DECODE_PARAMS = eINSTANCE.getValue_DecodeParams();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.Bean <em>Bean</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.Bean
		 * @see org.jboss.tools.smooks.model.javabean.JavaBeanPackage#getBean()
		 * @generated
		 */
		EClass BEAN = eINSTANCE.getBean();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BEAN__BEAN_ID = eINSTANCE.getBean_BeanId();

		/**
		 * The meta object literal for the '<em><b>Bean Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BEAN__BEAN_CLASS = eINSTANCE.getBean_BeanClass();

		/**
		 * The meta object literal for the '<em><b>Create On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BEAN__CREATE_ON_ELEMENT = eINSTANCE.getBean_CreateOnElement();

		/**
		 * The meta object literal for the '<em><b>Create On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BEAN__CREATE_ON_ELEMENT_NS = eINSTANCE.getBean_CreateOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Value Bindings</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BEAN__VALUE_BINDINGS = eINSTANCE.getBean_ValueBindings();

		/**
		 * The meta object literal for the '<em><b>Wire Bindings</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BEAN__WIRE_BINDINGS = eINSTANCE.getBean_WireBindings();

		/**
		 * The meta object literal for the '<em><b>Expression Bindings</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BEAN__EXPRESSION_BINDINGS = eINSTANCE.getBean_ExpressionBindings();

	}

} //IJavaBeanPackage
