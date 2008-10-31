/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.graphical;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.jboss.tools.smooks.graphical.GraphicalFactory
 * @model kind="package"
 * @generated
 */
public interface GraphicalPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "graphical";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.jboss.org/tools/smooks/graphicalInformation";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GraphicalPackage eINSTANCE = org.jboss.tools.smooks.graphical.impl.GraphicalPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.graphical.impl.GraphInformationsImpl <em>Graph Informations</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.graphical.impl.GraphInformationsImpl
	 * @see org.jboss.tools.smooks.graphical.impl.GraphicalPackageImpl#getGraphInformations()
	 * @generated
	 */
	int GRAPH_INFORMATIONS = 0;

	/**
	 * The feature id for the '<em><b>Mapping Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_INFORMATIONS__MAPPING_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Params</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_INFORMATIONS__PARAMS = 1;

	/**
	 * The number of structural features of the '<em>Graph Informations</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_INFORMATIONS_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.graphical.impl.MappingDataTypeImpl <em>Mapping Data Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.graphical.impl.MappingDataTypeImpl
	 * @see org.jboss.tools.smooks.graphical.impl.GraphicalPackageImpl#getMappingDataType()
	 * @generated
	 */
	int MAPPING_DATA_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Target Type ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_DATA_TYPE__TARGET_TYPE_ID = 0;

	/**
	 * The feature id for the '<em><b>Source Type ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_DATA_TYPE__SOURCE_TYPE_ID = 1;

	/**
	 * The number of structural features of the '<em>Mapping Data Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAPPING_DATA_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.graphical.impl.ParamsImpl <em>Params</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.graphical.impl.ParamsImpl
	 * @see org.jboss.tools.smooks.graphical.impl.GraphicalPackageImpl#getParams()
	 * @generated
	 */
	int PARAMS = 2;

	/**
	 * The feature id for the '<em><b>Param</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMS__PARAM = 0;

	/**
	 * The number of structural features of the '<em>Params</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMS_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.graphical.impl.ParamImpl <em>Param</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.graphical.impl.ParamImpl
	 * @see org.jboss.tools.smooks.graphical.impl.GraphicalPackageImpl#getParam()
	 * @generated
	 */
	int PARAM = 3;

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
	 * The number of structural features of the '<em>Param</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_FEATURE_COUNT = 2;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.graphical.GraphInformations <em>Graph Informations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Graph Informations</em>'.
	 * @see org.jboss.tools.smooks.graphical.GraphInformations
	 * @generated
	 */
	EClass getGraphInformations();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.graphical.GraphInformations#getMappingType <em>Mapping Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Mapping Type</em>'.
	 * @see org.jboss.tools.smooks.graphical.GraphInformations#getMappingType()
	 * @see #getGraphInformations()
	 * @generated
	 */
	EReference getGraphInformations_MappingType();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.graphical.GraphInformations#getParams <em>Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Params</em>'.
	 * @see org.jboss.tools.smooks.graphical.GraphInformations#getParams()
	 * @see #getGraphInformations()
	 * @generated
	 */
	EReference getGraphInformations_Params();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.graphical.MappingDataType <em>Mapping Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mapping Data Type</em>'.
	 * @see org.jboss.tools.smooks.graphical.MappingDataType
	 * @generated
	 */
	EClass getMappingDataType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.graphical.MappingDataType#getTargetTypeID <em>Target Type ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target Type ID</em>'.
	 * @see org.jboss.tools.smooks.graphical.MappingDataType#getTargetTypeID()
	 * @see #getMappingDataType()
	 * @generated
	 */
	EAttribute getMappingDataType_TargetTypeID();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.graphical.MappingDataType#getSourceTypeID <em>Source Type ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Type ID</em>'.
	 * @see org.jboss.tools.smooks.graphical.MappingDataType#getSourceTypeID()
	 * @see #getMappingDataType()
	 * @generated
	 */
	EAttribute getMappingDataType_SourceTypeID();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.graphical.Params <em>Params</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Params</em>'.
	 * @see org.jboss.tools.smooks.graphical.Params
	 * @generated
	 */
	EClass getParams();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.graphical.Params#getParam <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Param</em>'.
	 * @see org.jboss.tools.smooks.graphical.Params#getParam()
	 * @see #getParams()
	 * @generated
	 */
	EReference getParams_Param();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.graphical.Param <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Param</em>'.
	 * @see org.jboss.tools.smooks.graphical.Param
	 * @generated
	 */
	EClass getParam();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.graphical.Param#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.graphical.Param#getName()
	 * @see #getParam()
	 * @generated
	 */
	EAttribute getParam_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.graphical.Param#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.graphical.Param#getValue()
	 * @see #getParam()
	 * @generated
	 */
	EAttribute getParam_Value();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GraphicalFactory getGraphicalFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.graphical.impl.GraphInformationsImpl <em>Graph Informations</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.graphical.impl.GraphInformationsImpl
		 * @see org.jboss.tools.smooks.graphical.impl.GraphicalPackageImpl#getGraphInformations()
		 * @generated
		 */
		EClass GRAPH_INFORMATIONS = eINSTANCE.getGraphInformations();

		/**
		 * The meta object literal for the '<em><b>Mapping Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GRAPH_INFORMATIONS__MAPPING_TYPE = eINSTANCE.getGraphInformations_MappingType();

		/**
		 * The meta object literal for the '<em><b>Params</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GRAPH_INFORMATIONS__PARAMS = eINSTANCE.getGraphInformations_Params();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.graphical.impl.MappingDataTypeImpl <em>Mapping Data Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.graphical.impl.MappingDataTypeImpl
		 * @see org.jboss.tools.smooks.graphical.impl.GraphicalPackageImpl#getMappingDataType()
		 * @generated
		 */
		EClass MAPPING_DATA_TYPE = eINSTANCE.getMappingDataType();

		/**
		 * The meta object literal for the '<em><b>Target Type ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAPPING_DATA_TYPE__TARGET_TYPE_ID = eINSTANCE.getMappingDataType_TargetTypeID();

		/**
		 * The meta object literal for the '<em><b>Source Type ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MAPPING_DATA_TYPE__SOURCE_TYPE_ID = eINSTANCE.getMappingDataType_SourceTypeID();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.graphical.impl.ParamsImpl <em>Params</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.graphical.impl.ParamsImpl
		 * @see org.jboss.tools.smooks.graphical.impl.GraphicalPackageImpl#getParams()
		 * @generated
		 */
		EClass PARAMS = eINSTANCE.getParams();

		/**
		 * The meta object literal for the '<em><b>Param</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PARAMS__PARAM = eINSTANCE.getParams_Param();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.graphical.impl.ParamImpl <em>Param</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.graphical.impl.ParamImpl
		 * @see org.jboss.tools.smooks.graphical.impl.GraphicalPackageImpl#getParam()
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

	}

} //GraphicalPackage
