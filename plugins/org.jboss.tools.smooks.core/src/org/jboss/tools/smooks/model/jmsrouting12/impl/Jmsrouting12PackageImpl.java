/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12.impl;




import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.common.CommonPackage;
import org.jboss.tools.smooks.model.common.impl.CommonPackageImpl;
import org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode;
import org.jboss.tools.smooks.model.jmsrouting12.Connection;
import org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode;
import org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark;
import org.jboss.tools.smooks.model.jmsrouting12.JMS12Router;
import org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Factory;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package;
import org.jboss.tools.smooks.model.jmsrouting12.Jndi;
import org.jboss.tools.smooks.model.jmsrouting12.Message;
import org.jboss.tools.smooks.model.jmsrouting12.MessageType;
import org.jboss.tools.smooks.model.jmsrouting12.Session;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.impl.SmooksPackageImpl;



/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Jmsrouting12PackageImpl extends EPackageImpl implements Jmsrouting12Package {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass connectionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass jmsRouting12DocumentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass highWaterMarkEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass jndiEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass messageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass jms12RouterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sessionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum acknowledgeModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum deliveryModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum messageTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType acknowledgeModeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType deliveryModeObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType messageTypeObjectEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Jmsrouting12PackageImpl() {
		super(eNS_URI, Jmsrouting12Factory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link Jmsrouting12Package#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Jmsrouting12Package init() {
		if (isInited) return (Jmsrouting12Package)EPackage.Registry.INSTANCE.getEPackage(Jmsrouting12Package.eNS_URI);

		// Obtain or create and register package
		Jmsrouting12PackageImpl theJmsrouting12Package = (Jmsrouting12PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Jmsrouting12PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Jmsrouting12PackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		CommonPackageImpl theCommonPackage = (CommonPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) instanceof CommonPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) : CommonPackage.eINSTANCE);
		SmooksPackageImpl theSmooksPackage = (SmooksPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) instanceof SmooksPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) : SmooksPackage.eINSTANCE);

		// Create package meta-data objects
		theJmsrouting12Package.createPackageContents();
		theCommonPackage.createPackageContents();
		theSmooksPackage.createPackageContents();

		// Initialize created meta-data
		theJmsrouting12Package.initializePackageContents();
		theCommonPackage.initializePackageContents();
		theSmooksPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJmsrouting12Package.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Jmsrouting12Package.eNS_URI, theJmsrouting12Package);
		return theJmsrouting12Package;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConnection() {
		return connectionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConnection_Factory() {
		return (EAttribute)connectionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConnection_SecurityCredential() {
		return (EAttribute)connectionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConnection_SecurityPrincipal() {
		return (EAttribute)connectionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJMSRouting12DocumentRoot() {
		return jmsRouting12DocumentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJMSRouting12DocumentRoot_Mixed() {
		return (EAttribute)jmsRouting12DocumentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJMSRouting12DocumentRoot_XMLNSPrefixMap() {
		return (EReference)jmsRouting12DocumentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJMSRouting12DocumentRoot_XSISchemaLocation() {
		return (EReference)jmsRouting12DocumentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJMSRouting12DocumentRoot_Router() {
		return (EReference)jmsRouting12DocumentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHighWaterMark() {
		return highWaterMarkEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHighWaterMark_Mark() {
		return (EAttribute)highWaterMarkEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHighWaterMark_PollFrequency() {
		return (EAttribute)highWaterMarkEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHighWaterMark_Timeout() {
		return (EAttribute)highWaterMarkEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJndi() {
		return jndiEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJndi_ContextFactory() {
		return (EAttribute)jndiEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJndi_NamingFactory() {
		return (EAttribute)jndiEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJndi_Properties() {
		return (EAttribute)jndiEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJndi_ProviderUrl() {
		return (EAttribute)jndiEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMessage() {
		return messageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessage_CorrelationIdPattern() {
		return (EAttribute)messageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessage_DeliveryMode() {
		return (EAttribute)messageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessage_Priority() {
		return (EAttribute)messageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessage_TimeToLive() {
		return (EAttribute)messageEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMessage_Type() {
		return (EAttribute)messageEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getJMS12Router() {
		return jms12RouterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJMS12Router_Message() {
		return (EReference)jms12RouterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJMS12Router_Connection() {
		return (EReference)jms12RouterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJMS12Router_Session() {
		return (EReference)jms12RouterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJMS12Router_Jndi() {
		return (EReference)jms12RouterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getJMS12Router_HighWaterMark() {
		return (EReference)jms12RouterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJMS12Router_BeanId() {
		return (EAttribute)jms12RouterEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJMS12Router_Destination() {
		return (EAttribute)jms12RouterEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJMS12Router_ExecuteBefore() {
		return (EAttribute)jms12RouterEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJMS12Router_RouteOnElement() {
		return (EAttribute)jms12RouterEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getJMS12Router_RouteOnElementNS() {
		return (EAttribute)jms12RouterEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSession() {
		return sessionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSession_AcknowledgeMode() {
		return (EAttribute)sessionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSession_Transacted() {
		return (EAttribute)sessionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getAcknowledgeMode() {
		return acknowledgeModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getDeliveryMode() {
		return deliveryModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getMessageType() {
		return messageTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getAcknowledgeModeObject() {
		return acknowledgeModeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getDeliveryModeObject() {
		return deliveryModeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getMessageTypeObject() {
		return messageTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Jmsrouting12Factory getJmsrouting12Factory() {
		return (Jmsrouting12Factory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		connectionEClass = createEClass(CONNECTION);
		createEAttribute(connectionEClass, CONNECTION__FACTORY);
		createEAttribute(connectionEClass, CONNECTION__SECURITY_CREDENTIAL);
		createEAttribute(connectionEClass, CONNECTION__SECURITY_PRINCIPAL);

		jmsRouting12DocumentRootEClass = createEClass(JMS_ROUTING12_DOCUMENT_ROOT);
		createEAttribute(jmsRouting12DocumentRootEClass, JMS_ROUTING12_DOCUMENT_ROOT__MIXED);
		createEReference(jmsRouting12DocumentRootEClass, JMS_ROUTING12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(jmsRouting12DocumentRootEClass, JMS_ROUTING12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(jmsRouting12DocumentRootEClass, JMS_ROUTING12_DOCUMENT_ROOT__ROUTER);

		highWaterMarkEClass = createEClass(HIGH_WATER_MARK);
		createEAttribute(highWaterMarkEClass, HIGH_WATER_MARK__MARK);
		createEAttribute(highWaterMarkEClass, HIGH_WATER_MARK__POLL_FREQUENCY);
		createEAttribute(highWaterMarkEClass, HIGH_WATER_MARK__TIMEOUT);

		jndiEClass = createEClass(JNDI);
		createEAttribute(jndiEClass, JNDI__CONTEXT_FACTORY);
		createEAttribute(jndiEClass, JNDI__NAMING_FACTORY);
		createEAttribute(jndiEClass, JNDI__PROPERTIES);
		createEAttribute(jndiEClass, JNDI__PROVIDER_URL);

		messageEClass = createEClass(MESSAGE);
		createEAttribute(messageEClass, MESSAGE__CORRELATION_ID_PATTERN);
		createEAttribute(messageEClass, MESSAGE__DELIVERY_MODE);
		createEAttribute(messageEClass, MESSAGE__PRIORITY);
		createEAttribute(messageEClass, MESSAGE__TIME_TO_LIVE);
		createEAttribute(messageEClass, MESSAGE__TYPE);

		jms12RouterEClass = createEClass(JMS12_ROUTER);
		createEReference(jms12RouterEClass, JMS12_ROUTER__MESSAGE);
		createEReference(jms12RouterEClass, JMS12_ROUTER__CONNECTION);
		createEReference(jms12RouterEClass, JMS12_ROUTER__SESSION);
		createEReference(jms12RouterEClass, JMS12_ROUTER__JNDI);
		createEReference(jms12RouterEClass, JMS12_ROUTER__HIGH_WATER_MARK);
		createEAttribute(jms12RouterEClass, JMS12_ROUTER__BEAN_ID);
		createEAttribute(jms12RouterEClass, JMS12_ROUTER__DESTINATION);
		createEAttribute(jms12RouterEClass, JMS12_ROUTER__EXECUTE_BEFORE);
		createEAttribute(jms12RouterEClass, JMS12_ROUTER__ROUTE_ON_ELEMENT);
		createEAttribute(jms12RouterEClass, JMS12_ROUTER__ROUTE_ON_ELEMENT_NS);

		sessionEClass = createEClass(SESSION);
		createEAttribute(sessionEClass, SESSION__ACKNOWLEDGE_MODE);
		createEAttribute(sessionEClass, SESSION__TRANSACTED);

		// Create enums
		acknowledgeModeEEnum = createEEnum(ACKNOWLEDGE_MODE);
		deliveryModeEEnum = createEEnum(DELIVERY_MODE);
		messageTypeEEnum = createEEnum(MESSAGE_TYPE);

		// Create data types
		acknowledgeModeObjectEDataType = createEDataType(ACKNOWLEDGE_MODE_OBJECT);
		deliveryModeObjectEDataType = createEDataType(DELIVERY_MODE_OBJECT);
		messageTypeObjectEDataType = createEDataType(MESSAGE_TYPE_OBJECT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		CommonPackage theCommonPackage = (CommonPackage)EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
		SmooksPackage theSmooksPackage = (SmooksPackage)EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI);

		// Add supertypes to classes
		connectionEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		highWaterMarkEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		jndiEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		messageEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		jms12RouterEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());
		sessionEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());

		// Initialize classes and features; add operations and parameters
		initEClass(connectionEClass, Connection.class, "Connection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConnection_Factory(), theXMLTypePackage.getString(), "factory", "ConnectionFactory", 0, 1, Connection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConnection_SecurityCredential(), theXMLTypePackage.getString(), "securityCredential", null, 0, 1, Connection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConnection_SecurityPrincipal(), theXMLTypePackage.getString(), "securityPrincipal", null, 0, 1, Connection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(jmsRouting12DocumentRootEClass, JMSRouting12DocumentRoot.class, "JMSRouting12DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJMSRouting12DocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJMSRouting12DocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJMSRouting12DocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJMSRouting12DocumentRoot_Router(), this.getJMS12Router(), null, "router", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(highWaterMarkEClass, HighWaterMark.class, "HighWaterMark", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHighWaterMark_Mark(), theXMLTypePackage.getInt(), "mark", "200", 0, 1, HighWaterMark.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHighWaterMark_PollFrequency(), theXMLTypePackage.getInt(), "pollFrequency", "1000", 0, 1, HighWaterMark.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHighWaterMark_Timeout(), theXMLTypePackage.getInt(), "timeout", "60000", 0, 1, HighWaterMark.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(jndiEClass, Jndi.class, "Jndi", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJndi_ContextFactory(), theXMLTypePackage.getString(), "contextFactory", null, 0, 1, Jndi.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJndi_NamingFactory(), theXMLTypePackage.getString(), "namingFactory", null, 0, 1, Jndi.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJndi_Properties(), theXMLTypePackage.getString(), "properties", null, 0, 1, Jndi.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJndi_ProviderUrl(), theXMLTypePackage.getString(), "providerUrl", null, 0, 1, Jndi.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(messageEClass, Message.class, "Message", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMessage_CorrelationIdPattern(), theXMLTypePackage.getString(), "correlationIdPattern", null, 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMessage_DeliveryMode(), this.getDeliveryMode(), "deliveryMode", "persistent", 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMessage_Priority(), theXMLTypePackage.getInt(), "priority", "4", 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMessage_TimeToLive(), theXMLTypePackage.getLong(), "timeToLive", "0", 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMessage_Type(), this.getMessageType(), "type", "TextMessage", 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(jms12RouterEClass, JMS12Router.class, "JMS12Router", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getJMS12Router_Message(), this.getMessage(), null, "message", null, 0, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJMS12Router_Connection(), this.getConnection(), null, "connection", null, 0, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJMS12Router_Session(), this.getSession(), null, "session", null, 0, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJMS12Router_Jndi(), this.getJndi(), null, "jndi", null, 0, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJMS12Router_HighWaterMark(), this.getHighWaterMark(), null, "highWaterMark", null, 0, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJMS12Router_BeanId(), theXMLTypePackage.getString(), "beanId", null, 1, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJMS12Router_Destination(), theXMLTypePackage.getString(), "destination", null, 1, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJMS12Router_ExecuteBefore(), theXMLTypePackage.getBoolean(), "executeBefore", "false", 0, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJMS12Router_RouteOnElement(), theXMLTypePackage.getString(), "routeOnElement", null, 1, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJMS12Router_RouteOnElementNS(), theXMLTypePackage.getString(), "routeOnElementNS", null, 0, 1, JMS12Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sessionEClass, Session.class, "Session", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSession_AcknowledgeMode(), this.getAcknowledgeMode(), "acknowledgeMode", "AUTO_ACKNOWLEDGE", 0, 1, Session.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSession_Transacted(), theXMLTypePackage.getBoolean(), "transacted", "false", 0, 1, Session.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(acknowledgeModeEEnum, AcknowledgeMode.class, "AcknowledgeMode");
		addEEnumLiteral(acknowledgeModeEEnum, AcknowledgeMode.AUTOACKNOWLEDGE_LITERAL);
		addEEnumLiteral(acknowledgeModeEEnum, AcknowledgeMode.CLIENTACKNOWLEDGE_LITERAL);
		addEEnumLiteral(acknowledgeModeEEnum, AcknowledgeMode.DUPSOKACKNOWLEDGE_LITERAL);

		initEEnum(deliveryModeEEnum, DeliveryMode.class, "DeliveryMode");
		addEEnumLiteral(deliveryModeEEnum, DeliveryMode.PERSISTENT_LITERAL);
		addEEnumLiteral(deliveryModeEEnum, DeliveryMode.NON_PERSISTENT_LITERAL);

		initEEnum(messageTypeEEnum, MessageType.class, "MessageType");
		addEEnumLiteral(messageTypeEEnum, MessageType.TEXT_MESSAGE_LITERAL);
		addEEnumLiteral(messageTypeEEnum, MessageType.OBJECT_MESSAGE_LITERAL);
		addEEnumLiteral(messageTypeEEnum, MessageType.MAP_MESSAGE_LITERAL);

		// Initialize data types
		initEDataType(acknowledgeModeObjectEDataType, AcknowledgeMode.class, "AcknowledgeModeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(deliveryModeObjectEDataType, DeliveryMode.class, "DeliveryModeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(messageTypeObjectEDataType, MessageType.class, "MessageTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";				
		addAnnotation
		  (acknowledgeModeEEnum, 
		   source, 
		   new String[] {
			 "name", "acknowledgeMode"
		   });					
		addAnnotation
		  (acknowledgeModeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "acknowledgeMode:Object",
			 "baseType", "acknowledgeMode"
		   });			
		addAnnotation
		  (connectionEClass, 
		   source, 
		   new String[] {
			 "name", "connection",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getConnection_Factory(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "factory"
		   });			
		addAnnotation
		  (getConnection_SecurityCredential(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "securityCredential"
		   });			
		addAnnotation
		  (getConnection_SecurityPrincipal(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "securityPrincipal"
		   });			
		addAnnotation
		  (deliveryModeEEnum, 
		   source, 
		   new String[] {
			 "name", "deliveryMode"
		   });				
		addAnnotation
		  (deliveryModeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "deliveryMode:Object",
			 "baseType", "deliveryMode"
		   });		
		addAnnotation
		  (jmsRouting12DocumentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getJMSRouting12DocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getJMSRouting12DocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getJMSRouting12DocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });			
		addAnnotation
		  (getJMSRouting12DocumentRoot_Router(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "router",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config"
		   });			
		addAnnotation
		  (highWaterMarkEClass, 
		   source, 
		   new String[] {
			 "name", "highWaterMark",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getHighWaterMark_Mark(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "mark"
		   });			
		addAnnotation
		  (getHighWaterMark_PollFrequency(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "pollFrequency"
		   });			
		addAnnotation
		  (getHighWaterMark_Timeout(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "timeout"
		   });			
		addAnnotation
		  (jndiEClass, 
		   source, 
		   new String[] {
			 "name", "jndi",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getJndi_ContextFactory(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "contextFactory"
		   });			
		addAnnotation
		  (getJndi_NamingFactory(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "namingFactory"
		   });			
		addAnnotation
		  (getJndi_Properties(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "properties"
		   });			
		addAnnotation
		  (getJndi_ProviderUrl(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "providerUrl"
		   });			
		addAnnotation
		  (messageEClass, 
		   source, 
		   new String[] {
			 "name", "message",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getMessage_CorrelationIdPattern(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "correlationIdPattern",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getMessage_DeliveryMode(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "deliveryMode"
		   });			
		addAnnotation
		  (getMessage_Priority(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "priority"
		   });			
		addAnnotation
		  (getMessage_TimeToLive(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "timeToLive"
		   });			
		addAnnotation
		  (getMessage_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type"
		   });			
		addAnnotation
		  (messageTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "messageType"
		   });					
		addAnnotation
		  (messageTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "messageType:Object",
			 "baseType", "messageType"
		   });			
		addAnnotation
		  (jms12RouterEClass, 
		   source, 
		   new String[] {
			 "name", "router",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getJMS12Router_Message(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "message",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getJMS12Router_Connection(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "connection",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getJMS12Router_Session(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "session",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getJMS12Router_Jndi(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "jndi",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getJMS12Router_HighWaterMark(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "highWaterMark",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getJMS12Router_BeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "beanId"
		   });			
		addAnnotation
		  (getJMS12Router_Destination(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "destination"
		   });			
		addAnnotation
		  (getJMS12Router_ExecuteBefore(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "executeBefore"
		   });			
		addAnnotation
		  (getJMS12Router_RouteOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "routeOnElement"
		   });			
		addAnnotation
		  (getJMS12Router_RouteOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "routeOnElementNS"
		   });			
		addAnnotation
		  (sessionEClass, 
		   source, 
		   new String[] {
			 "name", "session",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getSession_AcknowledgeMode(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "acknowledgeMode"
		   });			
		addAnnotation
		  (getSession_Transacted(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "transacted"
		   });
	}

} //Jmsrouting12PackageImpl
