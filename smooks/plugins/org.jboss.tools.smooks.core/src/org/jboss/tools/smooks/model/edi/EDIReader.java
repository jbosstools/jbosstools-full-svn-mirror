/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.edi;

import org.jboss.tools.smooks.model.smooks.AbstractReader;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EDI Reader</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * EDI Reader
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.edi.EDIReader#getEncoding <em>Encoding</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.edi.EDIReader#getMappingModel <em>Mapping Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.edi.EdiPackage#getEDIReader()
 * @model extendedMetaData="name='reader' kind='empty'"
 * @generated
 */
public interface EDIReader extends AbstractReader {
	/**
	 * Returns the value of the '<em><b>Encoding</b></em>' attribute.
	 * The default value is <code>"UTF-8"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The character encoding. Default "UTF-8"
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Encoding</em>' attribute.
	 * @see #isSetEncoding()
	 * @see #unsetEncoding()
	 * @see #setEncoding(String)
	 * @see org.jboss.tools.smooks.model.edi.EdiPackage#getEDIReader_Encoding()
	 * @model default="UTF-8" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='encoding'"
	 * @generated
	 */
	String getEncoding();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.edi.EDIReader#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Encoding</em>' attribute.
	 * @see #isSetEncoding()
	 * @see #unsetEncoding()
	 * @see #getEncoding()
	 * @generated
	 */
	void setEncoding(String value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.edi.EDIReader#getEncoding <em>Encoding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetEncoding()
	 * @see #getEncoding()
	 * @see #setEncoding(String)
	 * @generated
	 */
	void unsetEncoding();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.edi.EDIReader#getEncoding <em>Encoding</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Encoding</em>' attribute is set.
	 * @see #unsetEncoding()
	 * @see #getEncoding()
	 * @see #setEncoding(String)
	 * @generated
	 */
	boolean isSetEncoding();

	/**
	 * Returns the value of the '<em><b>Mapping Model</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    		 	The URI of the EDI mapping model file of the EDI model.
	 * 			    		 	For more information about the mapping model take a
	 * 			    		 	look at the "http://www.milyn.org/schema/edi-message-mapping-1.0.xsd"
	 * 			    		 	schema.
	 * 			    		 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mapping Model</em>' attribute.
	 * @see #setMappingModel(String)
	 * @see org.jboss.tools.smooks.model.edi.EdiPackage#getEDIReader_MappingModel()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='mappingModel'"
	 * @generated
	 */
	String getMappingModel();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.edi.EDIReader#getMappingModel <em>Mapping Model</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mapping Model</em>' attribute.
	 * @see #getMappingModel()
	 * @generated
	 */
	void setMappingModel(String value);

} // EDIReader
