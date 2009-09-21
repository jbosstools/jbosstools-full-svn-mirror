/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Resource Configuration Resource Type (xsl, class etc)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.ResourceType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getResourceType()
 * @model extendedMetaData="name='resource_._type' kind='mixed'"
 * @generated
 */
public interface ResourceType extends AbstractType {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getResourceType_Type()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='type'"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks10.model.smooks.ResourceType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);
	
	
	String getStringValue();
	
	void setStringValue(String value);
	
	String getCDATAValue();
	
	void setCDATAValue(String value);
	

} // ResourceType
