/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getDeleter <em>Deleter</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getFlusher <em>Flusher</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getInserter <em>Inserter</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getLocator <em>Locator</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getUpdater <em>Updater</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getPersistence12DocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface Persistence12DocumentRoot extends EObject {
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getPersistence12DocumentRoot_Mixed()
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getPersistence12DocumentRoot_XMLNSPrefixMap()
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getPersistence12DocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	EMap getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Deleter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Deleter</em>' containment reference.
	 * @see #setDeleter(Deleter)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getPersistence12DocumentRoot_Deleter()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='deleter' namespace='##targetNamespace' affiliation='http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config'"
	 * @generated
	 */
	Deleter getDeleter();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getDeleter <em>Deleter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Deleter</em>' containment reference.
	 * @see #getDeleter()
	 * @generated
	 */
	void setDeleter(Deleter value);

	/**
	 * Returns the value of the '<em><b>Flusher</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Flusher</em>' containment reference.
	 * @see #setFlusher(Flusher)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getPersistence12DocumentRoot_Flusher()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='flusher' namespace='##targetNamespace' affiliation='http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config'"
	 * @generated
	 */
	Flusher getFlusher();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getFlusher <em>Flusher</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flusher</em>' containment reference.
	 * @see #getFlusher()
	 * @generated
	 */
	void setFlusher(Flusher value);

	/**
	 * Returns the value of the '<em><b>Inserter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Inserter</em>' containment reference.
	 * @see #setInserter(Inserter)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getPersistence12DocumentRoot_Inserter()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='inserter' namespace='##targetNamespace' affiliation='http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config'"
	 * @generated
	 */
	Inserter getInserter();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getInserter <em>Inserter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Inserter</em>' containment reference.
	 * @see #getInserter()
	 * @generated
	 */
	void setInserter(Inserter value);

	/**
	 * Returns the value of the '<em><b>Locator</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Locator</em>' containment reference.
	 * @see #setLocator(Locator)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getPersistence12DocumentRoot_Locator()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='locator' namespace='##targetNamespace' affiliation='http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config'"
	 * @generated
	 */
	Locator getLocator();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getLocator <em>Locator</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Locator</em>' containment reference.
	 * @see #getLocator()
	 * @generated
	 */
	void setLocator(Locator value);

	/**
	 * Returns the value of the '<em><b>Updater</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Updater</em>' containment reference.
	 * @see #setUpdater(Updater)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getPersistence12DocumentRoot_Updater()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='updater' namespace='##targetNamespace' affiliation='http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config'"
	 * @generated
	 */
	Updater getUpdater();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot#getUpdater <em>Updater</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Updater</em>' containment reference.
	 * @see #getUpdater()
	 * @generated
	 */
	void setUpdater(Updater value);

} // Persistence12DocumentRoot
