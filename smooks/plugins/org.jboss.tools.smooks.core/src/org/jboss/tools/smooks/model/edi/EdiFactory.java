/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.edi;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.edi.EdiPackage
 * @generated
 */
public interface EdiFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EdiFactory eINSTANCE = org.jboss.tools.smooks.model.edi.impl.EdiFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>EDI Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EDI Document Root</em>'.
	 * @generated
	 */
	EDIDocumentRoot createEDIDocumentRoot();

	/**
	 * Returns a new object of class '<em>EDI Reader</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>EDI Reader</em>'.
	 * @generated
	 */
	EDIReader createEDIReader();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EdiPackage getEdiPackage();

} //EdiFactory
