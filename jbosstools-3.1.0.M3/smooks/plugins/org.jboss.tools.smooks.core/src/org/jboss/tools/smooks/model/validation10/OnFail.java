/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.validation10;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>On Fail</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.jboss.tools.smooks.model.validation10.Validation10Package#getOnFail()
 * @model extendedMetaData="name='onFail'"
 * @generated
 */
public final class OnFail extends AbstractEnumerator {
	/**
	 * The '<em><b>OK</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>OK</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #OK_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int OK = 0;

	/**
	 * The '<em><b>WARN</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>WARN</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #WARN_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int WARN = 1;

	/**
	 * The '<em><b>ERROR</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>ERROR</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #ERROR_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int ERROR = 2;

	/**
	 * The '<em><b>FATAL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of '<em><b>FATAL</b></em>' literal object isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @see #FATAL_LITERAL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int FATAL = 3;

	/**
	 * The '<em><b>OK</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #OK
	 * @generated
	 * @ordered
	 */
	public static final OnFail OK_LITERAL = new OnFail(OK, "OK", "OK");

	/**
	 * The '<em><b>WARN</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #WARN
	 * @generated
	 * @ordered
	 */
	public static final OnFail WARN_LITERAL = new OnFail(WARN, "WARN", "WARN");

	/**
	 * The '<em><b>ERROR</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #ERROR
	 * @generated
	 * @ordered
	 */
	public static final OnFail ERROR_LITERAL = new OnFail(ERROR, "ERROR", "ERROR");

	/**
	 * The '<em><b>FATAL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #FATAL
	 * @generated
	 * @ordered
	 */
	public static final OnFail FATAL_LITERAL = new OnFail(FATAL, "FATAL", "FATAL");

	/**
	 * An array of all the '<em><b>On Fail</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final OnFail[] VALUES_ARRAY =
		new OnFail[] {
			OK_LITERAL,
			WARN_LITERAL,
			ERROR_LITERAL,
			FATAL_LITERAL,
		};

	/**
	 * A public read-only list of all the '<em><b>On Fail</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>On Fail</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OnFail get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OnFail result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>On Fail</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OnFail getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			OnFail result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>On Fail</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static OnFail get(int value) {
		switch (value) {
			case OK: return OK_LITERAL;
			case WARN: return WARN_LITERAL;
			case ERROR: return ERROR_LITERAL;
			case FATAL: return FATAL_LITERAL;
		}
		return null;
	}

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private OnFail(int value, String name, String literal) {
		super(value, name, literal);
	}

} //OnFail
