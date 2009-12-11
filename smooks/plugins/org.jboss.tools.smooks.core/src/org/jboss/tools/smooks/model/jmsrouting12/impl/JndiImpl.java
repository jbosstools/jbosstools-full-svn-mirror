/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12.impl;



import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.common.impl.AbstractAnyTypeImpl;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package;
import org.jboss.tools.smooks.model.jmsrouting12.Jndi;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Jndi</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JndiImpl#getContextFactory <em>Context Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JndiImpl#getNamingFactory <em>Naming Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JndiImpl#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.impl.JndiImpl#getProviderUrl <em>Provider Url</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JndiImpl extends AbstractAnyTypeImpl implements Jndi {
	/**
	 * The default value of the '{@link #getContextFactory() <em>Context Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextFactory()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTEXT_FACTORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContextFactory() <em>Context Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextFactory()
	 * @generated
	 * @ordered
	 */
	protected String contextFactory = CONTEXT_FACTORY_EDEFAULT;

	/**
	 * The default value of the '{@link #getNamingFactory() <em>Naming Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamingFactory()
	 * @generated
	 * @ordered
	 */
	protected static final String NAMING_FACTORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNamingFactory() <em>Naming Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamingFactory()
	 * @generated
	 * @ordered
	 */
	protected String namingFactory = NAMING_FACTORY_EDEFAULT;

	/**
	 * The default value of the '{@link #getProperties() <em>Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected static final String PROPERTIES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected String properties = PROPERTIES_EDEFAULT;

	/**
	 * The default value of the '{@link #getProviderUrl() <em>Provider Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProviderUrl()
	 * @generated
	 * @ordered
	 */
	protected static final String PROVIDER_URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProviderUrl() <em>Provider Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProviderUrl()
	 * @generated
	 * @ordered
	 */
	protected String providerUrl = PROVIDER_URL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JndiImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Jmsrouting12Package.Literals.JNDI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getContextFactory() {
		return contextFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setContextFactory(String newContextFactory) {
		String oldContextFactory = contextFactory;
		contextFactory = newContextFactory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JNDI__CONTEXT_FACTORY, oldContextFactory, contextFactory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNamingFactory() {
		return namingFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNamingFactory(String newNamingFactory) {
		String oldNamingFactory = namingFactory;
		namingFactory = newNamingFactory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JNDI__NAMING_FACTORY, oldNamingFactory, namingFactory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProperties() {
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProperties(String newProperties) {
		String oldProperties = properties;
		properties = newProperties;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JNDI__PROPERTIES, oldProperties, properties));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProviderUrl() {
		return providerUrl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProviderUrl(String newProviderUrl) {
		String oldProviderUrl = providerUrl;
		providerUrl = newProviderUrl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Jmsrouting12Package.JNDI__PROVIDER_URL, oldProviderUrl, providerUrl));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Jmsrouting12Package.JNDI__CONTEXT_FACTORY:
				return getContextFactory();
			case Jmsrouting12Package.JNDI__NAMING_FACTORY:
				return getNamingFactory();
			case Jmsrouting12Package.JNDI__PROPERTIES:
				return getProperties();
			case Jmsrouting12Package.JNDI__PROVIDER_URL:
				return getProviderUrl();
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
			case Jmsrouting12Package.JNDI__CONTEXT_FACTORY:
				setContextFactory((String)newValue);
				return;
			case Jmsrouting12Package.JNDI__NAMING_FACTORY:
				setNamingFactory((String)newValue);
				return;
			case Jmsrouting12Package.JNDI__PROPERTIES:
				setProperties((String)newValue);
				return;
			case Jmsrouting12Package.JNDI__PROVIDER_URL:
				setProviderUrl((String)newValue);
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
			case Jmsrouting12Package.JNDI__CONTEXT_FACTORY:
				setContextFactory(CONTEXT_FACTORY_EDEFAULT);
				return;
			case Jmsrouting12Package.JNDI__NAMING_FACTORY:
				setNamingFactory(NAMING_FACTORY_EDEFAULT);
				return;
			case Jmsrouting12Package.JNDI__PROPERTIES:
				setProperties(PROPERTIES_EDEFAULT);
				return;
			case Jmsrouting12Package.JNDI__PROVIDER_URL:
				setProviderUrl(PROVIDER_URL_EDEFAULT);
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
			case Jmsrouting12Package.JNDI__CONTEXT_FACTORY:
				return CONTEXT_FACTORY_EDEFAULT == null ? contextFactory != null : !CONTEXT_FACTORY_EDEFAULT.equals(contextFactory);
			case Jmsrouting12Package.JNDI__NAMING_FACTORY:
				return NAMING_FACTORY_EDEFAULT == null ? namingFactory != null : !NAMING_FACTORY_EDEFAULT.equals(namingFactory);
			case Jmsrouting12Package.JNDI__PROPERTIES:
				return PROPERTIES_EDEFAULT == null ? properties != null : !PROPERTIES_EDEFAULT.equals(properties);
			case Jmsrouting12Package.JNDI__PROVIDER_URL:
				return PROVIDER_URL_EDEFAULT == null ? providerUrl != null : !PROVIDER_URL_EDEFAULT.equals(providerUrl);
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
		result.append(" (contextFactory: "); //$NON-NLS-1$
		result.append(contextFactory);
		result.append(", namingFactory: "); //$NON-NLS-1$
		result.append(namingFactory);
		result.append(", properties: "); //$NON-NLS-1$
		result.append(properties);
		result.append(", providerUrl: "); //$NON-NLS-1$
		result.append(providerUrl);
		result.append(')');
		return result.toString();
	}

} //JndiImpl
