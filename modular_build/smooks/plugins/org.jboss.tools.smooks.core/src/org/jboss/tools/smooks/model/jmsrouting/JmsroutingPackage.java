/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting;

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
 * Smooks JMS Routing Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.jmsrouting.JmsroutingFactory
 * @model kind="package"
 * @generated
 */
public interface JmsroutingPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "jmsrouting"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/jms-routing-1.1.xsd"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "jms"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	JmsroutingPackage eINSTANCE = org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.ConnectionImpl <em>Connection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.ConnectionImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getConnection()
	 * @generated
	 */
	int CONNECTION = 0;

	/**
	 * The feature id for the '<em><b>Factory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION__FACTORY = 0;

	/**
	 * The feature id for the '<em><b>Security Credential</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION__SECURITY_CREDENTIAL = 1;

	/**
	 * The feature id for the '<em><b>Security Principal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION__SECURITY_PRINCIPAL = 2;

	/**
	 * The number of structural features of the '<em>Connection</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.JmsDocumentRootImpl <em>Jms Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsDocumentRootImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getJmsDocumentRoot()
	 * @generated
	 */
	int JMS_DOCUMENT_ROOT = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Router</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_DOCUMENT_ROOT__ROUTER = 3;

	/**
	 * The number of structural features of the '<em>Jms Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.HighWaterMarkImpl <em>High Water Mark</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.HighWaterMarkImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getHighWaterMark()
	 * @generated
	 */
	int HIGH_WATER_MARK = 2;

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
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.JndiImpl <em>Jndi</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JndiImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getJndi()
	 * @generated
	 */
	int JNDI = 3;

	/**
	 * The feature id for the '<em><b>Context Factory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JNDI__CONTEXT_FACTORY = 0;

	/**
	 * The feature id for the '<em><b>Naming Factory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JNDI__NAMING_FACTORY = 1;

	/**
	 * The feature id for the '<em><b>Provider Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JNDI__PROVIDER_URL = 2;

	/**
	 * The number of structural features of the '<em>Jndi</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JNDI_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.MessageImpl <em>Message</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.MessageImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getMessage()
	 * @generated
	 */
	int MESSAGE = 4;

	/**
	 * The feature id for the '<em><b>Correlation Id Pattern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE__CORRELATION_ID_PATTERN = 0;

	/**
	 * The feature id for the '<em><b>Delivery Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE__DELIVERY_MODE = 1;

	/**
	 * The feature id for the '<em><b>Priority</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE__PRIORITY = 2;

	/**
	 * The feature id for the '<em><b>Time To Live</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE__TIME_TO_LIVE = 3;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE__TYPE = 4;

	/**
	 * The number of structural features of the '<em>Message</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.JmsRouterImpl <em>Jms Router</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsRouterImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getJmsRouter()
	 * @generated
	 */
	int JMS_ROUTER = 5;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Message</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__MESSAGE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Connection</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__CONNECTION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Session</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__SESSION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Jndi</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__JNDI = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>High Water Mark</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__HIGH_WATER_MARK = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Destination</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__DESTINATION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Execute Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__EXECUTE_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Route On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__ROUTE_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Route On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER__ROUTE_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Jms Router</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTER_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 10;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.SessionImpl <em>Session</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.SessionImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getSession()
	 * @generated
	 */
	int SESSION = 6;

	/**
	 * The feature id for the '<em><b>Acknowledge Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SESSION__ACKNOWLEDGE_MODE = 0;

	/**
	 * The feature id for the '<em><b>Transacted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SESSION__TRANSACTED = 1;

	/**
	 * The number of structural features of the '<em>Session</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SESSION_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode <em>Acknowledge Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getAcknowledgeMode()
	 * @generated
	 */
	int ACKNOWLEDGE_MODE = 7;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.DeliveryMode <em>Delivery Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.DeliveryMode
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getDeliveryMode()
	 * @generated
	 */
	int DELIVERY_MODE = 8;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting.MessageType <em>Message Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.MessageType
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getMessageType()
	 * @generated
	 */
	int MESSAGE_TYPE = 9;

	/**
	 * The meta object id for the '<em>Acknowledge Mode Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getAcknowledgeModeObject()
	 * @generated
	 */
	int ACKNOWLEDGE_MODE_OBJECT = 10;

	/**
	 * The meta object id for the '<em>Delivery Mode Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.DeliveryMode
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getDeliveryModeObject()
	 * @generated
	 */
	int DELIVERY_MODE_OBJECT = 11;

	/**
	 * The meta object id for the '<em>Message Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting.MessageType
	 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getMessageTypeObject()
	 * @generated
	 */
	int MESSAGE_TYPE_OBJECT = 12;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting.Connection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connection</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Connection
	 * @generated
	 */
	EClass getConnection();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Connection#getFactory <em>Factory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Factory</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Connection#getFactory()
	 * @see #getConnection()
	 * @generated
	 */
	EAttribute getConnection_Factory();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Connection#getSecurityCredential <em>Security Credential</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Security Credential</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Connection#getSecurityCredential()
	 * @see #getConnection()
	 * @generated
	 */
	EAttribute getConnection_SecurityCredential();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Connection#getSecurityPrincipal <em>Security Principal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Security Principal</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Connection#getSecurityPrincipal()
	 * @see #getConnection()
	 * @generated
	 */
	EAttribute getConnection_SecurityPrincipal();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot <em>Jms Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Jms Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot
	 * @generated
	 */
	EClass getJmsDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot#getMixed()
	 * @see #getJmsDocumentRoot()
	 * @generated
	 */
	EAttribute getJmsDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot#getXMLNSPrefixMap()
	 * @see #getJmsDocumentRoot()
	 * @generated
	 */
	EReference getJmsDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot#getXSISchemaLocation()
	 * @see #getJmsDocumentRoot()
	 * @generated
	 */
	EReference getJmsDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot#getRouter <em>Router</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Router</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot#getRouter()
	 * @see #getJmsDocumentRoot()
	 * @generated
	 */
	EReference getJmsDocumentRoot_Router();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting.HighWaterMark <em>High Water Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>High Water Mark</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.HighWaterMark
	 * @generated
	 */
	EClass getHighWaterMark();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.HighWaterMark#getMark <em>Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mark</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.HighWaterMark#getMark()
	 * @see #getHighWaterMark()
	 * @generated
	 */
	EAttribute getHighWaterMark_Mark();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.HighWaterMark#getPollFrequency <em>Poll Frequency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Poll Frequency</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.HighWaterMark#getPollFrequency()
	 * @see #getHighWaterMark()
	 * @generated
	 */
	EAttribute getHighWaterMark_PollFrequency();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.HighWaterMark#getTimeout <em>Timeout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timeout</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.HighWaterMark#getTimeout()
	 * @see #getHighWaterMark()
	 * @generated
	 */
	EAttribute getHighWaterMark_Timeout();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi <em>Jndi</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Jndi</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Jndi
	 * @generated
	 */
	EClass getJndi();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getContextFactory <em>Context Factory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Context Factory</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Jndi#getContextFactory()
	 * @see #getJndi()
	 * @generated
	 */
	EAttribute getJndi_ContextFactory();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getNamingFactory <em>Naming Factory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Naming Factory</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Jndi#getNamingFactory()
	 * @see #getJndi()
	 * @generated
	 */
	EAttribute getJndi_NamingFactory();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getProviderUrl <em>Provider Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Provider Url</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Jndi#getProviderUrl()
	 * @see #getJndi()
	 * @generated
	 */
	EAttribute getJndi_ProviderUrl();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting.Message <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Message</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Message
	 * @generated
	 */
	EClass getMessage();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Message#getCorrelationIdPattern <em>Correlation Id Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Correlation Id Pattern</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Message#getCorrelationIdPattern()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_CorrelationIdPattern();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Message#getDeliveryMode <em>Delivery Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delivery Mode</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Message#getDeliveryMode()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_DeliveryMode();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Message#getPriority <em>Priority</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Priority</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Message#getPriority()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_Priority();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Message#getTimeToLive <em>Time To Live</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time To Live</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Message#getTimeToLive()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_TimeToLive();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Message#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Message#getType()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_Type();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter <em>Jms Router</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Jms Router</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter
	 * @generated
	 */
	EClass getJmsRouter();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getMessage <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Message</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getMessage()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EReference getJmsRouter_Message();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getConnection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Connection</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getConnection()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EReference getJmsRouter_Connection();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getSession <em>Session</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Session</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getSession()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EReference getJmsRouter_Session();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getJndi <em>Jndi</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Jndi</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getJndi()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EReference getJmsRouter_Jndi();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getHighWaterMark <em>High Water Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>High Water Mark</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getHighWaterMark()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EReference getJmsRouter_HighWaterMark();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getBeanId()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EAttribute getJmsRouter_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getDestination <em>Destination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Destination</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getDestination()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EAttribute getJmsRouter_Destination();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#isExecuteBefore <em>Execute Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute Before</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#isExecuteBefore()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EAttribute getJmsRouter_ExecuteBefore();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getRouteOnElement <em>Route On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route On Element</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getRouteOnElement()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EAttribute getJmsRouter_RouteOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getRouteOnElementNS <em>Route On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsRouter#getRouteOnElementNS()
	 * @see #getJmsRouter()
	 * @generated
	 */
	EAttribute getJmsRouter_RouteOnElementNS();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting.Session <em>Session</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Session</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Session
	 * @generated
	 */
	EClass getSession();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Session#getAcknowledgeMode <em>Acknowledge Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Acknowledge Mode</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Session#getAcknowledgeMode()
	 * @see #getSession()
	 * @generated
	 */
	EAttribute getSession_AcknowledgeMode();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting.Session#isTransacted <em>Transacted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transacted</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Session#isTransacted()
	 * @see #getSession()
	 * @generated
	 */
	EAttribute getSession_Transacted();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode <em>Acknowledge Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Acknowledge Mode</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode
	 * @generated
	 */
	EEnum getAcknowledgeMode();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.jmsrouting.DeliveryMode <em>Delivery Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Delivery Mode</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.DeliveryMode
	 * @generated
	 */
	EEnum getDeliveryMode();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.jmsrouting.MessageType <em>Message Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Message Type</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.MessageType
	 * @generated
	 */
	EEnum getMessageType();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode <em>Acknowledge Mode Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Acknowledge Mode Object</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode
	 * @model instanceClass="jmsrouting.AcknowledgeMode"
	 *        extendedMetaData="name='acknowledgeMode:Object' baseType='acknowledgeMode'"
	 * @generated
	 */
	EDataType getAcknowledgeModeObject();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.jmsrouting.DeliveryMode <em>Delivery Mode Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Delivery Mode Object</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.DeliveryMode
	 * @model instanceClass="jmsrouting.DeliveryMode"
	 *        extendedMetaData="name='deliveryMode:Object' baseType='deliveryMode'"
	 * @generated
	 */
	EDataType getDeliveryModeObject();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.jmsrouting.MessageType <em>Message Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Message Type Object</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting.MessageType
	 * @model instanceClass="jmsrouting.MessageType"
	 *        extendedMetaData="name='messageType:Object' baseType='messageType'"
	 * @generated
	 */
	EDataType getMessageTypeObject();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	JmsroutingFactory getJmsroutingFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.ConnectionImpl <em>Connection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.ConnectionImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getConnection()
		 * @generated
		 */
		EClass CONNECTION = eINSTANCE.getConnection();

		/**
		 * The meta object literal for the '<em><b>Factory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONNECTION__FACTORY = eINSTANCE.getConnection_Factory();

		/**
		 * The meta object literal for the '<em><b>Security Credential</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONNECTION__SECURITY_CREDENTIAL = eINSTANCE.getConnection_SecurityCredential();

		/**
		 * The meta object literal for the '<em><b>Security Principal</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONNECTION__SECURITY_PRINCIPAL = eINSTANCE.getConnection_SecurityPrincipal();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.JmsDocumentRootImpl <em>Jms Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsDocumentRootImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getJmsDocumentRoot()
		 * @generated
		 */
		EClass JMS_DOCUMENT_ROOT = eINSTANCE.getJmsDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JMS_DOCUMENT_ROOT__MIXED = eINSTANCE.getJmsDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getJmsDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getJmsDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Router</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_DOCUMENT_ROOT__ROUTER = eINSTANCE.getJmsDocumentRoot_Router();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.HighWaterMarkImpl <em>High Water Mark</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.HighWaterMarkImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getHighWaterMark()
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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.JndiImpl <em>Jndi</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JndiImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getJndi()
		 * @generated
		 */
		EClass JNDI = eINSTANCE.getJndi();

		/**
		 * The meta object literal for the '<em><b>Context Factory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JNDI__CONTEXT_FACTORY = eINSTANCE.getJndi_ContextFactory();

		/**
		 * The meta object literal for the '<em><b>Naming Factory</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JNDI__NAMING_FACTORY = eINSTANCE.getJndi_NamingFactory();

		/**
		 * The meta object literal for the '<em><b>Provider Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JNDI__PROVIDER_URL = eINSTANCE.getJndi_ProviderUrl();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.MessageImpl <em>Message</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.MessageImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getMessage()
		 * @generated
		 */
		EClass MESSAGE = eINSTANCE.getMessage();

		/**
		 * The meta object literal for the '<em><b>Correlation Id Pattern</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE__CORRELATION_ID_PATTERN = eINSTANCE.getMessage_CorrelationIdPattern();

		/**
		 * The meta object literal for the '<em><b>Delivery Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE__DELIVERY_MODE = eINSTANCE.getMessage_DeliveryMode();

		/**
		 * The meta object literal for the '<em><b>Priority</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE__PRIORITY = eINSTANCE.getMessage_Priority();

		/**
		 * The meta object literal for the '<em><b>Time To Live</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE__TIME_TO_LIVE = eINSTANCE.getMessage_TimeToLive();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MESSAGE__TYPE = eINSTANCE.getMessage_Type();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.JmsRouterImpl <em>Jms Router</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsRouterImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getJmsRouter()
		 * @generated
		 */
		EClass JMS_ROUTER = eINSTANCE.getJmsRouter();

		/**
		 * The meta object literal for the '<em><b>Message</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_ROUTER__MESSAGE = eINSTANCE.getJmsRouter_Message();

		/**
		 * The meta object literal for the '<em><b>Connection</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_ROUTER__CONNECTION = eINSTANCE.getJmsRouter_Connection();

		/**
		 * The meta object literal for the '<em><b>Session</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_ROUTER__SESSION = eINSTANCE.getJmsRouter_Session();

		/**
		 * The meta object literal for the '<em><b>Jndi</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_ROUTER__JNDI = eINSTANCE.getJmsRouter_Jndi();

		/**
		 * The meta object literal for the '<em><b>High Water Mark</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_ROUTER__HIGH_WATER_MARK = eINSTANCE.getJmsRouter_HighWaterMark();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JMS_ROUTER__BEAN_ID = eINSTANCE.getJmsRouter_BeanId();

		/**
		 * The meta object literal for the '<em><b>Destination</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JMS_ROUTER__DESTINATION = eINSTANCE.getJmsRouter_Destination();

		/**
		 * The meta object literal for the '<em><b>Execute Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JMS_ROUTER__EXECUTE_BEFORE = eINSTANCE.getJmsRouter_ExecuteBefore();

		/**
		 * The meta object literal for the '<em><b>Route On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JMS_ROUTER__ROUTE_ON_ELEMENT = eINSTANCE.getJmsRouter_RouteOnElement();

		/**
		 * The meta object literal for the '<em><b>Route On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JMS_ROUTER__ROUTE_ON_ELEMENT_NS = eINSTANCE.getJmsRouter_RouteOnElementNS();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.impl.SessionImpl <em>Session</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.SessionImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getSession()
		 * @generated
		 */
		EClass SESSION = eINSTANCE.getSession();

		/**
		 * The meta object literal for the '<em><b>Acknowledge Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SESSION__ACKNOWLEDGE_MODE = eINSTANCE.getSession_AcknowledgeMode();

		/**
		 * The meta object literal for the '<em><b>Transacted</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SESSION__TRANSACTED = eINSTANCE.getSession_Transacted();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode <em>Acknowledge Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getAcknowledgeMode()
		 * @generated
		 */
		EEnum ACKNOWLEDGE_MODE = eINSTANCE.getAcknowledgeMode();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.DeliveryMode <em>Delivery Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.DeliveryMode
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getDeliveryMode()
		 * @generated
		 */
		EEnum DELIVERY_MODE = eINSTANCE.getDeliveryMode();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting.MessageType <em>Message Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.MessageType
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getMessageType()
		 * @generated
		 */
		EEnum MESSAGE_TYPE = eINSTANCE.getMessageType();

		/**
		 * The meta object literal for the '<em>Acknowledge Mode Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getAcknowledgeModeObject()
		 * @generated
		 */
		EDataType ACKNOWLEDGE_MODE_OBJECT = eINSTANCE.getAcknowledgeModeObject();

		/**
		 * The meta object literal for the '<em>Delivery Mode Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.DeliveryMode
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getDeliveryModeObject()
		 * @generated
		 */
		EDataType DELIVERY_MODE_OBJECT = eINSTANCE.getDeliveryModeObject();

		/**
		 * The meta object literal for the '<em>Message Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting.MessageType
		 * @see org.jboss.tools.smooks.model.jmsrouting.impl.JmsroutingPackageImpl#getMessageTypeObject()
		 * @generated
		 */
		EDataType MESSAGE_TYPE_OBJECT = eINSTANCE.getMessageTypeObject();

	}

} //JmsroutingPackage
