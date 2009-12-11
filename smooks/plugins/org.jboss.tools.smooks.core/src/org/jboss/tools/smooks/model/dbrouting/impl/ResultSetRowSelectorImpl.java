/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.dbrouting.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.dbrouting.DbroutingPackage;
import org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Result Set Row Selector</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl#getWhere <em>Where</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl#getFailedSelectError <em>Failed Select Error</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl#getResultSetName <em>Result Set Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ResultSetRowSelectorImpl#getSelectRowOnElement <em>Select Row On Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResultSetRowSelectorImpl extends ElementVisitorImpl implements ResultSetRowSelector {
	/**
	 * The default value of the '{@link #getWhere() <em>Where</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWhere()
	 * @generated
	 * @ordered
	 */
	protected static final String WHERE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWhere() <em>Where</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWhere()
	 * @generated
	 * @ordered
	 */
	protected String where = WHERE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFailedSelectError() <em>Failed Select Error</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFailedSelectError()
	 * @generated
	 * @ordered
	 */
	protected static final String FAILED_SELECT_ERROR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFailedSelectError() <em>Failed Select Error</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFailedSelectError()
	 * @generated
	 * @ordered
	 */
	protected String failedSelectError = FAILED_SELECT_ERROR_EDEFAULT;

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
	 * The default value of the '{@link #isExecuteBefore() <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExecuteBefore()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXECUTE_BEFORE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isExecuteBefore() <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExecuteBefore()
	 * @generated
	 * @ordered
	 */
	protected boolean executeBefore = EXECUTE_BEFORE_EDEFAULT;

	/**
	 * This is true if the Execute Before attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean executeBeforeESet;

	/**
	 * The default value of the '{@link #getResultSetName() <em>Result Set Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSetName()
	 * @generated
	 * @ordered
	 */
	protected static final String RESULT_SET_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getResultSetName() <em>Result Set Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSetName()
	 * @generated
	 * @ordered
	 */
	protected String resultSetName = RESULT_SET_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getSelectRowOnElement() <em>Select Row On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectRowOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String SELECT_ROW_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSelectRowOnElement() <em>Select Row On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSelectRowOnElement()
	 * @generated
	 * @ordered
	 */
	protected String selectRowOnElement = SELECT_ROW_ON_ELEMENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ResultSetRowSelectorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DbroutingPackage.Literals.RESULT_SET_ROW_SELECTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWhere(String newWhere) {
		String oldWhere = where;
		where = newWhere;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.RESULT_SET_ROW_SELECTOR__WHERE, oldWhere, where));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFailedSelectError() {
		return failedSelectError;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFailedSelectError(String newFailedSelectError) {
		String oldFailedSelectError = failedSelectError;
		failedSelectError = newFailedSelectError;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.RESULT_SET_ROW_SELECTOR__FAILED_SELECT_ERROR, oldFailedSelectError, failedSelectError));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.RESULT_SET_ROW_SELECTOR__BEAN_ID, oldBeanId, beanId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExecuteBefore() {
		return executeBefore;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteBefore(boolean newExecuteBefore) {
		boolean oldExecuteBefore = executeBefore;
		executeBefore = newExecuteBefore;
		boolean oldExecuteBeforeESet = executeBeforeESet;
		executeBeforeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.RESULT_SET_ROW_SELECTOR__EXECUTE_BEFORE, oldExecuteBefore, executeBefore, !oldExecuteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetExecuteBefore() {
		boolean oldExecuteBefore = executeBefore;
		boolean oldExecuteBeforeESet = executeBeforeESet;
		executeBefore = EXECUTE_BEFORE_EDEFAULT;
		executeBeforeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DbroutingPackage.RESULT_SET_ROW_SELECTOR__EXECUTE_BEFORE, oldExecuteBefore, EXECUTE_BEFORE_EDEFAULT, oldExecuteBeforeESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetExecuteBefore() {
		return executeBeforeESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getResultSetName() {
		return resultSetName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResultSetName(String newResultSetName) {
		String oldResultSetName = resultSetName;
		resultSetName = newResultSetName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.RESULT_SET_ROW_SELECTOR__RESULT_SET_NAME, oldResultSetName, resultSetName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSelectRowOnElement() {
		return selectRowOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSelectRowOnElement(String newSelectRowOnElement) {
		String oldSelectRowOnElement = selectRowOnElement;
		selectRowOnElement = newSelectRowOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.RESULT_SET_ROW_SELECTOR__SELECT_ROW_ON_ELEMENT, oldSelectRowOnElement, selectRowOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__WHERE:
				return getWhere();
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__FAILED_SELECT_ERROR:
				return getFailedSelectError();
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__BEAN_ID:
				return getBeanId();
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__EXECUTE_BEFORE:
				return isExecuteBefore() ? Boolean.TRUE : Boolean.FALSE;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__RESULT_SET_NAME:
				return getResultSetName();
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__SELECT_ROW_ON_ELEMENT:
				return getSelectRowOnElement();
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
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__WHERE:
				setWhere((String)newValue);
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__FAILED_SELECT_ERROR:
				setFailedSelectError((String)newValue);
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__BEAN_ID:
				setBeanId((String)newValue);
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__EXECUTE_BEFORE:
				setExecuteBefore(((Boolean)newValue).booleanValue());
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__RESULT_SET_NAME:
				setResultSetName((String)newValue);
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__SELECT_ROW_ON_ELEMENT:
				setSelectRowOnElement((String)newValue);
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
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__WHERE:
				setWhere(WHERE_EDEFAULT);
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__FAILED_SELECT_ERROR:
				setFailedSelectError(FAILED_SELECT_ERROR_EDEFAULT);
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__BEAN_ID:
				setBeanId(BEAN_ID_EDEFAULT);
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__EXECUTE_BEFORE:
				unsetExecuteBefore();
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__RESULT_SET_NAME:
				setResultSetName(RESULT_SET_NAME_EDEFAULT);
				return;
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__SELECT_ROW_ON_ELEMENT:
				setSelectRowOnElement(SELECT_ROW_ON_ELEMENT_EDEFAULT);
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
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__WHERE:
				return WHERE_EDEFAULT == null ? where != null : !WHERE_EDEFAULT.equals(where);
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__FAILED_SELECT_ERROR:
				return FAILED_SELECT_ERROR_EDEFAULT == null ? failedSelectError != null : !FAILED_SELECT_ERROR_EDEFAULT.equals(failedSelectError);
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__BEAN_ID:
				return BEAN_ID_EDEFAULT == null ? beanId != null : !BEAN_ID_EDEFAULT.equals(beanId);
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__EXECUTE_BEFORE:
				return isSetExecuteBefore();
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__RESULT_SET_NAME:
				return RESULT_SET_NAME_EDEFAULT == null ? resultSetName != null : !RESULT_SET_NAME_EDEFAULT.equals(resultSetName);
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR__SELECT_ROW_ON_ELEMENT:
				return SELECT_ROW_ON_ELEMENT_EDEFAULT == null ? selectRowOnElement != null : !SELECT_ROW_ON_ELEMENT_EDEFAULT.equals(selectRowOnElement);
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
		result.append(" (where: "); //$NON-NLS-1$
		result.append(where);
		result.append(", failedSelectError: "); //$NON-NLS-1$
		result.append(failedSelectError);
		result.append(", beanId: "); //$NON-NLS-1$
		result.append(beanId);
		result.append(", executeBefore: "); //$NON-NLS-1$
		if (executeBeforeESet) result.append(executeBefore); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", resultSetName: "); //$NON-NLS-1$
		result.append(resultSetName);
		result.append(", selectRowOnElement: "); //$NON-NLS-1$
		result.append(selectRowOnElement);
		result.append(')');
		return result.toString();
	}

} //ResultSetRowSelectorImpl
