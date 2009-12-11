/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.fileRouting;

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
 * Smooks file Routing Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.fileRouting.FileRoutingFactory
 * @model kind="package"
 * @generated
 */
public interface FileRoutingPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "fileRouting"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/file-routing-1.1.xsd"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "file"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FileRoutingPackage eINSTANCE = org.jboss.tools.smooks.model.fileRouting.impl.FileRoutingPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.fileRouting.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.fileRouting.impl.DocumentRootImpl
	 * @see org.jboss.tools.smooks.model.fileRouting.impl.FileRoutingPackageImpl#getDocumentRoot()
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
	 * The feature id for the '<em><b>Output Stream</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__OUTPUT_STREAM = 3;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.fileRouting.impl.HighWaterMarkImpl <em>High Water Mark</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.fileRouting.impl.HighWaterMarkImpl
	 * @see org.jboss.tools.smooks.model.fileRouting.impl.FileRoutingPackageImpl#getHighWaterMark()
	 * @generated
	 */
	int HIGH_WATER_MARK = 1;

	/**
	 * The feature id for the '<em><b>Mark</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_WATER_MARK__MARK = 0;

	/**
	 * The feature id for the '<em><b>Poll Frequency</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_WATER_MARK__POLL_FREQUENCY = 1;

	/**
	 * The feature id for the '<em><b>Timeout</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_WATER_MARK__TIMEOUT = 2;

	/**
	 * The number of structural features of the '<em>High Water Mark</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HIGH_WATER_MARK_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl <em>Output Stream</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl
	 * @see org.jboss.tools.smooks.model.fileRouting.impl.FileRoutingPackageImpl#getOutputStream()
	 * @generated
	 */
	int OUTPUT_STREAM = 2;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>File Name Pattern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__FILE_NAME_PATTERN = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Destination Directory Pattern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__DESTINATION_DIRECTORY_PATTERN = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>List File Name Pattern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__LIST_FILE_NAME_PATTERN = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>High Water Mark</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__HIGH_WATER_MARK = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Close On Condition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__CLOSE_ON_CONDITION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Encoding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__ENCODING = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Open On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__OPEN_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Open On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__OPEN_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Resource Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM__RESOURCE_NAME = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 8;

	/**
	 * The number of structural features of the '<em>Output Stream</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OUTPUT_STREAM_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 9;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.fileRouting.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.DocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.fileRouting.DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.DocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.fileRouting.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.DocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.fileRouting.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.DocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.fileRouting.DocumentRoot#getOutputStream <em>Output Stream</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Output Stream</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.DocumentRoot#getOutputStream()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_OutputStream();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.fileRouting.HighWaterMark <em>High Water Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>High Water Mark</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.HighWaterMark
	 * @generated
	 */
	EClass getHighWaterMark();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.HighWaterMark#getMark <em>Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mark</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.HighWaterMark#getMark()
	 * @see #getHighWaterMark()
	 * @generated
	 */
	EAttribute getHighWaterMark_Mark();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.HighWaterMark#getPollFrequency <em>Poll Frequency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Poll Frequency</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.HighWaterMark#getPollFrequency()
	 * @see #getHighWaterMark()
	 * @generated
	 */
	EAttribute getHighWaterMark_PollFrequency();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.HighWaterMark#getTimeout <em>Timeout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timeout</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.HighWaterMark#getTimeout()
	 * @see #getHighWaterMark()
	 * @generated
	 */
	EAttribute getHighWaterMark_Timeout();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream <em>Output Stream</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Output Stream</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream
	 * @generated
	 */
	EClass getOutputStream();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream#getFileNamePattern <em>File Name Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>File Name Pattern</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream#getFileNamePattern()
	 * @see #getOutputStream()
	 * @generated
	 */
	EAttribute getOutputStream_FileNamePattern();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream#getDestinationDirectoryPattern <em>Destination Directory Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Destination Directory Pattern</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream#getDestinationDirectoryPattern()
	 * @see #getOutputStream()
	 * @generated
	 */
	EAttribute getOutputStream_DestinationDirectoryPattern();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream#getListFileNamePattern <em>List File Name Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>List File Name Pattern</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream#getListFileNamePattern()
	 * @see #getOutputStream()
	 * @generated
	 */
	EAttribute getOutputStream_ListFileNamePattern();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream#getHighWaterMark <em>High Water Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>High Water Mark</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream#getHighWaterMark()
	 * @see #getOutputStream()
	 * @generated
	 */
	EReference getOutputStream_HighWaterMark();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream#getCloseOnCondition <em>Close On Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Close On Condition</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream#getCloseOnCondition()
	 * @see #getOutputStream()
	 * @generated
	 */
	EAttribute getOutputStream_CloseOnCondition();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream#getEncoding <em>Encoding</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Encoding</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream#getEncoding()
	 * @see #getOutputStream()
	 * @generated
	 */
	EAttribute getOutputStream_Encoding();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream#getOpenOnElement <em>Open On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Open On Element</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream#getOpenOnElement()
	 * @see #getOutputStream()
	 * @generated
	 */
	EAttribute getOutputStream_OpenOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream#getOpenOnElementNS <em>Open On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Open On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream#getOpenOnElementNS()
	 * @see #getOutputStream()
	 * @generated
	 */
	EAttribute getOutputStream_OpenOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.fileRouting.OutputStream#getResourceName <em>Resource Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Resource Name</em>'.
	 * @see org.jboss.tools.smooks.model.fileRouting.OutputStream#getResourceName()
	 * @see #getOutputStream()
	 * @generated
	 */
	EAttribute getOutputStream_ResourceName();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FileRoutingFactory getFileRoutingFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.fileRouting.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.fileRouting.impl.DocumentRootImpl
		 * @see org.jboss.tools.smooks.model.fileRouting.impl.FileRoutingPackageImpl#getDocumentRoot()
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
		 * The meta object literal for the '<em><b>Output Stream</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__OUTPUT_STREAM = eINSTANCE.getDocumentRoot_OutputStream();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.fileRouting.impl.HighWaterMarkImpl <em>High Water Mark</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.fileRouting.impl.HighWaterMarkImpl
		 * @see org.jboss.tools.smooks.model.fileRouting.impl.FileRoutingPackageImpl#getHighWaterMark()
		 * @generated
		 */
		EClass HIGH_WATER_MARK = eINSTANCE.getHighWaterMark();

		/**
		 * The meta object literal for the '<em><b>Mark</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HIGH_WATER_MARK__MARK = eINSTANCE.getHighWaterMark_Mark();

		/**
		 * The meta object literal for the '<em><b>Poll Frequency</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HIGH_WATER_MARK__POLL_FREQUENCY = eINSTANCE.getHighWaterMark_PollFrequency();

		/**
		 * The meta object literal for the '<em><b>Timeout</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HIGH_WATER_MARK__TIMEOUT = eINSTANCE.getHighWaterMark_Timeout();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl <em>Output Stream</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.fileRouting.impl.OutputStreamImpl
		 * @see org.jboss.tools.smooks.model.fileRouting.impl.FileRoutingPackageImpl#getOutputStream()
		 * @generated
		 */
		EClass OUTPUT_STREAM = eINSTANCE.getOutputStream();

		/**
		 * The meta object literal for the '<em><b>File Name Pattern</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_STREAM__FILE_NAME_PATTERN = eINSTANCE.getOutputStream_FileNamePattern();

		/**
		 * The meta object literal for the '<em><b>Destination Directory Pattern</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_STREAM__DESTINATION_DIRECTORY_PATTERN = eINSTANCE.getOutputStream_DestinationDirectoryPattern();

		/**
		 * The meta object literal for the '<em><b>List File Name Pattern</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_STREAM__LIST_FILE_NAME_PATTERN = eINSTANCE.getOutputStream_ListFileNamePattern();

		/**
		 * The meta object literal for the '<em><b>High Water Mark</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OUTPUT_STREAM__HIGH_WATER_MARK = eINSTANCE.getOutputStream_HighWaterMark();

		/**
		 * The meta object literal for the '<em><b>Close On Condition</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_STREAM__CLOSE_ON_CONDITION = eINSTANCE.getOutputStream_CloseOnCondition();

		/**
		 * The meta object literal for the '<em><b>Encoding</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_STREAM__ENCODING = eINSTANCE.getOutputStream_Encoding();

		/**
		 * The meta object literal for the '<em><b>Open On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_STREAM__OPEN_ON_ELEMENT = eINSTANCE.getOutputStream_OpenOnElement();

		/**
		 * The meta object literal for the '<em><b>Open On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_STREAM__OPEN_ON_ELEMENT_NS = eINSTANCE.getOutputStream_OpenOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Resource Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OUTPUT_STREAM__RESOURCE_NAME = eINSTANCE.getOutputStream_ResourceName();

	}

} //FileRoutingPackage
