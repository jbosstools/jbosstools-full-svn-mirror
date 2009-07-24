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
 * A representation of the model object '<em><b>Session</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				The JMS session configuration.
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Session#getAcknowledgeMode <em>Acknowledge Mode</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Session#isTransacted <em>Transacted</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getSession()
 * @model extendedMetaData="name='session' kind='empty'"
 * @generated
 */
public interface Session extends EObject {
	/**
	 * Returns the value of the '<em><b>Acknowledge Mode</b></em>' attribute.
	 * The default value is <code>"AUTO_ACKNOWLEDGE"</code>.
	 * The literals are from the enumeration {@link org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The acknowledge mode to use. One of 'AUTO_ACKNOWLEDGE'(default), 'CLIENT_ACKNOWLEDGE', 'DUPS_OK_ACKNOWLEDGE'.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Acknowledge Mode</em>' attribute.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode
	 * @see #isSetAcknowledgeMode()
	 * @see #unsetAcknowledgeMode()
	 * @see #setAcknowledgeMode(AcknowledgeMode)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getSession_AcknowledgeMode()
	 * @model default="AUTO_ACKNOWLEDGE" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='acknowledgeMode'"
	 * @generated
	 */
	AcknowledgeMode getAcknowledgeMode();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Session#getAcknowledgeMode <em>Acknowledge Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Acknowledge Mode</em>' attribute.
	 * @see org.jboss.tools.smooks.model.jmsrouting12.AcknowledgeMode
	 * @see #isSetAcknowledgeMode()
	 * @see #unsetAcknowledgeMode()
	 * @see #getAcknowledgeMode()
	 * @generated
	 */
	void setAcknowledgeMode(AcknowledgeMode value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Session#getAcknowledgeMode <em>Acknowledge Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAcknowledgeMode()
	 * @see #getAcknowledgeMode()
	 * @see #setAcknowledgeMode(AcknowledgeMode)
	 * @generated
	 */
	void unsetAcknowledgeMode();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Session#getAcknowledgeMode <em>Acknowledge Mode</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Acknowledge Mode</em>' attribute is set.
	 * @see #unsetAcknowledgeMode()
	 * @see #getAcknowledgeMode()
	 * @see #setAcknowledgeMode(AcknowledgeMode)
	 * @generated
	 */
	boolean isSetAcknowledgeMode();

	/**
	 * Returns the value of the '<em><b>Transacted</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Determines if the session should be transacted. Defaults to 'false'.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transacted</em>' attribute.
	 * @see #isSetTransacted()
	 * @see #unsetTransacted()
	 * @see #setTransacted(boolean)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getSession_Transacted()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='transacted'"
	 * @generated
	 */
	boolean isTransacted();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Session#isTransacted <em>Transacted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transacted</em>' attribute.
	 * @see #isSetTransacted()
	 * @see #unsetTransacted()
	 * @see #isTransacted()
	 * @generated
	 */
	void setTransacted(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Session#isTransacted <em>Transacted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTransacted()
	 * @see #isTransacted()
	 * @see #setTransacted(boolean)
	 * @generated
	 */
	void unsetTransacted();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Session#isTransacted <em>Transacted</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Transacted</em>' attribute is set.
	 * @see #unsetTransacted()
	 * @see #isTransacted()
	 * @see #setTransacted(boolean)
	 * @generated
	 */
	boolean isSetTransacted();

} // Session
