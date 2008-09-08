/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.milyn.xsd.smooks;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profiles Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * List of Profiles
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.milyn.xsd.smooks.ProfilesType#getProfile <em>Profile</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.milyn.xsd.smooks.SmooksPackage#getProfilesType()
 * @model extendedMetaData="name='profiles_._type' kind='elementOnly'"
 * @generated
 */
public interface ProfilesType extends EObject {
	/**
	 * Returns the value of the '<em><b>Profile</b></em>' containment reference list.
	 * The list contents are of type {@link org.milyn.xsd.smooks.ProfileType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profile</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profile</em>' containment reference list.
	 * @see org.milyn.xsd.smooks.SmooksPackage#getProfilesType_Profile()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='profile' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ProfileType> getProfile();

} // ProfilesType
