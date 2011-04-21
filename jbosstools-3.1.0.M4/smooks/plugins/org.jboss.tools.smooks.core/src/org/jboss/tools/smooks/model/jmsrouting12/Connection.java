/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12;

import org.jboss.tools.smooks.model.common.AbstractAnyType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				The JMS connection configuration.
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getFactory <em>Factory</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getSecurityCredential <em>Security Credential</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getSecurityPrincipal <em>Security Principal</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getConnection()
 * @model extendedMetaData="name='connection' kind='empty'"
 * @generated
 */
public interface Connection extends AbstractAnyType {
	/**
	 * Returns the value of the '<em><b>Factory</b></em>' attribute.
	 * The default value is <code>"ConnectionFactory"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The ConnectionFactory to look up
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Factory</em>' attribute.
	 * @see #isSetFactory()
	 * @see #unsetFactory()
	 * @see #setFactory(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getConnection_Factory()
	 * @model default="ConnectionFactory" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='factory'"
	 * @generated
	 */
	String getFactory();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getFactory <em>Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Factory</em>' attribute.
	 * @see #isSetFactory()
	 * @see #unsetFactory()
	 * @see #getFactory()
	 * @generated
	 */
	void setFactory(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getFactory <em>Factory</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFactory()
	 * @see #getFactory()
	 * @see #setFactory(String)
	 * @generated
	 */
	void unsetFactory();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getFactory <em>Factory</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Factory</em>' attribute is set.
	 * @see #unsetFactory()
	 * @see #getFactory()
	 * @see #setFactory(String)
	 * @generated
	 */
	boolean isSetFactory();

	/**
	 * Returns the value of the '<em><b>Security Credential</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The security credentials to use when creating the JMS connection.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Security Credential</em>' attribute.
	 * @see #setSecurityCredential(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getConnection_SecurityCredential()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='securityCredential'"
	 * @generated
	 */
	String getSecurityCredential();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getSecurityCredential <em>Security Credential</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Security Credential</em>' attribute.
	 * @see #getSecurityCredential()
	 * @generated
	 */
	void setSecurityCredential(String value);

	/**
	 * Returns the value of the '<em><b>Security Principal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The security principal use when creating the JMS connection.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Security Principal</em>' attribute.
	 * @see #setSecurityPrincipal(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getConnection_SecurityPrincipal()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='securityPrincipal'"
	 * @generated
	 */
	String getSecurityPrincipal();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Connection#getSecurityPrincipal <em>Security Principal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Security Principal</em>' attribute.
	 * @see #getSecurityPrincipal()
	 * @generated
	 */
	void setSecurityPrincipal(String value);

} // Connection
