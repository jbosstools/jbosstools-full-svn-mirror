/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.common;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.xml.type.AnyType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Any Type</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.jboss.tools.smooks.model.common.CommonPackage#getAbstractAnyType()
 * @model abstract="true"
 * @generated
 */
public interface AbstractAnyType extends EObject, AnyType {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getCDATA();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model cdataDataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	void setCDATA(String cdata);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getStringValue();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model stringValueDataType="org.eclipse.emf.ecore.xml.type.String"
	 * @generated
	 */
	void setStringValue(String stringValue);

} // AbstractAnyType
