/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.esbrouting.impl;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage;
import org.jboss.tools.smooks.model.esbrouting.RouteBean;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Route Bean</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl#getBeanIdRef <em>Bean Id Ref</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl#getMessagePayloadLocation <em>Message Payload Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl#isRouteBefore <em>Route Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl#getRouteOnElement <em>Route On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl#getRouteOnElementNS <em>Route On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl#getToServiceCategory <em>To Service Category</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl#getToServiceName <em>To Service Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RouteBeanImpl extends ElementVisitorImpl implements RouteBean {
	/**
	 * The default value of the '{@link #getBeanIdRef() <em>Bean Id Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanIdRef()
	 * @generated
	 * @ordered
	 */
	protected static final String BEAN_ID_REF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeanIdRef() <em>Bean Id Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanIdRef()
	 * @generated
	 * @ordered
	 */
	protected String beanIdRef = BEAN_ID_REF_EDEFAULT;

	/**
	 * The default value of the '{@link #getMessagePayloadLocation() <em>Message Payload Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessagePayloadLocation()
	 * @generated
	 * @ordered
	 */
	protected static final String MESSAGE_PAYLOAD_LOCATION_EDEFAULT = ""; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getMessagePayloadLocation() <em>Message Payload Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessagePayloadLocation()
	 * @generated
	 * @ordered
	 */
	protected String messagePayloadLocation = MESSAGE_PAYLOAD_LOCATION_EDEFAULT;

	/**
	 * This is true if the Message Payload Location attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean messagePayloadLocationESet;

	/**
	 * The default value of the '{@link #isRouteBefore() <em>Route Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRouteBefore()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ROUTE_BEFORE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRouteBefore() <em>Route Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRouteBefore()
	 * @generated
	 * @ordered
	 */
	protected boolean routeBefore = ROUTE_BEFORE_EDEFAULT;

	/**
	 * This is true if the Route Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean routeBeforeESet;

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
	 * The default value of the '{@link #getToServiceCategory() <em>To Service Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToServiceCategory()
	 * @generated
	 * @ordered
	 */
	protected static final String TO_SERVICE_CATEGORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getToServiceCategory() <em>To Service Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToServiceCategory()
	 * @generated
	 * @ordered
	 */
	protected String toServiceCategory = TO_SERVICE_CATEGORY_EDEFAULT;

	/**
	 * The default value of the '{@link #getToServiceName() <em>To Service Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToServiceName()
	 * @generated
	 * @ordered
	 */
	protected static final String TO_SERVICE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getToServiceName() <em>To Service Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToServiceName()
	 * @generated
	 * @ordered
	 */
	protected String toServiceName = TO_SERVICE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RouteBeanImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EsbroutingPackage.Literals.ROUTE_BEAN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeanIdRef() {
		return beanIdRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanIdRef(String newBeanIdRef) {
		String oldBeanIdRef = beanIdRef;
		beanIdRef = newBeanIdRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsbroutingPackage.ROUTE_BEAN__BEAN_ID_REF, oldBeanIdRef, beanIdRef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMessagePayloadLocation() {
		return messagePayloadLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMessagePayloadLocation(String newMessagePayloadLocation) {
		String oldMessagePayloadLocation = messagePayloadLocation;
		messagePayloadLocation = newMessagePayloadLocation;
		boolean oldMessagePayloadLocationESet = messagePayloadLocationESet;
		messagePayloadLocationESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsbroutingPackage.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION, oldMessagePayloadLocation, messagePayloadLocation, !oldMessagePayloadLocationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetMessagePayloadLocation() {
		String oldMessagePayloadLocation = messagePayloadLocation;
		boolean oldMessagePayloadLocationESet = messagePayloadLocationESet;
		messagePayloadLocation = MESSAGE_PAYLOAD_LOCATION_EDEFAULT;
		messagePayloadLocationESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, EsbroutingPackage.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION, oldMessagePayloadLocation, MESSAGE_PAYLOAD_LOCATION_EDEFAULT, oldMessagePayloadLocationESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetMessagePayloadLocation() {
		return messagePayloadLocationESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRouteBefore() {
		return routeBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRouteBefore(boolean newRouteBefore) {
		boolean oldRouteBefore = routeBefore;
		routeBefore = newRouteBefore;
		boolean oldRouteBeforeESet = routeBeforeESet;
		routeBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsbroutingPackage.ROUTE_BEAN__ROUTE_BEFORE, oldRouteBefore, routeBefore, !oldRouteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetRouteBefore() {
		boolean oldRouteBefore = routeBefore;
		boolean oldRouteBeforeESet = routeBeforeESet;
		routeBefore = ROUTE_BEFORE_EDEFAULT;
		routeBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, EsbroutingPackage.ROUTE_BEAN__ROUTE_BEFORE, oldRouteBefore, ROUTE_BEFORE_EDEFAULT, oldRouteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetRouteBefore() {
		return routeBeforeESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT, oldRouteOnElement, routeOnElement));
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
			eNotify(new ENotificationImpl(this, Notification.SET, EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT_NS, oldRouteOnElementNS, routeOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getToServiceCategory() {
		return toServiceCategory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setToServiceCategory(String newToServiceCategory) {
		String oldToServiceCategory = toServiceCategory;
		toServiceCategory = newToServiceCategory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_CATEGORY, oldToServiceCategory, toServiceCategory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getToServiceName() {
		return toServiceName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setToServiceName(String newToServiceName) {
		String oldToServiceName = toServiceName;
		toServiceName = newToServiceName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_NAME, oldToServiceName, toServiceName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EsbroutingPackage.ROUTE_BEAN__BEAN_ID_REF:
				return getBeanIdRef();
			case EsbroutingPackage.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION:
				return getMessagePayloadLocation();
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_BEFORE:
				return isRouteBefore() ? Boolean.TRUE : Boolean.FALSE;
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT:
				return getRouteOnElement();
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT_NS:
				return getRouteOnElementNS();
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_CATEGORY:
				return getToServiceCategory();
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_NAME:
				return getToServiceName();
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
			case EsbroutingPackage.ROUTE_BEAN__BEAN_ID_REF:
				setBeanIdRef((String)newValue);
				return;
			case EsbroutingPackage.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION:
				setMessagePayloadLocation((String)newValue);
				return;
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_BEFORE:
				setRouteBefore(((Boolean)newValue).booleanValue());
				return;
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT:
				setRouteOnElement((String)newValue);
				return;
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT_NS:
				setRouteOnElementNS((String)newValue);
				return;
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_CATEGORY:
				setToServiceCategory((String)newValue);
				return;
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_NAME:
				setToServiceName((String)newValue);
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
			case EsbroutingPackage.ROUTE_BEAN__BEAN_ID_REF:
				setBeanIdRef(BEAN_ID_REF_EDEFAULT);
				return;
			case EsbroutingPackage.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION:
				unsetMessagePayloadLocation();
				return;
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_BEFORE:
				unsetRouteBefore();
				return;
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT:
				setRouteOnElement(ROUTE_ON_ELEMENT_EDEFAULT);
				return;
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT_NS:
				setRouteOnElementNS(ROUTE_ON_ELEMENT_NS_EDEFAULT);
				return;
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_CATEGORY:
				setToServiceCategory(TO_SERVICE_CATEGORY_EDEFAULT);
				return;
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_NAME:
				setToServiceName(TO_SERVICE_NAME_EDEFAULT);
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
			case EsbroutingPackage.ROUTE_BEAN__BEAN_ID_REF:
				return BEAN_ID_REF_EDEFAULT == null ? beanIdRef != null : !BEAN_ID_REF_EDEFAULT.equals(beanIdRef);
			case EsbroutingPackage.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION:
				return isSetMessagePayloadLocation();
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_BEFORE:
				return isSetRouteBefore();
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT:
				return ROUTE_ON_ELEMENT_EDEFAULT == null ? routeOnElement != null : !ROUTE_ON_ELEMENT_EDEFAULT.equals(routeOnElement);
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT_NS:
				return ROUTE_ON_ELEMENT_NS_EDEFAULT == null ? routeOnElementNS != null : !ROUTE_ON_ELEMENT_NS_EDEFAULT.equals(routeOnElementNS);
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_CATEGORY:
				return TO_SERVICE_CATEGORY_EDEFAULT == null ? toServiceCategory != null : !TO_SERVICE_CATEGORY_EDEFAULT.equals(toServiceCategory);
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_NAME:
				return TO_SERVICE_NAME_EDEFAULT == null ? toServiceName != null : !TO_SERVICE_NAME_EDEFAULT.equals(toServiceName);
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
		result.append(" (beanIdRef: "); //$NON-NLS-1$
		result.append(beanIdRef);
		result.append(", messagePayloadLocation: "); //$NON-NLS-1$
		if (messagePayloadLocationESet) result.append(messagePayloadLocation); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", routeBefore: "); //$NON-NLS-1$
		if (routeBeforeESet) result.append(routeBefore); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", routeOnElement: "); //$NON-NLS-1$
		result.append(routeOnElement);
		result.append(", routeOnElementNS: "); //$NON-NLS-1$
		result.append(routeOnElementNS);
		result.append(", toServiceCategory: "); //$NON-NLS-1$
		result.append(toServiceCategory);
		result.append(", toServiceName: "); //$NON-NLS-1$
		result.append(toServiceName);
		result.append(')');
		return result.toString();
	}

} //RouteBeanImpl
