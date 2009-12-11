/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.dbrouting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Result Set Scope Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.dbrouting.DbroutingPackage#getResultSetScopeType()
 * @model extendedMetaData="name='resultSetScopeType'"
 * @generated
 */
public enum ResultSetScopeType implements Enumerator {
	/**
	 * The '<em><b>EXECUTION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EXECUTION_VALUE
	 * @generated
	 * @ordered
	 */
	EXECUTION(0, "EXECUTION", "EXECUTION"), //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>APPLICATION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #APPLICATION_VALUE
	 * @generated
	 * @ordered
	 */
	APPLICATION(1, "APPLICATION", "APPLICATION"); //$NON-NLS-1$ //$NON-NLS-2$

	/**
	 * The '<em><b>EXECUTION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EXECUTION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EXECUTION
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int EXECUTION_VALUE = 0;

	/**
	 * The '<em><b>APPLICATION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>APPLICATION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #APPLICATION
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int APPLICATION_VALUE = 1;

	/**
	 * An array of all the '<em><b>Result Set Scope Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ResultSetScopeType[] VALUES_ARRAY =
		new ResultSetScopeType[] {
			EXECUTION,
			APPLICATION,
		};

	/**
	 * A public read-only list of all the '<em><b>Result Set Scope Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ResultSetScopeType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Result Set Scope Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ResultSetScopeType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ResultSetScopeType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Result Set Scope Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ResultSetScopeType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ResultSetScopeType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Result Set Scope Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ResultSetScopeType get(int value) {
		switch (value) {
			case EXECUTION_VALUE: return EXECUTION;
			case APPLICATION_VALUE: return APPLICATION;
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
	private ResultSetScopeType(int value, String name, String literal) {
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
	
} //ResultSetScopeType
