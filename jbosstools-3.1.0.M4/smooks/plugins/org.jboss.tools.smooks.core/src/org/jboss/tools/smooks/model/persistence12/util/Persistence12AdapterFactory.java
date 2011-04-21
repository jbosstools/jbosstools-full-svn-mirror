/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.util;


import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.jboss.tools.smooks.model.common.AbstractAnyType;
import org.jboss.tools.smooks.model.persistence12.*;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.ElementVisitor;



/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package
 * @generated
 */
public class Persistence12AdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Persistence12Package modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Persistence12AdapterFactory() {
		if (modelPackage == null) {
			modelPackage = Persistence12Package.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Persistence12Switch modelSwitch =
		new Persistence12Switch() {
			public Object caseDecoderParameter(DecoderParameter object) {
				return createDecoderParameterAdapter();
			}
			public Object caseDeleter(Deleter object) {
				return createDeleterAdapter();
			}
			public Object casePersistence12DocumentRoot(Persistence12DocumentRoot object) {
				return createPersistence12DocumentRootAdapter();
			}
			public Object caseExpressionParameter(ExpressionParameter object) {
				return createExpressionParameterAdapter();
			}
			public Object caseFlusher(Flusher object) {
				return createFlusherAdapter();
			}
			public Object caseInserter(Inserter object) {
				return createInserterAdapter();
			}
			public Object caseLocator(Locator object) {
				return createLocatorAdapter();
			}
			public Object caseParameters(Parameters object) {
				return createParametersAdapter();
			}
			public Object caseUpdater(Updater object) {
				return createUpdaterAdapter();
			}
			public Object caseValueParameter(ValueParameter object) {
				return createValueParameterAdapter();
			}
			public Object caseWiringParameter(WiringParameter object) {
				return createWiringParameterAdapter();
			}
			public Object caseAnyType(AnyType object) {
				return createAnyTypeAdapter();
			}
			public Object caseAbstractAnyType(AbstractAnyType object) {
				return createAbstractAnyTypeAdapter();
			}
			public Object caseAbstractResourceConfig(AbstractResourceConfig object) {
				return createAbstractResourceConfigAdapter();
			}
			public Object caseElementVisitor(ElementVisitor object) {
				return createElementVisitorAdapter();
			}
			public Object defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	public Adapter createAdapter(Notifier target) {
		return (Adapter)modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.DecoderParameter <em>Decoder Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.DecoderParameter
	 * @generated
	 */
	public Adapter createDecoderParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.Deleter <em>Deleter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.Deleter
	 * @generated
	 */
	public Adapter createDeleterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot
	 * @generated
	 */
	public Adapter createPersistence12DocumentRootAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter <em>Expression Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.ExpressionParameter
	 * @generated
	 */
	public Adapter createExpressionParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.Flusher <em>Flusher</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.Flusher
	 * @generated
	 */
	public Adapter createFlusherAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.Inserter <em>Inserter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.Inserter
	 * @generated
	 */
	public Adapter createInserterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.Locator <em>Locator</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.Locator
	 * @generated
	 */
	public Adapter createLocatorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.Parameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.Parameters
	 * @generated
	 */
	public Adapter createParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.Updater <em>Updater</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.Updater
	 * @generated
	 */
	public Adapter createUpdaterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.ValueParameter <em>Value Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.ValueParameter
	 * @generated
	 */
	public Adapter createValueParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter <em>Wiring Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.persistence12.WiringParameter
	 * @generated
	 */
	public Adapter createWiringParameterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.emf.ecore.xml.type.AnyType <em>Any Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.emf.ecore.xml.type.AnyType
	 * @generated
	 */
	public Adapter createAnyTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.common.AbstractAnyType <em>Abstract Any Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.common.AbstractAnyType
	 * @generated
	 */
	public Adapter createAbstractAnyTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.smooks.AbstractResourceConfig <em>Abstract Resource Config</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.smooks.AbstractResourceConfig
	 * @generated
	 */
	public Adapter createAbstractResourceConfigAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.smooks.ElementVisitor <em>Element Visitor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.smooks.ElementVisitor
	 * @generated
	 */
	public Adapter createElementVisitorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //Persistence12AdapterFactory
