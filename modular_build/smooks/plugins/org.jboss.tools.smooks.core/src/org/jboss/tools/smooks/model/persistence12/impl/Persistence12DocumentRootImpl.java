/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.impl;

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
import org.jboss.tools.smooks.model.persistence12.Deleter;
import org.jboss.tools.smooks.model.persistence12.Flusher;
import org.jboss.tools.smooks.model.persistence12.Inserter;
import org.jboss.tools.smooks.model.persistence12.Locator;
import org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.persistence12.Updater;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl#getDeleter <em>Deleter</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl#getFlusher <em>Flusher</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl#getInserter <em>Inserter</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl#getLocator <em>Locator</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.Persistence12DocumentRootImpl#getUpdater <em>Updater</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Persistence12DocumentRootImpl extends EObjectImpl implements Persistence12DocumentRoot {
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
	protected EMap xMLNSPrefixMap;

	/**
	 * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getXSISchemaLocation()
	 * @generated
	 * @ordered
	 */
	protected EMap xSISchemaLocation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Persistence12DocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__MIXED);
		}
		return mixed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap getXMLNSPrefixMap() {
		if (xMLNSPrefixMap == null) {
			xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		}
		return xMLNSPrefixMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap getXSISchemaLocation() {
		if (xSISchemaLocation == null) {
			xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Deleter getDeleter() {
		return (Deleter)getMixed().get(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__DELETER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDeleter(Deleter newDeleter, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__DELETER, newDeleter, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeleter(Deleter newDeleter) {
		((FeatureMap.Internal)getMixed()).set(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__DELETER, newDeleter);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Flusher getFlusher() {
		return (Flusher)getMixed().get(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__FLUSHER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFlusher(Flusher newFlusher, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__FLUSHER, newFlusher, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFlusher(Flusher newFlusher) {
		((FeatureMap.Internal)getMixed()).set(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__FLUSHER, newFlusher);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Inserter getInserter() {
		return (Inserter)getMixed().get(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__INSERTER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInserter(Inserter newInserter, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__INSERTER, newInserter, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInserter(Inserter newInserter) {
		((FeatureMap.Internal)getMixed()).set(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__INSERTER, newInserter);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Locator getLocator() {
		return (Locator)getMixed().get(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__LOCATOR, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLocator(Locator newLocator, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__LOCATOR, newLocator, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocator(Locator newLocator) {
		((FeatureMap.Internal)getMixed()).set(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__LOCATOR, newLocator);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Updater getUpdater() {
		return (Updater)getMixed().get(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__UPDATER, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUpdater(Updater newUpdater, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__UPDATER, newUpdater, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUpdater(Updater newUpdater) {
		((FeatureMap.Internal)getMixed()).set(Persistence12Package.Literals.PERSISTENCE12_DOCUMENT_ROOT__UPDATER, newUpdater);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__MIXED:
				return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__DELETER:
				return basicSetDeleter(null, msgs);
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__FLUSHER:
				return basicSetFlusher(null, msgs);
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__INSERTER:
				return basicSetInserter(null, msgs);
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__LOCATOR:
				return basicSetLocator(null, msgs);
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__UPDATER:
				return basicSetUpdater(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__MIXED:
				if (coreType) return getMixed();
				return ((FeatureMap.Internal)getMixed()).getWrapper();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				if (coreType) return getXMLNSPrefixMap();
				else return getXMLNSPrefixMap().map();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				if (coreType) return getXSISchemaLocation();
				else return getXSISchemaLocation().map();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__DELETER:
				return getDeleter();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__FLUSHER:
				return getFlusher();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__INSERTER:
				return getInserter();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__LOCATOR:
				return getLocator();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__UPDATER:
				return getUpdater();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__MIXED:
				((FeatureMap.Internal)getMixed()).set(newValue);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__DELETER:
				setDeleter((Deleter)newValue);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__FLUSHER:
				setFlusher((Flusher)newValue);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__INSERTER:
				setInserter((Inserter)newValue);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__LOCATOR:
				setLocator((Locator)newValue);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__UPDATER:
				setUpdater((Updater)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void eUnset(int featureID) {
		switch (featureID) {
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__DELETER:
				setDeleter((Deleter)null);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__FLUSHER:
				setFlusher((Flusher)null);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__INSERTER:
				setInserter((Inserter)null);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__LOCATOR:
				setLocator((Locator)null);
				return;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__UPDATER:
				setUpdater((Updater)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__DELETER:
				return getDeleter() != null;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__FLUSHER:
				return getFlusher() != null;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__INSERTER:
				return getInserter() != null;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__LOCATOR:
				return getLocator() != null;
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT__UPDATER:
				return getUpdater() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (mixed: "); //$NON-NLS-1$
		result.append(mixed);
		result.append(')');
		return result.toString();
	}

} //Persistence12DocumentRootImpl
