/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage
 * @generated
 */
public interface SmooksFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SmooksFactory eINSTANCE = org.jboss.tools.smooks10.model.smooks.impl.SmooksFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Condition Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Condition Type</em>'.
	 * @generated
	 */
	ConditionType createConditionType();

	/**
	 * Returns a new object of class '<em>Smooks10 Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Smooks10 Document Root</em>'.
	 * @generated
	 */
	DocumentRoot createSmooks10DocumentRoot();

	/**
	 * Returns a new object of class '<em>Import Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Import Type</em>'.
	 * @generated
	 */
	ImportType createImportType();

	/**
	 * Returns a new object of class '<em>Param Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Param Type</em>'.
	 * @generated
	 */
	ParamType createParamType();

	/**
	 * Returns a new object of class '<em>Profiles Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Profiles Type</em>'.
	 * @generated
	 */
	ProfilesType createProfilesType();

	/**
	 * Returns a new object of class '<em>Profile Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Profile Type</em>'.
	 * @generated
	 */
	ProfileType createProfileType();

	/**
	 * Returns a new object of class '<em>Resource Config Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Config Type</em>'.
	 * @generated
	 */
	ResourceConfigType createResourceConfigType();

	/**
	 * Returns a new object of class '<em>Resource Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Type</em>'.
	 * @generated
	 */
	ResourceType createResourceType();

	/**
	 * Returns a new object of class '<em>Resource List Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource List Type</em>'.
	 * @generated
	 */
	SmooksResourceListType createSmooksResourceListType();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SmooksPackage getSmooksPackage();

} //SmooksFactory
