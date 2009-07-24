/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.rules10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;
import org.jboss.tools.smooks.model.smooks.ElementVisitor;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule Bases Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.rules10.RuleBasesType#getGroup <em>Group</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.rules10.RuleBasesType#getRuleBase <em>Rule Base</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.rules10.Rules10Package#getRuleBasesType()
 * @model extendedMetaData="name='ruleBases_._type' kind='elementOnly'"
 * @generated
 */
public interface RuleBasesType extends ElementVisitor {
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
	 * @see org.jboss.tools.smooks.model.rules10.Rules10Package#getRuleBasesType_Group()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='group' name='group:2'"
	 * @generated
	 */
	FeatureMap getGroup();

	/**
	 * Returns the value of the '<em><b>Rule Base</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.rules10.RuleBase}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rule Base</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rule Base</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.rules10.Rules10Package#getRuleBasesType_RuleBase()
	 * @model type="rules10.RuleBase" containment="true" required="true" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='ruleBase' namespace='##targetNamespace' group='#group:2'"
	 * @generated
	 */
	EList getRuleBase();

} // RuleBasesType
