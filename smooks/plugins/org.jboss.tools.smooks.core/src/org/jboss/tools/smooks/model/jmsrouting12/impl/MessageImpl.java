/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12.impl;



import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.common.impl.AbstractAnyTypeImpl;
import org.jboss.tools.smooks.model.jmsrouting12.DeliveryMode;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package;
import org.jboss.tools.smooks.model.jmsrouting12.Message;
import org.jboss.tools.smooks.model.jmsrouting12.MessageType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Message</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.MessageImpl#getCorrelationIdPattern <em>Correlation Id Pattern</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.MessageImpl#getDeliveryMode <em>Delivery Mode</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.MessageImpl#getPriority <em>Priority</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.MessageImpl#getTimeToLive <em>Time To Live</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.MessageImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MessageImpl extends AbstractAnyTypeImpl implements Message {
	/**
	 * The default value of the '{@link #getCorrelationIdPattern() <em>Correlation Id Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCorrelationIdPattern()
	 * @generated
	 * @ordered
	 */
	protected static final String CORRELATION_ID_PATTERN_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCorrelationIdPattern() <em>Correlation Id Pattern</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCorrelationIdPattern()
	 * @generated
	 * @ordered
	 */
	protected String correlationIdPattern = CORRELATION_ID_PATTERN_EDEFAULT;

	/**
	 * The default value of the '{@link #getDeliveryMode() <em>Delivery Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeliveryMode()
	 * @generated
	 * @ordered
	 */
	protected static final DeliveryMode DELIVERY_MODE_EDEFAULT = DeliveryMode.PERSISTENT_LITERAL;

	/**
	 * The cached value of the '{@link #getDeliveryMode() <em>Delivery Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeliveryMode()
	 * @generated
	 * @ordered
	 */
	protected DeliveryMode deliveryMode = DELIVERY_MODE_EDEFAULT;

	/**
	 * This is true if the Delivery Mode attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean deliveryModeESet;

	/**
	 * The default value of the '{@link #getPriority() <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
	protected static final int PRIORITY_EDEFAULT = 4;

	/**
	 * The cached value of the '{@link #getPriority() <em>Priority</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPriority()
	 * @generated
	 * @ordered
	 */
	protected int priority = PRIORITY_EDEFAULT;

	/**
	 * This is true if the Priority attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean priorityESet;

	/**
	 * The default value of the '{@link #getTimeToLive() <em>Time To Live</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeToLive()
	 * @generated
	 * @ordered
	 */
	protected static final long TIME_TO_LIVE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getTimeToLive() <em>Time To Live</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeToLive()
	 * @generated
	 * @ordered
	 */
	protected long timeToLive = TIME_TO_LIVE_EDEFAULT;

	/**
	 * This is true if the Time To Live attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean timeToLiveESet;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final MessageType TYPE_EDEFAULT = MessageType.TEXT_MESSAGE_LITERAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected MessageType type = TYPE_EDEFAULT;

	/**
	 * This is true if the Type attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean typeESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MessageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Jmsrouting12Package.Literals.MESSAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCorrelationIdPattern() {
		return correlationIdPattern;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCorrelationIdPattern(String newCorrelationIdPattern) {
		String oldCorrelationIdPattern = correlationIdPattern;
		correlationIdPattern = newCorrelationIdPattern;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.MESSAGE__CORRELATION_ID_PATTERN, oldCorrelationIdPattern, correlationIdPattern));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeliveryMode getDeliveryMode() {
		return deliveryMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeliveryMode(DeliveryMode newDeliveryMode) {
		DeliveryMode oldDeliveryMode = deliveryMode;
		deliveryMode = newDeliveryMode == null ? DELIVERY_MODE_EDEFAULT : newDeliveryMode;
		boolean oldDeliveryModeESet = deliveryModeESet;
		deliveryModeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.MESSAGE__DELIVERY_MODE, oldDeliveryMode, deliveryMode, !oldDeliveryModeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDeliveryMode() {
		DeliveryMode oldDeliveryMode = deliveryMode;
		boolean oldDeliveryModeESet = deliveryModeESet;
		deliveryMode = DELIVERY_MODE_EDEFAULT;
		deliveryModeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Jmsrouting12Package.MESSAGE__DELIVERY_MODE, oldDeliveryMode, DELIVERY_MODE_EDEFAULT, oldDeliveryModeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDeliveryMode() {
		return deliveryModeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPriority(int newPriority) {
		int oldPriority = priority;
		priority = newPriority;
		boolean oldPriorityESet = priorityESet;
		priorityESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.MESSAGE__PRIORITY, oldPriority, priority, !oldPriorityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetPriority() {
		int oldPriority = priority;
		boolean oldPriorityESet = priorityESet;
		priority = PRIORITY_EDEFAULT;
		priorityESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Jmsrouting12Package.MESSAGE__PRIORITY, oldPriority, PRIORITY_EDEFAULT, oldPriorityESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetPriority() {
		return priorityESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getTimeToLive() {
		return timeToLive;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimeToLive(long newTimeToLive) {
		long oldTimeToLive = timeToLive;
		timeToLive = newTimeToLive;
		boolean oldTimeToLiveESet = timeToLiveESet;
		timeToLiveESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.MESSAGE__TIME_TO_LIVE, oldTimeToLive, timeToLive, !oldTimeToLiveESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetTimeToLive() {
		long oldTimeToLive = timeToLive;
		boolean oldTimeToLiveESet = timeToLiveESet;
		timeToLive = TIME_TO_LIVE_EDEFAULT;
		timeToLiveESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Jmsrouting12Package.MESSAGE__TIME_TO_LIVE, oldTimeToLive, TIME_TO_LIVE_EDEFAULT, oldTimeToLiveESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetTimeToLive() {
		return timeToLiveESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(MessageType newType) {
		MessageType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.MESSAGE__TYPE, oldType, type, !oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetType() {
		MessageType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Jmsrouting12Package.MESSAGE__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetType() {
		return typeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Jmsrouting12Package.MESSAGE__CORRELATION_ID_PATTERN:
				return getCorrelationIdPattern();
			case Jmsrouting12Package.MESSAGE__DELIVERY_MODE:
				return getDeliveryMode();
			case Jmsrouting12Package.MESSAGE__PRIORITY:
				return new Integer(getPriority());
			case Jmsrouting12Package.MESSAGE__TIME_TO_LIVE:
				return new Long(getTimeToLive());
			case Jmsrouting12Package.MESSAGE__TYPE:
				return getType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Jmsrouting12Package.MESSAGE__CORRELATION_ID_PATTERN:
				setCorrelationIdPattern((String)newValue);
				return;
			case Jmsrouting12Package.MESSAGE__DELIVERY_MODE:
				setDeliveryMode((DeliveryMode)newValue);
				return;
			case Jmsrouting12Package.MESSAGE__PRIORITY:
				setPriority(((Integer)newValue).intValue());
				return;
			case Jmsrouting12Package.MESSAGE__TIME_TO_LIVE:
				setTimeToLive(((Long)newValue).longValue());
				return;
			case Jmsrouting12Package.MESSAGE__TYPE:
				setType((MessageType)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case Jmsrouting12Package.MESSAGE__CORRELATION_ID_PATTERN:
				setCorrelationIdPattern(CORRELATION_ID_PATTERN_EDEFAULT);
				return;
			case Jmsrouting12Package.MESSAGE__DELIVERY_MODE:
				unsetDeliveryMode();
				return;
			case Jmsrouting12Package.MESSAGE__PRIORITY:
				unsetPriority();
				return;
			case Jmsrouting12Package.MESSAGE__TIME_TO_LIVE:
				unsetTimeToLive();
				return;
			case Jmsrouting12Package.MESSAGE__TYPE:
				unsetType();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Jmsrouting12Package.MESSAGE__CORRELATION_ID_PATTERN:
				return CORRELATION_ID_PATTERN_EDEFAULT == null ? correlationIdPattern != null : !CORRELATION_ID_PATTERN_EDEFAULT.equals(correlationIdPattern);
			case Jmsrouting12Package.MESSAGE__DELIVERY_MODE:
				return isSetDeliveryMode();
			case Jmsrouting12Package.MESSAGE__PRIORITY:
				return isSetPriority();
			case Jmsrouting12Package.MESSAGE__TIME_TO_LIVE:
				return isSetTimeToLive();
			case Jmsrouting12Package.MESSAGE__TYPE:
				return isSetType();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (correlationIdPattern: ");
		result.append(correlationIdPattern);
		result.append(", deliveryMode: ");
		if (deliveryModeESet) result.append(deliveryMode); else result.append("<unset>");
		result.append(", priority: ");
		if (priorityESet) result.append(priority); else result.append("<unset>");
		result.append(", timeToLive: ");
		if (timeToLiveESet) result.append(timeToLive); else result.append("<unset>");
		result.append(", type: ");
		if (typeESet) result.append(type); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //MessageImpl
