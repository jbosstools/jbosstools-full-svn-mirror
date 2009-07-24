/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12;

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
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getContextFactory <em>Context Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getNamingFactory <em>Naming Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getProperties <em>Properties</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getProviderUrl <em>Provider Url</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJndi()
 * @model extendedMetaData="name='jndi' kind='empty'"
 * @generated
 */
public interface Jndi extends EObject {
	/**
	 * Returns the value of the '<em><b>Context Factory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The JNDI ContextFactory to use
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Context Factory</em>' attribute.
	 * @see #setContextFactory(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJndi_ContextFactory()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='contextFactory'"
	 * @generated
	 */
	String getContextFactory();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getContextFactory <em>Context Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context Factory</em>' attribute.
	 * @see #getContextFactory()
	 * @generated
	 */
	void setContextFactory(String value);

	/**
	 * Returns the value of the '<em><b>Naming Factory</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The JNDI NamingFactory to use
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Naming Factory</em>' attribute.
	 * @see #setNamingFactory(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJndi_NamingFactory()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='namingFactory'"
	 * @generated
	 */
	String getNamingFactory();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getNamingFactory <em>Naming Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Naming Factory</em>' attribute.
	 * @see #getNamingFactory()
	 * @generated
	 */
	void setNamingFactory(String value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			        A .properties file containing the JNDI properties to be used for JMS resource
	 *                     lookups.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Properties</em>' attribute.
	 * @see #setProperties(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJndi_Properties()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='properties'"
	 * @generated
	 */
	String getProperties();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getProperties <em>Properties</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Properties</em>' attribute.
	 * @see #getProperties()
	 * @generated
	 */
	void setProperties(String value);

	/**
	 * Returns the value of the '<em><b>Provider Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The JNDI Provider URL to use.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Provider Url</em>' attribute.
	 * @see #setProviderUrl(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJndi_ProviderUrl()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='providerUrl'"
	 * @generated
	 */
	String getProviderUrl();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Jndi#getProviderUrl <em>Provider Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provider Url</em>' attribute.
	 * @see #getProviderUrl()
	 * @generated
	 */
	void setProviderUrl(String value);

} // Jndi
