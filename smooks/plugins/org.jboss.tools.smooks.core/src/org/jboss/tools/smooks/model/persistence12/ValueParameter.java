/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12;


import java.math.BigInteger;

import org.jboss.tools.smooks.model.common.AbstractAnyType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDecodeParam <em>Decode Param</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getData <em>Data</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDataNS <em>Data NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDecoder <em>Decoder</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDefault <em>Default</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getIndex <em>Index</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getValueParameter()
 * @model extendedMetaData="name='valueParameter' kind='elementOnly'"
 * @generated
 */
public interface ValueParameter extends AbstractAnyType {
	/**
	 * Returns the value of the '<em><b>Decode Param</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Decode Param</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Decode Param</em>' containment reference.
	 * @see #setDecodeParam(DecoderParameter)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getValueParameter_DecodeParam()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='decodeParam' namespace='##targetNamespace'"
	 * @generated
	 */
	DecoderParameter getDecodeParam();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDecodeParam <em>Decode Param</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Decode Param</em>' containment reference.
	 * @see #getDecodeParam()
	 * @generated
	 */
	void setDecodeParam(DecoderParameter value);

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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getValueParameter_Data()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='data'"
	 * @generated
	 */
	String getData();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getData <em>Data</em>}' attribute.
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getValueParameter_DataNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='dataNS'"
	 * @generated
	 */
	String getDataNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDataNS <em>Data NS</em>}' attribute.
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getValueParameter_Decoder()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='decoder'"
	 * @generated
	 */
	String getDecoder();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDecoder <em>Decoder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Decoder</em>' attribute.
	 * @see #getDecoder()
	 * @generated
	 */
	void setDecoder(String value);

	/**
	 * Returns the value of the '<em><b>Default</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default</em>' attribute.
	 * @see #setDefault(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getValueParameter_Default()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='default'"
	 * @generated
	 */
	String getDefault();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getDefault <em>Default</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default</em>' attribute.
	 * @see #getDefault()
	 * @generated
	 */
	void setDefault(String value);

	/**
	 * Returns the value of the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' attribute.
	 * @see #setIndex(BigInteger)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getValueParameter_Index()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Integer"
	 *        extendedMetaData="kind='attribute' name='index'"
	 * @generated
	 */
	BigInteger getIndex();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getIndex <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' attribute.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(BigInteger value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getValueParameter_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ValueParameter
