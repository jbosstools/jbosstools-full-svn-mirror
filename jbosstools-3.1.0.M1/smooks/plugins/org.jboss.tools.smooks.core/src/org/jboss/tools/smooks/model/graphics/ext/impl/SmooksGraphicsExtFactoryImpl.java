/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.jboss.tools.smooks.model.graphics.ext.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SmooksGraphicsExtFactoryImpl extends EFactoryImpl implements SmooksGraphicsExtFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SmooksGraphicsExtFactory init() {
		try {
			SmooksGraphicsExtFactory theSmooksGraphicsExtFactory = (SmooksGraphicsExtFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.jboss.org/jbosstools/smooks/smooks-graphics-ext.xsd"); 
			if (theSmooksGraphicsExtFactory != null) {
				return theSmooksGraphicsExtFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SmooksGraphicsExtFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksGraphicsExtFactoryImpl() {
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
			case SmooksGraphicsExtPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case SmooksGraphicsExtPackage.INPUT_TYPE: return createInputType();
			case SmooksGraphicsExtPackage.PARAM_TYPE: return createParamType();
			case SmooksGraphicsExtPackage.SMOOKS_GRAPHICS_EXT_TYPE: return createSmooksGraphicsExtType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
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
	public InputType createInputType() {
		InputTypeImpl inputType = new InputTypeImpl();
		return inputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParamType createParamType() {
		ParamTypeImpl paramType = new ParamTypeImpl();
		return paramType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksGraphicsExtType createSmooksGraphicsExtType() {
		SmooksGraphicsExtTypeImpl smooksGraphicsExtType = new SmooksGraphicsExtTypeImpl();
		return smooksGraphicsExtType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksGraphicsExtPackage getSmooksGraphicsExtPackage() {
		return (SmooksGraphicsExtPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SmooksGraphicsExtPackage getPackage() {
		return SmooksGraphicsExtPackage.eINSTANCE;
	}

} //SmooksGraphicsExtFactoryImpl
