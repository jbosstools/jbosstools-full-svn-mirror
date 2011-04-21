/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.impl;


import java.math.BigInteger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.jboss.tools.smooks.model.common.impl.AbstractAnyTypeImpl;
import org.jboss.tools.smooks.model.persistence12.DecoderParameter;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.persistence12.ValueParameter;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Value Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl#getDecodeParam <em>Decode Param</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl#getData <em>Data</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl#getDataNS <em>Data NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl#getDecoder <em>Decoder</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl#getDefault <em>Default</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.impl.ValueParameterImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ValueParameterImpl extends AbstractAnyTypeImpl implements ValueParameter {
	/**
	 * The cached value of the '{@link #getDecodeParam() <em>Decode Param</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDecodeParam()
	 * @generated
	 * @ordered
	 */
	protected DecoderParameter decodeParam;

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
	 * The default value of the '{@link #getDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefault()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefault() <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefault()
	 * @generated
	 * @ordered
	 */
	protected String default_ = DEFAULT_EDEFAULT;

	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger INDEX_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected BigInteger index = INDEX_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ValueParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EClass eStaticClass() {
		return Persistence12Package.Literals.VALUE_PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DecoderParameter getDecodeParam() {
		return decodeParam;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDecodeParam(DecoderParameter newDecodeParam, NotificationChain msgs) {
		DecoderParameter oldDecodeParam = decodeParam;
		decodeParam = newDecodeParam;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, Persistence12Package.VALUE_PARAMETER__DECODE_PARAM, oldDecodeParam, newDecodeParam);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDecodeParam(DecoderParameter newDecodeParam) {
		if (newDecodeParam != decodeParam) {
			NotificationChain msgs = null;
			if (decodeParam != null)
				msgs = ((InternalEObject)decodeParam).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - Persistence12Package.VALUE_PARAMETER__DECODE_PARAM, null, msgs);
			if (newDecodeParam != null)
				msgs = ((InternalEObject)newDecodeParam).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - Persistence12Package.VALUE_PARAMETER__DECODE_PARAM, null, msgs);
			msgs = basicSetDecodeParam(newDecodeParam, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.VALUE_PARAMETER__DECODE_PARAM, newDecodeParam, newDecodeParam));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.VALUE_PARAMETER__DATA, oldData, data));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.VALUE_PARAMETER__DATA_NS, oldDataNS, dataNS));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.VALUE_PARAMETER__DECODER, oldDecoder, decoder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefault() {
		return default_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefault(String newDefault) {
		String oldDefault = default_;
		default_ = newDefault;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.VALUE_PARAMETER__DEFAULT, oldDefault, default_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BigInteger getIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(BigInteger newIndex) {
		BigInteger oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.VALUE_PARAMETER__INDEX, oldIndex, index));
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
			eNotify(new ENotificationImpl(this, Notification.SET, Persistence12Package.VALUE_PARAMETER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case Persistence12Package.VALUE_PARAMETER__DECODE_PARAM:
				return basicSetDecodeParam(null, msgs);
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
			case Persistence12Package.VALUE_PARAMETER__DECODE_PARAM:
				return getDecodeParam();
			case Persistence12Package.VALUE_PARAMETER__DATA:
				return getData();
			case Persistence12Package.VALUE_PARAMETER__DATA_NS:
				return getDataNS();
			case Persistence12Package.VALUE_PARAMETER__DECODER:
				return getDecoder();
			case Persistence12Package.VALUE_PARAMETER__DEFAULT:
				return getDefault();
			case Persistence12Package.VALUE_PARAMETER__INDEX:
				return getIndex();
			case Persistence12Package.VALUE_PARAMETER__NAME:
				return getName();
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
			case Persistence12Package.VALUE_PARAMETER__DECODE_PARAM:
				setDecodeParam((DecoderParameter)newValue);
				return;
			case Persistence12Package.VALUE_PARAMETER__DATA:
				setData((String)newValue);
				return;
			case Persistence12Package.VALUE_PARAMETER__DATA_NS:
				setDataNS((String)newValue);
				return;
			case Persistence12Package.VALUE_PARAMETER__DECODER:
				setDecoder((String)newValue);
				return;
			case Persistence12Package.VALUE_PARAMETER__DEFAULT:
				setDefault((String)newValue);
				return;
			case Persistence12Package.VALUE_PARAMETER__INDEX:
				setIndex((BigInteger)newValue);
				return;
			case Persistence12Package.VALUE_PARAMETER__NAME:
				setName((String)newValue);
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
			case Persistence12Package.VALUE_PARAMETER__DECODE_PARAM:
				setDecodeParam((DecoderParameter)null);
				return;
			case Persistence12Package.VALUE_PARAMETER__DATA:
				setData(DATA_EDEFAULT);
				return;
			case Persistence12Package.VALUE_PARAMETER__DATA_NS:
				setDataNS(DATA_NS_EDEFAULT);
				return;
			case Persistence12Package.VALUE_PARAMETER__DECODER:
				setDecoder(DECODER_EDEFAULT);
				return;
			case Persistence12Package.VALUE_PARAMETER__DEFAULT:
				setDefault(DEFAULT_EDEFAULT);
				return;
			case Persistence12Package.VALUE_PARAMETER__INDEX:
				setIndex(INDEX_EDEFAULT);
				return;
			case Persistence12Package.VALUE_PARAMETER__NAME:
				setName(NAME_EDEFAULT);
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
			case Persistence12Package.VALUE_PARAMETER__DECODE_PARAM:
				return decodeParam != null;
			case Persistence12Package.VALUE_PARAMETER__DATA:
				return DATA_EDEFAULT == null ? data != null : !DATA_EDEFAULT.equals(data);
			case Persistence12Package.VALUE_PARAMETER__DATA_NS:
				return DATA_NS_EDEFAULT == null ? dataNS != null : !DATA_NS_EDEFAULT.equals(dataNS);
			case Persistence12Package.VALUE_PARAMETER__DECODER:
				return DECODER_EDEFAULT == null ? decoder != null : !DECODER_EDEFAULT.equals(decoder);
			case Persistence12Package.VALUE_PARAMETER__DEFAULT:
				return DEFAULT_EDEFAULT == null ? default_ != null : !DEFAULT_EDEFAULT.equals(default_);
			case Persistence12Package.VALUE_PARAMETER__INDEX:
				return INDEX_EDEFAULT == null ? index != null : !INDEX_EDEFAULT.equals(index);
			case Persistence12Package.VALUE_PARAMETER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
		result.append(" (data: "); //$NON-NLS-1$
		result.append(data);
		result.append(", dataNS: "); //$NON-NLS-1$
		result.append(dataNS);
		result.append(", decoder: "); //$NON-NLS-1$
		result.append(decoder);
		result.append(", default: "); //$NON-NLS-1$
		result.append(default_);
		result.append(", index: "); //$NON-NLS-1$
		result.append(index);
		result.append(", name: "); //$NON-NLS-1$
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //ValueParameterImpl
