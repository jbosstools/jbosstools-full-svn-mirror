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
 * A representation of the model object '<em><b>Direct</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *     			Direct Datasource
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.Direct#isAutoCommit <em>Auto Commit</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.Direct#getBindOnElement <em>Bind On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.Direct#getBindOnElementNS <em>Bind On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.Direct#getDatasource <em>Datasource</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.Direct#getDriver <em>Driver</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.Direct#getPassword <em>Password</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.Direct#getUrl <em>Url</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.datasource.Direct#getUsername <em>Username</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDirect()
 * @model extendedMetaData="name='direct' kind='elementOnly'"
 * @generated
 */
public interface Direct extends ElementVisitor {
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
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDirect_AutoCommit()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 *        extendedMetaData="kind='attribute' name='autoCommit'"
	 * @generated
	 */
	boolean isAutoCommit();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#isAutoCommit <em>Auto Commit</em>}' attribute.
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
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#isAutoCommit <em>Auto Commit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetAutoCommit()
	 * @see #isAutoCommit()
	 * @see #setAutoCommit(boolean)
	 * @generated
	 */
	void unsetAutoCommit();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#isAutoCommit <em>Auto Commit</em>}' attribute is set.
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
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDirect_BindOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='bindOnElement'"
	 * @generated
	 */
	String getBindOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#getBindOnElement <em>Bind On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bind On Element</em>' attribute.
	 * @see #getBindOnElement()
	 * @generated
	 */
	void setBindOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Bind On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 			    			The namespace of the bindOnElement element
	 * 			    		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bind On Element NS</em>' attribute.
	 * @see #setBindOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDirect_BindOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='bindOnElementNS'"
	 * @generated
	 */
	String getBindOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#getBindOnElementNS <em>Bind On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bind On Element NS</em>' attribute.
	 * @see #getBindOnElementNS()
	 * @generated
	 */
	void setBindOnElementNS(String value);

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
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDirect_Datasource()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='datasource'"
	 * @generated
	 */
	String getDatasource();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#getDatasource <em>Datasource</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Datasource</em>' attribute.
	 * @see #getDatasource()
	 * @generated
	 */
	void setDatasource(String value);

	/**
	 * Returns the value of the '<em><b>Driver</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						The JDBC driver name
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Driver</em>' attribute.
	 * @see #setDriver(String)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDirect_Driver()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='driver'"
	 * @generated
	 */
	String getDriver();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#getDriver <em>Driver</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Driver</em>' attribute.
	 * @see #getDriver()
	 * @generated
	 */
	void setDriver(String value);

	/**
	 * Returns the value of the '<em><b>Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						The password
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Password</em>' attribute.
	 * @see #setPassword(String)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDirect_Password()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='password'"
	 * @generated
	 */
	String getPassword();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#getPassword <em>Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Password</em>' attribute.
	 * @see #getPassword()
	 * @generated
	 */
	void setPassword(String value);

	/**
	 * Returns the value of the '<em><b>Url</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						The JDBC URL
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Url</em>' attribute.
	 * @see #setUrl(String)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDirect_Url()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
	 *        extendedMetaData="kind='attribute' name='url'"
	 * @generated
	 */
	String getUrl();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#getUrl <em>Url</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Url</em>' attribute.
	 * @see #getUrl()
	 * @generated
	 */
	void setUrl(String value);

	/**
	 * Returns the value of the '<em><b>Username</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						The username
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Username</em>' attribute.
	 * @see #setUsername(String)
	 * @see org.jboss.tools.smooks.model.datasource.DatasourcePackage#getDirect_Username()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='username'"
	 * @generated
	 */
	String getUsername();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.datasource.Direct#getUsername <em>Username</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Username</em>' attribute.
	 * @see #getUsername()
	 * @generated
	 */
	void setUsername(String value);

} // Direct
