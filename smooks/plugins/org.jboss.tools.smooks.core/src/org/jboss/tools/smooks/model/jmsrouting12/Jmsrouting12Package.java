/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12;

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
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Factory
 * @model kind="package"
 * @generated
 */
public interface Jmsrouting12Package extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "jmsrouting12";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/jms-routing-1.2.xsd";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "jms";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Jmsrouting12Package eINSTANCE = org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.ConnectionImpl <em>Connection</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.ConnectionImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getConnection()
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
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMSRouting12DocumentRootImpl <em>JMS Routing12 Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.JMSRouting12DocumentRootImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getJMSRouting12DocumentRoot()
	 * @generated
	 */
	int JMS_ROUTING12_DOCUMENT_ROOT = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTING12_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTING12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTING12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Router</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTING12_DOCUMENT_ROOT__ROUTER = 3;

	/**
	 * The number of structural features of the '<em>JMS Routing12 Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JMS_ROUTING12_DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.HighWaterMarkImpl <em>High Water Mark</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.HighWaterMarkImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getHighWaterMark()
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
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JndiImpl <em>Jndi</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.JndiImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getJndi()
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
	 * The feature id for the '<em><b>Properties</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JNDI__PROPERTIES = 2;

	/**
	 * The feature id for the '<em><b>Provider Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JNDI__PROVIDER_URL = 3;

	/**
	 * The number of structural features of the '<em>Jndi</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JNDI_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.MessageImpl <em>Message</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.MessageImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getMessage()
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
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.RouterImpl <em>Router</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.RouterImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getRouter()
	 * @generated
	 */
	int ROUTER = 5;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Message</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__MESSAGE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Connection</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__CONNECTION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Session</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__SESSION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Jndi</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__JNDI = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>High Water Mark</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__HIGH_WATER_MARK = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__BEAN_ID = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Destination</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__DESTINATION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Execute Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__EXECUTE_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;

	/**
	 * The feature id for the '<em><b>Route On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__ROUTE_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 8;

	/**
	 * The feature id for the '<em><b>Route On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER__ROUTE_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 9;

	/**
	 * The number of structural features of the '<em>Router</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTER_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 10;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.SessionImpl <em>Session</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.SessionImpl
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getSession()
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
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode <em>Acknowledge Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getAcknowledgeMode()
	 * @generated
	 */
	int ACKNOWLEDGE_MODE = 7;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode <em>Delivery Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getDeliveryMode()
	 * @generated
	 */
	int DELIVERY_MODE = 8;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.jmsrouting12.MessageType <em>Message Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.MessageType
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getMessageType()
	 * @generated
	 */
	int MESSAGE_TYPE = 9;

	/**
	 * The meta object id for the '<em>Acknowledge Mode Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getAcknowledgeModeObject()
	 * @generated
	 */
	int ACKNOWLEDGE_MODE_OBJECT = 10;

	/**
	 * The meta object id for the '<em>Delivery Mode Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getDeliveryModeObject()
	 * @generated
	 */
	int DELIVERY_MODE_OBJECT = 11;

	/**
	 * The meta object id for the '<em>Message Type Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.jmsrouting12.MessageType
	 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getMessageTypeObject()
	 * @generated
	 */
	int MESSAGE_TYPE_OBJECT = 12;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting12.Connection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connection</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Connection
	 * @generated
	 */
	EClass getConnection();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getFactory <em>Factory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Factory</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Connection#getFactory()
	 * @see #getConnection()
	 * @generated
	 */
	EAttribute getConnection_Factory();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getSecurityCredential <em>Security Credential</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Security Credential</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Connection#getSecurityCredential()
	 * @see #getConnection()
	 * @generated
	 */
	EAttribute getConnection_SecurityCredential();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getSecurityPrincipal <em>Security Principal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Security Principal</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Connection#getSecurityPrincipal()
	 * @see #getConnection()
	 * @generated
	 */
	EAttribute getConnection_SecurityPrincipal();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot <em>JMS Routing12 Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>JMS Routing12 Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot
	 * @generated
	 */
	EClass getJMSRouting12DocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getMixed()
	 * @see #getJMSRouting12DocumentRoot()
	 * @generated
	 */
	EAttribute getJMSRouting12DocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getXMLNSPrefixMap()
	 * @see #getJMSRouting12DocumentRoot()
	 * @generated
	 */
	EReference getJMSRouting12DocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getXSISchemaLocation()
	 * @see #getJMSRouting12DocumentRoot()
	 * @generated
	 */
	EReference getJMSRouting12DocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getRouter <em>Router</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Router</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getRouter()
	 * @see #getJMSRouting12DocumentRoot()
	 * @generated
	 */
	EReference getJMSRouting12DocumentRoot_Router();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark <em>High Water Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>High Water Mark</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark
	 * @generated
	 */
	EClass getHighWaterMark();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getMark <em>Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mark</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getMark()
	 * @see #getHighWaterMark()
	 * @generated
	 */
	EAttribute getHighWaterMark_Mark();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getPollFrequency <em>Poll Frequency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Poll Frequency</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getPollFrequency()
	 * @see #getHighWaterMark()
	 * @generated
	 */
	EAttribute getHighWaterMark_PollFrequency();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getTimeout <em>Timeout</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timeout</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getTimeout()
	 * @see #getHighWaterMark()
	 * @generated
	 */
	EAttribute getHighWaterMark_Timeout();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi <em>Jndi</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Jndi</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jndi
	 * @generated
	 */
	EClass getJndi();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getContextFactory <em>Context Factory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Context Factory</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jndi#getContextFactory()
	 * @see #getJndi()
	 * @generated
	 */
	EAttribute getJndi_ContextFactory();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getNamingFactory <em>Naming Factory</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Naming Factory</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jndi#getNamingFactory()
	 * @see #getJndi()
	 * @generated
	 */
	EAttribute getJndi_NamingFactory();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Properties</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jndi#getProperties()
	 * @see #getJndi()
	 * @generated
	 */
	EAttribute getJndi_Properties();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getProviderUrl <em>Provider Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Provider Url</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jndi#getProviderUrl()
	 * @see #getJndi()
	 * @generated
	 */
	EAttribute getJndi_ProviderUrl();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting12.Message <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Message</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Message
	 * @generated
	 */
	EClass getMessage();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getCorrelationIdPattern <em>Correlation Id Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Correlation Id Pattern</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Message#getCorrelationIdPattern()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_CorrelationIdPattern();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getDeliveryMode <em>Delivery Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Delivery Mode</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Message#getDeliveryMode()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_DeliveryMode();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getPriority <em>Priority</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Priority</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Message#getPriority()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_Priority();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getTimeToLive <em>Time To Live</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time To Live</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Message#getTimeToLive()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_TimeToLive();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Message#getType()
	 * @see #getMessage()
	 * @generated
	 */
	EAttribute getMessage_Type();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting12.Router <em>Router</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Router</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router
	 * @generated
	 */
	EClass getRouter();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getMessage <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Message</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#getMessage()
	 * @see #getRouter()
	 * @generated
	 */
	EReference getRouter_Message();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getConnection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Connection</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#getConnection()
	 * @see #getRouter()
	 * @generated
	 */
	EReference getRouter_Connection();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getSession <em>Session</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Session</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#getSession()
	 * @see #getRouter()
	 * @generated
	 */
	EReference getRouter_Session();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getJndi <em>Jndi</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Jndi</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#getJndi()
	 * @see #getRouter()
	 * @generated
	 */
	EReference getRouter_Jndi();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getHighWaterMark <em>High Water Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>High Water Mark</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#getHighWaterMark()
	 * @see #getRouter()
	 * @generated
	 */
	EReference getRouter_HighWaterMark();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getBeanId <em>Bean Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#getBeanId()
	 * @see #getRouter()
	 * @generated
	 */
	EAttribute getRouter_BeanId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getDestination <em>Destination</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Destination</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#getDestination()
	 * @see #getRouter()
	 * @generated
	 */
	EAttribute getRouter_Destination();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#isExecuteBefore <em>Execute Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute Before</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#isExecuteBefore()
	 * @see #getRouter()
	 * @generated
	 */
	EAttribute getRouter_ExecuteBefore();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getRouteOnElement <em>Route On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route On Element</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#getRouteOnElement()
	 * @see #getRouter()
	 * @generated
	 */
	EAttribute getRouter_RouteOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getRouteOnElementNS <em>Route On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Router#getRouteOnElementNS()
	 * @see #getRouter()
	 * @generated
	 */
	EAttribute getRouter_RouteOnElementNS();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.jmsrouting12.Session <em>Session</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Session</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Session
	 * @generated
	 */
	EClass getSession();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Session#getAcknowledgeMode <em>Acknowledge Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Acknowledge Mode</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Session#getAcknowledgeMode()
	 * @see #getSession()
	 * @generated
	 */
	EAttribute getSession_AcknowledgeMode();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.jmsrouting12.Session#isTransacted <em>Transacted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Transacted</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Session#isTransacted()
	 * @see #getSession()
	 * @generated
	 */
	EAttribute getSession_Transacted();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode <em>Acknowledge Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Acknowledge Mode</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode
	 * @generated
	 */
	EEnum getAcknowledgeMode();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode <em>Delivery Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Delivery Mode</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode
	 * @generated
	 */
	EEnum getDeliveryMode();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.jmsrouting12.MessageType <em>Message Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Message Type</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.MessageType
	 * @generated
	 */
	EEnum getMessageType();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode <em>Acknowledge Mode Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Acknowledge Mode Object</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode
	 * @model instanceClass="jmsrouting12.AcknowledgeMode"
	 *        extendedMetaData="name='acknowledgeMode:Object' baseType='acknowledgeMode'"
	 * @generated
	 */
	EDataType getAcknowledgeModeObject();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode <em>Delivery Mode Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Delivery Mode Object</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode
	 * @model instanceClass="jmsrouting12.DeliveryMode"
	 *        extendedMetaData="name='deliveryMode:Object' baseType='deliveryMode'"
	 * @generated
	 */
	EDataType getDeliveryModeObject();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.jmsrouting12.MessageType <em>Message Type Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Message Type Object</em>'.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.MessageType
	 * @model instanceClass="jmsrouting12.MessageType"
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
	Jmsrouting12Factory getJmsrouting12Factory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.ConnectionImpl <em>Connection</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.ConnectionImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getConnection()
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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMSRouting12DocumentRootImpl <em>JMS Routing12 Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.JMSRouting12DocumentRootImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getJMSRouting12DocumentRoot()
		 * @generated
		 */
		EClass JMS_ROUTING12_DOCUMENT_ROOT = eINSTANCE.getJMSRouting12DocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JMS_ROUTING12_DOCUMENT_ROOT__MIXED = eINSTANCE.getJMSRouting12DocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_ROUTING12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getJMSRouting12DocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_ROUTING12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getJMSRouting12DocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Router</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JMS_ROUTING12_DOCUMENT_ROOT__ROUTER = eINSTANCE.getJMSRouting12DocumentRoot_Router();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.HighWaterMarkImpl <em>High Water Mark</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.HighWaterMarkImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getHighWaterMark()
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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JndiImpl <em>Jndi</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.JndiImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getJndi()
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
		 * The meta object literal for the '<em><b>Properties</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JNDI__PROPERTIES = eINSTANCE.getJndi_Properties();

		/**
		 * The meta object literal for the '<em><b>Provider Url</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JNDI__PROVIDER_URL = eINSTANCE.getJndi_ProviderUrl();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.MessageImpl <em>Message</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.MessageImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getMessage()
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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.RouterImpl <em>Router</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.RouterImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getRouter()
		 * @generated
		 */
		EClass ROUTER = eINSTANCE.getRouter();

		/**
		 * The meta object literal for the '<em><b>Message</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTER__MESSAGE = eINSTANCE.getRouter_Message();

		/**
		 * The meta object literal for the '<em><b>Connection</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTER__CONNECTION = eINSTANCE.getRouter_Connection();

		/**
		 * The meta object literal for the '<em><b>Session</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTER__SESSION = eINSTANCE.getRouter_Session();

		/**
		 * The meta object literal for the '<em><b>Jndi</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTER__JNDI = eINSTANCE.getRouter_Jndi();

		/**
		 * The meta object literal for the '<em><b>High Water Mark</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTER__HIGH_WATER_MARK = eINSTANCE.getRouter_HighWaterMark();

		/**
		 * The meta object literal for the '<em><b>Bean Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTER__BEAN_ID = eINSTANCE.getRouter_BeanId();

		/**
		 * The meta object literal for the '<em><b>Destination</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTER__DESTINATION = eINSTANCE.getRouter_Destination();

		/**
		 * The meta object literal for the '<em><b>Execute Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTER__EXECUTE_BEFORE = eINSTANCE.getRouter_ExecuteBefore();

		/**
		 * The meta object literal for the '<em><b>Route On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTER__ROUTE_ON_ELEMENT = eINSTANCE.getRouter_RouteOnElement();

		/**
		 * The meta object literal for the '<em><b>Route On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTER__ROUTE_ON_ELEMENT_NS = eINSTANCE.getRouter_RouteOnElementNS();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.impl.SessionImpl <em>Session</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.SessionImpl
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getSession()
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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode <em>Acknowledge Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getAcknowledgeMode()
		 * @generated
		 */
		EEnum ACKNOWLEDGE_MODE = eINSTANCE.getAcknowledgeMode();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode <em>Delivery Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getDeliveryMode()
		 * @generated
		 */
		EEnum DELIVERY_MODE = eINSTANCE.getDeliveryMode();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.jmsrouting12.MessageType <em>Message Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.MessageType
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getMessageType()
		 * @generated
		 */
		EEnum MESSAGE_TYPE = eINSTANCE.getMessageType();

		/**
		 * The meta object literal for the '<em>Acknowledge Mode Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getAcknowledgeModeObject()
		 * @generated
		 */
		EDataType ACKNOWLEDGE_MODE_OBJECT = eINSTANCE.getAcknowledgeModeObject();

		/**
		 * The meta object literal for the '<em>Delivery Mode Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getDeliveryModeObject()
		 * @generated
		 */
		EDataType DELIVERY_MODE_OBJECT = eINSTANCE.getDeliveryModeObject();

		/**
		 * The meta object literal for the '<em>Message Type Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.jmsrouting12.MessageType
		 * @see org.jboss.tools.smooks.model.jmsrouting12.impl.Jmsrouting12PackageImpl#getMessageTypeObject()
		 * @generated
		 */
		EDataType MESSAGE_TYPE_OBJECT = eINSTANCE.getMessageTypeObject();

	}

} //Jmsrouting12Package
