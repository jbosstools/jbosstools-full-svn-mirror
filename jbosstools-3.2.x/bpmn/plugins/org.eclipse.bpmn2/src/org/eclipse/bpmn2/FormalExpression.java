/**
 * <copyright>
 * 
 * Copyright (c) 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Reiner Hille-Doering (SAP AG) - initial API and implementation and/or initial documentation
 * 
 * </copyright>
 */
package org.eclipse.bpmn2;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Formal Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.FormalExpression#getBody <em>Body</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.FormalExpression#getEvaluatesToTypeRef <em>Evaluates To Type Ref</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.FormalExpression#getLanguage <em>Language</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.bpmn2.Bpmn2Package#getFormalExpression()
 * @model extendedMetaData="name='tFormalExpression' kind='mixed'"
 * @generated
 */
public interface FormalExpression extends Expression {
    /**
     * Returns the value of the '<em><b>Body</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Body</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Body</em>' reference.
     * @see #setBody(Object)
     * @see org.eclipse.bpmn2.Bpmn2Package#getFormalExpression_Body()
     * @model required="true" transient="true" derived="true" ordered="false"
     * @generated
     */
    Object getBody();

    /**
     * Sets the value of the '{@link org.eclipse.bpmn2.FormalExpression#getBody <em>Body</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Body</em>' reference.
     * @see #getBody()
     * @generated
     */
    void setBody(Object value);

    /**
     * Returns the value of the '<em><b>Evaluates To Type Ref</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Evaluates To Type Ref</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Evaluates To Type Ref</em>' reference.
     * @see #setEvaluatesToTypeRef(ItemDefinition)
     * @see org.eclipse.bpmn2.Bpmn2Package#getFormalExpression_EvaluatesToTypeRef()
     * @model required="true" ordered="false"
     *        extendedMetaData="kind='attribute' name='evaluatesToTypeRef'"
     * @generated
     */
    ItemDefinition getEvaluatesToTypeRef();

    /**
     * Sets the value of the '{@link org.eclipse.bpmn2.FormalExpression#getEvaluatesToTypeRef <em>Evaluates To Type Ref</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Evaluates To Type Ref</em>' reference.
     * @see #getEvaluatesToTypeRef()
     * @generated
     */
    void setEvaluatesToTypeRef(ItemDefinition value);

    /**
     * Returns the value of the '<em><b>Language</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Language</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Language</em>' attribute.
     * @see #setLanguage(String)
     * @see org.eclipse.bpmn2.Bpmn2Package#getFormalExpression_Language()
     * @model required="true" ordered="false"
     *        extendedMetaData="kind='attribute' name='language'"
     * @generated
     */
    String getLanguage();

    /**
     * Sets the value of the '{@link org.eclipse.bpmn2.FormalExpression#getLanguage <em>Language</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Language</em>' attribute.
     * @see #getLanguage()
     * @generated
     */
    void setLanguage(String value);

} // FormalExpression
