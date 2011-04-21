/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.javabean.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JavabeanFactoryImpl extends EFactoryImpl implements JavabeanFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static JavabeanFactory init() {
		try {
			JavabeanFactory theJavabeanFactory = (JavabeanFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/javabean-1.1.xsd");  //$NON-NLS-1$
			if (theJavabeanFactory != null) {
				return theJavabeanFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new JavabeanFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavabeanFactoryImpl() {
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
			case JavabeanPackage.BINDINGS_TYPE: return createBindingsType();
			case JavabeanPackage.DECODE_PARAM_TYPE: return createDecodeParamType();
			case JavabeanPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case JavabeanPackage.EXPRESSION_TYPE: return createExpressionType();
			case JavabeanPackage.VALUE_TYPE: return createValueType();
			case JavabeanPackage.WIRING_TYPE: return createWiringType();
			default:
				throw new IllegalArgumentException(Messages.JavabeanFactoryImpl_Error_Class_not_valid + eClass.getName() + Messages.JavabeanFactoryImpl_Error_Not_Valid_Identifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BindingsType createBindingsType() {
		BindingsTypeImpl bindingsType = new BindingsTypeImpl();
		return bindingsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DecodeParamType createDecodeParamType() {
		DecodeParamTypeImpl decodeParamType = new DecodeParamTypeImpl();
		return decodeParamType;
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
	public ExpressionType createExpressionType() {
		ExpressionTypeImpl expressionType = new ExpressionTypeImpl();
		return expressionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ValueType createValueType() {
		ValueTypeImpl valueType = new ValueTypeImpl();
		return valueType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WiringType createWiringType() {
		WiringTypeImpl wiringType = new WiringTypeImpl();
		return wiringType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavabeanPackage getJavabeanPackage() {
		return (JavabeanPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static JavabeanPackage getPackage() {
		return JavabeanPackage.eINSTANCE;
	}

} //JavabeanFactoryImpl
