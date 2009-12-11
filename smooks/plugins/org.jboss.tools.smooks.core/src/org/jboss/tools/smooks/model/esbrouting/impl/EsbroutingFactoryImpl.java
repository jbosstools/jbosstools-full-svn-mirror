/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.esbrouting.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.esbrouting.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EsbroutingFactoryImpl extends EFactoryImpl implements EsbroutingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EsbroutingFactory init() {
		try {
			EsbroutingFactory theEsbroutingFactory = (EsbroutingFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.jboss.org/xsd/jbossesb/smooks/routing-1.0.xsd");  //$NON-NLS-1$
			if (theEsbroutingFactory != null) {
				return theEsbroutingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EsbroutingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EsbroutingFactoryImpl() {
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
			case EsbroutingPackage.ESB_ROUTING_DOCUMENT_ROOT: return createESBRoutingDocumentRoot();
			case EsbroutingPackage.ROUTE_BEAN: return createRouteBean();
			default:
				throw new IllegalArgumentException(Messages.EsbroutingFactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.EsbroutingFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ESBRoutingDocumentRoot createESBRoutingDocumentRoot() {
		ESBRoutingDocumentRootImpl esbRoutingDocumentRoot = new ESBRoutingDocumentRootImpl();
		return esbRoutingDocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RouteBean createRouteBean() {
		RouteBeanImpl routeBean = new RouteBeanImpl();
		return routeBean;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EsbroutingPackage getEsbroutingPackage() {
		return (EsbroutingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EsbroutingPackage getPackage() {
		return EsbroutingPackage.eINSTANCE;
	}

} //EsbroutingFactoryImpl
