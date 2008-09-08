/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.milyn.xsd.smooks;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Condition Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Resource Targetting Condition
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.milyn.xsd.smooks.ConditionType#getValue <em>Value</em>}</li>
 *   <li>{@link org.milyn.xsd.smooks.ConditionType#getEvaluator <em>Evaluator</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.milyn.xsd.smooks.SmooksPackage#getConditionType()
 * @model extendedMetaData="name='condition_._type' kind='simple'"
 * @generated
 */
public interface ConditionType extends EObject {
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
	 * @see org.milyn.xsd.smooks.SmooksPackage#getConditionType_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.milyn.xsd.smooks.ConditionType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Evaluator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Evaluator</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Evaluator</em>' attribute.
	 * @see #setEvaluator(String)
	 * @see org.milyn.xsd.smooks.SmooksPackage#getConditionType_Evaluator()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='evaluator'"
	 * @generated
	 */
	String getEvaluator();

	/**
	 * Sets the value of the '{@link org.milyn.xsd.smooks.ConditionType#getEvaluator <em>Evaluator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Evaluator</em>' attribute.
	 * @see #getEvaluator()
	 * @generated
	 */
	void setEvaluator(String value);

} // ConditionType
