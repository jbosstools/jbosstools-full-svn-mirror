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
import org.jboss.tools.smooks.model.persistence12.Flusher;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;



/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Flusher</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.FlusherImpl#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.FlusherImpl#isFlushBefore <em>Flush Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.FlusherImpl#getFlushOnElement <em>Flush On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.FlusherImpl#getFlushOnElementNS <em>Flush On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FlusherImpl extends ElementVisitorImpl implements Flusher {
	/**
	 * The default value of the '{@link #getDao() <em>Dao</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDao()
	 * @generated
	 * @ordered
	 */
	protected static final String DAO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDao() <em>Dao</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDao()
	 * @generated
	 * @ordered
	 */
	protected String dao = DAO_EDEFAULT;

	/**
	 * The default value of the '{@link #isFlushBefore() <em>Flush Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFlushBefore()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FLUSH_BEFORE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFlushBefore() <em>Flush Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFlushBefore()
	 * @generated
	 * @ordered
	 */
	protected boolean flushBefore = FLUSH_BEFORE_EDEFAULT;

	/**
	 * This is true if the Flush Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean flushBeforeESet;

	/**
	 * The default value of the '{@link #getFlushOnElement() <em>Flush On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlushOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String FLUSH_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFlushOnElement() <em>Flush On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlushOnElement()
	 * @generated
	 * @ordered
	 */
	protected String flushOnElement = FLUSH_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getFlushOnElementNS() <em>Flush On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlushOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String FLUSH_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFlushOnElementNS() <em>Flush On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlushOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String flushOnElementNS = FLUSH_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FlusherImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.FLUSHER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDao() {
		return dao;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDao(String newDao) {
		String oldDao = dao;
		dao = newDao;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.FLUSHER__DAO, oldDao, dao));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFlushBefore() {
		return flushBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFlushBefore(boolean newFlushBefore) {
		boolean oldFlushBefore = flushBefore;
		flushBefore = newFlushBefore;
		boolean oldFlushBeforeESet = flushBeforeESet;
		flushBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.FLUSHER__FLUSH_BEFORE, oldFlushBefore, flushBefore, !oldFlushBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFlushBefore() {
		boolean oldFlushBefore = flushBefore;
		boolean oldFlushBeforeESet = flushBeforeESet;
		flushBefore = FLUSH_BEFORE_EDEFAULT;
		flushBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Persistence12Package.FLUSHER__FLUSH_BEFORE, oldFlushBefore, FLUSH_BEFORE_EDEFAULT, oldFlushBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFlushBefore() {
		return flushBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFlushOnElement() {
		return flushOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFlushOnElement(String newFlushOnElement) {
		String oldFlushOnElement = flushOnElement;
		flushOnElement = newFlushOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT, oldFlushOnElement, flushOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFlushOnElementNS() {
		return flushOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFlushOnElementNS(String newFlushOnElementNS) {
		String oldFlushOnElementNS = flushOnElementNS;
		flushOnElementNS = newFlushOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT_NS, oldFlushOnElementNS, flushOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Persistence12Package.FLUSHER__DAO:
				return getDao();
			case Persistence12Package.FLUSHER__FLUSH_BEFORE:
				return isFlushBefore() ? Boolean.TRUE : Boolean.FALSE;
			case Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT:
				return getFlushOnElement();
			case Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT_NS:
				return getFlushOnElementNS();
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
			case Persistence12Package.FLUSHER__DAO:
				setDao((String)newValue);
				return;
			case Persistence12Package.FLUSHER__FLUSH_BEFORE:
				setFlushBefore(((Boolean)newValue).booleanValue());
				return;
			case Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT:
				setFlushOnElement((String)newValue);
				return;
			case Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT_NS:
				setFlushOnElementNS((String)newValue);
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
			case Persistence12Package.FLUSHER__DAO:
				setDao(DAO_EDEFAULT);
				return;
			case Persistence12Package.FLUSHER__FLUSH_BEFORE:
				unsetFlushBefore();
				return;
			case Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT:
				setFlushOnElement(FLUSH_ON_ELEMENT_EDEFAULT);
				return;
			case Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT_NS:
				setFlushOnElementNS(FLUSH_ON_ELEMENT_NS_EDEFAULT);
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
			case Persistence12Package.FLUSHER__DAO:
				return DAO_EDEFAULT == null ? dao != null : !DAO_EDEFAULT.equals(dao);
			case Persistence12Package.FLUSHER__FLUSH_BEFORE:
				return isSetFlushBefore();
			case Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT:
				return FLUSH_ON_ELEMENT_EDEFAULT == null ? flushOnElement != null : !FLUSH_ON_ELEMENT_EDEFAULT.equals(flushOnElement);
			case Persistence12Package.FLUSHER__FLUSH_ON_ELEMENT_NS:
				return FLUSH_ON_ELEMENT_NS_EDEFAULT == null ? flushOnElementNS != null : !FLUSH_ON_ELEMENT_NS_EDEFAULT.equals(flushOnElementNS);
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
		result.append(" (dao: ");
		result.append(dao);
		result.append(", flushBefore: ");
		if (flushBeforeESet) result.append(flushBefore); else result.append("<unset>");
		result.append(", flushOnElement: ");
		result.append(flushOnElement);
		result.append(", flushOnElementNS: ");
		result.append(flushOnElementNS);
		result.append(')');
		return result.toString();
	}

} //FlusherImpl
