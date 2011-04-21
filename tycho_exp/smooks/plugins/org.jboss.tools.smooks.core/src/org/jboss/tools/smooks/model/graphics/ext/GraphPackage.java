/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;


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
 * <!-- begin-model-doc -->
 * 
 * 			Smooks Graphics Editor Extention File Schema
 * 		
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.graphics.ext.GraphFactory
 * @model kind="package"
 * @generated
 */
public interface GraphPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "graph";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.jboss.org/jbosstools/smooks/smooks-graphics-ext.xsd";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "graph";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GraphPackage eINSTANCE = org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.ConnectionTypeImpl <em>Connection Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.ConnectionTypeImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getConnectionType()
	 * @generated
	 */
	int CONNECTION_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_TYPE__SOURCE = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_TYPE__TARGET = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_TYPE__ID = 2;

	/**
	 * The number of structural features of the '<em>Connection Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONNECTION_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl <em>Smooks Graph Extension Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Connection</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__CONNECTION = 3;

	/**
	 * The feature id for the '<em><b>Figure</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__FIGURE = 4;

	/**
	 * The feature id for the '<em><b>Graph</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__GRAPH = 5;

	/**
	 * The feature id for the '<em><b>Input</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__INPUT = 6;

	/**
	 * The feature id for the '<em><b>Param</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__PARAM = 7;

	/**
	 * The feature id for the '<em><b>Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__PROCESS = 8;

	/**
	 * The feature id for the '<em><b>Processes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__PROCESSES = 9;

	/**
	 * The feature id for the '<em><b>Smooks Graphics Ext</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT = 10;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__SOURCE = 11;

	/**
	 * The feature id for the '<em><b>Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__TARGET = 12;

	/**
	 * The feature id for the '<em><b>Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__TASK = 13;

	/**
	 * The number of structural features of the '<em>Smooks Graph Extension Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT_FEATURE_COUNT = 14;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl <em>Figure Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getFigureType()
	 * @generated
	 */
	int FIGURE_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Height</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIGURE_TYPE__HEIGHT = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIGURE_TYPE__ID = 1;

	/**
	 * The feature id for the '<em><b>Parent Figure Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIGURE_TYPE__PARENT_FIGURE_ID = 2;

	/**
	 * The feature id for the '<em><b>Width</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIGURE_TYPE__WIDTH = 3;

	/**
	 * The feature id for the '<em><b>X</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIGURE_TYPE__X = 4;

	/**
	 * The feature id for the '<em><b>Y</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIGURE_TYPE__Y = 5;

	/**
	 * The number of structural features of the '<em>Figure Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FIGURE_TYPE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.GraphTypeImpl <em>Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphTypeImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getGraphType()
	 * @generated
	 */
	int GRAPH_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Figure</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_TYPE__FIGURE = 0;

	/**
	 * The feature id for the '<em><b>Connection</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_TYPE__CONNECTION = 1;

	/**
	 * The number of structural features of the '<em>Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.InputTypeImpl <em>Input Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.InputTypeImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getInputType()
	 * @generated
	 */
	int INPUT_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Param</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_TYPE__PARAM = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_TYPE__TYPE = 1;

	/**
	 * The number of structural features of the '<em>Input Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_TYPE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.ParamTypeImpl <em>Param Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.ParamTypeImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getParamType()
	 * @generated
	 */
	int PARAM_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE__NAME = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE__TYPE = 2;

	/**
	 * The number of structural features of the '<em>Param Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAM_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.ProcessesTypeImpl <em>Processes Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.ProcessesTypeImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getProcessesType()
	 * @generated
	 */
	int PROCESSES_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESSES_TYPE__PROCESS = 0;

	/**
	 * The number of structural features of the '<em>Processes Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESSES_TYPE_FEATURE_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.ProcessTypeImpl <em>Process Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.ProcessTypeImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getProcessType()
	 * @generated
	 */
	int PROCESS_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Task</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_TYPE__TASK = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_TYPE__ID = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_TYPE__NAME = 2;

	/**
	 * The number of structural features of the '<em>Process Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROCESS_TYPE_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl <em>Smooks Graphics Ext Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getSmooksGraphicsExtType()
	 * @generated
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE = 8;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__MIXED = SmooksPackage.ABSTRACT_RESOURCE_CONFIG__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__ANY = SmooksPackage.ABSTRACT_RESOURCE_CONFIG__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__ANY_ATTRIBUTE = SmooksPackage.ABSTRACT_RESOURCE_CONFIG__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Input</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__INPUT = SmooksPackage.ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Graph</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__GRAPH = SmooksPackage.ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Processes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES = SmooksPackage.ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Author</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__AUTHOR = SmooksPackage.ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Input Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE = SmooksPackage.ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__NAME = SmooksPackage.ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Output Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE = SmooksPackage.ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Platform Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE__PLATFORM_VERSION = SmooksPackage.ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Smooks Graphics Ext Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SMOOKS_GRAPHICS_EXT_TYPE_FEATURE_COUNT = SmooksPackage.ABSTRACT_RESOURCE_CONFIG_FEATURE_COUNT + 8;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.TaskTypeImpl <em>Task Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.TaskTypeImpl
	 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getTaskType()
	 * @generated
	 */
	int TASK_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Task</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_TYPE__TASK = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_TYPE__ID = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_TYPE__NAME = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_TYPE__TYPE = 3;

	/**
	 * The number of structural features of the '<em>Task Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TASK_TYPE_FEATURE_COUNT = 4;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.ConnectionType <em>Connection Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Connection Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ConnectionType
	 * @generated
	 */
	EClass getConnectionType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.ConnectionType#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ConnectionType#getSource()
	 * @see #getConnectionType()
	 * @generated
	 */
	EAttribute getConnectionType_Source();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.ConnectionType#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ConnectionType#getTarget()
	 * @see #getConnectionType()
	 * @generated
	 */
	EAttribute getConnectionType_Target();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.ConnectionType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ConnectionType#getId()
	 * @see #getConnectionType()
	 * @generated
	 */
	EAttribute getConnectionType_Id();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot <em>Smooks Graph Extension Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Smooks Graph Extension Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot
	 * @generated
	 */
	EClass getSmooksGraphExtensionDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getMixed()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EAttribute getSmooksGraphExtensionDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getXMLNSPrefixMap()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getXSISchemaLocation()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getConnection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Connection</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getConnection()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_Connection();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getFigure <em>Figure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Figure</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getFigure()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_Figure();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getGraph <em>Graph</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Graph</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getGraph()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_Graph();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Input</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getInput()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_Input();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getParam <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Param</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getParam()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_Param();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getProcess <em>Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Process</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getProcess()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_Process();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getProcesses <em>Processes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Processes</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getProcesses()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_Processes();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getSmooksGraphicsExt <em>Smooks Graphics Ext</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Smooks Graphics Ext</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getSmooksGraphicsExt()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_SmooksGraphicsExt();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getSource()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EAttribute getSmooksGraphExtensionDocumentRoot_Source();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Target</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getTarget()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EAttribute getSmooksGraphExtensionDocumentRoot_Target();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Task</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getTask()
	 * @see #getSmooksGraphExtensionDocumentRoot()
	 * @generated
	 */
	EReference getSmooksGraphExtensionDocumentRoot_Task();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.FigureType <em>Figure Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Figure Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.FigureType
	 * @generated
	 */
	EClass getFigureType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.FigureType#getHeight <em>Height</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Height</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.FigureType#getHeight()
	 * @see #getFigureType()
	 * @generated
	 */
	EAttribute getFigureType_Height();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.FigureType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.FigureType#getId()
	 * @see #getFigureType()
	 * @generated
	 */
	EAttribute getFigureType_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.FigureType#getParentFigureId <em>Parent Figure Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Parent Figure Id</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.FigureType#getParentFigureId()
	 * @see #getFigureType()
	 * @generated
	 */
	EAttribute getFigureType_ParentFigureId();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.FigureType#getWidth <em>Width</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Width</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.FigureType#getWidth()
	 * @see #getFigureType()
	 * @generated
	 */
	EAttribute getFigureType_Width();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.FigureType#getX <em>X</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>X</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.FigureType#getX()
	 * @see #getFigureType()
	 * @generated
	 */
	EAttribute getFigureType_X();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.FigureType#getY <em>Y</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Y</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.FigureType#getY()
	 * @see #getFigureType()
	 * @generated
	 */
	EAttribute getFigureType_Y();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.GraphType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphType
	 * @generated
	 */
	EClass getGraphType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.graphics.ext.GraphType#getFigure <em>Figure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Figure</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphType#getFigure()
	 * @see #getGraphType()
	 * @generated
	 */
	EReference getGraphType_Figure();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.graphics.ext.GraphType#getConnection <em>Connection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Connection</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphType#getConnection()
	 * @see #getGraphType()
	 * @generated
	 */
	EReference getGraphType_Connection();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.InputType <em>Input Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.InputType
	 * @generated
	 */
	EClass getInputType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.graphics.ext.InputType#getParam <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Param</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.InputType#getParam()
	 * @see #getInputType()
	 * @generated
	 */
	EReference getInputType_Param();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.InputType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.InputType#getType()
	 * @see #getInputType()
	 * @generated
	 */
	EAttribute getInputType_Type();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.ParamType <em>Param Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Param Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ParamType
	 * @generated
	 */
	EClass getParamType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.ParamType#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ParamType#getValue()
	 * @see #getParamType()
	 * @generated
	 */
	EAttribute getParamType_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.ParamType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ParamType#getName()
	 * @see #getParamType()
	 * @generated
	 */
	EAttribute getParamType_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.ParamType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ParamType#getType()
	 * @see #getParamType()
	 * @generated
	 */
	EAttribute getParamType_Type();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.ProcessesType <em>Processes Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Processes Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ProcessesType
	 * @generated
	 */
	EClass getProcessesType();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.ProcessesType#getProcess <em>Process</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Process</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ProcessesType#getProcess()
	 * @see #getProcessesType()
	 * @generated
	 */
	EReference getProcessesType_Process();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.ProcessType <em>Process Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Process Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ProcessType
	 * @generated
	 */
	EClass getProcessType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.graphics.ext.ProcessType#getTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Task</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ProcessType#getTask()
	 * @see #getProcessType()
	 * @generated
	 */
	EReference getProcessType_Task();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.ProcessType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ProcessType#getId()
	 * @see #getProcessType()
	 * @generated
	 */
	EAttribute getProcessType_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.ProcessType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.ProcessType#getName()
	 * @see #getProcessType()
	 * @generated
	 */
	EAttribute getProcessType_Name();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType <em>Smooks Graphics Ext Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Smooks Graphics Ext Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType
	 * @generated
	 */
	EClass getSmooksGraphicsExtType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Input</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInput()
	 * @see #getSmooksGraphicsExtType()
	 * @generated
	 */
	EReference getSmooksGraphicsExtType_Input();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getGraph <em>Graph</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Graph</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getGraph()
	 * @see #getSmooksGraphicsExtType()
	 * @generated
	 */
	EReference getSmooksGraphicsExtType_Graph();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getProcesses <em>Processes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Processes</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getProcesses()
	 * @see #getSmooksGraphicsExtType()
	 * @generated
	 */
	EReference getSmooksGraphicsExtType_Processes();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getAuthor <em>Author</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Author</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getAuthor()
	 * @see #getSmooksGraphicsExtType()
	 * @generated
	 */
	EAttribute getSmooksGraphicsExtType_Author();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInputType <em>Input Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Input Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInputType()
	 * @see #getSmooksGraphicsExtType()
	 * @generated
	 */
	EAttribute getSmooksGraphicsExtType_InputType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getName()
	 * @see #getSmooksGraphicsExtType()
	 * @generated
	 */
	EAttribute getSmooksGraphicsExtType_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getOutputType <em>Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getOutputType()
	 * @see #getSmooksGraphicsExtType()
	 * @generated
	 */
	EAttribute getSmooksGraphicsExtType_OutputType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getPlatformVersion <em>Platform Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Platform Version</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getPlatformVersion()
	 * @see #getSmooksGraphicsExtType()
	 * @generated
	 */
	EAttribute getSmooksGraphicsExtType_PlatformVersion();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.graphics.ext.TaskType <em>Task Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Task Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.TaskType
	 * @generated
	 */
	EClass getTaskType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.graphics.ext.TaskType#getTask <em>Task</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Task</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.TaskType#getTask()
	 * @see #getTaskType()
	 * @generated
	 */
	EReference getTaskType_Task();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.TaskType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.TaskType#getId()
	 * @see #getTaskType()
	 * @generated
	 */
	EAttribute getTaskType_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.TaskType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.TaskType#getName()
	 * @see #getTaskType()
	 * @generated
	 */
	EAttribute getTaskType_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.graphics.ext.TaskType#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.jboss.tools.smooks.model.graphics.ext.TaskType#getType()
	 * @see #getTaskType()
	 * @generated
	 */
	EAttribute getTaskType_Type();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GraphFactory getGraphFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.ConnectionTypeImpl <em>Connection Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.ConnectionTypeImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getConnectionType()
		 * @generated
		 */
		EClass CONNECTION_TYPE = eINSTANCE.getConnectionType();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONNECTION_TYPE__SOURCE = eINSTANCE.getConnectionType_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONNECTION_TYPE__TARGET = eINSTANCE.getConnectionType_Target();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONNECTION_TYPE__ID = eINSTANCE.getConnectionType_Id();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl <em>Smooks Graph Extension Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphExtensionDocumentRootImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getSmooksGraphExtensionDocumentRoot()
		 * @generated
		 */
		EClass SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT = eINSTANCE.getSmooksGraphExtensionDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__MIXED = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getSmooksGraphExtensionDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getSmooksGraphExtensionDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Connection</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__CONNECTION = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Connection();

		/**
		 * The meta object literal for the '<em><b>Figure</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__FIGURE = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Figure();

		/**
		 * The meta object literal for the '<em><b>Graph</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__GRAPH = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Graph();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__INPUT = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Input();

		/**
		 * The meta object literal for the '<em><b>Param</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__PARAM = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Param();

		/**
		 * The meta object literal for the '<em><b>Process</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__PROCESS = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Process();

		/**
		 * The meta object literal for the '<em><b>Processes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__PROCESSES = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Processes();

		/**
		 * The meta object literal for the '<em><b>Smooks Graphics Ext</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT = eINSTANCE.getSmooksGraphExtensionDocumentRoot_SmooksGraphicsExt();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__SOURCE = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__TARGET = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Target();

		/**
		 * The meta object literal for the '<em><b>Task</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT__TASK = eINSTANCE.getSmooksGraphExtensionDocumentRoot_Task();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl <em>Figure Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.FigureTypeImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getFigureType()
		 * @generated
		 */
		EClass FIGURE_TYPE = eINSTANCE.getFigureType();

		/**
		 * The meta object literal for the '<em><b>Height</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIGURE_TYPE__HEIGHT = eINSTANCE.getFigureType_Height();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIGURE_TYPE__ID = eINSTANCE.getFigureType_Id();

		/**
		 * The meta object literal for the '<em><b>Parent Figure Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIGURE_TYPE__PARENT_FIGURE_ID = eINSTANCE.getFigureType_ParentFigureId();

		/**
		 * The meta object literal for the '<em><b>Width</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIGURE_TYPE__WIDTH = eINSTANCE.getFigureType_Width();

		/**
		 * The meta object literal for the '<em><b>X</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIGURE_TYPE__X = eINSTANCE.getFigureType_X();

		/**
		 * The meta object literal for the '<em><b>Y</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FIGURE_TYPE__Y = eINSTANCE.getFigureType_Y();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.GraphTypeImpl <em>Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphTypeImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getGraphType()
		 * @generated
		 */
		EClass GRAPH_TYPE = eINSTANCE.getGraphType();

		/**
		 * The meta object literal for the '<em><b>Figure</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GRAPH_TYPE__FIGURE = eINSTANCE.getGraphType_Figure();

		/**
		 * The meta object literal for the '<em><b>Connection</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GRAPH_TYPE__CONNECTION = eINSTANCE.getGraphType_Connection();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.InputTypeImpl <em>Input Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.InputTypeImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getInputType()
		 * @generated
		 */
		EClass INPUT_TYPE = eINSTANCE.getInputType();

		/**
		 * The meta object literal for the '<em><b>Param</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT_TYPE__PARAM = eINSTANCE.getInputType_Param();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INPUT_TYPE__TYPE = eINSTANCE.getInputType_Type();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.ParamTypeImpl <em>Param Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.ParamTypeImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getParamType()
		 * @generated
		 */
		EClass PARAM_TYPE = eINSTANCE.getParamType();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM_TYPE__VALUE = eINSTANCE.getParamType_Value();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM_TYPE__NAME = eINSTANCE.getParamType_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAM_TYPE__TYPE = eINSTANCE.getParamType_Type();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.ProcessesTypeImpl <em>Processes Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.ProcessesTypeImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getProcessesType()
		 * @generated
		 */
		EClass PROCESSES_TYPE = eINSTANCE.getProcessesType();

		/**
		 * The meta object literal for the '<em><b>Process</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROCESSES_TYPE__PROCESS = eINSTANCE.getProcessesType_Process();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.ProcessTypeImpl <em>Process Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.ProcessTypeImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getProcessType()
		 * @generated
		 */
		EClass PROCESS_TYPE = eINSTANCE.getProcessType();

		/**
		 * The meta object literal for the '<em><b>Task</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROCESS_TYPE__TASK = eINSTANCE.getProcessType_Task();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_TYPE__ID = eINSTANCE.getProcessType_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROCESS_TYPE__NAME = eINSTANCE.getProcessType_Name();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl <em>Smooks Graphics Ext Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.SmooksGraphicsExtTypeImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getSmooksGraphicsExtType()
		 * @generated
		 */
		EClass SMOOKS_GRAPHICS_EXT_TYPE = eINSTANCE.getSmooksGraphicsExtType();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPHICS_EXT_TYPE__INPUT = eINSTANCE.getSmooksGraphicsExtType_Input();

		/**
		 * The meta object literal for the '<em><b>Graph</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPHICS_EXT_TYPE__GRAPH = eINSTANCE.getSmooksGraphicsExtType_Graph();

		/**
		 * The meta object literal for the '<em><b>Processes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SMOOKS_GRAPHICS_EXT_TYPE__PROCESSES = eINSTANCE.getSmooksGraphicsExtType_Processes();

		/**
		 * The meta object literal for the '<em><b>Author</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_GRAPHICS_EXT_TYPE__AUTHOR = eINSTANCE.getSmooksGraphicsExtType_Author();

		/**
		 * The meta object literal for the '<em><b>Input Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE = eINSTANCE.getSmooksGraphicsExtType_InputType();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_GRAPHICS_EXT_TYPE__NAME = eINSTANCE.getSmooksGraphicsExtType_Name();

		/**
		 * The meta object literal for the '<em><b>Output Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE = eINSTANCE.getSmooksGraphicsExtType_OutputType();

		/**
		 * The meta object literal for the '<em><b>Platform Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SMOOKS_GRAPHICS_EXT_TYPE__PLATFORM_VERSION = eINSTANCE.getSmooksGraphicsExtType_PlatformVersion();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.graphics.ext.impl.TaskTypeImpl <em>Task Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.TaskTypeImpl
		 * @see org.jboss.tools.smooks.model.graphics.ext.impl.GraphPackageImpl#getTaskType()
		 * @generated
		 */
		EClass TASK_TYPE = eINSTANCE.getTaskType();

		/**
		 * The meta object literal for the '<em><b>Task</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TASK_TYPE__TASK = eINSTANCE.getTaskType_Task();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_TYPE__ID = eINSTANCE.getTaskType_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_TYPE__NAME = eINSTANCE.getTaskType_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TASK_TYPE__TYPE = eINSTANCE.getTaskType_Type();

	}

} //GraphPackage
