/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.datasource.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot;
import org.jboss.tools.smooks.model.datasource.DataSourceJndi;
import org.jboss.tools.smooks.model.datasource.DatasourceFactory;
import org.jboss.tools.smooks.model.datasource.DatasourcePackage;
import org.jboss.tools.smooks.model.datasource.Direct;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DatasourceFactoryImpl extends EFactoryImpl implements DatasourceFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DatasourceFactory init() {
		try {
			DatasourceFactory theDatasourceFactory = (DatasourceFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/datasource-1.1.xsd");  //$NON-NLS-1$
			if (theDatasourceFactory != null) {
				return theDatasourceFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DatasourceFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatasourceFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DatasourcePackage.DIRECT: return createDirect();
			case DatasourcePackage.DATA_SOURCE_DOCUMENT_ROOT: return createDataSourceDocumentRoot();
			case DatasourcePackage.DATA_SOURCE_JNDI: return createDataSourceJndi();
			default:
				throw new IllegalArgumentException(Messages.DatasourceFactoryImpl_Error_Invalid_Class + eClass.getName() + Messages.DatasourceFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Direct createDirect() {
		DirectImpl direct = new DirectImpl();
		return direct;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataSourceDocumentRoot createDataSourceDocumentRoot() {
		DataSourceDocumentRootImpl dataSourceDocumentRoot = new DataSourceDocumentRootImpl();
		return dataSourceDocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataSourceJndi createDataSourceJndi() {
		DataSourceJndiImpl dataSourceJndi = new DataSourceJndiImpl();
		return dataSourceJndi;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DatasourcePackage getDatasourcePackage() {
		return (DatasourcePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DatasourcePackage getPackage() {
		return DatasourcePackage.eINSTANCE;
	}

} //DatasourceFactoryImpl
