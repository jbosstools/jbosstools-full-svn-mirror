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
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.datasource.Direct;
import org.jboss.tools.smooks.model.smooks.impl.ElementVisitorImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Direct</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl#isAutoCommit <em>Auto Commit</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl#getBindOnElement <em>Bind On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl#getBindOnElementNS <em>Bind On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl#getDatasource <em>Datasource</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl#getDriver <em>Driver</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl#getPassword <em>Password</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl#getUrl <em>Url</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DirectImpl#getUsername <em>Username</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DirectImpl extends ElementVisitorImpl implements Direct {
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
	 * The default value of the '{@link #getBindOnElementNS() <em>Bind On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBindOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String BIND_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBindOnElementNS() <em>Bind On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBindOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String bindOnElementNS = BIND_ON_ELEMENT_NS_EDEFAULT;

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
	 * The default value of the '{@link #getDriver() <em>Driver</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDriver()
	 * @generated
	 * @ordered
	 */
	protected static final String DRIVER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDriver() <em>Driver</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDriver()
	 * @generated
	 * @ordered
	 */
	protected String driver = DRIVER_EDEFAULT;

	/**
	 * The default value of the '{@link #getPassword() <em>Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPassword()
	 * @generated
	 * @ordered
	 */
	protected static final String PASSWORD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPassword() <em>Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPassword()
	 * @generated
	 * @ordered
	 */
	protected String password = PASSWORD_EDEFAULT;

	/**
	 * The default value of the '{@link #getUrl() <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUrl()
	 * @generated
	 * @ordered
	 */
	protected static final String URL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUrl() <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUrl()
	 * @generated
	 * @ordered
	 */
	protected String url = URL_EDEFAULT;

	/**
	 * The default value of the '{@link #getUsername() <em>Username</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUsername()
	 * @generated
	 * @ordered
	 */
	protected static final String USERNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUsername() <em>Username</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUsername()
	 * @generated
	 * @ordered
	 */
	protected String username = USERNAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DirectImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatasourcePackage.Literals.DIRECT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DIRECT__AUTO_COMMIT, oldAutoCommit, autoCommit, !oldAutoCommitESet));
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
			eNotify(new ENotificationImpl(this, Notification.UNSET, DatasourcePackage.DIRECT__AUTO_COMMIT, oldAutoCommit, AUTO_COMMIT_EDEFAULT, oldAutoCommitESet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DIRECT__BIND_ON_ELEMENT, oldBindOnElement, bindOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBindOnElementNS() {
		return bindOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBindOnElementNS(String newBindOnElementNS) {
		String oldBindOnElementNS = bindOnElementNS;
		bindOnElementNS = newBindOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DIRECT__BIND_ON_ELEMENT_NS, oldBindOnElementNS, bindOnElementNS));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DIRECT__DATASOURCE, oldDatasource, datasource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDriver(String newDriver) {
		String oldDriver = driver;
		driver = newDriver;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DIRECT__DRIVER, oldDriver, driver));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPassword(String newPassword) {
		String oldPassword = password;
		password = newPassword;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DIRECT__PASSWORD, oldPassword, password));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUrl(String newUrl) {
		String oldUrl = url;
		url = newUrl;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DIRECT__URL, oldUrl, url));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUsername(String newUsername) {
		String oldUsername = username;
		username = newUsername;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DatasourcePackage.DIRECT__USERNAME, oldUsername, username));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DatasourcePackage.DIRECT__AUTO_COMMIT:
				return isAutoCommit() ? Boolean.TRUE : Boolean.FALSE;
			case DatasourcePackage.DIRECT__BIND_ON_ELEMENT:
				return getBindOnElement();
			case DatasourcePackage.DIRECT__BIND_ON_ELEMENT_NS:
				return getBindOnElementNS();
			case DatasourcePackage.DIRECT__DATASOURCE:
				return getDatasource();
			case DatasourcePackage.DIRECT__DRIVER:
				return getDriver();
			case DatasourcePackage.DIRECT__PASSWORD:
				return getPassword();
			case DatasourcePackage.DIRECT__URL:
				return getUrl();
			case DatasourcePackage.DIRECT__USERNAME:
				return getUsername();
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
			case DatasourcePackage.DIRECT__AUTO_COMMIT:
				setAutoCommit(((Boolean)newValue).booleanValue());
				return;
			case DatasourcePackage.DIRECT__BIND_ON_ELEMENT:
				setBindOnElement((String)newValue);
				return;
			case DatasourcePackage.DIRECT__BIND_ON_ELEMENT_NS:
				setBindOnElementNS((String)newValue);
				return;
			case DatasourcePackage.DIRECT__DATASOURCE:
				setDatasource((String)newValue);
				return;
			case DatasourcePackage.DIRECT__DRIVER:
				setDriver((String)newValue);
				return;
			case DatasourcePackage.DIRECT__PASSWORD:
				setPassword((String)newValue);
				return;
			case DatasourcePackage.DIRECT__URL:
				setUrl((String)newValue);
				return;
			case DatasourcePackage.DIRECT__USERNAME:
				setUsername((String)newValue);
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
			case DatasourcePackage.DIRECT__AUTO_COMMIT:
				unsetAutoCommit();
				return;
			case DatasourcePackage.DIRECT__BIND_ON_ELEMENT:
				setBindOnElement(BIND_ON_ELEMENT_EDEFAULT);
				return;
			case DatasourcePackage.DIRECT__BIND_ON_ELEMENT_NS:
				setBindOnElementNS(BIND_ON_ELEMENT_NS_EDEFAULT);
				return;
			case DatasourcePackage.DIRECT__DATASOURCE:
				setDatasource(DATASOURCE_EDEFAULT);
				return;
			case DatasourcePackage.DIRECT__DRIVER:
				setDriver(DRIVER_EDEFAULT);
				return;
			case DatasourcePackage.DIRECT__PASSWORD:
				setPassword(PASSWORD_EDEFAULT);
				return;
			case DatasourcePackage.DIRECT__URL:
				setUrl(URL_EDEFAULT);
				return;
			case DatasourcePackage.DIRECT__USERNAME:
				setUsername(USERNAME_EDEFAULT);
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
			case DatasourcePackage.DIRECT__AUTO_COMMIT:
				return isSetAutoCommit();
			case DatasourcePackage.DIRECT__BIND_ON_ELEMENT:
				return BIND_ON_ELEMENT_EDEFAULT == null ? bindOnElement != null : !BIND_ON_ELEMENT_EDEFAULT.equals(bindOnElement);
			case DatasourcePackage.DIRECT__BIND_ON_ELEMENT_NS:
				return BIND_ON_ELEMENT_NS_EDEFAULT == null ? bindOnElementNS != null : !BIND_ON_ELEMENT_NS_EDEFAULT.equals(bindOnElementNS);
			case DatasourcePackage.DIRECT__DATASOURCE:
				return DATASOURCE_EDEFAULT == null ? datasource != null : !DATASOURCE_EDEFAULT.equals(datasource);
			case DatasourcePackage.DIRECT__DRIVER:
				return DRIVER_EDEFAULT == null ? driver != null : !DRIVER_EDEFAULT.equals(driver);
			case DatasourcePackage.DIRECT__PASSWORD:
				return PASSWORD_EDEFAULT == null ? password != null : !PASSWORD_EDEFAULT.equals(password);
			case DatasourcePackage.DIRECT__URL:
				return URL_EDEFAULT == null ? url != null : !URL_EDEFAULT.equals(url);
			case DatasourcePackage.DIRECT__USERNAME:
				return USERNAME_EDEFAULT == null ? username != null : !USERNAME_EDEFAULT.equals(username);
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
		result.append(", bindOnElementNS: "); //$NON-NLS-1$
		result.append(bindOnElementNS);
		result.append(", datasource: "); //$NON-NLS-1$
		result.append(datasource);
		result.append(", driver: "); //$NON-NLS-1$
		result.append(driver);
		result.append(", password: "); //$NON-NLS-1$
		result.append(password);
		result.append(", url: "); //$NON-NLS-1$
		result.append(url);
		result.append(", username: "); //$NON-NLS-1$
		result.append(username);
		result.append(')');
		return result.toString();
	}

} //DirectImpl
