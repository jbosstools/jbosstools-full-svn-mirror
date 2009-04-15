/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.calc;

import org.jboss.tools.smooks.model.smooks.ElementVisitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Counter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *     			Counter
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#getStartExpression <em>Start Expression</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#getAmountExpression <em>Amount Expression</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#getResetCondition <em>Reset Condition</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#getAmount <em>Amount</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#getCountOnElement <em>Count On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#getCountOnElementNS <em>Count On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#getDirection <em>Direction</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#isExecuteAfter <em>Execute After</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.Counter#getStart <em>Start</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter()
 * @model extendedMetaData="name='counter' kind='elementOnly'"
 * @generated
 */
public interface Counter extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Start Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								The result of this expression is the counter start value.
	 * 								This expression is executed at the first count and every time the counter
	 * 								is reset. The expression must result in an integer or a long.
	 * 								If the startIndex attribute of the counter is set then this expression never gets
	 * 								executed.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Start Expression</em>' attribute.
	 * @see #setStartExpression(String)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_StartExpression()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='startExpression' namespace='##targetNamespace'"
	 * @generated
	 */
	String getStartExpression();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getStartExpression <em>Start Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Expression</em>' attribute.
	 * @see #getStartExpression()
	 * @generated
	 */
	void setStartExpression(String value);

	/**
	 * Returns the value of the '<em><b>Amount Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				    			The result of this expression is the amount the counter increments or decrements.
	 * 								This expression is executed every time the counter counts.
	 * 								The expression must result in an integer.
	 * 								If the amount attribute of the counter is set then this expression never gets
	 * 								executed.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Amount Expression</em>' attribute.
	 * @see #setAmountExpression(String)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_AmountExpression()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='amountExpression' namespace='##targetNamespace'"
	 * @generated
	 */
	String getAmountExpression();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getAmountExpression <em>Amount Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Amount Expression</em>' attribute.
	 * @see #getAmountExpression()
	 * @generated
	 */
	void setAmountExpression(String value);

	/**
	 * Returns the value of the '<em><b>Reset Condition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								When the expression is set and results in a true value then the counter is reset to
	 * 								the start index. The expression must result in a boolean.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Reset Condition</em>' attribute.
	 * @see #setResetCondition(String)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_ResetCondition()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='resetCondition' namespace='##targetNamespace'"
	 * @generated
	 */
	String getResetCondition();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getResetCondition <em>Reset Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reset Condition</em>' attribute.
	 * @see #getResetCondition()
	 * @generated
	 */
	void setResetCondition(String value);

	/**
	 * Returns the value of the '<em><b>Amount</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							The amount that the counter increments or decrements the counter value.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Amount</em>' attribute.
	 * @see #isSetAmount()
	 * @see #unsetAmount()
	 * @see #setAmount(int)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_Amount()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='attribute' name='amount'"
	 * @generated
	 */
	int getAmount();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getAmount <em>Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Amount</em>' attribute.
	 * @see #isSetAmount()
	 * @see #unsetAmount()
	 * @see #getAmount()
	 * @generated
	 */
	void setAmount(int value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getAmount <em>Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAmount()
	 * @see #getAmount()
	 * @see #setAmount(int)
	 * @generated
	 */
	void unsetAmount();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getAmount <em>Amount</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Amount</em>' attribute is set.
	 * @see #unsetAmount()
	 * @see #getAmount()
	 * @see #setAmount(int)
	 * @generated
	 */
	boolean isSetAmount();

	/**
	 * Returns the value of the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							The beanId in which the counter value is stored. The value is always stored
	 * 							as a Long type.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Id</em>' attribute.
	 * @see #setBeanId(String)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_BeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='beanId'"
	 * @generated
	 */
	String getBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getBeanId <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Id</em>' attribute.
	 * @see #getBeanId()
	 * @generated
	 */
	void setBeanId(String value);

	/**
	 * Returns the value of the '<em><b>Count On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The element on which the counter counts.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Count On Element</em>' attribute.
	 * @see #setCountOnElement(String)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_CountOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='countOnElement'"
	 * @generated
	 */
	String getCountOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getCountOnElement <em>Count On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Count On Element</em>' attribute.
	 * @see #getCountOnElement()
	 * @generated
	 */
	void setCountOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Count On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The namespace of the countOnElement element.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Count On Element NS</em>' attribute.
	 * @see #setCountOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_CountOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='countOnElementNS'"
	 * @generated
	 */
	String getCountOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getCountOnElementNS <em>Count On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Count On Element NS</em>' attribute.
	 * @see #getCountOnElementNS()
	 * @generated
	 */
	void setCountOnElementNS(String value);

	/**
	 * Returns the value of the '<em><b>Direction</b></em>' attribute.
	 * The default value is <code>"INCREMENT"</code>.
	 * The literals are from the enumeration {@link org.jboss.tools.smooks.model.calc.CountDirection}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							The direction that the counter counts. Can be INCREMENT (default) or DECREMENT.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Direction</em>' attribute.
	 * @see org.jboss.tools.smooks.model.calc.CountDirection
	 * @see #isSetDirection()
	 * @see #unsetDirection()
	 * @see #setDirection(CountDirection)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_Direction()
	 * @model default="INCREMENT" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='direction'"
	 * @generated
	 */
	CountDirection getDirection();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Direction</em>' attribute.
	 * @see org.jboss.tools.smooks.model.calc.CountDirection
	 * @see #isSetDirection()
	 * @see #unsetDirection()
	 * @see #getDirection()
	 * @generated
	 */
	void setDirection(CountDirection value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getDirection <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDirection()
	 * @see #getDirection()
	 * @see #setDirection(CountDirection)
	 * @generated
	 */
	void unsetDirection();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getDirection <em>Direction</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Direction</em>' attribute is set.
	 * @see #unsetDirection()
	 * @see #getDirection()
	 * @see #setDirection(CountDirection)
	 * @generated
	 */
	boolean isSetDirection();

	/**
	 * Returns the value of the '<em><b>Execute After</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			If the counter is executed after the element else it will execute before the element.
	 * 			    			Default is 'false'.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute After</em>' attribute.
	 * @see #isSetExecuteAfter()
	 * @see #unsetExecuteAfter()
	 * @see #setExecuteAfter(boolean)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_ExecuteAfter()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='executeAfter'"
	 * @generated
	 */
	boolean isExecuteAfter();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#isExecuteAfter <em>Execute After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execute After</em>' attribute.
	 * @see #isSetExecuteAfter()
	 * @see #unsetExecuteAfter()
	 * @see #isExecuteAfter()
	 * @generated
	 */
	void setExecuteAfter(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#isExecuteAfter <em>Execute After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetExecuteAfter()
	 * @see #isExecuteAfter()
	 * @see #setExecuteAfter(boolean)
	 * @generated
	 */
	void unsetExecuteAfter();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#isExecuteAfter <em>Execute After</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Execute After</em>' attribute is set.
	 * @see #unsetExecuteAfter()
	 * @see #isExecuteAfter()
	 * @see #setExecuteAfter(boolean)
	 * @generated
	 */
	boolean isSetExecuteAfter();

	/**
	 * Returns the value of the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							The counter start value.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Start</em>' attribute.
	 * @see #isSetStart()
	 * @see #unsetStart()
	 * @see #setStart(long)
	 * @see org.jboss.tools.smooks.model.calc.CalcPackage#getCounter_Start()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Long"
	 *        extendedMetaData="kind='attribute' name='start'"
	 * @generated
	 */
	long getStart();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start</em>' attribute.
	 * @see #isSetStart()
	 * @see #unsetStart()
	 * @see #getStart()
	 * @generated
	 */
	void setStart(long value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getStart <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetStart()
	 * @see #getStart()
	 * @see #setStart(long)
	 * @generated
	 */
	void unsetStart();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.calc.Counter#getStart <em>Start</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start</em>' attribute is set.
	 * @see #unsetStart()
	 * @see #getStart()
	 * @see #setStart(long)
	 * @generated
	 */
	boolean isSetStart();

} // Counter
