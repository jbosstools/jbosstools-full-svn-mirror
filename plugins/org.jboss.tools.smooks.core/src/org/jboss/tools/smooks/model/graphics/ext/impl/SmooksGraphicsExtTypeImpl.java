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
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.jboss.tools.smooks.model.graphics.ext.ISmooksGraphChangeListener;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtPackage;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getInput <em>Input</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getInputType <em>Input Type</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl#getOutputType <em>Output Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SmooksGraphicsExtTypeImpl extends EObjectImpl implements SmooksGraphicsExtType {
	
	private List<ISmooksGraphChangeListener> changeListeners;
	
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
		return SmooksGraphicsExtPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InputType> getInput() {
		if (input == null) {
			input = new EObjectContainmentEList<InputType>(InputType.class, this, SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT);
		}
		return input;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE, oldInputType, inputType));
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
			eNotify(new ENotificationImpl(this, Notification.SET, SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE, oldOutputType, outputType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				return ((InternalEList<?>)getInput()).basicRemove(otherEnd, msgs);
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
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				return getInput();
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE:
				return getInputType();
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE:
				return getOutputType();
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
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				getInput().clear();
				getInput().addAll((Collection<? extends InputType>)newValue);
				return;
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE:
				setInputType((String)newValue);
				return;
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE:
				setOutputType((String)newValue);
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
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				getInput().clear();
				return;
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE:
				setInputType(INPUT_TYPE_EDEFAULT);
				return;
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE:
				setOutputType(OUTPUT_TYPE_EDEFAULT);
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
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT:
				return input != null && !input.isEmpty();
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE:
				return INPUT_TYPE_EDEFAULT == null ? inputType != null : !INPUT_TYPE_EDEFAULT.equals(inputType);
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE:
				return OUTPUT_TYPE_EDEFAULT == null ? outputType != null : !OUTPUT_TYPE_EDEFAULT.equals(outputType);
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
		result.append(" (inputType: ");
		result.append(inputType);
		result.append(", outputType: ");
		result.append(outputType);
		result.append(')');
		return result.toString();
	}

	/**
	 * @return the changeListeners
	 */
	public List<ISmooksGraphChangeListener> getChangeListeners() {
		if(changeListeners == null){
			changeListeners = new ArrayList<ISmooksGraphChangeListener>();
		}
		return changeListeners;
	}

	/**
	 * @param changeListeners the changeListeners to set
	 */
	public void setChangeListeners(List<ISmooksGraphChangeListener> changeListeners) {
		this.changeListeners = changeListeners;
	}

	public void addSmooksGraphChangeListener(ISmooksGraphChangeListener listener) {
		this.getChangeListeners().add(listener);
		
	}

	public void removeSmooksGraphChangeListener(ISmooksGraphChangeListener listener) {
		this.getChangeListeners().remove(listener);
	}
	
	

} //SmooksGraphicsExtTypeImpl
