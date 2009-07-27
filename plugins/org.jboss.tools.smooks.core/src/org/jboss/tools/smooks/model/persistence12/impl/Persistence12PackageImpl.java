/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.impl;



import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.common.CommonPackage;
import org.jboss.tools.smooks.model.common.impl.CommonPackageImpl;
import org.jboss.tools.smooks.model.persistence12.DecoderParameter;
import org.jboss.tools.smooks.model.persistence12.Deleter;
import org.jboss.tools.smooks.model.persistence12.ExpressionParameter;
import org.jboss.tools.smooks.model.persistence12.Flusher;
import org.jboss.tools.smooks.model.persistence12.Inserter;
import org.jboss.tools.smooks.model.persistence12.Locator;
import org.jboss.tools.smooks.model.persistence12.OnNoResult;
import org.jboss.tools.smooks.model.persistence12.ParameterType;
import org.jboss.tools.smooks.model.persistence12.Parameters;
import org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot;
import org.jboss.tools.smooks.model.persistence12.Persistence12Factory;
import org.jboss.tools.smooks.model.persistence12.Persistence12Package;
import org.jboss.tools.smooks.model.persistence12.Updater;
import org.jboss.tools.smooks.model.persistence12.ValueParameter;
import org.jboss.tools.smooks.model.persistence12.WiringParameter;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.impl.SmooksPackageImpl;




/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Persistence12PackageImpl extends EPackageImpl implements Persistence12Package {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass decoderParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass deleterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass persistence12DocumentRootEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass expressionParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass flusherEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass inserterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass locatorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass parametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass updaterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass valueParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass wiringParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum onNoResultEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum parameterTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType onNoResultObjectEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType parameterTypeObjectEDataType = null;

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
	 * @see org.jboss.tools.smooks.model.persistence12.Persistence12Package#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private Persistence12PackageImpl() {
		super(eNS_URI, Persistence12Factory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link Persistence12Package#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static Persistence12Package init() {
		if (isInited) return (Persistence12Package)EPackage.Registry.INSTANCE.getEPackage(Persistence12Package.eNS_URI);

		// Obtain or create and register package
		Persistence12PackageImpl thePersistence12Package = (Persistence12PackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof Persistence12PackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new Persistence12PackageImpl());

		isInited = true;

		// Initialize simple dependencies
		XMLTypePackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		CommonPackageImpl theCommonPackage = (CommonPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) instanceof CommonPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI) : CommonPackage.eINSTANCE);
		SmooksPackageImpl theSmooksPackage = (SmooksPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) instanceof SmooksPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI) : SmooksPackage.eINSTANCE);

		// Create package meta-data objects
		thePersistence12Package.createPackageContents();
		theCommonPackage.createPackageContents();
		theSmooksPackage.createPackageContents();

		// Initialize created meta-data
		thePersistence12Package.initializePackageContents();
		theCommonPackage.initializePackageContents();
		theSmooksPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePersistence12Package.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(Persistence12Package.eNS_URI, thePersistence12Package);
		return thePersistence12Package;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDecoderParameter() {
		return decoderParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDecoderParameter_Value() {
		return (EAttribute)decoderParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDecoderParameter_Name() {
		return (EAttribute)decoderParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDeleter() {
		return deleterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleter_BeanId() {
		return (EAttribute)deleterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleter_Dao() {
		return (EAttribute)deleterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleter_DeleteBefore() {
		return (EAttribute)deleterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleter_DeletedBeanId() {
		return (EAttribute)deleterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleter_DeleteOnElement() {
		return (EAttribute)deleterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleter_DeleteOnElementNS() {
		return (EAttribute)deleterEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDeleter_Name() {
		return (EAttribute)deleterEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPersistence12DocumentRoot() {
		return persistence12DocumentRootEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPersistence12DocumentRoot_Mixed() {
		return (EAttribute)persistence12DocumentRootEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPersistence12DocumentRoot_XMLNSPrefixMap() {
		return (EReference)persistence12DocumentRootEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPersistence12DocumentRoot_XSISchemaLocation() {
		return (EReference)persistence12DocumentRootEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPersistence12DocumentRoot_Deleter() {
		return (EReference)persistence12DocumentRootEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPersistence12DocumentRoot_Flusher() {
		return (EReference)persistence12DocumentRootEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPersistence12DocumentRoot_Inserter() {
		return (EReference)persistence12DocumentRootEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPersistence12DocumentRoot_Locator() {
		return (EReference)persistence12DocumentRootEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPersistence12DocumentRoot_Updater() {
		return (EReference)persistence12DocumentRootEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getExpressionParameter() {
		return expressionParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionParameter_Value() {
		return (EAttribute)expressionParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionParameter_ExecOnElement() {
		return (EAttribute)expressionParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionParameter_ExecOnElementNS() {
		return (EAttribute)expressionParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getExpressionParameter_Name() {
		return (EAttribute)expressionParameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFlusher() {
		return flusherEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFlusher_Dao() {
		return (EAttribute)flusherEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFlusher_FlushBefore() {
		return (EAttribute)flusherEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFlusher_FlushOnElement() {
		return (EAttribute)flusherEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFlusher_FlushOnElementNS() {
		return (EAttribute)flusherEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInserter() {
		return inserterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInserter_BeanId() {
		return (EAttribute)inserterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInserter_Dao() {
		return (EAttribute)inserterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInserter_InsertBefore() {
		return (EAttribute)inserterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInserter_InsertedBeanId() {
		return (EAttribute)inserterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInserter_InsertOnElement() {
		return (EAttribute)inserterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInserter_InsertOnElementNS() {
		return (EAttribute)inserterEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInserter_Name() {
		return (EAttribute)inserterEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getLocator() {
		return locatorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocator_Query() {
		return (EAttribute)locatorEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getLocator_Params() {
		return (EReference)locatorEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocator_BeanId() {
		return (EAttribute)locatorEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocator_Dao() {
		return (EAttribute)locatorEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocator_Lookup() {
		return (EAttribute)locatorEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocator_LookupOnElement() {
		return (EAttribute)locatorEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocator_LookupOnElementNS() {
		return (EAttribute)locatorEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocator_OnNoResult() {
		return (EAttribute)locatorEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getLocator_UniqueResult() {
		return (EAttribute)locatorEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getParameters() {
		return parametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameters_Group() {
		return (EAttribute)parametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameters_Value() {
		return (EReference)parametersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameters_Wiring() {
		return (EReference)parametersEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getParameters_Expression() {
		return (EReference)parametersEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getParameters_Type() {
		return (EAttribute)parametersEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUpdater() {
		return updaterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdater_BeanId() {
		return (EAttribute)updaterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdater_Dao() {
		return (EAttribute)updaterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdater_Name() {
		return (EAttribute)updaterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdater_UpdateBefore() {
		return (EAttribute)updaterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdater_UpdatedBeanId() {
		return (EAttribute)updaterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdater_UpdateOnElement() {
		return (EAttribute)updaterEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUpdater_UpdateOnElementNS() {
		return (EAttribute)updaterEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getValueParameter() {
		return valueParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getValueParameter_DecodeParam() {
		return (EReference)valueParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueParameter_Data() {
		return (EAttribute)valueParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueParameter_DataNS() {
		return (EAttribute)valueParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueParameter_Decoder() {
		return (EAttribute)valueParameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueParameter_Default() {
		return (EAttribute)valueParameterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueParameter_Index() {
		return (EAttribute)valueParameterEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getValueParameter_Name() {
		return (EAttribute)valueParameterEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWiringParameter() {
		return wiringParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiringParameter_BeanIdRef() {
		return (EAttribute)wiringParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiringParameter_Name() {
		return (EAttribute)wiringParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiringParameter_WireOnElement() {
		return (EAttribute)wiringParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWiringParameter_WireOnElementNS() {
		return (EAttribute)wiringParameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getOnNoResult() {
		return onNoResultEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getParameterType() {
		return parameterTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getOnNoResultObject() {
		return onNoResultObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getParameterTypeObject() {
		return parameterTypeObjectEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Persistence12Factory getPersistence12Factory() {
		return (Persistence12Factory)getEFactoryInstance();
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
		decoderParameterEClass = createEClass(DECODER_PARAMETER);
		createEAttribute(decoderParameterEClass, DECODER_PARAMETER__VALUE);
		createEAttribute(decoderParameterEClass, DECODER_PARAMETER__NAME);

		deleterEClass = createEClass(DELETER);
		createEAttribute(deleterEClass, DELETER__BEAN_ID);
		createEAttribute(deleterEClass, DELETER__DAO);
		createEAttribute(deleterEClass, DELETER__DELETE_BEFORE);
		createEAttribute(deleterEClass, DELETER__DELETED_BEAN_ID);
		createEAttribute(deleterEClass, DELETER__DELETE_ON_ELEMENT);
		createEAttribute(deleterEClass, DELETER__DELETE_ON_ELEMENT_NS);
		createEAttribute(deleterEClass, DELETER__NAME);

		persistence12DocumentRootEClass = createEClass(PERSISTENCE12_DOCUMENT_ROOT);
		createEAttribute(persistence12DocumentRootEClass, PERSISTENCE12_DOCUMENT_ROOT__MIXED);
		createEReference(persistence12DocumentRootEClass, PERSISTENCE12_DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
		createEReference(persistence12DocumentRootEClass, PERSISTENCE12_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
		createEReference(persistence12DocumentRootEClass, PERSISTENCE12_DOCUMENT_ROOT__DELETER);
		createEReference(persistence12DocumentRootEClass, PERSISTENCE12_DOCUMENT_ROOT__FLUSHER);
		createEReference(persistence12DocumentRootEClass, PERSISTENCE12_DOCUMENT_ROOT__INSERTER);
		createEReference(persistence12DocumentRootEClass, PERSISTENCE12_DOCUMENT_ROOT__LOCATOR);
		createEReference(persistence12DocumentRootEClass, PERSISTENCE12_DOCUMENT_ROOT__UPDATER);

		expressionParameterEClass = createEClass(EXPRESSION_PARAMETER);
		createEAttribute(expressionParameterEClass, EXPRESSION_PARAMETER__VALUE);
		createEAttribute(expressionParameterEClass, EXPRESSION_PARAMETER__EXEC_ON_ELEMENT);
		createEAttribute(expressionParameterEClass, EXPRESSION_PARAMETER__EXEC_ON_ELEMENT_NS);
		createEAttribute(expressionParameterEClass, EXPRESSION_PARAMETER__NAME);

		flusherEClass = createEClass(FLUSHER);
		createEAttribute(flusherEClass, FLUSHER__DAO);
		createEAttribute(flusherEClass, FLUSHER__FLUSH_BEFORE);
		createEAttribute(flusherEClass, FLUSHER__FLUSH_ON_ELEMENT);
		createEAttribute(flusherEClass, FLUSHER__FLUSH_ON_ELEMENT_NS);

		inserterEClass = createEClass(INSERTER);
		createEAttribute(inserterEClass, INSERTER__BEAN_ID);
		createEAttribute(inserterEClass, INSERTER__DAO);
		createEAttribute(inserterEClass, INSERTER__INSERT_BEFORE);
		createEAttribute(inserterEClass, INSERTER__INSERTED_BEAN_ID);
		createEAttribute(inserterEClass, INSERTER__INSERT_ON_ELEMENT);
		createEAttribute(inserterEClass, INSERTER__INSERT_ON_ELEMENT_NS);
		createEAttribute(inserterEClass, INSERTER__NAME);

		locatorEClass = createEClass(LOCATOR);
		createEAttribute(locatorEClass, LOCATOR__QUERY);
		createEReference(locatorEClass, LOCATOR__PARAMS);
		createEAttribute(locatorEClass, LOCATOR__BEAN_ID);
		createEAttribute(locatorEClass, LOCATOR__DAO);
		createEAttribute(locatorEClass, LOCATOR__LOOKUP);
		createEAttribute(locatorEClass, LOCATOR__LOOKUP_ON_ELEMENT);
		createEAttribute(locatorEClass, LOCATOR__LOOKUP_ON_ELEMENT_NS);
		createEAttribute(locatorEClass, LOCATOR__ON_NO_RESULT);
		createEAttribute(locatorEClass, LOCATOR__UNIQUE_RESULT);

		parametersEClass = createEClass(PARAMETERS);
		createEAttribute(parametersEClass, PARAMETERS__GROUP);
		createEReference(parametersEClass, PARAMETERS__VALUE);
		createEReference(parametersEClass, PARAMETERS__WIRING);
		createEReference(parametersEClass, PARAMETERS__EXPRESSION);
		createEAttribute(parametersEClass, PARAMETERS__TYPE);

		updaterEClass = createEClass(UPDATER);
		createEAttribute(updaterEClass, UPDATER__BEAN_ID);
		createEAttribute(updaterEClass, UPDATER__DAO);
		createEAttribute(updaterEClass, UPDATER__NAME);
		createEAttribute(updaterEClass, UPDATER__UPDATE_BEFORE);
		createEAttribute(updaterEClass, UPDATER__UPDATED_BEAN_ID);
		createEAttribute(updaterEClass, UPDATER__UPDATE_ON_ELEMENT);
		createEAttribute(updaterEClass, UPDATER__UPDATE_ON_ELEMENT_NS);

		valueParameterEClass = createEClass(VALUE_PARAMETER);
		createEReference(valueParameterEClass, VALUE_PARAMETER__DECODE_PARAM);
		createEAttribute(valueParameterEClass, VALUE_PARAMETER__DATA);
		createEAttribute(valueParameterEClass, VALUE_PARAMETER__DATA_NS);
		createEAttribute(valueParameterEClass, VALUE_PARAMETER__DECODER);
		createEAttribute(valueParameterEClass, VALUE_PARAMETER__DEFAULT);
		createEAttribute(valueParameterEClass, VALUE_PARAMETER__INDEX);
		createEAttribute(valueParameterEClass, VALUE_PARAMETER__NAME);

		wiringParameterEClass = createEClass(WIRING_PARAMETER);
		createEAttribute(wiringParameterEClass, WIRING_PARAMETER__BEAN_ID_REF);
		createEAttribute(wiringParameterEClass, WIRING_PARAMETER__NAME);
		createEAttribute(wiringParameterEClass, WIRING_PARAMETER__WIRE_ON_ELEMENT);
		createEAttribute(wiringParameterEClass, WIRING_PARAMETER__WIRE_ON_ELEMENT_NS);

		// Create enums
		onNoResultEEnum = createEEnum(ON_NO_RESULT);
		parameterTypeEEnum = createEEnum(PARAMETER_TYPE);

		// Create data types
		onNoResultObjectEDataType = createEDataType(ON_NO_RESULT_OBJECT);
		parameterTypeObjectEDataType = createEDataType(PARAMETER_TYPE_OBJECT);
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
		CommonPackage theCommonPackage = (CommonPackage)EPackage.Registry.INSTANCE.getEPackage(CommonPackage.eNS_URI);
		XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);
		SmooksPackage theSmooksPackage = (SmooksPackage)EPackage.Registry.INSTANCE.getEPackage(SmooksPackage.eNS_URI);

		// Add supertypes to classes
		decoderParameterEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		deleterEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());
		expressionParameterEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		flusherEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());
		inserterEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());
		locatorEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());
		parametersEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		updaterEClass.getESuperTypes().add(theSmooksPackage.getElementVisitor());
		valueParameterEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());
		wiringParameterEClass.getESuperTypes().add(theCommonPackage.getAbstractAnyType());

		// Initialize classes and features; add operations and parameters
		initEClass(decoderParameterEClass, DecoderParameter.class, "DecoderParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDecoderParameter_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, DecoderParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDecoderParameter_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, DecoderParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(deleterEClass, Deleter.class, "Deleter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDeleter_BeanId(), theXMLTypePackage.getString(), "beanId", null, 1, 1, Deleter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeleter_Dao(), theXMLTypePackage.getString(), "dao", null, 0, 1, Deleter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeleter_DeleteBefore(), theXMLTypePackage.getBoolean(), "deleteBefore", "false", 0, 1, Deleter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeleter_DeletedBeanId(), theXMLTypePackage.getString(), "deletedBeanId", null, 0, 1, Deleter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeleter_DeleteOnElement(), theXMLTypePackage.getString(), "deleteOnElement", null, 1, 1, Deleter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeleter_DeleteOnElementNS(), theXMLTypePackage.getAnyURI(), "deleteOnElementNS", null, 0, 1, Deleter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDeleter_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Deleter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(persistence12DocumentRootEClass, Persistence12DocumentRoot.class, "Persistence12DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPersistence12DocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPersistence12DocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPersistence12DocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPersistence12DocumentRoot_Deleter(), this.getDeleter(), null, "deleter", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getPersistence12DocumentRoot_Flusher(), this.getFlusher(), null, "flusher", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getPersistence12DocumentRoot_Inserter(), this.getInserter(), null, "inserter", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getPersistence12DocumentRoot_Locator(), this.getLocator(), null, "locator", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getPersistence12DocumentRoot_Updater(), this.getUpdater(), null, "updater", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

		initEClass(expressionParameterEClass, ExpressionParameter.class, "ExpressionParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getExpressionParameter_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, ExpressionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpressionParameter_ExecOnElement(), theXMLTypePackage.getString(), "execOnElement", null, 0, 1, ExpressionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpressionParameter_ExecOnElementNS(), theXMLTypePackage.getString(), "execOnElementNS", null, 0, 1, ExpressionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getExpressionParameter_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ExpressionParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(flusherEClass, Flusher.class, "Flusher", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFlusher_Dao(), theXMLTypePackage.getString(), "dao", null, 0, 1, Flusher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFlusher_FlushBefore(), theXMLTypePackage.getBoolean(), "flushBefore", "false", 0, 1, Flusher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFlusher_FlushOnElement(), theXMLTypePackage.getString(), "flushOnElement", null, 1, 1, Flusher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFlusher_FlushOnElementNS(), theXMLTypePackage.getAnyURI(), "flushOnElementNS", null, 0, 1, Flusher.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(inserterEClass, Inserter.class, "Inserter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInserter_BeanId(), theXMLTypePackage.getString(), "beanId", null, 1, 1, Inserter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInserter_Dao(), theXMLTypePackage.getString(), "dao", null, 0, 1, Inserter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInserter_InsertBefore(), theXMLTypePackage.getBoolean(), "insertBefore", "false", 0, 1, Inserter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInserter_InsertedBeanId(), theXMLTypePackage.getString(), "insertedBeanId", null, 0, 1, Inserter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInserter_InsertOnElement(), theXMLTypePackage.getString(), "insertOnElement", null, 1, 1, Inserter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInserter_InsertOnElementNS(), theXMLTypePackage.getAnyURI(), "insertOnElementNS", null, 0, 1, Inserter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getInserter_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Inserter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(locatorEClass, Locator.class, "Locator", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLocator_Query(), theXMLTypePackage.getString(), "query", null, 0, 1, Locator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLocator_Params(), this.getParameters(), null, "params", null, 0, 1, Locator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocator_BeanId(), theXMLTypePackage.getString(), "beanId", null, 1, 1, Locator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocator_Dao(), theXMLTypePackage.getString(), "dao", null, 0, 1, Locator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocator_Lookup(), theXMLTypePackage.getString(), "lookup", null, 0, 1, Locator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocator_LookupOnElement(), theXMLTypePackage.getString(), "lookupOnElement", null, 1, 1, Locator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocator_LookupOnElementNS(), theXMLTypePackage.getAnyURI(), "lookupOnElementNS", null, 0, 1, Locator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocator_OnNoResult(), this.getOnNoResult(), "onNoResult", "NULLIFY", 0, 1, Locator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocator_UniqueResult(), theXMLTypePackage.getBoolean(), "uniqueResult", "false", 0, 1, Locator.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(parametersEClass, Parameters.class, "Parameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getParameters_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, Parameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getParameters_Value(), this.getValueParameter(), null, "value", null, 0, -1, Parameters.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getParameters_Wiring(), this.getWiringParameter(), null, "wiring", null, 0, -1, Parameters.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEReference(getParameters_Expression(), this.getExpressionParameter(), null, "expression", null, 0, -1, Parameters.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
		initEAttribute(getParameters_Type(), this.getParameterType(), "type", "NAMED", 0, 1, Parameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(updaterEClass, Updater.class, "Updater", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUpdater_BeanId(), theXMLTypePackage.getString(), "beanId", null, 1, 1, Updater.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUpdater_Dao(), theXMLTypePackage.getString(), "dao", null, 0, 1, Updater.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUpdater_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Updater.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUpdater_UpdateBefore(), theXMLTypePackage.getBoolean(), "updateBefore", "false", 0, 1, Updater.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUpdater_UpdatedBeanId(), theXMLTypePackage.getString(), "updatedBeanId", null, 0, 1, Updater.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUpdater_UpdateOnElement(), theXMLTypePackage.getString(), "updateOnElement", null, 1, 1, Updater.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUpdater_UpdateOnElementNS(), theXMLTypePackage.getAnyURI(), "updateOnElementNS", null, 0, 1, Updater.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(valueParameterEClass, ValueParameter.class, "ValueParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getValueParameter_DecodeParam(), this.getDecoderParameter(), null, "decodeParam", null, 0, 1, ValueParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueParameter_Data(), theXMLTypePackage.getString(), "data", null, 0, 1, ValueParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueParameter_DataNS(), theXMLTypePackage.getString(), "dataNS", null, 0, 1, ValueParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueParameter_Decoder(), theXMLTypePackage.getString(), "decoder", null, 0, 1, ValueParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueParameter_Default(), theXMLTypePackage.getString(), "default", null, 0, 1, ValueParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueParameter_Index(), theXMLTypePackage.getInteger(), "index", null, 0, 1, ValueParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getValueParameter_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, ValueParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wiringParameterEClass, WiringParameter.class, "WiringParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getWiringParameter_BeanIdRef(), theXMLTypePackage.getString(), "beanIdRef", null, 0, 1, WiringParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiringParameter_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, WiringParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiringParameter_WireOnElement(), theXMLTypePackage.getString(), "wireOnElement", null, 0, 1, WiringParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWiringParameter_WireOnElementNS(), theXMLTypePackage.getString(), "wireOnElementNS", null, 0, 1, WiringParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(onNoResultEEnum, OnNoResult.class, "OnNoResult");
		addEEnumLiteral(onNoResultEEnum, OnNoResult.NULLIFY_LITERAL);
		addEEnumLiteral(onNoResultEEnum, OnNoResult.EXCEPTION_LITERAL);

		initEEnum(parameterTypeEEnum, ParameterType.class, "ParameterType");
		addEEnumLiteral(parameterTypeEEnum, ParameterType.POSITIONAL_LITERAL);
		addEEnumLiteral(parameterTypeEEnum, ParameterType.NAMED_LITERAL);

		// Initialize data types
		initEDataType(onNoResultObjectEDataType, OnNoResult.class, "OnNoResultObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);
		initEDataType(parameterTypeObjectEDataType, ParameterType.class, "ParameterTypeObject", IS_SERIALIZABLE, IS_GENERATED_INSTANCE_CLASS);

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
		  (decoderParameterEClass, 
		   source, 
		   new String[] {
			 "name", "decoderParameter",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getDecoderParameter_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getDecoderParameter_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });			
		addAnnotation
		  (deleterEClass, 
		   source, 
		   new String[] {
			 "name", "deleter",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getDeleter_BeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "beanId"
		   });			
		addAnnotation
		  (getDeleter_Dao(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dao"
		   });			
		addAnnotation
		  (getDeleter_DeleteBefore(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "deleteBefore"
		   });			
		addAnnotation
		  (getDeleter_DeletedBeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "deletedBeanId"
		   });			
		addAnnotation
		  (getDeleter_DeleteOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "deleteOnElement"
		   });			
		addAnnotation
		  (getDeleter_DeleteOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "deleteOnElementNS"
		   });			
		addAnnotation
		  (getDeleter_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (persistence12DocumentRootEClass, 
		   source, 
		   new String[] {
			 "name", "",
			 "kind", "mixed"
		   });		
		addAnnotation
		  (getPersistence12DocumentRoot_Mixed(), 
		   source, 
		   new String[] {
			 "kind", "elementWildcard",
			 "name", ":mixed"
		   });		
		addAnnotation
		  (getPersistence12DocumentRoot_XMLNSPrefixMap(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xmlns:prefix"
		   });		
		addAnnotation
		  (getPersistence12DocumentRoot_XSISchemaLocation(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "xsi:schemaLocation"
		   });			
		addAnnotation
		  (getPersistence12DocumentRoot_Deleter(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "deleter",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config"
		   });			
		addAnnotation
		  (getPersistence12DocumentRoot_Flusher(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "flusher",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config"
		   });			
		addAnnotation
		  (getPersistence12DocumentRoot_Inserter(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "inserter",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config"
		   });			
		addAnnotation
		  (getPersistence12DocumentRoot_Locator(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "locator",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config"
		   });			
		addAnnotation
		  (getPersistence12DocumentRoot_Updater(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "updater",
			 "namespace", "##targetNamespace",
			 "affiliation", "http://www.milyn.org/xsd/smooks-1.1.xsd#abstract-resource-config"
		   });		
		addAnnotation
		  (expressionParameterEClass, 
		   source, 
		   new String[] {
			 "name", "expressionParameter",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getExpressionParameter_Value(), 
		   source, 
		   new String[] {
			 "name", ":0",
			 "kind", "simple"
		   });		
		addAnnotation
		  (getExpressionParameter_ExecOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "execOnElement"
		   });		
		addAnnotation
		  (getExpressionParameter_ExecOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "execOnElementNS"
		   });		
		addAnnotation
		  (getExpressionParameter_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });			
		addAnnotation
		  (flusherEClass, 
		   source, 
		   new String[] {
			 "name", "flusher",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getFlusher_Dao(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dao"
		   });			
		addAnnotation
		  (getFlusher_FlushBefore(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "flushBefore"
		   });			
		addAnnotation
		  (getFlusher_FlushOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "flushOnElement"
		   });			
		addAnnotation
		  (getFlusher_FlushOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "flushOnElementNS"
		   });			
		addAnnotation
		  (inserterEClass, 
		   source, 
		   new String[] {
			 "name", "inserter",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getInserter_BeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "beanId"
		   });			
		addAnnotation
		  (getInserter_Dao(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dao"
		   });			
		addAnnotation
		  (getInserter_InsertBefore(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "insertBefore"
		   });			
		addAnnotation
		  (getInserter_InsertedBeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "insertedBeanId"
		   });			
		addAnnotation
		  (getInserter_InsertOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "insertOnElement"
		   });			
		addAnnotation
		  (getInserter_InsertOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "insertOnElementNS"
		   });			
		addAnnotation
		  (getInserter_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (locatorEClass, 
		   source, 
		   new String[] {
			 "name", "locator",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getLocator_Query(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "query",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getLocator_Params(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "params",
			 "namespace", "##targetNamespace"
		   });			
		addAnnotation
		  (getLocator_BeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "beanId"
		   });			
		addAnnotation
		  (getLocator_Dao(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dao"
		   });			
		addAnnotation
		  (getLocator_Lookup(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "lookup"
		   });			
		addAnnotation
		  (getLocator_LookupOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "lookupOnElement"
		   });			
		addAnnotation
		  (getLocator_LookupOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "lookupOnElementNS"
		   });			
		addAnnotation
		  (getLocator_OnNoResult(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "onNoResult"
		   });			
		addAnnotation
		  (getLocator_UniqueResult(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "uniqueResult"
		   });		
		addAnnotation
		  (onNoResultEEnum, 
		   source, 
		   new String[] {
			 "name", "OnNoResult"
		   });		
		addAnnotation
		  (onNoResultObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "OnNoResult:Object",
			 "baseType", "OnNoResult"
		   });		
		addAnnotation
		  (parametersEClass, 
		   source, 
		   new String[] {
			 "name", "parameters",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getParameters_Group(), 
		   source, 
		   new String[] {
			 "kind", "group",
			 "name", "group:0"
		   });		
		addAnnotation
		  (getParameters_Value(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "value",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });		
		addAnnotation
		  (getParameters_Wiring(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "wiring",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });		
		addAnnotation
		  (getParameters_Expression(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "expression",
			 "namespace", "##targetNamespace",
			 "group", "#group:0"
		   });			
		addAnnotation
		  (getParameters_Type(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "type"
		   });		
		addAnnotation
		  (parameterTypeEEnum, 
		   source, 
		   new String[] {
			 "name", "parameterType"
		   });		
		addAnnotation
		  (parameterTypeObjectEDataType, 
		   source, 
		   new String[] {
			 "name", "parameterType:Object",
			 "baseType", "parameterType"
		   });			
		addAnnotation
		  (updaterEClass, 
		   source, 
		   new String[] {
			 "name", "updater",
			 "kind", "elementOnly"
		   });			
		addAnnotation
		  (getUpdater_BeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "beanId"
		   });			
		addAnnotation
		  (getUpdater_Dao(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dao"
		   });			
		addAnnotation
		  (getUpdater_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });			
		addAnnotation
		  (getUpdater_UpdateBefore(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updateBefore"
		   });			
		addAnnotation
		  (getUpdater_UpdatedBeanId(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updatedBeanId"
		   });			
		addAnnotation
		  (getUpdater_UpdateOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updateOnElement"
		   });			
		addAnnotation
		  (getUpdater_UpdateOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "updateOnElementNS"
		   });		
		addAnnotation
		  (valueParameterEClass, 
		   source, 
		   new String[] {
			 "name", "valueParameter",
			 "kind", "elementOnly"
		   });		
		addAnnotation
		  (getValueParameter_DecodeParam(), 
		   source, 
		   new String[] {
			 "kind", "element",
			 "name", "decodeParam",
			 "namespace", "##targetNamespace"
		   });		
		addAnnotation
		  (getValueParameter_Data(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "data"
		   });		
		addAnnotation
		  (getValueParameter_DataNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "dataNS"
		   });		
		addAnnotation
		  (getValueParameter_Decoder(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "decoder"
		   });		
		addAnnotation
		  (getValueParameter_Default(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "default"
		   });		
		addAnnotation
		  (getValueParameter_Index(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "index"
		   });		
		addAnnotation
		  (getValueParameter_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (wiringParameterEClass, 
		   source, 
		   new String[] {
			 "name", "wiringParameter",
			 "kind", "empty"
		   });		
		addAnnotation
		  (getWiringParameter_BeanIdRef(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "beanIdRef"
		   });		
		addAnnotation
		  (getWiringParameter_Name(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "name"
		   });		
		addAnnotation
		  (getWiringParameter_WireOnElement(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "wireOnElement"
		   });		
		addAnnotation
		  (getWiringParameter_WireOnElementNS(), 
		   source, 
		   new String[] {
			 "kind", "attribute",
			 "name", "wireOnElementNS"
		   });
	}

} //Persistence12PackageImpl
