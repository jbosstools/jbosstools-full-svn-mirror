/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.dbrouting.impl;


import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.jboss.tools.smooks.model.dbrouting.DbroutingPackage;
import org.jboss.tools.smooks.model.dbrouting.ResultSet;
import org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Result Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetImpl#getScope <em>Scope</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetImpl#getTimeToLive <em>Time To Live</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResultSetImpl extends EObjectImpl implements ResultSet {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getScope() <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScope()
	 * @generated not
	 * @ordered
	 */
	protected static final ResultSetScopeType SCOPE_EDEFAULT = null;// ResultSetScopeType.EXECUTION;

	/**
	 * The cached value of the '{@link #getScope() <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScope()
	 * @generated
	 * @ordered
	 */
	protected ResultSetScopeType scope = SCOPE_EDEFAULT;

	/**
	 * This is true if the Scope attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean scopeESet;

	/**
	 * The default value of the '{@link #getTimeToLive() <em>Time To Live</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeToLive()
	 * @generated
	 * @ordered
	 */
	protected static final long TIME_TO_LIVE_EDEFAULT = 900000L;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResultSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DbroutingPackage.Literals.RESULT_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.RESULT_SET__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultSetScopeType getScope() {
		return scope;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScope(ResultSetScopeType newScope) {
		ResultSetScopeType oldScope = scope;
		scope = newScope == null ? SCOPE_EDEFAULT : newScope;
		boolean oldScopeESet = scopeESet;
		scopeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.RESULT_SET__SCOPE, oldScope, scope, !oldScopeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetScope() {
		ResultSetScopeType oldScope = scope;
		boolean oldScopeESet = scopeESet;
		scope = SCOPE_EDEFAULT;
		scopeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DbroutingPackage.RESULT_SET__SCOPE, oldScope, SCOPE_EDEFAULT, oldScopeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetScope() {
		return scopeESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.RESULT_SET__TIME_TO_LIVE, oldTimeToLive, timeToLive, !oldTimeToLiveESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, DbroutingPackage.RESULT_SET__TIME_TO_LIVE, oldTimeToLive, TIME_TO_LIVE_EDEFAULT, oldTimeToLiveESet));
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DbroutingPackage.RESULT_SET__NAME:
				return getName();
			case DbroutingPackage.RESULT_SET__SCOPE:
				return getScope();
			case DbroutingPackage.RESULT_SET__TIME_TO_LIVE:
				return new Long(getTimeToLive());
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
			case DbroutingPackage.RESULT_SET__NAME:
				setName((String)newValue);
				return;
			case DbroutingPackage.RESULT_SET__SCOPE:
				setScope((ResultSetScopeType)newValue);
				return;
			case DbroutingPackage.RESULT_SET__TIME_TO_LIVE:
				setTimeToLive(((Long)newValue).longValue());
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
			case DbroutingPackage.RESULT_SET__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DbroutingPackage.RESULT_SET__SCOPE:
				unsetScope();
				return;
			case DbroutingPackage.RESULT_SET__TIME_TO_LIVE:
				unsetTimeToLive();
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
			case DbroutingPackage.RESULT_SET__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DbroutingPackage.RESULT_SET__SCOPE:
				return isSetScope();
			case DbroutingPackage.RESULT_SET__TIME_TO_LIVE:
				return isSetTimeToLive();
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
		result.append(" (name: "); //$NON-NLS-1$
		result.append(name);
		result.append(", scope: "); //$NON-NLS-1$
		if (scopeESet) result.append(scope); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", timeToLive: "); //$NON-NLS-1$
		if (timeToLiveESet) result.append(timeToLive); else result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

} //ResultSetImpl
