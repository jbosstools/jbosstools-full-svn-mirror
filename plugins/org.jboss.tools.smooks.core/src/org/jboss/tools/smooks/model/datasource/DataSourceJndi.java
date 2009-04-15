/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.datasource;

import org.jboss.tools.smooks.model.smooks.ElementVisitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Source Jndi</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *     			JNDI Datasource
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#isAutoCommit <em>Auto Commit</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#getBindOnElement <em>Bind On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#getDatasource <em>Datasource</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceJndi()
 * @model extendedMetaData="name='Jndi' kind='elementOnly'"
 * @generated
 */
public interface DataSourceJndi extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Auto Commit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							If the datasource should automaticly commit.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Auto Commit</em>' attribute.
	 * @see #isSetAutoCommit()
	 * @see #unsetAutoCommit()
	 * @see #setAutoCommit(boolean)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceJndi_AutoCommit()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='attribute' name='autoCommit'"
	 * @generated
	 */
	boolean isAutoCommit();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#isAutoCommit <em>Auto Commit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Auto Commit</em>' attribute.
	 * @see #isSetAutoCommit()
	 * @see #unsetAutoCommit()
	 * @see #isAutoCommit()
	 * @generated
	 */
	void setAutoCommit(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#isAutoCommit <em>Auto Commit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAutoCommit()
	 * @see #isAutoCommit()
	 * @see #setAutoCommit(boolean)
	 * @generated
	 */
	void unsetAutoCommit();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#isAutoCommit <em>Auto Commit</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Auto Commit</em>' attribute is set.
	 * @see #unsetAutoCommit()
	 * @see #isAutoCommit()
	 * @see #setAutoCommit(boolean)
	 * @generated
	 */
	boolean isSetAutoCommit();

	/**
	 * Returns the value of the '<em><b>Bind On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The element on which the datasource is bound. On the visitAfter
	 * 			    			of the element the connection does a Commit or Rollback depending
	 * 			    			if a exception got thrown.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bind On Element</em>' attribute.
	 * @see #setBindOnElement(String)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceJndi_BindOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='bindOnElement'"
	 * @generated
	 */
	String getBindOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#getBindOnElement <em>Bind On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bind On Element</em>' attribute.
	 * @see #getBindOnElement()
	 * @generated
	 */
	void setBindOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Datasource</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The reference name of the datasource. This name must be used
	 * 			    			to retrieve the datasource from the ExecutionContext.
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Datasource</em>' attribute.
	 * @see #setDatasource(String)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDataSourceJndi_Datasource()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='datasource'"
	 * @generated
	 */
	String getDatasource();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.DataSourceJndi#getDatasource <em>Datasource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Datasource</em>' attribute.
	 * @see #getDatasource()
	 * @generated
	 */
	void setDatasource(String value);

} // DataSourceJndi
