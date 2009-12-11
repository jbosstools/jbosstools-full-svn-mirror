/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

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
 * @see org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage#getAcknowledgeMode()
 * @model extendedMetaData="name='acknowledgeMode'"
 * @generated
 */
public enum AcknowledgeMode implements Enumerator {
	/**
	 * The '<em><b>AUTOACKNOWLEDGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #AUTOACKNOWLEDGE_VALUE
	 * @generated
	 * @ordered
	 */
	AUTOACKNOWLEDGE(0, "AUTOACKNOWLEDGE", "AUTO_ACKNOWLEDGE"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>CLIENTACKNOWLEDGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CLIENTACKNOWLEDGE_VALUE
	 * @generated
	 * @ordered
	 */
	CLIENTACKNOWLEDGE(1, "CLIENTACKNOWLEDGE", "CLIENT_ACKNOWLEDGE"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>DUPSOKACKNOWLEDGE</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #DUPSOKACKNOWLEDGE_VALUE
	 * @generated
	 * @ordered
	 */
	DUPSOKACKNOWLEDGE(2, "DUPSOKACKNOWLEDGE", "DUPS_OK_ACKNOWLEDGE"); //$NON-NLS-1$ //$NON-NLS-2$

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
	 * @see #AUTOACKNOWLEDGE
	 * @model literal="AUTO_ACKNOWLEDGE"
	 * @generated
	 * @ordered
	 */
	public static final int AUTOACKNOWLEDGE_VALUE = 0;

	/**
	 * The '<em><b>CLIENTACKNOWLEDGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						With this acknowledgement mode, the client acknowledges a message by calling a message's acknowledge method.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #CLIENTACKNOWLEDGE
	 * @model literal="CLIENT_ACKNOWLEDGE"
	 * @generated
	 * @ordered
	 */
	public static final int CLIENTACKNOWLEDGE_VALUE = 1;

	/**
	 * The '<em><b>DUPSOKACKNOWLEDGE</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						This acknowledgement mode instructs the session to lazily acknowledge the delivery of messages.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #DUPSOKACKNOWLEDGE
	 * @model literal="DUPS_OK_ACKNOWLEDGE"
	 * @generated
	 * @ordered
	 */
	public static final int DUPSOKACKNOWLEDGE_VALUE = 2;

	/**
	 * An array of all the '<em><b>Acknowledge Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final AcknowledgeMode[] VALUES_ARRAY =
		new AcknowledgeMode[] {
			AUTOACKNOWLEDGE,
			CLIENTACKNOWLEDGE,
			DUPSOKACKNOWLEDGE,
		};

	/**
	 * A public read-only list of all the '<em><b>Acknowledge Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<AcknowledgeMode> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

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
			case AUTOACKNOWLEDGE_VALUE: return AUTOACKNOWLEDGE;
			case CLIENTACKNOWLEDGE_VALUE: return CLIENTACKNOWLEDGE;
			case DUPSOKACKNOWLEDGE_VALUE: return DUPSOKACKNOWLEDGE;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private AcknowledgeMode(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //AcknowledgeMode
