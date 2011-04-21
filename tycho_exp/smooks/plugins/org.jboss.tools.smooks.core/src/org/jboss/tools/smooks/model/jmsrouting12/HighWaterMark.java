/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12;

import org.jboss.tools.smooks.model.common.AbstractAnyType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>High Water Mark</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				The configuration for the max number of messages that can be sitting in the
 * 				JMS Destination at any any time.
 *     		
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getMark <em>Mark</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getPollFrequency <em>Poll Frequency</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getTimeout <em>Timeout</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getHighWaterMark()
 * @model extendedMetaData="name='highWaterMark' kind='empty'"
 * @generated
 */
public interface HighWaterMark extends AbstractAnyType {
	/**
	 * Returns the value of the '<em><b>Mark</b></em>' attribute.
	 * The default value is <code>"200"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The max number of messages that can be sitting in the JMS Destination at any any time. Default is 200.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mark</em>' attribute.
	 * @see #isSetMark()
	 * @see #unsetMark()
	 * @see #setMark(int)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getHighWaterMark_Mark()
	 * @model default="200" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='attribute' name='mark'"
	 * @generated
	 */
	int getMark();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getMark <em>Mark</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mark</em>' attribute.
	 * @see #isSetMark()
	 * @see #unsetMark()
	 * @see #getMark()
	 * @generated
	 */
	void setMark(int value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getMark <em>Mark</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMark()
	 * @see #getMark()
	 * @see #setMark(int)
	 * @generated
	 */
	void unsetMark();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getMark <em>Mark</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Mark</em>' attribute is set.
	 * @see #unsetMark()
	 * @see #getMark()
	 * @see #setMark(int)
	 * @generated
	 */
	boolean isSetMark();

	/**
	 * Returns the value of the '<em><b>Poll Frequency</b></em>' attribute.
	 * The default value is <code>"1000"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The number of milliseconds to wait between checks on the High Water Mark, while waiting for it to drop.
	 * 					Default is 1000 ms.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Poll Frequency</em>' attribute.
	 * @see #isSetPollFrequency()
	 * @see #unsetPollFrequency()
	 * @see #setPollFrequency(int)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getHighWaterMark_PollFrequency()
	 * @model default="1000" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='attribute' name='pollFrequency'"
	 * @generated
	 */
	int getPollFrequency();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getPollFrequency <em>Poll Frequency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Poll Frequency</em>' attribute.
	 * @see #isSetPollFrequency()
	 * @see #unsetPollFrequency()
	 * @see #getPollFrequency()
	 * @generated
	 */
	void setPollFrequency(int value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getPollFrequency <em>Poll Frequency</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPollFrequency()
	 * @see #getPollFrequency()
	 * @see #setPollFrequency(int)
	 * @generated
	 */
	void unsetPollFrequency();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getPollFrequency <em>Poll Frequency</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Poll Frequency</em>' attribute is set.
	 * @see #unsetPollFrequency()
	 * @see #getPollFrequency()
	 * @see #setPollFrequency(int)
	 * @generated
	 */
	boolean isSetPollFrequency();

	/**
	 * Returns the value of the '<em><b>Timeout</b></em>' attribute.
	 * The default value is <code>"60000"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					The number of milliseconds to wait for the system to process JMS Messages from the JMS destination so that the
	 * 					number of JMS Messages drops below the highWaterMark. Default is 60000 ms.
	 * 		 		
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Timeout</em>' attribute.
	 * @see #isSetTimeout()
	 * @see #unsetTimeout()
	 * @see #setTimeout(int)
	 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getHighWaterMark_Timeout()
	 * @model default="60000" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int"
	 *        extendedMetaData="kind='attribute' name='timeout'"
	 * @generated
	 */
	int getTimeout();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getTimeout <em>Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Timeout</em>' attribute.
	 * @see #isSetTimeout()
	 * @see #unsetTimeout()
	 * @see #getTimeout()
	 * @generated
	 */
	void setTimeout(int value);

	/**
	 * Unsets the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getTimeout <em>Timeout</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetTimeout()
	 * @see #getTimeout()
	 * @see #setTimeout(int)
	 * @generated
	 */
	void unsetTimeout();

	/**
	 * Returns whether the value of the '{@link org.jboss.tools.smooks.model.jmsrouting12.HighWaterMark#getTimeout <em>Timeout</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Timeout</em>' attribute is set.
	 * @see #unsetTimeout()
	 * @see #getTimeout()
	 * @see #setTimeout(int)
	 * @generated
	 */
	boolean isSetTimeout();

} // HighWaterMark
