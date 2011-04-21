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
import org.jboss.tools.smooks.model.common.CommonPackage;
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
 * Smooks Java Binding Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.javabean.JavabeanFactory
 * @model kind="package"
 * @generated
 */
public interface JavabeanPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "javabean"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/javabean-1.1.xsd"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "jb"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	JavabeanPackage eINSTANCE = org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.impl.BindingsTypeImpl <em>Bindings Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.impl.BindingsTypeImpl
	 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getBindingsType()
	 * @generated
	 */
	int BINDINGS_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__GROUP = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__VALUE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Wiring</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__WIRING = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__EXPRESSION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__CLASS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Create On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__CREATE_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Create On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__CREATE_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Extend Lifecycle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE__EXTEND_LIFECYCLE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 8;

	/**
	 * The number of structural features of the '<em>Bindings Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BINDINGS_TYPE_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 9;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.impl.DecodeParamTypeImpl <em>Decode Param Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.impl.DecodeParamTypeImpl
	 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getDecodeParamType()
	 * @generated
	 */
	int DECODE_PARAM_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODE_PARAM_TYPE__MIXED = CommonPackage.ABSTRACT_ANY_TYPE__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODE_PARAM_TYPE__ANY = CommonPackage.ABSTRACT_ANY_TYPE__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODE_PARAM_TYPE__ANY_ATTRIBUTE = CommonPackage.ABSTRACT_ANY_TYPE__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODE_PARAM_TYPE__VALUE = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODE_PARAM_TYPE__NAME = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Decode Param Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODE_PARAM_TYPE_FEATURE_COUNT = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.impl.DocumentRootImpl
	 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 2;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Bindings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__BINDINGS = 3;

	/**
	 * The feature id for the '<em><b>Decode Param</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__DECODE_PARAM = 4;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__EXPRESSION = 5;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__VALUE = 6;

	/**
	 * The feature id for the '<em><b>Wiring</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__WIRING = 7;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.impl.ExpressionTypeImpl <em>Expression Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.impl.ExpressionTypeImpl
	 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getExpressionType()
	 * @generated
	 */
	int EXPRESSION_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_TYPE__MIXED = CommonPackage.ABSTRACT_ANY_TYPE__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_TYPE__ANY = CommonPackage.ABSTRACT_ANY_TYPE__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_TYPE__ANY_ATTRIBUTE = CommonPackage.ABSTRACT_ANY_TYPE__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_TYPE__VALUE = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Exec On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_TYPE__EXEC_ON_ELEMENT = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Exec On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_TYPE__EXEC_ON_ELEMENT_NS = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_TYPE__PROPERTY = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Setter Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_TYPE__SETTER_METHOD = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Expression Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_TYPE_FEATURE_COUNT = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.impl.ValueTypeImpl <em>Value Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.impl.ValueTypeImpl
	 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getValueType()
	 * @generated
	 */
	int VALUE_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__MIXED = CommonPackage.ABSTRACT_ANY_TYPE__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__ANY = CommonPackage.ABSTRACT_ANY_TYPE__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__ANY_ATTRIBUTE = CommonPackage.ABSTRACT_ANY_TYPE__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Decode Param</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__DECODE_PARAM = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Data</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__DATA = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Data NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__DATA_NS = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Decoder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__DECODER = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__DEFAULT = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__PROPERTY = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Setter Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE__SETTER_METHOD = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Value Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_TYPE_FEATURE_COUNT = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.javabean.impl.WiringTypeImpl <em>Wiring Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.javabean.impl.WiringTypeImpl
	 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getWiringType()
	 * @generated
	 */
	int WIRING_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_TYPE__MIXED = CommonPackage.ABSTRACT_ANY_TYPE__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_TYPE__ANY = CommonPackage.ABSTRACT_ANY_TYPE__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_TYPE__ANY_ATTRIBUTE = CommonPackage.ABSTRACT_ANY_TYPE__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Bean Id Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_TYPE__BEAN_ID_REF = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_TYPE__PROPERTY = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Setter Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_TYPE__SETTER_METHOD = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Wire On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_TYPE__WIRE_ON_ELEMENT = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Wire On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_TYPE__WIRE_ON_ELEMENT_NS = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Wiring Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_TYPE_FEATURE_COUNT = CommonPackage.ABSTRACT_ANY_TYPE_FEATURE_COUNT + 5;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.BindingsType <em>Bindings Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bindings Type</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType
	 * @generated
	 */
	EClass getBindingsType();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.javabean.BindingsType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType#getGroup()
	 * @see #getBindingsType()
	 * @generated
	 */
	EAttribute getBindingsType_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.javabean.BindingsType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType#getValue()
	 * @see #getBindingsType()
	 * @generated
	 */
	EReference getBindingsType_Value();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.javabean.BindingsType#getWiring <em>Wiring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Wiring</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType#getWiring()
	 * @see #getBindingsType()
	 * @generated
	 */
	EReference getBindingsType_Wiring();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.javabean.BindingsType#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Expression</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType#getExpression()
	 * @see #getBindingsType()
	 * @generated
	 */
	EReference getBindingsType_Expression();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.BindingsType#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType#getBeanId()
	 * @see #getBindingsType()
	 * @generated
	 */
	EAttribute getBindingsType_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.BindingsType#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Class</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType#getClass_()
	 * @see #getBindingsType()
	 * @generated
	 */
	EAttribute getBindingsType_Class();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.BindingsType#getCreateOnElement <em>Create On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Create On Element</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType#getCreateOnElement()
	 * @see #getBindingsType()
	 * @generated
	 */
	EAttribute getBindingsType_CreateOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.BindingsType#getCreateOnElementNS <em>Create On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Create On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType#getCreateOnElementNS()
	 * @see #getBindingsType()
	 * @generated
	 */
	EAttribute getBindingsType_CreateOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.BindingsType#isExtendLifecycle <em>Extend Lifecycle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Extend Lifecycle</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.BindingsType#isExtendLifecycle()
	 * @see #getBindingsType()
	 * @generated
	 */
	EAttribute getBindingsType_ExtendLifecycle();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.DecodeParamType <em>Decode Param Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Decode Param Type</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DecodeParamType
	 * @generated
	 */
	EClass getDecodeParamType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.DecodeParamType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DecodeParamType#getValue()
	 * @see #getDecodeParamType()
	 * @generated
	 */
	EAttribute getDecodeParamType_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.DecodeParamType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DecodeParamType#getName()
	 * @see #getDecodeParamType()
	 * @generated
	 */
	EAttribute getDecodeParamType_Name();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.javabean.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.javabean.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.javabean.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.javabean.DocumentRoot#getBindings <em>Bindings</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Bindings</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DocumentRoot#getBindings()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Bindings();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.javabean.DocumentRoot#getDecodeParam <em>Decode Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Decode Param</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DocumentRoot#getDecodeParam()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_DecodeParam();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.javabean.DocumentRoot#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Expression</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DocumentRoot#getExpression()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Expression();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.javabean.DocumentRoot#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DocumentRoot#getValue()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Value();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.javabean.DocumentRoot#getWiring <em>Wiring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Wiring</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.DocumentRoot#getWiring()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Wiring();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.ExpressionType <em>Expression Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Expression Type</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ExpressionType
	 * @generated
	 */
	EClass getExpressionType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ExpressionType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ExpressionType#getValue()
	 * @see #getExpressionType()
	 * @generated
	 */
	EAttribute getExpressionType_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ExpressionType#getExecOnElement <em>Exec On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exec On Element</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ExpressionType#getExecOnElement()
	 * @see #getExpressionType()
	 * @generated
	 */
	EAttribute getExpressionType_ExecOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ExpressionType#getExecOnElementNS <em>Exec On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exec On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ExpressionType#getExecOnElementNS()
	 * @see #getExpressionType()
	 * @generated
	 */
	EAttribute getExpressionType_ExecOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ExpressionType#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ExpressionType#getProperty()
	 * @see #getExpressionType()
	 * @generated
	 */
	EAttribute getExpressionType_Property();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ExpressionType#getSetterMethod <em>Setter Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Setter Method</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ExpressionType#getSetterMethod()
	 * @see #getExpressionType()
	 * @generated
	 */
	EAttribute getExpressionType_SetterMethod();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.ValueType <em>Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Value Type</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ValueType
	 * @generated
	 */
	EClass getValueType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.javabean.ValueType#getDecodeParam <em>Decode Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Decode Param</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ValueType#getDecodeParam()
	 * @see #getValueType()
	 * @generated
	 */
	EReference getValueType_DecodeParam();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ValueType#getData <em>Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ValueType#getData()
	 * @see #getValueType()
	 * @generated
	 */
	EAttribute getValueType_Data();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ValueType#getDataNS <em>Data NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data NS</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ValueType#getDataNS()
	 * @see #getValueType()
	 * @generated
	 */
	EAttribute getValueType_DataNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ValueType#getDecoder <em>Decoder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Decoder</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ValueType#getDecoder()
	 * @see #getValueType()
	 * @generated
	 */
	EAttribute getValueType_Decoder();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ValueType#getDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ValueType#getDefault()
	 * @see #getValueType()
	 * @generated
	 */
	EAttribute getValueType_Default();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ValueType#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ValueType#getProperty()
	 * @see #getValueType()
	 * @generated
	 */
	EAttribute getValueType_Property();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.ValueType#getSetterMethod <em>Setter Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Setter Method</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.ValueType#getSetterMethod()
	 * @see #getValueType()
	 * @generated
	 */
	EAttribute getValueType_SetterMethod();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.javabean.WiringType <em>Wiring Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Wiring Type</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.WiringType
	 * @generated
	 */
	EClass getWiringType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.WiringType#getBeanIdRef <em>Bean Id Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id Ref</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.WiringType#getBeanIdRef()
	 * @see #getWiringType()
	 * @generated
	 */
	EAttribute getWiringType_BeanIdRef();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.WiringType#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Property</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.WiringType#getProperty()
	 * @see #getWiringType()
	 * @generated
	 */
	EAttribute getWiringType_Property();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.WiringType#getSetterMethod <em>Setter Method</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Setter Method</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.WiringType#getSetterMethod()
	 * @see #getWiringType()
	 * @generated
	 */
	EAttribute getWiringType_SetterMethod();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.WiringType#getWireOnElement <em>Wire On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wire On Element</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.WiringType#getWireOnElement()
	 * @see #getWiringType()
	 * @generated
	 */
	EAttribute getWiringType_WireOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.javabean.WiringType#getWireOnElementNS <em>Wire On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wire On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.javabean.WiringType#getWireOnElementNS()
	 * @see #getWiringType()
	 * @generated
	 */
	EAttribute getWiringType_WireOnElementNS();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	JavabeanFactory getJavabeanFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.impl.BindingsTypeImpl <em>Bindings Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.impl.BindingsTypeImpl
		 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getBindingsType()
		 * @generated
		 */
		EClass BINDINGS_TYPE = eINSTANCE.getBindingsType();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BINDINGS_TYPE__GROUP = eINSTANCE.getBindingsType_Group();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BINDINGS_TYPE__VALUE = eINSTANCE.getBindingsType_Value();

		/**
		 * The meta object literal for the '<em><b>Wiring</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BINDINGS_TYPE__WIRING = eINSTANCE.getBindingsType_Wiring();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BINDINGS_TYPE__EXPRESSION = eINSTANCE.getBindingsType_Expression();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BINDINGS_TYPE__BEAN_ID = eINSTANCE.getBindingsType_BeanId();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BINDINGS_TYPE__CLASS = eINSTANCE.getBindingsType_Class();

		/**
		 * The meta object literal for the '<em><b>Create On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BINDINGS_TYPE__CREATE_ON_ELEMENT = eINSTANCE.getBindingsType_CreateOnElement();

		/**
		 * The meta object literal for the '<em><b>Create On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BINDINGS_TYPE__CREATE_ON_ELEMENT_NS = eINSTANCE.getBindingsType_CreateOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Extend Lifecycle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BINDINGS_TYPE__EXTEND_LIFECYCLE = eINSTANCE.getBindingsType_ExtendLifecycle();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.impl.DecodeParamTypeImpl <em>Decode Param Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.impl.DecodeParamTypeImpl
		 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getDecodeParamType()
		 * @generated
		 */
		EClass DECODE_PARAM_TYPE = eINSTANCE.getDecodeParamType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DECODE_PARAM_TYPE__VALUE = eINSTANCE.getDecodeParamType_Value();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DECODE_PARAM_TYPE__NAME = eINSTANCE.getDecodeParamType_Name();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.impl.DocumentRootImpl
		 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getDocumentRoot()
		 * @generated
		 */
		EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Bindings</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__BINDINGS = eINSTANCE.getDocumentRoot_Bindings();

		/**
		 * The meta object literal for the '<em><b>Decode Param</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__DECODE_PARAM = eINSTANCE.getDocumentRoot_DecodeParam();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__EXPRESSION = eINSTANCE.getDocumentRoot_Expression();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__VALUE = eINSTANCE.getDocumentRoot_Value();

		/**
		 * The meta object literal for the '<em><b>Wiring</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__WIRING = eINSTANCE.getDocumentRoot_Wiring();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.impl.ExpressionTypeImpl <em>Expression Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.impl.ExpressionTypeImpl
		 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getExpressionType()
		 * @generated
		 */
		EClass EXPRESSION_TYPE = eINSTANCE.getExpressionType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_TYPE__VALUE = eINSTANCE.getExpressionType_Value();

		/**
		 * The meta object literal for the '<em><b>Exec On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_TYPE__EXEC_ON_ELEMENT = eINSTANCE.getExpressionType_ExecOnElement();

		/**
		 * The meta object literal for the '<em><b>Exec On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_TYPE__EXEC_ON_ELEMENT_NS = eINSTANCE.getExpressionType_ExecOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_TYPE__PROPERTY = eINSTANCE.getExpressionType_Property();

		/**
		 * The meta object literal for the '<em><b>Setter Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_TYPE__SETTER_METHOD = eINSTANCE.getExpressionType_SetterMethod();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.impl.ValueTypeImpl <em>Value Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.impl.ValueTypeImpl
		 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getValueType()
		 * @generated
		 */
		EClass VALUE_TYPE = eINSTANCE.getValueType();

		/**
		 * The meta object literal for the '<em><b>Decode Param</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALUE_TYPE__DECODE_PARAM = eINSTANCE.getValueType_DecodeParam();

		/**
		 * The meta object literal for the '<em><b>Data</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_TYPE__DATA = eINSTANCE.getValueType_Data();

		/**
		 * The meta object literal for the '<em><b>Data NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_TYPE__DATA_NS = eINSTANCE.getValueType_DataNS();

		/**
		 * The meta object literal for the '<em><b>Decoder</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_TYPE__DECODER = eINSTANCE.getValueType_Decoder();

		/**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_TYPE__DEFAULT = eINSTANCE.getValueType_Default();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_TYPE__PROPERTY = eINSTANCE.getValueType_Property();

		/**
		 * The meta object literal for the '<em><b>Setter Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_TYPE__SETTER_METHOD = eINSTANCE.getValueType_SetterMethod();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.javabean.impl.WiringTypeImpl <em>Wiring Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.javabean.impl.WiringTypeImpl
		 * @see org.jboss.tools.smooks.model.javabean.impl.JavabeanPackageImpl#getWiringType()
		 * @generated
		 */
		EClass WIRING_TYPE = eINSTANCE.getWiringType();

		/**
		 * The meta object literal for the '<em><b>Bean Id Ref</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING_TYPE__BEAN_ID_REF = eINSTANCE.getWiringType_BeanIdRef();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING_TYPE__PROPERTY = eINSTANCE.getWiringType_Property();

		/**
		 * The meta object literal for the '<em><b>Setter Method</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING_TYPE__SETTER_METHOD = eINSTANCE.getWiringType_SetterMethod();

		/**
		 * The meta object literal for the '<em><b>Wire On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING_TYPE__WIRE_ON_ELEMENT = eINSTANCE.getWiringType_WireOnElement();

		/**
		 * The meta object literal for the '<em><b>Wire On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING_TYPE__WIRE_ON_ELEMENT_NS = eINSTANCE.getWiringType_WireOnElementNS();

	}

} //JavabeanPackage
