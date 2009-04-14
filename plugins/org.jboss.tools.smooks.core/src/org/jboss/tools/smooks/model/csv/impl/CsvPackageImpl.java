/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.csv.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.common.CommonPackage;
import org.jboss.tools.smooks.model.common.impl.CommonPackageImpl;
import org.jboss.tools.smooks.model.csv.CsvFactory;
import org.jboss.tools.smooks.model.csv.CsvPackage;
import org.jboss.tools.smooks.model.csv.DocumentRoot;
import org.jboss.tools.smooks.model.csv.Reader;
import org.jboss.tools.smooks.model.csv.util.CsvValidator;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.impl.SmooksPackageImpl;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CsvPackageImpl extends EPackageImpl implements CsvPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass documentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass readerEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType charEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.jboss.tools.smooks.model.csv.CsvPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private CsvPackageImpl() {
		super(eNS_URI, CsvFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static CsvPackage init() {
		if (isInited) return (CsvPackage)EPackage.Registry.INSTANCE.getEPackage(CsvPackage.eNS_URI);

		// Obtain or create and register package
		CsvPackageImpl theCsvPackage = (CsvPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof CsvPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new CsvPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		SmooksPackageImpl theSmooksPackage = (SmooksPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) instanceof SmooksPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) : SmooksPackage.eINSTANCE);
		CommonPackageImpl theCommonPackage = (CommonPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) instanceof CommonPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) : CommonPackage.eINSTANCE);

		// Create package meta-data objects
		theCsvPackage.createPackageContents();
		theSmooksPackage.createPackageContents();
		theCommonPackage.createPackageContents();

		// Initialize created meta-data
		theCsvPackage.initializePackageContents();
		theSmooksPackage.initializePackageContents();
		theCommonPackage.initializePackageContents();

		// Register package validator
		EValidator.Registry.INSTANCE.put
			(theCsvPackage, 
			 new EValidator.Descriptor() {
				 public EValidator getEValidator() {
					 return CsvValidator.INSTANCE;
				 }
			 });

		// Mark meta-data to indicate it can't be changed
		theCsvPackage.freeze();

		return theCsvPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDocumentRoot() {
		return documentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Mixed() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_XMLNSPrefixMap() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_XSISchemaLocation() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Reader() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getReader() {
		return readerEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReader_Encoding() {
		return (EAttribute)readerEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReader_Fields() {
		return (EAttribute)readerEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReader_Quote() {
		return (EAttribute)readerEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReader_Separator() {
		return (EAttribute)readerEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getReader_SkipLines() {
		return (EAttribute)readerEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getChar() {
		return charEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CsvFactory getCsvFactory() {
		return (CsvFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__READER);

		readerEClass = createEClass(READER);
		createEAttribute(readerEClass, READER__ENCODING);
		createEAttribute(readerEClass, READER__FIELDS);
		createEAttribute(readerEClass, READER__QUOTE);
		createEAttribute(readerEClass, READER__SEPARATOR);
		createEAttribute(readerEClass, READER__SKIP_LINES);

		// Create data types
		charEDataType = createEDataType(CHAR);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		SmooksPackage theSmooksPackage = (SmooksPackage)EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		readerEClass.getESuperTypes().add(theSmooksPackage.getAbstractReader());

		// Initialize classes and features; add operations and parameters
		initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Reader(), this.getReader(), null, "reader", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(readerEClass, Reader.class, "CSV Reader", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getReader_Encoding(), theXMLTypePackage.getString(), "encoding", "UTF-8", 0, 1, Reader.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReader_Fields(), theXMLTypePackage.getString(), "fields", null, 0, 1, Reader.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReader_Quote(), this.getChar(), "quote", "\"", 0, 1, Reader.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReader_Separator(), this.getChar(), "separator", ",", 0, 1, Reader.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getReader_SkipLines(), theXMLTypePackage.getInteger(), "skipLines", "0", 0, 1, Reader.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(charEDataType, String.class, "Char", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http:///org/eclipse/emf/ecore/util/ExtendedMetaData
		createExtendedMetaDataAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createExtendedMetaDataAnnotations() {
		String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";			
		addAnnotation
		  (charEDataType, 
		   source, 
		   new String[] {
			 "name", "char",
			 "baseType", "http://www.eclipse.org/emf/2003/XMLType#string",
			 "length", "1"
		   });		
		addAnnotation
		  (documentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getDocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getDocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });			
		addAnnotation
		  (getDocumentRoot_Reader(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "reader",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-reader"
		   });			
		addAnnotation
		  (readerEClass, 
		   source, 
		   new String[] {
			 "name", "reader",
			 "kind", "empty"
		   });			
		addAnnotation
		  (getReader_Encoding(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "encoding"
		   });			
		addAnnotation
		  (getReader_Fields(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "fields"
		   });			
		addAnnotation
		  (getReader_Quote(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "quote"
		   });			
		addAnnotation
		  (getReader_Separator(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "separator"
		   });			
		addAnnotation
		  (getReader_SkipLines(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "skipLines"
		   });
	}

} //CsvPackageImpl
