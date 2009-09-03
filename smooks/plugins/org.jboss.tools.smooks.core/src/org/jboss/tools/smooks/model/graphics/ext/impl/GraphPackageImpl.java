/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.graphics.ext.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.jboss.tools.smooks.model.graphics.ext.ConnectionType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphExtensionDocumentRoot;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.GraphPackage;
import org.jboss.tools.smooks.model.graphics.ext.FigureType;
import org.jboss.tools.smooks.model.graphics.ext.GraphType;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.ParamType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GraphPackageImpl extends EPackageImpl implements GraphPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass connectionTypeEClass = null;

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
	private EClass figureTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass graphTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inputTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass paramTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass smooksGraphicsExtTypeEClass = null;

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
	 * @see org.jboss.tools.smooks.model.graphics.ext.GraphPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private GraphPackageImpl() {
		super(eNS_URI, GraphFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link GraphPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static GraphPackage init() {
		if (isInited) return (GraphPackage)EPackage.Registry.INSTANCE.getEPackage(GraphPackage.eNS_URI);

		// Obtain or create and register package
		GraphPackageImpl theExtPackage = (GraphPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof GraphPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new GraphPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theExtPackage.createPackageContents();

		// Initialize created meta-data
		theExtPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theExtPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(GraphPackage.eNS_URI, theExtPackage);
		return theExtPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConnectionType() {
		return connectionTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConnectionType_Source() {
		return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConnectionType_Target() {
		return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getConnectionType_Id() {
		return (EAttribute)connectionTypeEClass.getEStructuralFeatures().get(2);
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
	public EReference getDocumentRoot_Connection() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Figure() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Graph() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Input() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_Param() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDocumentRoot_SmooksGraphicsExt() {
		return (EReference)documentRootEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Source() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDocumentRoot_Target() {
		return (EAttribute)documentRootEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFigureType() {
		return figureTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFigureType_Height() {
		return (EAttribute)figureTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFigureType_Id() {
		return (EAttribute)figureTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFigureType_ParentFigureId() {
		return (EAttribute)figureTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFigureType_Width() {
		return (EAttribute)figureTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFigureType_X() {
		return (EAttribute)figureTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFigureType_Y() {
		return (EAttribute)figureTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getGraphType() {
		return graphTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGraphType_Figure() {
		return (EReference)graphTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getGraphType_Connection() {
		return (EReference)graphTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInputType() {
		return inputTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInputType_Param() {
		return (EReference)inputTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInputType_Type() {
		return (EAttribute)inputTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParamType() {
		return paramTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParamType_Value() {
		return (EAttribute)paramTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParamType_Name() {
		return (EAttribute)paramTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParamType_Type() {
		return (EAttribute)paramTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSmooksGraphicsExtType() {
		return smooksGraphicsExtTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooksGraphicsExtType_Input() {
		return (EReference)smooksGraphicsExtTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSmooksGraphicsExtType_Graph() {
		return (EReference)smooksGraphicsExtTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooksGraphicsExtType_Author() {
		return (EAttribute)smooksGraphicsExtTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooksGraphicsExtType_InputType() {
		return (EAttribute)smooksGraphicsExtTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooksGraphicsExtType_Name() {
		return (EAttribute)smooksGraphicsExtTypeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooksGraphicsExtType_OutputType() {
		return (EAttribute)smooksGraphicsExtTypeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSmooksGraphicsExtType_PlatformVersion() {
		return (EAttribute)smooksGraphicsExtTypeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GraphFactory getExtFactory() {
		return (GraphFactory)getEFactoryInstance();
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
		connectionTypeEClass = createEClass(CONNECTION_TYPE);
		createEAttribute(connectionTypeEClass, CONNECTION_TYPE__SOURCE);
		createEAttribute(connectionTypeEClass, CONNECTION_TYPE__TARGET);
		createEAttribute(connectionTypeEClass, CONNECTION_TYPE__ID);

		documentRootEClass = createEClass(DOCUMENT_ROOT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__CONNECTION);
		createEReference(documentRootEClass, DOCUMENT_ROOT__FIGURE);
		createEReference(documentRootEClass, DOCUMENT_ROOT__GRAPH);
		createEReference(documentRootEClass, DOCUMENT_ROOT__INPUT);
		createEReference(documentRootEClass, DOCUMENT_ROOT__PARAM);
		createEReference(documentRootEClass, DOCUMENT_ROOT__SMOOKS_GRAPHICS_EXT);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__SOURCE);
		createEAttribute(documentRootEClass, DOCUMENT_ROOT__TARGET);

		figureTypeEClass = createEClass(FIGURE_TYPE);
		createEAttribute(figureTypeEClass, FIGURE_TYPE__HEIGHT);
		createEAttribute(figureTypeEClass, FIGURE_TYPE__ID);
		createEAttribute(figureTypeEClass, FIGURE_TYPE__PARENT_FIGURE_ID);
		createEAttribute(figureTypeEClass, FIGURE_TYPE__WIDTH);
		createEAttribute(figureTypeEClass, FIGURE_TYPE__X);
		createEAttribute(figureTypeEClass, FIGURE_TYPE__Y);

		graphTypeEClass = createEClass(GRAPH_TYPE);
		createEReference(graphTypeEClass, GRAPH_TYPE__FIGURE);
		createEReference(graphTypeEClass, GRAPH_TYPE__CONNECTION);

		inputTypeEClass = createEClass(INPUT_TYPE);
		createEReference(inputTypeEClass, INPUT_TYPE__PARAM);
		createEAttribute(inputTypeEClass, INPUT_TYPE__TYPE);

		paramTypeEClass = createEClass(PARAM_TYPE);
		createEAttribute(paramTypeEClass, PARAM_TYPE__VALUE);
		createEAttribute(paramTypeEClass, PARAM_TYPE__NAME);
		createEAttribute(paramTypeEClass, PARAM_TYPE__TYPE);

		smooksGraphicsExtTypeEClass = createEClass(SMOOKS_GRAPHICS_EXT_TYPE);
		createEReference(smooksGraphicsExtTypeEClass, SMOOKS_GRAPHICS_EXT_TYPE__INPUT);
		createEReference(smooksGraphicsExtTypeEClass, SMOOKS_GRAPHICS_EXT_TYPE__GRAPH);
		createEAttribute(smooksGraphicsExtTypeEClass, SMOOKS_GRAPHICS_EXT_TYPE__AUTHOR);
		createEAttribute(smooksGraphicsExtTypeEClass, SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE);
		createEAttribute(smooksGraphicsExtTypeEClass, SMOOKS_GRAPHICS_EXT_TYPE__NAME);
		createEAttribute(smooksGraphicsExtTypeEClass, SMOOKS_GRAPHICS_EXT_TYPE__OUTPUT_TYPE);
		createEAttribute(smooksGraphicsExtTypeEClass, SMOOKS_GRAPHICS_EXT_TYPE__PLATFORM_VERSION);
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
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(connectionTypeEClass, ConnectionType.class, "ConnectionType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getConnectionType_Source(), theXMLTypePackage.getString(), "source", null, 1, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConnectionType_Target(), theXMLTypePackage.getString(), "target", null, 1, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getConnectionType_Id(), theXMLTypePackage.getString(), "id", null, 0, 1, ConnectionType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(documentRootEClass, SmooksGraphExtensionDocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Connection(), this.getConnectionType(), null, "connection", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Figure(), this.getFigureType(), null, "figure", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Graph(), this.getGraphType(), null, "graph", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Input(), this.getInputType(), null, "input", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_Param(), this.getParamType(), null, "param", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getDocumentRoot_SmooksGraphicsExt(), this.getSmooksGraphicsExtType(), null, "smooksGraphicsExt", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Source(), theXMLTypePackage.getString(), "source", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getDocumentRoot_Target(), theXMLTypePackage.getString(), "target", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(figureTypeEClass, FigureType.class, "FigureType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFigureType_Height(), theXMLTypePackage.getString(), "height", null, 0, 1, FigureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFigureType_Id(), theXMLTypePackage.getString(), "id", null, 0, 1, FigureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFigureType_ParentFigureId(), theXMLTypePackage.getString(), "parentFigureId", null, 0, 1, FigureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFigureType_Width(), theXMLTypePackage.getString(), "width", null, 0, 1, FigureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFigureType_X(), theXMLTypePackage.getString(), "x", null, 0, 1, FigureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFigureType_Y(), theXMLTypePackage.getString(), "y", null, 0, 1, FigureType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(graphTypeEClass, GraphType.class, "GraphType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGraphType_Figure(), this.getFigureType(), null, "figure", null, 0, -1, GraphType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getGraphType_Connection(), this.getConnectionType(), null, "connection", null, 0, -1, GraphType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(inputTypeEClass, InputType.class, "InputType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInputType_Param(), this.getParamType(), null, "param", null, 0, -1, InputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInputType_Type(), theXMLTypePackage.getString(), "type", null, 0, 1, InputType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(paramTypeEClass, ParamType.class, "ParamType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParamType_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ParamType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParamType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ParamType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getParamType_Type(), theXMLTypePackage.getString(), "type", null, 0, 1, ParamType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(smooksGraphicsExtTypeEClass, SmooksGraphicsExtType.class, "SmooksGraphicsExtType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSmooksGraphicsExtType_Input(), this.getInputType(), null, "input", null, 0, -1, SmooksGraphicsExtType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSmooksGraphicsExtType_Graph(), this.getGraphType(), null, "graph", null, 0, 1, SmooksGraphicsExtType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSmooksGraphicsExtType_Author(), theXMLTypePackage.getString(), "author", null, 0, 1, SmooksGraphicsExtType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSmooksGraphicsExtType_InputType(), theXMLTypePackage.getString(), "inputType", null, 0, 1, SmooksGraphicsExtType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSmooksGraphicsExtType_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, SmooksGraphicsExtType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSmooksGraphicsExtType_OutputType(), theXMLTypePackage.getString(), "outputType", null, 0, 1, SmooksGraphicsExtType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSmooksGraphicsExtType_PlatformVersion(), theXMLTypePackage.getString(), "platformVersion", null, 0, 1, SmooksGraphicsExtType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		  (connectionTypeEClass, 
		   source, 
		   new String[] {
			 "name", "connection_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getConnectionType_Source(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "source",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getConnectionType_Target(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "target",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getConnectionType_Id(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "id"
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
		  (getDocumentRoot_Connection(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "connection",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Figure(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "figure",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Graph(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "graph",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Input(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "input",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Param(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "param",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_SmooksGraphicsExt(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "smooks-graphics-ext",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Source(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "source",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getDocumentRoot_Target(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "target",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (figureTypeEClass, 
		   source, 
		   new String[] {
			 "name", "figure_._type",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getFigureType_Height(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "height"
		   });		
		addAnnotation
		  (getFigureType_Id(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "id"
		   });		
		addAnnotation
		  (getFigureType_ParentFigureId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "parentFigureId"
		   });		
		addAnnotation
		  (getFigureType_Width(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "width"
		   });		
		addAnnotation
		  (getFigureType_X(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "x"
		   });		
		addAnnotation
		  (getFigureType_Y(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "y"
		   });		
		addAnnotation
		  (graphTypeEClass, 
		   source, 
		   new String[] {
			 "name", "graph_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getGraphType_Figure(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "figure",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getGraphType_Connection(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "connection",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (inputTypeEClass, 
		   source, 
		   new String[] {
			 "name", "input_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getInputType_Param(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "param",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getInputType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type"
		   });		
		addAnnotation
		  (paramTypeEClass, 
		   source, 
		   new String[] {
			 "name", "param_._type",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getParamType_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getParamType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getParamType_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type"
		   });		
		addAnnotation
		  (smooksGraphicsExtTypeEClass, 
		   source, 
		   new String[] {
			 "name", "smooks-graphics-ext_._type",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getSmooksGraphicsExtType_Input(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "input",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooksGraphicsExtType_Graph(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "graph",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getSmooksGraphicsExtType_Author(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "author"
		   });		
		addAnnotation
		  (getSmooksGraphicsExtType_InputType(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "inputType"
		   });		
		addAnnotation
		  (getSmooksGraphicsExtType_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getSmooksGraphicsExtType_OutputType(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "outputType"
		   });		
		addAnnotation
		  (getSmooksGraphicsExtType_PlatformVersion(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "platformVersion"
		   });
	}

} //ExtPackageImpl
