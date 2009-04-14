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
 * A representation of the model object '<em><b>Executor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *     			SQL Executor
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.Executor#getStatement <em>Statement</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.Executor#getResultSet <em>Result Set</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.Executor#getDatasource <em>Datasource</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.Executor#isExecuteBefore <em>Execute Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.Executor#getExecuteOnElement <em>Execute On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.Executor#getExecuteOnElementNS <em>Execute On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getExecutor()
 * @model extendedMetaData="name='executor' kind='elementOnly'"
 * @generated
 */
public interface Executor extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Statement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				    			The SQL statement to be executed. Supports templating in the form of ${variable}.
	 * 				    			The variables are resolved from the bean context.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Statement</em>' attribute.
	 * @see #setStatement(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getExecutor_Statement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='statement' namespace='##targetNamespace'"
	 * @generated
	 */
	String getStatement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getStatement <em>Statement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Statement</em>' attribute.
	 * @see #getStatement()
	 * @generated
	 */
	void setStatement(String value);

	/**
	 * Returns the value of the '<em><b>Result Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				    			Sets the resultSet specific configuration like the resultSet name, scope and timeToLive.
	 * 				    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Result Set</em>' containment reference.
	 * @see #setResultSet(ResultSet)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getExecutor_ResultSet()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='resultSet' namespace='##targetNamespace'"
	 * @generated
	 */
	ResultSet getResultSet();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getResultSet <em>Result Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result Set</em>' containment reference.
	 * @see #getResultSet()
	 * @generated
	 */
	void setResultSet(ResultSet value);

	/**
	 * Returns the value of the '<em><b>Datasource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The name of the datasource configuration to use. See the datasource configuration
	 * 			    			of the Smooks core library.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Datasource</em>' attribute.
	 * @see #setDatasource(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getExecutor_Datasource()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='datasource'"
	 * @generated
	 */
	String getDatasource();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getDatasource <em>Datasource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Datasource</em>' attribute.
	 * @see #getDatasource()
	 * @generated
	 */
	void setDatasource(String value);

	/**
	 * Returns the value of the '<em><b>Execute Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			If the query is executed before the element else it will execute after the element.
	 * 			    			Default is 'false'.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute Before</em>' attribute.
	 * @see #isSetExecuteBefore()
	 * @see #unsetExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getExecutor_ExecuteBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='executeBefore'"
	 * @generated
	 */
	boolean isExecuteBefore();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.Executor#isExecuteBefore <em>Execute Before</em>}' attribute.
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
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.Executor#isExecuteBefore <em>Execute Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetExecuteBefore()
	 * @see #isExecuteBefore()
	 * @see #setExecuteBefore(boolean)
	 * @generated
	 */
	void unsetExecuteBefore();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.dbrouting.Executor#isExecuteBefore <em>Execute Before</em>}' attribute is set.
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
	 * 			    			The element on which the query is executed. With the 'executeBefore'
	 * 			    			attribute can be set if the query is executed before or after the element.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute On Element</em>' attribute.
	 * @see #setExecuteOnElement(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getExecutor_ExecuteOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='executeOnElement'"
	 * @generated
	 */
	String getExecuteOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getExecuteOnElement <em>Execute On Element</em>}' attribute.
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
	 * 			    			The namespace of the executeOnElement element.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Execute On Element NS</em>' attribute.
	 * @see #setExecuteOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getExecutor_ExecuteOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='executeOnElementNS'"
	 * @generated
	 */
	String getExecuteOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.Executor#getExecuteOnElementNS <em>Execute On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Execute On Element NS</em>' attribute.
	 * @see #getExecuteOnElementNS()
	 * @generated
	 */
	void setExecuteOnElementNS(String value);

} // Executor
