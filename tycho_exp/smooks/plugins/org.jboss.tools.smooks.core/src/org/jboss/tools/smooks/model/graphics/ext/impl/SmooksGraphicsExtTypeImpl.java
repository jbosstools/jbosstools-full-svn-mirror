/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ProcessesType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.smooks.impl.AbstractResourceConfigImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Smooks Graphics Ext Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getInput <em>Input</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getGraph <em>Graph</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getProcesses <em>Processes</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getAuthor <em>Author</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getInputType <em>Input Type</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getOutputType <em>Output Type</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getPlatformVersion <em>Platform Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SmooksGraphicsExtTypeImpl extends AbstractResourceConfigImpl implements SmooksGraphicsExtType {
	
	private List<ISmooksGraphChangeListener> changeListeners = null;

	public List<ISmooksGraphChangeListener> getChangeListeners() {
		if(changeListeners == null){
			changeListeners = new ArrayList<ISmooksGraphChangeListener>();
		}
		return changeListeners;
	}

	public void addSmooksGraphChangeListener(ISmooksGraphChangeListener listener) {
		getChangeListeners().add(listener);
	}

	public void removeSmooksGraphChangeListener(ISmooksGraphChangeListener listener) {
		getChangeListeners().remove(listener);
	}
	
	/**
	 * The cached value of the '{@link #getInput() <em>Input</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInput()
	 * @generated
	 * @ordered
	 */
	protected EList<InputType> input;

	/**
	 * The cached value of the '{@link #getGraph() <em>Graph</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGraph()
	 * @generated
	 * @ordered
	 */
	protected GraphType graph;

	/**
	 * The cached value of the '{@link #getProcesses() <em>Processes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcesses()
	 * @generated
	 * @ordered
	 */
	protected ProcessesType processes;

	/**
	 * The default value of the '{@link #getAuthor() <em>Author</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthor()
	 * @generated
	 * @ordered
	 */
	protected static final String AUTHOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAuthor() <em>Author</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthor()
	 * @generated
	 * @ordered
	 */
	protected String author = AUTHOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getInputType() <em>Input Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputType()
	 * @generated
	 * @ordered
	 */
	protected static final String INPUT_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInputType() <em>Input Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputType()
	 * @generated
	 * @ordered
	 */
	protected String inputType = INPUT_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getOutputType() <em>Output Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputType()
	 * @generated
	 * @ordered
	 */
	protected static final String OUTPUT_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOutputType() <em>Output Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputType()
	 * @generated
	 * @ordered
	 */
	protected String outputType = OUTPUT_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPlatformVersion() <em>Platform Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlatformVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String PLATFORM_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPlatformVersion() <em>Platform Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlatformVersion()
	 * @generated
	 * @ordered
	 */
	protected String platformVersion = PLATFORM_VERSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SmooksGraphicsExtTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InputType> getInput() {
		if (input == null) {
			input = new EObjectContainmentEList<InputType>(InputType.class, this, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT);
		}
		return input;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GraphType getGraph() {
		return graph;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGraph(GraphType newGraph, NotificationChain msgs) {
		GraphType oldGraph = graph;
		graph = newGraph;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__GRAPH, oldGraph, newGraph);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGraph(GraphType newGraph) {
		if (newGraph != graph) {
			NotificationChain msgs = null;
			if (graph != null)
				msgs = ((InternalEObject)graph).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__GRAPH, null, msgs);
			if (newGraph != null)
				msgs = ((InternalEObject)newGraph).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__GRAPH, null, msgs);
			msgs = basicSetGraph(newGraph, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__GRAPH, newGraph, newGraph));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessesType getProcesses() {
		return processes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetProcesses(ProcessesType newProcesses, NotificationChain msgs) {
		ProcessesType oldProcesses = processes;
		processes = newProcesses;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES, oldProcesses, newProcesses);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProcesses(ProcessesType newProcesses) {
		if (newProcesses != processes) {
			NotificationChain msgs = null;
			if (processes != null)
				msgs = ((InternalEObject)processes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES, null, msgs);
			if (newProcesses != null)
				msgs = ((InternalEObject)newProcesses).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES, null, msgs);
			msgs = basicSetProcesses(newProcesses, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES, newProcesses, newProcesses));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAuthor(String newAuthor) {
		String oldAuthor = author;
		author = newAuthor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__AUTHOR, oldAuthor, author));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInputType() {
		return inputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputType(String newInputType) {
		String oldInputType = inputType;
		inputType = newInputType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE, oldInputType, inputType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOutputType() {
		return outputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutputType(String newOutputType) {
		String oldOutputType = outputType;
		outputType = newOutputType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE, oldOutputType, outputType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPlatformVersion() {
		return platformVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPlatformVersion(String newPlatformVersion) {
		String oldPlatformVersion = platformVersion;
		platformVersion = newPlatformVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PLATFORM_VERSION, oldPlatformVersion, platformVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				return ((InternalEList<?>)getInput()).basicRemove(otherEnd, msgs);
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__GRAPH:
				return basicSetGraph(null, msgs);
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES:
				return basicSetProcesses(null, msgs);
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
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				return getInput();
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__GRAPH:
				return getGraph();
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES:
				return getProcesses();
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__AUTHOR:
				return getAuthor();
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE:
				return getInputType();
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__NAME:
				return getName();
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE:
				return getOutputType();
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PLATFORM_VERSION:
				return getPlatformVersion();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				getInput().clear();
				getInput().addAll((Collection<? extends InputType>)newValue);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__GRAPH:
				setGraph((GraphType)newValue);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES:
				setProcesses((ProcessesType)newValue);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__AUTHOR:
				setAuthor((String)newValue);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE:
				setInputType((String)newValue);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__NAME:
				setName((String)newValue);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE:
				setOutputType((String)newValue);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PLATFORM_VERSION:
				setPlatformVersion((String)newValue);
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
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				getInput().clear();
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__GRAPH:
				setGraph((GraphType)null);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES:
				setProcesses((ProcessesType)null);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__AUTHOR:
				setAuthor(AUTHOR_EDEFAULT);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE:
				setInputType(INPUT_TYPE_EDEFAULT);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE:
				setOutputType(OUTPUT_TYPE_EDEFAULT);
				return;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PLATFORM_VERSION:
				setPlatformVersion(PLATFORM_VERSION_EDEFAULT);
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
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				return input != null && !input.isEmpty();
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__GRAPH:
				return graph != null;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES:
				return processes != null;
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__AUTHOR:
				return AUTHOR_EDEFAULT == null ? author != null : !AUTHOR_EDEFAULT.equals(author);
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE:
				return INPUT_TYPE_EDEFAULT == null ? inputType != null : !INPUT_TYPE_EDEFAULT.equals(inputType);
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE:
				return OUTPUT_TYPE_EDEFAULT == null ? outputType != null : !OUTPUT_TYPE_EDEFAULT.equals(outputType);
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE__PLATFORM_VERSION:
				return PLATFORM_VERSION_EDEFAULT == null ? platformVersion != null : !PLATFORM_VERSION_EDEFAULT.equals(platformVersion);
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
		result.append(" (author: ");
		result.append(author);
		result.append(", inputType: ");
		result.append(inputType);
		result.append(", name: ");
		result.append(name);
		result.append(", outputType: ");
		result.append(outputType);
		result.append(", platformVersion: ");
		result.append(platformVersion);
		result.append(')');
		return result.toString();
	}

} //SmooksGraphicsExtTypeImpl
