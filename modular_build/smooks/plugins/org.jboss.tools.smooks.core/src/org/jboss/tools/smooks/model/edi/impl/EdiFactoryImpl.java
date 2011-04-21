/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.edi.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.edi.EDIDocumentRoot;
import org.jboss.tools.smooks.model.edi.EDIReader;
import org.jboss.tools.smooks.model.edi.EdiFactory;
import org.jboss.tools.smooks.model.edi.EdiPackage;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EdiFactoryImpl extends EFactoryImpl implements EdiFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EdiFactory init() {
		try {
			EdiFactory theEdiFactory = (EdiFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/edi-1.1.xsd");  //$NON-NLS-1$
			if (theEdiFactory != null) {
				return theEdiFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EdiFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EdiFactoryImpl() {
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
			case EdiPackage.EDI_DOCUMENT_ROOT: return createEDIDocumentRoot();
			case EdiPackage.EDI_READER: return createEDIReader();
			default:
				throw new IllegalArgumentException(Messages.EdiFactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.EdiFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDIDocumentRoot createEDIDocumentRoot() {
		EDIDocumentRootImpl ediDocumentRoot = new EDIDocumentRootImpl();
		return ediDocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDIReader createEDIReader() {
		EDIReaderImpl ediReader = new EDIReaderImpl();
		return ediReader;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EdiPackage getEdiPackage() {
		return (EdiPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EdiPackage getPackage() {
		return EdiPackage.eINSTANCE;
	}

} //EdiFactoryImpl
