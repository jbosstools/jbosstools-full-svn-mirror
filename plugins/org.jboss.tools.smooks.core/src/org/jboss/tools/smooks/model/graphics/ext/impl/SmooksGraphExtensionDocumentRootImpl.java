/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext.impl;


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
import org.jboss.tools.smooks.model.graphics.ext.ConnectionType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getFigure <em>Figure</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getGraph <em>Graph</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getInput <em>Input</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getParam <em>Param</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getSmooksGraphicsExt <em>Smooks Graphics Ext</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SmooksGraphExtensionDocumentRootImpl extends EObjectImpl implements SmooksGraphExtensionDocumentRoot {
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
	 * The default value of the '{@link #getSource() <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected static final String SOURCE_EDEFAULT = null;

	/**
	 * The default value of the '{@link #getTarget() <em>Target</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected static final String TARGET_EDEFAULT = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SmooksGraphExtensionDocumentRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GraphPackage.Literals.DOCUMENT_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FeatureMap getMixed() {
		if (mixed == null) {
			mixed = new BasicFeatureMap(this, GraphPackage.DOCUMENT_ROOT__MIXED);
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
			xMLNSPrefixMap = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, GraphPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
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
			xSISchemaLocation = new EcoreEMap<String,String>(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, GraphPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		}
		return xSISchemaLocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConnectionType getConnection() {
		return (ConnectionType)getMixed().get(GraphPackage.Literals.DOCUMENT_ROOT__CONNECTION, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConnection(ConnectionType newConnection, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GraphPackage.Literals.DOCUMENT_ROOT__CONNECTION, newConnection, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConnection(ConnectionType newConnection) {
		((FeatureMap.Internal)getMixed()).set(GraphPackage.Literals.DOCUMENT_ROOT__CONNECTION, newConnection);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FigureType getFigure() {
		return (FigureType)getMixed().get(GraphPackage.Literals.DOCUMENT_ROOT__FIGURE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFigure(FigureType newFigure, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GraphPackage.Literals.DOCUMENT_ROOT__FIGURE, newFigure, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFigure(FigureType newFigure) {
		((FeatureMap.Internal)getMixed()).set(GraphPackage.Literals.DOCUMENT_ROOT__FIGURE, newFigure);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GraphType getGraph() {
		return (GraphType)getMixed().get(GraphPackage.Literals.DOCUMENT_ROOT__GRAPH, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGraph(GraphType newGraph, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GraphPackage.Literals.DOCUMENT_ROOT__GRAPH, newGraph, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGraph(GraphType newGraph) {
		((FeatureMap.Internal)getMixed()).set(GraphPackage.Literals.DOCUMENT_ROOT__GRAPH, newGraph);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputType getInput() {
		return (InputType)getMixed().get(GraphPackage.Literals.DOCUMENT_ROOT__INPUT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInput(InputType newInput, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GraphPackage.Literals.DOCUMENT_ROOT__INPUT, newInput, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInput(InputType newInput) {
		((FeatureMap.Internal)getMixed()).set(GraphPackage.Literals.DOCUMENT_ROOT__INPUT, newInput);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParamType getParam() {
		return (ParamType)getMixed().get(GraphPackage.Literals.DOCUMENT_ROOT__PARAM, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParam(ParamType newParam, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GraphPackage.Literals.DOCUMENT_ROOT__PARAM, newParam, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParam(ParamType newParam) {
		((FeatureMap.Internal)getMixed()).set(GraphPackage.Literals.DOCUMENT_ROOT__PARAM, newParam);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksGraphicsExtType getSmooksGraphicsExt() {
		return (SmooksGraphicsExtType)getMixed().get(GraphPackage.Literals.DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSmooksGraphicsExt(SmooksGraphicsExtType newSmooksGraphicsExt, NotificationChain msgs) {
		return ((FeatureMap.Internal)getMixed()).basicAdd(GraphPackage.Literals.DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT, newSmooksGraphicsExt, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSmooksGraphicsExt(SmooksGraphicsExtType newSmooksGraphicsExt) {
		((FeatureMap.Internal)getMixed()).set(GraphPackage.Literals.DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT, newSmooksGraphicsExt);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSource() {
		return (String)getMixed().get(GraphPackage.Literals.DOCUMENT_ROOT__SOURCE, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSource(String newSource) {
		((FeatureMap.Internal)getMixed()).set(GraphPackage.Literals.DOCUMENT_ROOT__SOURCE, newSource);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTarget() {
		return (String)getMixed().get(GraphPackage.Literals.DOCUMENT_ROOT__TARGET, true);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTarget(String newTarget) {
		((FeatureMap.Internal)getMixed()).set(GraphPackage.Literals.DOCUMENT_ROOT__TARGET, newTarget);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GraphPackage.DOCUMENT_ROOT__MIXED:
				return ((InternalEList<?>)getMixed()).basicRemove(otherEnd, msgs);
			case GraphPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return ((InternalEList<?>)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
			case GraphPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return ((InternalEList<?>)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
			case GraphPackage.DOCUMENT_ROOT__CONNECTION:
				return basicSetConnection(null, msgs);
			case GraphPackage.DOCUMENT_ROOT__FIGURE:
				return basicSetFigure(null, msgs);
			case GraphPackage.DOCUMENT_ROOT__GRAPH:
				return basicSetGraph(null, msgs);
			case GraphPackage.DOCUMENT_ROOT__INPUT:
				return basicSetInput(null, msgs);
			case GraphPackage.DOCUMENT_ROOT__PARAM:
				return basicSetParam(null, msgs);
			case GraphPackage.DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT:
				return basicSetSmooksGraphicsExt(null, msgs);
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
			case GraphPackage.DOCUMENT_ROOT__MIXED:
				if (coreType) return getMixed();
				return ((FeatureMap.Internal)getMixed()).getWrapper();
			case GraphPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				if (coreType) return getXMLNSPrefixMap();
				else return getXMLNSPrefixMap().map();
			case GraphPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				if (coreType) return getXSISchemaLocation();
				else return getXSISchemaLocation().map();
			case GraphPackage.DOCUMENT_ROOT__CONNECTION:
				return getConnection();
			case GraphPackage.DOCUMENT_ROOT__FIGURE:
				return getFigure();
			case GraphPackage.DOCUMENT_ROOT__GRAPH:
				return getGraph();
			case GraphPackage.DOCUMENT_ROOT__INPUT:
				return getInput();
			case GraphPackage.DOCUMENT_ROOT__PARAM:
				return getParam();
			case GraphPackage.DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT:
				return getSmooksGraphicsExt();
			case GraphPackage.DOCUMENT_ROOT__SOURCE:
				return getSource();
			case GraphPackage.DOCUMENT_ROOT__TARGET:
				return getTarget();
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
			case GraphPackage.DOCUMENT_ROOT__MIXED:
				((FeatureMap.Internal)getMixed()).set(newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__CONNECTION:
				setConnection((ConnectionType)newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__FIGURE:
				setFigure((FigureType)newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__GRAPH:
				setGraph((GraphType)newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__INPUT:
				setInput((InputType)newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__PARAM:
				setParam((ParamType)newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT:
				setSmooksGraphicsExt((SmooksGraphicsExtType)newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__SOURCE:
				setSource((String)newValue);
				return;
			case GraphPackage.DOCUMENT_ROOT__TARGET:
				setTarget((String)newValue);
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
			case GraphPackage.DOCUMENT_ROOT__MIXED:
				getMixed().clear();
				return;
			case GraphPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				getXMLNSPrefixMap().clear();
				return;
			case GraphPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				getXSISchemaLocation().clear();
				return;
			case GraphPackage.DOCUMENT_ROOT__CONNECTION:
				setConnection((ConnectionType)null);
				return;
			case GraphPackage.DOCUMENT_ROOT__FIGURE:
				setFigure((FigureType)null);
				return;
			case GraphPackage.DOCUMENT_ROOT__GRAPH:
				setGraph((GraphType)null);
				return;
			case GraphPackage.DOCUMENT_ROOT__INPUT:
				setInput((InputType)null);
				return;
			case GraphPackage.DOCUMENT_ROOT__PARAM:
				setParam((ParamType)null);
				return;
			case GraphPackage.DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT:
				setSmooksGraphicsExt((SmooksGraphicsExtType)null);
				return;
			case GraphPackage.DOCUMENT_ROOT__SOURCE:
				setSource(SOURCE_EDEFAULT);
				return;
			case GraphPackage.DOCUMENT_ROOT__TARGET:
				setTarget(TARGET_EDEFAULT);
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
			case GraphPackage.DOCUMENT_ROOT__MIXED:
				return mixed != null && !mixed.isEmpty();
			case GraphPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
				return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
			case GraphPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
				return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
			case GraphPackage.DOCUMENT_ROOT__CONNECTION:
				return getConnection() != null;
			case GraphPackage.DOCUMENT_ROOT__FIGURE:
				return getFigure() != null;
			case GraphPackage.DOCUMENT_ROOT__GRAPH:
				return getGraph() != null;
			case GraphPackage.DOCUMENT_ROOT__INPUT:
				return getInput() != null;
			case GraphPackage.DOCUMENT_ROOT__PARAM:
				return getParam() != null;
			case GraphPackage.DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT:
				return getSmooksGraphicsExt() != null;
			case GraphPackage.DOCUMENT_ROOT__SOURCE:
				return SOURCE_EDEFAULT == null ? getSource() != null : !SOURCE_EDEFAULT.equals(getSource());
			case GraphPackage.DOCUMENT_ROOT__TARGET:
				return TARGET_EDEFAULT == null ? getTarget() != null : !TARGET_EDEFAULT.equals(getTarget());
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

} //DocumentRootImpl
