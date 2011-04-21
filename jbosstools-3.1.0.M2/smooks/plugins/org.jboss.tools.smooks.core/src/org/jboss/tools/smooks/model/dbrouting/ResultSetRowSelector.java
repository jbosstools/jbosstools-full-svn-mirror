/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.dbrouting;

import org.jboss.tools.smooks.model.smooks.ElementVisitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Result Set Row Selector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *     			ResultSet row selector
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getWhere <em>Where</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getFailedSelectError <em>Failed Select Error</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getResultSetName <em>Result Set Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getSelectRowOnElement <em>Select Row On Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSetRowSelector()
 * @model extendedMetaData="name='resultSetRowSelector' kind='elementOnly'"
 * @generated
 */
public interface ResultSetRowSelector extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Where</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				    			The MVEL condition to select the correct row.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Where</em>' attribute.
	 * @see #setWhere(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSetRowSelector_Where()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='where' namespace='##targetNamespace'"
	 * @generated
	 */
	String getWhere();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getWhere <em>Where</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Where</em>' attribute.
	 * @see #getWhere()
	 * @generated
	 */
	void setWhere(String value);

	/**
	 * Returns the value of the '<em><b>Failed Select Error</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				    			The error message that is thrown in org.milyn.routing.db.DataSelectionException
	 * 				    			exception when no row was found that matches the 'where' query.
	 * 				    			If the error message is not set then no exception will be thrown.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Failed Select Error</em>' attribute.
	 * @see #setFailedSelectError(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSetRowSelector_FailedSelectError()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='failedSelectError' namespace='##targetNamespace'"
	 * @generated
	 */
	String getFailedSelectError();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getFailedSelectError <em>Failed Select Error</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Failed Select Error</em>' attribute.
	 * @see #getFailedSelectError()
	 * @generated
	 */
	void setFailedSelectError(String value);

	/**
	 * Returns the value of the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						The beanId under which the selected row will be added in the bean context.
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Id</em>' attribute.
	 * @see #setBeanId(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSetRowSelector_BeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='beanId'"
	 * @generated
	 */
	String getBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getBeanId <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Id</em>' attribute.
	 * @see #getBeanId()
	 * @generated
	 */
	void setBeanId(String value);

	/**
	 * Returns the value of the '<em><b>Execute Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						If the selection is done before or after the selected element. Default is 'false'.
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute Before</em>' attribute.
	 * @see #isSetExecuteBefore()
	 * @see #unsetExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSetRowSelector_ExecuteBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='executeBefore'"
	 * @generated
	 */
	boolean isExecuteBefore();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#isExecuteBefore <em>Execute Before</em>}' attribute.
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
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#isExecuteBefore <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetExecuteBefore()
	 * @see #isExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @generated
	 */
	void unsetExecuteBefore();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#isExecuteBefore <em>Execute Before</em>}' attribute is set.
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
	 * Returns the value of the '<em><b>Result Set Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The name of the resultSet to select the row from.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Result Set Name</em>' attribute.
	 * @see #setResultSetName(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSetRowSelector_ResultSetName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='resultSetName'"
	 * @generated
	 */
	String getResultSetName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getResultSetName <em>Result Set Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result Set Name</em>' attribute.
	 * @see #getResultSetName()
	 * @generated
	 */
	void setResultSetName(String value);

	/**
	 * Returns the value of the '<em><b>Select Row On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The element on which the row selection is executed.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Select Row On Element</em>' attribute.
	 * @see #setSelectRowOnElement(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSetRowSelector_SelectRowOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='selectRowOnElement'"
	 * @generated
	 */
	String getSelectRowOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector#getSelectRowOnElement <em>Select Row On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Select Row On Element</em>' attribute.
	 * @see #getSelectRowOnElement()
	 * @generated
	 */
	void setSelectRowOnElement(String value);

} // ResultSetRowSelector
