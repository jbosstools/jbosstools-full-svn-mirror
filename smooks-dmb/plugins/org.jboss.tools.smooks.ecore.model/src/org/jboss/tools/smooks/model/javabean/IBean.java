/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean;

import org.eclipse.emf.common.util.EList;

import org.jboss.tools.smooks.model.core.IComponent;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bean</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IBean#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IBean#getBeanClass <em>Bean Class</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IBean#getCreateOnElement <em>Create On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IBean#getCreateOnElementNS <em>Create On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IBean#getValueBindings <em>Value Bindings</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IBean#getWireBindings <em>Wire Bindings</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IBean#getExpressionBindings <em>Expression Bindings</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getBean()
 * @model
 * @generated
 */
public interface IBean extends IComponent {
	/**
	 * Returns the value of the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bean Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bean Id</em>' attribute.
	 * @see #setBeanId(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getBean_BeanId()
	 * @model
	 * @generated
	 */
	String getBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IBean#getBeanId <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Id</em>' attribute.
	 * @see #getBeanId()
	 * @generated
	 */
	void setBeanId(String value);

	/**
	 * Returns the value of the '<em><b>Bean Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bean Class</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bean Class</em>' attribute.
	 * @see #setBeanClass(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getBean_BeanClass()
	 * @model
	 * @generated
	 */
	String getBeanClass();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IBean#getBeanClass <em>Bean Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Class</em>' attribute.
	 * @see #getBeanClass()
	 * @generated
	 */
	void setBeanClass(String value);

	/**
	 * Returns the value of the '<em><b>Create On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Create On Element</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Create On Element</em>' attribute.
	 * @see #setCreateOnElement(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getBean_CreateOnElement()
	 * @model
	 * @generated
	 */
	String getCreateOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IBean#getCreateOnElement <em>Create On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Create On Element</em>' attribute.
	 * @see #getCreateOnElement()
	 * @generated
	 */
	void setCreateOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Create On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Create On Element NS</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Create On Element NS</em>' attribute.
	 * @see #setCreateOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getBean_CreateOnElementNS()
	 * @model
	 * @generated
	 */
	String getCreateOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IBean#getCreateOnElementNS <em>Create On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Create On Element NS</em>' attribute.
	 * @see #getCreateOnElementNS()
	 * @generated
	 */
	void setCreateOnElementNS(String value);

	/**
	 * Returns the value of the '<em><b>Value Bindings</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.javabean.IValue}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value Bindings</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Bindings</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getBean_ValueBindings()
	 * @model containment="true"
	 * @generated
	 */
	EList<IValue> getValueBindings();

	/**
	 * Returns the value of the '<em><b>Wire Bindings</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.javabean.IWiring}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wire Bindings</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wire Bindings</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getBean_WireBindings()
	 * @model containment="true"
	 * @generated
	 */
	EList<IWiring> getWireBindings();

	/**
	 * Returns the value of the '<em><b>Expression Bindings</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.javabean.IExpression}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression Bindings</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression Bindings</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getBean_ExpressionBindings()
	 * @model containment="true"
	 * @generated
	 */
	EList<IExpression> getExpressionBindings();

} // IBean
