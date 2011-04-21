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
import org.jboss.tools.smooks.model.jmsrouting.Connection;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.impl.ConnectionImpl#getFactory <em>Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.impl.ConnectionImpl#getSecurityCredential <em>Security Credential</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.impl.ConnectionImpl#getSecurityPrincipal <em>Security Principal</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConnectionImpl extends EObjectImpl implements Connection {
	/**
	 * The default value of the '{@link #getFactory() <em>Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFactory()
	 * @generated
	 * @ordered
	 */
	protected static final String FACTORY_EDEFAULT = "ConnectionFactory"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getFactory() <em>Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFactory()
	 * @generated
	 * @ordered
	 */
	protected String factory = FACTORY_EDEFAULT;

	/**
	 * This is true if the Factory attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean factoryESet;

	/**
	 * The default value of the '{@link #getSecurityCredential() <em>Security Credential</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecurityCredential()
	 * @generated
	 * @ordered
	 */
	protected static final String SECURITY_CREDENTIAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSecurityCredential() <em>Security Credential</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecurityCredential()
	 * @generated
	 * @ordered
	 */
	protected String securityCredential = SECURITY_CREDENTIAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getSecurityPrincipal() <em>Security Principal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecurityPrincipal()
	 * @generated
	 * @ordered
	 */
	protected static final String SECURITY_PRINCIPAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSecurityPrincipal() <em>Security Principal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSecurityPrincipal()
	 * @generated
	 * @ordered
	 */
	protected String securityPrincipal = SECURITY_PRINCIPAL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return JmsroutingPackage.Literals.CONNECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFactory() {
		return factory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFactory(String newFactory) {
		String oldFactory = factory;
		factory = newFactory;
		boolean oldFactoryESet = factoryESet;
		factoryESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JmsroutingPackage.CONNECTION__FACTORY, oldFactory, factory, !oldFactoryESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetFactory() {
		String oldFactory = factory;
		boolean oldFactoryESet = factoryESet;
		factory = FACTORY_EDEFAULT;
		factoryESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, JmsroutingPackage.CONNECTION__FACTORY, oldFactory, FACTORY_EDEFAULT, oldFactoryESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetFactory() {
		return factoryESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSecurityCredential() {
		return securityCredential;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSecurityCredential(String newSecurityCredential) {
		String oldSecurityCredential = securityCredential;
		securityCredential = newSecurityCredential;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JmsroutingPackage.CONNECTION__SECURITY_CREDENTIAL, oldSecurityCredential, securityCredential));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSecurityPrincipal() {
		return securityPrincipal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSecurityPrincipal(String newSecurityPrincipal) {
		String oldSecurityPrincipal = securityPrincipal;
		securityPrincipal = newSecurityPrincipal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, JmsroutingPackage.CONNECTION__SECURITY_PRINCIPAL, oldSecurityPrincipal, securityPrincipal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case JmsroutingPackage.CONNECTION__FACTORY:
				return getFactory();
			case JmsroutingPackage.CONNECTION__SECURITY_CREDENTIAL:
				return getSecurityCredential();
			case JmsroutingPackage.CONNECTION__SECURITY_PRINCIPAL:
				return getSecurityPrincipal();
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
			case JmsroutingPackage.CONNECTION__FACTORY:
				setFactory((String)newValue);
				return;
			case JmsroutingPackage.CONNECTION__SECURITY_CREDENTIAL:
				setSecurityCredential((String)newValue);
				return;
			case JmsroutingPackage.CONNECTION__SECURITY_PRINCIPAL:
				setSecurityPrincipal((String)newValue);
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
			case JmsroutingPackage.CONNECTION__FACTORY:
				unsetFactory();
				return;
			case JmsroutingPackage.CONNECTION__SECURITY_CREDENTIAL:
				setSecurityCredential(SECURITY_CREDENTIAL_EDEFAULT);
				return;
			case JmsroutingPackage.CONNECTION__SECURITY_PRINCIPAL:
				setSecurityPrincipal(SECURITY_PRINCIPAL_EDEFAULT);
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
			case JmsroutingPackage.CONNECTION__FACTORY:
				return isSetFactory();
			case JmsroutingPackage.CONNECTION__SECURITY_CREDENTIAL:
				return SECURITY_CREDENTIAL_EDEFAULT == null ? securityCredential != null : !SECURITY_CREDENTIAL_EDEFAULT.equals(securityCredential);
			case JmsroutingPackage.CONNECTION__SECURITY_PRINCIPAL:
				return SECURITY_PRINCIPAL_EDEFAULT == null ? securityPrincipal != null : !SECURITY_PRINCIPAL_EDEFAULT.equals(securityPrincipal);
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
		result.append(" (factory: "); //$NON-NLS-1$
		if (factoryESet) result.append(factory); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", securityCredential: "); //$NON-NLS-1$
		result.append(securityCredential);
		result.append(", securityPrincipal: "); //$NON-NLS-1$
		result.append(securityPrincipal);
		result.append(')');
		return result.toString();
	}

} //ConnectionImpl
