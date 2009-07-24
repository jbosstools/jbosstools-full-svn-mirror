/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12;

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
 * Smooks Persistence Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Factory
 * @model kind="package"
 * @generated
 */
public interface Persistence12Package extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "persistence12";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/persistence-1.2.xsd";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "persistence";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Persistence12Package eINSTANCE = org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.DecoderParameterImpl <em>Decoder Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.DecoderParameterImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getDecoderParameter()
	 * @generated
	 */
	int DECODER_PARAMETER = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODER_PARAMETER__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODER_PARAMETER__NAME = 1;

	/**
	 * The number of structural features of the '<em>Decoder Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DECODER_PARAMETER_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl <em>Deleter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getDeleter()
	 * @generated
	 */
	int DELETER = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Dao</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__DAO = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Delete Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__DELETE_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Deleted Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__DELETED_BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Delete On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__DELETE_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Delete On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__DELETE_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER__NAME = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Deleter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DELETER_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getPersistence12DocumentRoot()
	 * @generated
	 */
	int PERSISTENCE12_DOCUMENT_ROOT = 2;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSISTENCE12_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSISTENCE12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSISTENCE12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Deleter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSISTENCE12_DOCUMENT_ROOT__DELETER = 3;

	/**
	 * The feature id for the '<em><b>Flusher</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSISTENCE12_DOCUMENT_ROOT__FLUSHER = 4;

	/**
	 * The feature id for the '<em><b>Inserter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSISTENCE12_DOCUMENT_ROOT__INSERTER = 5;

	/**
	 * The feature id for the '<em><b>Locator</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSISTENCE12_DOCUMENT_ROOT__LOCATOR = 6;

	/**
	 * The feature id for the '<em><b>Updater</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSISTENCE12_DOCUMENT_ROOT__UPDATER = 7;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PERSISTENCE12_DOCUMENT_ROOT_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.ExpressionParameterImpl <em>Expression Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.ExpressionParameterImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getExpressionParameter()
	 * @generated
	 */
	int EXPRESSION_PARAMETER = 3;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_PARAMETER__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Exec On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_PARAMETER__EXEC_ON_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Exec On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_PARAMETER__EXEC_ON_ELEMENT_NS = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_PARAMETER__NAME = 3;

	/**
	 * The number of structural features of the '<em>Expression Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPRESSION_PARAMETER_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.FlusherImpl <em>Flusher</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.FlusherImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getFlusher()
	 * @generated
	 */
	int FLUSHER = 4;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Dao</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER__DAO = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Flush Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER__FLUSH_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Flush On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER__FLUSH_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Flush On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER__FLUSH_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Flusher</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLUSHER_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.InserterImpl <em>Inserter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.InserterImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getInserter()
	 * @generated
	 */
	int INSERTER = 5;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Dao</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__DAO = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Insert Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__INSERT_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Inserted Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__INSERTED_BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Insert On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__INSERT_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Insert On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__INSERT_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER__NAME = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Inserter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTER_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl <em>Locator</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getLocator()
	 * @generated
	 */
	int LOCATOR = 6;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Query</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__QUERY = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Params</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__PARAMS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Dao</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__DAO = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Lookup</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__LOOKUP = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Lookup On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__LOOKUP_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Lookup On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__LOOKUP_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>On No Result</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__ON_NO_RESULT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Unique Result</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR__UNIQUE_RESULT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 8;

	/**
	 * The number of structural features of the '<em>Locator</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATOR_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 9;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.ParametersImpl <em>Parameters</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.ParametersImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getParameters()
	 * @generated
	 */
	int PARAMETERS = 7;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS__GROUP = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS__VALUE = 1;

	/**
	 * The feature id for the '<em><b>Wiring</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS__WIRING = 2;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS__EXPRESSION = 3;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS__TYPE = 4;

	/**
	 * The number of structural features of the '<em>Parameters</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETERS_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl <em>Updater</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getUpdater()
	 * @generated
	 */
	int UPDATER = 8;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Dao</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__DAO = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__NAME = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Update Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__UPDATE_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Updated Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__UPDATED_BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Update On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__UPDATE_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Update On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER__UPDATE_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Updater</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UPDATER_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl <em>Value Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getValueParameter()
	 * @generated
	 */
	int VALUE_PARAMETER = 9;

	/**
	 * The feature id for the '<em><b>Decode Param</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_PARAMETER__DECODE_PARAM = 0;

	/**
	 * The feature id for the '<em><b>Data</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_PARAMETER__DATA = 1;

	/**
	 * The feature id for the '<em><b>Data NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_PARAMETER__DATA_NS = 2;

	/**
	 * The feature id for the '<em><b>Decoder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_PARAMETER__DECODER = 3;

	/**
	 * The feature id for the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_PARAMETER__DEFAULT = 4;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_PARAMETER__INDEX = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_PARAMETER__NAME = 6;

	/**
	 * The number of structural features of the '<em>Value Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALUE_PARAMETER_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.impl.WiringParameterImpl <em>Wiring Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.impl.WiringParameterImpl
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getWiringParameter()
	 * @generated
	 */
	int WIRING_PARAMETER = 10;

	/**
	 * The feature id for the '<em><b>Bean Id Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_PARAMETER__BEAN_ID_REF = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_PARAMETER__NAME = 1;

	/**
	 * The feature id for the '<em><b>Wire On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_PARAMETER__WIRE_ON_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Wire On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_PARAMETER__WIRE_ON_ELEMENT_NS = 3;

	/**
	 * The number of structural features of the '<em>Wiring Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WIRING_PARAMETER_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.OnNoResult <em>On No Result</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.OnNoResult
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getOnNoResult()
	 * @generated
	 */
	int ON_NO_RESULT = 11;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.persistence12.ParameterType <em>Parameter Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.ParameterType
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getParameterType()
	 * @generated
	 */
	int PARAMETER_TYPE = 12;

	/**
	 * The meta object id for the '<em>On No Result Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.OnNoResult
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getOnNoResultObject()
	 * @generated
	 */
	int ON_NO_RESULT_OBJECT = 13;

	/**
	 * The meta object id for the '<em>Parameter Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.persistence12.ParameterType
	 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getParameterTypeObject()
	 * @generated
	 */
	int PARAMETER_TYPE_OBJECT = 14;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.DecoderParameter <em>Decoder Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Decoder Parameter</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.DecoderParameter
	 * @generated
	 */
	EClass getDecoderParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.DecoderParameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.DecoderParameter#getValue()
	 * @see #getDecoderParameter()
	 * @generated
	 */
	EAttribute getDecoderParameter_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.DecoderParameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.DecoderParameter#getName()
	 * @see #getDecoderParameter()
	 * @generated
	 */
	EAttribute getDecoderParameter_Name();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.Deleter <em>Deleter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deleter</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Deleter
	 * @generated
	 */
	EClass getDeleter();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Deleter#getBeanId()
	 * @see #getDeleter()
	 * @generated
	 */
	EAttribute getDeleter_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDao <em>Dao</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dao</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Deleter#getDao()
	 * @see #getDeleter()
	 * @generated
	 */
	EAttribute getDeleter_Dao();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Deleter#isDeleteBefore <em>Delete Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delete Before</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Deleter#isDeleteBefore()
	 * @see #getDeleter()
	 * @generated
	 */
	EAttribute getDeleter_DeleteBefore();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDeletedBeanId <em>Deleted Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Deleted Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Deleter#getDeletedBeanId()
	 * @see #getDeleter()
	 * @generated
	 */
	EAttribute getDeleter_DeletedBeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDeleteOnElement <em>Delete On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delete On Element</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Deleter#getDeleteOnElement()
	 * @see #getDeleter()
	 * @generated
	 */
	EAttribute getDeleter_DeleteOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDeleteOnElementNS <em>Delete On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delete On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Deleter#getDeleteOnElementNS()
	 * @see #getDeleter()
	 * @generated
	 */
	EAttribute getDeleter_DeleteOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Deleter#getName()
	 * @see #getDeleter()
	 * @generated
	 */
	EAttribute getDeleter_Name();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot
	 * @generated
	 */
	EClass getPersistence12DocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getMixed()
	 * @see #getPersistence12DocumentRoot()
	 * @generated
	 */
	EAttribute getPersistence12DocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getXMLNSPrefixMap()
	 * @see #getPersistence12DocumentRoot()
	 * @generated
	 */
	EReference getPersistence12DocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getXSISchemaLocation()
	 * @see #getPersistence12DocumentRoot()
	 * @generated
	 */
	EReference getPersistence12DocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getDeleter <em>Deleter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Deleter</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getDeleter()
	 * @see #getPersistence12DocumentRoot()
	 * @generated
	 */
	EReference getPersistence12DocumentRoot_Deleter();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getFlusher <em>Flusher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Flusher</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getFlusher()
	 * @see #getPersistence12DocumentRoot()
	 * @generated
	 */
	EReference getPersistence12DocumentRoot_Flusher();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getInserter <em>Inserter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Inserter</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getInserter()
	 * @see #getPersistence12DocumentRoot()
	 * @generated
	 */
	EReference getPersistence12DocumentRoot_Inserter();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getLocator <em>Locator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Locator</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getLocator()
	 * @see #getPersistence12DocumentRoot()
	 * @generated
	 */
	EReference getPersistence12DocumentRoot_Locator();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getUpdater <em>Updater</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Updater</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getUpdater()
	 * @see #getPersistence12DocumentRoot()
	 * @generated
	 */
	EReference getPersistence12DocumentRoot_Updater();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter <em>Expression Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Expression Parameter</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ExpressionParameter
	 * @generated
	 */
	EClass getExpressionParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getValue()
	 * @see #getExpressionParameter()
	 * @generated
	 */
	EAttribute getExpressionParameter_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getExecOnElement <em>Exec On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exec On Element</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getExecOnElement()
	 * @see #getExpressionParameter()
	 * @generated
	 */
	EAttribute getExpressionParameter_ExecOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getExecOnElementNS <em>Exec On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exec On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getExecOnElementNS()
	 * @see #getExpressionParameter()
	 * @generated
	 */
	EAttribute getExpressionParameter_ExecOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getName()
	 * @see #getExpressionParameter()
	 * @generated
	 */
	EAttribute getExpressionParameter_Name();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.Flusher <em>Flusher</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Flusher</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Flusher
	 * @generated
	 */
	EClass getFlusher();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Flusher#getDao <em>Dao</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dao</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Flusher#getDao()
	 * @see #getFlusher()
	 * @generated
	 */
	EAttribute getFlusher_Dao();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Flusher#isFlushBefore <em>Flush Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Flush Before</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Flusher#isFlushBefore()
	 * @see #getFlusher()
	 * @generated
	 */
	EAttribute getFlusher_FlushBefore();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Flusher#getFlushOnElement <em>Flush On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Flush On Element</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Flusher#getFlushOnElement()
	 * @see #getFlusher()
	 * @generated
	 */
	EAttribute getFlusher_FlushOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Flusher#getFlushOnElementNS <em>Flush On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Flush On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Flusher#getFlushOnElementNS()
	 * @see #getFlusher()
	 * @generated
	 */
	EAttribute getFlusher_FlushOnElementNS();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.Inserter <em>Inserter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Inserter</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Inserter
	 * @generated
	 */
	EClass getInserter();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Inserter#getBeanId()
	 * @see #getInserter()
	 * @generated
	 */
	EAttribute getInserter_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getDao <em>Dao</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dao</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Inserter#getDao()
	 * @see #getInserter()
	 * @generated
	 */
	EAttribute getInserter_Dao();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Inserter#isInsertBefore <em>Insert Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Insert Before</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Inserter#isInsertBefore()
	 * @see #getInserter()
	 * @generated
	 */
	EAttribute getInserter_InsertBefore();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getInsertedBeanId <em>Inserted Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Inserted Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Inserter#getInsertedBeanId()
	 * @see #getInserter()
	 * @generated
	 */
	EAttribute getInserter_InsertedBeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getInsertOnElement <em>Insert On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Insert On Element</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Inserter#getInsertOnElement()
	 * @see #getInserter()
	 * @generated
	 */
	EAttribute getInserter_InsertOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getInsertOnElementNS <em>Insert On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Insert On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Inserter#getInsertOnElementNS()
	 * @see #getInserter()
	 * @generated
	 */
	EAttribute getInserter_InsertOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Inserter#getName()
	 * @see #getInserter()
	 * @generated
	 */
	EAttribute getInserter_Name();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.Locator <em>Locator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Locator</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator
	 * @generated
	 */
	EClass getLocator();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Locator#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Query</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator#getQuery()
	 * @see #getLocator()
	 * @generated
	 */
	EAttribute getLocator_Query();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.persistence12.Locator#getParams <em>Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Params</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator#getParams()
	 * @see #getLocator()
	 * @generated
	 */
	EReference getLocator_Params();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Locator#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator#getBeanId()
	 * @see #getLocator()
	 * @generated
	 */
	EAttribute getLocator_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Locator#getDao <em>Dao</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dao</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator#getDao()
	 * @see #getLocator()
	 * @generated
	 */
	EAttribute getLocator_Dao();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Locator#getLookup <em>Lookup</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lookup</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator#getLookup()
	 * @see #getLocator()
	 * @generated
	 */
	EAttribute getLocator_Lookup();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Locator#getLookupOnElement <em>Lookup On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lookup On Element</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator#getLookupOnElement()
	 * @see #getLocator()
	 * @generated
	 */
	EAttribute getLocator_LookupOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Locator#getLookupOnElementNS <em>Lookup On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lookup On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator#getLookupOnElementNS()
	 * @see #getLocator()
	 * @generated
	 */
	EAttribute getLocator_LookupOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Locator#getOnNoResult <em>On No Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>On No Result</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator#getOnNoResult()
	 * @see #getLocator()
	 * @generated
	 */
	EAttribute getLocator_OnNoResult();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Locator#isUniqueResult <em>Unique Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unique Result</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator#isUniqueResult()
	 * @see #getLocator()
	 * @generated
	 */
	EAttribute getLocator_UniqueResult();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.Parameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameters</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Parameters
	 * @generated
	 */
	EClass getParameters();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.persistence12.Parameters#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Parameters#getGroup()
	 * @see #getParameters()
	 * @generated
	 */
	EAttribute getParameters_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.persistence12.Parameters#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Parameters#getValue()
	 * @see #getParameters()
	 * @generated
	 */
	EReference getParameters_Value();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.persistence12.Parameters#getWiring <em>Wiring</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Wiring</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Parameters#getWiring()
	 * @see #getParameters()
	 * @generated
	 */
	EReference getParameters_Wiring();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.persistence12.Parameters#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Expression</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Parameters#getExpression()
	 * @see #getParameters()
	 * @generated
	 */
	EReference getParameters_Expression();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Parameters#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Parameters#getType()
	 * @see #getParameters()
	 * @generated
	 */
	EAttribute getParameters_Type();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.Updater <em>Updater</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Updater</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Updater
	 * @generated
	 */
	EClass getUpdater();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Updater#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Updater#getBeanId()
	 * @see #getUpdater()
	 * @generated
	 */
	EAttribute getUpdater_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Updater#getDao <em>Dao</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dao</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Updater#getDao()
	 * @see #getUpdater()
	 * @generated
	 */
	EAttribute getUpdater_Dao();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Updater#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Updater#getName()
	 * @see #getUpdater()
	 * @generated
	 */
	EAttribute getUpdater_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Updater#isUpdateBefore <em>Update Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Update Before</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Updater#isUpdateBefore()
	 * @see #getUpdater()
	 * @generated
	 */
	EAttribute getUpdater_UpdateBefore();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Updater#getUpdatedBeanId <em>Updated Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Updated Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Updater#getUpdatedBeanId()
	 * @see #getUpdater()
	 * @generated
	 */
	EAttribute getUpdater_UpdatedBeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Updater#getUpdateOnElement <em>Update On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Update On Element</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Updater#getUpdateOnElement()
	 * @see #getUpdater()
	 * @generated
	 */
	EAttribute getUpdater_UpdateOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.Updater#getUpdateOnElementNS <em>Update On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Update On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.Updater#getUpdateOnElementNS()
	 * @see #getUpdater()
	 * @generated
	 */
	EAttribute getUpdater_UpdateOnElementNS();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter <em>Value Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Value Parameter</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ValueParameter
	 * @generated
	 */
	EClass getValueParameter();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDecodeParam <em>Decode Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Decode Param</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ValueParameter#getDecodeParam()
	 * @see #getValueParameter()
	 * @generated
	 */
	EReference getValueParameter_DecodeParam();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getData <em>Data</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ValueParameter#getData()
	 * @see #getValueParameter()
	 * @generated
	 */
	EAttribute getValueParameter_Data();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDataNS <em>Data NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data NS</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ValueParameter#getDataNS()
	 * @see #getValueParameter()
	 * @generated
	 */
	EAttribute getValueParameter_DataNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDecoder <em>Decoder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Decoder</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ValueParameter#getDecoder()
	 * @see #getValueParameter()
	 * @generated
	 */
	EAttribute getValueParameter_Decoder();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDefault <em>Default</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ValueParameter#getDefault()
	 * @see #getValueParameter()
	 * @generated
	 */
	EAttribute getValueParameter_Default();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ValueParameter#getIndex()
	 * @see #getValueParameter()
	 * @generated
	 */
	EAttribute getValueParameter_Index();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ValueParameter#getName()
	 * @see #getValueParameter()
	 * @generated
	 */
	EAttribute getValueParameter_Name();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter <em>Wiring Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Wiring Parameter</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.WiringParameter
	 * @generated
	 */
	EClass getWiringParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getBeanIdRef <em>Bean Id Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id Ref</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.WiringParameter#getBeanIdRef()
	 * @see #getWiringParameter()
	 * @generated
	 */
	EAttribute getWiringParameter_BeanIdRef();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.WiringParameter#getName()
	 * @see #getWiringParameter()
	 * @generated
	 */
	EAttribute getWiringParameter_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getWireOnElement <em>Wire On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wire On Element</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.WiringParameter#getWireOnElement()
	 * @see #getWiringParameter()
	 * @generated
	 */
	EAttribute getWiringParameter_WireOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getWireOnElementNS <em>Wire On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Wire On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.WiringParameter#getWireOnElementNS()
	 * @see #getWiringParameter()
	 * @generated
	 */
	EAttribute getWiringParameter_WireOnElementNS();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.persistence12.OnNoResult <em>On No Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>On No Result</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.OnNoResult
	 * @generated
	 */
	EEnum getOnNoResult();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.persistence12.ParameterType <em>Parameter Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Parameter Type</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ParameterType
	 * @generated
	 */
	EEnum getParameterType();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.persistence12.OnNoResult <em>On No Result Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>On No Result Object</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.OnNoResult
	 * @model instanceClass="persistence12.OnNoResult"
	 *        extendedMetaData="name='OnNoResult:Object' baseType='OnNoResult'"
	 * @generated
	 */
	EDataType getOnNoResultObject();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.persistence12.ParameterType <em>Parameter Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Parameter Type Object</em>'.
	 * @see org.jboss.tools.smooks.model.persistence12.ParameterType
	 * @model instanceClass="persistence12.ParameterType"
	 *        extendedMetaData="name='parameterType:Object' baseType='parameterType'"
	 * @generated
	 */
	EDataType getParameterTypeObject();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Persistence12Factory getPersistence12Factory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.DecoderParameterImpl <em>Decoder Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.DecoderParameterImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getDecoderParameter()
		 * @generated
		 */
		EClass DECODER_PARAMETER = eINSTANCE.getDecoderParameter();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DECODER_PARAMETER__VALUE = eINSTANCE.getDecoderParameter_Value();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DECODER_PARAMETER__NAME = eINSTANCE.getDecoderParameter_Name();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl <em>Deleter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getDeleter()
		 * @generated
		 */
		EClass DELETER = eINSTANCE.getDeleter();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELETER__BEAN_ID = eINSTANCE.getDeleter_BeanId();

		/**
		 * The meta object literal for the '<em><b>Dao</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELETER__DAO = eINSTANCE.getDeleter_Dao();

		/**
		 * The meta object literal for the '<em><b>Delete Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELETER__DELETE_BEFORE = eINSTANCE.getDeleter_DeleteBefore();

		/**
		 * The meta object literal for the '<em><b>Deleted Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELETER__DELETED_BEAN_ID = eINSTANCE.getDeleter_DeletedBeanId();

		/**
		 * The meta object literal for the '<em><b>Delete On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELETER__DELETE_ON_ELEMENT = eINSTANCE.getDeleter_DeleteOnElement();

		/**
		 * The meta object literal for the '<em><b>Delete On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELETER__DELETE_ON_ELEMENT_NS = eINSTANCE.getDeleter_DeleteOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DELETER__NAME = eINSTANCE.getDeleter_Name();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getPersistence12DocumentRoot()
		 * @generated
		 */
		EClass PERSISTENCE12_DOCUMENT_ROOT = eINSTANCE.getPersistence12DocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PERSISTENCE12_DOCUMENT_ROOT__MIXED = eINSTANCE.getPersistence12DocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERSISTENCE12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getPersistence12DocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERSISTENCE12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getPersistence12DocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Deleter</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERSISTENCE12_DOCUMENT_ROOT__DELETER = eINSTANCE.getPersistence12DocumentRoot_Deleter();

		/**
		 * The meta object literal for the '<em><b>Flusher</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERSISTENCE12_DOCUMENT_ROOT__FLUSHER = eINSTANCE.getPersistence12DocumentRoot_Flusher();

		/**
		 * The meta object literal for the '<em><b>Inserter</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERSISTENCE12_DOCUMENT_ROOT__INSERTER = eINSTANCE.getPersistence12DocumentRoot_Inserter();

		/**
		 * The meta object literal for the '<em><b>Locator</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERSISTENCE12_DOCUMENT_ROOT__LOCATOR = eINSTANCE.getPersistence12DocumentRoot_Locator();

		/**
		 * The meta object literal for the '<em><b>Updater</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PERSISTENCE12_DOCUMENT_ROOT__UPDATER = eINSTANCE.getPersistence12DocumentRoot_Updater();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.ExpressionParameterImpl <em>Expression Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.ExpressionParameterImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getExpressionParameter()
		 * @generated
		 */
		EClass EXPRESSION_PARAMETER = eINSTANCE.getExpressionParameter();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_PARAMETER__VALUE = eINSTANCE.getExpressionParameter_Value();

		/**
		 * The meta object literal for the '<em><b>Exec On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_PARAMETER__EXEC_ON_ELEMENT = eINSTANCE.getExpressionParameter_ExecOnElement();

		/**
		 * The meta object literal for the '<em><b>Exec On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_PARAMETER__EXEC_ON_ELEMENT_NS = eINSTANCE.getExpressionParameter_ExecOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPRESSION_PARAMETER__NAME = eINSTANCE.getExpressionParameter_Name();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.FlusherImpl <em>Flusher</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.FlusherImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getFlusher()
		 * @generated
		 */
		EClass FLUSHER = eINSTANCE.getFlusher();

		/**
		 * The meta object literal for the '<em><b>Dao</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLUSHER__DAO = eINSTANCE.getFlusher_Dao();

		/**
		 * The meta object literal for the '<em><b>Flush Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLUSHER__FLUSH_BEFORE = eINSTANCE.getFlusher_FlushBefore();

		/**
		 * The meta object literal for the '<em><b>Flush On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLUSHER__FLUSH_ON_ELEMENT = eINSTANCE.getFlusher_FlushOnElement();

		/**
		 * The meta object literal for the '<em><b>Flush On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLUSHER__FLUSH_ON_ELEMENT_NS = eINSTANCE.getFlusher_FlushOnElementNS();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.InserterImpl <em>Inserter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.InserterImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getInserter()
		 * @generated
		 */
		EClass INSERTER = eINSTANCE.getInserter();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTER__BEAN_ID = eINSTANCE.getInserter_BeanId();

		/**
		 * The meta object literal for the '<em><b>Dao</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTER__DAO = eINSTANCE.getInserter_Dao();

		/**
		 * The meta object literal for the '<em><b>Insert Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTER__INSERT_BEFORE = eINSTANCE.getInserter_InsertBefore();

		/**
		 * The meta object literal for the '<em><b>Inserted Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTER__INSERTED_BEAN_ID = eINSTANCE.getInserter_InsertedBeanId();

		/**
		 * The meta object literal for the '<em><b>Insert On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTER__INSERT_ON_ELEMENT = eINSTANCE.getInserter_InsertOnElement();

		/**
		 * The meta object literal for the '<em><b>Insert On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTER__INSERT_ON_ELEMENT_NS = eINSTANCE.getInserter_InsertOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTER__NAME = eINSTANCE.getInserter_Name();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl <em>Locator</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getLocator()
		 * @generated
		 */
		EClass LOCATOR = eINSTANCE.getLocator();

		/**
		 * The meta object literal for the '<em><b>Query</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATOR__QUERY = eINSTANCE.getLocator_Query();

		/**
		 * The meta object literal for the '<em><b>Params</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LOCATOR__PARAMS = eINSTANCE.getLocator_Params();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATOR__BEAN_ID = eINSTANCE.getLocator_BeanId();

		/**
		 * The meta object literal for the '<em><b>Dao</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATOR__DAO = eINSTANCE.getLocator_Dao();

		/**
		 * The meta object literal for the '<em><b>Lookup</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATOR__LOOKUP = eINSTANCE.getLocator_Lookup();

		/**
		 * The meta object literal for the '<em><b>Lookup On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATOR__LOOKUP_ON_ELEMENT = eINSTANCE.getLocator_LookupOnElement();

		/**
		 * The meta object literal for the '<em><b>Lookup On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATOR__LOOKUP_ON_ELEMENT_NS = eINSTANCE.getLocator_LookupOnElementNS();

		/**
		 * The meta object literal for the '<em><b>On No Result</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATOR__ON_NO_RESULT = eINSTANCE.getLocator_OnNoResult();

		/**
		 * The meta object literal for the '<em><b>Unique Result</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATOR__UNIQUE_RESULT = eINSTANCE.getLocator_UniqueResult();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.ParametersImpl <em>Parameters</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.ParametersImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getParameters()
		 * @generated
		 */
		EClass PARAMETERS = eINSTANCE.getParameters();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETERS__GROUP = eINSTANCE.getParameters_Group();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETERS__VALUE = eINSTANCE.getParameters_Value();

		/**
		 * The meta object literal for the '<em><b>Wiring</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETERS__WIRING = eINSTANCE.getParameters_Wiring();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMETERS__EXPRESSION = eINSTANCE.getParameters_Expression();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETERS__TYPE = eINSTANCE.getParameters_Type();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl <em>Updater</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getUpdater()
		 * @generated
		 */
		EClass UPDATER = eINSTANCE.getUpdater();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UPDATER__BEAN_ID = eINSTANCE.getUpdater_BeanId();

		/**
		 * The meta object literal for the '<em><b>Dao</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UPDATER__DAO = eINSTANCE.getUpdater_Dao();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UPDATER__NAME = eINSTANCE.getUpdater_Name();

		/**
		 * The meta object literal for the '<em><b>Update Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UPDATER__UPDATE_BEFORE = eINSTANCE.getUpdater_UpdateBefore();

		/**
		 * The meta object literal for the '<em><b>Updated Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UPDATER__UPDATED_BEAN_ID = eINSTANCE.getUpdater_UpdatedBeanId();

		/**
		 * The meta object literal for the '<em><b>Update On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UPDATER__UPDATE_ON_ELEMENT = eINSTANCE.getUpdater_UpdateOnElement();

		/**
		 * The meta object literal for the '<em><b>Update On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UPDATER__UPDATE_ON_ELEMENT_NS = eINSTANCE.getUpdater_UpdateOnElementNS();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl <em>Value Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getValueParameter()
		 * @generated
		 */
		EClass VALUE_PARAMETER = eINSTANCE.getValueParameter();

		/**
		 * The meta object literal for the '<em><b>Decode Param</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALUE_PARAMETER__DECODE_PARAM = eINSTANCE.getValueParameter_DecodeParam();

		/**
		 * The meta object literal for the '<em><b>Data</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_PARAMETER__DATA = eINSTANCE.getValueParameter_Data();

		/**
		 * The meta object literal for the '<em><b>Data NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_PARAMETER__DATA_NS = eINSTANCE.getValueParameter_DataNS();

		/**
		 * The meta object literal for the '<em><b>Decoder</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_PARAMETER__DECODER = eINSTANCE.getValueParameter_Decoder();

		/**
		 * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_PARAMETER__DEFAULT = eINSTANCE.getValueParameter_Default();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_PARAMETER__INDEX = eINSTANCE.getValueParameter_Index();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALUE_PARAMETER__NAME = eINSTANCE.getValueParameter_Name();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.impl.WiringParameterImpl <em>Wiring Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.impl.WiringParameterImpl
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getWiringParameter()
		 * @generated
		 */
		EClass WIRING_PARAMETER = eINSTANCE.getWiringParameter();

		/**
		 * The meta object literal for the '<em><b>Bean Id Ref</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING_PARAMETER__BEAN_ID_REF = eINSTANCE.getWiringParameter_BeanIdRef();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING_PARAMETER__NAME = eINSTANCE.getWiringParameter_Name();

		/**
		 * The meta object literal for the '<em><b>Wire On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING_PARAMETER__WIRE_ON_ELEMENT = eINSTANCE.getWiringParameter_WireOnElement();

		/**
		 * The meta object literal for the '<em><b>Wire On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WIRING_PARAMETER__WIRE_ON_ELEMENT_NS = eINSTANCE.getWiringParameter_WireOnElementNS();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.OnNoResult <em>On No Result</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.OnNoResult
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getOnNoResult()
		 * @generated
		 */
		EEnum ON_NO_RESULT = eINSTANCE.getOnNoResult();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.persistence12.ParameterType <em>Parameter Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.ParameterType
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getParameterType()
		 * @generated
		 */
		EEnum PARAMETER_TYPE = eINSTANCE.getParameterType();

		/**
		 * The meta object literal for the '<em>On No Result Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.OnNoResult
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getOnNoResultObject()
		 * @generated
		 */
		EDataType ON_NO_RESULT_OBJECT = eINSTANCE.getOnNoResultObject();

		/**
		 * The meta object literal for the '<em>Parameter Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.persistence12.ParameterType
		 * @see org.jboss.tools.smooks.model.persistence12.impl.Persistence12PackageImpl#getParameterTypeObject()
		 * @generated
		 */
		EDataType PARAMETER_TYPE_OBJECT = eINSTANCE.getParameterTypeObject();

	}

} //Persistence12Package
