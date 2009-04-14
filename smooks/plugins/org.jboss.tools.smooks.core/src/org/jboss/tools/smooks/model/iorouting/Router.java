/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.iorouting;

import org.jboss.tools.smooks.model.smooks.ElementVisitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Router</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *     			Output stream router
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.Router#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.Router#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.Router#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.Router#getResourceName <em>Resource Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.Router#getRouteOnElement <em>Route On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.iorouting.Router#getRouteOnElementNS <em>Route On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.iorouting.IoroutingPackage#getRouter()
 * @model extendedMetaData="name='router' kind='elementOnly'"
 * @generated
 */
public interface Router extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The beanId of the bean from the bean context to be written to the OutputStream.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Id</em>' attribute.
	 * @see #setBeanId(String)
	 * @see org.jboss.tools.smooks.model.iorouting.IoroutingPackage#getRouter_BeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='beanId'"
	 * @generated
	 */
	String getBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#getBeanId <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Id</em>' attribute.
	 * @see #getBeanId()
	 * @generated
	 */
	void setBeanId(String value);

	/**
	 * Returns the value of the '<em><b>Encoding</b></em>' attribute.
	 * The default value is <code>"UTF-8"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			 The encoding used when writing characters to the stream. Default is 'UTF-8'
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Encoding</em>' attribute.
	 * @see #isSetEncoding()
	 * @see #unsetEncoding()
	 * @see #setEncoding(String)
	 * @see org.jboss.tools.smooks.model.iorouting.IoroutingPackage#getRouter_Encoding()
	 * @model default="UTF-8" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='encoding'"
	 * @generated
	 */
	String getEncoding();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Encoding</em>' attribute.
	 * @see #isSetEncoding()
	 * @see #unsetEncoding()
	 * @see #getEncoding()
	 * @generated
	 */
	void setEncoding(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEncoding()
	 * @see #getEncoding()
	 * @see #setEncoding(String)
	 * @generated
	 */
	void unsetEncoding();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#getEncoding <em>Encoding</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Encoding</em>' attribute is set.
	 * @see #unsetEncoding()
	 * @see #getEncoding()
	 * @see #setEncoding(String)
	 * @generated
	 */
	boolean isSetEncoding();

	/**
	 * Returns the value of the '<em><b>Execute Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			If the routing is done before or after the selected element. Default is 'false'.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute Before</em>' attribute.
	 * @see #isSetExecuteBefore()
	 * @see #unsetExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @see org.jboss.tools.smooks.model.iorouting.IoroutingPackage#getRouter_ExecuteBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='executeBefore'"
	 * @generated
	 */
	boolean isExecuteBefore();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#isExecuteBefore <em>Execute Before</em>}' attribute.
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
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#isExecuteBefore <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetExecuteBefore()
	 * @see #isExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @generated
	 */
	void unsetExecuteBefore();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#isExecuteBefore <em>Execute Before</em>}' attribute is set.
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
	 * Returns the value of the '<em><b>Resource Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The resource name of the AbstractOutputStreamResource the bean should be routed to.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Resource Name</em>' attribute.
	 * @see #setResourceName(String)
	 * @see org.jboss.tools.smooks.model.iorouting.IoroutingPackage#getRouter_ResourceName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='resourceName'"
	 * @generated
	 */
	String getResourceName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#getResourceName <em>Resource Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Resource Name</em>' attribute.
	 * @see #getResourceName()
	 * @generated
	 */
	void setResourceName(String value);

	/**
	 * Returns the value of the '<em><b>Route On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The element to route on.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Route On Element</em>' attribute.
	 * @see #setRouteOnElement(String)
	 * @see org.jboss.tools.smooks.model.iorouting.IoroutingPackage#getRouter_RouteOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='routeOnElement'"
	 * @generated
	 */
	String getRouteOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#getRouteOnElement <em>Route On Element</em>}' attribute.
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
	 * @see org.jboss.tools.smooks.model.iorouting.IoroutingPackage#getRouter_RouteOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='routeOnElementNS'"
	 * @generated
	 */
	String getRouteOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.iorouting.Router#getRouteOnElementNS <em>Route On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route On Element NS</em>' attribute.
	 * @see #getRouteOnElementNS()
	 * @generated
	 */
	void setRouteOnElementNS(String value);

} // Router
