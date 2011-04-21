/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.json;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Key Map</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.json.KeyMap#getKey <em>Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.json.JsonPackage#getKeyMap()
 * @model extendedMetaData="name='keyMap' kind='elementOnly'"
 * @generated
 */
public interface KeyMap extends EObject {
	/**
	 * Returns the value of the '<em><b>Key</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.json.Key}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			   		 	Defines a JSON element name mapping
	 * 			   		 	The "from" key will be replaced with the "to" key or the contents of this element.
	 * 		   		 	
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Key</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.json.JsonPackage#getKeyMap_Key()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='key' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Key> getKey();

} // KeyMap
