/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bean</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Bean#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Bean#getBeanClass <em>Bean Class</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Bean#getCreateOnElement <em>Create On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Bean#getCreateOnElementNS <em>Create On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Bean#getValueBindings <em>Value Bindings</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Bean#getWireBindings <em>Wire Bindings</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Bean#getExpressionBindings <em>Expression Bindings</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Bean extends EObjectImpl implements IBean {
	/**
	 * The default value of the '{@link #getBeanId() <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanId()
	 * @generated
	 * @ordered
	 */
	protected static final String BEAN_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeanId() <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanId()
	 * @generated
	 * @ordered
	 */
	protected String beanId = BEAN_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getBeanClass() <em>Bean Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanClass()
	 * @generated
	 * @ordered
	 */
	protected static final String BEAN_CLASS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeanClass() <em>Bean Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanClass()
	 * @generated
	 * @ordered
	 */
	protected String beanClass = BEAN_CLASS_EDEFAULT;

	/**
	 * The default value of the '{@link #getCreateOnElement() <em>Create On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreateOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String CREATE_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCreateOnElement() <em>Create On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreateOnElement()
	 * @generated
	 * @ordered
	 */
	protected String createOnElement = CREATE_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getCreateOnElementNS() <em>Create On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreateOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String CREATE_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCreateOnElementNS() <em>Create On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreateOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String createOnElementNS = CREATE_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getValueBindings() <em>Value Bindings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValueBindings()
	 * @generated
	 * @ordered
	 */
	protected EList<IValue> valueBindings;

	/**
	 * The cached value of the '{@link #getWireBindings() <em>Wire Bindings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWireBindings()
	 * @generated
	 * @ordered
	 */
	protected EList<IWiring> wireBindings;

	/**
	 * The cached value of the '{@link #getExpressionBindings() <em>Expression Bindings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExpressionBindings()
	 * @generated
	 * @ordered
	 */
	protected EList<IExpression> expressionBindings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Bean() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IJavaBeanPackage.Literals.BEAN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeanId() {
		return beanId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanId(String newBeanId) {
		String oldBeanId = beanId;
		beanId = newBeanId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.BEAN__BEAN_ID, oldBeanId, beanId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeanClass() {
		return beanClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanClass(String newBeanClass) {
		String oldBeanClass = beanClass;
		beanClass = newBeanClass;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.BEAN__BEAN_CLASS, oldBeanClass, beanClass));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCreateOnElement() {
		return createOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCreateOnElement(String newCreateOnElement) {
		String oldCreateOnElement = createOnElement;
		createOnElement = newCreateOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT, oldCreateOnElement, createOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCreateOnElementNS() {
		return createOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCreateOnElementNS(String newCreateOnElementNS) {
		String oldCreateOnElementNS = createOnElementNS;
		createOnElementNS = newCreateOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT_NS, oldCreateOnElementNS, createOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IValue> getValueBindings() {
		if (valueBindings == null) {
			valueBindings = new EObjectContainmentEList<IValue>(IValue.class, this, IJavaBeanPackage.BEAN__VALUE_BINDINGS);
		}
		return valueBindings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IWiring> getWireBindings() {
		if (wireBindings == null) {
			wireBindings = new EObjectContainmentEList<IWiring>(IWiring.class, this, IJavaBeanPackage.BEAN__WIRE_BINDINGS);
		}
		return wireBindings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IExpression> getExpressionBindings() {
		if (expressionBindings == null) {
			expressionBindings = new EObjectContainmentEList<IExpression>(IExpression.class, this, IJavaBeanPackage.BEAN__EXPRESSION_BINDINGS);
		}
		return expressionBindings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case IJavaBeanPackage.BEAN__VALUE_BINDINGS:
				return ((InternalEList<?>)getValueBindings()).basicRemove(otherEnd, msgs);
			case IJavaBeanPackage.BEAN__WIRE_BINDINGS:
				return ((InternalEList<?>)getWireBindings()).basicRemove(otherEnd, msgs);
			case IJavaBeanPackage.BEAN__EXPRESSION_BINDINGS:
				return ((InternalEList<?>)getExpressionBindings()).basicRemove(otherEnd, msgs);
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
			case IJavaBeanPackage.BEAN__BEAN_ID:
				return getBeanId();
			case IJavaBeanPackage.BEAN__BEAN_CLASS:
				return getBeanClass();
			case IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT:
				return getCreateOnElement();
			case IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT_NS:
				return getCreateOnElementNS();
			case IJavaBeanPackage.BEAN__VALUE_BINDINGS:
				return getValueBindings();
			case IJavaBeanPackage.BEAN__WIRE_BINDINGS:
				return getWireBindings();
			case IJavaBeanPackage.BEAN__EXPRESSION_BINDINGS:
				return getExpressionBindings();
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
			case IJavaBeanPackage.BEAN__BEAN_ID:
				setBeanId((String)newValue);
				return;
			case IJavaBeanPackage.BEAN__BEAN_CLASS:
				setBeanClass((String)newValue);
				return;
			case IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT:
				setCreateOnElement((String)newValue);
				return;
			case IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT_NS:
				setCreateOnElementNS((String)newValue);
				return;
			case IJavaBeanPackage.BEAN__VALUE_BINDINGS:
				getValueBindings().clear();
				getValueBindings().addAll((Collection<? extends IValue>)newValue);
				return;
			case IJavaBeanPackage.BEAN__WIRE_BINDINGS:
				getWireBindings().clear();
				getWireBindings().addAll((Collection<? extends IWiring>)newValue);
				return;
			case IJavaBeanPackage.BEAN__EXPRESSION_BINDINGS:
				getExpressionBindings().clear();
				getExpressionBindings().addAll((Collection<? extends IExpression>)newValue);
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
			case IJavaBeanPackage.BEAN__BEAN_ID:
				setBeanId(BEAN_ID_EDEFAULT);
				return;
			case IJavaBeanPackage.BEAN__BEAN_CLASS:
				setBeanClass(BEAN_CLASS_EDEFAULT);
				return;
			case IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT:
				setCreateOnElement(CREATE_ON_ELEMENT_EDEFAULT);
				return;
			case IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT_NS:
				setCreateOnElementNS(CREATE_ON_ELEMENT_NS_EDEFAULT);
				return;
			case IJavaBeanPackage.BEAN__VALUE_BINDINGS:
				getValueBindings().clear();
				return;
			case IJavaBeanPackage.BEAN__WIRE_BINDINGS:
				getWireBindings().clear();
				return;
			case IJavaBeanPackage.BEAN__EXPRESSION_BINDINGS:
				getExpressionBindings().clear();
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
			case IJavaBeanPackage.BEAN__BEAN_ID:
				return BEAN_ID_EDEFAULT == null ? beanId != null : !BEAN_ID_EDEFAULT.equals(beanId);
			case IJavaBeanPackage.BEAN__BEAN_CLASS:
				return BEAN_CLASS_EDEFAULT == null ? beanClass != null : !BEAN_CLASS_EDEFAULT.equals(beanClass);
			case IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT:
				return CREATE_ON_ELEMENT_EDEFAULT == null ? createOnElement != null : !CREATE_ON_ELEMENT_EDEFAULT.equals(createOnElement);
			case IJavaBeanPackage.BEAN__CREATE_ON_ELEMENT_NS:
				return CREATE_ON_ELEMENT_NS_EDEFAULT == null ? createOnElementNS != null : !CREATE_ON_ELEMENT_NS_EDEFAULT.equals(createOnElementNS);
			case IJavaBeanPackage.BEAN__VALUE_BINDINGS:
				return valueBindings != null && !valueBindings.isEmpty();
			case IJavaBeanPackage.BEAN__WIRE_BINDINGS:
				return wireBindings != null && !wireBindings.isEmpty();
			case IJavaBeanPackage.BEAN__EXPRESSION_BINDINGS:
				return expressionBindings != null && !expressionBindings.isEmpty();
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
		result.append(" (beanId: ");
		result.append(beanId);
		result.append(", beanClass: ");
		result.append(beanClass);
		result.append(", createOnElement: ");
		result.append(createOnElement);
		result.append(", createOnElementNS: ");
		result.append(createOnElementNS);
		result.append(')');
		return result.toString();
	}

} //Bean
