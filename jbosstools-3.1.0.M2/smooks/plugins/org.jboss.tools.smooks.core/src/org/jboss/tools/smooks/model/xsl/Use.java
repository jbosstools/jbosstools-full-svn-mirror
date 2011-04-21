/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.xsl;

import org.jboss.tools.smooks.model.common.AbstractAnyType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Use</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *                 The &lt;use&gt; element allows you to define how the templating result is to be used.
 *             
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.xsl.Use#getInline <em>Inline</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.xsl.Use#getBindTo <em>Bind To</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.xsl.Use#getOutputTo <em>Output To</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.xsl.XslPackage#getUse()
 * @model extendedMetaData="name='use' kind='elementOnly'"
 * @generated
 */
public interface Use extends AbstractAnyType {
	/**
	 * Returns the value of the '<em><b>Inline</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Inline</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inline</em>' containment reference.
	 * @see #setInline(Inline)
	 * @see org.jboss.tools.smooks.model.xsl.XslPackage#getUse_Inline()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='inline' namespace='##targetNamespace'"
	 * @generated
	 */
	Inline getInline();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.xsl.Use#getInline <em>Inline</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Inline</em>' containment reference.
	 * @see #getInline()
	 * @generated
	 */
	void setInline(Inline value);

	/**
	 * Returns the value of the '<em><b>Bind To</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bind To</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bind To</em>' containment reference.
	 * @see #setBindTo(BindTo)
	 * @see org.jboss.tools.smooks.model.xsl.XslPackage#getUse_BindTo()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='bindTo' namespace='##targetNamespace'"
	 * @generated
	 */
	BindTo getBindTo();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.xsl.Use#getBindTo <em>Bind To</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bind To</em>' containment reference.
	 * @see #getBindTo()
	 * @generated
	 */
	void setBindTo(BindTo value);

	/**
	 * Returns the value of the '<em><b>Output To</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output To</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output To</em>' containment reference.
	 * @see #setOutputTo(OutputTo)
	 * @see org.jboss.tools.smooks.model.xsl.XslPackage#getUse_OutputTo()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='outputTo' namespace='##targetNamespace'"
	 * @generated
	 */
	OutputTo getOutputTo();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.xsl.Use#getOutputTo <em>Output To</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output To</em>' containment reference.
	 * @see #getOutputTo()
	 * @generated
	 */
	void setOutputTo(OutputTo value);

} // Use
