/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.datasource;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Source Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getDirect <em>Direct</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getJNDI <em>JNDI</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DataSourceDocumentRoot extends EObject {
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
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceDocumentRoot_Mixed()
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
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceDocumentRoot_XMLNSPrefixMap()
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
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceDocumentRoot_XSISchemaLocation()
	 * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
	 *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
	 * @generated
	 */
	EMap<String, String> getXSISchemaLocation();

	/**
	 * Returns the value of the '<em><b>Direct</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     			Direct datasource configuration
	 *     			The direct datasource resource creates a connection to a datasource and
	 *     			makes it available in the ExecutionContext. The datasource can then be used
	 *     			by other cartridges to access the datasource.
	 *     		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Direct</em>' containment reference.
	 * @see #setDirect(Direct)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceDocumentRoot_Direct()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='direct' namespace='##targetNamespace' affiliation='http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config'"
	 * @generated
	 */
	Direct getDirect();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getDirect <em>Direct</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Direct</em>' containment reference.
	 * @see #getDirect()
	 * @generated
	 */
	void setDirect(Direct value);

	/**
	 * Returns the value of the '<em><b>JNDI</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     			JNDI datasource configuration
	 *     			The JNDI datasource resource retrieves a datasource from the JNDI and
	 *     			makes it available in the ExecutionContext. The datasource can then be used
	 *     			by other cartridges to access the datasource.
	 *     		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>JNDI</em>' containment reference.
	 * @see #setJNDI(DataSourceJndi)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceDocumentRoot_JNDI()
	 * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='JNDI' namespace='##targetNamespace' affiliation='http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config'"
	 * @generated
	 */
	DataSourceJndi getJNDI();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.DataSourceDocumentRoot#getJNDI <em>JNDI</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>JNDI</em>' containment reference.
	 * @see #getJNDI()
	 * @generated
	 */
	void setJNDI(DataSourceJndi value);

} // DataSourceDocumentRoot
