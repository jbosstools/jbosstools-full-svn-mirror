/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.graphical;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mapping Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.graphical.MappingDataType#getTargetTypeID <em>Target Type ID</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.graphical.MappingDataType#getSourceTypeID <em>Source Type ID</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.graphical.GraphicalPackage#getMappingDataType()
 * @model
 * @generated
 */
public interface MappingDataType extends EObject {
	/**
	 * Returns the value of the '<em><b>Target Type ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Type ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Type ID</em>' attribute.
	 * @see #setTargetTypeID(String)
	 * @see org.jboss.tools.smooks.graphical.GraphicalPackage#getMappingDataType_TargetTypeID()
	 * @model
	 * @generated
	 */
	String getTargetTypeID();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.graphical.MappingDataType#getTargetTypeID <em>Target Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Type ID</em>' attribute.
	 * @see #getTargetTypeID()
	 * @generated
	 */
	void setTargetTypeID(String value);

	/**
	 * Returns the value of the '<em><b>Source Type ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Type ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Type ID</em>' attribute.
	 * @see #setSourceTypeID(String)
	 * @see org.jboss.tools.smooks.graphical.GraphicalPackage#getMappingDataType_SourceTypeID()
	 * @model
	 * @generated
	 */
	String getSourceTypeID();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.graphical.MappingDataType#getSourceTypeID <em>Source Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Type ID</em>' attribute.
	 * @see #getSourceTypeID()
	 * @generated
	 */
	void setSourceTypeID(String value);

} // MappingDataType
