/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.impl;


import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.jboss.tools.smooks.model.common.impl.AbstractAnyTypeImpl;
import org.jboss.tools.smooks.model.persistence12.ParameterType;
import org.jboss.tools.smooks.model.persistence12.Parameters;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameters</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ParametersImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ParametersImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ParametersImpl#getWiring <em>Wiring</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ParametersImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ParametersImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ParametersImpl extends AbstractAnyTypeImpl implements Parameters {
	/**
	 * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap group;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final ParameterType TYPE_EDEFAULT = ParameterType.NAMED_LITERAL;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected ParameterType type = TYPE_EDEFAULT;

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
	protected ParametersImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.PARAMETERS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getGroup() {
		if (group == null) {
			group = new BasicFeatureMap(this, Persistence12Package.PARAMETERS__GROUP);
		}
		return group;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getValue() {
		return getGroup().list(Persistence12Package.Literals.PARAMETERS__VALUE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getWiring() {
		return getGroup().list(Persistence12Package.Literals.PARAMETERS__WIRING);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList getExpression() {
		return getGroup().list(Persistence12Package.Literals.PARAMETERS__EXPRESSION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterType getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(ParameterType newType) {
		ParameterType oldType = type;
		type = newType == null ? TYPE_EDEFAULT : newType;
		boolean oldTypeESet = typeESet;
		typeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.PARAMETERS__TYPE, oldType, type, !oldTypeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetType() {
		ParameterType oldType = type;
		boolean oldTypeESet = typeESet;
		type = TYPE_EDEFAULT;
		typeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Persistence12Package.PARAMETERS__TYPE, oldType, TYPE_EDEFAULT, oldTypeESet));
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Persistence12Package.PARAMETERS__GROUP:
				return ((InternalEList)getGroup()).basicRemove(otherEnd, msgs);
			case Persistence12Package.PARAMETERS__VALUE:
				return ((InternalEList)getValue()).basicRemove(otherEnd, msgs);
			case Persistence12Package.PARAMETERS__WIRING:
				return ((InternalEList)getWiring()).basicRemove(otherEnd, msgs);
			case Persistence12Package.PARAMETERS__EXPRESSION:
				return ((InternalEList)getExpression()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Persistence12Package.PARAMETERS__GROUP:
				if (coreType) return getGroup();
				return ((FeatureMap.Internal)getGroup()).getWrapper();
			case Persistence12Package.PARAMETERS__VALUE:
				return getValue();
			case Persistence12Package.PARAMETERS__WIRING:
				return getWiring();
			case Persistence12Package.PARAMETERS__EXPRESSION:
				return getExpression();
			case Persistence12Package.PARAMETERS__TYPE:
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
			case Persistence12Package.PARAMETERS__GROUP:
				((FeatureMap.Internal)getGroup()).set(newValue);
				return;
			case Persistence12Package.PARAMETERS__VALUE:
				getValue().clear();
				getValue().addAll((Collection)newValue);
				return;
			case Persistence12Package.PARAMETERS__WIRING:
				getWiring().clear();
				getWiring().addAll((Collection)newValue);
				return;
			case Persistence12Package.PARAMETERS__EXPRESSION:
				getExpression().clear();
				getExpression().addAll((Collection)newValue);
				return;
			case Persistence12Package.PARAMETERS__TYPE:
				setType((ParameterType)newValue);
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
			case Persistence12Package.PARAMETERS__GROUP:
				getGroup().clear();
				return;
			case Persistence12Package.PARAMETERS__VALUE:
				getValue().clear();
				return;
			case Persistence12Package.PARAMETERS__WIRING:
				getWiring().clear();
				return;
			case Persistence12Package.PARAMETERS__EXPRESSION:
				getExpression().clear();
				return;
			case Persistence12Package.PARAMETERS__TYPE:
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
			case Persistence12Package.PARAMETERS__GROUP:
				return group != null && !group.isEmpty();
			case Persistence12Package.PARAMETERS__VALUE:
				return !getValue().isEmpty();
			case Persistence12Package.PARAMETERS__WIRING:
				return !getWiring().isEmpty();
			case Persistence12Package.PARAMETERS__EXPRESSION:
				return !getExpression().isEmpty();
			case Persistence12Package.PARAMETERS__TYPE:
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
		result.append(" (group: ");
		result.append(group);
		result.append(", type: ");
		if (typeESet) result.append(type); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ParametersImpl
