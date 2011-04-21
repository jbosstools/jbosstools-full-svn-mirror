/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.iorouting.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.iorouting.IODocumentRoot;
import org.jboss.tools.smooks.model.iorouting.IORouter;
import org.jboss.tools.smooks.model.iorouting.IoroutingFactory;
import org.jboss.tools.smooks.model.iorouting.IoroutingPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class IoroutingFactoryImpl extends EFactoryImpl implements IoroutingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static IoroutingFactory init() {
		try {
			IoroutingFactory theIoroutingFactory = (IoroutingFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/io-routing-1.1.xsd");  //$NON-NLS-1$
			if (theIoroutingFactory != null) {
				return theIoroutingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new IoroutingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IoroutingFactoryImpl() {
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
			case IoroutingPackage.IO_DOCUMENT_ROOT: return createIODocumentRoot();
			case IoroutingPackage.IO_ROUTER: return createIORouter();
			default:
				throw new IllegalArgumentException(Messages.IoroutingFactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.IoroutingFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IODocumentRoot createIODocumentRoot() {
		IODocumentRootImpl ioDocumentRoot = new IODocumentRootImpl();
		return ioDocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IORouter createIORouter() {
		IORouterImpl ioRouter = new IORouterImpl();
		return ioRouter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IoroutingPackage getIoroutingPackage() {
		return (IoroutingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static IoroutingPackage getPackage() {
		return IoroutingPackage.eINSTANCE;
	}

} //IoroutingFactoryImpl
