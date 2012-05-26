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

import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extension Attribute Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.bpmn2.ExtensionAttributeValue#getValueRef <em>Value Ref</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.ExtensionAttributeValue#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.bpmn2.ExtensionAttributeValue#getExtensionAttributeDefinition <em>Extension Attribute Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.bpmn2.Bpmn2Package#getExtensionAttributeValue()
 * @model extendedMetaData="name='tExtensionElements' kind='elementOnly'"
 * @generated
 */
public interface ExtensionAttributeValue extends EObject {
    /**
     * Returns the value of the '<em><b>Value Ref</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value Ref</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value Ref</em>' reference.
     * @see #setValueRef(Object)
     * @see org.eclipse.bpmn2.Bpmn2Package#getExtensionAttributeValue_ValueRef()
     * @model transient="true" derived="true" ordered="false"
     * @generated
     */
    Object getValueRef();

    /**
     * Sets the value of the '{@link org.eclipse.bpmn2.ExtensionAttributeValue#getValueRef <em>Value Ref</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value Ref</em>' reference.
     * @see #getValueRef()
     * @generated
     */
    void setValueRef(Object value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' containment reference list.
     * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' containment reference list.
     * @see org.eclipse.bpmn2.Bpmn2Package#getExtensionAttributeValue_Value()
     * @model containment="true" ordered="false"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':0' processing='lax'"
     * @generated
     */
    List<EObject> getValue();

    /**
     * Returns the value of the '<em><b>Extension Attribute Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Extension Attribute Definition</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Extension Attribute Definition</em>' reference.
     * @see #setExtensionAttributeDefinition(ExtensionAttributeDefinition)
     * @see org.eclipse.bpmn2.Bpmn2Package#getExtensionAttributeValue_ExtensionAttributeDefinition()
     * @model required="true" transient="true" derived="true" ordered="false"
     * @generated
     */
    ExtensionAttributeDefinition getExtensionAttributeDefinition();

    /**
     * Sets the value of the '{@link org.eclipse.bpmn2.ExtensionAttributeValue#getExtensionAttributeDefinition <em>Extension Attribute Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extension Attribute Definition</em>' reference.
     * @see #getExtensionAttributeDefinition()
     * @generated
     */
    void setExtensionAttributeDefinition(ExtensionAttributeDefinition value);

} // ExtensionAttributeValue
