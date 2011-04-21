/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Smooks Graph Extension Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getConnection <em>Connection</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getFigure <em>Figure</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getGraph <em>Graph</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getInput <em>Input</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getParam <em>Param</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getProcess <em>Process</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getProcesses <em>Processes</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getSmooksGraphicsExt <em>Smooks Graphics Ext</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getSource <em>Source</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getTarget <em>Target</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getTask <em>Task</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface SmooksGraphExtensionDocumentRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
	FeatureMap getMixed();

	/**
	 * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XMLNS Prefix Map</em>' map.
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
	EMap<String, String> getXMLNSPrefixMap();

	/**
	 * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>XSI Schema Location</em>' map.
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	EMap<String, String> getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Connection</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Connection</em>' containment reference.
	 * @see #setConnection(ConnectionType)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Connection()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='connection' namespace='##targetNamespace'"
	 * @generated
	 */
	ConnectionType getConnection();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getConnection <em>Connection</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Connection</em>' containment reference.
	 * @see #getConnection()
	 * @generated
	 */
	void setConnection(ConnectionType value);

	/**
	 * Returns the value of the '<em><b>Figure</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Figure</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Figure</em>' containment reference.
	 * @see #setFigure(FigureType)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Figure()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='figure' namespace='##targetNamespace'"
	 * @generated
	 */
	FigureType getFigure();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getFigure <em>Figure</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Figure</em>' containment reference.
	 * @see #getFigure()
	 * @generated
	 */
	void setFigure(FigureType value);

	/**
	 * Returns the value of the '<em><b>Graph</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Graph</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Graph</em>' containment reference.
	 * @see #setGraph(GraphType)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Graph()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='graph' namespace='##targetNamespace'"
	 * @generated
	 */
	GraphType getGraph();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getGraph <em>Graph</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Graph</em>' containment reference.
	 * @see #getGraph()
	 * @generated
	 */
	void setGraph(GraphType value);

	/**
	 * Returns the value of the '<em><b>Input</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input</em>' containment reference.
	 * @see #setInput(InputType)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Input()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='input' namespace='##targetNamespace'"
	 * @generated
	 */
	InputType getInput();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getInput <em>Input</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input</em>' containment reference.
	 * @see #getInput()
	 * @generated
	 */
	void setInput(InputType value);

	/**
	 * Returns the value of the '<em><b>Param</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Param</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Param</em>' containment reference.
	 * @see #setParam(ParamType)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Param()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='param' namespace='##targetNamespace'"
	 * @generated
	 */
	ParamType getParam();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getParam <em>Param</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Param</em>' containment reference.
	 * @see #getParam()
	 * @generated
	 */
	void setParam(ParamType value);

	/**
	 * Returns the value of the '<em><b>Process</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Process</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Process</em>' containment reference.
	 * @see #setProcess(ProcessType)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Process()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='process' namespace='##targetNamespace'"
	 * @generated
	 */
	ProcessType getProcess();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getProcess <em>Process</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Process</em>' containment reference.
	 * @see #getProcess()
	 * @generated
	 */
	void setProcess(ProcessType value);

	/**
	 * Returns the value of the '<em><b>Processes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Processes</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Processes</em>' containment reference.
	 * @see #setProcesses(ProcessesType)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Processes()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='processes' namespace='##targetNamespace'"
	 * @generated
	 */
	ProcessesType getProcesses();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getProcesses <em>Processes</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Processes</em>' containment reference.
	 * @see #getProcesses()
	 * @generated
	 */
	void setProcesses(ProcessesType value);

	/**
	 * Returns the value of the '<em><b>Smooks Graphics Ext</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Smooks Graphics Ext</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Smooks Graphics Ext</em>' containment reference.
	 * @see #setSmooksGraphicsExt(SmooksGraphicsExtType)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_SmooksGraphicsExt()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='smooks-graphics-ext' namespace='##targetNamespace' affiliation='http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config'"
	 * @generated
	 */
	SmooksGraphicsExtType getSmooksGraphicsExt();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getSmooksGraphicsExt <em>Smooks Graphics Ext</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Smooks Graphics Ext</em>' containment reference.
	 * @see #getSmooksGraphicsExt()
	 * @generated
	 */
	void setSmooksGraphicsExt(SmooksGraphicsExtType value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' attribute.
	 * @see #setSource(String)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Source()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='source' namespace='##targetNamespace'"
	 * @generated
	 */
	String getSource();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getSource <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' attribute.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(String value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' attribute.
	 * @see #setTarget(String)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Target()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='target' namespace='##targetNamespace'"
	 * @generated
	 */
	String getTarget();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getTarget <em>Target</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' attribute.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(String value);

	/**
	 * Returns the value of the '<em><b>Task</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Task</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Task</em>' containment reference.
	 * @see #setTask(TaskType)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphExtensionDocumentRoot_Task()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='task' namespace='##targetNamespace'"
	 * @generated
	 */
	TaskType getTask();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot#getTask <em>Task</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Task</em>' containment reference.
	 * @see #getTask()
	 * @generated
	 */
	void setTask(TaskType value);

} // SmooksGraphExtensionDocumentRoot
