/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.csv.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.csv.CsvDocumentRoot;
import org.jboss.tools.smooks.model.csv.CsvFactory;
import org.jboss.tools.smooks.model.csv.CsvPackage;
import org.jboss.tools.smooks.model.csv.CsvReader;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CsvFactoryImpl extends EFactoryImpl implements CsvFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CsvFactory init() {
		try {
			CsvFactory theCsvFactory = (CsvFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/csv-1.1.xsd");  //$NON-NLS-1$
			if (theCsvFactory != null) {
				return theCsvFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CsvFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CsvFactoryImpl() {
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
			case CsvPackage.CSV_DOCUMENT_ROOT: return createCsvDocumentRoot();
			case CsvPackage.CSV_READER: return createCsvReader();
			default:
				throw new IllegalArgumentException(Messages.CsvFactoryImpl_Error_Invalid_Class_Classifier + eClass.getName() + Messages.CsvFactoryImpl_Error_Invalid_Classifier);
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
			case CsvPackage.CHAR:
				return createCharFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(Messages.CsvFactoryImpl_Error_Datatype_Invalid + eDataType.getName() + Messages.CsvFactoryImpl_Error_Invalid_Classifier);
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
			case CsvPackage.CHAR:
				return convertCharToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(Messages.CsvFactoryImpl_Error_Datatype_Invalid + eDataType.getName() + Messages.CsvFactoryImpl_Error_Invalid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CsvDocumentRoot createCsvDocumentRoot() {
		CsvDocumentRootImpl csvDocumentRoot = new CsvDocumentRootImpl();
		return csvDocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CsvReader createCsvReader() {
		CsvReaderImpl csvReader = new CsvReaderImpl();
		return csvReader;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createCharFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertCharToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CsvPackage getCsvPackage() {
		return (CsvPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CsvPackage getPackage() {
		return CsvPackage.eINSTANCE;
	}

} //CsvFactoryImpl
