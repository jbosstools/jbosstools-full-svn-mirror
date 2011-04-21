/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.graphical;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Params</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.graphical.Params#getParam <em>Param</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.graphical.GraphicalPackage#getParams()
 * @model
 * @generated
 */
public interface Params extends EObject {
	/**
	 * Returns the value of the '<em><b>Param</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.graphical.Param}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Param</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Param</em>' containment reference list.
	 * @see org.jboss.tools.smooks.graphical.GraphicalPackage#getParams_Param()
	 * @model containment="true"
	 * @generated
	 */
	EList<Param> getParam();

} // Params
