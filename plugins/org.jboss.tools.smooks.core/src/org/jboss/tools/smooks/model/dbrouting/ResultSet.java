/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.dbrouting;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Result Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getScope <em>Scope</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getTimeToLive <em>Time To Live</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSet()
 * @model extendedMetaData="name='resultSet' kind='empty'"
 * @generated
 */
public interface ResultSet extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     				If the statement is a query statement then the ResultSet will be bound
	 * 	    			with this id in the ExecutionContext. Must be specified if the 'statement' is a query
	 * 	    			statement, otherwise it is optional.
	 *     			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSet_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Scope</b></em>' attribute.
	 * The default value is <code>"EXECUTION"</code>.
	 * The literals are from the enumeration {@link org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     				The scope on which the resultSet will be stored. The scope can be on a EXECUTION or
	 *     				on a APPLICATION level. On a EXECUTION level the query is executed every time on the selected element.
	 *     				On a APPLICATION level the query is only executed once for the
	 *     				whole Smooks instance until the resultSet expires. With the 'timeToLive' attribute the expire time
	 *     				can be configured. After the resultSet is expired the query will be executed again.
	 *     			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Scope</em>' attribute.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType
	 * @see #isSetScope()
	 * @see #unsetScope()
	 * @see #setScope(ResultSetScopeType)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSet_Scope()
	 * @model default="EXECUTION" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='scope'"
	 * @generated
	 */
	ResultSetScopeType getScope();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getScope <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scope</em>' attribute.
	 * @see org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType
	 * @see #isSetScope()
	 * @see #unsetScope()
	 * @see #getScope()
	 * @generated
	 */
	void setScope(ResultSetScopeType value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getScope <em>Scope</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetScope()
	 * @see #getScope()
	 * @see #setScope(ResultSetScopeType)
	 * @generated
	 */
	void unsetScope();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getScope <em>Scope</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Scope</em>' attribute is set.
	 * @see #unsetScope()
	 * @see #getScope()
	 * @see #setScope(ResultSetScopeType)
	 * @generated
	 */
	boolean isSetScope();

	/**
	 * Returns the value of the '<em><b>Time To Live</b></em>' attribute.
	 * The default value is <code>"900000"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     				If the 'scope' attribute is set to APPLICATION this attribute determines the expire time
	 *     				in milliseconds of the resultSet. After the resultSet is expired the query will be executed again.
	 *     			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Time To Live</em>' attribute.
	 * @see #isSetTimeToLive()
	 * @see #unsetTimeToLive()
	 * @see #setTimeToLive(long)
	 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSet_TimeToLive()
	 * @model default="900000" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Long"
	 *        extendedMetaData="kind='attribute' name='timeToLive'"
	 * @generated
	 */
	long getTimeToLive();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getTimeToLive <em>Time To Live</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time To Live</em>' attribute.
	 * @see #isSetTimeToLive()
	 * @see #unsetTimeToLive()
	 * @see #getTimeToLive()
	 * @generated
	 */
	void setTimeToLive(long value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getTimeToLive <em>Time To Live</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTimeToLive()
	 * @see #getTimeToLive()
	 * @see #setTimeToLive(long)
	 * @generated
	 */
	void unsetTimeToLive();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.dbrouting.ResultSet#getTimeToLive <em>Time To Live</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Time To Live</em>' attribute is set.
	 * @see #unsetTimeToLive()
	 * @see #getTimeToLive()
	 * @see #setTimeToLive(long)
	 * @generated
	 */
	boolean isSetTimeToLive();

} // ResultSet
