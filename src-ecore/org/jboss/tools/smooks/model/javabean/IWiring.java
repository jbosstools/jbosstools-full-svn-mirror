/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Wiring</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IWiring#getProperty <em>Property</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IWiring#getSetterMethod <em>Setter Method</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IWiring#getBeanIdRef <em>Bean Id Ref</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IWiring#getWireOnElement <em>Wire On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IWiring#getWireOnElementNS <em>Wire On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getWiring()
 * @model
 * @generated
 */
public interface IWiring extends EObject {
	/**
	 * Returns the value of the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' attribute.
	 * @see #setProperty(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getWiring_Property()
	 * @model required="true"
	 * @generated
	 */
	String getProperty();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IWiring#getProperty <em>Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' attribute.
	 * @see #getProperty()
	 * @generated
	 */
	void setProperty(String value);

	/**
	 * Returns the value of the '<em><b>Setter Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Setter Method</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Setter Method</em>' attribute.
	 * @see #setSetterMethod(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getWiring_SetterMethod()
	 * @model required="true"
	 * @generated
	 */
	String getSetterMethod();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IWiring#getSetterMethod <em>Setter Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Setter Method</em>' attribute.
	 * @see #getSetterMethod()
	 * @generated
	 */
	void setSetterMethod(String value);

	/**
	 * Returns the value of the '<em><b>Bean Id Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bean Id Ref</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bean Id Ref</em>' attribute.
	 * @see #setBeanIdRef(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getWiring_BeanIdRef()
	 * @model required="true"
	 * @generated
	 */
	String getBeanIdRef();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IWiring#getBeanIdRef <em>Bean Id Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Id Ref</em>' attribute.
	 * @see #getBeanIdRef()
	 * @generated
	 */
	void setBeanIdRef(String value);

	/**
	 * Returns the value of the '<em><b>Wire On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wire On Element</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wire On Element</em>' attribute.
	 * @see #setWireOnElement(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getWiring_WireOnElement()
	 * @model required="true"
	 * @generated
	 */
	String getWireOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IWiring#getWireOnElement <em>Wire On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wire On Element</em>' attribute.
	 * @see #getWireOnElement()
	 * @generated
	 */
	void setWireOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Wire On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wire On Element NS</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wire On Element NS</em>' attribute.
	 * @see #setWireOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getWiring_WireOnElementNS()
	 * @model required="true"
	 * @generated
	 */
	String getWireOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IWiring#getWireOnElementNS <em>Wire On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wire On Element NS</em>' attribute.
	 * @see #getWireOnElementNS()
	 * @generated
	 */
	void setWireOnElementNS(String value);

} // IWiring
