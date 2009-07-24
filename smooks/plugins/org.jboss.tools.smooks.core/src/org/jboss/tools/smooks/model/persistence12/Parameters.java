/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Parameters#getGroup <em>Group</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Parameters#getValue <em>Value</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Parameters#getWiring <em>Wiring</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Parameters#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Parameters#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getParameters()
 * @model extendedMetaData="name='parameters' kind='elementOnly'"
 * @generated
 */
public interface Parameters extends EObject {
	/**
	 * Returns the value of the '<em><b>Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Group</em>' attribute list.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getParameters_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:0'"
	 * @generated
	 */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.persistence12.ValueParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getParameters_Value()
	 * @model type="persistence12.ValueParameter" containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='value' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
	EList getValue();

	/**
	 * Returns the value of the '<em><b>Wiring</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.persistence12.WiringParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wiring</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wiring</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getParameters_Wiring()
	 * @model type="persistence12.WiringParameter" containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='wiring' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
	EList getWiring();

	/**
	 * Returns the value of the '<em><b>Expression</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expression</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getParameters_Expression()
	 * @model type="persistence12.ExpressionParameter" containment="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='expression' namespace='##targetNamespace' group='#group:0'"
	 * @generated
	 */
	EList getExpression();

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The default value is <code>"NAMED"</code>.
	 * The literals are from the enumeration {@link org.jboss.tools.smooks.model.persistence12.ParameterType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.jboss.tools.smooks.model.persistence12.ParameterType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(ParameterType)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getParameters_Type()
	 * @model default="NAMED" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='type'"
	 * @generated
	 */
	ParameterType getType();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Parameters#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.jboss.tools.smooks.model.persistence12.ParameterType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(ParameterType value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Parameters#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(ParameterType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.persistence12.Parameters#getType <em>Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(ParameterType)
	 * @generated
	 */
	boolean isSetType();

} // Parameters
