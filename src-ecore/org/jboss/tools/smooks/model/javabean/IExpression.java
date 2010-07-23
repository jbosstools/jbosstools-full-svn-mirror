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
 * A representation of the model object '<em><b>Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IExpression#getProperty <em>Property</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IExpression#getSetterMethod <em>Setter Method</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IExpression#getExecOnElement <em>Exec On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IExpression#getExecOnElementNS <em>Exec On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IExpression#getInitVal <em>Init Val</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getExpression()
 * @model
 * @generated
 */
public interface IExpression extends EObject {
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
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getExpression_Property()
	 * @model required="true"
	 * @generated
	 */
	String getProperty();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IExpression#getProperty <em>Property</em>}' attribute.
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
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getExpression_SetterMethod()
	 * @model required="true"
	 * @generated
	 */
	String getSetterMethod();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IExpression#getSetterMethod <em>Setter Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Setter Method</em>' attribute.
	 * @see #getSetterMethod()
	 * @generated
	 */
	void setSetterMethod(String value);

	/**
	 * Returns the value of the '<em><b>Exec On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exec On Element</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exec On Element</em>' attribute.
	 * @see #setExecOnElement(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getExpression_ExecOnElement()
	 * @model required="true"
	 * @generated
	 */
	String getExecOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IExpression#getExecOnElement <em>Exec On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exec On Element</em>' attribute.
	 * @see #getExecOnElement()
	 * @generated
	 */
	void setExecOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Exec On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exec On Element NS</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exec On Element NS</em>' attribute.
	 * @see #setExecOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getExpression_ExecOnElementNS()
	 * @model
	 * @generated
	 */
	String getExecOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IExpression#getExecOnElementNS <em>Exec On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exec On Element NS</em>' attribute.
	 * @see #getExecOnElementNS()
	 * @generated
	 */
	void setExecOnElementNS(String value);

	/**
	 * Returns the value of the '<em><b>Init Val</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Init Val</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Init Val</em>' attribute.
	 * @see #setInitVal(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getExpression_InitVal()
	 * @model required="true"
	 * @generated
	 */
	String getInitVal();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IExpression#getInitVal <em>Init Val</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Init Val</em>' attribute.
	 * @see #getInitVal()
	 * @generated
	 */
	void setInitVal(String value);

} // IExpression
