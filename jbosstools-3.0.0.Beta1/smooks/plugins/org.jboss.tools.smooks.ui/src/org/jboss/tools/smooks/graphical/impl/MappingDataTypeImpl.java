/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.graphical.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.jboss.tools.smooks.graphical.GraphicalPackage;
import org.jboss.tools.smooks.graphical.MappingDataType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mapping Data Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.graphical.impl.MappingDataTypeImpl#getTargetTypeID <em>Target Type ID</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.graphical.impl.MappingDataTypeImpl#getSourceTypeID <em>Source Type ID</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MappingDataTypeImpl extends EObjectImpl implements MappingDataType {
	/**
	 * The default value of the '{@link #getTargetTypeID() <em>Target Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetTypeID()
	 * @generated
	 * @ordered
	 */
	protected static final String TARGET_TYPE_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTargetTypeID() <em>Target Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetTypeID()
	 * @generated
	 * @ordered
	 */
	protected String targetTypeID = TARGET_TYPE_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getSourceTypeID() <em>Source Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceTypeID()
	 * @generated
	 * @ordered
	 */
	protected static final String SOURCE_TYPE_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSourceTypeID() <em>Source Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceTypeID()
	 * @generated
	 * @ordered
	 */
	protected String sourceTypeID = SOURCE_TYPE_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MappingDataTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GraphicalPackage.Literals.MAPPING_DATA_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTargetTypeID() {
		return targetTypeID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetTypeID(String newTargetTypeID) {
		String oldTargetTypeID = targetTypeID;
		targetTypeID = newTargetTypeID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphicalPackage.MAPPING_DATA_TYPE__TARGET_TYPE_ID, oldTargetTypeID, targetTypeID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSourceTypeID() {
		return sourceTypeID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceTypeID(String newSourceTypeID) {
		String oldSourceTypeID = sourceTypeID;
		sourceTypeID = newSourceTypeID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphicalPackage.MAPPING_DATA_TYPE__SOURCE_TYPE_ID, oldSourceTypeID, sourceTypeID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GraphicalPackage.MAPPING_DATA_TYPE__TARGET_TYPE_ID:
				return getTargetTypeID();
			case GraphicalPackage.MAPPING_DATA_TYPE__SOURCE_TYPE_ID:
				return getSourceTypeID();
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
			case GraphicalPackage.MAPPING_DATA_TYPE__TARGET_TYPE_ID:
				setTargetTypeID((String)newValue);
				return;
			case GraphicalPackage.MAPPING_DATA_TYPE__SOURCE_TYPE_ID:
				setSourceTypeID((String)newValue);
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
			case GraphicalPackage.MAPPING_DATA_TYPE__TARGET_TYPE_ID:
				setTargetTypeID(TARGET_TYPE_ID_EDEFAULT);
				return;
			case GraphicalPackage.MAPPING_DATA_TYPE__SOURCE_TYPE_ID:
				setSourceTypeID(SOURCE_TYPE_ID_EDEFAULT);
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
			case GraphicalPackage.MAPPING_DATA_TYPE__TARGET_TYPE_ID:
				return TARGET_TYPE_ID_EDEFAULT == null ? targetTypeID != null : !TARGET_TYPE_ID_EDEFAULT.equals(targetTypeID);
			case GraphicalPackage.MAPPING_DATA_TYPE__SOURCE_TYPE_ID:
				return SOURCE_TYPE_ID_EDEFAULT == null ? sourceTypeID != null : !SOURCE_TYPE_ID_EDEFAULT.equals(sourceTypeID);
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
		result.append(" (targetTypeID: ");
		result.append(targetTypeID);
		result.append(", sourceTypeID: ");
		result.append(sourceTypeID);
		result.append(')');
		return result.toString();
	}

} //MappingDataTypeImpl
