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
import org.jboss.tools.smooks.model.persistence12.Inserter;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;



/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Inserter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.InserterImpl#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.InserterImpl#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.InserterImpl#isInsertBefore <em>Insert Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.InserterImpl#getInsertedBeanId <em>Inserted Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.InserterImpl#getInsertOnElement <em>Insert On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.InserterImpl#getInsertOnElementNS <em>Insert On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.InserterImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InserterImpl extends ElementVisitorImpl implements Inserter {
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
	 * The default value of the '{@link #isInsertBefore() <em>Insert Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInsertBefore()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INSERT_BEFORE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isInsertBefore() <em>Insert Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isInsertBefore()
	 * @generated
	 * @ordered
	 */
	protected boolean insertBefore = INSERT_BEFORE_EDEFAULT;

	/**
	 * This is true if the Insert Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean insertBeforeESet;

	/**
	 * The default value of the '{@link #getInsertedBeanId() <em>Inserted Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertedBeanId()
	 * @generated
	 * @ordered
	 */
	protected static final String INSERTED_BEAN_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInsertedBeanId() <em>Inserted Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertedBeanId()
	 * @generated
	 * @ordered
	 */
	protected String insertedBeanId = INSERTED_BEAN_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getInsertOnElement() <em>Insert On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String INSERT_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInsertOnElement() <em>Insert On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertOnElement()
	 * @generated
	 * @ordered
	 */
	protected String insertOnElement = INSERT_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getInsertOnElementNS() <em>Insert On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String INSERT_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInsertOnElementNS() <em>Insert On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String insertOnElementNS = INSERT_ON_ELEMENT_NS_EDEFAULT;

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
	protected InserterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.INSERTER;
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.INSERTER__BEAN_ID, oldBeanId, beanId));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.INSERTER__DAO, oldDao, dao));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isInsertBefore() {
		return insertBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInsertBefore(boolean newInsertBefore) {
		boolean oldInsertBefore = insertBefore;
		insertBefore = newInsertBefore;
		boolean oldInsertBeforeESet = insertBeforeESet;
		insertBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.INSERTER__INSERT_BEFORE, oldInsertBefore, insertBefore, !oldInsertBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetInsertBefore() {
		boolean oldInsertBefore = insertBefore;
		boolean oldInsertBeforeESet = insertBeforeESet;
		insertBefore = INSERT_BEFORE_EDEFAULT;
		insertBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Persistence12Package.INSERTER__INSERT_BEFORE, oldInsertBefore, INSERT_BEFORE_EDEFAULT, oldInsertBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetInsertBefore() {
		return insertBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInsertedBeanId() {
		return insertedBeanId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInsertedBeanId(String newInsertedBeanId) {
		String oldInsertedBeanId = insertedBeanId;
		insertedBeanId = newInsertedBeanId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.INSERTER__INSERTED_BEAN_ID, oldInsertedBeanId, insertedBeanId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInsertOnElement() {
		return insertOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInsertOnElement(String newInsertOnElement) {
		String oldInsertOnElement = insertOnElement;
		insertOnElement = newInsertOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.INSERTER__INSERT_ON_ELEMENT, oldInsertOnElement, insertOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInsertOnElementNS() {
		return insertOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInsertOnElementNS(String newInsertOnElementNS) {
		String oldInsertOnElementNS = insertOnElementNS;
		insertOnElementNS = newInsertOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.INSERTER__INSERT_ON_ELEMENT_NS, oldInsertOnElementNS, insertOnElementNS));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.INSERTER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Persistence12Package.INSERTER__BEAN_ID:
				return getBeanId();
			case Persistence12Package.INSERTER__DAO:
				return getDao();
			case Persistence12Package.INSERTER__INSERT_BEFORE:
				return isInsertBefore() ? Boolean.TRUE : Boolean.FALSE;
			case Persistence12Package.INSERTER__INSERTED_BEAN_ID:
				return getInsertedBeanId();
			case Persistence12Package.INSERTER__INSERT_ON_ELEMENT:
				return getInsertOnElement();
			case Persistence12Package.INSERTER__INSERT_ON_ELEMENT_NS:
				return getInsertOnElementNS();
			case Persistence12Package.INSERTER__NAME:
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
			case Persistence12Package.INSERTER__BEAN_ID:
				setBeanId((String)newValue);
				return;
			case Persistence12Package.INSERTER__DAO:
				setDao((String)newValue);
				return;
			case Persistence12Package.INSERTER__INSERT_BEFORE:
				setInsertBefore(((Boolean)newValue).booleanValue());
				return;
			case Persistence12Package.INSERTER__INSERTED_BEAN_ID:
				setInsertedBeanId((String)newValue);
				return;
			case Persistence12Package.INSERTER__INSERT_ON_ELEMENT:
				setInsertOnElement((String)newValue);
				return;
			case Persistence12Package.INSERTER__INSERT_ON_ELEMENT_NS:
				setInsertOnElementNS((String)newValue);
				return;
			case Persistence12Package.INSERTER__NAME:
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
			case Persistence12Package.INSERTER__BEAN_ID:
				setBeanId(BEAN_ID_EDEFAULT);
				return;
			case Persistence12Package.INSERTER__DAO:
				setDao(DAO_EDEFAULT);
				return;
			case Persistence12Package.INSERTER__INSERT_BEFORE:
				unsetInsertBefore();
				return;
			case Persistence12Package.INSERTER__INSERTED_BEAN_ID:
				setInsertedBeanId(INSERTED_BEAN_ID_EDEFAULT);
				return;
			case Persistence12Package.INSERTER__INSERT_ON_ELEMENT:
				setInsertOnElement(INSERT_ON_ELEMENT_EDEFAULT);
				return;
			case Persistence12Package.INSERTER__INSERT_ON_ELEMENT_NS:
				setInsertOnElementNS(INSERT_ON_ELEMENT_NS_EDEFAULT);
				return;
			case Persistence12Package.INSERTER__NAME:
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
			case Persistence12Package.INSERTER__BEAN_ID:
				return BEAN_ID_EDEFAULT == null ? beanId != null : !BEAN_ID_EDEFAULT.equals(beanId);
			case Persistence12Package.INSERTER__DAO:
				return DAO_EDEFAULT == null ? dao != null : !DAO_EDEFAULT.equals(dao);
			case Persistence12Package.INSERTER__INSERT_BEFORE:
				return isSetInsertBefore();
			case Persistence12Package.INSERTER__INSERTED_BEAN_ID:
				return INSERTED_BEAN_ID_EDEFAULT == null ? insertedBeanId != null : !INSERTED_BEAN_ID_EDEFAULT.equals(insertedBeanId);
			case Persistence12Package.INSERTER__INSERT_ON_ELEMENT:
				return INSERT_ON_ELEMENT_EDEFAULT == null ? insertOnElement != null : !INSERT_ON_ELEMENT_EDEFAULT.equals(insertOnElement);
			case Persistence12Package.INSERTER__INSERT_ON_ELEMENT_NS:
				return INSERT_ON_ELEMENT_NS_EDEFAULT == null ? insertOnElementNS != null : !INSERT_ON_ELEMENT_NS_EDEFAULT.equals(insertOnElementNS);
			case Persistence12Package.INSERTER__NAME:
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
		result.append(" (beanId: ");
		result.append(beanId);
		result.append(", dao: ");
		result.append(dao);
		result.append(", insertBefore: ");
		if (insertBeforeESet) result.append(insertBefore); else result.append("<unset>");
		result.append(", insertedBeanId: ");
		result.append(insertedBeanId);
		result.append(", insertOnElement: ");
		result.append(insertOnElement);
		result.append(", insertOnElementNS: ");
		result.append(insertOnElementNS);
		result.append(", name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //InserterImpl
