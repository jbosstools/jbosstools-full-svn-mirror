/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.persistence12.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Persistence12FactoryImpl extends EFactoryImpl implements Persistence12Factory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Persistence12Factory init() {
		try {
			Persistence12Factory thePersistence12Factory = (Persistence12Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/persistence-1.2.xsd");  //$NON-NLS-1$
			if (thePersistence12Factory != null) {
				return thePersistence12Factory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Persistence12FactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Persistence12FactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Persistence12Package.DECODER_PARAMETER: return createDecoderParameter();
			case Persistence12Package.DELETER: return createDeleter();
			case Persistence12Package.PERSISTENCE12_DOCUMENT_ROOT: return createPersistence12DocumentRoot();
			case Persistence12Package.EXPRESSION_PARAMETER: return createExpressionParameter();
			case Persistence12Package.FLUSHER: return createFlusher();
			case Persistence12Package.INSERTER: return createInserter();
			case Persistence12Package.LOCATOR: return createLocator();
			case Persistence12Package.PARAMETERS: return createParameters();
			case Persistence12Package.UPDATER: return createUpdater();
			case Persistence12Package.VALUE_PARAMETER: return createValueParameter();
			case Persistence12Package.WIRING_PARAMETER: return createWiringParameter();
			default:
				throw new IllegalArgumentException(Messages.Persistence12FactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.Persistence12FactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case Persistence12Package.ON_NO_RESULT:
				return createOnNoResultFromString(eDataType, initialValue);
			case Persistence12Package.PARAMETER_TYPE:
				return createParameterTypeFromString(eDataType, initialValue);
			case Persistence12Package.ON_NO_RESULT_OBJECT:
				return createOnNoResultObjectFromString(eDataType, initialValue);
			case Persistence12Package.PARAMETER_TYPE_OBJECT:
				return createParameterTypeObjectFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(Messages.Persistence12FactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.Persistence12FactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case Persistence12Package.ON_NO_RESULT:
				return convertOnNoResultToString(eDataType, instanceValue);
			case Persistence12Package.PARAMETER_TYPE:
				return convertParameterTypeToString(eDataType, instanceValue);
			case Persistence12Package.ON_NO_RESULT_OBJECT:
				return convertOnNoResultObjectToString(eDataType, instanceValue);
			case Persistence12Package.PARAMETER_TYPE_OBJECT:
				return convertParameterTypeObjectToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(Messages.Persistence12FactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.Persistence12FactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DecoderParameter createDecoderParameter() {
		DecoderParameterImpl decoderParameter = new DecoderParameterImpl();
		return decoderParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Deleter createDeleter() {
		DeleterImpl deleter = new DeleterImpl();
		return deleter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Persistence12DocumentRoot createPersistence12DocumentRoot() {
		Persistence12DocumentRootImpl persistence12DocumentRoot = new Persistence12DocumentRootImpl();
		return persistence12DocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExpressionParameter createExpressionParameter() {
		ExpressionParameterImpl expressionParameter = new ExpressionParameterImpl();
		return expressionParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Flusher createFlusher() {
		FlusherImpl flusher = new FlusherImpl();
		return flusher;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Inserter createInserter() {
		InserterImpl inserter = new InserterImpl();
		return inserter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Locator createLocator() {
		LocatorImpl locator = new LocatorImpl();
		return locator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Parameters createParameters() {
		ParametersImpl parameters = new ParametersImpl();
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Updater createUpdater() {
		UpdaterImpl updater = new UpdaterImpl();
		return updater;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueParameter createValueParameter() {
		ValueParameterImpl valueParameter = new ValueParameterImpl();
		return valueParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WiringParameter createWiringParameter() {
		WiringParameterImpl wiringParameter = new WiringParameterImpl();
		return wiringParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OnNoResult createOnNoResultFromString(EDataType eDataType, String initialValue) {
		OnNoResult result = OnNoResult.get(initialValue);
		if (result == null) throw new IllegalArgumentException(Messages.Persistence12FactoryImpl_Error_Value + initialValue + Messages.Persistence12FactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOnNoResultToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterType createParameterTypeFromString(EDataType eDataType, String initialValue) {
		ParameterType result = ParameterType.get(initialValue);
		if (result == null) throw new IllegalArgumentException(Messages.Persistence12FactoryImpl_Error_Value + initialValue + Messages.Persistence12FactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + "'"); //$NON-NLS-3$ //$NON-NLS-1$
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertParameterTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OnNoResult createOnNoResultObjectFromString(EDataType eDataType, String initialValue) {
		return createOnNoResultFromString(Persistence12Package.Literals.ON_NO_RESULT, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOnNoResultObjectToString(EDataType eDataType, Object instanceValue) {
		return convertOnNoResultToString(Persistence12Package.Literals.ON_NO_RESULT, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterType createParameterTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createParameterTypeFromString(Persistence12Package.Literals.PARAMETER_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertParameterTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertParameterTypeToString(Persistence12Package.Literals.PARAMETER_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Persistence12Package getPersistence12Package() {
		return (Persistence12Package)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static Persistence12Package getPackage() {
		return Persistence12Package.eINSTANCE;
	}

} //Persistence12FactoryImpl
