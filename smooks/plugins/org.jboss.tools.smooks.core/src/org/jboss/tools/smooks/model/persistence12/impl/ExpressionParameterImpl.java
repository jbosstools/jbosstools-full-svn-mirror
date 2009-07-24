/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.jboss.tools.smooks.model.persistence12.ExpressionParameter;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Expression Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ExpressionParameterImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ExpressionParameterImpl#getExecOnElement <em>Exec On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ExpressionParameterImpl#getExecOnElementNS <em>Exec On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ExpressionParameterImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExpressionParameterImpl extends EObjectImpl implements ExpressionParameter {
	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getExecOnElement() <em>Exec On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String EXEC_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecOnElement() <em>Exec On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecOnElement()
	 * @generated
	 * @ordered
	 */
	protected String execOnElement = EXEC_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getExecOnElementNS() <em>Exec On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String EXEC_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecOnElementNS() <em>Exec On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String execOnElementNS = EXEC_ON_ELEMENT_NS_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExpressionParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.EXPRESSION_PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.EXPRESSION_PARAMETER__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExecOnElement() {
		return execOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecOnElement(String newExecOnElement) {
		String oldExecOnElement = execOnElement;
		execOnElement = newExecOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT, oldExecOnElement, execOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExecOnElementNS() {
		return execOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecOnElementNS(String newExecOnElementNS) {
		String oldExecOnElementNS = execOnElementNS;
		execOnElementNS = newExecOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT_NS, oldExecOnElementNS, execOnElementNS));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.EXPRESSION_PARAMETER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Persistence12Package.EXPRESSION_PARAMETER__VALUE:
				return getValue();
			case Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT:
				return getExecOnElement();
			case Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT_NS:
				return getExecOnElementNS();
			case Persistence12Package.EXPRESSION_PARAMETER__NAME:
				return getName();
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
			case Persistence12Package.EXPRESSION_PARAMETER__VALUE:
				setValue((String)newValue);
				return;
			case Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT:
				setExecOnElement((String)newValue);
				return;
			case Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT_NS:
				setExecOnElementNS((String)newValue);
				return;
			case Persistence12Package.EXPRESSION_PARAMETER__NAME:
				setName((String)newValue);
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
			case Persistence12Package.EXPRESSION_PARAMETER__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT:
				setExecOnElement(EXEC_ON_ELEMENT_EDEFAULT);
				return;
			case Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT_NS:
				setExecOnElementNS(EXEC_ON_ELEMENT_NS_EDEFAULT);
				return;
			case Persistence12Package.EXPRESSION_PARAMETER__NAME:
				setName(NAME_EDEFAULT);
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
			case Persistence12Package.EXPRESSION_PARAMETER__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
			case Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT:
				return EXEC_ON_ELEMENT_EDEFAULT == null ? execOnElement != null : !EXEC_ON_ELEMENT_EDEFAULT.equals(execOnElement);
			case Persistence12Package.EXPRESSION_PARAMETER__EXEC_ON_ELEMENT_NS:
				return EXEC_ON_ELEMENT_NS_EDEFAULT == null ? execOnElementNS != null : !EXEC_ON_ELEMENT_NS_EDEFAULT.equals(execOnElementNS);
			case Persistence12Package.EXPRESSION_PARAMETER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
		result.append(" (value: ");
		result.append(value);
		result.append(", execOnElement: ");
		result.append(execOnElement);
		result.append(", execOnElementNS: ");
		result.append(execOnElementNS);
		result.append(", name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //ExpressionParameterImpl
