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
 * A representation of the model object '<em><b>Deleter</b></em>'.
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
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Deleter#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Deleter#isDeleteBefore <em>Delete Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDeletedBeanId <em>Deleted Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDeleteOnElement <em>Delete On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDeleteOnElementNS <em>Delete On Element NS</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Deleter#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getDeleter()
 * @model extendedMetaData="name='deleter' kind='elementOnly'"
 * @generated
 */
public interface Deleter extends ElementVisitor {
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getDeleter_BeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='beanId'"
	 * @generated
	 */
	String getBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getBeanId <em>Bean Id</em>}' attribute.
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getDeleter_Dao()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='dao'"
	 * @generated
	 */
	String getDao();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDao <em>Dao</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dao</em>' attribute.
	 * @see #getDao()
	 * @generated
	 */
	void setDao(String value);

	/**
	 * Returns the value of the '<em><b>Delete Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Delete Before</em>' attribute.
	 * @see #isSetDeleteBefore()
	 * @see #unsetDeleteBefore()
	 * @see #setDeleteBefore(boolean)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getDeleter_DeleteBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='deleteBefore'"
	 * @generated
	 */
	boolean isDeleteBefore();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Deleter#isDeleteBefore <em>Delete Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delete Before</em>' attribute.
	 * @see #isSetDeleteBefore()
	 * @see #unsetDeleteBefore()
	 * @see #isDeleteBefore()
	 * @generated
	 */
	void setDeleteBefore(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Deleter#isDeleteBefore <em>Delete Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDeleteBefore()
	 * @see #isDeleteBefore()
	 * @see #setDeleteBefore(boolean)
	 * @generated
	 */
	void unsetDeleteBefore();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.persistence12.Deleter#isDeleteBefore <em>Delete Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Delete Before</em>' attribute is set.
	 * @see #unsetDeleteBefore()
	 * @see #isDeleteBefore()
	 * @see #setDeleteBefore(boolean)
	 * @generated
	 */
	boolean isSetDeleteBefore();

	/**
	 * Returns the value of the '<em><b>Deleted Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Deleted Bean Id</em>' attribute.
	 * @see #setDeletedBeanId(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getDeleter_DeletedBeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='deletedBeanId'"
	 * @generated
	 */
	String getDeletedBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDeletedBeanId <em>Deleted Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Deleted Bean Id</em>' attribute.
	 * @see #getDeletedBeanId()
	 * @generated
	 */
	void setDeletedBeanId(String value);

	/**
	 * Returns the value of the '<em><b>Delete On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Delete On Element</em>' attribute.
	 * @see #setDeleteOnElement(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getDeleter_DeleteOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='deleteOnElement'"
	 * @generated
	 */
	String getDeleteOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDeleteOnElement <em>Delete On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delete On Element</em>' attribute.
	 * @see #getDeleteOnElement()
	 * @generated
	 */
	void setDeleteOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Delete On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        						Namespace control for the
	 *        						"deleteOnElement" attribute.
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Delete On Element NS</em>' attribute.
	 * @see #setDeleteOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getDeleter_DeleteOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='deleteOnElementNS'"
	 * @generated
	 */
	String getDeleteOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getDeleteOnElementNS <em>Delete On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delete On Element NS</em>' attribute.
	 * @see #getDeleteOnElementNS()
	 * @generated
	 */
	void setDeleteOnElementNS(String value);

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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getDeleter_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Deleter#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // Deleter
