/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>On No Result</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#getOnNoResult()
 * @model extendedMetaData="name='OnNoResult'"
 * @generated
 */
public final class OnNoResult extends AbstractEnumerator {
	/**
	 * The '<em><b>NULLIFY</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>NULLIFY</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #NULLIFY_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int NULLIFY = 0;

	/**
	 * The '<em><b>EXCEPTION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>EXCEPTION</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #EXCEPTION_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int EXCEPTION = 1;

	/**
	 * The '<em><b>NULLIFY</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #NULLIFY
	 * @generated
	 * @ordered
	 */
	public static final OnNoResult NULLIFY_LITERAL = new OnNoResult(NULLIFY, "NULLIFY", "NULLIFY");

	/**
	 * The '<em><b>EXCEPTION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EXCEPTION
	 * @generated
	 * @ordered
	 */
	public static final OnNoResult EXCEPTION_LITERAL = new OnNoResult(EXCEPTION, "EXCEPTION", "EXCEPTION");

	/**
	 * An array of all the '<em><b>On No Result</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final OnNoResult[] VALUES_ARRAY =
		new OnNoResult[] {
			NULLIFY_LITERAL,
			EXCEPTION_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>On No Result</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>On No Result</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OnNoResult get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OnNoResult result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>On No Result</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OnNoResult getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OnNoResult result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>On No Result</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OnNoResult get(int value) {
		switch (value) {
			case NULLIFY: return NULLIFY_LITERAL;
			case EXCEPTION: return EXCEPTION_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private OnNoResult(int value, String name, String literal) {
		super(value, name, literal);
	}

} //OnNoResult
