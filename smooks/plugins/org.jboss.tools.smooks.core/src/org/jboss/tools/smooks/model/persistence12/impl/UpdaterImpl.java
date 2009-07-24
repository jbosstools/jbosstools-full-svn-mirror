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
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.persistence12.Updater;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;



/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Updater</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl#isUpdateBefore <em>Update Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl#getUpdatedBeanId <em>Updated Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl#getUpdateOnElement <em>Update On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.UpdaterImpl#getUpdateOnElementNS <em>Update On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UpdaterImpl extends ElementVisitorImpl implements Updater {
	/**
	 * The default value of the '{@link #getBeanId() <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanId()
	 * @generated
	 * @ordered
	 */
	protected static final String BEAN_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeanId() <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanId()
	 * @generated
	 * @ordered
	 */
	protected String beanId = BEAN_ID_EDEFAULT;

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
	 * The default value of the '{@link #isUpdateBefore() <em>Update Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUpdateBefore()
	 * @generated
	 * @ordered
	 */
	protected static final boolean UPDATE_BEFORE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUpdateBefore() <em>Update Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUpdateBefore()
	 * @generated
	 * @ordered
	 */
	protected boolean updateBefore = UPDATE_BEFORE_EDEFAULT;

	/**
	 * This is true if the Update Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean updateBeforeESet;

	/**
	 * The default value of the '{@link #getUpdatedBeanId() <em>Updated Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdatedBeanId()
	 * @generated
	 * @ordered
	 */
	protected static final String UPDATED_BEAN_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUpdatedBeanId() <em>Updated Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdatedBeanId()
	 * @generated
	 * @ordered
	 */
	protected String updatedBeanId = UPDATED_BEAN_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getUpdateOnElement() <em>Update On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdateOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String UPDATE_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUpdateOnElement() <em>Update On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdateOnElement()
	 * @generated
	 * @ordered
	 */
	protected String updateOnElement = UPDATE_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getUpdateOnElementNS() <em>Update On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdateOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String UPDATE_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUpdateOnElementNS() <em>Update On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpdateOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String updateOnElementNS = UPDATE_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UpdaterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.UPDATER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeanId() {
		return beanId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanId(String newBeanId) {
		String oldBeanId = beanId;
		beanId = newBeanId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.UPDATER__BEAN_ID, oldBeanId, beanId));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.UPDATER__DAO, oldDao, dao));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.UPDATER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUpdateBefore() {
		return updateBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUpdateBefore(boolean newUpdateBefore) {
		boolean oldUpdateBefore = updateBefore;
		updateBefore = newUpdateBefore;
		boolean oldUpdateBeforeESet = updateBeforeESet;
		updateBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.UPDATER__UPDATE_BEFORE, oldUpdateBefore, updateBefore, !oldUpdateBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetUpdateBefore() {
		boolean oldUpdateBefore = updateBefore;
		boolean oldUpdateBeforeESet = updateBeforeESet;
		updateBefore = UPDATE_BEFORE_EDEFAULT;
		updateBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Persistence12Package.UPDATER__UPDATE_BEFORE, oldUpdateBefore, UPDATE_BEFORE_EDEFAULT, oldUpdateBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetUpdateBefore() {
		return updateBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUpdatedBeanId() {
		return updatedBeanId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUpdatedBeanId(String newUpdatedBeanId) {
		String oldUpdatedBeanId = updatedBeanId;
		updatedBeanId = newUpdatedBeanId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.UPDATER__UPDATED_BEAN_ID, oldUpdatedBeanId, updatedBeanId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUpdateOnElement() {
		return updateOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUpdateOnElement(String newUpdateOnElement) {
		String oldUpdateOnElement = updateOnElement;
		updateOnElement = newUpdateOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.UPDATER__UPDATE_ON_ELEMENT, oldUpdateOnElement, updateOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUpdateOnElementNS() {
		return updateOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUpdateOnElementNS(String newUpdateOnElementNS) {
		String oldUpdateOnElementNS = updateOnElementNS;
		updateOnElementNS = newUpdateOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.UPDATER__UPDATE_ON_ELEMENT_NS, oldUpdateOnElementNS, updateOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Persistence12Package.UPDATER__BEAN_ID:
				return getBeanId();
			case Persistence12Package.UPDATER__DAO:
				return getDao();
			case Persistence12Package.UPDATER__NAME:
				return getName();
			case Persistence12Package.UPDATER__UPDATE_BEFORE:
				return isUpdateBefore() ? Boolean.TRUE : Boolean.FALSE;
			case Persistence12Package.UPDATER__UPDATED_BEAN_ID:
				return getUpdatedBeanId();
			case Persistence12Package.UPDATER__UPDATE_ON_ELEMENT:
				return getUpdateOnElement();
			case Persistence12Package.UPDATER__UPDATE_ON_ELEMENT_NS:
				return getUpdateOnElementNS();
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
			case Persistence12Package.UPDATER__BEAN_ID:
				setBeanId((String)newValue);
				return;
			case Persistence12Package.UPDATER__DAO:
				setDao((String)newValue);
				return;
			case Persistence12Package.UPDATER__NAME:
				setName((String)newValue);
				return;
			case Persistence12Package.UPDATER__UPDATE_BEFORE:
				setUpdateBefore(((Boolean)newValue).booleanValue());
				return;
			case Persistence12Package.UPDATER__UPDATED_BEAN_ID:
				setUpdatedBeanId((String)newValue);
				return;
			case Persistence12Package.UPDATER__UPDATE_ON_ELEMENT:
				setUpdateOnElement((String)newValue);
				return;
			case Persistence12Package.UPDATER__UPDATE_ON_ELEMENT_NS:
				setUpdateOnElementNS((String)newValue);
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
			case Persistence12Package.UPDATER__BEAN_ID:
				setBeanId(BEAN_ID_EDEFAULT);
				return;
			case Persistence12Package.UPDATER__DAO:
				setDao(DAO_EDEFAULT);
				return;
			case Persistence12Package.UPDATER__NAME:
				setName(NAME_EDEFAULT);
				return;
			case Persistence12Package.UPDATER__UPDATE_BEFORE:
				unsetUpdateBefore();
				return;
			case Persistence12Package.UPDATER__UPDATED_BEAN_ID:
				setUpdatedBeanId(UPDATED_BEAN_ID_EDEFAULT);
				return;
			case Persistence12Package.UPDATER__UPDATE_ON_ELEMENT:
				setUpdateOnElement(UPDATE_ON_ELEMENT_EDEFAULT);
				return;
			case Persistence12Package.UPDATER__UPDATE_ON_ELEMENT_NS:
				setUpdateOnElementNS(UPDATE_ON_ELEMENT_NS_EDEFAULT);
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
			case Persistence12Package.UPDATER__BEAN_ID:
				return BEAN_ID_EDEFAULT == null ? beanId != null : !BEAN_ID_EDEFAULT.equals(beanId);
			case Persistence12Package.UPDATER__DAO:
				return DAO_EDEFAULT == null ? dao != null : !DAO_EDEFAULT.equals(dao);
			case Persistence12Package.UPDATER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case Persistence12Package.UPDATER__UPDATE_BEFORE:
				return isSetUpdateBefore();
			case Persistence12Package.UPDATER__UPDATED_BEAN_ID:
				return UPDATED_BEAN_ID_EDEFAULT == null ? updatedBeanId != null : !UPDATED_BEAN_ID_EDEFAULT.equals(updatedBeanId);
			case Persistence12Package.UPDATER__UPDATE_ON_ELEMENT:
				return UPDATE_ON_ELEMENT_EDEFAULT == null ? updateOnElement != null : !UPDATE_ON_ELEMENT_EDEFAULT.equals(updateOnElement);
			case Persistence12Package.UPDATER__UPDATE_ON_ELEMENT_NS:
				return UPDATE_ON_ELEMENT_NS_EDEFAULT == null ? updateOnElementNS != null : !UPDATE_ON_ELEMENT_NS_EDEFAULT.equals(updateOnElementNS);
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
		result.append(" (beanId: ");
		result.append(beanId);
		result.append(", dao: ");
		result.append(dao);
		result.append(", name: ");
		result.append(name);
		result.append(", updateBefore: ");
		if (updateBeforeESet) result.append(updateBefore); else result.append("<unset>");
		result.append(", updatedBeanId: ");
		result.append(updatedBeanId);
		result.append(", updateOnElement: ");
		result.append(updateOnElement);
		result.append(", updateOnElementNS: ");
		result.append(updateOnElementNS);
		result.append(')');
		return result.toString();
	}

} //UpdaterImpl
