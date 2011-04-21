/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12;

import org.jboss.tools.smooks.model.common.AbstractAnyType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Message</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				The message configuration.
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getCorrelationIdPattern <em>Correlation Id Pattern</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getDeliveryMode <em>Delivery Mode</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getPriority <em>Priority</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getTimeToLive <em>Time To Live</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getMessage()
 * @model extendedMetaData="name='message' kind='elementOnly'"
 * @generated
 */
public interface Message extends AbstractAnyType {
	/**
	 * Returns the value of the '<em><b>Correlation Id Pattern</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						JMS Correlation pattern that will be used for the outgoing message.
	 * 						Templating support is available via the FreeMarker template engine.
	 * 			  		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Correlation Id Pattern</em>' attribute.
	 * @see #setCorrelationIdPattern(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getMessage_CorrelationIdPattern()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='correlationIdPattern' namespace='##targetNamespace'"
	 * @generated
	 */
	String getCorrelationIdPattern();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getCorrelationIdPattern <em>Correlation Id Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Correlation Id Pattern</em>' attribute.
	 * @see #getCorrelationIdPattern()
	 * @generated
	 */
	void setCorrelationIdPattern(String value);

	/**
	 * Returns the value of the '<em><b>Delivery Mode</b></em>' attribute.
	 * The default value is <code>"persistent"</code>.
	 * The literals are from the enumeration {@link org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The JMS DeliveryMode. 'persistent'(default) or 'non-persistent'.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Delivery Mode</em>' attribute.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode
	 * @see #isSetDeliveryMode()
	 * @see #unsetDeliveryMode()
	 * @see #setDeliveryMode(DeliveryMode)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getMessage_DeliveryMode()
	 * @model default="persistent" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='deliveryMode'"
	 * @generated
	 */
	DeliveryMode getDeliveryMode();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getDeliveryMode <em>Delivery Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delivery Mode</em>' attribute.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode
	 * @see #isSetDeliveryMode()
	 * @see #unsetDeliveryMode()
	 * @see #getDeliveryMode()
	 * @generated
	 */
	void setDeliveryMode(DeliveryMode value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getDeliveryMode <em>Delivery Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDeliveryMode()
	 * @see #getDeliveryMode()
	 * @see #setDeliveryMode(DeliveryMode)
	 * @generated
	 */
	void unsetDeliveryMode();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getDeliveryMode <em>Delivery Mode</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Delivery Mode</em>' attribute is set.
	 * @see #unsetDeliveryMode()
	 * @see #getDeliveryMode()
	 * @see #setDeliveryMode(DeliveryMode)
	 * @generated
	 */
	boolean isSetDeliveryMode();

	/**
	 * Returns the value of the '<em><b>Priority</b></em>' attribute.
	 * The default value is <code>"4"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The JMS Priority to be used
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Priority</em>' attribute.
	 * @see #isSetPriority()
	 * @see #unsetPriority()
	 * @see #setPriority(int)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getMessage_Priority()
	 * @model default="4" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='attribute' name='priority'"
	 * @generated
	 */
	int getPriority();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getPriority <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Priority</em>' attribute.
	 * @see #isSetPriority()
	 * @see #unsetPriority()
	 * @see #getPriority()
	 * @generated
	 */
	void setPriority(int value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getPriority <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPriority()
	 * @see #getPriority()
	 * @see #setPriority(int)
	 * @generated
	 */
	void unsetPriority();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getPriority <em>Priority</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Priority</em>' attribute is set.
	 * @see #unsetPriority()
	 * @see #getPriority()
	 * @see #setPriority(int)
	 * @generated
	 */
	boolean isSetPriority();

	/**
	 * Returns the value of the '<em><b>Time To Live</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The JMS Time-To-Live to be used.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Time To Live</em>' attribute.
	 * @see #isSetTimeToLive()
	 * @see #unsetTimeToLive()
	 * @see #setTimeToLive(long)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getMessage_TimeToLive()
	 * @model default="0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Long"
	 *        extendedMetaData="kind='attribute' name='timeToLive'"
	 * @generated
	 */
	long getTimeToLive();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getTimeToLive <em>Time To Live</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time To Live</em>' attribute.
	 * @see #isSetTimeToLive()
	 * @see #unsetTimeToLive()
	 * @see #getTimeToLive()
	 * @generated
	 */
	void setTimeToLive(long value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getTimeToLive <em>Time To Live</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTimeToLive()
	 * @see #getTimeToLive()
	 * @see #setTimeToLive(long)
	 * @generated
	 */
	void unsetTimeToLive();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getTimeToLive <em>Time To Live</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Time To Live</em>' attribute is set.
	 * @see #unsetTimeToLive()
	 * @see #getTimeToLive()
	 * @see #setTimeToLive(long)
	 * @generated
	 */
	boolean isSetTimeToLive();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The default value is <code>"TextMessage"</code>.
	 * The literals are from the enumeration {@link org.jboss.tools.smooks.model.jmsrouting12.MessageType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The type of JMS Message that should be sent. 'TextMessage'(default) or 'ObjectMessage'.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.MessageType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(MessageType)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getMessage_Type()
	 * @model default="TextMessage" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='type'"
	 * @generated
	 */
	MessageType getType();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.MessageType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(MessageType value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(MessageType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Message#getType <em>Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(MessageType)
	 * @generated
	 */
	boolean isSetType();

} // Message
