/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12;

import org.jboss.tools.smooks.model.smooks.ElementVisitor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Locator</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Locator#getQuery <em>Query</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Locator#getParams <em>Params</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Locator#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Locator#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Locator#getLookup <em>Lookup</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Locator#getLookupOnElement <em>Lookup On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Locator#getLookupOnElementNS <em>Lookup On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Locator#getOnNoResult <em>On No Result</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Locator#isUniqueResult <em>Unique Result</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator()
 * @model extendedMetaData="name='locator' kind='elementOnly'"
 * @generated
 */
public interface Locator extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Query</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Query</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Query</em>' attribute.
	 * @see #setQuery(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator_Query()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='query' namespace='##targetNamespace'"
	 * @generated
	 */
	String getQuery();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getQuery <em>Query</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Query</em>' attribute.
	 * @see #getQuery()
	 * @generated
	 */
	void setQuery(String value);

	/**
	 * Returns the value of the '<em><b>Params</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Params</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Params</em>' containment reference.
	 * @see #setParams(Parameters)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator_Params()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='params' namespace='##targetNamespace'"
	 * @generated
	 */
	Parameters getParams();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getParams <em>Params</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Params</em>' containment reference.
	 * @see #getParams()
	 * @generated
	 */
	void setParams(Parameters value);

	/**
	 * Returns the value of the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Id</em>' attribute.
	 * @see #setBeanId(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator_BeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='beanId'"
	 * @generated
	 */
	String getBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getBeanId <em>Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Bean Id</em>' attribute.
	 * @see #getBeanId()
	 * @generated
	 */
	void setBeanId(String value);

	/**
	 * Returns the value of the '<em><b>Dao</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Dao</em>' attribute.
	 * @see #setDao(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator_Dao()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='dao'"
	 * @generated
	 */
	String getDao();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getDao <em>Dao</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dao</em>' attribute.
	 * @see #getDao()
	 * @generated
	 */
	void setDao(String value);

	/**
	 * Returns the value of the '<em><b>Lookup</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Lookup</em>' attribute.
	 * @see #setLookup(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator_Lookup()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='lookup'"
	 * @generated
	 */
	String getLookup();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getLookup <em>Lookup</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lookup</em>' attribute.
	 * @see #getLookup()
	 * @generated
	 */
	void setLookup(String value);

	/**
	 * Returns the value of the '<em><b>Lookup On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Lookup On Element</em>' attribute.
	 * @see #setLookupOnElement(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator_LookupOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='lookupOnElement'"
	 * @generated
	 */
	String getLookupOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getLookupOnElement <em>Lookup On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lookup On Element</em>' attribute.
	 * @see #getLookupOnElement()
	 * @generated
	 */
	void setLookupOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Lookup On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     						Namespace control for the "flushOnElement"
	 *     						attribute.
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Lookup On Element NS</em>' attribute.
	 * @see #setLookupOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator_LookupOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='lookupOnElementNS'"
	 * @generated
	 */
	String getLookupOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getLookupOnElementNS <em>Lookup On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lookup On Element NS</em>' attribute.
	 * @see #getLookupOnElementNS()
	 * @generated
	 */
	void setLookupOnElementNS(String value);

	/**
	 * Returns the value of the '<em><b>On No Result</b></em>' attribute.
	 * The default value is <code>"NULLIFY"</code>.
	 * The literals are from the enumeration {@link org.jboss.tools.smooks.model.persistence12.OnNoResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>On No Result</em>' attribute.
	 * @see org.jboss.tools.smooks.model.persistence12.OnNoResult
	 * @see #isSetOnNoResult()
	 * @see #unsetOnNoResult()
	 * @see #setOnNoResult(OnNoResult)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator_OnNoResult()
	 * @model default="NULLIFY" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='onNoResult'"
	 * @generated
	 */
	OnNoResult getOnNoResult();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getOnNoResult <em>On No Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>On No Result</em>' attribute.
	 * @see org.jboss.tools.smooks.model.persistence12.OnNoResult
	 * @see #isSetOnNoResult()
	 * @see #unsetOnNoResult()
	 * @see #getOnNoResult()
	 * @generated
	 */
	void setOnNoResult(OnNoResult value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getOnNoResult <em>On No Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetOnNoResult()
	 * @see #getOnNoResult()
	 * @see #setOnNoResult(OnNoResult)
	 * @generated
	 */
	void unsetOnNoResult();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#getOnNoResult <em>On No Result</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>On No Result</em>' attribute is set.
	 * @see #unsetOnNoResult()
	 * @see #getOnNoResult()
	 * @see #setOnNoResult(OnNoResult)
	 * @generated
	 */
	boolean isSetOnNoResult();

	/**
	 * Returns the value of the '<em><b>Unique Result</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *     					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Unique Result</em>' attribute.
	 * @see #isSetUniqueResult()
	 * @see #unsetUniqueResult()
	 * @see #setUniqueResult(boolean)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getLocator_UniqueResult()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='uniqueResult'"
	 * @generated
	 */
	boolean isUniqueResult();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#isUniqueResult <em>Unique Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unique Result</em>' attribute.
	 * @see #isSetUniqueResult()
	 * @see #unsetUniqueResult()
	 * @see #isUniqueResult()
	 * @generated
	 */
	void setUniqueResult(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#isUniqueResult <em>Unique Result</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetUniqueResult()
	 * @see #isUniqueResult()
	 * @see #setUniqueResult(boolean)
	 * @generated
	 */
	void unsetUniqueResult();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.persistence12.Locator#isUniqueResult <em>Unique Result</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Unique Result</em>' attribute is set.
	 * @see #unsetUniqueResult()
	 * @see #isUniqueResult()
	 * @see #setUniqueResult(boolean)
	 * @generated
	 */
	boolean isSetUniqueResult();

} // Locator
