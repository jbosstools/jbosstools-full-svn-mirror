/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.groovy;

import org.eclipse.emf.common.util.EList;
import org.jboss.tools.smooks.model.smooks.ElementVisitor;
import org.jboss.tools.smooks.model.smooks.ParamType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Groovy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *                 Groovy Script.
 *             
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link groovy.Groovy#getImports <em>Imports</em>}</li>
 *   <li>{@link groovy.Groovy#getParam <em>Param</em>}</li>
 *   <li>{@link groovy.Groovy#getScript <em>Script</em>}</li>
 *   <li>{@link groovy.Groovy#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link groovy.Groovy#getExecuteOnElement <em>Execute On Element</em>}</li>
 *   <li>{@link groovy.Groovy#getExecuteOnElementNS <em>Execute On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @see groovy.GroovyPackage#getGroovy()
 * @model extendedMetaData="name='groovy' kind='elementOnly'"
 * @generated
 */
public interface Groovy extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Imports</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Imports</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Imports</em>' attribute.
	 * @see #setImports(String)
	 * @see groovy.GroovyPackage#getGroovy_Imports()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='imports' namespace='##targetNamespace'"
	 * @generated
	 */
	String getImports();

	/**
	 * Sets the value of the '{@link groovy.Groovy#getImports <em>Imports</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Imports</em>' attribute.
	 * @see #getImports()
	 * @generated
	 */
	void setImports(String value);

	/**
	 * Returns the value of the '<em><b>Param</b></em>' containment reference list.
	 * The list contents are of type {@link smooks.ParamType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Param</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Param</em>' containment reference list.
	 * @see groovy.GroovyPackage#getGroovy_Param()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='param' namespace='http://www.milyn.org/xsd/smooks-1.1.xsd'"
	 * @generated
	 */
	EList<ParamType> getParam();

	/**
	 * Returns the value of the '<em><b>Script</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Script</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Script</em>' containment reference.
	 * @see #setScript(ScriptType)
	 * @see groovy.GroovyPackage#getGroovy_Script()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='script' namespace='##targetNamespace'"
	 * @generated
	 */
	ScriptType getScript();

	/**
	 * Sets the value of the '{@link groovy.Groovy#getScript <em>Script</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Script</em>' containment reference.
	 * @see #getScript()
	 * @generated
	 */
	void setScript(ScriptType value);

	/**
	 * Returns the value of the '<em><b>Execute Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                             Execute the script before visiting the elements child content. Default is 'false'.
	 *                         
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute Before</em>' attribute.
	 * @see #isSetExecuteBefore()
	 * @see #unsetExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @see groovy.GroovyPackage#getGroovy_ExecuteBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='executeBefore'"
	 * @generated
	 */
	boolean isExecuteBefore();

	/**
	 * Sets the value of the '{@link groovy.Groovy#isExecuteBefore <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execute Before</em>' attribute.
	 * @see #isSetExecuteBefore()
	 * @see #unsetExecuteBefore()
	 * @see #isExecuteBefore()
	 * @generated
	 */
	void setExecuteBefore(boolean value);

	/**
	 * Unsets the value of the '{@link groovy.Groovy#isExecuteBefore <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetExecuteBefore()
	 * @see #isExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @generated
	 */
	void unsetExecuteBefore();

	/**
	 * Returns whether the value of the '{@link groovy.Groovy#isExecuteBefore <em>Execute Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Execute Before</em>' attribute is set.
	 * @see #unsetExecuteBefore()
	 * @see #isExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @generated
	 */
	boolean isSetExecuteBefore();

	/**
	 * Returns the value of the '<em><b>Execute On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                             The name of the element on which to execute the script.
	 *                         
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute On Element</em>' attribute.
	 * @see #setExecuteOnElement(String)
	 * @see groovy.GroovyPackage#getGroovy_ExecuteOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='executeOnElement'"
	 * @generated
	 */
	String getExecuteOnElement();

	/**
	 * Sets the value of the '{@link groovy.Groovy#getExecuteOnElement <em>Execute On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execute On Element</em>' attribute.
	 * @see #getExecuteOnElement()
	 * @generated
	 */
	void setExecuteOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Execute On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                             The namespace of the element on which to execute the script.
	 *                         
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute On Element NS</em>' attribute.
	 * @see #setExecuteOnElementNS(String)
	 * @see groovy.GroovyPackage#getGroovy_ExecuteOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='executeOnElementNS'"
	 * @generated
	 */
	String getExecuteOnElementNS();

	/**
	 * Sets the value of the '{@link groovy.Groovy#getExecuteOnElementNS <em>Execute On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execute On Element NS</em>' attribute.
	 * @see #getExecuteOnElementNS()
	 * @generated
	 */
	void setExecuteOnElementNS(String value);

} // Groovy
