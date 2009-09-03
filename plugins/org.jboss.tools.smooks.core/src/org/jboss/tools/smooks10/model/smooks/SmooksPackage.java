/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

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
 * @see smooks.SmooksFactory
 * @model kind="package"
 * @generated
 */
public interface SmooksPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "smooks";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks-1.0.xsd";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "smooks";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SmooksPackage eINSTANCE = org.jboss.tools.smooks10.model.smooks.impl.SmooksPackageImpl.init();

	/**
	 * The meta object id for the '{@link smooks.impl.AbstractResourceConfigImpl <em>Abstract Resource Config</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smooks.impl.AbstractResourceConfigImpl
	 * @see smooks.impl.SmooksPackageImpl#getAbstractResourceConfig()
	 * @generated
	 */
	int ABSTRACT_RESOURCE_CONFIG = 0;

	/**
	 * The number of structural features of the '<em>Abstract Resource Config</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link smooks.impl.ConditionTypeImpl <em>Condition Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smooks.impl.ConditionTypeImpl
	 * @see smooks.impl.SmooksPackageImpl#getConditionType()
	 * @generated
	 */
	int CONDITION_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITION_TYPE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Evaluator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITION_TYPE__EVALUATOR = 1;

	/**
	 * The number of structural features of the '<em>Condition Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONDITION_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link SmooksGraphExtDocumentRootImpl.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see SmooksGraphExtDocumentRootImpl.impl.DocumentRootImpl
	 * @see smooks.impl.SmooksPackageImpl#getDocumentRoot()
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
	 * The feature id for the '<em><b>Abstract Resource Config</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__ABSTRACT_RESOURCE_CONFIG = 3;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__CONDITION = 4;

	/**
	 * The feature id for the '<em><b>Import</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__IMPORT = 5;

	/**
	 * The feature id for the '<em><b>Param</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PARAM = 6;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PROFILE = 7;

	/**
	 * The feature id for the '<em><b>Profiles</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__PROFILES = 8;

	/**
	 * The feature id for the '<em><b>Resource</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__RESOURCE = 9;

	/**
	 * The feature id for the '<em><b>Resource Config</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__RESOURCE_CONFIG = 10;

	/**
	 * The feature id for the '<em><b>Smooks Resource List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST = 11;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 12;

	/**
	 * The meta object id for the '{@link smooks.impl.ImportTypeImpl <em>Import Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smooks.impl.ImportTypeImpl
	 * @see smooks.impl.SmooksPackageImpl#getImportType()
	 * @generated
	 */
	int IMPORT_TYPE = 3;

	/**
	 * The feature id for the '<em><b>File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPORT_TYPE__FILE = ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Import Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPORT_TYPE_FEATURE_COUNT = ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link smooks.impl.ParamTypeImpl <em>Param Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smooks.impl.ParamTypeImpl
	 * @see smooks.impl.SmooksPackageImpl#getParamType()
	 * @generated
	 */
	int PARAM_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE__MIXED = XMLTypePackage.ANY_TYPE__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE__ANY = XMLTypePackage.ANY_TYPE__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE__ANY_ATTRIBUTE = XMLTypePackage.ANY_TYPE__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE__NAME = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE__TYPE = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Param Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE_FEATURE_COUNT = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link smooks.impl.ProfilesTypeImpl <em>Profiles Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smooks.impl.ProfilesTypeImpl
	 * @see smooks.impl.SmooksPackageImpl#getProfilesType()
	 * @generated
	 */
	int PROFILES_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Profile</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILES_TYPE__PROFILE = 0;

	/**
	 * The number of structural features of the '<em>Profiles Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILES_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link smooks.impl.ProfileTypeImpl <em>Profile Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smooks.impl.ProfileTypeImpl
	 * @see smooks.impl.SmooksPackageImpl#getProfileType()
	 * @generated
	 */
	int PROFILE_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_TYPE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Base Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_TYPE__BASE_PROFILE = 1;

	/**
	 * The feature id for the '<em><b>Sub Profiles</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_TYPE__SUB_PROFILES = 2;

	/**
	 * The number of structural features of the '<em>Profile Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROFILE_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link smooks.impl.ResourceConfigTypeImpl <em>Resource Config Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smooks.impl.ResourceConfigTypeImpl
	 * @see smooks.impl.SmooksPackageImpl#getResourceConfigType()
	 * @generated
	 */
	int RESOURCE_CONFIG_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Resource</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONFIG_TYPE__RESOURCE = ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONFIG_TYPE__CONDITION = ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Param</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONFIG_TYPE__PARAM = ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Selector</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONFIG_TYPE__SELECTOR = ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Selector Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONFIG_TYPE__SELECTOR_NAMESPACE = ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONFIG_TYPE__TARGET_PROFILE = ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Resource Config Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_CONFIG_TYPE_FEATURE_COUNT = ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link smooks.impl.ResourceTypeImpl <em>Resource Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smooks.impl.ResourceTypeImpl
	 * @see smooks.impl.SmooksPackageImpl#getResourceType()
	 * @generated
	 */
	int RESOURCE_TYPE = 8;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_TYPE__MIXED = XMLTypePackage.ANY_TYPE__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_TYPE__ANY = XMLTypePackage.ANY_TYPE__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_TYPE__ANY_ATTRIBUTE = XMLTypePackage.ANY_TYPE__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_TYPE__TYPE = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Resource Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_TYPE_FEATURE_COUNT = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link smooks.impl.SmooksResourceListTypeImpl <em>Resource List Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see smooks.impl.SmooksResourceListTypeImpl
	 * @see smooks.impl.SmooksPackageImpl#getSmooksResourceListType()
	 * @generated
	 */
	int SMOOKS_RESOURCE_LIST_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Profiles</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_RESOURCE_LIST_TYPE__PROFILES = 0;

	/**
	 * The feature id for the '<em><b>Abstract Resource Config Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP = 1;

	/**
	 * The feature id for the '<em><b>Abstract Resource Config</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG = 2;

	/**
	 * The feature id for the '<em><b>Default Selector</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR = 3;

	/**
	 * The feature id for the '<em><b>Default Selector Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE = 4;

	/**
	 * The feature id for the '<em><b>Default Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE = 5;

	/**
	 * The number of structural features of the '<em>Resource List Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_RESOURCE_LIST_TYPE_FEATURE_COUNT = 6;


	/**
	 * Returns the meta object for class '{@link smooks.AbstractResourceConfig <em>Abstract Resource Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Abstract Resource Config</em>'.
	 * @see smooks.AbstractResourceConfig
	 * @generated
	 */
	EClass getAbstractResourceConfig();

	/**
	 * Returns the meta object for class '{@link smooks.ConditionType <em>Condition Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Condition Type</em>'.
	 * @see smooks.ConditionType
	 * @generated
	 */
	EClass getConditionType();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ConditionType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see smooks.ConditionType#getStringValue()
	 * @see #getConditionType()
	 * @generated
	 */
	EAttribute getConditionType_Value();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ConditionType#getEvaluator <em>Evaluator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Evaluator</em>'.
	 * @see smooks.ConditionType#getEvaluator()
	 * @see #getConditionType()
	 * @generated
	 */
	EAttribute getConditionType_Evaluator();

	/**
	 * Returns the meta object for class '{@link smooks.SmooksGraphExtDocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link smooks.SmooksGraphExtDocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link smooks.SmooksGraphExtDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link smooks.SmooksGraphExtDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksGraphExtDocumentRoot#getAbstractResourceConfig <em>Abstract Resource Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Abstract Resource Config</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getAbstractResourceConfig()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_AbstractResourceConfig();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksGraphExtDocumentRoot#getCondition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Condition</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getCondition()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Condition();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksGraphExtDocumentRoot#getImport <em>Import</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Import</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getImport()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Import();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksGraphExtDocumentRoot#getParam <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Param</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getParam()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Param();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksGraphExtDocumentRoot#getProfile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Profile</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getProfile()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Profile();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksGraphExtDocumentRoot#getProfiles <em>Profiles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Profiles</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getProfiles()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Profiles();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksGraphExtDocumentRoot#getResource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Resource</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getResource()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Resource();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksGraphExtDocumentRoot#getResourceConfig <em>Resource Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Resource Config</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getResourceConfig()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_ResourceConfig();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksGraphExtDocumentRoot#getSmooksResourceList <em>Smooks Resource List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Smooks Resource List</em>'.
	 * @see smooks.SmooksGraphExtDocumentRoot#getSmooksResourceList()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_SmooksResourceList();

	/**
	 * Returns the meta object for class '{@link smooks.ImportType <em>Import Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Import Type</em>'.
	 * @see smooks.ImportType
	 * @generated
	 */
	EClass getImportType();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ImportType#getFile <em>File</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File</em>'.
	 * @see smooks.ImportType#getFile()
	 * @see #getImportType()
	 * @generated
	 */
	EAttribute getImportType_File();

	/**
	 * Returns the meta object for class '{@link smooks.ParamType <em>Param Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Param Type</em>'.
	 * @see smooks.ParamType
	 * @generated
	 */
	EClass getParamType();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ParamType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see smooks.ParamType#getName()
	 * @see #getParamType()
	 * @generated
	 */
	EAttribute getParamType_Name();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ParamType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see smooks.ParamType#getType()
	 * @see #getParamType()
	 * @generated
	 */
	EAttribute getParamType_Type();

	/**
	 * Returns the meta object for class '{@link smooks.ProfilesType <em>Profiles Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profiles Type</em>'.
	 * @see smooks.ProfilesType
	 * @generated
	 */
	EClass getProfilesType();

	/**
	 * Returns the meta object for the containment reference list '{@link smooks.ProfilesType#getProfile <em>Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Profile</em>'.
	 * @see smooks.ProfilesType#getProfile()
	 * @see #getProfilesType()
	 * @generated
	 */
	EReference getProfilesType_Profile();

	/**
	 * Returns the meta object for class '{@link smooks.ProfileType <em>Profile Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Profile Type</em>'.
	 * @see smooks.ProfileType
	 * @generated
	 */
	EClass getProfileType();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ProfileType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see smooks.ProfileType#getStringValue()
	 * @see #getProfileType()
	 * @generated
	 */
	EAttribute getProfileType_Value();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ProfileType#getBaseProfile <em>Base Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Profile</em>'.
	 * @see smooks.ProfileType#getBaseProfile()
	 * @see #getProfileType()
	 * @generated
	 */
	EAttribute getProfileType_BaseProfile();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ProfileType#getSubProfiles <em>Sub Profiles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Sub Profiles</em>'.
	 * @see smooks.ProfileType#getSubProfiles()
	 * @see #getProfileType()
	 * @generated
	 */
	EAttribute getProfileType_SubProfiles();

	/**
	 * Returns the meta object for class '{@link smooks.ResourceConfigType <em>Resource Config Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Config Type</em>'.
	 * @see smooks.ResourceConfigType
	 * @generated
	 */
	EClass getResourceConfigType();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.ResourceConfigType#getResource <em>Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Resource</em>'.
	 * @see smooks.ResourceConfigType#getResource()
	 * @see #getResourceConfigType()
	 * @generated
	 */
	EReference getResourceConfigType_Resource();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.ResourceConfigType#getCondition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Condition</em>'.
	 * @see smooks.ResourceConfigType#getCondition()
	 * @see #getResourceConfigType()
	 * @generated
	 */
	EReference getResourceConfigType_Condition();

	/**
	 * Returns the meta object for the containment reference list '{@link smooks.ResourceConfigType#getParam <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Param</em>'.
	 * @see smooks.ResourceConfigType#getParam()
	 * @see #getResourceConfigType()
	 * @generated
	 */
	EReference getResourceConfigType_Param();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ResourceConfigType#getSelector <em>Selector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Selector</em>'.
	 * @see smooks.ResourceConfigType#getSelector()
	 * @see #getResourceConfigType()
	 * @generated
	 */
	EAttribute getResourceConfigType_Selector();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ResourceConfigType#getSelectorNamespace <em>Selector Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Selector Namespace</em>'.
	 * @see smooks.ResourceConfigType#getSelectorNamespace()
	 * @see #getResourceConfigType()
	 * @generated
	 */
	EAttribute getResourceConfigType_SelectorNamespace();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ResourceConfigType#getTargetProfile <em>Target Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target Profile</em>'.
	 * @see smooks.ResourceConfigType#getTargetProfile()
	 * @see #getResourceConfigType()
	 * @generated
	 */
	EAttribute getResourceConfigType_TargetProfile();

	/**
	 * Returns the meta object for class '{@link smooks.ResourceType <em>Resource Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Type</em>'.
	 * @see smooks.ResourceType
	 * @generated
	 */
	EClass getResourceType();

	/**
	 * Returns the meta object for the attribute '{@link smooks.ResourceType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see smooks.ResourceType#getType()
	 * @see #getResourceType()
	 * @generated
	 */
	EAttribute getResourceType_Type();

	/**
	 * Returns the meta object for class '{@link smooks.SmooksResourceListType <em>Resource List Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource List Type</em>'.
	 * @see smooks.SmooksResourceListType
	 * @generated
	 */
	EClass getSmooksResourceListType();

	/**
	 * Returns the meta object for the containment reference '{@link smooks.SmooksResourceListType#getProfiles <em>Profiles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Profiles</em>'.
	 * @see smooks.SmooksResourceListType#getProfiles()
	 * @see #getSmooksResourceListType()
	 * @generated
	 */
	EReference getSmooksResourceListType_Profiles();

	/**
	 * Returns the meta object for the attribute list '{@link smooks.SmooksResourceListType#getAbstractResourceConfigGroup <em>Abstract Resource Config Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Abstract Resource Config Group</em>'.
	 * @see smooks.SmooksResourceListType#getAbstractResourceConfigGroup()
	 * @see #getSmooksResourceListType()
	 * @generated
	 */
	EAttribute getSmooksResourceListType_AbstractResourceConfigGroup();

	/**
	 * Returns the meta object for the containment reference list '{@link smooks.SmooksResourceListType#getAbstractResourceConfig <em>Abstract Resource Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Abstract Resource Config</em>'.
	 * @see smooks.SmooksResourceListType#getAbstractResourceConfig()
	 * @see #getSmooksResourceListType()
	 * @generated
	 */
	EReference getSmooksResourceListType_AbstractResourceConfig();

	/**
	 * Returns the meta object for the attribute '{@link smooks.SmooksResourceListType#getDefaultSelector <em>Default Selector</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Selector</em>'.
	 * @see smooks.SmooksResourceListType#getDefaultSelector()
	 * @see #getSmooksResourceListType()
	 * @generated
	 */
	EAttribute getSmooksResourceListType_DefaultSelector();

	/**
	 * Returns the meta object for the attribute '{@link smooks.SmooksResourceListType#getDefaultSelectorNamespace <em>Default Selector Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Selector Namespace</em>'.
	 * @see smooks.SmooksResourceListType#getDefaultSelectorNamespace()
	 * @see #getSmooksResourceListType()
	 * @generated
	 */
	EAttribute getSmooksResourceListType_DefaultSelectorNamespace();

	/**
	 * Returns the meta object for the attribute '{@link smooks.SmooksResourceListType#getDefaultTargetProfile <em>Default Target Profile</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Target Profile</em>'.
	 * @see smooks.SmooksResourceListType#getDefaultTargetProfile()
	 * @see #getSmooksResourceListType()
	 * @generated
	 */
	EAttribute getSmooksResourceListType_DefaultTargetProfile();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SmooksFactory getSmooksFactory();

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
		 * The meta object literal for the '{@link smooks.impl.AbstractResourceConfigImpl <em>Abstract Resource Config</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smooks.impl.AbstractResourceConfigImpl
		 * @see smooks.impl.SmooksPackageImpl#getAbstractResourceConfig()
		 * @generated
		 */
		EClass ABSTRACT_RESOURCE_CONFIG = eINSTANCE.getAbstractResourceConfig();

		/**
		 * The meta object literal for the '{@link smooks.impl.ConditionTypeImpl <em>Condition Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smooks.impl.ConditionTypeImpl
		 * @see smooks.impl.SmooksPackageImpl#getConditionType()
		 * @generated
		 */
		EClass CONDITION_TYPE = eINSTANCE.getConditionType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONDITION_TYPE__VALUE = eINSTANCE.getConditionType_Value();

		/**
		 * The meta object literal for the '<em><b>Evaluator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONDITION_TYPE__EVALUATOR = eINSTANCE.getConditionType_Evaluator();

		/**
		 * The meta object literal for the '{@link SmooksGraphExtDocumentRootImpl.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see SmooksGraphExtDocumentRootImpl.impl.DocumentRootImpl
		 * @see smooks.impl.SmooksPackageImpl#getDocumentRoot()
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
		 * The meta object literal for the '<em><b>Abstract Resource Config</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__ABSTRACT_RESOURCE_CONFIG = eINSTANCE.getDocumentRoot_AbstractResourceConfig();

		/**
		 * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__CONDITION = eINSTANCE.getDocumentRoot_Condition();

		/**
		 * The meta object literal for the '<em><b>Import</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__IMPORT = eINSTANCE.getDocumentRoot_Import();

		/**
		 * The meta object literal for the '<em><b>Param</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__PARAM = eINSTANCE.getDocumentRoot_Param();

		/**
		 * The meta object literal for the '<em><b>Profile</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__PROFILE = eINSTANCE.getDocumentRoot_Profile();

		/**
		 * The meta object literal for the '<em><b>Profiles</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__PROFILES = eINSTANCE.getDocumentRoot_Profiles();

		/**
		 * The meta object literal for the '<em><b>Resource</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__RESOURCE = eINSTANCE.getDocumentRoot_Resource();

		/**
		 * The meta object literal for the '<em><b>Resource Config</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__RESOURCE_CONFIG = eINSTANCE.getDocumentRoot_ResourceConfig();

		/**
		 * The meta object literal for the '<em><b>Smooks Resource List</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST = eINSTANCE.getDocumentRoot_SmooksResourceList();

		/**
		 * The meta object literal for the '{@link smooks.impl.ImportTypeImpl <em>Import Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smooks.impl.ImportTypeImpl
		 * @see smooks.impl.SmooksPackageImpl#getImportType()
		 * @generated
		 */
		EClass IMPORT_TYPE = eINSTANCE.getImportType();

		/**
		 * The meta object literal for the '<em><b>File</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute IMPORT_TYPE__FILE = eINSTANCE.getImportType_File();

		/**
		 * The meta object literal for the '{@link smooks.impl.ParamTypeImpl <em>Param Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smooks.impl.ParamTypeImpl
		 * @see smooks.impl.SmooksPackageImpl#getParamType()
		 * @generated
		 */
		EClass PARAM_TYPE = eINSTANCE.getParamType();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM_TYPE__NAME = eINSTANCE.getParamType_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM_TYPE__TYPE = eINSTANCE.getParamType_Type();

		/**
		 * The meta object literal for the '{@link smooks.impl.ProfilesTypeImpl <em>Profiles Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smooks.impl.ProfilesTypeImpl
		 * @see smooks.impl.SmooksPackageImpl#getProfilesType()
		 * @generated
		 */
		EClass PROFILES_TYPE = eINSTANCE.getProfilesType();

		/**
		 * The meta object literal for the '<em><b>Profile</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROFILES_TYPE__PROFILE = eINSTANCE.getProfilesType_Profile();

		/**
		 * The meta object literal for the '{@link smooks.impl.ProfileTypeImpl <em>Profile Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smooks.impl.ProfileTypeImpl
		 * @see smooks.impl.SmooksPackageImpl#getProfileType()
		 * @generated
		 */
		EClass PROFILE_TYPE = eINSTANCE.getProfileType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_TYPE__VALUE = eINSTANCE.getProfileType_Value();

		/**
		 * The meta object literal for the '<em><b>Base Profile</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_TYPE__BASE_PROFILE = eINSTANCE.getProfileType_BaseProfile();

		/**
		 * The meta object literal for the '<em><b>Sub Profiles</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROFILE_TYPE__SUB_PROFILES = eINSTANCE.getProfileType_SubProfiles();

		/**
		 * The meta object literal for the '{@link smooks.impl.ResourceConfigTypeImpl <em>Resource Config Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smooks.impl.ResourceConfigTypeImpl
		 * @see smooks.impl.SmooksPackageImpl#getResourceConfigType()
		 * @generated
		 */
		EClass RESOURCE_CONFIG_TYPE = eINSTANCE.getResourceConfigType();

		/**
		 * The meta object literal for the '<em><b>Resource</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE_CONFIG_TYPE__RESOURCE = eINSTANCE.getResourceConfigType_Resource();

		/**
		 * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE_CONFIG_TYPE__CONDITION = eINSTANCE.getResourceConfigType_Condition();

		/**
		 * The meta object literal for the '<em><b>Param</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RESOURCE_CONFIG_TYPE__PARAM = eINSTANCE.getResourceConfigType_Param();

		/**
		 * The meta object literal for the '<em><b>Selector</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE_CONFIG_TYPE__SELECTOR = eINSTANCE.getResourceConfigType_Selector();

		/**
		 * The meta object literal for the '<em><b>Selector Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE_CONFIG_TYPE__SELECTOR_NAMESPACE = eINSTANCE.getResourceConfigType_SelectorNamespace();

		/**
		 * The meta object literal for the '<em><b>Target Profile</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE_CONFIG_TYPE__TARGET_PROFILE = eINSTANCE.getResourceConfigType_TargetProfile();

		/**
		 * The meta object literal for the '{@link smooks.impl.ResourceTypeImpl <em>Resource Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smooks.impl.ResourceTypeImpl
		 * @see smooks.impl.SmooksPackageImpl#getResourceType()
		 * @generated
		 */
		EClass RESOURCE_TYPE = eINSTANCE.getResourceType();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE_TYPE__TYPE = eINSTANCE.getResourceType_Type();

		/**
		 * The meta object literal for the '{@link smooks.impl.SmooksResourceListTypeImpl <em>Resource List Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see smooks.impl.SmooksResourceListTypeImpl
		 * @see smooks.impl.SmooksPackageImpl#getSmooksResourceListType()
		 * @generated
		 */
		EClass SMOOKS_RESOURCE_LIST_TYPE = eINSTANCE.getSmooksResourceListType();

		/**
		 * The meta object literal for the '<em><b>Profiles</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_RESOURCE_LIST_TYPE__PROFILES = eINSTANCE.getSmooksResourceListType_Profiles();

		/**
		 * The meta object literal for the '<em><b>Abstract Resource Config Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP = eINSTANCE.getSmooksResourceListType_AbstractResourceConfigGroup();

		/**
		 * The meta object literal for the '<em><b>Abstract Resource Config</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG = eINSTANCE.getSmooksResourceListType_AbstractResourceConfig();

		/**
		 * The meta object literal for the '<em><b>Default Selector</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR = eINSTANCE.getSmooksResourceListType_DefaultSelector();

		/**
		 * The meta object literal for the '<em><b>Default Selector Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE = eINSTANCE.getSmooksResourceListType_DefaultSelectorNamespace();

		/**
		 * The meta object literal for the '<em><b>Default Target Profile</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE = eINSTANCE.getSmooksResourceListType_DefaultTargetProfile();

	}

} //SmooksPackage
