/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext.impl;


import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.jboss.tools.smooks.model.graphics.ext.ConnectionType;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.GraphTypeImpl#getFigure <em>Figure</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.GraphTypeImpl#getConnection <em>Connection</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GraphTypeImpl extends EObjectImpl implements GraphType {
	/**
	 * The cached value of the '{@link #getFigure() <em>Figure</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFigure()
	 * @generated
	 * @ordered
	 */
	protected EList<FigureType> figure;

	/**
	 * The cached value of the '{@link #getConnection() <em>Connection</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnection()
	 * @generated
	 * @ordered
	 */
	protected EList<ConnectionType> connection;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GraphTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GraphPackage.Literals.GRAPH_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FigureType> getFigure() {
		if (figure == null) {
			figure = new EObjectContainmentEList<FigureType>(FigureType.class, this, GraphPackage.GRAPH_TYPE__FIGURE);
		}
		return figure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ConnectionType> getConnection() {
		if (connection == null) {
			connection = new EObjectContainmentEList<ConnectionType>(ConnectionType.class, this, GraphPackage.GRAPH_TYPE__CONNECTION);
		}
		return connection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GraphPackage.GRAPH_TYPE__FIGURE:
				return ((InternalEList<?>)getFigure()).basicRemove(otherEnd, msgs);
			case GraphPackage.GRAPH_TYPE__CONNECTION:
				return ((InternalEList<?>)getConnection()).basicRemove(otherEnd, msgs);
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
			case GraphPackage.GRAPH_TYPE__FIGURE:
				return getFigure();
			case GraphPackage.GRAPH_TYPE__CONNECTION:
				return getConnection();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GraphPackage.GRAPH_TYPE__FIGURE:
				getFigure().clear();
				getFigure().addAll((Collection<? extends FigureType>)newValue);
				return;
			case GraphPackage.GRAPH_TYPE__CONNECTION:
				getConnection().clear();
				getConnection().addAll((Collection<? extends ConnectionType>)newValue);
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
			case GraphPackage.GRAPH_TYPE__FIGURE:
				getFigure().clear();
				return;
			case GraphPackage.GRAPH_TYPE__CONNECTION:
				getConnection().clear();
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
			case GraphPackage.GRAPH_TYPE__FIGURE:
				return figure != null && !figure.isEmpty();
			case GraphPackage.GRAPH_TYPE__CONNECTION:
				return connection != null && !connection.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //GraphTypeImpl
