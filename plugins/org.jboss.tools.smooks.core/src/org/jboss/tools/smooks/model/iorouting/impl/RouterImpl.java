/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.iorouting.impl;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.iorouting.IoroutingPackage;
import org.jboss.tools.smooks.model.iorouting.Router;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Router</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.impl.RouterImpl#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.impl.RouterImpl#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.impl.RouterImpl#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.impl.RouterImpl#getResourceName <em>Resource Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.impl.RouterImpl#getRouteOnElement <em>Route On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.impl.RouterImpl#getRouteOnElementNS <em>Route On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RouterImpl extends ElementVisitorImpl implements Router {
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
	 * The default value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEncoding()
	 * @generated
	 * @ordered
	 */
	protected static final String ENCODING_EDEFAULT = "UTF-8";

	/**
	 * The cached value of the '{@link #getEncoding() <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEncoding()
	 * @generated
	 * @ordered
	 */
	protected String encoding = ENCODING_EDEFAULT;

	/**
	 * This is true if the Encoding attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean encodingESet;

	/**
	 * The default value of the '{@link #isExecuteBefore() <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExecuteBefore()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXECUTE_BEFORE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExecuteBefore() <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExecuteBefore()
	 * @generated
	 * @ordered
	 */
	protected boolean executeBefore = EXECUTE_BEFORE_EDEFAULT;

	/**
	 * This is true if the Execute Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean executeBeforeESet;

	/**
	 * The default value of the '{@link #getResourceName() <em>Resource Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceName()
	 * @generated
	 * @ordered
	 */
	protected static final String RESOURCE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getResourceName() <em>Resource Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResourceName()
	 * @generated
	 * @ordered
	 */
	protected String resourceName = RESOURCE_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getRouteOnElement() <em>Route On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String ROUTE_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRouteOnElement() <em>Route On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOnElement()
	 * @generated
	 * @ordered
	 */
	protected String routeOnElement = ROUTE_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getRouteOnElementNS() <em>Route On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String ROUTE_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRouteOnElementNS() <em>Route On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String routeOnElementNS = ROUTE_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RouterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IoroutingPackage.Literals.ROUTER;
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
			eNotify(new ENotificationImpl(this, Notification.SET, IoroutingPackage.ROUTER__BEAN_ID, oldBeanId, beanId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEncoding(String newEncoding) {
		String oldEncoding = encoding;
		encoding = newEncoding;
		boolean oldEncodingESet = encodingESet;
		encodingESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IoroutingPackage.ROUTER__ENCODING, oldEncoding, encoding, !oldEncodingESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEncoding() {
		String oldEncoding = encoding;
		boolean oldEncodingESet = encodingESet;
		encoding = ENCODING_EDEFAULT;
		encodingESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, IoroutingPackage.ROUTER__ENCODING, oldEncoding, ENCODING_EDEFAULT, oldEncodingESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEncoding() {
		return encodingESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExecuteBefore() {
		return executeBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteBefore(boolean newExecuteBefore) {
		boolean oldExecuteBefore = executeBefore;
		executeBefore = newExecuteBefore;
		boolean oldExecuteBeforeESet = executeBeforeESet;
		executeBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IoroutingPackage.ROUTER__EXECUTE_BEFORE, oldExecuteBefore, executeBefore, !oldExecuteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetExecuteBefore() {
		boolean oldExecuteBefore = executeBefore;
		boolean oldExecuteBeforeESet = executeBeforeESet;
		executeBefore = EXECUTE_BEFORE_EDEFAULT;
		executeBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, IoroutingPackage.ROUTER__EXECUTE_BEFORE, oldExecuteBefore, EXECUTE_BEFORE_EDEFAULT, oldExecuteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetExecuteBefore() {
		return executeBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getResourceName() {
		return resourceName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResourceName(String newResourceName) {
		String oldResourceName = resourceName;
		resourceName = newResourceName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IoroutingPackage.ROUTER__RESOURCE_NAME, oldResourceName, resourceName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRouteOnElement() {
		return routeOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRouteOnElement(String newRouteOnElement) {
		String oldRouteOnElement = routeOnElement;
		routeOnElement = newRouteOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT, oldRouteOnElement, routeOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRouteOnElementNS() {
		return routeOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRouteOnElementNS(String newRouteOnElementNS) {
		String oldRouteOnElementNS = routeOnElementNS;
		routeOnElementNS = newRouteOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT_NS, oldRouteOnElementNS, routeOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case IoroutingPackage.ROUTER__BEAN_ID:
				return getBeanId();
			case IoroutingPackage.ROUTER__ENCODING:
				return getEncoding();
			case IoroutingPackage.ROUTER__EXECUTE_BEFORE:
				return isExecuteBefore() ? Boolean.TRUE : Boolean.FALSE;
			case IoroutingPackage.ROUTER__RESOURCE_NAME:
				return getResourceName();
			case IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT:
				return getRouteOnElement();
			case IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT_NS:
				return getRouteOnElementNS();
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
			case IoroutingPackage.ROUTER__BEAN_ID:
				setBeanId((String)newValue);
				return;
			case IoroutingPackage.ROUTER__ENCODING:
				setEncoding((String)newValue);
				return;
			case IoroutingPackage.ROUTER__EXECUTE_BEFORE:
				setExecuteBefore(((Boolean)newValue).booleanValue());
				return;
			case IoroutingPackage.ROUTER__RESOURCE_NAME:
				setResourceName((String)newValue);
				return;
			case IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT:
				setRouteOnElement((String)newValue);
				return;
			case IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT_NS:
				setRouteOnElementNS((String)newValue);
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
			case IoroutingPackage.ROUTER__BEAN_ID:
				setBeanId(BEAN_ID_EDEFAULT);
				return;
			case IoroutingPackage.ROUTER__ENCODING:
				unsetEncoding();
				return;
			case IoroutingPackage.ROUTER__EXECUTE_BEFORE:
				unsetExecuteBefore();
				return;
			case IoroutingPackage.ROUTER__RESOURCE_NAME:
				setResourceName(RESOURCE_NAME_EDEFAULT);
				return;
			case IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT:
				setRouteOnElement(ROUTE_ON_ELEMENT_EDEFAULT);
				return;
			case IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT_NS:
				setRouteOnElementNS(ROUTE_ON_ELEMENT_NS_EDEFAULT);
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
			case IoroutingPackage.ROUTER__BEAN_ID:
				return BEAN_ID_EDEFAULT == null ? beanId != null : !BEAN_ID_EDEFAULT.equals(beanId);
			case IoroutingPackage.ROUTER__ENCODING:
				return isSetEncoding();
			case IoroutingPackage.ROUTER__EXECUTE_BEFORE:
				return isSetExecuteBefore();
			case IoroutingPackage.ROUTER__RESOURCE_NAME:
				return RESOURCE_NAME_EDEFAULT == null ? resourceName != null : !RESOURCE_NAME_EDEFAULT.equals(resourceName);
			case IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT:
				return ROUTE_ON_ELEMENT_EDEFAULT == null ? routeOnElement != null : !ROUTE_ON_ELEMENT_EDEFAULT.equals(routeOnElement);
			case IoroutingPackage.ROUTER__ROUTE_ON_ELEMENT_NS:
				return ROUTE_ON_ELEMENT_NS_EDEFAULT == null ? routeOnElementNS != null : !ROUTE_ON_ELEMENT_NS_EDEFAULT.equals(routeOnElementNS);
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
		result.append(" (beanId: ");
		result.append(beanId);
		result.append(", encoding: ");
		if (encodingESet) result.append(encoding); else result.append("<unset>");
		result.append(", executeBefore: ");
		if (executeBeforeESet) result.append(executeBefore); else result.append("<unset>");
		result.append(", resourceName: ");
		result.append(resourceName);
		result.append(", routeOnElement: ");
		result.append(routeOnElement);
		result.append(", routeOnElementNS: ");
		result.append(routeOnElementNS);
		result.append(')');
		return result.toString();
	}

} //RouterImpl
