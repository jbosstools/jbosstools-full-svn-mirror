/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.iorouting;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.iorouting.IoroutingPackage
 * @generated
 */
public interface IoroutingFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	IoroutingFactory eINSTANCE = org.jboss.tools.smooks.model.iorouting.impl.IoroutingFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>IO Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IO Document Root</em>'.
	 * @generated
	 */
	IODocumentRoot createIODocumentRoot();

	/**
	 * Returns a new object of class '<em>IO Router</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>IO Router</em>'.
	 * @generated
	 */
	IORouter createIORouter();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	IoroutingPackage getIoroutingPackage();

} //IoroutingFactory
