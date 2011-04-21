/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.validation10;

import org.jboss.tools.smooks.model.smooks.ElementVisitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.validation10.RuleType#getExecuteOn <em>Execute On</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.validation10.RuleType#getExecuteOnNS <em>Execute On NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.validation10.RuleType#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.validation10.RuleType#getOnFail <em>On Fail</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.validation10.Validation10Package#getRuleType()
 * @model extendedMetaData="name='rule_._type' kind='elementOnly'"
 * @generated
 */
public interface RuleType extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Execute On</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                             The fragment that this validator should operate on. 
	 *                         
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute On</em>' attribute.
	 * @see #setExecuteOn(String)
	 * @see org.jboss.tools.smooks.model.validation10.Validation10Package#getRuleType_ExecuteOn()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='executeOn'"
	 * @generated
	 */
	String getExecuteOn();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.validation10.RuleType#getExecuteOn <em>Execute On</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execute On</em>' attribute.
	 * @see #getExecuteOn()
	 * @generated
	 */
	void setExecuteOn(String value);

	/**
	 * Returns the value of the '<em><b>Execute On NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                             Namespace for the "executeOn" attribute.
	 *                         
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute On NS</em>' attribute.
	 * @see #setExecuteOnNS(String)
	 * @see org.jboss.tools.smooks.model.validation10.Validation10Package#getRuleType_ExecuteOnNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='executeOnNS'"
	 * @generated
	 */
	String getExecuteOnNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.validation10.RuleType#getExecuteOnNS <em>Execute On NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execute On NS</em>' attribute.
	 * @see #getExecuteOnNS()
	 * @generated
	 */
	void setExecuteOnNS(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                             The name of the rule defined in Smooks. This name matches the ruleBase elements
	 *                             name attribute and tell Smooks to use that rule for validation.
	 *                         
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.jboss.tools.smooks.model.validation10.Validation10Package#getRuleType_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.validation10.RuleType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>On Fail</b></em>' attribute.
	 * The literals are from the enumeration {@link org.jboss.tools.smooks.model.validation10.OnFail}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Validation failure categorization.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>On Fail</em>' attribute.
	 * @see org.jboss.tools.smooks.model.validation10.OnFail
	 * @see #isSetOnFail()
	 * @see #unsetOnFail()
	 * @see #setOnFail(OnFail)
	 * @see org.jboss.tools.smooks.model.validation10.Validation10Package#getRuleType_OnFail()
	 * @model unsettable="true" required="true"
	 *        extendedMetaData="kind='attribute' name='onFail'"
	 * @generated
	 */
	OnFail getOnFail();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.validation10.RuleType#getOnFail <em>On Fail</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>On Fail</em>' attribute.
	 * @see org.jboss.tools.smooks.model.validation10.OnFail
	 * @see #isSetOnFail()
	 * @see #unsetOnFail()
	 * @see #getOnFail()
	 * @generated
	 */
	void setOnFail(OnFail value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.validation10.RuleType#getOnFail <em>On Fail</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetOnFail()
	 * @see #getOnFail()
	 * @see #setOnFail(OnFail)
	 * @generated
	 */
	void unsetOnFail();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.validation10.RuleType#getOnFail <em>On Fail</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>On Fail</em>' attribute is set.
	 * @see #unsetOnFail()
	 * @see #getOnFail()
	 * @see #setOnFail(OnFail)
	 * @generated
	 */
	boolean isSetOnFail();

} // RuleType
