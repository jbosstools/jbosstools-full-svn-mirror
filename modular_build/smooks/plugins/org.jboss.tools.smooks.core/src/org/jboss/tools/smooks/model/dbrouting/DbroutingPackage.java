/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.dbrouting;

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
 * Smooks SQL Routing Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingFactory
 * @model kind="package"
 * @generated
 */
public interface DbroutingPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "dbrouting"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/db-routing-1.1.xsd"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "db"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DbroutingPackage eINSTANCE = org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.dbrouting.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.DocumentRootImpl
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 0;

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
	 * The feature id for the '<em><b>Executor</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__EXECUTOR = 3;

	/**
	 * The feature id for the '<em><b>Result Set Row Selector</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__RESULT_SET_ROW_SELECTOR = 4;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl <em>Executor</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getExecutor()
	 * @generated
	 */
	int EXECUTOR = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Statement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__STATEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Result Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__RESULT_SET = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Datasource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__DATASOURCE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Execute Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__EXECUTE_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Execute On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__EXECUTE_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Execute On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR__EXECUTE_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Executor</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTOR_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetImpl <em>Result Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.ResultSetImpl
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getResultSet()
	 * @generated
	 */
	int RESULT_SET = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET__NAME = 0;

	/**
	 * The feature id for the '<em><b>Scope</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET__SCOPE = 1;

	/**
	 * The feature id for the '<em><b>Time To Live</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET__TIME_TO_LIVE = 2;

	/**
	 * The number of structural features of the '<em>Result Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl <em>Result Set Row Selector</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getResultSetRowSelector()
	 * @generated
	 */
	int RESULT_SET_ROW_SELECTOR = 3;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Where</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__WHERE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Failed Select Error</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__FAILED_SELECT_ERROR = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Execute Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__EXECUTE_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Result Set Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__RESULT_SET_NAME = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Select Row On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR__SELECT_ROW_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Result Set Row Selector</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESULT_SET_ROW_SELECTOR_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType <em>Result Set Scope Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getResultSetScopeType()
	 * @generated
	 */
	int RESULT_SET_SCOPE_TYPE = 4;

	/**
	 * The meta object id for the '<em>Result Set Scope Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType
	 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getResultSetScopeTypeObject()
	 * @generated
	 */
	int RESULT_SET_SCOPE_TYPE_OBJECT = 5;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.dbrouting.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getExecutor <em>Executor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Executor</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getExecutor()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Executor();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getResultSetRowSelector <em>Result Set Row Selector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Result Set Row Selector</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.DocumentRoot#getResultSetRowSelector()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_ResultSetRowSelector();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.dbrouting.Executor <em>Executor</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Executor</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.Executor
	 * @generated
	 */
	EClass getExecutor();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getStatement <em>Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Statement</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.Executor#getStatement()
	 * @see #getExecutor()
	 * @generated
	 */
	EAttribute getExecutor_Statement();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getResultSet <em>Result Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Result Set</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.Executor#getResultSet()
	 * @see #getExecutor()
	 * @generated
	 */
	EReference getExecutor_ResultSet();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getDatasource <em>Datasource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Datasource</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.Executor#getDatasource()
	 * @see #getExecutor()
	 * @generated
	 */
	EAttribute getExecutor_Datasource();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.Executor#isExecuteBefore <em>Execute Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute Before</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.Executor#isExecuteBefore()
	 * @see #getExecutor()
	 * @generated
	 */
	EAttribute getExecutor_ExecuteBefore();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getExecuteOnElement <em>Execute On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute On Element</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.Executor#getExecuteOnElement()
	 * @see #getExecutor()
	 * @generated
	 */
	EAttribute getExecutor_ExecuteOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getExecuteOnElementNS <em>Execute On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.Executor#getExecuteOnElementNS()
	 * @see #getExecutor()
	 * @generated
	 */
	EAttribute getExecutor_ExecuteOnElementNS();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet <em>Result Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Result Set</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSet
	 * @generated
	 */
	EClass getResultSet();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSet#getName()
	 * @see #getResultSet()
	 * @generated
	 */
	EAttribute getResultSet_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getScope <em>Scope</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scope</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSet#getScope()
	 * @see #getResultSet()
	 * @generated
	 */
	EAttribute getResultSet_Scope();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getTimeToLive <em>Time To Live</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time To Live</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSet#getTimeToLive()
	 * @see #getResultSet()
	 * @generated
	 */
	EAttribute getResultSet_TimeToLive();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector <em>Result Set Row Selector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Result Set Row Selector</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector
	 * @generated
	 */
	EClass getResultSetRowSelector();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getWhere <em>Where</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Where</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getWhere()
	 * @see #getResultSetRowSelector()
	 * @generated
	 */
	EAttribute getResultSetRowSelector_Where();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getFailedSelectError <em>Failed Select Error</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Failed Select Error</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getFailedSelectError()
	 * @see #getResultSetRowSelector()
	 * @generated
	 */
	EAttribute getResultSetRowSelector_FailedSelectError();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getBeanId()
	 * @see #getResultSetRowSelector()
	 * @generated
	 */
	EAttribute getResultSetRowSelector_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#isExecuteBefore <em>Execute Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute Before</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#isExecuteBefore()
	 * @see #getResultSetRowSelector()
	 * @generated
	 */
	EAttribute getResultSetRowSelector_ExecuteBefore();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getResultSetName <em>Result Set Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Result Set Name</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getResultSetName()
	 * @see #getResultSetRowSelector()
	 * @generated
	 */
	EAttribute getResultSetRowSelector_ResultSetName();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getSelectRowOnElement <em>Select Row On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Select Row On Element</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getSelectRowOnElement()
	 * @see #getResultSetRowSelector()
	 * @generated
	 */
	EAttribute getResultSetRowSelector_SelectRowOnElement();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType <em>Result Set Scope Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Result Set Scope Type</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType
	 * @generated
	 */
	EEnum getResultSetScopeType();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType <em>Result Set Scope Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Result Set Scope Type Object</em>'.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType
	 * @model instanceClass="dbrouting.ResultSetScopeType"
	 *        extendedMetaData="name='resultSetScopeType:Object' baseType='resultSetScopeType'"
	 * @generated
	 */
	EDataType getResultSetScopeTypeObject();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DbroutingFactory getDbroutingFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.dbrouting.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.DocumentRootImpl
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getDocumentRoot()
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
		 * The meta object literal for the '<em><b>Executor</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__EXECUTOR = eINSTANCE.getDocumentRoot_Executor();

		/**
		 * The meta object literal for the '<em><b>Result Set Row Selector</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__RESULT_SET_ROW_SELECTOR = eINSTANCE.getDocumentRoot_ResultSetRowSelector();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl <em>Executor</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getExecutor()
		 * @generated
		 */
		EClass EXECUTOR = eINSTANCE.getExecutor();

		/**
		 * The meta object literal for the '<em><b>Statement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXECUTOR__STATEMENT = eINSTANCE.getExecutor_Statement();

		/**
		 * The meta object literal for the '<em><b>Result Set</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTOR__RESULT_SET = eINSTANCE.getExecutor_ResultSet();

		/**
		 * The meta object literal for the '<em><b>Datasource</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXECUTOR__DATASOURCE = eINSTANCE.getExecutor_Datasource();

		/**
		 * The meta object literal for the '<em><b>Execute Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXECUTOR__EXECUTE_BEFORE = eINSTANCE.getExecutor_ExecuteBefore();

		/**
		 * The meta object literal for the '<em><b>Execute On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXECUTOR__EXECUTE_ON_ELEMENT = eINSTANCE.getExecutor_ExecuteOnElement();

		/**
		 * The meta object literal for the '<em><b>Execute On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXECUTOR__EXECUTE_ON_ELEMENT_NS = eINSTANCE.getExecutor_ExecuteOnElementNS();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetImpl <em>Result Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.ResultSetImpl
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getResultSet()
		 * @generated
		 */
		EClass RESULT_SET = eINSTANCE.getResultSet();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET__NAME = eINSTANCE.getResultSet_Name();

		/**
		 * The meta object literal for the '<em><b>Scope</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET__SCOPE = eINSTANCE.getResultSet_Scope();

		/**
		 * The meta object literal for the '<em><b>Time To Live</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET__TIME_TO_LIVE = eINSTANCE.getResultSet_TimeToLive();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl <em>Result Set Row Selector</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getResultSetRowSelector()
		 * @generated
		 */
		EClass RESULT_SET_ROW_SELECTOR = eINSTANCE.getResultSetRowSelector();

		/**
		 * The meta object literal for the '<em><b>Where</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET_ROW_SELECTOR__WHERE = eINSTANCE.getResultSetRowSelector_Where();

		/**
		 * The meta object literal for the '<em><b>Failed Select Error</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET_ROW_SELECTOR__FAILED_SELECT_ERROR = eINSTANCE.getResultSetRowSelector_FailedSelectError();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET_ROW_SELECTOR__BEAN_ID = eINSTANCE.getResultSetRowSelector_BeanId();

		/**
		 * The meta object literal for the '<em><b>Execute Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET_ROW_SELECTOR__EXECUTE_BEFORE = eINSTANCE.getResultSetRowSelector_ExecuteBefore();

		/**
		 * The meta object literal for the '<em><b>Result Set Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET_ROW_SELECTOR__RESULT_SET_NAME = eINSTANCE.getResultSetRowSelector_ResultSetName();

		/**
		 * The meta object literal for the '<em><b>Select Row On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESULT_SET_ROW_SELECTOR__SELECT_ROW_ON_ELEMENT = eINSTANCE.getResultSetRowSelector_SelectRowOnElement();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType <em>Result Set Scope Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getResultSetScopeType()
		 * @generated
		 */
		EEnum RESULT_SET_SCOPE_TYPE = eINSTANCE.getResultSetScopeType();

		/**
		 * The meta object literal for the '<em>Result Set Scope Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType
		 * @see org.jboss.tools.smooks.model.dbrouting.impl.DbroutingPackageImpl#getResultSetScopeTypeObject()
		 * @generated
		 */
		EDataType RESULT_SET_SCOPE_TYPE_OBJECT = eINSTANCE.getResultSetScopeTypeObject();

	}

} //DbroutingPackage
