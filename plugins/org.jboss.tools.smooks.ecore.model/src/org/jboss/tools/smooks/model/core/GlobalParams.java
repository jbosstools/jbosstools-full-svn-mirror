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
	 * @generated
	 */
	public IGlobalParams setFilterType(StreamFilterType filterType) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public StreamFilterType getFilterType() {
		String filterType = getParam(Filter.STREAM_FILTER_TYPE);
		
		if(filterType == null) {
			return null;
		} 
		
		try {
			return StreamFilterType.valueOf(filterType);
		} catch(Exception e) {
			return null;
		}
	}

} //GlobalParams
