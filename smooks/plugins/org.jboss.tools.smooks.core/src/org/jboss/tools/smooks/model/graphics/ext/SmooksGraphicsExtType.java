/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInput <em>Input</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInputType <em>Input Type</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getOutputType <em>Output Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtPackage#getSmooksGraphicsExtType()
 * @model extendedMetaData="name='smooks-graphics-ext_._type' kind='elementOnly'"
 * @generated
 */
public interface SmooksGraphicsExtType extends EObject {
	/**
	 * Returns the value of the '<em><b>Input</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.graphics.ext.InputType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtPackage#getSmooksGraphicsExtType_Input()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='input' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<InputType> getInput();

	/**
	 * Returns the value of the '<em><b>Input Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Type</em>' attribute.
	 * @see #setInputType(String)
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtPackage#getSmooksGraphicsExtType_InputType()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='inputType'"
	 * @generated
	 */
	String getInputType();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInputType <em>Input Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Type</em>' attribute.
	 * @see #getInputType()
	 * @generated
	 */
	void setInputType(String value);

	/**
	 * Returns the value of the '<em><b>Output Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Type</em>' attribute.
	 * @see #setOutputType(String)
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtPackage#getSmooksGraphicsExtType_OutputType()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='outputType'"
	 * @generated
	 */
	String getOutputType();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getOutputType <em>Output Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output Type</em>' attribute.
	 * @see #getOutputType()
	 * @generated
	 */
	void setOutputType(String value);

} // SmooksGraphicsExtType
