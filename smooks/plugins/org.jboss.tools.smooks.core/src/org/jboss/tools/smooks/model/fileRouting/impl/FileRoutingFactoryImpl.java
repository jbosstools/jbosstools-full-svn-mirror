/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.fileRouting.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.fileRouting.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FileRoutingFactoryImpl extends EFactoryImpl implements FileRoutingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FileRoutingFactory init() {
		try {
			FileRoutingFactory theFileRoutingFactory = (FileRoutingFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/file-routing-1.1.xsd");  //$NON-NLS-1$
			if (theFileRoutingFactory != null) {
				return theFileRoutingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new FileRoutingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FileRoutingFactoryImpl() {
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
			case FileRoutingPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case FileRoutingPackage.HIGH_WATER_MARK: return createHighWaterMark();
			case FileRoutingPackage.OUTPUT_STREAM: return createOutputStream();
			default:
				throw new IllegalArgumentException(Messages.FileRoutingFactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.FileRoutingFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DocumentRoot createDocumentRoot() {
		DocumentRootImpl documentRoot = new DocumentRootImpl();
		return documentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HighWaterMark createHighWaterMark() {
		HighWaterMarkImpl highWaterMark = new HighWaterMarkImpl();
		return highWaterMark;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputStream createOutputStream() {
		OutputStreamImpl outputStream = new OutputStreamImpl();
		return outputStream;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FileRoutingPackage getFileRoutingPackage() {
		return (FileRoutingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static FileRoutingPackage getPackage() {
		return FileRoutingPackage.eINSTANCE;
	}

} //FileRoutingFactoryImpl
