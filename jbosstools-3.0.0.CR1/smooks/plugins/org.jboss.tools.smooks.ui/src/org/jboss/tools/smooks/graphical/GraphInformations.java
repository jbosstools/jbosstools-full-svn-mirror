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
 * A representation of the model object '<em><b>Graph Informations</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.graphical.GraphInformations#getMappingType <em>Mapping Type</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.graphical.GraphInformations#getParams <em>Params</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.graphical.GraphicalPackage#getGraphInformations()
 * @model
 * @generated
 */
public interface GraphInformations extends EObject {
	/**
	 * Returns the value of the '<em><b>Mapping Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mapping Type</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mapping Type</em>' containment reference.
	 * @see #setMappingType(MappingDataType)
	 * @see org.jboss.tools.smooks.graphical.GraphicalPackage#getGraphInformations_MappingType()
	 * @model containment="true"
	 * @generated
	 */
	MappingDataType getMappingType();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.graphical.GraphInformations#getMappingType <em>Mapping Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mapping Type</em>' containment reference.
	 * @see #getMappingType()
	 * @generated
	 */
	void setMappingType(MappingDataType value);

	/**
	 * Returns the value of the '<em><b>Params</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Params</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Params</em>' containment reference.
	 * @see #setParams(Params)
	 * @see org.jboss.tools.smooks.graphical.GraphicalPackage#getGraphInformations_Params()
	 * @model containment="true"
	 * @generated
	 */
	Params getParams();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.graphical.GraphInformations#getParams <em>Params</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Params</em>' containment reference.
	 * @see #getParams()
	 * @generated
	 */
	void setParams(Params value);

} // GraphInformations
