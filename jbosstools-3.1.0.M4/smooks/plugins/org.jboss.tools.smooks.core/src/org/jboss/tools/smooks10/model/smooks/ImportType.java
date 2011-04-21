/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Import Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Smooks Resource Configuration Import
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.ImportType#getFile <em>File</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getImportType()
 * @model extendedMetaData="name='import_._type' kind='empty'"
 * @generated
 */
public interface ImportType extends AbstractResourceConfig {
	/**
	 * Returns the value of the '<em><b>File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>File</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>File</em>' attribute.
	 * @see #setFile(String)
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getImportType_File()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
	 *        extendedMetaData="kind='attribute' name='file'"
	 * @generated
	 */
	String getFile();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks10.model.smooks.ImportType#getFile <em>File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>File</em>' attribute.
	 * @see #getFile()
	 * @generated
	 */
	void setFile(String value);

} // ImportType
