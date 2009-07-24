/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Acknowledge Mode</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 * 				The possible JMS acknowledge modes
 *     		
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getAcknowledgeMode()
 * @model extendedMetaData="name='acknowledgeMode'"
 * @generated
 */
public final class AcknowledgeMode extends AbstractEnumerator {
	/**
	 * The '<em><b>AUTOACKNOWLEDGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						With this acknowledgement mode, the session automatically acknowledges a client's receipt of a
	 * 						message when it has either successfully returned from a call to receive or the message listener
	 * 						it has called to process the message successfully returns.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #AUTOACKNOWLEDGE_LITERAL
	 * @model literal="AUTO_ACKNOWLEDGE"
	 * @generated
	 * @ordered
	 */
	public static final int AUTOACKNOWLEDGE = 0;

	/**
	 * The '<em><b>CLIENTACKNOWLEDGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						With this acknowledgement mode, the client acknowledges a message by calling a message's acknowledge method.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #CLIENTACKNOWLEDGE_LITERAL
	 * @model literal="CLIENT_ACKNOWLEDGE"
	 * @generated
	 * @ordered
	 */
	public static final int CLIENTACKNOWLEDGE = 1;

	/**
	 * The '<em><b>DUPSOKACKNOWLEDGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						This acknowledgement mode instructs the session to lazily acknowledge the delivery of messages.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #DUPSOKACKNOWLEDGE_LITERAL
	 * @model literal="DUPS_OK_ACKNOWLEDGE"
	 * @generated
	 * @ordered
	 */
	public static final int DUPSOKACKNOWLEDGE = 2;

	/**
	 * The '<em><b>AUTOACKNOWLEDGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #AUTOACKNOWLEDGE
	 * @generated
	 * @ordered
	 */
	public static final AcknowledgeMode AUTOACKNOWLEDGE_LITERAL = new AcknowledgeMode(AUTOACKNOWLEDGE, "AUTOACKNOWLEDGE", "AUTO_ACKNOWLEDGE");

	/**
	 * The '<em><b>CLIENTACKNOWLEDGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CLIENTACKNOWLEDGE
	 * @generated
	 * @ordered
	 */
	public static final AcknowledgeMode CLIENTACKNOWLEDGE_LITERAL = new AcknowledgeMode(CLIENTACKNOWLEDGE, "CLIENTACKNOWLEDGE", "CLIENT_ACKNOWLEDGE");

	/**
	 * The '<em><b>DUPSOKACKNOWLEDGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DUPSOKACKNOWLEDGE
	 * @generated
	 * @ordered
	 */
	public static final AcknowledgeMode DUPSOKACKNOWLEDGE_LITERAL = new AcknowledgeMode(DUPSOKACKNOWLEDGE, "DUPSOKACKNOWLEDGE", "DUPS_OK_ACKNOWLEDGE");

	/**
	 * An array of all the '<em><b>Acknowledge Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final AcknowledgeMode[] VALUES_ARRAY =
		new AcknowledgeMode[] {
			AUTOACKNOWLEDGE_LITERAL,
			CLIENTACKNOWLEDGE_LITERAL,
			DUPSOKACKNOWLEDGE_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Acknowledge Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Acknowledge Mode</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AcknowledgeMode get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AcknowledgeMode result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Acknowledge Mode</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AcknowledgeMode getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			AcknowledgeMode result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Acknowledge Mode</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AcknowledgeMode get(int value) {
		switch (value) {
			case AUTOACKNOWLEDGE: return AUTOACKNOWLEDGE_LITERAL;
			case CLIENTACKNOWLEDGE: return CLIENTACKNOWLEDGE_LITERAL;
			case DUPSOKACKNOWLEDGE: return DUPSOKACKNOWLEDGE_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private AcknowledgeMode(int value, String name, String literal) {
		super(value, name, literal);
	}

} //AcknowledgeMode
