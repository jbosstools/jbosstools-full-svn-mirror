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
 * A representation of the literals of the enumeration '<em><b>Delivery Mode</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 * 				The possible JMS delivery modes.
 *     		
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package#getDeliveryMode()
 * @model extendedMetaData="name='deliveryMode'"
 * @generated
 */
public final class DeliveryMode extends AbstractEnumerator {
	/**
	 * The '<em><b>Persistent</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						This mode instructs the JMS provider to log the message to stable storage as part of the client's send operation.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #PERSISTENT_LITERAL
	 * @model name="persistent"
	 * @generated
	 * @ordered
	 */
	public static final int PERSISTENT = 0;

	/**
	 * The '<em><b>Non Persistent</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						This is the lowest overhead delivery mode because it does not require that the message be logged to stable storage.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #NON_PERSISTENT_LITERAL
	 * @model name="nonPersistent" literal="non-persistent"
	 * @generated
	 * @ordered
	 */
	public static final int NON_PERSISTENT = 1;

	/**
	 * The '<em><b>Persistent</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PERSISTENT
	 * @generated
	 * @ordered
	 */
	public static final DeliveryMode PERSISTENT_LITERAL = new DeliveryMode(PERSISTENT, "persistent", "persistent"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Non Persistent</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NON_PERSISTENT
	 * @generated
	 * @ordered
	 */
	public static final DeliveryMode NON_PERSISTENT_LITERAL = new DeliveryMode(NON_PERSISTENT, "nonPersistent", "non-persistent"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * An array of all the '<em><b>Delivery Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final DeliveryMode[] VALUES_ARRAY =
		new DeliveryMode[] {
			PERSISTENT_LITERAL,
			NON_PERSISTENT_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>Delivery Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Delivery Mode</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DeliveryMode get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			DeliveryMode result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Delivery Mode</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DeliveryMode getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			DeliveryMode result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Delivery Mode</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DeliveryMode get(int value) {
		switch (value) {
			case PERSISTENT: return PERSISTENT_LITERAL;
			case NON_PERSISTENT: return NON_PERSISTENT_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private DeliveryMode(int value, String name, String literal) {
		super(value, name, literal);
	}

} //DeliveryMode
