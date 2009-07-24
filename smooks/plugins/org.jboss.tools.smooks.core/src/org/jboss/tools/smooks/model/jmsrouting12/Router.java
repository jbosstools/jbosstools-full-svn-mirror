/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12;

import org.jboss.tools.smooks.model.smooks.ElementVisitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Router</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *     			JMS Router
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getMessage <em>Message</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getSession <em>Session</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getJndi <em>Jndi</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getHighWaterMark <em>High Water Mark</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getDestination <em>Destination</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getRouteOnElement <em>Route On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getRouteOnElementNS <em>Route On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter()
 * @model extendedMetaData="name='router' kind='elementOnly'"
 * @generated
 */
public interface Router extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Message</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The message configuration.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Message</em>' containment reference.
	 * @see #setMessage(Message)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_Message()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='message' namespace='##targetNamespace'"
	 * @generated
	 */
	Message getMessage();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getMessage <em>Message</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Message</em>' containment reference.
	 * @see #getMessage()
	 * @generated
	 */
	void setMessage(Message value);

	/**
	 * Returns the value of the '<em><b>Connection</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The JMS connection configuration.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Connection</em>' containment reference.
	 * @see #setConnection(Connection)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_Connection()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='connection' namespace='##targetNamespace'"
	 * @generated
	 */
	Connection getConnection();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getConnection <em>Connection</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connection</em>' containment reference.
	 * @see #getConnection()
	 * @generated
	 */
	void setConnection(Connection value);

	/**
	 * Returns the value of the '<em><b>Session</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The JMS session configuration.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Session</em>' containment reference.
	 * @see #setSession(Session)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_Session()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='session' namespace='##targetNamespace'"
	 * @generated
	 */
	Session getSession();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getSession <em>Session</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Session</em>' containment reference.
	 * @see #getSession()
	 * @generated
	 */
	void setSession(Session value);

	/**
	 * Returns the value of the '<em><b>Jndi</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The JNDI configuration.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Jndi</em>' containment reference.
	 * @see #setJndi(Jndi)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_Jndi()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='jndi' namespace='##targetNamespace'"
	 * @generated
	 */
	Jndi getJndi();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getJndi <em>Jndi</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Jndi</em>' containment reference.
	 * @see #getJndi()
	 * @generated
	 */
	void setJndi(Jndi value);

	/**
	 * Returns the value of the '<em><b>High Water Mark</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The configuration for the max number of messages that can be sitting in the
	 * 								JMS Destination at any any time.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>High Water Mark</em>' containment reference.
	 * @see #setHighWaterMark(HighWaterMark)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_HighWaterMark()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='highWaterMark' namespace='##targetNamespace'"
	 * @generated
	 */
	HighWaterMark getHighWaterMark();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getHighWaterMark <em>High Water Mark</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>High Water Mark</em>' containment reference.
	 * @see #getHighWaterMark()
	 * @generated
	 */
	void setHighWaterMark(HighWaterMark value);

	/**
	 * Returns the value of the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							The beanId of the bean from the bean context to be used as message payload.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Id</em>' attribute.
	 * @see #setBeanId(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_BeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='beanId'"
	 * @generated
	 */
	String getBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getBeanId <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Id</em>' attribute.
	 * @see #getBeanId()
	 * @generated
	 */
	void setBeanId(String value);

	/**
	 * Returns the value of the '<em><b>Destination</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							The JMS destination string
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Destination</em>' attribute.
	 * @see #setDestination(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_Destination()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='destination'"
	 * @generated
	 */
	String getDestination();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getDestination <em>Destination</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination</em>' attribute.
	 * @see #getDestination()
	 * @generated
	 */
	void setDestination(String value);

	/**
	 * Returns the value of the '<em><b>Execute Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							If the routing is done before or after the selected element. Default is 'false'.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute Before</em>' attribute.
	 * @see #isSetExecuteBefore()
	 * @see #unsetExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_ExecuteBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='executeBefore'"
	 * @generated
	 */
	boolean isExecuteBefore();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#isExecuteBefore <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execute Before</em>' attribute.
	 * @see #isSetExecuteBefore()
	 * @see #unsetExecuteBefore()
	 * @see #isExecuteBefore()
	 * @generated
	 */
	void setExecuteBefore(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#isExecuteBefore <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetExecuteBefore()
	 * @see #isExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @generated
	 */
	void unsetExecuteBefore();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#isExecuteBefore <em>Execute Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Execute Before</em>' attribute is set.
	 * @see #unsetExecuteBefore()
	 * @see #isExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @generated
	 */
	boolean isSetExecuteBefore();

	/**
	 * Returns the value of the '<em><b>Route On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							The element to route on.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Route On Element</em>' attribute.
	 * @see #setRouteOnElement(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_RouteOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='routeOnElement'"
	 * @generated
	 */
	String getRouteOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getRouteOnElement <em>Route On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route On Element</em>' attribute.
	 * @see #getRouteOnElement()
	 * @generated
	 */
	void setRouteOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Route On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The namespace of the routeOnElement element.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Route On Element NS</em>' attribute.
	 * @see #setRouteOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getRouter_RouteOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='routeOnElementNS'"
	 * @generated
	 */
	String getRouteOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.Router#getRouteOnElementNS <em>Route On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route On Element NS</em>' attribute.
	 * @see #getRouteOnElementNS()
	 * @generated
	 */
	void setRouteOnElementNS(String value);

} // Router
