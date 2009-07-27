/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12.impl;


import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.jmsrouting12.Connection;
import org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark;
import org.jboss.tools.smooks.model.jmsrouting12.JMS12Router;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package;
import org.jboss.tools.smooks.model.jmsrouting12.Jndi;
import org.jboss.tools.smooks.model.jmsrouting12.Message;
import org.jboss.tools.smooks.model.jmsrouting12.Session;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>JMS12 Router</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#getMessage <em>Message</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#getSession <em>Session</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#getJndi <em>Jndi</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#getHighWaterMark <em>High Water Mark</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#getDestination <em>Destination</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#getRouteOnElement <em>Route On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JMS12RouterImpl#getRouteOnElementNS <em>Route On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JMS12RouterImpl extends ElementVisitorImpl implements JMS12Router {
	/**
	 * The cached value of the '{@link #getMessage() <em>Message</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMessage()
	 * @generated
	 * @ordered
	 */
	protected Message message;

	/**
	 * The cached value of the '{@link #getConnection() <em>Connection</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConnection()
	 * @generated
	 * @ordered
	 */
	protected Connection connection;

	/**
	 * The cached value of the '{@link #getSession() <em>Session</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSession()
	 * @generated
	 * @ordered
	 */
	protected Session session;

	/**
	 * The cached value of the '{@link #getJndi() <em>Jndi</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJndi()
	 * @generated
	 * @ordered
	 */
	protected Jndi jndi;

	/**
	 * The cached value of the '{@link #getHighWaterMark() <em>High Water Mark</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHighWaterMark()
	 * @generated
	 * @ordered
	 */
	protected HighWaterMark highWaterMark;

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
	 * The default value of the '{@link #getDestination() <em>Destination</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestination()
	 * @generated
	 * @ordered
	 */
	protected static final String DESTINATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDestination() <em>Destination</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestination()
	 * @generated
	 * @ordered
	 */
	protected String destination = DESTINATION_EDEFAULT;

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
	protected JMS12RouterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Jmsrouting12Package.Literals.JMS12_ROUTER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Message getMessage() {
		return message;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMessage(Message newMessage, NotificationChain msgs) {
		Message oldMessage = message;
		message = newMessage;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__MESSAGE, oldMessage, newMessage);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMessage(Message newMessage) {
		if (newMessage != message) {
			NotificationChain msgs = null;
			if (message != null)
				msgs = ((InternalEObject)message).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__MESSAGE, null, msgs);
			if (newMessage != null)
				msgs = ((InternalEObject)newMessage).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__MESSAGE, null, msgs);
			msgs = basicSetMessage(newMessage, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__MESSAGE, newMessage, newMessage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConnection(Connection newConnection, NotificationChain msgs) {
		Connection oldConnection = connection;
		connection = newConnection;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__CONNECTION, oldConnection, newConnection);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConnection(Connection newConnection) {
		if (newConnection != connection) {
			NotificationChain msgs = null;
			if (connection != null)
				msgs = ((InternalEObject)connection).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__CONNECTION, null, msgs);
			if (newConnection != null)
				msgs = ((InternalEObject)newConnection).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__CONNECTION, null, msgs);
			msgs = basicSetConnection(newConnection, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__CONNECTION, newConnection, newConnection));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSession(Session newSession, NotificationChain msgs) {
		Session oldSession = session;
		session = newSession;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__SESSION, oldSession, newSession);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSession(Session newSession) {
		if (newSession != session) {
			NotificationChain msgs = null;
			if (session != null)
				msgs = ((InternalEObject)session).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__SESSION, null, msgs);
			if (newSession != null)
				msgs = ((InternalEObject)newSession).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__SESSION, null, msgs);
			msgs = basicSetSession(newSession, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__SESSION, newSession, newSession));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Jndi getJndi() {
		return jndi;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetJndi(Jndi newJndi, NotificationChain msgs) {
		Jndi oldJndi = jndi;
		jndi = newJndi;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__JNDI, oldJndi, newJndi);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJndi(Jndi newJndi) {
		if (newJndi != jndi) {
			NotificationChain msgs = null;
			if (jndi != null)
				msgs = ((InternalEObject)jndi).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__JNDI, null, msgs);
			if (newJndi != null)
				msgs = ((InternalEObject)newJndi).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__JNDI, null, msgs);
			msgs = basicSetJndi(newJndi, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__JNDI, newJndi, newJndi));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighWaterMark getHighWaterMark() {
		return highWaterMark;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHighWaterMark(HighWaterMark newHighWaterMark, NotificationChain msgs) {
		HighWaterMark oldHighWaterMark = highWaterMark;
		highWaterMark = newHighWaterMark;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK, oldHighWaterMark, newHighWaterMark);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHighWaterMark(HighWaterMark newHighWaterMark) {
		if (newHighWaterMark != highWaterMark) {
			NotificationChain msgs = null;
			if (highWaterMark != null)
				msgs = ((InternalEObject)highWaterMark).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK, null, msgs);
			if (newHighWaterMark != null)
				msgs = ((InternalEObject)newHighWaterMark).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK, null, msgs);
			msgs = basicSetHighWaterMark(newHighWaterMark, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK, newHighWaterMark, newHighWaterMark));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__BEAN_ID, oldBeanId, beanId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDestination(String newDestination) {
		String oldDestination = destination;
		destination = newDestination;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__DESTINATION, oldDestination, destination));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__EXECUTE_BEFORE, oldExecuteBefore, executeBefore, !oldExecuteBeforeESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, Jmsrouting12Package.JMS12_ROUTER__EXECUTE_BEFORE, oldExecuteBefore, EXECUTE_BEFORE_EDEFAULT, oldExecuteBeforeESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT, oldRouteOnElement, routeOnElement));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT_NS, oldRouteOnElementNS, routeOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Jmsrouting12Package.JMS12_ROUTER__MESSAGE:
				return basicSetMessage(null, msgs);
			case Jmsrouting12Package.JMS12_ROUTER__CONNECTION:
				return basicSetConnection(null, msgs);
			case Jmsrouting12Package.JMS12_ROUTER__SESSION:
				return basicSetSession(null, msgs);
			case Jmsrouting12Package.JMS12_ROUTER__JNDI:
				return basicSetJndi(null, msgs);
			case Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK:
				return basicSetHighWaterMark(null, msgs);
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
			case Jmsrouting12Package.JMS12_ROUTER__MESSAGE:
				return getMessage();
			case Jmsrouting12Package.JMS12_ROUTER__CONNECTION:
				return getConnection();
			case Jmsrouting12Package.JMS12_ROUTER__SESSION:
				return getSession();
			case Jmsrouting12Package.JMS12_ROUTER__JNDI:
				return getJndi();
			case Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK:
				return getHighWaterMark();
			case Jmsrouting12Package.JMS12_ROUTER__BEAN_ID:
				return getBeanId();
			case Jmsrouting12Package.JMS12_ROUTER__DESTINATION:
				return getDestination();
			case Jmsrouting12Package.JMS12_ROUTER__EXECUTE_BEFORE:
				return isExecuteBefore() ? Boolean.TRUE : Boolean.FALSE;
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT:
				return getRouteOnElement();
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT_NS:
				return getRouteOnElementNS();
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
			case Jmsrouting12Package.JMS12_ROUTER__MESSAGE:
				setMessage((Message)newValue);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__CONNECTION:
				setConnection((Connection)newValue);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__SESSION:
				setSession((Session)newValue);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__JNDI:
				setJndi((Jndi)newValue);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK:
				setHighWaterMark((HighWaterMark)newValue);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__BEAN_ID:
				setBeanId((String)newValue);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__DESTINATION:
				setDestination((String)newValue);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__EXECUTE_BEFORE:
				setExecuteBefore(((Boolean)newValue).booleanValue());
				return;
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT:
				setRouteOnElement((String)newValue);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT_NS:
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
	public void eUnset(int featureID) {
		switch (featureID) {
			case Jmsrouting12Package.JMS12_ROUTER__MESSAGE:
				setMessage((Message)null);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__CONNECTION:
				setConnection((Connection)null);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__SESSION:
				setSession((Session)null);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__JNDI:
				setJndi((Jndi)null);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK:
				setHighWaterMark((HighWaterMark)null);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__BEAN_ID:
				setBeanId(BEAN_ID_EDEFAULT);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__DESTINATION:
				setDestination(DESTINATION_EDEFAULT);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__EXECUTE_BEFORE:
				unsetExecuteBefore();
				return;
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT:
				setRouteOnElement(ROUTE_ON_ELEMENT_EDEFAULT);
				return;
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT_NS:
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
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Jmsrouting12Package.JMS12_ROUTER__MESSAGE:
				return message != null;
			case Jmsrouting12Package.JMS12_ROUTER__CONNECTION:
				return connection != null;
			case Jmsrouting12Package.JMS12_ROUTER__SESSION:
				return session != null;
			case Jmsrouting12Package.JMS12_ROUTER__JNDI:
				return jndi != null;
			case Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK:
				return highWaterMark != null;
			case Jmsrouting12Package.JMS12_ROUTER__BEAN_ID:
				return BEAN_ID_EDEFAULT == null ? beanId != null : !BEAN_ID_EDEFAULT.equals(beanId);
			case Jmsrouting12Package.JMS12_ROUTER__DESTINATION:
				return DESTINATION_EDEFAULT == null ? destination != null : !DESTINATION_EDEFAULT.equals(destination);
			case Jmsrouting12Package.JMS12_ROUTER__EXECUTE_BEFORE:
				return isSetExecuteBefore();
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT:
				return ROUTE_ON_ELEMENT_EDEFAULT == null ? routeOnElement != null : !ROUTE_ON_ELEMENT_EDEFAULT.equals(routeOnElement);
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT_NS:
				return ROUTE_ON_ELEMENT_NS_EDEFAULT == null ? routeOnElementNS != null : !ROUTE_ON_ELEMENT_NS_EDEFAULT.equals(routeOnElementNS);
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
		result.append(", destination: ");
		result.append(destination);
		result.append(", executeBefore: ");
		if (executeBeforeESet) result.append(executeBefore); else result.append("<unset>");
		result.append(", routeOnElement: ");
		result.append(routeOnElement);
		result.append(", routeOnElementNS: ");
		result.append(routeOnElementNS);
		result.append(')');
		return result.toString();
	}

} //JMS12RouterImpl
