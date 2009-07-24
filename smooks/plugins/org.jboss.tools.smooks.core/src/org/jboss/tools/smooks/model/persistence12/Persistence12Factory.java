/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package
 * @generated
 */
public interface Persistence12Factory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Persistence12Factory eINSTANCE = org.jboss.tools.smooks.model.persistence12.impl.Persistence12FactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Decoder Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Decoder Parameter</em>'.
	 * @generated
	 */
	DecoderParameter createDecoderParameter();

	/**
	 * Returns a new object of class '<em>Deleter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Deleter</em>'.
	 * @generated
	 */
	Deleter createDeleter();

	/**
	 * Returns a new object of class '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Document Root</em>'.
	 * @generated
	 */
	Persistence12DocumentRoot createPersistence12DocumentRoot();

	/**
	 * Returns a new object of class '<em>Expression Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Expression Parameter</em>'.
	 * @generated
	 */
	ExpressionParameter createExpressionParameter();

	/**
	 * Returns a new object of class '<em>Flusher</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Flusher</em>'.
	 * @generated
	 */
	Flusher createFlusher();

	/**
	 * Returns a new object of class '<em>Inserter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Inserter</em>'.
	 * @generated
	 */
	Inserter createInserter();

	/**
	 * Returns a new object of class '<em>Locator</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Locator</em>'.
	 * @generated
	 */
	Locator createLocator();

	/**
	 * Returns a new object of class '<em>Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parameters</em>'.
	 * @generated
	 */
	Parameters createParameters();

	/**
	 * Returns a new object of class '<em>Updater</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Updater</em>'.
	 * @generated
	 */
	Updater createUpdater();

	/**
	 * Returns a new object of class '<em>Value Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Value Parameter</em>'.
	 * @generated
	 */
	ValueParameter createValueParameter();

	/**
	 * Returns a new object of class '<em>Wiring Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Wiring Parameter</em>'.
	 * @generated
	 */
	WiringParameter createWiringParameter();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	Persistence12Package getPersistence12Package();

} //Persistence12Factory
