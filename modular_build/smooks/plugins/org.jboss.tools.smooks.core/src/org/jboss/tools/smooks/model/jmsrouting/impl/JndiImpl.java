/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting.impl;


import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting.Jndi;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Jndi</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.impl.JndiImpl#getContextFactory <em>Context Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.impl.JndiImpl#getNamingFactory <em>Naming Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.impl.JndiImpl#getProviderUrl <em>Provider Url</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JndiImpl extends EObjectImpl implements Jndi {
	/**
	 * The default value of the '{@link #getContextFactory() <em>Context Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextFactory()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTEXT_FACTORY_EDEFAULT = "org.jnp.interfaces.NamingContextFactory"; //$NON-NLS-1$

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
	 * This is true if the Context Factory attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean contextFactoryESet;

	/**
	 * The default value of the '{@link #getNamingFactory() <em>Naming Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamingFactory()
	 * @generated
	 * @ordered
	 */
	protected static final String NAMING_FACTORY_EDEFAULT = "org.jboss.naming:java.naming.factory.url.pkgs"; //$NON-NLS-1$

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
	 * This is true if the Naming Factory attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean namingFactoryESet;

	/**
	 * The default value of the '{@link #getProviderUrl() <em>Provider Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProviderUrl()
	 * @generated
	 * @ordered
	 */
	protected static final String PROVIDER_URL_EDEFAULT = "jnp://localhost:1099"; //$NON-NLS-1$

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
	 * This is true if the Provider Url attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean providerUrlESet;

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
	@Override
	protected EClass eStaticClass() {
		return JmsroutingPackage.Literals.JNDI;
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
		boolean oldContextFactoryESet = contextFactoryESet;
		contextFactoryESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JmsroutingPackage.JNDI__CONTEXT_FACTORY, oldContextFactory, contextFactory, !oldContextFactoryESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetContextFactory() {
		String oldContextFactory = contextFactory;
		boolean oldContextFactoryESet = contextFactoryESet;
		contextFactory = CONTEXT_FACTORY_EDEFAULT;
		contextFactoryESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JmsroutingPackage.JNDI__CONTEXT_FACTORY, oldContextFactory, CONTEXT_FACTORY_EDEFAULT, oldContextFactoryESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetContextFactory() {
		return contextFactoryESet;
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
		boolean oldNamingFactoryESet = namingFactoryESet;
		namingFactoryESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JmsroutingPackage.JNDI__NAMING_FACTORY, oldNamingFactory, namingFactory, !oldNamingFactoryESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetNamingFactory() {
		String oldNamingFactory = namingFactory;
		boolean oldNamingFactoryESet = namingFactoryESet;
		namingFactory = NAMING_FACTORY_EDEFAULT;
		namingFactoryESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JmsroutingPackage.JNDI__NAMING_FACTORY, oldNamingFactory, NAMING_FACTORY_EDEFAULT, oldNamingFactoryESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetNamingFactory() {
		return namingFactoryESet;
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
		boolean oldProviderUrlESet = providerUrlESet;
		providerUrlESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JmsroutingPackage.JNDI__PROVIDER_URL, oldProviderUrl, providerUrl, !oldProviderUrlESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetProviderUrl() {
		String oldProviderUrl = providerUrl;
		boolean oldProviderUrlESet = providerUrlESet;
		providerUrl = PROVIDER_URL_EDEFAULT;
		providerUrlESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JmsroutingPackage.JNDI__PROVIDER_URL, oldProviderUrl, PROVIDER_URL_EDEFAULT, oldProviderUrlESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetProviderUrl() {
		return providerUrlESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JmsroutingPackage.JNDI__CONTEXT_FACTORY:
				return getContextFactory();
			case JmsroutingPackage.JNDI__NAMING_FACTORY:
				return getNamingFactory();
			case JmsroutingPackage.JNDI__PROVIDER_URL:
				return getProviderUrl();
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
			case JmsroutingPackage.JNDI__CONTEXT_FACTORY:
				setContextFactory((String)newValue);
				return;
			case JmsroutingPackage.JNDI__NAMING_FACTORY:
				setNamingFactory((String)newValue);
				return;
			case JmsroutingPackage.JNDI__PROVIDER_URL:
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
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case JmsroutingPackage.JNDI__CONTEXT_FACTORY:
				unsetContextFactory();
				return;
			case JmsroutingPackage.JNDI__NAMING_FACTORY:
				unsetNamingFactory();
				return;
			case JmsroutingPackage.JNDI__PROVIDER_URL:
				unsetProviderUrl();
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
			case JmsroutingPackage.JNDI__CONTEXT_FACTORY:
				return isSetContextFactory();
			case JmsroutingPackage.JNDI__NAMING_FACTORY:
				return isSetNamingFactory();
			case JmsroutingPackage.JNDI__PROVIDER_URL:
				return isSetProviderUrl();
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
		result.append(" (contextFactory: "); //$NON-NLS-1$
		if (contextFactoryESet) result.append(contextFactory); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", namingFactory: "); //$NON-NLS-1$
		if (namingFactoryESet) result.append(namingFactory); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", providerUrl: "); //$NON-NLS-1$
		if (providerUrlESet) result.append(providerUrl); else result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

} //JndiImpl
