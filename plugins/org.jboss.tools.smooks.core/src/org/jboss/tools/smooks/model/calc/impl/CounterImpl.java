/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.calc.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.jboss.tools.smooks.model.calc.CalcPackage;
import org.jboss.tools.smooks.model.calc.CountDirection;
import org.jboss.tools.smooks.model.calc.Counter;

import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Counter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#getStartExpression <em>Start Expression</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#getAmountExpression <em>Amount Expression</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#getResetCondition <em>Reset Condition</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#getAmount <em>Amount</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#getCountOnElement <em>Count On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#getCountOnElementNS <em>Count On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#getDirection <em>Direction</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#isExecuteAfter <em>Execute After</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.calc.impl.CounterImpl#getStart <em>Start</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CounterImpl extends ElementVisitorImpl implements Counter {
	/**
	 * The default value of the '{@link #getStartExpression() <em>Start Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String START_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStartExpression() <em>Start Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartExpression()
	 * @generated
	 * @ordered
	 */
	protected String startExpression = START_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getAmountExpression() <em>Amount Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAmountExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String AMOUNT_EXPRESSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAmountExpression() <em>Amount Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAmountExpression()
	 * @generated
	 * @ordered
	 */
	protected String amountExpression = AMOUNT_EXPRESSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getResetCondition() <em>Reset Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResetCondition()
	 * @generated
	 * @ordered
	 */
	protected static final String RESET_CONDITION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getResetCondition() <em>Reset Condition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResetCondition()
	 * @generated
	 * @ordered
	 */
	protected String resetCondition = RESET_CONDITION_EDEFAULT;

	/**
	 * The default value of the '{@link #getAmount() <em>Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAmount()
	 * @generated
	 * @ordered
	 */
	protected static final int AMOUNT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getAmount() <em>Amount</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAmount()
	 * @generated
	 * @ordered
	 */
	protected int amount = AMOUNT_EDEFAULT;

	/**
	 * This is true if the Amount attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean amountESet;

	/**
	 * The default value of the '{@link #getBeanId() <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanId()
	 * @generated
	 * @ordered
	 */
	protected static final String BEAN_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeanId() <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanId()
	 * @generated
	 * @ordered
	 */
	protected String beanId = BEAN_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getCountOnElement() <em>Count On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String COUNT_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCountOnElement() <em>Count On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountOnElement()
	 * @generated
	 * @ordered
	 */
	protected String countOnElement = COUNT_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getCountOnElementNS() <em>Count On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String COUNT_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCountOnElementNS() <em>Count On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCountOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String countOnElementNS = COUNT_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDirection()
	 * @generated not
	 * @ordered
	 */
	protected static final CountDirection DIRECTION_EDEFAULT = null;// CountDirection.INCREMENT;

	/**
	 * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected CountDirection direction = DIRECTION_EDEFAULT;

	/**
	 * This is true if the Direction attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean directionESet;

	/**
	 * The default value of the '{@link #isExecuteAfter() <em>Execute After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExecuteAfter()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXECUTE_AFTER_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExecuteAfter() <em>Execute After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExecuteAfter()
	 * @generated
	 * @ordered
	 */
	protected boolean executeAfter = EXECUTE_AFTER_EDEFAULT;

	/**
	 * This is true if the Execute After attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean executeAfterESet;

	/**
	 * The default value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected static final long START_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected long start = START_EDEFAULT;

	/**
	 * This is true if the Start attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean startESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CounterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CalcPackage.Literals.COUNTER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStartExpression() {
		return startExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartExpression(String newStartExpression) {
		String oldStartExpression = startExpression;
		startExpression = newStartExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__START_EXPRESSION, oldStartExpression, startExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAmountExpression() {
		return amountExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAmountExpression(String newAmountExpression) {
		String oldAmountExpression = amountExpression;
		amountExpression = newAmountExpression;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__AMOUNT_EXPRESSION, oldAmountExpression, amountExpression));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getResetCondition() {
		return resetCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResetCondition(String newResetCondition) {
		String oldResetCondition = resetCondition;
		resetCondition = newResetCondition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__RESET_CONDITION, oldResetCondition, resetCondition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAmount(int newAmount) {
		int oldAmount = amount;
		amount = newAmount;
		boolean oldAmountESet = amountESet;
		amountESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__AMOUNT, oldAmount, amount, !oldAmountESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAmount() {
		int oldAmount = amount;
		boolean oldAmountESet = amountESet;
		amount = AMOUNT_EDEFAULT;
		amountESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CalcPackage.COUNTER__AMOUNT, oldAmount, AMOUNT_EDEFAULT, oldAmountESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAmount() {
		return amountESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeanId() {
		return beanId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanId(String newBeanId) {
		String oldBeanId = beanId;
		beanId = newBeanId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__BEAN_ID, oldBeanId, beanId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCountOnElement() {
		return countOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCountOnElement(String newCountOnElement) {
		String oldCountOnElement = countOnElement;
		countOnElement = newCountOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__COUNT_ON_ELEMENT, oldCountOnElement, countOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCountOnElementNS() {
		return countOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCountOnElementNS(String newCountOnElementNS) {
		String oldCountOnElementNS = countOnElementNS;
		countOnElementNS = newCountOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__COUNT_ON_ELEMENT_NS, oldCountOnElementNS, countOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CountDirection getDirection() {
		return direction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDirection(CountDirection newDirection) {
		CountDirection oldDirection = direction;
		direction = newDirection == null ? DIRECTION_EDEFAULT : newDirection;
		boolean oldDirectionESet = directionESet;
		directionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__DIRECTION, oldDirection, direction, !oldDirectionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDirection() {
		CountDirection oldDirection = direction;
		boolean oldDirectionESet = directionESet;
		direction = DIRECTION_EDEFAULT;
		directionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CalcPackage.COUNTER__DIRECTION, oldDirection, DIRECTION_EDEFAULT, oldDirectionESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDirection() {
		return directionESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExecuteAfter() {
		return executeAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteAfter(boolean newExecuteAfter) {
		boolean oldExecuteAfter = executeAfter;
		executeAfter = newExecuteAfter;
		boolean oldExecuteAfterESet = executeAfterESet;
		executeAfterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__EXECUTE_AFTER, oldExecuteAfter, executeAfter, !oldExecuteAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetExecuteAfter() {
		boolean oldExecuteAfter = executeAfter;
		boolean oldExecuteAfterESet = executeAfterESet;
		executeAfter = EXECUTE_AFTER_EDEFAULT;
		executeAfterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CalcPackage.COUNTER__EXECUTE_AFTER, oldExecuteAfter, EXECUTE_AFTER_EDEFAULT, oldExecuteAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetExecuteAfter() {
		return executeAfterESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getStart() {
		return start;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStart(long newStart) {
		long oldStart = start;
		start = newStart;
		boolean oldStartESet = startESet;
		startESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CalcPackage.COUNTER__START, oldStart, start, !oldStartESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetStart() {
		long oldStart = start;
		boolean oldStartESet = startESet;
		start = START_EDEFAULT;
		startESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CalcPackage.COUNTER__START, oldStart, START_EDEFAULT, oldStartESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetStart() {
		return startESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CalcPackage.COUNTER__START_EXPRESSION:
				return getStartExpression();
			case CalcPackage.COUNTER__AMOUNT_EXPRESSION:
				return getAmountExpression();
			case CalcPackage.COUNTER__RESET_CONDITION:
				return getResetCondition();
			case CalcPackage.COUNTER__AMOUNT:
				return new Integer(getAmount());
			case CalcPackage.COUNTER__BEAN_ID:
				return getBeanId();
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT:
				return getCountOnElement();
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT_NS:
				return getCountOnElementNS();
			case CalcPackage.COUNTER__DIRECTION:
				return getDirection();
			case CalcPackage.COUNTER__EXECUTE_AFTER:
				return isExecuteAfter() ? Boolean.TRUE : Boolean.FALSE;
			case CalcPackage.COUNTER__START:
				return new Long(getStart());
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CalcPackage.COUNTER__START_EXPRESSION:
				setStartExpression((String)newValue);
				return;
			case CalcPackage.COUNTER__AMOUNT_EXPRESSION:
				setAmountExpression((String)newValue);
				return;
			case CalcPackage.COUNTER__RESET_CONDITION:
				setResetCondition((String)newValue);
				return;
			case CalcPackage.COUNTER__AMOUNT:
				setAmount(((Integer)newValue).intValue());
				return;
			case CalcPackage.COUNTER__BEAN_ID:
				setBeanId((String)newValue);
				return;
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT:
				setCountOnElement((String)newValue);
				return;
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT_NS:
				setCountOnElementNS((String)newValue);
				return;
			case CalcPackage.COUNTER__DIRECTION:
				setDirection((CountDirection)newValue);
				return;
			case CalcPackage.COUNTER__EXECUTE_AFTER:
				setExecuteAfter(((Boolean)newValue).booleanValue());
				return;
			case CalcPackage.COUNTER__START:
				setStart(((Long)newValue).longValue());
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case CalcPackage.COUNTER__START_EXPRESSION:
				setStartExpression(START_EXPRESSION_EDEFAULT);
				return;
			case CalcPackage.COUNTER__AMOUNT_EXPRESSION:
				setAmountExpression(AMOUNT_EXPRESSION_EDEFAULT);
				return;
			case CalcPackage.COUNTER__RESET_CONDITION:
				setResetCondition(RESET_CONDITION_EDEFAULT);
				return;
			case CalcPackage.COUNTER__AMOUNT:
				unsetAmount();
				return;
			case CalcPackage.COUNTER__BEAN_ID:
				setBeanId(BEAN_ID_EDEFAULT);
				return;
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT:
				setCountOnElement(COUNT_ON_ELEMENT_EDEFAULT);
				return;
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT_NS:
				setCountOnElementNS(COUNT_ON_ELEMENT_NS_EDEFAULT);
				return;
			case CalcPackage.COUNTER__DIRECTION:
				unsetDirection();
				return;
			case CalcPackage.COUNTER__EXECUTE_AFTER:
				unsetExecuteAfter();
				return;
			case CalcPackage.COUNTER__START:
				unsetStart();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case CalcPackage.COUNTER__START_EXPRESSION:
				return START_EXPRESSION_EDEFAULT == null ? startExpression != null : !START_EXPRESSION_EDEFAULT.equals(startExpression);
			case CalcPackage.COUNTER__AMOUNT_EXPRESSION:
				return AMOUNT_EXPRESSION_EDEFAULT == null ? amountExpression != null : !AMOUNT_EXPRESSION_EDEFAULT.equals(amountExpression);
			case CalcPackage.COUNTER__RESET_CONDITION:
				return RESET_CONDITION_EDEFAULT == null ? resetCondition != null : !RESET_CONDITION_EDEFAULT.equals(resetCondition);
			case CalcPackage.COUNTER__AMOUNT:
				return isSetAmount();
			case CalcPackage.COUNTER__BEAN_ID:
				return BEAN_ID_EDEFAULT == null ? beanId != null : !BEAN_ID_EDEFAULT.equals(beanId);
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT:
				return COUNT_ON_ELEMENT_EDEFAULT == null ? countOnElement != null : !COUNT_ON_ELEMENT_EDEFAULT.equals(countOnElement);
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT_NS:
				return COUNT_ON_ELEMENT_NS_EDEFAULT == null ? countOnElementNS != null : !COUNT_ON_ELEMENT_NS_EDEFAULT.equals(countOnElementNS);
			case CalcPackage.COUNTER__DIRECTION:
				return isSetDirection();
			case CalcPackage.COUNTER__EXECUTE_AFTER:
				return isSetExecuteAfter();
			case CalcPackage.COUNTER__START:
				return isSetStart();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (startExpression: "); //$NON-NLS-1$
		result.append(startExpression);
		result.append(", amountExpression: "); //$NON-NLS-1$
		result.append(amountExpression);
		result.append(", resetCondition: "); //$NON-NLS-1$
		result.append(resetCondition);
		result.append(", amount: "); //$NON-NLS-1$
		if (amountESet) result.append(amount); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", beanId: "); //$NON-NLS-1$
		result.append(beanId);
		result.append(", countOnElement: "); //$NON-NLS-1$
		result.append(countOnElement);
		result.append(", countOnElementNS: "); //$NON-NLS-1$
		result.append(countOnElementNS);
		result.append(", direction: "); //$NON-NLS-1$
		if (directionESet) result.append(direction); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", executeAfter: "); //$NON-NLS-1$
		if (executeAfterESet) result.append(executeAfter); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", start: "); //$NON-NLS-1$
		if (startESet) result.append(start); else result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

} //CounterImpl
