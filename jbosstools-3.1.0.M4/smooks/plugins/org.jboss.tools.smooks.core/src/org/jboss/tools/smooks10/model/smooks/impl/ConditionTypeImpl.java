/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks10.model.smooks.ConditionType;
import org.jboss.tools.smooks10.model.smooks.SmooksPackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Condition Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.ConditionTypeImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.ConditionTypeImpl#getEvaluator <em>Evaluator</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConditionTypeImpl extends AbstractTypeImpl implements ConditionType {
	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final String VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected String value = VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getEvaluator() <em>Evaluator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvaluator()
	 * @generated
	 * @ordered
	 */
	protected static final String EVALUATOR_EDEFAULT = "org.milyn.javabean.expression.BeanMapExpressionEvaluator";

	/**
	 * The cached value of the '{@link #getEvaluator() <em>Evaluator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvaluator()
	 * @generated
	 * @ordered
	 */
	protected String evaluator = EVALUATOR_EDEFAULT;

	/**
	 * This is true if the Evaluator attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean evaluatorESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConditionTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SmooksPackage.Literals.CONDITION_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setValue(String newValue) {
		String oldValue = value;
		value = newValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmooksPackage.CONDITION_TYPE__VALUE, oldValue, value));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getEvaluator() {
		return evaluator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEvaluator(String newEvaluator) {
		String oldEvaluator = evaluator;
		evaluator = newEvaluator;
		boolean oldEvaluatorESet = evaluatorESet;
		evaluatorESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmooksPackage.CONDITION_TYPE__EVALUATOR, oldEvaluator, evaluator, !oldEvaluatorESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetEvaluator() {
		String oldEvaluator = evaluator;
		boolean oldEvaluatorESet = evaluatorESet;
		evaluator = EVALUATOR_EDEFAULT;
		evaluatorESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SmooksPackage.CONDITION_TYPE__EVALUATOR, oldEvaluator, EVALUATOR_EDEFAULT, oldEvaluatorESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetEvaluator() {
		return evaluatorESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SmooksPackage.CONDITION_TYPE__VALUE:
				return getValue();
			case SmooksPackage.CONDITION_TYPE__EVALUATOR:
				return getEvaluator();
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
			case SmooksPackage.CONDITION_TYPE__VALUE:
				setValue((String)newValue);
				return;
			case SmooksPackage.CONDITION_TYPE__EVALUATOR:
				setEvaluator((String)newValue);
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
			case SmooksPackage.CONDITION_TYPE__VALUE:
				setValue(VALUE_EDEFAULT);
				return;
			case SmooksPackage.CONDITION_TYPE__EVALUATOR:
				unsetEvaluator();
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
			case SmooksPackage.CONDITION_TYPE__VALUE:
				return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
			case SmooksPackage.CONDITION_TYPE__EVALUATOR:
				return isSetEvaluator();
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
		result.append(" (value: ");
		result.append(value);
		result.append(", evaluator: ");
		if (evaluatorESet) result.append(evaluator); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ConditionTypeImpl
