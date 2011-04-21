/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.core;

import org.milyn.StreamFilterType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Global Params</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.jboss.tools.smooks.model.core.ICorePackage#getGlobalParams()
 * @model
 * @generated
 */
public interface IGlobalParams extends IParams {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model filterTypeDataType="org.jboss.tools.smooks.model.core.StreamFilterType"
	 * @generated
	 */
	IGlobalParams setFilterType(StreamFilterType filterType);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" dataType="org.jboss.tools.smooks.model.core.StreamFilterType"
	 * @generated
	 */
	StreamFilterType getFilterType();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isDefaultSerializationOn();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	IGlobalParams setDefaultSerializationOn(boolean defaultSerializationOn);

} // IGlobalParams
