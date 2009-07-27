/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.util;


import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.xml.type.AnyType;
import org.jboss.tools.smooks.model.common.AbstractAnyType;
import org.jboss.tools.smooks.model.persistence12.*;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.ElementVisitor;



/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package
 * @generated
 */
public class Persistence12Switch {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Persistence12Package modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Persistence12Switch() {
		if (modelPackage == null) {
			modelPackage = Persistence12Package.eINSTANCE;
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	public Object doSwitch(EObject theEObject) {
		return doSwitch(theEObject.eClass(), theEObject);
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(EClass theEClass, EObject theEObject) {
		if (theEClass.eContainer() == modelPackage) {
			return doSwitch(theEClass.getClassifierID(), theEObject);
		}
		else {
			List eSuperTypes = theEClass.getESuperTypes();
			return
				eSuperTypes.isEmpty() ?
					defaultCase(theEObject) :
					doSwitch((EClass)eSuperTypes.get(0), theEObject);
		}
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	protected Object doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case Persistence12Package.DECODER_PARAMETER: {
				DecoderParameter decoderParameter = (DecoderParameter)theEObject;
				Object result = caseDecoderParameter(decoderParameter);
				if (result == null) result = caseAbstractAnyType(decoderParameter);
				if (result == null) result = caseAnyType(decoderParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.DELETER: {
				Deleter deleter = (Deleter)theEObject;
				Object result = caseDeleter(deleter);
				if (result == null) result = caseElementVisitor(deleter);
				if (result == null) result = caseAbstractResourceConfig(deleter);
				if (result == null) result = caseAbstractAnyType(deleter);
				if (result == null) result = caseAnyType(deleter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT: {
				Persistence12DocumentRoot persistence12DocumentRoot = (Persistence12DocumentRoot)theEObject;
				Object result = casePersistence12DocumentRoot(persistence12DocumentRoot);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.EXPRESSION_PARAMETER: {
				ExpressionParameter expressionParameter = (ExpressionParameter)theEObject;
				Object result = caseExpressionParameter(expressionParameter);
				if (result == null) result = caseAbstractAnyType(expressionParameter);
				if (result == null) result = caseAnyType(expressionParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.FLUSHER: {
				Flusher flusher = (Flusher)theEObject;
				Object result = caseFlusher(flusher);
				if (result == null) result = caseElementVisitor(flusher);
				if (result == null) result = caseAbstractResourceConfig(flusher);
				if (result == null) result = caseAbstractAnyType(flusher);
				if (result == null) result = caseAnyType(flusher);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.INSERTER: {
				Inserter inserter = (Inserter)theEObject;
				Object result = caseInserter(inserter);
				if (result == null) result = caseElementVisitor(inserter);
				if (result == null) result = caseAbstractResourceConfig(inserter);
				if (result == null) result = caseAbstractAnyType(inserter);
				if (result == null) result = caseAnyType(inserter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.LOCATOR: {
				Locator locator = (Locator)theEObject;
				Object result = caseLocator(locator);
				if (result == null) result = caseElementVisitor(locator);
				if (result == null) result = caseAbstractResourceConfig(locator);
				if (result == null) result = caseAbstractAnyType(locator);
				if (result == null) result = caseAnyType(locator);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.PARAMETERS: {
				Parameters parameters = (Parameters)theEObject;
				Object result = caseParameters(parameters);
				if (result == null) result = caseAbstractAnyType(parameters);
				if (result == null) result = caseAnyType(parameters);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.UPDATER: {
				Updater updater = (Updater)theEObject;
				Object result = caseUpdater(updater);
				if (result == null) result = caseElementVisitor(updater);
				if (result == null) result = caseAbstractResourceConfig(updater);
				if (result == null) result = caseAbstractAnyType(updater);
				if (result == null) result = caseAnyType(updater);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.VALUE_PARAMETER: {
				ValueParameter valueParameter = (ValueParameter)theEObject;
				Object result = caseValueParameter(valueParameter);
				if (result == null) result = caseAbstractAnyType(valueParameter);
				if (result == null) result = caseAnyType(valueParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case Persistence12Package.WIRING_PARAMETER: {
				WiringParameter wiringParameter = (WiringParameter)theEObject;
				Object result = caseWiringParameter(wiringParameter);
				if (result == null) result = caseAbstractAnyType(wiringParameter);
				if (result == null) result = caseAnyType(wiringParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Decoder Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Decoder Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDecoderParameter(DecoderParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Deleter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Deleter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseDeleter(Deleter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object casePersistence12DocumentRoot(Persistence12DocumentRoot object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Expression Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Expression Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseExpressionParameter(ExpressionParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Flusher</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Flusher</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseFlusher(Flusher object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Inserter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Inserter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseInserter(Inserter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Locator</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Locator</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseLocator(Locator object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Parameters</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Parameters</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseParameters(Parameters object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Updater</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Updater</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseUpdater(Updater object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Value Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Value Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseValueParameter(ValueParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Wiring Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Wiring Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseWiringParameter(WiringParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Any Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Any Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAnyType(AnyType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Any Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Any Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAbstractAnyType(AbstractAnyType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Abstract Resource Config</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Abstract Resource Config</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseAbstractResourceConfig(AbstractResourceConfig object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Element Visitor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Element Visitor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public Object caseElementVisitor(ElementVisitor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	public Object defaultCase(EObject object) {
		return null;
	}

} //Persistence12Switch
