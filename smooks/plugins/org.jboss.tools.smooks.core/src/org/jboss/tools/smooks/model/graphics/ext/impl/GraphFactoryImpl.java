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
public class GraphFactoryImpl extends EFactoryImpl implements GraphFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static GraphFactory init() {
		try {
			GraphFactory theGraphFactory = (GraphFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.jboss.org/jbosstools/smooks/smooks-graphics-ext.xsd"); 
			if (theGraphFactory != null) {
				return theGraphFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new GraphFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GraphFactoryImpl() {
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
			case GraphPackage.CONNECTION_TYPE: return createConnectionType();
			case GraphPackage.SMOOKS_GRAPH_EXTENSION_DOCUMENT_ROOT: return createSmooksGraphExtensionDocumentRoot();
			case GraphPackage.FIGURE_TYPE: return createFigureType();
			case GraphPackage.GRAPH_TYPE: return createGraphType();
			case GraphPackage.INPUT_TYPE: return createInputType();
			case GraphPackage.PARAM_TYPE: return createParamType();
			case GraphPackage.PROCESSES_TYPE: return createProcessesType();
			case GraphPackage.PROCESS_TYPE: return createProcessType();
			case GraphPackage.SMOOKS_GRAPHICS_EXT_TYPE: return createSmooksGraphicsExtType();
			case GraphPackage.TASK_TYPE: return createTaskType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConnectionType createConnectionType() {
		ConnectionTypeImpl connectionType = new ConnectionTypeImpl();
		return connectionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksGraphExtensionDocumentRoot createSmooksGraphExtensionDocumentRoot() {
		SmooksGraphExtensionDocumentRootImpl smooksGraphExtensionDocumentRoot = new SmooksGraphExtensionDocumentRootImpl();
		return smooksGraphExtensionDocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FigureType createFigureType() {
		FigureTypeImpl figureType = new FigureTypeImpl();
		return figureType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GraphType createGraphType() {
		GraphTypeImpl graphType = new GraphTypeImpl();
		return graphType;
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
	public ProcessesType createProcessesType() {
		ProcessesTypeImpl processesType = new ProcessesTypeImpl();
		return processesType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProcessType createProcessType() {
		ProcessTypeImpl processType = new ProcessTypeImpl();
		return processType;
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
	public TaskType createTaskType() {
		TaskTypeImpl taskType = new TaskTypeImpl();
		return taskType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GraphPackage getGraphPackage() {
		return (GraphPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static GraphPackage getPackage() {
		return GraphPackage.eINSTANCE;
	}

} //GraphFactoryImpl
