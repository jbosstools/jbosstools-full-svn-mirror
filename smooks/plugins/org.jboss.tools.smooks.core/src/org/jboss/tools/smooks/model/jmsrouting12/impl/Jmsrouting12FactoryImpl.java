/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.jmsrouting12.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Jmsrouting12FactoryImpl extends EFactoryImpl implements Jmsrouting12Factory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Jmsrouting12Factory init() {
		try {
			Jmsrouting12Factory theJmsrouting12Factory = (Jmsrouting12Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/jms-routing-1.2.xsd");  //$NON-NLS-1$
			if (theJmsrouting12Factory != null) {
				return theJmsrouting12Factory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Jmsrouting12FactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Jmsrouting12FactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Jmsrouting12Package.CONNECTION: return createConnection();
			case Jmsrouting12Package.JMS_ROUTING12_DOCUMENT_ROOT: return createJMSRouting12DocumentRoot();
			case Jmsrouting12Package.HIGH_WATER_MARK: return createHighWaterMark();
			case Jmsrouting12Package.JNDI: return createJndi();
			case Jmsrouting12Package.MESSAGE: return createMessage();
			case Jmsrouting12Package.JMS12_ROUTER: return createJMS12Router();
			case Jmsrouting12Package.SESSION: return createSession();
			default:
				throw new IllegalArgumentException(Messages.Jmsrouting12FactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.Jmsrouting12FactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case Jmsrouting12Package.ACKNOWLEDGE_MODE:
				return createAcknowledgeModeFromString(eDataType, initialValue);
			case Jmsrouting12Package.DELIVERY_MODE:
				return createDeliveryModeFromString(eDataType, initialValue);
			case Jmsrouting12Package.MESSAGE_TYPE:
				return createMessageTypeFromString(eDataType, initialValue);
			case Jmsrouting12Package.ACKNOWLEDGE_MODE_OBJECT:
				return createAcknowledgeModeObjectFromString(eDataType, initialValue);
			case Jmsrouting12Package.DELIVERY_MODE_OBJECT:
				return createDeliveryModeObjectFromString(eDataType, initialValue);
			case Jmsrouting12Package.MESSAGE_TYPE_OBJECT:
				return createMessageTypeObjectFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(Messages.Jmsrouting12FactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.Jmsrouting12FactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case Jmsrouting12Package.ACKNOWLEDGE_MODE:
				return convertAcknowledgeModeToString(eDataType, instanceValue);
			case Jmsrouting12Package.DELIVERY_MODE:
				return convertDeliveryModeToString(eDataType, instanceValue);
			case Jmsrouting12Package.MESSAGE_TYPE:
				return convertMessageTypeToString(eDataType, instanceValue);
			case Jmsrouting12Package.ACKNOWLEDGE_MODE_OBJECT:
				return convertAcknowledgeModeObjectToString(eDataType, instanceValue);
			case Jmsrouting12Package.DELIVERY_MODE_OBJECT:
				return convertDeliveryModeObjectToString(eDataType, instanceValue);
			case Jmsrouting12Package.MESSAGE_TYPE_OBJECT:
				return convertMessageTypeObjectToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(Messages.Jmsrouting12FactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.Jmsrouting12FactoryImpl_Error_Not_Valid_Classifier);
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
	public JMSRouting12DocumentRoot createJMSRouting12DocumentRoot() {
		JMSRouting12DocumentRootImpl jmsRouting12DocumentRoot = new JMSRouting12DocumentRootImpl();
		return jmsRouting12DocumentRoot;
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
	public JMS12Router createJMS12Router() {
		JMS12RouterImpl jms12Router = new JMS12RouterImpl();
		return jms12Router;
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
		if (result == null) throw new IllegalArgumentException(Messages.Jmsrouting12FactoryImpl_Error_Value + initialValue + Messages.Jmsrouting12FactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
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
		if (result == null) throw new IllegalArgumentException(Messages.Jmsrouting12FactoryImpl_Error_Value + initialValue + Messages.Jmsrouting12FactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
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
		if (result == null) throw new IllegalArgumentException(Messages.Jmsrouting12FactoryImpl_Error_Value + initialValue + Messages.Jmsrouting12FactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
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
		return createAcknowledgeModeFromString(Jmsrouting12Package.Literals.ACKNOWLEDGE_MODE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAcknowledgeModeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertAcknowledgeModeToString(Jmsrouting12Package.Literals.ACKNOWLEDGE_MODE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeliveryMode createDeliveryModeObjectFromString(EDataType eDataType, String initialValue) {
		return createDeliveryModeFromString(Jmsrouting12Package.Literals.DELIVERY_MODE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDeliveryModeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertDeliveryModeToString(Jmsrouting12Package.Literals.DELIVERY_MODE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageType createMessageTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createMessageTypeFromString(Jmsrouting12Package.Literals.MESSAGE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertMessageTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertMessageTypeToString(Jmsrouting12Package.Literals.MESSAGE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Jmsrouting12Package getJmsrouting12Package() {
		return (Jmsrouting12Package)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static Jmsrouting12Package getPackage() {
		return Jmsrouting12Package.eINSTANCE;
	}

} //Jmsrouting12FactoryImpl
