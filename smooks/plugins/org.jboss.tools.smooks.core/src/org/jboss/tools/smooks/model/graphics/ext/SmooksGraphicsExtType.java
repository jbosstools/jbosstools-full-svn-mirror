/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Smooks Graphics Ext Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInput <em>Input</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getGraph <em>Graph</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getAuthor <em>Author</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInputType <em>Input Type</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getOutputType <em>Output Type</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getPlatformVersion <em>Platform Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphicsExtType()
 * @model extendedMetaData="name='smooks-graphics-ext_._type' kind='elementOnly'"
 * @generated
 */
public interface SmooksGraphicsExtType extends EObject {
	
	List<ISmooksGraphChangeListener> getChangeListeners();

	void addSmooksGraphChangeListener(ISmooksGraphChangeListener listener);

	void removeSmooksGraphChangeListener(ISmooksGraphChangeListener listener);

	
	/**
	 * Returns the value of the '<em><b>Input</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks.model.graphics.ext.InputType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input</em>' containment reference list.
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphicsExtType_Input()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='input' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<InputType> getInput();

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
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphicsExtType_Graph()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='graph' namespace='##targetNamespace'"
	 * @generated
	 */
	GraphType getGraph();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getGraph <em>Graph</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Graph</em>' containment reference.
	 * @see #getGraph()
	 * @generated
	 */
	void setGraph(GraphType value);

	/**
	 * Returns the value of the '<em><b>Author</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Author</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Author</em>' attribute.
	 * @see #setAuthor(String)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphicsExtType_Author()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='author'"
	 * @generated
	 */
	String getAuthor();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getAuthor <em>Author</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Author</em>' attribute.
	 * @see #getAuthor()
	 * @generated
	 */
	void setAuthor(String value);

	/**
	 * Returns the value of the '<em><b>Input Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Type</em>' attribute.
	 * @see #setInputType(String)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphicsExtType_InputType()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='inputType'"
	 * @generated
	 */
	String getInputType();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getInputType <em>Input Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Type</em>' attribute.
	 * @see #getInputType()
	 * @generated
	 */
	void setInputType(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphicsExtType_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Output Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Type</em>' attribute.
	 * @see #setOutputType(String)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphicsExtType_OutputType()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='outputType'"
	 * @generated
	 */
	String getOutputType();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getOutputType <em>Output Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output Type</em>' attribute.
	 * @see #getOutputType()
	 * @generated
	 */
	void setOutputType(String value);

	/**
	 * Returns the value of the '<em><b>Platform Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Platform Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Platform Version</em>' attribute.
	 * @see #setPlatformVersion(String)
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#getSmooksGraphicsExtType_PlatformVersion()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='platformVersion'"
	 * @generated
	 */
	String getPlatformVersion();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType#getPlatformVersion <em>Platform Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Platform Version</em>' attribute.
	 * @see #getPlatformVersion()
	 * @generated
	 */
	void setPlatformVersion(String value);

} // SmooksGraphicsExtType
