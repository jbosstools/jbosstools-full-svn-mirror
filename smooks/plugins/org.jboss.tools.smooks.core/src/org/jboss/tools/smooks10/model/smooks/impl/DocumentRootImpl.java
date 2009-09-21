/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks.impl;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
import org.jboss.tools.smooks10.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks10.model.smooks.ConditionType;
import org.jboss.tools.smooks10.model.smooks.ImportType;
import org.jboss.tools.smooks10.model.smooks.ParamType;
import org.jboss.tools.smooks10.model.smooks.ProfileType;
import org.jboss.tools.smooks10.model.smooks.ProfilesType;
import org.jboss.tools.smooks10.model.smooks.ResourceConfigType;
import org.jboss.tools.smooks10.model.smooks.ResourceType;
import org.jboss.tools.smooks10.model.smooks.DocumentRoot;
import org.jboss.tools.smooks10.model.smooks.SmooksPackage;
import org.jboss.tools.smooks10.model.smooks.SmooksResourceListType;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Smooks10 Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getAbstractResourceConfig <em>Abstract Resource Config</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getCondition <em>Condition</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getImport <em>Import</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getParam <em>Param</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getProfile <em>Profile</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getProfiles <em>Profiles</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getResource <em>Resource</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getResourceConfig <em>Resource Config</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.impl.DocumentRootImpl#getSmooksResourceList <em>Smooks Resource List</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
	/**
	 * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMixed()
	 * @generated
	 * @ordered
	 */
	protected FeatureMap mixed;

	/**
	 * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXMLNSPrefixMap()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> xMLNSPrefixMap;

	/**
	 * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXSISchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, String> xSISchemaLocation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, SmooksPackage.SMOOKS10_DOCUMENT_ROOT__MIXED);
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, String> getXMLNSPrefixMap() {
		if (xMLNSPrefixMap == null) {
			xMLNSPrefixMap = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		}
		return xMLNSPrefixMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, String> getXSISchemaLocation() {
		if (xSISchemaLocation == null) {
			xSISchemaLocation = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractResourceConfig getAbstractResourceConfig() {
		return (AbstractResourceConfig)getMixed().get(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__ABSTRACT_RESOURCE_CONFIG, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAbstractResourceConfig(AbstractResourceConfig newAbstractResourceConfig, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__ABSTRACT_RESOURCE_CONFIG, newAbstractResourceConfig, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConditionType getCondition() {
		return (ConditionType)getMixed().get(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__CONDITION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCondition(ConditionType newCondition, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__CONDITION, newCondition, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCondition(ConditionType newCondition) {
		((FeatureMap.Internal)getMixed()).set(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__CONDITION, newCondition);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImportType getImport() {
		return (ImportType)getMixed().get(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__IMPORT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetImport(ImportType newImport, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__IMPORT, newImport, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setImport(ImportType newImport) {
		((FeatureMap.Internal)getMixed()).set(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__IMPORT, newImport);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParamType getParam() {
		return (ParamType)getMixed().get(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__PARAM, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParam(ParamType newParam, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__PARAM, newParam, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParam(ParamType newParam) {
		((FeatureMap.Internal)getMixed()).set(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__PARAM, newParam);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfileType getProfile() {
		return (ProfileType)getMixed().get(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__PROFILE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProfile(ProfileType newProfile, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__PROFILE, newProfile, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProfile(ProfileType newProfile) {
		((FeatureMap.Internal)getMixed()).set(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__PROFILE, newProfile);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfilesType getProfiles() {
		return (ProfilesType)getMixed().get(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__PROFILES, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProfiles(ProfilesType newProfiles, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__PROFILES, newProfiles, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProfiles(ProfilesType newProfiles) {
		((FeatureMap.Internal)getMixed()).set(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__PROFILES, newProfiles);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceType getResource() {
		return (ResourceType)getMixed().get(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__RESOURCE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResource(ResourceType newResource, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__RESOURCE, newResource, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResource(ResourceType newResource) {
		((FeatureMap.Internal)getMixed()).set(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__RESOURCE, newResource);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceConfigType getResourceConfig() {
		return (ResourceConfigType)getMixed().get(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__RESOURCE_CONFIG, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetResourceConfig(ResourceConfigType newResourceConfig, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__RESOURCE_CONFIG, newResourceConfig, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResourceConfig(ResourceConfigType newResourceConfig) {
		((FeatureMap.Internal)getMixed()).set(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__RESOURCE_CONFIG, newResourceConfig);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksResourceListType getSmooksResourceList() {
		return (SmooksResourceListType)getMixed().get(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSmooksResourceList(SmooksResourceListType newSmooksResourceList, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST, newSmooksResourceList, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSmooksResourceList(SmooksResourceListType newSmooksResourceList) {
		((FeatureMap.Internal)getMixed()).set(SmooksPackage.Literals.SMOOKS10_DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST, newSmooksResourceList);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__MIXED:
				return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__ABSTRACT_RESOURCE_CONFIG:
				return basicSetAbstractResourceConfig(null, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__CONDITION:
				return basicSetCondition(null, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__IMPORT:
				return basicSetImport(null, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PARAM:
				return basicSetParam(null, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILE:
				return basicSetProfile(null, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILES:
				return basicSetProfiles(null, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE:
				return basicSetResource(null, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE_CONFIG:
				return basicSetResourceConfig(null, msgs);
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST:
				return basicSetSmooksResourceList(null, msgs);
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
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__MIXED:
				if (coreType) return getMixed();
				return ((FeatureMap.Internal)getMixed()).getWrapper();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				if (coreType) return getXMLNSPrefixMap();
				else return getXMLNSPrefixMap().map();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				if (coreType) return getXSISchemaLocation();
				else return getXSISchemaLocation().map();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__ABSTRACT_RESOURCE_CONFIG:
				return getAbstractResourceConfig();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__CONDITION:
				return getCondition();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__IMPORT:
				return getImport();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PARAM:
				return getParam();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILE:
				return getProfile();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILES:
				return getProfiles();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE:
				return getResource();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE_CONFIG:
				return getResourceConfig();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST:
				return getSmooksResourceList();
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
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__MIXED:
				((FeatureMap.Internal)getMixed()).set(newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__CONDITION:
				setCondition((ConditionType)newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__IMPORT:
				setImport((ImportType)newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PARAM:
				setParam((ParamType)newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILE:
				setProfile((ProfileType)newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILES:
				setProfiles((ProfilesType)newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE:
				setResource((ResourceType)newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE_CONFIG:
				setResourceConfig((ResourceConfigType)newValue);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST:
				setSmooksResourceList((SmooksResourceListType)newValue);
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
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__CONDITION:
				setCondition((ConditionType)null);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__IMPORT:
				setImport((ImportType)null);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PARAM:
				setParam((ParamType)null);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILE:
				setProfile((ProfileType)null);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILES:
				setProfiles((ProfilesType)null);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE:
				setResource((ResourceType)null);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE_CONFIG:
				setResourceConfig((ResourceConfigType)null);
				return;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST:
				setSmooksResourceList((SmooksResourceListType)null);
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
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__ABSTRACT_RESOURCE_CONFIG:
				return getAbstractResourceConfig() != null;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__CONDITION:
				return getCondition() != null;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__IMPORT:
				return getImport() != null;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PARAM:
				return getParam() != null;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILE:
				return getProfile() != null;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__PROFILES:
				return getProfiles() != null;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE:
				return getResource() != null;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__RESOURCE_CONFIG:
				return getResourceConfig() != null;
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST:
				return getSmooksResourceList() != null;
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
		result.append(" (mixed: ");
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} //Smooks10DocumentRootImpl
