/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.core;

import org.eclipse.emf.ecore.EClass;

import org.milyn.StreamFilterType;
import org.milyn.delivery.Filter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Global Params</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class GlobalParams extends Params implements IGlobalParams {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GlobalParams() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ICorePackage.Literals.GLOBAL_PARAMS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public IGlobalParams setFilterType(StreamFilterType filterType) {
		setParam(Filter.STREAM_FILTER_TYPE, filterType.toString());		
		return this;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public StreamFilterType getFilterType() {
		String filterType = getParamValue(Filter.STREAM_FILTER_TYPE);
		
		if(filterType == null) {
			return null;
		} 
		
		try {
			return StreamFilterType.valueOf(filterType);
		} catch(Exception e) {
			return null;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public IGlobalParams setDefaultSerializationOn(boolean defaultSerializationOn) {
		setParam(Filter.DEFAULT_SERIALIZATION_ON, Boolean.toString(defaultSerializationOn));		
		return this;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean isDefaultSerializationOn() {
		String defaultSerializationOn = getParamValue(Filter.DEFAULT_SERIALIZATION_ON);
		
		if(defaultSerializationOn == null) {
			return true;
		} 
		
		try {
			// On, unless it's explicitly turned off...
			return !defaultSerializationOn.equals("false");
		} catch(Exception e) {
			return true;
		}
	}
} //GlobalParams
