/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.core;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.core.ICoreFactory
 * @model kind="package"
 * @generated
 */
public interface ICorePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "core";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/smooks-core/core/1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "smooks.core";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ICorePackage eINSTANCE = org.jboss.tools.smooks.model.core.CorePackage.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.core.Param <em>Param</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.core.Param
	 * @see org.jboss.tools.smooks.model.core.CorePackage#getParam()
	 * @generated
	 */
	int PARAM = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM__VALUE = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM__TYPE = 2;

	/**
	 * The number of structural features of the '<em>Param</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.core.Params <em>Params</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.core.Params
	 * @see org.jboss.tools.smooks.model.core.CorePackage#getParams()
	 * @generated
	 */
	int PARAMS = 1;

	/**
	 * The feature id for the '<em><b>Params</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMS__PARAMS = 0;

	/**
	 * The number of structural features of the '<em>Params</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMS_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.core.GlobalParams <em>Global Params</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.core.GlobalParams
	 * @see org.jboss.tools.smooks.model.core.CorePackage#getGlobalParams()
	 * @generated
	 */
	int GLOBAL_PARAMS = 2;

	/**
	 * The feature id for the '<em><b>Params</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GLOBAL_PARAMS__PARAMS = PARAMS__PARAMS;

	/**
	 * The number of structural features of the '<em>Global Params</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GLOBAL_PARAMS_FEATURE_COUNT = PARAMS_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.core.IComponent <em>Component</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.core.IComponent
	 * @see org.jboss.tools.smooks.model.core.CorePackage#getComponent()
	 * @generated
	 */
	int COMPONENT = 3;

	/**
	 * The number of structural features of the '<em>Component</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '<em>Stream Filter Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.milyn.StreamFilterType
	 * @see org.jboss.tools.smooks.model.core.CorePackage#getStreamFilterType()
	 * @generated
	 */
	int STREAM_FILTER_TYPE = 4;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.core.IParam <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Param</em>'.
	 * @see org.jboss.tools.smooks.model.core.IParam
	 * @generated
	 */
	EClass getParam();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.core.IParam#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.core.IParam#getName()
	 * @see #getParam()
	 * @generated
	 */
	EAttribute getParam_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.core.IParam#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.core.IParam#getValue()
	 * @see #getParam()
	 * @generated
	 */
	EAttribute getParam_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.core.IParam#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.jboss.tools.smooks.model.core.IParam#getType()
	 * @see #getParam()
	 * @generated
	 */
	EAttribute getParam_Type();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.core.IParams <em>Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Params</em>'.
	 * @see org.jboss.tools.smooks.model.core.IParams
	 * @generated
	 */
	EClass getParams();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.core.IParams#getParams <em>Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Params</em>'.
	 * @see org.jboss.tools.smooks.model.core.IParams#getParams()
	 * @see #getParams()
	 * @generated
	 */
	EReference getParams_Params();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.core.IGlobalParams <em>Global Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Global Params</em>'.
	 * @see org.jboss.tools.smooks.model.core.IGlobalParams
	 * @generated
	 */
	EClass getGlobalParams();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.core.IComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component</em>'.
	 * @see org.jboss.tools.smooks.model.core.IComponent
	 * @generated
	 */
	EClass getComponent();

	/**
	 * Returns the meta object for data type '{@link org.milyn.StreamFilterType <em>Stream Filter Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Stream Filter Type</em>'.
	 * @see org.milyn.StreamFilterType
	 * @model instanceClass="org.milyn.StreamFilterType"
	 * @generated
	 */
	EDataType getStreamFilterType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ICoreFactory getCoreFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.core.Param <em>Param</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.core.Param
		 * @see org.jboss.tools.smooks.model.core.CorePackage#getParam()
		 * @generated
		 */
		EClass PARAM = eINSTANCE.getParam();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM__NAME = eINSTANCE.getParam_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM__VALUE = eINSTANCE.getParam_Value();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM__TYPE = eINSTANCE.getParam_Type();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.core.Params <em>Params</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.core.Params
		 * @see org.jboss.tools.smooks.model.core.CorePackage#getParams()
		 * @generated
		 */
		EClass PARAMS = eINSTANCE.getParams();

		/**
		 * The meta object literal for the '<em><b>Params</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMS__PARAMS = eINSTANCE.getParams_Params();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.core.GlobalParams <em>Global Params</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.core.GlobalParams
		 * @see org.jboss.tools.smooks.model.core.CorePackage#getGlobalParams()
		 * @generated
		 */
		EClass GLOBAL_PARAMS = eINSTANCE.getGlobalParams();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.core.IComponent <em>Component</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.core.IComponent
		 * @see org.jboss.tools.smooks.model.core.CorePackage#getComponent()
		 * @generated
		 */
		EClass COMPONENT = eINSTANCE.getComponent();

		/**
		 * The meta object literal for the '<em>Stream Filter Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.milyn.StreamFilterType
		 * @see org.jboss.tools.smooks.model.core.CorePackage#getStreamFilterType()
		 * @generated
		 */
		EDataType STREAM_FILTER_TYPE = eINSTANCE.getStreamFilterType();

	}

} //ICorePackage
