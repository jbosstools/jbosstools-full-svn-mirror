/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting.impl;


import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.jboss.tools.smooks.model.jmsrouting.AcknowledgeMode;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting.Session;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Session</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.impl.SessionImpl#getAcknowledgeMode <em>Acknowledge Mode</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.impl.SessionImpl#isTransacted <em>Transacted</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SessionImpl extends EObjectImpl implements Session {
	/**
	 * The default value of the '{@link #getAcknowledgeMode() <em>Acknowledge Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAcknowledgeMode()
	 * @generated not
	 * @ordered
	 */
	protected static final AcknowledgeMode ACKNOWLEDGE_MODE_EDEFAULT = null;// AcknowledgeMode.AUTOACKNOWLEDGE;

	/**
	 * The cached value of the '{@link #getAcknowledgeMode() <em>Acknowledge Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAcknowledgeMode()
	 * @generated
	 * @ordered
	 */
	protected AcknowledgeMode acknowledgeMode = ACKNOWLEDGE_MODE_EDEFAULT;

	/**
	 * This is true if the Acknowledge Mode attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean acknowledgeModeESet;

	/**
	 * The default value of the '{@link #isTransacted() <em>Transacted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTransacted()
	 * @generated
	 * @ordered
	 */
	protected static final boolean TRANSACTED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isTransacted() <em>Transacted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isTransacted()
	 * @generated
	 * @ordered
	 */
	protected boolean transacted = TRANSACTED_EDEFAULT;

	/**
	 * This is true if the Transacted attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean transactedESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SessionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return JmsroutingPackage.Literals.SESSION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AcknowledgeMode getAcknowledgeMode() {
		return acknowledgeMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAcknowledgeMode(AcknowledgeMode newAcknowledgeMode) {
		AcknowledgeMode oldAcknowledgeMode = acknowledgeMode;
		acknowledgeMode = newAcknowledgeMode == null ? ACKNOWLEDGE_MODE_EDEFAULT : newAcknowledgeMode;
		boolean oldAcknowledgeModeESet = acknowledgeModeESet;
		acknowledgeModeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JmsroutingPackage.SESSION__ACKNOWLEDGE_MODE, oldAcknowledgeMode, acknowledgeMode, !oldAcknowledgeModeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAcknowledgeMode() {
		AcknowledgeMode oldAcknowledgeMode = acknowledgeMode;
		boolean oldAcknowledgeModeESet = acknowledgeModeESet;
		acknowledgeMode = ACKNOWLEDGE_MODE_EDEFAULT;
		acknowledgeModeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JmsroutingPackage.SESSION__ACKNOWLEDGE_MODE, oldAcknowledgeMode, ACKNOWLEDGE_MODE_EDEFAULT, oldAcknowledgeModeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAcknowledgeMode() {
		return acknowledgeModeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isTransacted() {
		return transacted;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTransacted(boolean newTransacted) {
		boolean oldTransacted = transacted;
		transacted = newTransacted;
		boolean oldTransactedESet = transactedESet;
		transactedESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JmsroutingPackage.SESSION__TRANSACTED, oldTransacted, transacted, !oldTransactedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetTransacted() {
		boolean oldTransacted = transacted;
		boolean oldTransactedESet = transactedESet;
		transacted = TRANSACTED_EDEFAULT;
		transactedESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JmsroutingPackage.SESSION__TRANSACTED, oldTransacted, TRANSACTED_EDEFAULT, oldTransactedESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetTransacted() {
		return transactedESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JmsroutingPackage.SESSION__ACKNOWLEDGE_MODE:
				return getAcknowledgeMode();
			case JmsroutingPackage.SESSION__TRANSACTED:
				return isTransacted() ? Boolean.TRUE : Boolean.FALSE;
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case JmsroutingPackage.SESSION__ACKNOWLEDGE_MODE:
				setAcknowledgeMode((AcknowledgeMode)newValue);
				return;
			case JmsroutingPackage.SESSION__TRANSACTED:
				setTransacted(((Boolean)newValue).booleanValue());
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case JmsroutingPackage.SESSION__ACKNOWLEDGE_MODE:
				unsetAcknowledgeMode();
				return;
			case JmsroutingPackage.SESSION__TRANSACTED:
				unsetTransacted();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case JmsroutingPackage.SESSION__ACKNOWLEDGE_MODE:
				return isSetAcknowledgeMode();
			case JmsroutingPackage.SESSION__TRANSACTED:
				return isSetTransacted();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (acknowledgeMode: "); //$NON-NLS-1$
		if (acknowledgeModeESet) result.append(acknowledgeMode); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", transacted: "); //$NON-NLS-1$
		if (transactedESet) result.append(transacted); else result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

} //SessionImpl
