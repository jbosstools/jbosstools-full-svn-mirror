/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.persistence12.Locator;
import org.jboss.tools.smooks.model.persistence12.OnNoResult;
import org.jboss.tools.smooks.model.persistence12.Parameters;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;



/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Locator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl#getQuery <em>Query</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl#getParams <em>Params</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl#getLookup <em>Lookup</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl#getLookupOnElement <em>Lookup On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl#getLookupOnElementNS <em>Lookup On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl#getOnNoResult <em>On No Result</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.LocatorImpl#isUniqueResult <em>Unique Result</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LocatorImpl extends ElementVisitorImpl implements Locator {
	/**
	 * The default value of the '{@link #getQuery() <em>Query</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuery()
	 * @generated
	 * @ordered
	 */
	protected static final String QUERY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getQuery() <em>Query</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuery()
	 * @generated
	 * @ordered
	 */
	protected String query = QUERY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getParams() <em>Params</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParams()
	 * @generated
	 * @ordered
	 */
	protected Parameters params;

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
	 * The default value of the '{@link #getLookup() <em>Lookup</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLookup()
	 * @generated
	 * @ordered
	 */
	protected static final String LOOKUP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLookup() <em>Lookup</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLookup()
	 * @generated
	 * @ordered
	 */
	protected String lookup = LOOKUP_EDEFAULT;

	/**
	 * The default value of the '{@link #getLookupOnElement() <em>Lookup On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLookupOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String LOOKUP_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLookupOnElement() <em>Lookup On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLookupOnElement()
	 * @generated
	 * @ordered
	 */
	protected String lookupOnElement = LOOKUP_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLookupOnElementNS() <em>Lookup On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLookupOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String LOOKUP_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLookupOnElementNS() <em>Lookup On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLookupOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String lookupOnElementNS = LOOKUP_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * The default value of the '{@link #getOnNoResult() <em>On No Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOnNoResult()
	 * @generated
	 * @ordered
	 */
	protected static final OnNoResult ON_NO_RESULT_EDEFAULT = OnNoResult.NULLIFY_LITERAL;

	/**
	 * The cached value of the '{@link #getOnNoResult() <em>On No Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOnNoResult()
	 * @generated
	 * @ordered
	 */
	protected OnNoResult onNoResult = ON_NO_RESULT_EDEFAULT;

	/**
	 * This is true if the On No Result attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean onNoResultESet;

	/**
	 * The default value of the '{@link #isUniqueResult() <em>Unique Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUniqueResult()
	 * @generated
	 * @ordered
	 */
	protected static final boolean UNIQUE_RESULT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUniqueResult() <em>Unique Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUniqueResult()
	 * @generated
	 * @ordered
	 */
	protected boolean uniqueResult = UNIQUE_RESULT_EDEFAULT;

	/**
	 * This is true if the Unique Result attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean uniqueResultESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocatorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.LOCATOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuery(String newQuery) {
		String oldQuery = query;
		query = newQuery;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__QUERY, oldQuery, query));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameters getParams() {
		return params;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParams(Parameters newParams, NotificationChain msgs) {
		Parameters oldParams = params;
		params = newParams;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__PARAMS, oldParams, newParams);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParams(Parameters newParams) {
		if (newParams != params) {
			NotificationChain msgs = null;
			if (params != null)
				msgs = ((InternalEObject)params).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Persistence12Package.LOCATOR__PARAMS, null, msgs);
			if (newParams != null)
				msgs = ((InternalEObject)newParams).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Persistence12Package.LOCATOR__PARAMS, null, msgs);
			msgs = basicSetParams(newParams, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__PARAMS, newParams, newParams));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__BEAN_ID, oldBeanId, beanId));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__DAO, oldDao, dao));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLookup() {
		return lookup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLookup(String newLookup) {
		String oldLookup = lookup;
		lookup = newLookup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__LOOKUP, oldLookup, lookup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLookupOnElement() {
		return lookupOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLookupOnElement(String newLookupOnElement) {
		String oldLookupOnElement = lookupOnElement;
		lookupOnElement = newLookupOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT, oldLookupOnElement, lookupOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLookupOnElementNS() {
		return lookupOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLookupOnElementNS(String newLookupOnElementNS) {
		String oldLookupOnElementNS = lookupOnElementNS;
		lookupOnElementNS = newLookupOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT_NS, oldLookupOnElementNS, lookupOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OnNoResult getOnNoResult() {
		return onNoResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOnNoResult(OnNoResult newOnNoResult) {
		OnNoResult oldOnNoResult = onNoResult;
		onNoResult = newOnNoResult == null ? ON_NO_RESULT_EDEFAULT : newOnNoResult;
		boolean oldOnNoResultESet = onNoResultESet;
		onNoResultESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__ON_NO_RESULT, oldOnNoResult, onNoResult, !oldOnNoResultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetOnNoResult() {
		OnNoResult oldOnNoResult = onNoResult;
		boolean oldOnNoResultESet = onNoResultESet;
		onNoResult = ON_NO_RESULT_EDEFAULT;
		onNoResultESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Persistence12Package.LOCATOR__ON_NO_RESULT, oldOnNoResult, ON_NO_RESULT_EDEFAULT, oldOnNoResultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetOnNoResult() {
		return onNoResultESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUniqueResult() {
		return uniqueResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUniqueResult(boolean newUniqueResult) {
		boolean oldUniqueResult = uniqueResult;
		uniqueResult = newUniqueResult;
		boolean oldUniqueResultESet = uniqueResultESet;
		uniqueResultESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.LOCATOR__UNIQUE_RESULT, oldUniqueResult, uniqueResult, !oldUniqueResultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetUniqueResult() {
		boolean oldUniqueResult = uniqueResult;
		boolean oldUniqueResultESet = uniqueResultESet;
		uniqueResult = UNIQUE_RESULT_EDEFAULT;
		uniqueResultESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, Persistence12Package.LOCATOR__UNIQUE_RESULT, oldUniqueResult, UNIQUE_RESULT_EDEFAULT, oldUniqueResultESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetUniqueResult() {
		return uniqueResultESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Persistence12Package.LOCATOR__PARAMS:
				return basicSetParams(null, msgs);
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
			case Persistence12Package.LOCATOR__QUERY:
				return getQuery();
			case Persistence12Package.LOCATOR__PARAMS:
				return getParams();
			case Persistence12Package.LOCATOR__BEAN_ID:
				return getBeanId();
			case Persistence12Package.LOCATOR__DAO:
				return getDao();
			case Persistence12Package.LOCATOR__LOOKUP:
				return getLookup();
			case Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT:
				return getLookupOnElement();
			case Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT_NS:
				return getLookupOnElementNS();
			case Persistence12Package.LOCATOR__ON_NO_RESULT:
				return getOnNoResult();
			case Persistence12Package.LOCATOR__UNIQUE_RESULT:
				return isUniqueResult() ? Boolean.TRUE : Boolean.FALSE;
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
			case Persistence12Package.LOCATOR__QUERY:
				setQuery((String)newValue);
				return;
			case Persistence12Package.LOCATOR__PARAMS:
				setParams((Parameters)newValue);
				return;
			case Persistence12Package.LOCATOR__BEAN_ID:
				setBeanId((String)newValue);
				return;
			case Persistence12Package.LOCATOR__DAO:
				setDao((String)newValue);
				return;
			case Persistence12Package.LOCATOR__LOOKUP:
				setLookup((String)newValue);
				return;
			case Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT:
				setLookupOnElement((String)newValue);
				return;
			case Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT_NS:
				setLookupOnElementNS((String)newValue);
				return;
			case Persistence12Package.LOCATOR__ON_NO_RESULT:
				setOnNoResult((OnNoResult)newValue);
				return;
			case Persistence12Package.LOCATOR__UNIQUE_RESULT:
				setUniqueResult(((Boolean)newValue).booleanValue());
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
			case Persistence12Package.LOCATOR__QUERY:
				setQuery(QUERY_EDEFAULT);
				return;
			case Persistence12Package.LOCATOR__PARAMS:
				setParams((Parameters)null);
				return;
			case Persistence12Package.LOCATOR__BEAN_ID:
				setBeanId(BEAN_ID_EDEFAULT);
				return;
			case Persistence12Package.LOCATOR__DAO:
				setDao(DAO_EDEFAULT);
				return;
			case Persistence12Package.LOCATOR__LOOKUP:
				setLookup(LOOKUP_EDEFAULT);
				return;
			case Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT:
				setLookupOnElement(LOOKUP_ON_ELEMENT_EDEFAULT);
				return;
			case Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT_NS:
				setLookupOnElementNS(LOOKUP_ON_ELEMENT_NS_EDEFAULT);
				return;
			case Persistence12Package.LOCATOR__ON_NO_RESULT:
				unsetOnNoResult();
				return;
			case Persistence12Package.LOCATOR__UNIQUE_RESULT:
				unsetUniqueResult();
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
			case Persistence12Package.LOCATOR__QUERY:
				return QUERY_EDEFAULT == null ? query != null : !QUERY_EDEFAULT.equals(query);
			case Persistence12Package.LOCATOR__PARAMS:
				return params != null;
			case Persistence12Package.LOCATOR__BEAN_ID:
				return BEAN_ID_EDEFAULT == null ? beanId != null : !BEAN_ID_EDEFAULT.equals(beanId);
			case Persistence12Package.LOCATOR__DAO:
				return DAO_EDEFAULT == null ? dao != null : !DAO_EDEFAULT.equals(dao);
			case Persistence12Package.LOCATOR__LOOKUP:
				return LOOKUP_EDEFAULT == null ? lookup != null : !LOOKUP_EDEFAULT.equals(lookup);
			case Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT:
				return LOOKUP_ON_ELEMENT_EDEFAULT == null ? lookupOnElement != null : !LOOKUP_ON_ELEMENT_EDEFAULT.equals(lookupOnElement);
			case Persistence12Package.LOCATOR__LOOKUP_ON_ELEMENT_NS:
				return LOOKUP_ON_ELEMENT_NS_EDEFAULT == null ? lookupOnElementNS != null : !LOOKUP_ON_ELEMENT_NS_EDEFAULT.equals(lookupOnElementNS);
			case Persistence12Package.LOCATOR__ON_NO_RESULT:
				return isSetOnNoResult();
			case Persistence12Package.LOCATOR__UNIQUE_RESULT:
				return isSetUniqueResult();
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
		result.append(" (query: ");
		result.append(query);
		result.append(", beanId: ");
		result.append(beanId);
		result.append(", dao: ");
		result.append(dao);
		result.append(", lookup: ");
		result.append(lookup);
		result.append(", lookupOnElement: ");
		result.append(lookupOnElement);
		result.append(", lookupOnElementNS: ");
		result.append(lookupOnElementNS);
		result.append(", onNoResult: ");
		if (onNoResultESet) result.append(onNoResult); else result.append("<unset>");
		result.append(", uniqueResult: ");
		if (uniqueResultESet) result.append(uniqueResult); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //LocatorImpl
