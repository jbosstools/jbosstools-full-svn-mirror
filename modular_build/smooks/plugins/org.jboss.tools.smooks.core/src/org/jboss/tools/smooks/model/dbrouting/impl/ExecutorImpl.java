/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.dbrouting.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.dbrouting.DbroutingPackage;
import org.jboss.tools.smooks.model.dbrouting.Executor;
import org.jboss.tools.smooks.model.dbrouting.ResultSet;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Executor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl#getStatement <em>Statement</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl#getResultSet <em>Result Set</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl#getDatasource <em>Datasource</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl#getExecuteOnElement <em>Execute On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.impl.ExecutorImpl#getExecuteOnElementNS <em>Execute On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExecutorImpl extends ElementVisitorImpl implements Executor {
	/**
	 * The default value of the '{@link #getStatement() <em>Statement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatement()
	 * @generated
	 * @ordered
	 */
	protected static final String STATEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStatement() <em>Statement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatement()
	 * @generated
	 * @ordered
	 */
	protected String statement = STATEMENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getResultSet() <em>Result Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSet()
	 * @generated
	 * @ordered
	 */
	protected ResultSet resultSet;

	/**
	 * The default value of the '{@link #getDatasource() <em>Datasource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDatasource()
	 * @generated
	 * @ordered
	 */
	protected static final String DATASOURCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDatasource() <em>Datasource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDatasource()
	 * @generated
	 * @ordered
	 */
	protected String datasource = DATASOURCE_EDEFAULT;

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
	 * The default value of the '{@link #getExecuteOnElement() <em>Execute On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String EXECUTE_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecuteOnElement() <em>Execute On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnElement()
	 * @generated
	 * @ordered
	 */
	protected String executeOnElement = EXECUTE_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getExecuteOnElementNS() <em>Execute On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String EXECUTE_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getExecuteOnElementNS() <em>Execute On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecuteOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String executeOnElementNS = EXECUTE_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExecutorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DbroutingPackage.Literals.EXECUTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getStatement() {
		return statement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStatement(String newStatement) {
		String oldStatement = statement;
		statement = newStatement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.EXECUTOR__STATEMENT, oldStatement, statement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResultSet(ResultSet newResultSet, NotificationChain msgs) {
		ResultSet oldResultSet = resultSet;
		resultSet = newResultSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DbroutingPackage.EXECUTOR__RESULT_SET, oldResultSet, newResultSet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResultSet(ResultSet newResultSet) {
		if (newResultSet != resultSet) {
			NotificationChain msgs = null;
			if (resultSet != null)
				msgs = ((InternalEObject)resultSet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DbroutingPackage.EXECUTOR__RESULT_SET, null, msgs);
			if (newResultSet != null)
				msgs = ((InternalEObject)newResultSet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DbroutingPackage.EXECUTOR__RESULT_SET, null, msgs);
			msgs = basicSetResultSet(newResultSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.EXECUTOR__RESULT_SET, newResultSet, newResultSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDatasource() {
		return datasource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDatasource(String newDatasource) {
		String oldDatasource = datasource;
		datasource = newDatasource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.EXECUTOR__DATASOURCE, oldDatasource, datasource));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.EXECUTOR__EXECUTE_BEFORE, oldExecuteBefore, executeBefore, !oldExecuteBeforeESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, DbroutingPackage.EXECUTOR__EXECUTE_BEFORE, oldExecuteBefore, EXECUTE_BEFORE_EDEFAULT, oldExecuteBeforeESet));
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
	public String getExecuteOnElement() {
		return executeOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteOnElement(String newExecuteOnElement) {
		String oldExecuteOnElement = executeOnElement;
		executeOnElement = newExecuteOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT, oldExecuteOnElement, executeOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getExecuteOnElementNS() {
		return executeOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecuteOnElementNS(String newExecuteOnElementNS) {
		String oldExecuteOnElementNS = executeOnElementNS;
		executeOnElementNS = newExecuteOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT_NS, oldExecuteOnElementNS, executeOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DbroutingPackage.EXECUTOR__RESULT_SET:
				return basicSetResultSet(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DbroutingPackage.EXECUTOR__STATEMENT:
				return getStatement();
			case DbroutingPackage.EXECUTOR__RESULT_SET:
				return getResultSet();
			case DbroutingPackage.EXECUTOR__DATASOURCE:
				return getDatasource();
			case DbroutingPackage.EXECUTOR__EXECUTE_BEFORE:
				return isExecuteBefore() ? Boolean.TRUE : Boolean.FALSE;
			case DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT:
				return getExecuteOnElement();
			case DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT_NS:
				return getExecuteOnElementNS();
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
			case DbroutingPackage.EXECUTOR__STATEMENT:
				setStatement((String)newValue);
				return;
			case DbroutingPackage.EXECUTOR__RESULT_SET:
				setResultSet((ResultSet)newValue);
				return;
			case DbroutingPackage.EXECUTOR__DATASOURCE:
				setDatasource((String)newValue);
				return;
			case DbroutingPackage.EXECUTOR__EXECUTE_BEFORE:
				setExecuteBefore(((Boolean)newValue).booleanValue());
				return;
			case DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT:
				setExecuteOnElement((String)newValue);
				return;
			case DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT_NS:
				setExecuteOnElementNS((String)newValue);
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
			case DbroutingPackage.EXECUTOR__STATEMENT:
				setStatement(STATEMENT_EDEFAULT);
				return;
			case DbroutingPackage.EXECUTOR__RESULT_SET:
				setResultSet((ResultSet)null);
				return;
			case DbroutingPackage.EXECUTOR__DATASOURCE:
				setDatasource(DATASOURCE_EDEFAULT);
				return;
			case DbroutingPackage.EXECUTOR__EXECUTE_BEFORE:
				unsetExecuteBefore();
				return;
			case DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT:
				setExecuteOnElement(EXECUTE_ON_ELEMENT_EDEFAULT);
				return;
			case DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT_NS:
				setExecuteOnElementNS(EXECUTE_ON_ELEMENT_NS_EDEFAULT);
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
			case DbroutingPackage.EXECUTOR__STATEMENT:
				return STATEMENT_EDEFAULT == null ? statement != null : !STATEMENT_EDEFAULT.equals(statement);
			case DbroutingPackage.EXECUTOR__RESULT_SET:
				return resultSet != null;
			case DbroutingPackage.EXECUTOR__DATASOURCE:
				return DATASOURCE_EDEFAULT == null ? datasource != null : !DATASOURCE_EDEFAULT.equals(datasource);
			case DbroutingPackage.EXECUTOR__EXECUTE_BEFORE:
				return isSetExecuteBefore();
			case DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT:
				return EXECUTE_ON_ELEMENT_EDEFAULT == null ? executeOnElement != null : !EXECUTE_ON_ELEMENT_EDEFAULT.equals(executeOnElement);
			case DbroutingPackage.EXECUTOR__EXECUTE_ON_ELEMENT_NS:
				return EXECUTE_ON_ELEMENT_NS_EDEFAULT == null ? executeOnElementNS != null : !EXECUTE_ON_ELEMENT_NS_EDEFAULT.equals(executeOnElementNS);
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
		result.append(" (statement: "); //$NON-NLS-1$
		result.append(statement);
		result.append(", datasource: "); //$NON-NLS-1$
		result.append(datasource);
		result.append(", executeBefore: "); //$NON-NLS-1$
		if (executeBeforeESet) result.append(executeBefore); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", executeOnElement: "); //$NON-NLS-1$
		result.append(executeOnElement);
		result.append(", executeOnElementNS: "); //$NON-NLS-1$
		result.append(executeOnElementNS);
		result.append(')');
		return result.toString();
	}

} //ExecutorImpl
