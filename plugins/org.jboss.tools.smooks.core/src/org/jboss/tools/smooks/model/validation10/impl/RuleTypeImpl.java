/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.validation10.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;
import org.jboss.tools.smooks.model.validation10.OnFail;
import org.jboss.tools.smooks.model.validation10.RuleType;
import org.jboss.tools.smooks.model.validation10.Validation10Package;



/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rule Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.validation10.impl.RuleTypeImpl#getExecuteOn <em>Execute On</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.validation10.impl.RuleTypeImpl#getExecuteOnNS <em>Execute On NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.validation10.impl.RuleTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.validation10.impl.RuleTypeImpl#getOnFail <em>On Fail</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RuleTypeImpl extends ElementVisitorImpl implements RuleType {
	/**
	 * The default value of the '{@link #getExecuteOn() <em>Execute On</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOn()
	 * @generated
	 * @ordered
	 */
	protected static final String EXECUTE_ON_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecuteOn() <em>Execute On</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOn()
	 * @generated
	 * @ordered
	 */
	protected String executeOn = EXECUTE_ON_EDEFAULT;

	/**
	 * The default value of the '{@link #getExecuteOnNS() <em>Execute On NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnNS()
	 * @generated
	 * @ordered
	 */
	protected static final String EXECUTE_ON_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecuteOnNS() <em>Execute On NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnNS()
	 * @generated
	 * @ordered
	 */
	protected String executeOnNS = EXECUTE_ON_NS_EDEFAULT;

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
	 * The default value of the '{@link #getOnFail() <em>On Fail</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOnFail()
	 * @generated
	 * @ordered
	 */
	protected static final OnFail ON_FAIL_EDEFAULT = OnFail.OK_LITERAL;

	/**
	 * The cached value of the '{@link #getOnFail() <em>On Fail</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOnFail()
	 * @generated
	 * @ordered
	 */
	protected OnFail onFail = ON_FAIL_EDEFAULT;

	/**
	 * This is true if the On Fail attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean onFailESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RuleTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Validation10Package.Literals.RULE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExecuteOn() {
		return executeOn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteOn(String newExecuteOn) {
		String oldExecuteOn = executeOn;
		executeOn = newExecuteOn;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Validation10Package.RULE_TYPE__EXECUTE_ON, oldExecuteOn, executeOn));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExecuteOnNS() {
		return executeOnNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteOnNS(String newExecuteOnNS) {
		String oldExecuteOnNS = executeOnNS;
		executeOnNS = newExecuteOnNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Validation10Package.RULE_TYPE__EXECUTE_ON_NS, oldExecuteOnNS, executeOnNS));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Validation10Package.RULE_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OnFail getOnFail() {
		return onFail;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOnFail(OnFail newOnFail) {
		OnFail oldOnFail = onFail;
		onFail = newOnFail == null ? ON_FAIL_EDEFAULT : newOnFail;
		boolean oldOnFailESet = onFailESet;
		onFailESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Validation10Package.RULE_TYPE__ON_FAIL, oldOnFail, onFail, !oldOnFailESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetOnFail() {
		OnFail oldOnFail = onFail;
		boolean oldOnFailESet = onFailESet;
		onFail = ON_FAIL_EDEFAULT;
		onFailESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Validation10Package.RULE_TYPE__ON_FAIL, oldOnFail, ON_FAIL_EDEFAULT, oldOnFailESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetOnFail() {
		return onFailESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Validation10Package.RULE_TYPE__EXECUTE_ON:
				return getExecuteOn();
			case Validation10Package.RULE_TYPE__EXECUTE_ON_NS:
				return getExecuteOnNS();
			case Validation10Package.RULE_TYPE__NAME:
				return getName();
			case Validation10Package.RULE_TYPE__ON_FAIL:
				return getOnFail();
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
			case Validation10Package.RULE_TYPE__EXECUTE_ON:
				setExecuteOn((String)newValue);
				return;
			case Validation10Package.RULE_TYPE__EXECUTE_ON_NS:
				setExecuteOnNS((String)newValue);
				return;
			case Validation10Package.RULE_TYPE__NAME:
				setName((String)newValue);
				return;
			case Validation10Package.RULE_TYPE__ON_FAIL:
				setOnFail((OnFail)newValue);
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
			case Validation10Package.RULE_TYPE__EXECUTE_ON:
				setExecuteOn(EXECUTE_ON_EDEFAULT);
				return;
			case Validation10Package.RULE_TYPE__EXECUTE_ON_NS:
				setExecuteOnNS(EXECUTE_ON_NS_EDEFAULT);
				return;
			case Validation10Package.RULE_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case Validation10Package.RULE_TYPE__ON_FAIL:
				unsetOnFail();
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
			case Validation10Package.RULE_TYPE__EXECUTE_ON:
				return EXECUTE_ON_EDEFAULT == null ? executeOn != null : !EXECUTE_ON_EDEFAULT.equals(executeOn);
			case Validation10Package.RULE_TYPE__EXECUTE_ON_NS:
				return EXECUTE_ON_NS_EDEFAULT == null ? executeOnNS != null : !EXECUTE_ON_NS_EDEFAULT.equals(executeOnNS);
			case Validation10Package.RULE_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case Validation10Package.RULE_TYPE__ON_FAIL:
				return isSetOnFail();
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
		result.append(" (executeOn: ");
		result.append(executeOn);
		result.append(", executeOnNS: ");
		result.append(executeOnNS);
		result.append(", name: ");
		result.append(name);
		result.append(", onFail: ");
		if (onFailESet) result.append(onFail); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //RuleTypeImpl
