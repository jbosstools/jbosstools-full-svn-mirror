/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.datasource;

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
 * Smooks Datasource Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.datasource.DatasourceFactory
 * @model kind="package"
 * @generated
 */
public interface DatasourcePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "datasource"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/datasource-1.1.xsd"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ds"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DatasourcePackage eINSTANCE = org.jboss.tools.smooks.model.datasource.impl.DatasourcePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl <em>Direct</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.datasource.impl.DirectImpl
	 * @see org.jboss.tools.smooks.model.datasource.impl.DatasourcePackageImpl#getDirect()
	 * @generated
	 */
	int DIRECT = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Auto Commit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__AUTO_COMMIT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Bind On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__BIND_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Bind On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__BIND_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Datasource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__DATASOURCE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Driver</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__DRIVER = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__PASSWORD = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__URL = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Username</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT__USERNAME = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Direct</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIRECT_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceDocumentRootImpl <em>Data Source Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.datasource.impl.DataSourceDocumentRootImpl
	 * @see org.jboss.tools.smooks.model.datasource.impl.DatasourcePackageImpl#getDataSourceDocumentRoot()
	 * @generated
	 */
	int DATA_SOURCE_DOCUMENT_ROOT = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Direct</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_DOCUMENT_ROOT__DIRECT = 3;

	/**
	 * The feature id for the '<em><b>JNDI</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_DOCUMENT_ROOT__JNDI = 4;

	/**
	 * The number of structural features of the '<em>Data Source Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_DOCUMENT_ROOT_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceJndiImpl <em>Data Source Jndi</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.datasource.impl.DataSourceJndiImpl
	 * @see org.jboss.tools.smooks.model.datasource.impl.DatasourcePackageImpl#getDataSourceJndi()
	 * @generated
	 */
	int DATA_SOURCE_JNDI = 2;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_JNDI__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_JNDI__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_JNDI__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_JNDI__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_JNDI__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Auto Commit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_JNDI__AUTO_COMMIT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Bind On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_JNDI__BIND_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Datasource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_JNDI__DATASOURCE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Data Source Jndi</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SOURCE_JNDI_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.datasource.Direct <em>Direct</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Direct</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.Direct
	 * @generated
	 */
	EClass getDirect();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.Direct#isAutoCommit <em>Auto Commit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Auto Commit</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.Direct#isAutoCommit()
	 * @see #getDirect()
	 * @generated
	 */
	EAttribute getDirect_AutoCommit();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.Direct#getBindOnElement <em>Bind On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bind On Element</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.Direct#getBindOnElement()
	 * @see #getDirect()
	 * @generated
	 */
	EAttribute getDirect_BindOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.Direct#getBindOnElementNS <em>Bind On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bind On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.Direct#getBindOnElementNS()
	 * @see #getDirect()
	 * @generated
	 */
	EAttribute getDirect_BindOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.Direct#getDatasource <em>Datasource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Datasource</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.Direct#getDatasource()
	 * @see #getDirect()
	 * @generated
	 */
	EAttribute getDirect_Datasource();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.Direct#getDriver <em>Driver</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Driver</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.Direct#getDriver()
	 * @see #getDirect()
	 * @generated
	 */
	EAttribute getDirect_Driver();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.Direct#getPassword <em>Password</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Password</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.Direct#getPassword()
	 * @see #getDirect()
	 * @generated
	 */
	EAttribute getDirect_Password();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.Direct#getUrl <em>Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Url</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.Direct#getUrl()
	 * @see #getDirect()
	 * @generated
	 */
	EAttribute getDirect_Url();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.Direct#getUsername <em>Username</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Username</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.Direct#getUsername()
	 * @see #getDirect()
	 * @generated
	 */
	EAttribute getDirect_Username();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot <em>Data Source Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Source Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot
	 * @generated
	 */
	EClass getDataSourceDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getMixed()
	 * @see #getDataSourceDocumentRoot()
	 * @generated
	 */
	EAttribute getDataSourceDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getXMLNSPrefixMap()
	 * @see #getDataSourceDocumentRoot()
	 * @generated
	 */
	EReference getDataSourceDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getXSISchemaLocation()
	 * @see #getDataSourceDocumentRoot()
	 * @generated
	 */
	EReference getDataSourceDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getDirect <em>Direct</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Direct</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getDirect()
	 * @see #getDataSourceDocumentRoot()
	 * @generated
	 */
	EReference getDataSourceDocumentRoot_Direct();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getJNDI <em>JNDI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>JNDI</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getJNDI()
	 * @see #getDataSourceDocumentRoot()
	 * @generated
	 */
	EReference getDataSourceDocumentRoot_JNDI();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi <em>Data Source Jndi</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Source Jndi</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceJndi
	 * @generated
	 */
	EClass getDataSourceJndi();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#isAutoCommit <em>Auto Commit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Auto Commit</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceJndi#isAutoCommit()
	 * @see #getDataSourceJndi()
	 * @generated
	 */
	EAttribute getDataSourceJndi_AutoCommit();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#getBindOnElement <em>Bind On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bind On Element</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceJndi#getBindOnElement()
	 * @see #getDataSourceJndi()
	 * @generated
	 */
	EAttribute getDataSourceJndi_BindOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#getDatasource <em>Datasource</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Datasource</em>'.
	 * @see org.jboss.tools.smooks.model.datasource.DataSourceJndi#getDatasource()
	 * @see #getDataSourceJndi()
	 * @generated
	 */
	EAttribute getDataSourceJndi_Datasource();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DatasourceFactory getDatasourceFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl <em>Direct</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.datasource.impl.DirectImpl
		 * @see org.jboss.tools.smooks.model.datasource.impl.DatasourcePackageImpl#getDirect()
		 * @generated
		 */
		EClass DIRECT = eINSTANCE.getDirect();

		/**
		 * The meta object literal for the '<em><b>Auto Commit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT__AUTO_COMMIT = eINSTANCE.getDirect_AutoCommit();

		/**
		 * The meta object literal for the '<em><b>Bind On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT__BIND_ON_ELEMENT = eINSTANCE.getDirect_BindOnElement();

		/**
		 * The meta object literal for the '<em><b>Bind On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT__BIND_ON_ELEMENT_NS = eINSTANCE.getDirect_BindOnElementNS();

		/**
		 * The meta object literal for the '<em><b>Datasource</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT__DATASOURCE = eINSTANCE.getDirect_Datasource();

		/**
		 * The meta object literal for the '<em><b>Driver</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT__DRIVER = eINSTANCE.getDirect_Driver();

		/**
		 * The meta object literal for the '<em><b>Password</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT__PASSWORD = eINSTANCE.getDirect_Password();

		/**
		 * The meta object literal for the '<em><b>Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT__URL = eINSTANCE.getDirect_Url();

		/**
		 * The meta object literal for the '<em><b>Username</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DIRECT__USERNAME = eINSTANCE.getDirect_Username();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceDocumentRootImpl <em>Data Source Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.datasource.impl.DataSourceDocumentRootImpl
		 * @see org.jboss.tools.smooks.model.datasource.impl.DatasourcePackageImpl#getDataSourceDocumentRoot()
		 * @generated
		 */
		EClass DATA_SOURCE_DOCUMENT_ROOT = eINSTANCE.getDataSourceDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_SOURCE_DOCUMENT_ROOT__MIXED = eINSTANCE.getDataSourceDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SOURCE_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDataSourceDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SOURCE_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDataSourceDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Direct</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SOURCE_DOCUMENT_ROOT__DIRECT = eINSTANCE.getDataSourceDocumentRoot_Direct();

		/**
		 * The meta object literal for the '<em><b>JNDI</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SOURCE_DOCUMENT_ROOT__JNDI = eINSTANCE.getDataSourceDocumentRoot_JNDI();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceJndiImpl <em>Data Source Jndi</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.datasource.impl.DataSourceJndiImpl
		 * @see org.jboss.tools.smooks.model.datasource.impl.DatasourcePackageImpl#getDataSourceJndi()
		 * @generated
		 */
		EClass DATA_SOURCE_JNDI = eINSTANCE.getDataSourceJndi();

		/**
		 * The meta object literal for the '<em><b>Auto Commit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_SOURCE_JNDI__AUTO_COMMIT = eINSTANCE.getDataSourceJndi_AutoCommit();

		/**
		 * The meta object literal for the '<em><b>Bind On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_SOURCE_JNDI__BIND_ON_ELEMENT = eINSTANCE.getDataSourceJndi_BindOnElement();

		/**
		 * The meta object literal for the '<em><b>Datasource</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_SOURCE_JNDI__DATASOURCE = eINSTANCE.getDataSourceJndi_Datasource();

	}

} //DatasourcePackage
