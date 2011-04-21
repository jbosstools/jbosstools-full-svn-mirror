/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.graphical.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.jboss.tools.smooks.graphical.GraphInformations;
import org.jboss.tools.smooks.graphical.GraphicalPackage;
import org.jboss.tools.smooks.graphical.MappingDataType;
import org.jboss.tools.smooks.graphical.Params;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Graph Informations</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.graphical.impl.GraphInformationsImpl#getMappingType <em>Mapping Type</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.graphical.impl.GraphInformationsImpl#getParams <em>Params</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GraphInformationsImpl extends EObjectImpl implements GraphInformations {
	/**
	 * The cached value of the '{@link #getMappingType() <em>Mapping Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMappingType()
	 * @generated
	 * @ordered
	 */
	protected MappingDataType mappingType;

	/**
	 * The cached value of the '{@link #getParams() <em>Params</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParams()
	 * @generated
	 * @ordered
	 */
	protected Params params;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GraphInformationsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GraphicalPackage.Literals.GRAPH_INFORMATIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingDataType getMappingType() {
		return mappingType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMappingType(MappingDataType newMappingType, NotificationChain msgs) {
		MappingDataType oldMappingType = mappingType;
		mappingType = newMappingType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GraphicalPackage.GRAPH_INFORMATIONS__MAPPING_TYPE, oldMappingType, newMappingType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMappingType(MappingDataType newMappingType) {
		if (newMappingType != mappingType) {
			NotificationChain msgs = null;
			if (mappingType != null)
				msgs = ((InternalEObject)mappingType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GraphicalPackage.GRAPH_INFORMATIONS__MAPPING_TYPE, null, msgs);
			if (newMappingType != null)
				msgs = ((InternalEObject)newMappingType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GraphicalPackage.GRAPH_INFORMATIONS__MAPPING_TYPE, null, msgs);
			msgs = basicSetMappingType(newMappingType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphicalPackage.GRAPH_INFORMATIONS__MAPPING_TYPE, newMappingType, newMappingType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Params getParams() {
		return params;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParams(Params newParams, NotificationChain msgs) {
		Params oldParams = params;
		params = newParams;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GraphicalPackage.GRAPH_INFORMATIONS__PARAMS, oldParams, newParams);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParams(Params newParams) {
		if (newParams != params) {
			NotificationChain msgs = null;
			if (params != null)
				msgs = ((InternalEObject)params).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GraphicalPackage.GRAPH_INFORMATIONS__PARAMS, null, msgs);
			if (newParams != null)
				msgs = ((InternalEObject)newParams).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GraphicalPackage.GRAPH_INFORMATIONS__PARAMS, null, msgs);
			msgs = basicSetParams(newParams, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphicalPackage.GRAPH_INFORMATIONS__PARAMS, newParams, newParams));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GraphicalPackage.GRAPH_INFORMATIONS__MAPPING_TYPE:
				return basicSetMappingType(null, msgs);
			case GraphicalPackage.GRAPH_INFORMATIONS__PARAMS:
				return basicSetParams(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GraphicalPackage.GRAPH_INFORMATIONS__MAPPING_TYPE:
				return getMappingType();
			case GraphicalPackage.GRAPH_INFORMATIONS__PARAMS:
				return getParams();
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
			case GraphicalPackage.GRAPH_INFORMATIONS__MAPPING_TYPE:
				setMappingType((MappingDataType)newValue);
				return;
			case GraphicalPackage.GRAPH_INFORMATIONS__PARAMS:
				setParams((Params)newValue);
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
			case GraphicalPackage.GRAPH_INFORMATIONS__MAPPING_TYPE:
				setMappingType((MappingDataType)null);
				return;
			case GraphicalPackage.GRAPH_INFORMATIONS__PARAMS:
				setParams((Params)null);
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
			case GraphicalPackage.GRAPH_INFORMATIONS__MAPPING_TYPE:
				return mappingType != null;
			case GraphicalPackage.GRAPH_INFORMATIONS__PARAMS:
				return params != null;
		}
		return super.eIsSet(featureID);
	}

} //GraphInformationsImpl
