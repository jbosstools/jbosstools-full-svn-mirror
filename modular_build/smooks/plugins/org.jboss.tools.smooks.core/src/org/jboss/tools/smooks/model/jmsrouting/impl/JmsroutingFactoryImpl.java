/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode;
import org.jboss.tools.smooks.model.jmsrouting.Connection;
import org.jboss.tools.smooks.model.jmsrouting.DeliveryMode;
import org.jboss.tools.smooks.model.jmsrouting.HighWaterMark;
import org.jboss.tools.smooks.model.jmsrouting.JmsDocumentRoot;
import org.jboss.tools.smooks.model.jmsrouting.JmsRouter;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingFactory;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting.Jndi;
import org.jboss.tools.smooks.model.jmsrouting.Message;
import org.jboss.tools.smooks.model.jmsrouting.MessageType;
import org.jboss.tools.smooks.model.jmsrouting.Session;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JmsroutingFactoryImpl extends EFactoryImpl implements JmsroutingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static JmsroutingFactory init() {
		try {
			JmsroutingFactory theJmsroutingFactory = (JmsroutingFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/jms-routing-1.1.xsd");  //$NON-NLS-1$
			if (theJmsroutingFactory != null) {
				return theJmsroutingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new JmsroutingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JmsroutingFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case JmsroutingPackage.CONNECTION: return createConnection();
			case JmsroutingPackage.JMS_DOCUMENT_ROOT: return createJmsDocumentRoot();
			case JmsroutingPackage.HIGH_WATER_MARK: return createHighWaterMark();
			case JmsroutingPackage.JNDI: return createJndi();
			case JmsroutingPackage.MESSAGE: return createMessage();
			case JmsroutingPackage.JMS_ROUTER: return createJmsRouter();
			case JmsroutingPackage.SESSION: return createSession();
			default:
				throw new IllegalArgumentException(Messages.JmsroutingFactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.JmsroutingFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case JmsroutingPackage.ACKNOWLEDGE_MODE:
				return createAcknowledgeModeFromString(eDataType, initialValue);
			case JmsroutingPackage.DELIVERY_MODE:
				return createDeliveryModeFromString(eDataType, initialValue);
			case JmsroutingPackage.MESSAGE_TYPE:
				return createMessageTypeFromString(eDataType, initialValue);
			case JmsroutingPackage.ACKNOWLEDGE_MODE_OBJECT:
				return createAcknowledgeModeObjectFromString(eDataType, initialValue);
			case JmsroutingPackage.DELIVERY_MODE_OBJECT:
				return createDeliveryModeObjectFromString(eDataType, initialValue);
			case JmsroutingPackage.MESSAGE_TYPE_OBJECT:
				return createMessageTypeObjectFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(Messages.JmsroutingFactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.JmsroutingFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case JmsroutingPackage.ACKNOWLEDGE_MODE:
				return convertAcknowledgeModeToString(eDataType, instanceValue);
			case JmsroutingPackage.DELIVERY_MODE:
				return convertDeliveryModeToString(eDataType, instanceValue);
			case JmsroutingPackage.MESSAGE_TYPE:
				return convertMessageTypeToString(eDataType, instanceValue);
			case JmsroutingPackage.ACKNOWLEDGE_MODE_OBJECT:
				return convertAcknowledgeModeObjectToString(eDataType, instanceValue);
			case JmsroutingPackage.DELIVERY_MODE_OBJECT:
				return convertDeliveryModeObjectToString(eDataType, instanceValue);
			case JmsroutingPackage.MESSAGE_TYPE_OBJECT:
				return convertMessageTypeObjectToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(Messages.JmsroutingFactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.JmsroutingFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connection createConnection() {
		ConnectionImpl connection = new ConnectionImpl();
		return connection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JmsDocumentRoot createJmsDocumentRoot() {
		JmsDocumentRootImpl jmsDocumentRoot = new JmsDocumentRootImpl();
		return jmsDocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighWaterMark createHighWaterMark() {
		HighWaterMarkImpl highWaterMark = new HighWaterMarkImpl();
		return highWaterMark;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Jndi createJndi() {
		JndiImpl jndi = new JndiImpl();
		return jndi;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Message createMessage() {
		MessageImpl message = new MessageImpl();
		return message;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JmsRouter createJmsRouter() {
		JmsRouterImpl jmsRouter = new JmsRouterImpl();
		return jmsRouter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Session createSession() {
		SessionImpl session = new SessionImpl();
		return session;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AcknowledgeMode createAcknowledgeModeFromString(EDataType eDataType, String initialValue) {
		AcknowledgeMode result = AcknowledgeMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException(Messages.JmsroutingFactoryImpl_Error_Value + initialValue + Messages.JmsroutingFactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAcknowledgeModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeliveryMode createDeliveryModeFromString(EDataType eDataType, String initialValue) {
		DeliveryMode result = DeliveryMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException(Messages.JmsroutingFactoryImpl_Error_Value + initialValue + Messages.JmsroutingFactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDeliveryModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageType createMessageTypeFromString(EDataType eDataType, String initialValue) {
		MessageType result = MessageType.get(initialValue);
		if (result == null) throw new IllegalArgumentException(Messages.JmsroutingFactoryImpl_Error_Value + initialValue + Messages.JmsroutingFactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMessageTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AcknowledgeMode createAcknowledgeModeObjectFromString(EDataType eDataType, String initialValue) {
		return createAcknowledgeModeFromString(JmsroutingPackage.Literals.ACKNOWLEDGE_MODE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAcknowledgeModeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertAcknowledgeModeToString(JmsroutingPackage.Literals.ACKNOWLEDGE_MODE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeliveryMode createDeliveryModeObjectFromString(EDataType eDataType, String initialValue) {
		return createDeliveryModeFromString(JmsroutingPackage.Literals.DELIVERY_MODE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDeliveryModeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertDeliveryModeToString(JmsroutingPackage.Literals.DELIVERY_MODE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageType createMessageTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createMessageTypeFromString(JmsroutingPackage.Literals.MESSAGE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMessageTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertMessageTypeToString(JmsroutingPackage.Literals.MESSAGE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JmsroutingPackage getJmsroutingPackage() {
		return (JmsroutingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static JmsroutingPackage getPackage() {
		return JmsroutingPackage.eINSTANCE;
	}

} //JmsroutingFactoryImpl
