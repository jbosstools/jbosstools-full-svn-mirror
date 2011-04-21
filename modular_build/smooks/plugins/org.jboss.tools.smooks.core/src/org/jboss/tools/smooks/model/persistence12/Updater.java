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
 * A representation of the model object '<em><b>Updater</b></em>'.
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
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Updater#getBeanId <em>Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Updater#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Updater#getName <em>Name</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Updater#isUpdateBefore <em>Update Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Updater#getUpdatedBeanId <em>Updated Bean Id</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Updater#getUpdateOnElement <em>Update On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Updater#getUpdateOnElementNS <em>Update On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getUpdater()
 * @model extendedMetaData="name='updater' kind='elementOnly'"
 * @generated
 */
public interface Updater extends ElementVisitor {
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getUpdater_BeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='beanId'"
	 * @generated
	 */
	String getBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Updater#getBeanId <em>Bean Id</em>}' attribute.
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getUpdater_Dao()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='dao'"
	 * @generated
	 */
	String getDao();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Updater#getDao <em>Dao</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dao</em>' attribute.
	 * @see #getDao()
	 * @generated
	 */
	void setDao(String value);

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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getUpdater_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Updater#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Update Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update Before</em>' attribute.
	 * @see #isSetUpdateBefore()
	 * @see #unsetUpdateBefore()
	 * @see #setUpdateBefore(boolean)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getUpdater_UpdateBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='updateBefore'"
	 * @generated
	 */
	boolean isUpdateBefore();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Updater#isUpdateBefore <em>Update Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Update Before</em>' attribute.
	 * @see #isSetUpdateBefore()
	 * @see #unsetUpdateBefore()
	 * @see #isUpdateBefore()
	 * @generated
	 */
	void setUpdateBefore(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Updater#isUpdateBefore <em>Update Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetUpdateBefore()
	 * @see #isUpdateBefore()
	 * @see #setUpdateBefore(boolean)
	 * @generated
	 */
	void unsetUpdateBefore();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.persistence12.Updater#isUpdateBefore <em>Update Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Update Before</em>' attribute is set.
	 * @see #unsetUpdateBefore()
	 * @see #isUpdateBefore()
	 * @see #setUpdateBefore(boolean)
	 * @generated
	 */
	boolean isSetUpdateBefore();

	/**
	 * Returns the value of the '<em><b>Updated Bean Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Updated Bean Id</em>' attribute.
	 * @see #setUpdatedBeanId(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getUpdater_UpdatedBeanId()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='updatedBeanId'"
	 * @generated
	 */
	String getUpdatedBeanId();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Updater#getUpdatedBeanId <em>Updated Bean Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Updated Bean Id</em>' attribute.
	 * @see #getUpdatedBeanId()
	 * @generated
	 */
	void setUpdatedBeanId(String value);

	/**
	 * Returns the value of the '<em><b>Update On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update On Element</em>' attribute.
	 * @see #setUpdateOnElement(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getUpdater_UpdateOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='updateOnElement'"
	 * @generated
	 */
	String getUpdateOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Updater#getUpdateOnElement <em>Update On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Update On Element</em>' attribute.
	 * @see #getUpdateOnElement()
	 * @generated
	 */
	void setUpdateOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Update On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        						Namespace control for the
	 *        						"updateOnElement" attribute.
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Update On Element NS</em>' attribute.
	 * @see #setUpdateOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getUpdater_UpdateOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='updateOnElementNS'"
	 * @generated
	 */
	String getUpdateOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Updater#getUpdateOnElementNS <em>Update On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Update On Element NS</em>' attribute.
	 * @see #getUpdateOnElementNS()
	 * @generated
	 */
	void setUpdateOnElementNS(String value);

} // Updater
