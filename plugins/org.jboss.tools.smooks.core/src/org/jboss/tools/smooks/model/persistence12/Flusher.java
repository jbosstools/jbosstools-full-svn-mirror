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
 * A representation of the model object '<em><b>Flusher</b></em>'.
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
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Flusher#getDao <em>Dao</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Flusher#isFlushBefore <em>Flush Before</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Flusher#getFlushOnElement <em>Flush On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.persistence12.Flusher#getFlushOnElementNS <em>Flush On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getFlusher()
 * @model extendedMetaData="name='flusher' kind='elementOnly'"
 * @generated
 */
public interface Flusher extends ElementVisitor {
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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getFlusher_Dao()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='dao'"
	 * @generated
	 */
	String getDao();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Flusher#getDao <em>Dao</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dao</em>' attribute.
	 * @see #getDao()
	 * @generated
	 */
	void setDao(String value);

	/**
	 * Returns the value of the '<em><b>Flush Before</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Flush Before</em>' attribute.
	 * @see #isSetFlushBefore()
	 * @see #unsetFlushBefore()
	 * @see #setFlushBefore(boolean)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getFlusher_FlushBefore()
	 * @model default="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='flushBefore'"
	 * @generated
	 */
	boolean isFlushBefore();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Flusher#isFlushBefore <em>Flush Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flush Before</em>' attribute.
	 * @see #isSetFlushBefore()
	 * @see #unsetFlushBefore()
	 * @see #isFlushBefore()
	 * @generated
	 */
	void setFlushBefore(boolean value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Flusher#isFlushBefore <em>Flush Before</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFlushBefore()
	 * @see #isFlushBefore()
	 * @see #setFlushBefore(boolean)
	 * @generated
	 */
	void unsetFlushBefore();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.persistence12.Flusher#isFlushBefore <em>Flush Before</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Flush Before</em>' attribute is set.
	 * @see #unsetFlushBefore()
	 * @see #isFlushBefore()
	 * @see #setFlushBefore(boolean)
	 * @generated
	 */
	boolean isSetFlushBefore();

	/**
	 * Returns the value of the '<em><b>Flush On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Flush On Element</em>' attribute.
	 * @see #setFlushOnElement(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getFlusher_FlushOnElement()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='attribute' name='flushOnElement'"
	 * @generated
	 */
	String getFlushOnElement();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Flusher#getFlushOnElement <em>Flush On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flush On Element</em>' attribute.
	 * @see #getFlushOnElement()
	 * @generated
	 */
	void setFlushOnElement(String value);

	/**
	 * Returns the value of the '<em><b>Flush On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *        						Namespace control for the
	 *        						"flushOnElement" attribute.
	 *        					
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Flush On Element NS</em>' attribute.
	 * @see #setFlushOnElementNS(String)
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getFlusher_FlushOnElementNS()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='flushOnElementNS'"
	 * @generated
	 */
	String getFlushOnElementNS();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.persistence12.Flusher#getFlushOnElementNS <em>Flush On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Flush On Element NS</em>' attribute.
	 * @see #getFlushOnElementNS()
	 * @generated
	 */
	void setFlushOnElementNS(String value);

} // Flusher
