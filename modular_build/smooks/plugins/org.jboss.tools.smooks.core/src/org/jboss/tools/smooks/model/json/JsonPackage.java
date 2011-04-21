/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.json;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * Smooks JSON Reader Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.json.JsonFactory
 * @model kind="package"
 * @generated
 */
public interface JsonPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "json"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/json-1.1.xsd"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "json"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	JsonPackage eINSTANCE = org.jboss.tools.smooks.model.json.impl.JsonPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.json.impl.JsonDocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.json.impl.JsonDocumentRootImpl
	 * @see org.jboss.tools.smooks.model.json.impl.JsonPackageImpl#getJsonDocumentRoot()
	 * @generated
	 */
	int JSON_DOCUMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Reader</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_DOCUMENT_ROOT__READER = 3;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.json.impl.KeyImpl <em>Key</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.json.impl.KeyImpl
	 * @see org.jboss.tools.smooks.model.json.impl.JsonPackageImpl#getKey()
	 * @generated
	 */
	int KEY = 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__VALUE = 0;

	/**
	 * The feature id for the '<em><b>From</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__FROM = 1;

	/**
	 * The feature id for the '<em><b>To</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY__TO = 2;

	/**
	 * The number of structural features of the '<em>Key</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.json.impl.KeyMapImpl <em>Key Map</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.json.impl.KeyMapImpl
	 * @see org.jboss.tools.smooks.model.json.impl.JsonPackageImpl#getKeyMap()
	 * @generated
	 */
	int KEY_MAP = 2;

	/**
	 * The feature id for the '<em><b>Key</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY_MAP__KEY = 0;

	/**
	 * The number of structural features of the '<em>Key Map</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int KEY_MAP_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.json.impl.JsonReaderImpl <em>Reader</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.json.impl.JsonReaderImpl
	 * @see org.jboss.tools.smooks.model.json.impl.JsonPackageImpl#getJsonReader()
	 * @generated
	 */
	int JSON_READER = 3;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__MIXED = SmooksPackage.ABSTRACT_READER__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__ANY = SmooksPackage.ABSTRACT_READER__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__ANY_ATTRIBUTE = SmooksPackage.ABSTRACT_READER__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__TARGET_PROFILE = SmooksPackage.ABSTRACT_READER__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Key Map</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__KEY_MAP = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Array Element Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__ARRAY_ELEMENT_NAME = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__ENCODING = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Illegal Element Name Char Replacement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__ILLEGAL_ELEMENT_NAME_CHAR_REPLACEMENT = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Key Prefix On Numeric</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__KEY_PREFIX_ON_NUMERIC = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Key Whitspace Replacement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__KEY_WHITSPACE_REPLACEMENT = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Null Value Replacement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__NULL_VALUE_REPLACEMENT = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Root Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER__ROOT_NAME = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Reader</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JSON_READER_FEATURE_COUNT = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 8;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.json.JsonDocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonDocumentRoot
	 * @generated
	 */
	EClass getJsonDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.json.JsonDocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonDocumentRoot#getMixed()
	 * @see #getJsonDocumentRoot()
	 * @generated
	 */
	EAttribute getJsonDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.json.JsonDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonDocumentRoot#getXMLNSPrefixMap()
	 * @see #getJsonDocumentRoot()
	 * @generated
	 */
	EReference getJsonDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.json.JsonDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonDocumentRoot#getXSISchemaLocation()
	 * @see #getJsonDocumentRoot()
	 * @generated
	 */
	EReference getJsonDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.json.JsonDocumentRoot#getReader <em>Reader</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Reader</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonDocumentRoot#getReader()
	 * @see #getJsonDocumentRoot()
	 * @generated
	 */
	EReference getJsonDocumentRoot_Reader();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.json.Key <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Key</em>'.
	 * @see org.jboss.tools.smooks.model.json.Key
	 * @generated
	 */
	EClass getKey();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.Key#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.json.Key#getValue()
	 * @see #getKey()
	 * @generated
	 */
	EAttribute getKey_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.Key#getFrom <em>From</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>From</em>'.
	 * @see org.jboss.tools.smooks.model.json.Key#getFrom()
	 * @see #getKey()
	 * @generated
	 */
	EAttribute getKey_From();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.Key#getTo <em>To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>To</em>'.
	 * @see org.jboss.tools.smooks.model.json.Key#getTo()
	 * @see #getKey()
	 * @generated
	 */
	EAttribute getKey_To();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.json.KeyMap <em>Key Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Key Map</em>'.
	 * @see org.jboss.tools.smooks.model.json.KeyMap
	 * @generated
	 */
	EClass getKeyMap();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.json.KeyMap#getKey <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Key</em>'.
	 * @see org.jboss.tools.smooks.model.json.KeyMap#getKey()
	 * @see #getKeyMap()
	 * @generated
	 */
	EReference getKeyMap_Key();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.json.JsonReader <em>Reader</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reader</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonReader
	 * @generated
	 */
	EClass getJsonReader();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.json.JsonReader#getKeyMap <em>Key Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key Map</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonReader#getKeyMap()
	 * @see #getJsonReader()
	 * @generated
	 */
	EReference getJsonReader_KeyMap();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.JsonReader#getArrayElementName <em>Array Element Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Array Element Name</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonReader#getArrayElementName()
	 * @see #getJsonReader()
	 * @generated
	 */
	EAttribute getJsonReader_ArrayElementName();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.JsonReader#getEncoding <em>Encoding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Encoding</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonReader#getEncoding()
	 * @see #getJsonReader()
	 * @generated
	 */
	EAttribute getJsonReader_Encoding();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.JsonReader#getIllegalElementNameCharReplacement <em>Illegal Element Name Char Replacement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Illegal Element Name Char Replacement</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonReader#getIllegalElementNameCharReplacement()
	 * @see #getJsonReader()
	 * @generated
	 */
	EAttribute getJsonReader_IllegalElementNameCharReplacement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.JsonReader#getKeyPrefixOnNumeric <em>Key Prefix On Numeric</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key Prefix On Numeric</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonReader#getKeyPrefixOnNumeric()
	 * @see #getJsonReader()
	 * @generated
	 */
	EAttribute getJsonReader_KeyPrefixOnNumeric();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.JsonReader#getKeyWhitspaceReplacement <em>Key Whitspace Replacement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key Whitspace Replacement</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonReader#getKeyWhitspaceReplacement()
	 * @see #getJsonReader()
	 * @generated
	 */
	EAttribute getJsonReader_KeyWhitspaceReplacement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.JsonReader#getNullValueReplacement <em>Null Value Replacement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Null Value Replacement</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonReader#getNullValueReplacement()
	 * @see #getJsonReader()
	 * @generated
	 */
	EAttribute getJsonReader_NullValueReplacement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.json.JsonReader#getRootName <em>Root Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Root Name</em>'.
	 * @see org.jboss.tools.smooks.model.json.JsonReader#getRootName()
	 * @see #getJsonReader()
	 * @generated
	 */
	EAttribute getJsonReader_RootName();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	JsonFactory getJsonFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.json.impl.JsonDocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.json.impl.JsonDocumentRootImpl
		 * @see org.jboss.tools.smooks.model.json.impl.JsonPackageImpl#getJsonDocumentRoot()
		 * @generated
		 */
		EClass JSON_DOCUMENT_ROOT = eINSTANCE.getJsonDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JSON_DOCUMENT_ROOT__MIXED = eINSTANCE.getJsonDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JSON_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getJsonDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JSON_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getJsonDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Reader</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JSON_DOCUMENT_ROOT__READER = eINSTANCE.getJsonDocumentRoot_Reader();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.json.impl.KeyImpl <em>Key</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.json.impl.KeyImpl
		 * @see org.jboss.tools.smooks.model.json.impl.JsonPackageImpl#getKey()
		 * @generated
		 */
		EClass KEY = eINSTANCE.getKey();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY__VALUE = eINSTANCE.getKey_Value();

		/**
		 * The meta object literal for the '<em><b>From</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY__FROM = eINSTANCE.getKey_From();

		/**
		 * The meta object literal for the '<em><b>To</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute KEY__TO = eINSTANCE.getKey_To();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.json.impl.KeyMapImpl <em>Key Map</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.json.impl.KeyMapImpl
		 * @see org.jboss.tools.smooks.model.json.impl.JsonPackageImpl#getKeyMap()
		 * @generated
		 */
		EClass KEY_MAP = eINSTANCE.getKeyMap();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference KEY_MAP__KEY = eINSTANCE.getKeyMap_Key();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.json.impl.JsonReaderImpl <em>Reader</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.json.impl.JsonReaderImpl
		 * @see org.jboss.tools.smooks.model.json.impl.JsonPackageImpl#getJsonReader()
		 * @generated
		 */
		EClass JSON_READER = eINSTANCE.getJsonReader();

		/**
		 * The meta object literal for the '<em><b>Key Map</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JSON_READER__KEY_MAP = eINSTANCE.getJsonReader_KeyMap();

		/**
		 * The meta object literal for the '<em><b>Array Element Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JSON_READER__ARRAY_ELEMENT_NAME = eINSTANCE.getJsonReader_ArrayElementName();

		/**
		 * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JSON_READER__ENCODING = eINSTANCE.getJsonReader_Encoding();

		/**
		 * The meta object literal for the '<em><b>Illegal Element Name Char Replacement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JSON_READER__ILLEGAL_ELEMENT_NAME_CHAR_REPLACEMENT = eINSTANCE.getJsonReader_IllegalElementNameCharReplacement();

		/**
		 * The meta object literal for the '<em><b>Key Prefix On Numeric</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JSON_READER__KEY_PREFIX_ON_NUMERIC = eINSTANCE.getJsonReader_KeyPrefixOnNumeric();

		/**
		 * The meta object literal for the '<em><b>Key Whitspace Replacement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JSON_READER__KEY_WHITSPACE_REPLACEMENT = eINSTANCE.getJsonReader_KeyWhitspaceReplacement();

		/**
		 * The meta object literal for the '<em><b>Null Value Replacement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JSON_READER__NULL_VALUE_REPLACEMENT = eINSTANCE.getJsonReader_NullValueReplacement();

		/**
		 * The meta object literal for the '<em><b>Root Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JSON_READER__ROOT_NAME = eINSTANCE.getJsonReader_RootName();

	}

} //JsonPackage
