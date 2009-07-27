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
import org.jboss.tools.smooks.model.common.impl.AbstractAnyTypeImpl;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.persistence12.WiringParameter;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Wiring Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.WiringParameterImpl#getBeanIdRef <em>Bean Id Ref</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.WiringParameterImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.WiringParameterImpl#getWireOnElement <em>Wire On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.WiringParameterImpl#getWireOnElementNS <em>Wire On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WiringParameterImpl extends AbstractAnyTypeImpl implements WiringParameter {
	/**
	 * The default value of the '{@link #getBeanIdRef() <em>Bean Id Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanIdRef()
	 * @generated
	 * @ordered
	 */
	protected static final String BEAN_ID_REF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeanIdRef() <em>Bean Id Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanIdRef()
	 * @generated
	 * @ordered
	 */
	protected String beanIdRef = BEAN_ID_REF_EDEFAULT;

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
	 * The default value of the '{@link #getWireOnElement() <em>Wire On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWireOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String WIRE_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWireOnElement() <em>Wire On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWireOnElement()
	 * @generated
	 * @ordered
	 */
	protected String wireOnElement = WIRE_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getWireOnElementNS() <em>Wire On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWireOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String WIRE_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWireOnElementNS() <em>Wire On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWireOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String wireOnElementNS = WIRE_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WiringParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.WIRING_PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeanIdRef() {
		return beanIdRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanIdRef(String newBeanIdRef) {
		String oldBeanIdRef = beanIdRef;
		beanIdRef = newBeanIdRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.WIRING_PARAMETER__BEAN_ID_REF, oldBeanIdRef, beanIdRef));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.WIRING_PARAMETER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWireOnElement() {
		return wireOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWireOnElement(String newWireOnElement) {
		String oldWireOnElement = wireOnElement;
		wireOnElement = newWireOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT, oldWireOnElement, wireOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWireOnElementNS() {
		return wireOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWireOnElementNS(String newWireOnElementNS) {
		String oldWireOnElementNS = wireOnElementNS;
		wireOnElementNS = newWireOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT_NS, oldWireOnElementNS, wireOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Persistence12Package.WIRING_PARAMETER__BEAN_ID_REF:
				return getBeanIdRef();
			case Persistence12Package.WIRING_PARAMETER__NAME:
				return getName();
			case Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT:
				return getWireOnElement();
			case Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT_NS:
				return getWireOnElementNS();
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
			case Persistence12Package.WIRING_PARAMETER__BEAN_ID_REF:
				setBeanIdRef((String)newValue);
				return;
			case Persistence12Package.WIRING_PARAMETER__NAME:
				setName((String)newValue);
				return;
			case Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT:
				setWireOnElement((String)newValue);
				return;
			case Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT_NS:
				setWireOnElementNS((String)newValue);
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
			case Persistence12Package.WIRING_PARAMETER__BEAN_ID_REF:
				setBeanIdRef(BEAN_ID_REF_EDEFAULT);
				return;
			case Persistence12Package.WIRING_PARAMETER__NAME:
				setName(NAME_EDEFAULT);
				return;
			case Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT:
				setWireOnElement(WIRE_ON_ELEMENT_EDEFAULT);
				return;
			case Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT_NS:
				setWireOnElementNS(WIRE_ON_ELEMENT_NS_EDEFAULT);
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
			case Persistence12Package.WIRING_PARAMETER__BEAN_ID_REF:
				return BEAN_ID_REF_EDEFAULT == null ? beanIdRef != null : !BEAN_ID_REF_EDEFAULT.equals(beanIdRef);
			case Persistence12Package.WIRING_PARAMETER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT:
				return WIRE_ON_ELEMENT_EDEFAULT == null ? wireOnElement != null : !WIRE_ON_ELEMENT_EDEFAULT.equals(wireOnElement);
			case Persistence12Package.WIRING_PARAMETER__WIRE_ON_ELEMENT_NS:
				return WIRE_ON_ELEMENT_NS_EDEFAULT == null ? wireOnElementNS != null : !WIRE_ON_ELEMENT_NS_EDEFAULT.equals(wireOnElementNS);
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
		result.append(" (beanIdRef: ");
		result.append(beanIdRef);
		result.append(", name: ");
		result.append(name);
		result.append(", wireOnElement: ");
		result.append(wireOnElement);
		result.append(", wireOnElementNS: ");
		result.append(wireOnElementNS);
		result.append(')');
		return result.toString();
	}

} //WiringParameterImpl
