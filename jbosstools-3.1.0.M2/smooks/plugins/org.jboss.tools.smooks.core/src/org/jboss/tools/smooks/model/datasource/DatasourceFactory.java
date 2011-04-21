/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.datasource;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage
 * @generated
 */
public interface DatasourceFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DatasourceFactory eINSTANCE = org.jboss.tools.smooks.model.datasource.impl.DatasourceFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Direct</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Direct</em>'.
	 * @generated
	 */
	Direct createDirect();

	/**
	 * Returns a new object of class '<em>Data Source Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Source Document Root</em>'.
	 * @generated
	 */
	DataSourceDocumentRoot createDataSourceDocumentRoot();

	/**
	 * Returns a new object of class '<em>Data Source Jndi</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Source Jndi</em>'.
	 * @generated
	 */
	DataSourceJndi createDataSourceJndi();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DatasourcePackage getDatasourcePackage();

} //DatasourceFactory
