/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks;


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
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.ConditionType#getValue <em>Value</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.ConditionType#getEvaluator <em>Evaluator</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getConditionType()
 * @model extendedMetaData="name='condition_._type' kind='simple'"
 * @generated
 */
public interface ConditionType extends AbstractType {
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
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getConditionType_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
	String getValue();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks10.model.smooks.ConditionType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(String value);

	/**
	 * Returns the value of the '<em><b>Evaluator</b></em>' attribute.
	 * The default value is <code>"org.milyn.javabean.expression.BeanMapExpressionEvaluator"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Evaluator</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Evaluator</em>' attribute.
	 * @see #isSetEvaluator()
	 * @see #unsetEvaluator()
	 * @see #setEvaluator(String)
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getConditionType_Evaluator()
	 * @model default="org.milyn.javabean.expression.BeanMapExpressionEvaluator" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='evaluator'"
	 * @generated
	 */
	String getEvaluator();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks10.model.smooks.ConditionType#getEvaluator <em>Evaluator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Evaluator</em>' attribute.
	 * @see #isSetEvaluator()
	 * @see #unsetEvaluator()
	 * @see #getEvaluator()
	 * @generated
	 */
	void setEvaluator(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks10.model.smooks.ConditionType#getEvaluator <em>Evaluator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEvaluator()
	 * @see #getEvaluator()
	 * @see #setEvaluator(String)
	 * @generated
	 */
	void unsetEvaluator();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks10.model.smooks.ConditionType#getEvaluator <em>Evaluator</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Evaluator</em>' attribute is set.
	 * @see #unsetEvaluator()
	 * @see #getEvaluator()
	 * @see #setEvaluator(String)
	 * @generated
	 */
	boolean isSetEvaluator();

} // ConditionType
