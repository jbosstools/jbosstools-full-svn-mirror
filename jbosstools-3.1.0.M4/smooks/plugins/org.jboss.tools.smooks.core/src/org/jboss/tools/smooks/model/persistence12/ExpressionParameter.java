/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12;

import org.jboss.tools.smooks.model.common.AbstractAnyType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Expression Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getValue <em>Value</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getExecOnElement <em>Exec On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getExecOnElementNS <em>Exec On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getExpressionParameter()
 * @model extendedMetaData="name='expressionParameter' kind='simple'"
 * @generated
 */
public interface ExpressionParameter extends AbstractAnyType {
	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getExpressionParameter_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getExpressionParameter_ExecOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='execOnElement'"
	 * @generated
	 */
	String getExecOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getExecOnElement <em>Exec On Element</em>}' attribute.
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getExpressionParameter_ExecOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='execOnElementNS'"
	 * @generated
	 */
	String getExecOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getExecOnElementNS <em>Exec On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exec On Element NS</em>' attribute.
	 * @see #getExecOnElementNS()
	 * @generated
	 */
	void setExecOnElementNS(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getExpressionParameter_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ExpressionParameter
