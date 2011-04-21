/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.esbrouting;

import org.jboss.tools.smooks.model.smooks.ElementVisitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Route Bean</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * JBoss ESB Async Router.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getBeanIdRef <em>Bean Id Ref</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getMessagePayloadLocation <em>Message Payload Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#isRouteBefore <em>Route Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getRouteOnElement <em>Route On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getRouteOnElementNS <em>Route On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getToServiceCategory <em>To Service Category</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getToServiceName <em>To Service Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage#getRouteBean()
 * @model extendedMetaData="name='routeBean' kind='elementOnly'"
 * @generated
 */
public interface RouteBean extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Bean Id Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Bean ID reference of the bean to be routed to the target Service.
	 *                         
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Id Ref</em>' attribute.
	 * @see #setBeanIdRef(String)
	 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage#getRouteBean_BeanIdRef()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='beanIdRef'"
	 * @generated
	 */
	String getBeanIdRef();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getBeanIdRef <em>Bean Id Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Id Ref</em>' attribute.
	 * @see #getBeanIdRef()
	 * @generated
	 */
	void setBeanIdRef(String value);

	/**
	 * Returns the value of the '<em><b>Message Payload Location</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * ESB Message.Body location on which the routed bean will be set.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Message Payload Location</em>' attribute.
	 * @see #isSetMessagePayloadLocation()
	 * @see #unsetMessagePayloadLocation()
	 * @see #setMessagePayloadLocation(String)
	 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage#getRouteBean_MessagePayloadLocation()
	 * @model default="" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='messagePayloadLocation'"
	 * @generated
	 */
	String getMessagePayloadLocation();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getMessagePayloadLocation <em>Message Payload Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Message Payload Location</em>' attribute.
	 * @see #isSetMessagePayloadLocation()
	 * @see #unsetMessagePayloadLocation()
	 * @see #getMessagePayloadLocation()
	 * @generated
	 */
	void setMessagePayloadLocation(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getMessagePayloadLocation <em>Message Payload Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMessagePayloadLocation()
	 * @see #getMessagePayloadLocation()
	 * @see #setMessagePayloadLocation(String)
	 * @generated
	 */
	void unsetMessagePayloadLocation();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getMessagePayloadLocation <em>Message Payload Location</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Message Payload Location</em>' attribute is set.
	 * @see #unsetMessagePayloadLocation()
	 * @see #getMessagePayloadLocation()
	 * @see #setMessagePayloadLocation(String)
	 * @generated
	 */
	boolean isSetMessagePayloadLocation();

	/**
	 * Returns the value of the '<em><b>Route Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Route on visitBefore of the routeOnElement.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Route Before</em>' attribute.
	 * @see #isSetRouteBefore()
	 * @see #unsetRouteBefore()
	 * @see #setRouteBefore(boolean)
	 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage#getRouteBean_RouteBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='routeBefore'"
	 * @generated
	 */
	boolean isRouteBefore();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#isRouteBefore <em>Route Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route Before</em>' attribute.
	 * @see #isSetRouteBefore()
	 * @see #unsetRouteBefore()
	 * @see #isRouteBefore()
	 * @generated
	 */
	void setRouteBefore(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#isRouteBefore <em>Route Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetRouteBefore()
	 * @see #isRouteBefore()
	 * @see #setRouteBefore(boolean)
	 * @generated
	 */
	void unsetRouteBefore();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#isRouteBefore <em>Route Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Route Before</em>' attribute is set.
	 * @see #unsetRouteBefore()
	 * @see #isRouteBefore()
	 * @see #setRouteBefore(boolean)
	 * @generated
	 */
	boolean isSetRouteBefore();

	/**
	 * Returns the value of the '<em><b>Route On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Route on Element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Route On Element</em>' attribute.
	 * @see #setRouteOnElement(String)
	 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage#getRouteBean_RouteOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='routeOnElement'"
	 * @generated
	 */
	String getRouteOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getRouteOnElement <em>Route On Element</em>}' attribute.
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
	 * Route on Element Namespace.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Route On Element NS</em>' attribute.
	 * @see #setRouteOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage#getRouteBean_RouteOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='routeOnElementNS'"
	 * @generated
	 */
	String getRouteOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getRouteOnElementNS <em>Route On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route On Element NS</em>' attribute.
	 * @see #getRouteOnElementNS()
	 * @generated
	 */
	void setRouteOnElementNS(String value);

	/**
	 * Returns the value of the '<em><b>To Service Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Target Service Category.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>To Service Category</em>' attribute.
	 * @see #setToServiceCategory(String)
	 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage#getRouteBean_ToServiceCategory()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='toServiceCategory'"
	 * @generated
	 */
	String getToServiceCategory();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getToServiceCategory <em>To Service Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Service Category</em>' attribute.
	 * @see #getToServiceCategory()
	 * @generated
	 */
	void setToServiceCategory(String value);

	/**
	 * Returns the value of the '<em><b>To Service Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Target Service Name.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>To Service Name</em>' attribute.
	 * @see #setToServiceName(String)
	 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage#getRouteBean_ToServiceName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='toServiceName'"
	 * @generated
	 */
	String getToServiceName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getToServiceName <em>To Service Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>To Service Name</em>' attribute.
	 * @see #getToServiceName()
	 * @generated
	 */
	void setToServiceName(String value);

} // RouteBean
