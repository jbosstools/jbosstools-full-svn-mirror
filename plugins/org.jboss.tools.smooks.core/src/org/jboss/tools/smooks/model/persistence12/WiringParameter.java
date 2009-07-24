/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Wiring Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getBeanIdRef <em>Bean Id Ref</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getWireOnElement <em>Wire On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getWireOnElementNS <em>Wire On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getWiringParameter()
 * @model extendedMetaData="name='wiringParameter' kind='empty'"
 * @generated
 */
public interface WiringParameter extends EObject {
	/**
	 * Returns the value of the '<em><b>Bean Id Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bean Id Ref</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bean Id Ref</em>' attribute.
	 * @see #setBeanIdRef(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getWiringParameter_BeanIdRef()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='beanIdRef'"
	 * @generated
	 */
	String getBeanIdRef();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getBeanIdRef <em>Bean Id Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Id Ref</em>' attribute.
	 * @see #getBeanIdRef()
	 * @generated
	 */
	void setBeanIdRef(String value);

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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getWiringParameter_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Wire On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wire On Element</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wire On Element</em>' attribute.
	 * @see #setWireOnElement(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getWiringParameter_WireOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='wireOnElement'"
	 * @generated
	 */
	String getWireOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getWireOnElement <em>Wire On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wire On Element</em>' attribute.
	 * @see #getWireOnElement()
	 * @generated
	 */
	void setWireOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Wire On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wire On Element NS</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wire On Element NS</em>' attribute.
	 * @see #setWireOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getWiringParameter_WireOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='wireOnElementNS'"
	 * @generated
	 */
	String getWireOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.WiringParameter#getWireOnElementNS <em>Wire On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wire On Element NS</em>' attribute.
	 * @see #getWireOnElementNS()
	 * @generated
	 */
	void setWireOnElementNS(String value);

} // WiringParameter
