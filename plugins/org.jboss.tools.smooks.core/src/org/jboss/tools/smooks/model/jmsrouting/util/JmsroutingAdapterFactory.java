/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting.util;


import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.jboss.tools.smooks.model.common.AbstractAnyType;
import org.jboss.tools.smooks.model.jmsrouting.Connection;
import org.jboss.tools.smooks.model.jmsrouting.DocumentRoot;
import org.jboss.tools.smooks.model.jmsrouting.HighWaterMark;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting.Jndi;
import org.jboss.tools.smooks.model.jmsrouting.Message;
import org.jboss.tools.smooks.model.jmsrouting.Router;
import org.jboss.tools.smooks.model.jmsrouting.Session;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.ElementVisitor;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage
 * @generated
 */
public class JmsroutingAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static JmsroutingPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JmsroutingAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = JmsroutingPackage.eINSTANCE;
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
	@Override
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
	protected JmsroutingSwitch<Adapter> modelSwitch =
		new JmsroutingSwitch<Adapter>() {
			@Override
			public Adapter caseConnection(Connection object) {
				return createConnectionAdapter();
			}
			@Override
			public Adapter caseDocumentRoot(DocumentRoot object) {
				return createDocumentRootAdapter();
			}
			@Override
			public Adapter caseHighWaterMark(HighWaterMark object) {
				return createHighWaterMarkAdapter();
			}
			@Override
			public Adapter caseJndi(Jndi object) {
				return createJndiAdapter();
			}
			@Override
			public Adapter caseMessage(Message object) {
				return createMessageAdapter();
			}
			@Override
			public Adapter caseRouter(Router object) {
				return createRouterAdapter();
			}
			@Override
			public Adapter caseSession(Session object) {
				return createSessionAdapter();
			}
			@Override
			public Adapter caseAnyType(AnyType object) {
				return createAnyTypeAdapter();
			}
			@Override
			public Adapter caseAbstractAnyType(AbstractAnyType object) {
				return createAbstractAnyTypeAdapter();
			}
			@Override
			public Adapter caseAbstractResourceConfig(AbstractResourceConfig object) {
				return createAbstractResourceConfigAdapter();
			}
			@Override
			public Adapter caseElementVisitor(ElementVisitor object) {
				return createElementVisitorAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
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
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.jmsrouting.Connection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Connection
	 * @generated
	 */
	public Adapter createConnectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.jmsrouting.DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.jmsrouting.DocumentRoot
	 * @generated
	 */
	public Adapter createDocumentRootAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.jmsrouting.HighWaterMark <em>High Water Mark</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.jmsrouting.HighWaterMark
	 * @generated
	 */
	public Adapter createHighWaterMarkAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.jmsrouting.Jndi <em>Jndi</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Jndi
	 * @generated
	 */
	public Adapter createJndiAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.jmsrouting.Message <em>Message</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Message
	 * @generated
	 */
	public Adapter createMessageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.jmsrouting.Router <em>Router</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Router
	 * @generated
	 */
	public Adapter createRouterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.jboss.tools.smooks.model.jmsrouting.Session <em>Session</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.jboss.tools.smooks.model.jmsrouting.Session
	 * @generated
	 */
	public Adapter createSessionAdapter() {
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
	 * Creates a new adapter for an object of class '{@link common.AbstractAnyType <em>Abstract Any Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see common.AbstractAnyType
	 * @generated
	 */
	public Adapter createAbstractAnyTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link smooks.AbstractResourceConfig <em>Abstract Resource Config</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see smooks.AbstractResourceConfig
	 * @generated
	 */
	public Adapter createAbstractResourceConfigAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link smooks.ElementVisitor <em>Element Visitor</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see smooks.ElementVisitor
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

} //JmsroutingAdapterFactory
