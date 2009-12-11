/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.dbrouting.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.dbrouting.DbroutingFactory;
import org.jboss.tools.smooks.model.dbrouting.DbroutingPackage;
import org.jboss.tools.smooks.model.dbrouting.DocumentRoot;
import org.jboss.tools.smooks.model.dbrouting.Executor;
import org.jboss.tools.smooks.model.dbrouting.ResultSet;
import org.jboss.tools.smooks.model.dbrouting.ResultSetRowSelector;
import org.jboss.tools.smooks.model.dbrouting.ResultSetScopeType;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DbroutingFactoryImpl extends EFactoryImpl implements DbroutingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DbroutingFactory init() {
		try {
			DbroutingFactory theDbroutingFactory = (DbroutingFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/db-routing-1.1.xsd");  //$NON-NLS-1$
			if (theDbroutingFactory != null) {
				return theDbroutingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DbroutingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DbroutingFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DbroutingPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case DbroutingPackage.EXECUTOR: return createExecutor();
			case DbroutingPackage.RESULT_SET: return createResultSet();
			case DbroutingPackage.RESULT_SET_ROW_SELECTOR: return createResultSetRowSelector();
			default:
				throw new IllegalArgumentException(Messages.DbroutingFactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.DbroutingFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case DbroutingPackage.RESULT_SET_SCOPE_TYPE:
				return createResultSetScopeTypeFromString(eDataType, initialValue);
			case DbroutingPackage.RESULT_SET_SCOPE_TYPE_OBJECT:
				return createResultSetScopeTypeObjectFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(Messages.DbroutingFactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.DbroutingFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case DbroutingPackage.RESULT_SET_SCOPE_TYPE:
				return convertResultSetScopeTypeToString(eDataType, instanceValue);
			case DbroutingPackage.RESULT_SET_SCOPE_TYPE_OBJECT:
				return convertResultSetScopeTypeObjectToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(Messages.DbroutingFactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.DbroutingFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DocumentRoot createDocumentRoot() {
		DocumentRootImpl documentRoot = new DocumentRootImpl();
		return documentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Executor createExecutor() {
		ExecutorImpl executor = new ExecutorImpl();
		return executor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultSet createResultSet() {
		ResultSetImpl resultSet = new ResultSetImpl();
		return resultSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultSetRowSelector createResultSetRowSelector() {
		ResultSetRowSelectorImpl resultSetRowSelector = new ResultSetRowSelectorImpl();
		return resultSetRowSelector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultSetScopeType createResultSetScopeTypeFromString(EDataType eDataType, String initialValue) {
		ResultSetScopeType result = ResultSetScopeType.get(initialValue);
		if (result == null) throw new IllegalArgumentException(Messages.DbroutingFactoryImpl_Error_Value_Not_Valid + initialValue + Messages.DbroutingFactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + Messages.DbroutingFactoryImpl_9);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertResultSetScopeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResultSetScopeType createResultSetScopeTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createResultSetScopeTypeFromString(DbroutingPackage.Literals.RESULT_SET_SCOPE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertResultSetScopeTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertResultSetScopeTypeToString(DbroutingPackage.Literals.RESULT_SET_SCOPE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DbroutingPackage getDbroutingPackage() {
		return (DbroutingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DbroutingPackage getPackage() {
		return DbroutingPackage.eINSTANCE;
	}

} //DbroutingFactoryImpl
