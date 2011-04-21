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
 * A representation of the literals of the enumeration '<em><b>Delivery Mode</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * 
 * 				The possible JMS delivery modes.
 *     		
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage#getDeliveryMode()
 * @model extendedMetaData="name='deliveryMode'"
 * @generated
 */
public enum DeliveryMode implements Enumerator {
	/**
	 * The '<em><b>Persistent</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PERSISTENT_VALUE
	 * @generated
	 * @ordered
	 */
	PERSISTENT(0, "persistent", "persistent"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Non Persistent</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NON_PERSISTENT_VALUE
	 * @generated
	 * @ordered
	 */
	NON_PERSISTENT(1, "nonPersistent", "non-persistent"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>Persistent</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						This mode instructs the JMS provider to log the message to stable storage as part of the client's send operation.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #PERSISTENT
	 * @model name="persistent"
	 * @generated
	 * @ordered
	 */
	public static final int PERSISTENT_VALUE = 0;

	/**
	 * The '<em><b>Non Persistent</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 						This is the lowest overhead delivery mode because it does not require that the message be logged to stable storage.
	 *     				
	 * <!-- end-model-doc -->
	 * @see #NON_PERSISTENT
	 * @model name="nonPersistent" literal="non-persistent"
	 * @generated
	 * @ordered
	 */
	public static final int NON_PERSISTENT_VALUE = 1;

	/**
	 * An array of all the '<em><b>Delivery Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final DeliveryMode[] VALUES_ARRAY =
		new DeliveryMode[] {
			PERSISTENT,
			NON_PERSISTENT,
		};

	/**
	 * A public read-only list of all the '<em><b>Delivery Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<DeliveryMode> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

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
			case PERSISTENT_VALUE: return PERSISTENT;
			case NON_PERSISTENT_VALUE: return NON_PERSISTENT;
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
	private DeliveryMode(int value, String name, String literal) {
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
	
} //DeliveryMode
