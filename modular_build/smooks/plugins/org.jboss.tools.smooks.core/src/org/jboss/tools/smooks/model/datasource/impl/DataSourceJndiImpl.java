/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.datasource.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.datasource.DataSourceJndi;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Source Jndi</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceJndiImpl#isAutoCommit <em>Auto Commit</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceJndiImpl#getBindOnElement <em>Bind On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceJndiImpl#getDatasource <em>Datasource</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataSourceJndiImpl extends ElementVisitorImpl implements DataSourceJndi {
	/**
	 * The default value of the '{@link #isAutoCommit() <em>Auto Commit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutoCommit()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AUTO_COMMIT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAutoCommit() <em>Auto Commit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAutoCommit()
	 * @generated
	 * @ordered
	 */
	protected boolean autoCommit = AUTO_COMMIT_EDEFAULT;

	/**
	 * This is true if the Auto Commit attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean autoCommitESet;

	/**
	 * The default value of the '{@link #getBindOnElement() <em>Bind On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBindOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String BIND_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBindOnElement() <em>Bind On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBindOnElement()
	 * @generated
	 * @ordered
	 */
	protected String bindOnElement = BIND_ON_ELEMENT_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataSourceJndiImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatasourcePackage.Literals.DATA_SOURCE_JNDI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAutoCommit() {
		return autoCommit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAutoCommit(boolean newAutoCommit) {
		boolean oldAutoCommit = autoCommit;
		autoCommit = newAutoCommit;
		boolean oldAutoCommitESet = autoCommitESet;
		autoCommitESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DATA_SOURCE_JNDI__AUTO_COMMIT, oldAutoCommit, autoCommit, !oldAutoCommitESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAutoCommit() {
		boolean oldAutoCommit = autoCommit;
		boolean oldAutoCommitESet = autoCommitESet;
		autoCommit = AUTO_COMMIT_EDEFAULT;
		autoCommitESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DatasourcePackage.DATA_SOURCE_JNDI__AUTO_COMMIT, oldAutoCommit, AUTO_COMMIT_EDEFAULT, oldAutoCommitESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAutoCommit() {
		return autoCommitESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBindOnElement() {
		return bindOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBindOnElement(String newBindOnElement) {
		String oldBindOnElement = bindOnElement;
		bindOnElement = newBindOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DATA_SOURCE_JNDI__BIND_ON_ELEMENT, oldBindOnElement, bindOnElement));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DATA_SOURCE_JNDI__DATASOURCE, oldDatasource, datasource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DatasourcePackage.DATA_SOURCE_JNDI__AUTO_COMMIT:
				return isAutoCommit() ? Boolean.TRUE : Boolean.FALSE;
			case DatasourcePackage.DATA_SOURCE_JNDI__BIND_ON_ELEMENT:
				return getBindOnElement();
			case DatasourcePackage.DATA_SOURCE_JNDI__DATASOURCE:
				return getDatasource();
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
			case DatasourcePackage.DATA_SOURCE_JNDI__AUTO_COMMIT:
				setAutoCommit(((Boolean)newValue).booleanValue());
				return;
			case DatasourcePackage.DATA_SOURCE_JNDI__BIND_ON_ELEMENT:
				setBindOnElement((String)newValue);
				return;
			case DatasourcePackage.DATA_SOURCE_JNDI__DATASOURCE:
				setDatasource((String)newValue);
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
			case DatasourcePackage.DATA_SOURCE_JNDI__AUTO_COMMIT:
				unsetAutoCommit();
				return;
			case DatasourcePackage.DATA_SOURCE_JNDI__BIND_ON_ELEMENT:
				setBindOnElement(BIND_ON_ELEMENT_EDEFAULT);
				return;
			case DatasourcePackage.DATA_SOURCE_JNDI__DATASOURCE:
				setDatasource(DATASOURCE_EDEFAULT);
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
			case DatasourcePackage.DATA_SOURCE_JNDI__AUTO_COMMIT:
				return isSetAutoCommit();
			case DatasourcePackage.DATA_SOURCE_JNDI__BIND_ON_ELEMENT:
				return BIND_ON_ELEMENT_EDEFAULT == null ? bindOnElement != null : !BIND_ON_ELEMENT_EDEFAULT.equals(bindOnElement);
			case DatasourcePackage.DATA_SOURCE_JNDI__DATASOURCE:
				return DATASOURCE_EDEFAULT == null ? datasource != null : !DATASOURCE_EDEFAULT.equals(datasource);
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
		result.append(" (autoCommit: "); //$NON-NLS-1$
		if (autoCommitESet) result.append(autoCommit); else result.append("<unset>"); //$NON-NLS-1$
		result.append(", bindOnElement: "); //$NON-NLS-1$
		result.append(bindOnElement);
		result.append(", datasource: "); //$NON-NLS-1$
		result.append(datasource);
		result.append(')');
		return result.toString();
	}

} //DataSourceJndiImpl
