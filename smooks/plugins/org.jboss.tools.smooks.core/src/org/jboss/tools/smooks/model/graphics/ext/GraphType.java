/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Graph Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.GraphType#getFigure <em>Figure</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.GraphType#getConnection <em>Connection</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getGraphType()
 * @model extendedMetaData="name='graph_._type' kind='elementOnly'"
 * @generated
 */
public interface GraphType extends EObject {
	/**
	 * Returns the value of the '<em><b>Figure</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.graphics.ext.FigureType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Figure</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Figure</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getGraphType_Figure()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='figure' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<FigureType> getFigure();

	/**
	 * Returns the value of the '<em><b>Connection</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.graphics.ext.ConnectionType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getGraphType_Connection()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='connection' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ConnectionType> getConnection();

} // GraphType
