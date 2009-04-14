/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting.impl;


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
import org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode;
import org.jboss.tools.smooks.model.jmsrouting.Connection;
import org.jboss.tools.smooks.model.jmsrouting.DeliveryMode;
import org.jboss.tools.smooks.model.jmsrouting.DocumentRoot;
import org.jboss.tools.smooks.model.jmsrouting.HighWaterMark;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingFactory;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting.Jndi;
import org.jboss.tools.smooks.model.jmsrouting.Message;
import org.jboss.tools.smooks.model.jmsrouting.MessageType;
import org.jboss.tools.smooks.model.jmsrouting.Router;
import org.jboss.tools.smooks.model.jmsrouting.Session;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.impl.SmooksPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JmsroutingPackageImpl extends EPackageImpl implements JmsroutingPackage {
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
	private EClass documentRootEClass = null;

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
	private EClass routerEClass = null;

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
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private JmsroutingPackageImpl() {
		super(eNS_URI, JmsroutingFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static JmsroutingPackage init() {
		if (isInited) return (JmsroutingPackage)EPackage.Registry.INSTANCE.getEPackage(JmsroutingPackage.eNS_URI);

		// Obtain or create and register package
		JmsroutingPackageImpl theJmsroutingPackage = (JmsroutingPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof JmsroutingPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new JmsroutingPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		SmooksPackageImpl theSmooksPackage = (SmooksPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) instanceof SmooksPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) : SmooksPackage.eINSTANCE);
		CommonPackageImpl theCommonPackage = (CommonPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) instanceof CommonPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) : CommonPackage.eINSTANCE);

		// Create package meta-data objects
		theJmsroutingPackage.createPackageContents();
		theSmooksPackage.createPackageContents();
		theCommonPackage.createPackageContents();

		// Initialize created meta-data
		theJmsroutingPackage.initializePackageContents();
		theSmooksPackage.initializePackageContents();
		theCommonPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theJmsroutingPackage.freeze();

		return theJmsroutingPackage;
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
	public EClass getDocumentRoot() {
		return documentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Mixed() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_XMLNSPrefixMap() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_XSISchemaLocation() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Router() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
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
	public EAttribute getJndi_ProviderUrl() {
		return (EAttribute)jndiEClass.getEStructuralFeatures().get(2);
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
	public EClass getRouter() {
		return routerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRouter_Message() {
		return (EReference)routerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRouter_Connection() {
		return (EReference)routerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRouter_Session() {
		return (EReference)routerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRouter_Jndi() {
		return (EReference)routerEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRouter_HighWaterMark() {
		return (EReference)routerEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRouter_BeanId() {
		return (EAttribute)routerEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRouter_Destination() {
		return (EAttribute)routerEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRouter_ExecuteBefore() {
		return (EAttribute)routerEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRouter_RouteOnElement() {
		return (EAttribute)routerEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRouter_RouteOnElementNS() {
		return (EAttribute)routerEClass.getEStructuralFeatures().get(9);
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
	public JmsroutingFactory getJmsroutingFactory() {
		return (JmsroutingFactory)getEFactoryInstance();
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

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__ROUTER);

		highWaterMarkEClass = createEClass(HIGH_WATER_MARK);
		createEAttribute(highWaterMarkEClass, HIGH_WATER_MARK__MARK);
		createEAttribute(highWaterMarkEClass, HIGH_WATER_MARK__POLL_FREQUENCY);
		createEAttribute(highWaterMarkEClass, HIGH_WATER_MARK__TIMEOUT);

		jndiEClass = createEClass(JNDI);
		createEAttribute(jndiEClass, JNDI__CONTEXT_FACTORY);
		createEAttribute(jndiEClass, JNDI__NAMING_FACTORY);
		createEAttribute(jndiEClass, JNDI__PROVIDER_URL);

		messageEClass = createEClass(MESSAGE);
		createEAttribute(messageEClass, MESSAGE__CORRELATION_ID_PATTERN);
		createEAttribute(messageEClass, MESSAGE__DELIVERY_MODE);
		createEAttribute(messageEClass, MESSAGE__PRIORITY);
		createEAttribute(messageEClass, MESSAGE__TIME_TO_LIVE);
		createEAttribute(messageEClass, MESSAGE__TYPE);

		routerEClass = createEClass(ROUTER);
		createEReference(routerEClass, ROUTER__MESSAGE);
		createEReference(routerEClass, ROUTER__CONNECTION);
		createEReference(routerEClass, ROUTER__SESSION);
		createEReference(routerEClass, ROUTER__JNDI);
		createEReference(routerEClass, ROUTER__HIGH_WATER_MARK);
		createEAttribute(routerEClass, ROUTER__BEAN_ID);
		createEAttribute(routerEClass, ROUTER__DESTINATION);
		createEAttribute(routerEClass, ROUTER__EXECUTE_BEFORE);
		createEAttribute(routerEClass, ROUTER__ROUTE_ON_ELEMENT);
		createEAttribute(routerEClass, ROUTER__ROUTE_ON_ELEMENT_NS);

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
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
		SmooksPackage theSmooksPackage = (SmooksPackage)EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		routerEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());

		// Initialize classes and features; add operations and parameters
		initEClass(connectionEClass, Connection.class, "Connection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConnection_Factory(), theXMLTypePackage.getString(), "factory", "ConnectionFactory", 0, 1, Connection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConnection_SecurityCredential(), theXMLTypePackage.getString(), "securityCredential", null, 0, 1, Connection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConnection_SecurityPrincipal(), theXMLTypePackage.getString(), "securityPrincipal", null, 0, 1, Connection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Router(), this.getRouter(), null, "router", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(highWaterMarkEClass, HighWaterMark.class, "HighWaterMark", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHighWaterMark_Mark(), theXMLTypePackage.getInt(), "mark", "200", 0, 1, HighWaterMark.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHighWaterMark_PollFrequency(), theXMLTypePackage.getInt(), "pollFrequency", "1000", 0, 1, HighWaterMark.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getHighWaterMark_Timeout(), theXMLTypePackage.getInt(), "timeout", "60000", 0, 1, HighWaterMark.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(jndiEClass, Jndi.class, "Jndi", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJndi_ContextFactory(), theXMLTypePackage.getString(), "contextFactory", "org.jnp.interfaces.NamingContextFactory", 0, 1, Jndi.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJndi_NamingFactory(), theXMLTypePackage.getString(), "namingFactory", "org.jboss.naming:java.naming.factory.url.pkgs", 0, 1, Jndi.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJndi_ProviderUrl(), theXMLTypePackage.getString(), "providerUrl", "jnp://localhost:1099", 0, 1, Jndi.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(messageEClass, Message.class, "Message", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMessage_CorrelationIdPattern(), theXMLTypePackage.getString(), "correlationIdPattern", null, 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMessage_DeliveryMode(), this.getDeliveryMode(), "deliveryMode", "persistent", 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMessage_Priority(), theXMLTypePackage.getInt(), "priority", "4", 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMessage_TimeToLive(), theXMLTypePackage.getLong(), "timeToLive", "0", 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getMessage_Type(), this.getMessageType(), "type", "TextMessage", 0, 1, Message.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(routerEClass, Router.class, "JMS Router", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRouter_Message(), this.getMessage(), null, "message", null, 0, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRouter_Connection(), this.getConnection(), null, "connection", null, 0, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRouter_Session(), this.getSession(), null, "session", null, 0, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRouter_Jndi(), this.getJndi(), null, "jndi", null, 0, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRouter_HighWaterMark(), this.getHighWaterMark(), null, "highWaterMark", null, 0, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouter_BeanId(), theXMLTypePackage.getString(), "beanId", null, 1, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouter_Destination(), theXMLTypePackage.getString(), "destination", null, 1, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouter_ExecuteBefore(), theXMLTypePackage.getBoolean(), "executeBefore", "false", 0, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouter_RouteOnElement(), theXMLTypePackage.getString(), "routeOnElement", null, 1, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouter_RouteOnElementNS(), theXMLTypePackage.getString(), "routeOnElementNS", null, 0, 1, Router.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sessionEClass, Session.class, "Session", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSession_AcknowledgeMode(), this.getAcknowledgeMode(), "acknowledgeMode", "AUTO_ACKNOWLEDGE", 0, 1, Session.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSession_Transacted(), theXMLTypePackage.getBoolean(), "transacted", "false", 0, 1, Session.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(acknowledgeModeEEnum, AcknowledgeMode.class, "AcknowledgeMode");
		addEEnumLiteral(acknowledgeModeEEnum, AcknowledgeMode.AUTOACKNOWLEDGE);
		addEEnumLiteral(acknowledgeModeEEnum, AcknowledgeMode.CLIENTACKNOWLEDGE);
		addEEnumLiteral(acknowledgeModeEEnum, AcknowledgeMode.DUPSOKACKNOWLEDGE);

		initEEnum(deliveryModeEEnum, DeliveryMode.class, "DeliveryMode");
		addEEnumLiteral(deliveryModeEEnum, DeliveryMode.PERSISTENT);
		addEEnumLiteral(deliveryModeEEnum, DeliveryMode.NON_PERSISTENT);

		initEEnum(messageTypeEEnum, MessageType.class, "MessageType");
		addEEnumLiteral(messageTypeEEnum, MessageType.TEXT_MESSAGE);
		addEEnumLiteral(messageTypeEEnum, MessageType.OBJECT_MESSAGE);
		addEEnumLiteral(messageTypeEEnum, MessageType.MAP_MESSAGE);

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
		  (documentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getDocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });			
		addAnnotation
		  (getDocumentRoot_Router(), 
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
		  (routerEClass, 
		   source, 
		   new String[] {
			 "name", "router",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getRouter_Message(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "message",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getRouter_Connection(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "connection",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getRouter_Session(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "session",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getRouter_Jndi(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "jndi",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getRouter_HighWaterMark(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "highWaterMark",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getRouter_BeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "beanId"
		   });			
		addAnnotation
		  (getRouter_Destination(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "destination"
		   });			
		addAnnotation
		  (getRouter_ExecuteBefore(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "executeBefore"
		   });			
		addAnnotation
		  (getRouter_RouteOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "routeOnElement"
		   });			
		addAnnotation
		  (getRouter_RouteOnElementNS(), 
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

} //JmsroutingPackageImpl
