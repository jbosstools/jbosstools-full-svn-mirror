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
import org.jboss.tools.smooks.model.persistence12.Deleter;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;



/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deleter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl#isDeleteBefore <em>Delete Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl#getDeletedBeanId <em>Deleted Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl#getDeleteOnElement <em>Delete On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl#getDeleteOnElementNS <em>Delete On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.DeleterImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DeleterImpl extends ElementVisitorImpl implements Deleter {
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
	 * The default value of the '{@link #isDeleteBefore() <em>Delete Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDeleteBefore()
	 * @generated
	 * @ordered
	 */
	protected static final boolean DELETE_BEFORE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isDeleteBefore() <em>Delete Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isDeleteBefore()
	 * @generated
	 * @ordered
	 */
	protected boolean deleteBefore = DELETE_BEFORE_EDEFAULT;

	/**
	 * This is true if the Delete Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean deleteBeforeESet;

	/**
	 * The default value of the '{@link #getDeletedBeanId() <em>Deleted Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeletedBeanId()
	 * @generated
	 * @ordered
	 */
	protected static final String DELETED_BEAN_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDeletedBeanId() <em>Deleted Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeletedBeanId()
	 * @generated
	 * @ordered
	 */
	protected String deletedBeanId = DELETED_BEAN_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getDeleteOnElement() <em>Delete On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeleteOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String DELETE_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDeleteOnElement() <em>Delete On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeleteOnElement()
	 * @generated
	 * @ordered
	 */
	protected String deleteOnElement = DELETE_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDeleteOnElementNS() <em>Delete On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeleteOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String DELETE_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDeleteOnElementNS() <em>Delete On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeleteOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String deleteOnElementNS = DELETE_ON_ELEMENT_NS_EDEFAULT;

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
	protected DeleterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.DELETER;
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.DELETER__BEAN_ID, oldBeanId, beanId));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.DELETER__DAO, oldDao, dao));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isDeleteBefore() {
		return deleteBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeleteBefore(boolean newDeleteBefore) {
		boolean oldDeleteBefore = deleteBefore;
		deleteBefore = newDeleteBefore;
		boolean oldDeleteBeforeESet = deleteBeforeESet;
		deleteBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.DELETER__DELETE_BEFORE, oldDeleteBefore, deleteBefore, !oldDeleteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDeleteBefore() {
		boolean oldDeleteBefore = deleteBefore;
		boolean oldDeleteBeforeESet = deleteBeforeESet;
		deleteBefore = DELETE_BEFORE_EDEFAULT;
		deleteBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Persistence12Package.DELETER__DELETE_BEFORE, oldDeleteBefore, DELETE_BEFORE_EDEFAULT, oldDeleteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDeleteBefore() {
		return deleteBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDeletedBeanId() {
		return deletedBeanId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeletedBeanId(String newDeletedBeanId) {
		String oldDeletedBeanId = deletedBeanId;
		deletedBeanId = newDeletedBeanId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.DELETER__DELETED_BEAN_ID, oldDeletedBeanId, deletedBeanId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDeleteOnElement() {
		return deleteOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeleteOnElement(String newDeleteOnElement) {
		String oldDeleteOnElement = deleteOnElement;
		deleteOnElement = newDeleteOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.DELETER__DELETE_ON_ELEMENT, oldDeleteOnElement, deleteOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDeleteOnElementNS() {
		return deleteOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeleteOnElementNS(String newDeleteOnElementNS) {
		String oldDeleteOnElementNS = deleteOnElementNS;
		deleteOnElementNS = newDeleteOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.DELETER__DELETE_ON_ELEMENT_NS, oldDeleteOnElementNS, deleteOnElementNS));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.DELETER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Persistence12Package.DELETER__BEAN_ID:
				return getBeanId();
			case Persistence12Package.DELETER__DAO:
				return getDao();
			case Persistence12Package.DELETER__DELETE_BEFORE:
				return isDeleteBefore() ? Boolean.TRUE : Boolean.FALSE;
			case Persistence12Package.DELETER__DELETED_BEAN_ID:
				return getDeletedBeanId();
			case Persistence12Package.DELETER__DELETE_ON_ELEMENT:
				return getDeleteOnElement();
			case Persistence12Package.DELETER__DELETE_ON_ELEMENT_NS:
				return getDeleteOnElementNS();
			case Persistence12Package.DELETER__NAME:
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
			case Persistence12Package.DELETER__BEAN_ID:
				setBeanId((String)newValue);
				return;
			case Persistence12Package.DELETER__DAO:
				setDao((String)newValue);
				return;
			case Persistence12Package.DELETER__DELETE_BEFORE:
				setDeleteBefore(((Boolean)newValue).booleanValue());
				return;
			case Persistence12Package.DELETER__DELETED_BEAN_ID:
				setDeletedBeanId((String)newValue);
				return;
			case Persistence12Package.DELETER__DELETE_ON_ELEMENT:
				setDeleteOnElement((String)newValue);
				return;
			case Persistence12Package.DELETER__DELETE_ON_ELEMENT_NS:
				setDeleteOnElementNS((String)newValue);
				return;
			case Persistence12Package.DELETER__NAME:
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
			case Persistence12Package.DELETER__BEAN_ID:
				setBeanId(BEAN_ID_EDEFAULT);
				return;
			case Persistence12Package.DELETER__DAO:
				setDao(DAO_EDEFAULT);
				return;
			case Persistence12Package.DELETER__DELETE_BEFORE:
				unsetDeleteBefore();
				return;
			case Persistence12Package.DELETER__DELETED_BEAN_ID:
				setDeletedBeanId(DELETED_BEAN_ID_EDEFAULT);
				return;
			case Persistence12Package.DELETER__DELETE_ON_ELEMENT:
				setDeleteOnElement(DELETE_ON_ELEMENT_EDEFAULT);
				return;
			case Persistence12Package.DELETER__DELETE_ON_ELEMENT_NS:
				setDeleteOnElementNS(DELETE_ON_ELEMENT_NS_EDEFAULT);
				return;
			case Persistence12Package.DELETER__NAME:
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
			case Persistence12Package.DELETER__BEAN_ID:
				return BEAN_ID_EDEFAULT == null ? beanId != null : !BEAN_ID_EDEFAULT.equals(beanId);
			case Persistence12Package.DELETER__DAO:
				return DAO_EDEFAULT == null ? dao != null : !DAO_EDEFAULT.equals(dao);
			case Persistence12Package.DELETER__DELETE_BEFORE:
				return isSetDeleteBefore();
			case Persistence12Package.DELETER__DELETED_BEAN_ID:
				return DELETED_BEAN_ID_EDEFAULT == null ? deletedBeanId != null : !DELETED_BEAN_ID_EDEFAULT.equals(deletedBeanId);
			case Persistence12Package.DELETER__DELETE_ON_ELEMENT:
				return DELETE_ON_ELEMENT_EDEFAULT == null ? deleteOnElement != null : !DELETE_ON_ELEMENT_EDEFAULT.equals(deleteOnElement);
			case Persistence12Package.DELETER__DELETE_ON_ELEMENT_NS:
				return DELETE_ON_ELEMENT_NS_EDEFAULT == null ? deleteOnElementNS != null : !DELETE_ON_ELEMENT_NS_EDEFAULT.equals(deleteOnElementNS);
			case Persistence12Package.DELETER__NAME:
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
		result.append(" (beanId: "); //$NON-NLS-1$
		result.append(beanId);
		result.append(", dao: "); //$NON-NLS-1$
		result.append(dao);
		result.append(", deleteBefore: "); //$NON-NLS-1$
		if (deleteBeforeESet) result.append(deleteBefore); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", deletedBeanId: "); //$NON-NLS-1$
		result.append(deletedBeanId);
		result.append(", deleteOnElement: "); //$NON-NLS-1$
		result.append(deleteOnElement);
		result.append(", deleteOnElementNS: "); //$NON-NLS-1$
		result.append(deleteOnElementNS);
		result.append(", name: "); //$NON-NLS-1$
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //DeleterImpl
