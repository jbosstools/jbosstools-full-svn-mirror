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
 * A representation of the literals of the enumeration '<em><b>Message Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 * 				The possible JMS message types.
 *     		
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getMessageType()
 * @model extendedMetaData="name='messageType'"
 * @generated
 */
public final class MessageType extends AbstractEnumerator {
	/**
	 * The '<em><b>Text Message</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						A TextMessage is used to send a message containing a java.lang.String.
	 * 						To retrieve the string the cartridge calls the 'toString()' method of the target bean.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #TEXT_MESSAGE_LITERAL
	 * @model name="TextMessage"
	 * @generated
	 * @ordered
	 */
	public static final int TEXT_MESSAGE = 0;

	/**
	 * The '<em><b>Object Message</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						An ObjectMessage is used to send a message that contains a serializable Java object.
	 * 						Only Serializable Java objects can be used.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #OBJECT_MESSAGE_LITERAL
	 * @model name="ObjectMessage"
	 * @generated
	 * @ordered
	 */
	public static final int OBJECT_MESSAGE = 1;

	/**
	 * The '<em><b>Map Message</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						A MapMessage is used to send a set of name-value pairs where names are Strings and values are Java primitive types or Strings.
	 * 						Only Map Java objects can be used.
	 * 						For the keys of the map the 'toString()' method is called to retrieve the String representation.
	 * 						Objects that aren't primitive types or Strings get the toString() method called to return the String representation.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #MAP_MESSAGE_LITERAL
	 * @model name="MapMessage"
	 * @generated
	 * @ordered
	 */
	public static final int MAP_MESSAGE = 2;

	/**
	 * The '<em><b>Text Message</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TEXT_MESSAGE
	 * @generated
	 * @ordered
	 */
	public static final MessageType TEXT_MESSAGE_LITERAL = new MessageType(TEXT_MESSAGE, "TextMessage", "TextMessage");

	/**
	 * The '<em><b>Object Message</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #OBJECT_MESSAGE
	 * @generated
	 * @ordered
	 */
	public static final MessageType OBJECT_MESSAGE_LITERAL = new MessageType(OBJECT_MESSAGE, "ObjectMessage", "ObjectMessage");

	/**
	 * The '<em><b>Map Message</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MAP_MESSAGE
	 * @generated
	 * @ordered
	 */
	public static final MessageType MAP_MESSAGE_LITERAL = new MessageType(MAP_MESSAGE, "MapMessage", "MapMessage");

	/**
	 * An array of all the '<em><b>Message Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final MessageType[] VALUES_ARRAY =
		new MessageType[] {
			TEXT_MESSAGE_LITERAL,
			OBJECT_MESSAGE_LITERAL,
			MAP_MESSAGE_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Message Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Message Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MessageType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MessageType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Message Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MessageType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			MessageType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Message Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static MessageType get(int value) {
		switch (value) {
			case TEXT_MESSAGE: return TEXT_MESSAGE_LITERAL;
			case OBJECT_MESSAGE: return OBJECT_MESSAGE_LITERAL;
			case MAP_MESSAGE: return MAP_MESSAGE_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private MessageType(int value, String name, String literal) {
		super(value, name, literal);
	}

} //MessageType
