/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.validation10.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.validation10.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Validation10FactoryImpl extends EFactoryImpl implements Validation10Factory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Validation10Factory init() {
		try {
			Validation10Factory theValidation10Factory = (Validation10Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/validation-1.0.xsd");  //$NON-NLS-1$
			if (theValidation10Factory != null) {
				return theValidation10Factory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Validation10FactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Validation10FactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Validation10Package.VALIDATION10_DOCUMENT_ROOT: return createValidation10DocumentRoot();
			case Validation10Package.RULE_TYPE: return createRuleType();
			default:
				throw new IllegalArgumentException(Messages.Validation10FactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.Validation10FactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case Validation10Package.ON_FAIL:
				return createOnFailFromString(eDataType, initialValue);
			case Validation10Package.ON_FAIL_OBJECT:
				return createOnFailObjectFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(Messages.Validation10FactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.Validation10FactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case Validation10Package.ON_FAIL:
				return convertOnFailToString(eDataType, instanceValue);
			case Validation10Package.ON_FAIL_OBJECT:
				return convertOnFailObjectToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(Messages.Validation10FactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.Validation10FactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Validation10DocumentRoot createValidation10DocumentRoot() {
		Validation10DocumentRootImpl validation10DocumentRoot = new Validation10DocumentRootImpl();
		return validation10DocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuleType createRuleType() {
		RuleTypeImpl ruleType = new RuleTypeImpl();
		return ruleType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OnFail createOnFailFromString(EDataType eDataType, String initialValue) {
		OnFail result = OnFail.get(initialValue);
		if (result == null) throw new IllegalArgumentException(Messages.Validation10FactoryImpl_Error_Value_Not_Valid + initialValue + Messages.Validation10FactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$ //$NON-NLS-1$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOnFailToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OnFail createOnFailObjectFromString(EDataType eDataType, String initialValue) {
		return createOnFailFromString(Validation10Package.Literals.ON_FAIL, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOnFailObjectToString(EDataType eDataType, Object instanceValue) {
		return convertOnFailToString(Validation10Package.Literals.ON_FAIL, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Validation10Package getValidation10Package() {
		return (Validation10Package)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static Validation10Package getPackage() {
		return Validation10Package.eINSTANCE;
	}

} //Validation10FactoryImpl
