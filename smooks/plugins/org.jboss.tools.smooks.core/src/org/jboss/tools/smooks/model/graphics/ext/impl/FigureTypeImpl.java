/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Figure Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl#getHeight <em>Height</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl#getParentFigureId <em>Parent Figure Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl#getWidth <em>Width</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl#getX <em>X</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl#getY <em>Y</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FigureTypeImpl extends EObjectImpl implements FigureType {
	/**
	 * The default value of the '{@link #getHeight() <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeight()
	 * @generated
	 * @ordered
	 */
	protected static final String HEIGHT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHeight() <em>Height</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeight()
	 * @generated
	 * @ordered
	 */
	protected String height = HEIGHT_EDEFAULT;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getParentFigureId() <em>Parent Figure Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentFigureId()
	 * @generated
	 * @ordered
	 */
	protected static final String PARENT_FIGURE_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getParentFigureId() <em>Parent Figure Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentFigureId()
	 * @generated
	 * @ordered
	 */
	protected String parentFigureId = PARENT_FIGURE_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
	protected static final String WIDTH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWidth() <em>Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWidth()
	 * @generated
	 * @ordered
	 */
	protected String width = WIDTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getX() <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX()
	 * @generated
	 * @ordered
	 */
	protected static final String X_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getX() <em>X</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getX()
	 * @generated
	 * @ordered
	 */
	protected String x = X_EDEFAULT;

	/**
	 * The default value of the '{@link #getY() <em>Y</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getY()
	 * @generated
	 * @ordered
	 */
	protected static final String Y_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getY() <em>Y</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getY()
	 * @generated
	 * @ordered
	 */
	protected String y = Y_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FigureTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GraphPackage.Literals.FIGURE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHeight() {
		return height;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeight(String newHeight) {
		String oldHeight = height;
		height = newHeight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.FIGURE_TYPE__HEIGHT, oldHeight, height));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.FIGURE_TYPE__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getParentFigureId() {
		return parentFigureId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentFigureId(String newParentFigureId) {
		String oldParentFigureId = parentFigureId;
		parentFigureId = newParentFigureId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.FIGURE_TYPE__PARENT_FIGURE_ID, oldParentFigureId, parentFigureId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWidth(String newWidth) {
		String oldWidth = width;
		width = newWidth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.FIGURE_TYPE__WIDTH, oldWidth, width));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getX() {
		return x;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setX(String newX) {
		String oldX = x;
		x = newX;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.FIGURE_TYPE__X, oldX, x));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getY() {
		return y;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setY(String newY) {
		String oldY = y;
		y = newY;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.FIGURE_TYPE__Y, oldY, y));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GraphPackage.FIGURE_TYPE__HEIGHT:
				return getHeight();
			case GraphPackage.FIGURE_TYPE__ID:
				return getId();
			case GraphPackage.FIGURE_TYPE__PARENT_FIGURE_ID:
				return getParentFigureId();
			case GraphPackage.FIGURE_TYPE__WIDTH:
				return getWidth();
			case GraphPackage.FIGURE_TYPE__X:
				return getX();
			case GraphPackage.FIGURE_TYPE__Y:
				return getY();
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
			case GraphPackage.FIGURE_TYPE__HEIGHT:
				setHeight((String)newValue);
				return;
			case GraphPackage.FIGURE_TYPE__ID:
				setId((String)newValue);
				return;
			case GraphPackage.FIGURE_TYPE__PARENT_FIGURE_ID:
				setParentFigureId((String)newValue);
				return;
			case GraphPackage.FIGURE_TYPE__WIDTH:
				setWidth((String)newValue);
				return;
			case GraphPackage.FIGURE_TYPE__X:
				setX((String)newValue);
				return;
			case GraphPackage.FIGURE_TYPE__Y:
				setY((String)newValue);
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
			case GraphPackage.FIGURE_TYPE__HEIGHT:
				setHeight(HEIGHT_EDEFAULT);
				return;
			case GraphPackage.FIGURE_TYPE__ID:
				setId(ID_EDEFAULT);
				return;
			case GraphPackage.FIGURE_TYPE__PARENT_FIGURE_ID:
				setParentFigureId(PARENT_FIGURE_ID_EDEFAULT);
				return;
			case GraphPackage.FIGURE_TYPE__WIDTH:
				setWidth(WIDTH_EDEFAULT);
				return;
			case GraphPackage.FIGURE_TYPE__X:
				setX(X_EDEFAULT);
				return;
			case GraphPackage.FIGURE_TYPE__Y:
				setY(Y_EDEFAULT);
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
			case GraphPackage.FIGURE_TYPE__HEIGHT:
				return HEIGHT_EDEFAULT == null ? height != null : !HEIGHT_EDEFAULT.equals(height);
			case GraphPackage.FIGURE_TYPE__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case GraphPackage.FIGURE_TYPE__PARENT_FIGURE_ID:
				return PARENT_FIGURE_ID_EDEFAULT == null ? parentFigureId != null : !PARENT_FIGURE_ID_EDEFAULT.equals(parentFigureId);
			case GraphPackage.FIGURE_TYPE__WIDTH:
				return WIDTH_EDEFAULT == null ? width != null : !WIDTH_EDEFAULT.equals(width);
			case GraphPackage.FIGURE_TYPE__X:
				return X_EDEFAULT == null ? x != null : !X_EDEFAULT.equals(x);
			case GraphPackage.FIGURE_TYPE__Y:
				return Y_EDEFAULT == null ? y != null : !Y_EDEFAULT.equals(y);
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
		result.append(" (height: ");
		result.append(height);
		result.append(", id: ");
		result.append(id);
		result.append(", parentFigureId: ");
		result.append(parentFigureId);
		result.append(", width: ");
		result.append(width);
		result.append(", x: ");
		result.append(x);
		result.append(", y: ");
		result.append(y);
		result.append(')');
		return result.toString();
	}

} //FigureTypeImpl
