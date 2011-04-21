/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IValue#getProperty <em>Property</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IValue#getSetterMethod <em>Setter Method</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IValue#getData <em>Data</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IValue#getDataNS <em>Data NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IValue#getDecoder <em>Decoder</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IValue#getDefaultVal <em>Default Val</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.IValue#getDecodeParams <em>Decode Params</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getValue()
 * @model
 * @generated
 */
public interface IValue extends EObject {
	/**
	 * Returns the value of the '<em><b>Property</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' attribute.
	 * @see #setProperty(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getValue_Property()
	 * @model required="true"
	 * @generated
	 */
	String getProperty();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IValue#getProperty <em>Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Property</em>' attribute.
	 * @see #getProperty()
	 * @generated
	 */
	void setProperty(String value);

	/**
	 * Returns the value of the '<em><b>Setter Method</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Setter Method</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Setter Method</em>' attribute.
	 * @see #setSetterMethod(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getValue_SetterMethod()
	 * @model required="true"
	 * @generated
	 */
	String getSetterMethod();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IValue#getSetterMethod <em>Setter Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Setter Method</em>' attribute.
	 * @see #getSetterMethod()
	 * @generated
	 */
	void setSetterMethod(String value);

	/**
	 * Returns the value of the '<em><b>Data</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data</em>' attribute.
	 * @see #setData(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getValue_Data()
	 * @model required="true"
	 * @generated
	 */
	String getData();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IValue#getData <em>Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data</em>' attribute.
	 * @see #getData()
	 * @generated
	 */
	void setData(String value);

	/**
	 * Returns the value of the '<em><b>Data NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data NS</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data NS</em>' attribute.
	 * @see #setDataNS(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getValue_DataNS()
	 * @model required="true"
	 * @generated
	 */
	String getDataNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IValue#getDataNS <em>Data NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data NS</em>' attribute.
	 * @see #getDataNS()
	 * @generated
	 */
	void setDataNS(String value);

	/**
	 * Returns the value of the '<em><b>Decoder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Decoder</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Decoder</em>' attribute.
	 * @see #setDecoder(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getValue_Decoder()
	 * @model required="true"
	 * @generated
	 */
	String getDecoder();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IValue#getDecoder <em>Decoder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Decoder</em>' attribute.
	 * @see #getDecoder()
	 * @generated
	 */
	void setDecoder(String value);

	/**
	 * Returns the value of the '<em><b>Default Val</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Val</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Val</em>' attribute.
	 * @see #setDefaultVal(String)
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getValue_DefaultVal()
	 * @model required="true"
	 * @generated
	 */
	String getDefaultVal();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.javabean.IValue#getDefaultVal <em>Default Val</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Val</em>' attribute.
	 * @see #getDefaultVal()
	 * @generated
	 */
	void setDefaultVal(String value);

	/**
	 * Returns the value of the '<em><b>Decode Params</b></em>' reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.javabean.IDecodeParam}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Decode Params</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Decode Params</em>' reference list.
	 * @see org.jboss.tools.smooks.model.javabean.IJavaBeanPackage#getValue_DecodeParams()
	 * @model
	 * @generated
	 */
	EList<IDecodeParam> getDecodeParams();

} // IValue
