/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>JMS Routing12 Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getRouter <em>Router</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJMSRouting12DocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface JMSRouting12DocumentRoot extends EObject {
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
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJMSRouting12DocumentRoot_Mixed()
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
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJMSRouting12DocumentRoot_XMLNSPrefixMap()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
	 * @generated
	 */
	EMap getXMLNSPrefixMap();

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
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJMSRouting12DocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	EMap getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Router</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 	   			JMS router configuration
	 *     			The JMS router routes messages to a JMS destination (Queue or Topic).
	 *     			The message payload is created from a bean out of the bean context. It is possible
	 *     			to send Text or Object messages. The correlation id can be generated out of a FreeMarker
	 *     			template.
	 * 	   		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Router</em>' containment reference.
	 * @see #setRouter(JMS12Router)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getJMSRouting12DocumentRoot_Router()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='router' namespace='##targetNamespace' affiliation='http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config'"
	 * @generated
	 */
	JMS12Router getRouter();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.JMSRouting12DocumentRoot#getRouter <em>Router</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Router</em>' containment reference.
	 * @see #getRouter()
	 * @generated
	 */
	void setRouter(JMS12Router value);

} // JMSRouting12DocumentRoot
