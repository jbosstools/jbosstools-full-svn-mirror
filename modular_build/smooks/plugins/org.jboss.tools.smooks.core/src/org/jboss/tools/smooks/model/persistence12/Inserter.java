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
 * A representation of the model object '<em><b>Inserter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Inserter#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Inserter#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Inserter#isInsertBefore <em>Insert Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Inserter#getInsertedBeanId <em>Inserted Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Inserter#getInsertOnElement <em>Insert On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Inserter#getInsertOnElementNS <em>Insert On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Inserter#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getInserter()
 * @model extendedMetaData="name='inserter' kind='elementOnly'"
 * @generated
 */
public interface Inserter extends ElementVisitor {
	/**
	 * Returns the value of the '<em><b>Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        						The ID under which the entity bean is
	 *        						bound in the bean context.
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bean Id</em>' attribute.
	 * @see #setBeanId(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getInserter_BeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='beanId'"
	 * @generated
	 */
	String getBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getBeanId <em>Bean Id</em>}' attribute.
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getInserter_Dao()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='dao'"
	 * @generated
	 */
	String getDao();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getDao <em>Dao</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dao</em>' attribute.
	 * @see #getDao()
	 * @generated
	 */
	void setDao(String value);

	/**
	 * Returns the value of the '<em><b>Insert Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Insert Before</em>' attribute.
	 * @see #isSetInsertBefore()
	 * @see #unsetInsertBefore()
	 * @see #setInsertBefore(boolean)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getInserter_InsertBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='insertBefore'"
	 * @generated
	 */
	boolean isInsertBefore();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Inserter#isInsertBefore <em>Insert Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Insert Before</em>' attribute.
	 * @see #isSetInsertBefore()
	 * @see #unsetInsertBefore()
	 * @see #isInsertBefore()
	 * @generated
	 */
	void setInsertBefore(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Inserter#isInsertBefore <em>Insert Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInsertBefore()
	 * @see #isInsertBefore()
	 * @see #setInsertBefore(boolean)
	 * @generated
	 */
	void unsetInsertBefore();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.persistence12.Inserter#isInsertBefore <em>Insert Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Insert Before</em>' attribute is set.
	 * @see #unsetInsertBefore()
	 * @see #isInsertBefore()
	 * @see #setInsertBefore(boolean)
	 * @generated
	 */
	boolean isSetInsertBefore();

	/**
	 * Returns the value of the '<em><b>Inserted Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Inserted Bean Id</em>' attribute.
	 * @see #setInsertedBeanId(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getInserter_InsertedBeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='insertedBeanId'"
	 * @generated
	 */
	String getInsertedBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getInsertedBeanId <em>Inserted Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Inserted Bean Id</em>' attribute.
	 * @see #getInsertedBeanId()
	 * @generated
	 */
	void setInsertedBeanId(String value);

	/**
	 * Returns the value of the '<em><b>Insert On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Insert On Element</em>' attribute.
	 * @see #setInsertOnElement(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getInserter_InsertOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='insertOnElement'"
	 * @generated
	 */
	String getInsertOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getInsertOnElement <em>Insert On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Insert On Element</em>' attribute.
	 * @see #getInsertOnElement()
	 * @generated
	 */
	void setInsertOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Insert On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        						Namespace control for the
	 *        						"insertOnElement" attribute.
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Insert On Element NS</em>' attribute.
	 * @see #setInsertOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getInserter_InsertOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='insertOnElementNS'"
	 * @generated
	 */
	String getInsertOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getInsertOnElementNS <em>Insert On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Insert On Element NS</em>' attribute.
	 * @see #getInsertOnElementNS()
	 * @generated
	 */
	void setInsertOnElementNS(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getInserter_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Inserter#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // Inserter
