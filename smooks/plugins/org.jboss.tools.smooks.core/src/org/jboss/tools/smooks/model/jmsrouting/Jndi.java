/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Jndi</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				The JNDI configuration.
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getContextFactory <em>Context Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getNamingFactory <em>Naming Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getProviderUrl <em>Provider Url</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage#getJndi()
 * @model extendedMetaData="name='jndi' kind='empty'"
 * @generated
 */
public interface Jndi extends EObject {
	/**
	 * Returns the value of the '<em><b>Context Factory</b></em>' attribute.
	 * The default value is <code>"org.jnp.interfaces.NamingContextFactory"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The JNDI ContextFactory to use
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Context Factory</em>' attribute.
	 * @see #isSetContextFactory()
	 * @see #unsetContextFactory()
	 * @see #setContextFactory(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage#getJndi_ContextFactory()
	 * @model default="org.jnp.interfaces.NamingContextFactory" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='contextFactory'"
	 * @generated
	 */
	String getContextFactory();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getContextFactory <em>Context Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context Factory</em>' attribute.
	 * @see #isSetContextFactory()
	 * @see #unsetContextFactory()
	 * @see #getContextFactory()
	 * @generated
	 */
	void setContextFactory(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getContextFactory <em>Context Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetContextFactory()
	 * @see #getContextFactory()
	 * @see #setContextFactory(String)
	 * @generated
	 */
	void unsetContextFactory();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getContextFactory <em>Context Factory</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Context Factory</em>' attribute is set.
	 * @see #unsetContextFactory()
	 * @see #getContextFactory()
	 * @see #setContextFactory(String)
	 * @generated
	 */
	boolean isSetContextFactory();

	/**
	 * Returns the value of the '<em><b>Naming Factory</b></em>' attribute.
	 * The default value is <code>"org.jboss.naming:java.naming.factory.url.pkgs"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The JNDI NamingFactory to use
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Naming Factory</em>' attribute.
	 * @see #isSetNamingFactory()
	 * @see #unsetNamingFactory()
	 * @see #setNamingFactory(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage#getJndi_NamingFactory()
	 * @model default="org.jboss.naming:java.naming.factory.url.pkgs" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='namingFactory'"
	 * @generated
	 */
	String getNamingFactory();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getNamingFactory <em>Naming Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Naming Factory</em>' attribute.
	 * @see #isSetNamingFactory()
	 * @see #unsetNamingFactory()
	 * @see #getNamingFactory()
	 * @generated
	 */
	void setNamingFactory(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getNamingFactory <em>Naming Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetNamingFactory()
	 * @see #getNamingFactory()
	 * @see #setNamingFactory(String)
	 * @generated
	 */
	void unsetNamingFactory();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getNamingFactory <em>Naming Factory</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Naming Factory</em>' attribute is set.
	 * @see #unsetNamingFactory()
	 * @see #getNamingFactory()
	 * @see #setNamingFactory(String)
	 * @generated
	 */
	boolean isSetNamingFactory();

	/**
	 * Returns the value of the '<em><b>Provider Url</b></em>' attribute.
	 * The default value is <code>"jnp://localhost:1099"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The JNDI Provider URL to use.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Provider Url</em>' attribute.
	 * @see #isSetProviderUrl()
	 * @see #unsetProviderUrl()
	 * @see #setProviderUrl(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage#getJndi_ProviderUrl()
	 * @model default="jnp://localhost:1099" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='providerUrl'"
	 * @generated
	 */
	String getProviderUrl();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getProviderUrl <em>Provider Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provider Url</em>' attribute.
	 * @see #isSetProviderUrl()
	 * @see #unsetProviderUrl()
	 * @see #getProviderUrl()
	 * @generated
	 */
	void setProviderUrl(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getProviderUrl <em>Provider Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetProviderUrl()
	 * @see #getProviderUrl()
	 * @see #setProviderUrl(String)
	 * @generated
	 */
	void unsetProviderUrl();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi#getProviderUrl <em>Provider Url</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Provider Url</em>' attribute is set.
	 * @see #unsetProviderUrl()
	 * @see #getProviderUrl()
	 * @see #setProviderUrl(String)
	 * @generated
	 */
	boolean isSetProviderUrl();

} // Jndi
