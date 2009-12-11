/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.datasource.impl;


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
import org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot;
import org.jboss.tools.smooks.model.datasource.DataSourceJndi;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.datasource.Direct;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Source Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceDocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceDocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceDocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceDocumentRootImpl#getDirect <em>Direct</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.impl.DataSourceDocumentRootImpl#getJNDI <em>JNDI</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataSourceDocumentRootImpl extends EObjectImpl implements DataSourceDocumentRoot {
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
	protected DataSourceDocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DatasourcePackage.Literals.DATA_SOURCE_DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__MIXED);
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
			xMLNSPrefixMap = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
			xSISchemaLocation = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Direct getDirect() {
		return (Direct)getMixed().get(DatasourcePackage.Literals.DATA_SOURCE_DOCUMENT_ROOT__DIRECT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDirect(Direct newDirect, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(DatasourcePackage.Literals.DATA_SOURCE_DOCUMENT_ROOT__DIRECT, newDirect, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDirect(Direct newDirect) {
		((FeatureMap.Internal)getMixed()).set(DatasourcePackage.Literals.DATA_SOURCE_DOCUMENT_ROOT__DIRECT, newDirect);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataSourceJndi getJNDI() {
		return (DataSourceJndi)getMixed().get(DatasourcePackage.Literals.DATA_SOURCE_DOCUMENT_ROOT__JNDI, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetJNDI(DataSourceJndi newJNDI, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(DatasourcePackage.Literals.DATA_SOURCE_DOCUMENT_ROOT__JNDI, newJNDI, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJNDI(DataSourceJndi newJNDI) {
		((FeatureMap.Internal)getMixed()).set(DatasourcePackage.Literals.DATA_SOURCE_DOCUMENT_ROOT__JNDI, newJNDI);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__MIXED:
				return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__DIRECT:
				return basicSetDirect(null, msgs);
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__JNDI:
				return basicSetJNDI(null, msgs);
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
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__MIXED:
				if (coreType) return getMixed();
				return ((FeatureMap.Internal)getMixed()).getWrapper();
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				if (coreType) return getXMLNSPrefixMap();
				else return getXMLNSPrefixMap().map();
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				if (coreType) return getXSISchemaLocation();
				else return getXSISchemaLocation().map();
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__DIRECT:
				return getDirect();
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__JNDI:
				return getJNDI();
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
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__MIXED:
				((FeatureMap.Internal)getMixed()).set(newValue);
				return;
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
				return;
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
				return;
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__DIRECT:
				setDirect((Direct)newValue);
				return;
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__JNDI:
				setJNDI((DataSourceJndi)newValue);
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
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__DIRECT:
				setDirect((Direct)null);
				return;
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__JNDI:
				setJNDI((DataSourceJndi)null);
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
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__DIRECT:
				return getDirect() != null;
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT__JNDI:
				return getJNDI() != null;
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
		result.append(" (mixed: "); //$NON-NLS-1$
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} //DataSourceDocumentRootImpl
