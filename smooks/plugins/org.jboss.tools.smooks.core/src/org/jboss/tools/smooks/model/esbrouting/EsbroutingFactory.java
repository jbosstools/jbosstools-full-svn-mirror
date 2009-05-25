/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.esbrouting;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage
 * @generated
 */
public interface EsbroutingFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EsbroutingFactory eINSTANCE = org.jboss.tools.smooks.model.esbrouting.impl.EsbroutingFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>ESB Routing Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>ESB Routing Document Root</em>'.
	 * @generated
	 */
	ESBRoutingDocumentRoot createESBRoutingDocumentRoot();

	/**
	 * Returns a new object of class '<em>Route Bean</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Route Bean</em>'.
	 * @generated
	 */
	RouteBean createRouteBean();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EsbroutingPackage getEsbroutingPackage();

} //EsbroutingFactory
