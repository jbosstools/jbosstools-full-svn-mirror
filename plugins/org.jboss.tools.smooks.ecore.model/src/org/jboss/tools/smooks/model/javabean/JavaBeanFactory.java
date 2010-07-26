/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JavaBeanFactory extends EFactoryImpl implements IJavaBeanFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static IJavaBeanFactory init() {
		try {
			IJavaBeanFactory theJavaBeanFactory = (IJavaBeanFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/smooks-ui/javabean/1.0.0"); 
			if (theJavaBeanFactory != null) {
				return theJavaBeanFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new JavaBeanFactory();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaBeanFactory() {
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
			case IJavaBeanPackage.DECODE_PARAM: return createDecodeParam();
			case IJavaBeanPackage.WIRING: return createWiring();
			case IJavaBeanPackage.EXPRESSION: return createExpression();
			case IJavaBeanPackage.VALUE: return createValue();
			case IJavaBeanPackage.BEAN: return createBean();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IDecodeParam createDecodeParam() {
		DecodeParam decodeParam = new DecodeParam();
		return decodeParam;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IWiring createWiring() {
		Wiring wiring = new Wiring();
		return wiring;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IExpression createExpression() {
		Expression expression = new Expression();
		return expression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IValue createValue() {
		Value value = new Value();
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IBean createBean() {
		Bean bean = new Bean();
		return bean;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IJavaBeanPackage getJavaBeanPackage() {
		return (IJavaBeanPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static IJavaBeanPackage getPackage() {
		return IJavaBeanPackage.eINSTANCE;
	}

} //JavaBeanFactory
