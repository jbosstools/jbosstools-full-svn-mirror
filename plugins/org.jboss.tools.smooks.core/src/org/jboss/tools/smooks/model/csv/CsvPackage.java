/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.csv;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * Smooks CSV Reader Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.csv.CsvFactory
 * @model kind="package"
 * @generated
 */
public interface CsvPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "csv";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/csv-1.1.xsd";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "cvs";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CsvPackage eINSTANCE = org.jboss.tools.smooks.model.csv.impl.CsvPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.csv.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.csv.impl.DocumentRootImpl
	 * @see org.jboss.tools.smooks.model.csv.impl.CsvPackageImpl#getDocumentRoot()
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
	 * The feature id for the '<em><b>Reader</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__READER = 3;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.csv.impl.ReaderImpl <em>Reader</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.csv.impl.ReaderImpl
	 * @see org.jboss.tools.smooks.model.csv.impl.CsvPackageImpl#getReader()
	 * @generated
	 */
	int READER = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__MIXED = SmooksPackage.ABSTRACT_READER__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__ANY = SmooksPackage.ABSTRACT_READER__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__ANY_ATTRIBUTE = SmooksPackage.ABSTRACT_READER__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__TARGET_PROFILE = SmooksPackage.ABSTRACT_READER__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__ENCODING = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Fields</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__FIELDS = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Quote</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__QUOTE = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Separator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__SEPARATOR = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Skip Lines</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER__SKIP_LINES = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Reader</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int READER_FEATURE_COUNT = SmooksPackage.ABSTRACT_READER_FEATURE_COUNT + 5;

	/**
	 * The meta object id for the '<em>Char</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.lang.String
	 * @see org.jboss.tools.smooks.model.csv.impl.CsvPackageImpl#getChar()
	 * @generated
	 */
	int CHAR = 2;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.csv.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.csv.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.csv.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.csv.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.csv.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.csv.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.csv.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.csv.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.csv.DocumentRoot#getReader <em>Reader</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Reader</em>'.
	 * @see org.jboss.tools.smooks.model.csv.DocumentRoot#getReader()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Reader();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.csv.Reader <em>Reader</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Reader</em>'.
	 * @see org.jboss.tools.smooks.model.csv.Reader
	 * @generated
	 */
	EClass getReader();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.csv.Reader#getEncoding <em>Encoding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Encoding</em>'.
	 * @see org.jboss.tools.smooks.model.csv.Reader#getEncoding()
	 * @see #getReader()
	 * @generated
	 */
	EAttribute getReader_Encoding();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.csv.Reader#getFields <em>Fields</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fields</em>'.
	 * @see org.jboss.tools.smooks.model.csv.Reader#getFields()
	 * @see #getReader()
	 * @generated
	 */
	EAttribute getReader_Fields();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.csv.Reader#getQuote <em>Quote</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Quote</em>'.
	 * @see org.jboss.tools.smooks.model.csv.Reader#getQuote()
	 * @see #getReader()
	 * @generated
	 */
	EAttribute getReader_Quote();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.csv.Reader#getSeparator <em>Separator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Separator</em>'.
	 * @see org.jboss.tools.smooks.model.csv.Reader#getSeparator()
	 * @see #getReader()
	 * @generated
	 */
	EAttribute getReader_Separator();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.csv.Reader#getSkipLines <em>Skip Lines</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Skip Lines</em>'.
	 * @see org.jboss.tools.smooks.model.csv.Reader#getSkipLines()
	 * @see #getReader()
	 * @generated
	 */
	EAttribute getReader_SkipLines();

	/**
	 * Returns the meta object for data type '{@link java.lang.String <em>Char</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Char</em>'.
	 * @see java.lang.String
	 * @model instanceClass="java.lang.String"
	 *        extendedMetaData="name='char' baseType='http://www.eclipse.org/emf/2003/XMLType#string' length='1'"
	 * @generated
	 */
	EDataType getChar();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CsvFactory getCsvFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.csv.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.csv.impl.DocumentRootImpl
		 * @see org.jboss.tools.smooks.model.csv.impl.CsvPackageImpl#getDocumentRoot()
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
		 * The meta object literal for the '<em><b>Reader</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__READER = eINSTANCE.getDocumentRoot_Reader();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.csv.impl.ReaderImpl <em>Reader</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.csv.impl.ReaderImpl
		 * @see org.jboss.tools.smooks.model.csv.impl.CsvPackageImpl#getReader()
		 * @generated
		 */
		EClass READER = eINSTANCE.getReader();

		/**
		 * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute READER__ENCODING = eINSTANCE.getReader_Encoding();

		/**
		 * The meta object literal for the '<em><b>Fields</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute READER__FIELDS = eINSTANCE.getReader_Fields();

		/**
		 * The meta object literal for the '<em><b>Quote</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute READER__QUOTE = eINSTANCE.getReader_Quote();

		/**
		 * The meta object literal for the '<em><b>Separator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute READER__SEPARATOR = eINSTANCE.getReader_Separator();

		/**
		 * The meta object literal for the '<em><b>Skip Lines</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute READER__SKIP_LINES = eINSTANCE.getReader_SkipLines();

		/**
		 * The meta object literal for the '<em>Char</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.lang.String
		 * @see org.jboss.tools.smooks.model.csv.impl.CsvPackageImpl#getChar()
		 * @generated
		 */
		EDataType CHAR = eINSTANCE.getChar();

	}

} //CsvPackage
