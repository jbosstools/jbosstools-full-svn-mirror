/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.jboss.tools.smooks10.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks10.model.smooks.ProfilesType;
import org.jboss.tools.smooks10.model.smooks.SmooksPackage;
import org.jboss.tools.smooks10.model.smooks.SmooksResourceListType;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Resource List Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.SmooksResourceListTypeImpl#getProfiles <em>Profiles</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.SmooksResourceListTypeImpl#getAbstractResourceConfigGroup <em>Abstract Resource Config Group</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.SmooksResourceListTypeImpl#getAbstractResourceConfig <em>Abstract Resource Config</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.SmooksResourceListTypeImpl#getDefaultSelector <em>Default Selector</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.SmooksResourceListTypeImpl#getDefaultSelectorNamespace <em>Default Selector Namespace</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.SmooksResourceListTypeImpl#getDefaultTargetProfile <em>Default Target Profile</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SmooksResourceListTypeImpl extends AbstractTypeImpl implements SmooksResourceListType {
	/**
	 * The cached value of the '{@link #getProfiles() <em>Profiles</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfiles()
	 * @generated
	 * @ordered
	 */
	protected ProfilesType profiles;

	/**
	 * The cached value of the '{@link #getAbstractResourceConfigGroup() <em>Abstract Resource Config Group</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbstractResourceConfigGroup()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap abstractResourceConfigGroup;

	/**
	 * The default value of the '{@link #getDefaultSelector() <em>Default Selector</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultSelector()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_SELECTOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultSelector() <em>Default Selector</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultSelector()
	 * @generated
	 * @ordered
	 */
	protected String defaultSelector = DEFAULT_SELECTOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultSelectorNamespace() <em>Default Selector Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultSelectorNamespace()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_SELECTOR_NAMESPACE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultSelectorNamespace() <em>Default Selector Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultSelectorNamespace()
	 * @generated
	 * @ordered
	 */
	protected String defaultSelectorNamespace = DEFAULT_SELECTOR_NAMESPACE_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultTargetProfile() <em>Default Target Profile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultTargetProfile()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_TARGET_PROFILE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultTargetProfile() <em>Default Target Profile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultTargetProfile()
	 * @generated
	 * @ordered
	 */
	protected String defaultTargetProfile = DEFAULT_TARGET_PROFILE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SmooksResourceListTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfilesType getProfiles() {
		return profiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProfiles(ProfilesType newProfiles, NotificationChain msgs) {
		ProfilesType oldProfiles = profiles;
		profiles = newProfiles;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES, oldProfiles, newProfiles);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProfiles(ProfilesType newProfiles) {
		if (newProfiles != profiles) {
			NotificationChain msgs = null;
			if (profiles != null)
				msgs = ((InternalEObject)profiles).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES, null, msgs);
			if (newProfiles != null)
				msgs = ((InternalEObject)newProfiles).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES, null, msgs);
			msgs = basicSetProfiles(newProfiles, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES, newProfiles, newProfiles));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getAbstractResourceConfigGroup() {
		if (abstractResourceConfigGroup == null) {
			abstractResourceConfigGroup = new BasicFeatureMap(this, SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP);
		}
		return abstractResourceConfigGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AbstractResourceConfig> getAbstractResourceConfig() {
		return getAbstractResourceConfigGroup().list(SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultSelector() {
		return defaultSelector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultSelector(String newDefaultSelector) {
		String oldDefaultSelector = defaultSelector;
		defaultSelector = newDefaultSelector;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR, oldDefaultSelector, defaultSelector));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultSelectorNamespace() {
		return defaultSelectorNamespace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultSelectorNamespace(String newDefaultSelectorNamespace) {
		String oldDefaultSelectorNamespace = defaultSelectorNamespace;
		defaultSelectorNamespace = newDefaultSelectorNamespace;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE, oldDefaultSelectorNamespace, defaultSelectorNamespace));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultTargetProfile() {
		return defaultTargetProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultTargetProfile(String newDefaultTargetProfile) {
		String oldDefaultTargetProfile = defaultTargetProfile;
		defaultTargetProfile = newDefaultTargetProfile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE, oldDefaultTargetProfile, defaultTargetProfile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES:
				return basicSetProfiles(null, msgs);
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP:
				return ((InternalEList<?>)getAbstractResourceConfigGroup()).basicRemove(otherEnd, msgs);
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG:
				return ((InternalEList<?>)getAbstractResourceConfig()).basicRemove(otherEnd, msgs);
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
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES:
				return getProfiles();
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP:
				if (coreType) return getAbstractResourceConfigGroup();
				return ((FeatureMap.Internal)getAbstractResourceConfigGroup()).getWrapper();
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG:
				return getAbstractResourceConfig();
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR:
				return getDefaultSelector();
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE:
				return getDefaultSelectorNamespace();
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE:
				return getDefaultTargetProfile();
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
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES:
				setProfiles((ProfilesType)newValue);
				return;
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP:
				((FeatureMap.Internal)getAbstractResourceConfigGroup()).set(newValue);
				return;
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR:
				setDefaultSelector((String)newValue);
				return;
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE:
				setDefaultSelectorNamespace((String)newValue);
				return;
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE:
				setDefaultTargetProfile((String)newValue);
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
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES:
				setProfiles((ProfilesType)null);
				return;
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP:
				getAbstractResourceConfigGroup().clear();
				return;
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR:
				setDefaultSelector(DEFAULT_SELECTOR_EDEFAULT);
				return;
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE:
				setDefaultSelectorNamespace(DEFAULT_SELECTOR_NAMESPACE_EDEFAULT);
				return;
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE:
				setDefaultTargetProfile(DEFAULT_TARGET_PROFILE_EDEFAULT);
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
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__PROFILES:
				return profiles != null;
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG_GROUP:
				return abstractResourceConfigGroup != null && !abstractResourceConfigGroup.isEmpty();
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_RESOURCE_CONFIG:
				return !getAbstractResourceConfig().isEmpty();
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR:
				return DEFAULT_SELECTOR_EDEFAULT == null ? defaultSelector != null : !DEFAULT_SELECTOR_EDEFAULT.equals(defaultSelector);
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_SELECTOR_NAMESPACE:
				return DEFAULT_SELECTOR_NAMESPACE_EDEFAULT == null ? defaultSelectorNamespace != null : !DEFAULT_SELECTOR_NAMESPACE_EDEFAULT.equals(defaultSelectorNamespace);
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE__DEFAULT_TARGET_PROFILE:
				return DEFAULT_TARGET_PROFILE_EDEFAULT == null ? defaultTargetProfile != null : !DEFAULT_TARGET_PROFILE_EDEFAULT.equals(defaultTargetProfile);
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
		result.append(" (abstractResourceConfigGroup: ");
		result.append(abstractResourceConfigGroup);
		result.append(", defaultSelector: ");
		result.append(defaultSelector);
		result.append(", defaultSelectorNamespace: ");
		result.append(defaultSelectorNamespace);
		result.append(", defaultTargetProfile: ");
		result.append(defaultTargetProfile);
		result.append(')');
		return result.toString();
	}

} //SmooksResourceListTypeImpl
