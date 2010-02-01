/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.calc.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.jboss.tools.smooks.model.calc.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CalcFactoryImpl extends EFactoryImpl implements CalcFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CalcFactory init() {
		try {
			CalcFactory theCalcFactory = (CalcFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/calc-1.1.xsd");  //$NON-NLS-1$
			if (theCalcFactory != null) {
				return theCalcFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CalcFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CalcFactoryImpl() {
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
			case CalcPackage.COUNTER: return createCounter();
			case CalcPackage.CALC_DOCUMENT_ROOT: return createCalcDocumentRoot();
			default:
				throw new IllegalArgumentException(Messages.CalcFactoryImpl_Error_Invalid_Classifier + eClass.getName() + Messages.CalcFactoryImpl_Error_Invalid_Classifier2);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case CalcPackage.COUNT_DIRECTION:
				return createCountDirectionFromString(eDataType, initialValue);
			case CalcPackage.COUNT_DIRECTION_OBJECT:
				return createCountDirectionObjectFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(Messages.CalcFactoryImpl_Error_Invalid_Datatype + eDataType.getName() + Messages.CalcFactoryImpl_Error_Invalid_Classifier2);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case CalcPackage.COUNT_DIRECTION:
				return convertCountDirectionToString(eDataType, instanceValue);
			case CalcPackage.COUNT_DIRECTION_OBJECT:
				return convertCountDirectionObjectToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(Messages.CalcFactoryImpl_Error_Invalid_Datatype + eDataType.getName() + Messages.CalcFactoryImpl_Error_Invalid_Classifier2);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Counter createCounter() {
		CounterImpl counter = new CounterImpl();
		return counter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CalcDocumentRoot createCalcDocumentRoot() {
		CalcDocumentRootImpl calcDocumentRoot = new CalcDocumentRootImpl();
		return calcDocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CountDirection createCountDirectionFromString(EDataType eDataType, String initialValue) {
		CountDirection result = CountDirection.get(initialValue);
		if (result == null) throw new IllegalArgumentException(Messages.CalcFactoryImpl_Error_Invalid_Enumerator + initialValue + Messages.CalcFactoryImpl_Error_Invalid_Enumerator2 + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$ //$NON-NLS-1$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCountDirectionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CountDirection createCountDirectionObjectFromString(EDataType eDataType, String initialValue) {
		return createCountDirectionFromString(CalcPackage.Literals.COUNT_DIRECTION, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCountDirectionObjectToString(EDataType eDataType, Object instanceValue) {
		return convertCountDirectionToString(CalcPackage.Literals.COUNT_DIRECTION, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CalcPackage getCalcPackage() {
		return (CalcPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CalcPackage getPackage() {
		return CalcPackage.eINSTANCE;
	}

} //CalcFactoryImpl
