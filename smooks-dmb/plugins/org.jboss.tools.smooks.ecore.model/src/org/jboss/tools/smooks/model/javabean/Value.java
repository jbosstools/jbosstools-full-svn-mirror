/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Value</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Value#getProperty <em>Property</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Value#getSetterMethod <em>Setter Method</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Value#getData <em>Data</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Value#getDataNS <em>Data NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Value#getDecoder <em>Decoder</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Value#getDefaultVal <em>Default Val</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Value#getDecodeParams <em>Decode Params</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Value extends EObjectImpl implements IValue {
	/**
	 * The default value of the '{@link #getProperty() <em>Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperty()
	 * @generated
	 * @ordered
	 */
	protected static final String PROPERTY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProperty() <em>Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperty()
	 * @generated
	 * @ordered
	 */
	protected String property = PROPERTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getSetterMethod() <em>Setter Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSetterMethod()
	 * @generated
	 * @ordered
	 */
	protected static final String SETTER_METHOD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSetterMethod() <em>Setter Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSetterMethod()
	 * @generated
	 * @ordered
	 */
	protected String setterMethod = SETTER_METHOD_EDEFAULT;

	/**
	 * The default value of the '{@link #getData() <em>Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getData()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getData() <em>Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getData()
	 * @generated
	 * @ordered
	 */
	protected String data = DATA_EDEFAULT;

	/**
	 * The default value of the '{@link #getDataNS() <em>Data NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataNS()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataNS() <em>Data NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataNS()
	 * @generated
	 * @ordered
	 */
	protected String dataNS = DATA_NS_EDEFAULT;

	/**
	 * The default value of the '{@link #getDecoder() <em>Decoder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDecoder()
	 * @generated
	 * @ordered
	 */
	protected static final String DECODER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDecoder() <em>Decoder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDecoder()
	 * @generated
	 * @ordered
	 */
	protected String decoder = DECODER_EDEFAULT;

	/**
	 * The default value of the '{@link #getDefaultVal() <em>Default Val</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultVal()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_VAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultVal() <em>Default Val</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultVal()
	 * @generated
	 * @ordered
	 */
	protected String defaultVal = DEFAULT_VAL_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDecodeParams() <em>Decode Params</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDecodeParams()
	 * @generated
	 * @ordered
	 */
	protected EList<IDecodeParam> decodeParams;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Value() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IJavaBeanPackage.Literals.VALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProperty(String newProperty) {
		String oldProperty = property;
		property = newProperty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.VALUE__PROPERTY, oldProperty, property));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSetterMethod() {
		return setterMethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSetterMethod(String newSetterMethod) {
		String oldSetterMethod = setterMethod;
		setterMethod = newSetterMethod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.VALUE__SETTER_METHOD, oldSetterMethod, setterMethod));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getData() {
		return data;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setData(String newData) {
		String oldData = data;
		data = newData;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.VALUE__DATA, oldData, data));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDataNS() {
		return dataNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDataNS(String newDataNS) {
		String oldDataNS = dataNS;
		dataNS = newDataNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.VALUE__DATA_NS, oldDataNS, dataNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDecoder() {
		return decoder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDecoder(String newDecoder) {
		String oldDecoder = decoder;
		decoder = newDecoder;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.VALUE__DECODER, oldDecoder, decoder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultVal() {
		return defaultVal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultVal(String newDefaultVal) {
		String oldDefaultVal = defaultVal;
		defaultVal = newDefaultVal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.VALUE__DEFAULT_VAL, oldDefaultVal, defaultVal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<IDecodeParam> getDecodeParams() {
		if (decodeParams == null) {
			decodeParams = new EObjectResolvingEList<IDecodeParam>(IDecodeParam.class, this, IJavaBeanPackage.VALUE__DECODE_PARAMS);
		}
		return decodeParams;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case IJavaBeanPackage.VALUE__PROPERTY:
				return getProperty();
			case IJavaBeanPackage.VALUE__SETTER_METHOD:
				return getSetterMethod();
			case IJavaBeanPackage.VALUE__DATA:
				return getData();
			case IJavaBeanPackage.VALUE__DATA_NS:
				return getDataNS();
			case IJavaBeanPackage.VALUE__DECODER:
				return getDecoder();
			case IJavaBeanPackage.VALUE__DEFAULT_VAL:
				return getDefaultVal();
			case IJavaBeanPackage.VALUE__DECODE_PARAMS:
				return getDecodeParams();
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
			case IJavaBeanPackage.VALUE__PROPERTY:
				setProperty((String)newValue);
				return;
			case IJavaBeanPackage.VALUE__SETTER_METHOD:
				setSetterMethod((String)newValue);
				return;
			case IJavaBeanPackage.VALUE__DATA:
				setData((String)newValue);
				return;
			case IJavaBeanPackage.VALUE__DATA_NS:
				setDataNS((String)newValue);
				return;
			case IJavaBeanPackage.VALUE__DECODER:
				setDecoder((String)newValue);
				return;
			case IJavaBeanPackage.VALUE__DEFAULT_VAL:
				setDefaultVal((String)newValue);
				return;
			case IJavaBeanPackage.VALUE__DECODE_PARAMS:
				getDecodeParams().clear();
				getDecodeParams().addAll((Collection<? extends IDecodeParam>)newValue);
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
			case IJavaBeanPackage.VALUE__PROPERTY:
				setProperty(PROPERTY_EDEFAULT);
				return;
			case IJavaBeanPackage.VALUE__SETTER_METHOD:
				setSetterMethod(SETTER_METHOD_EDEFAULT);
				return;
			case IJavaBeanPackage.VALUE__DATA:
				setData(DATA_EDEFAULT);
				return;
			case IJavaBeanPackage.VALUE__DATA_NS:
				setDataNS(DATA_NS_EDEFAULT);
				return;
			case IJavaBeanPackage.VALUE__DECODER:
				setDecoder(DECODER_EDEFAULT);
				return;
			case IJavaBeanPackage.VALUE__DEFAULT_VAL:
				setDefaultVal(DEFAULT_VAL_EDEFAULT);
				return;
			case IJavaBeanPackage.VALUE__DECODE_PARAMS:
				getDecodeParams().clear();
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
			case IJavaBeanPackage.VALUE__PROPERTY:
				return PROPERTY_EDEFAULT == null ? property != null : !PROPERTY_EDEFAULT.equals(property);
			case IJavaBeanPackage.VALUE__SETTER_METHOD:
				return SETTER_METHOD_EDEFAULT == null ? setterMethod != null : !SETTER_METHOD_EDEFAULT.equals(setterMethod);
			case IJavaBeanPackage.VALUE__DATA:
				return DATA_EDEFAULT == null ? data != null : !DATA_EDEFAULT.equals(data);
			case IJavaBeanPackage.VALUE__DATA_NS:
				return DATA_NS_EDEFAULT == null ? dataNS != null : !DATA_NS_EDEFAULT.equals(dataNS);
			case IJavaBeanPackage.VALUE__DECODER:
				return DECODER_EDEFAULT == null ? decoder != null : !DECODER_EDEFAULT.equals(decoder);
			case IJavaBeanPackage.VALUE__DEFAULT_VAL:
				return DEFAULT_VAL_EDEFAULT == null ? defaultVal != null : !DEFAULT_VAL_EDEFAULT.equals(defaultVal);
			case IJavaBeanPackage.VALUE__DECODE_PARAMS:
				return decodeParams != null && !decodeParams.isEmpty();
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
		result.append(" (property: ");
		result.append(property);
		result.append(", setterMethod: ");
		result.append(setterMethod);
		result.append(", data: ");
		result.append(data);
		result.append(", dataNS: ");
		result.append(dataNS);
		result.append(", decoder: ");
		result.append(decoder);
		result.append(", defaultVal: ");
		result.append(defaultVal);
		result.append(')');
		return result.toString();
	}

} //Value
