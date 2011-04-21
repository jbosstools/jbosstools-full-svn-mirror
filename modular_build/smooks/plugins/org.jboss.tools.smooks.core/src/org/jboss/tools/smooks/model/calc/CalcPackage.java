/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.calc;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.jboss.tools.smooks.model.smooks.SmooksPackage;

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
 * <!-- begin-model-doc -->
 * Smooks Calculation Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.calc.CalcFactory
 * @model kind="package"
 * @generated
 */
public interface CalcPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "calc"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/calc-1.1.xsd"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "calc"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CalcPackage eINSTANCE = org.jboss.tools.smooks.model.calc.impl.CalcPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl <em>Counter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.calc.impl.CounterImpl
	 * @see org.jboss.tools.smooks.model.calc.impl.CalcPackageImpl#getCounter()
	 * @generated
	 */
	int COUNTER = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Start Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__START_EXPRESSION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Amount Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__AMOUNT_EXPRESSION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Reset Condition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__RESET_CONDITION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__AMOUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Count On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__COUNT_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Count On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__COUNT_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__DIRECTION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Execute After</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__EXECUTE_AFTER = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER__START = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Counter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COUNTER_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 10;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.calc.impl.CalcDocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.calc.impl.CalcDocumentRootImpl
	 * @see org.jboss.tools.smooks.model.calc.impl.CalcPackageImpl#getCalcDocumentRoot()
	 * @generated
	 */
	int CALC_DOCUMENT_ROOT = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALC_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALC_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALC_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Counter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALC_DOCUMENT_ROOT__COUNTER = 3;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALC_DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.calc.CountDirection <em>Count Direction</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.calc.CountDirection
	 * @see org.jboss.tools.smooks.model.calc.impl.CalcPackageImpl#getCountDirection()
	 * @generated
	 */
	int COUNT_DIRECTION = 2;

	/**
	 * The meta object id for the '<em>Count Direction Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.calc.CountDirection
	 * @see org.jboss.tools.smooks.model.calc.impl.CalcPackageImpl#getCountDirectionObject()
	 * @generated
	 */
	int COUNT_DIRECTION_OBJECT = 3;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.calc.Counter <em>Counter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Counter</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter
	 * @generated
	 */
	EClass getCounter();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#getStartExpression <em>Start Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Expression</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#getStartExpression()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_StartExpression();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#getAmountExpression <em>Amount Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Amount Expression</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#getAmountExpression()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_AmountExpression();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#getResetCondition <em>Reset Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Reset Condition</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#getResetCondition()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_ResetCondition();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#getAmount <em>Amount</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Amount</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#getAmount()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_Amount();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#getBeanId()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#getCountOnElement <em>Count On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Count On Element</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#getCountOnElement()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_CountOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#getCountOnElementNS <em>Count On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Count On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#getCountOnElementNS()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_CountOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#getDirection <em>Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#getDirection()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_Direction();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#isExecuteAfter <em>Execute After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute After</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#isExecuteAfter()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_ExecuteAfter();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.calc.Counter#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see org.jboss.tools.smooks.model.calc.Counter#getStart()
	 * @see #getCounter()
	 * @generated
	 */
	EAttribute getCounter_Start();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.calc.CalcDocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.calc.CalcDocumentRoot
	 * @generated
	 */
	EClass getCalcDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.calc.CalcDocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.calc.CalcDocumentRoot#getMixed()
	 * @see #getCalcDocumentRoot()
	 * @generated
	 */
	EAttribute getCalcDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.calc.CalcDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.calc.CalcDocumentRoot#getXMLNSPrefixMap()
	 * @see #getCalcDocumentRoot()
	 * @generated
	 */
	EReference getCalcDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.calc.CalcDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.calc.CalcDocumentRoot#getXSISchemaLocation()
	 * @see #getCalcDocumentRoot()
	 * @generated
	 */
	EReference getCalcDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.calc.CalcDocumentRoot#getCounter <em>Counter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Counter</em>'.
	 * @see org.jboss.tools.smooks.model.calc.CalcDocumentRoot#getCounter()
	 * @see #getCalcDocumentRoot()
	 * @generated
	 */
	EReference getCalcDocumentRoot_Counter();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.calc.CountDirection <em>Count Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Count Direction</em>'.
	 * @see org.jboss.tools.smooks.model.calc.CountDirection
	 * @generated
	 */
	EEnum getCountDirection();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.calc.CountDirection <em>Count Direction Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Count Direction Object</em>'.
	 * @see org.jboss.tools.smooks.model.calc.CountDirection
	 * @model instanceClass="org.jboss.tools.smooks.model.calc.CountDirection"
	 *        extendedMetaData="name='CountDirection:Object' baseType='CountDirection'"
	 * @generated
	 */
	EDataType getCountDirectionObject();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CalcFactory getCalcFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl <em>Counter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.calc.impl.CounterImpl
		 * @see org.jboss.tools.smooks.model.calc.impl.CalcPackageImpl#getCounter()
		 * @generated
		 */
		EClass COUNTER = eINSTANCE.getCounter();

		/**
		 * The meta object literal for the '<em><b>Start Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__START_EXPRESSION = eINSTANCE.getCounter_StartExpression();

		/**
		 * The meta object literal for the '<em><b>Amount Expression</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__AMOUNT_EXPRESSION = eINSTANCE.getCounter_AmountExpression();

		/**
		 * The meta object literal for the '<em><b>Reset Condition</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__RESET_CONDITION = eINSTANCE.getCounter_ResetCondition();

		/**
		 * The meta object literal for the '<em><b>Amount</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__AMOUNT = eINSTANCE.getCounter_Amount();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__BEAN_ID = eINSTANCE.getCounter_BeanId();

		/**
		 * The meta object literal for the '<em><b>Count On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__COUNT_ON_ELEMENT = eINSTANCE.getCounter_CountOnElement();

		/**
		 * The meta object literal for the '<em><b>Count On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__COUNT_ON_ELEMENT_NS = eINSTANCE.getCounter_CountOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Direction</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__DIRECTION = eINSTANCE.getCounter_Direction();

		/**
		 * The meta object literal for the '<em><b>Execute After</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__EXECUTE_AFTER = eINSTANCE.getCounter_ExecuteAfter();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COUNTER__START = eINSTANCE.getCounter_Start();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.calc.impl.CalcDocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.calc.impl.CalcDocumentRootImpl
		 * @see org.jboss.tools.smooks.model.calc.impl.CalcPackageImpl#getCalcDocumentRoot()
		 * @generated
		 */
		EClass CALC_DOCUMENT_ROOT = eINSTANCE.getCalcDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CALC_DOCUMENT_ROOT__MIXED = eINSTANCE.getCalcDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CALC_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getCalcDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CALC_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getCalcDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Counter</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CALC_DOCUMENT_ROOT__COUNTER = eINSTANCE.getCalcDocumentRoot_Counter();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.calc.CountDirection <em>Count Direction</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.calc.CountDirection
		 * @see org.jboss.tools.smooks.model.calc.impl.CalcPackageImpl#getCountDirection()
		 * @generated
		 */
		EEnum COUNT_DIRECTION = eINSTANCE.getCountDirection();

		/**
		 * The meta object literal for the '<em>Count Direction Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.calc.CountDirection
		 * @see org.jboss.tools.smooks.model.calc.impl.CalcPackageImpl#getCountDirectionObject()
		 * @generated
		 */
		EDataType COUNT_DIRECTION_OBJECT = eINSTANCE.getCountDirectionObject();

	}

} //CalcPackage
