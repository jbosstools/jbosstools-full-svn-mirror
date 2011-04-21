/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks10.model.smooks.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SmooksFactoryImpl extends EFactoryImpl implements SmooksFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SmooksFactory init() {
		try {
			SmooksFactory theSmooksFactory = (SmooksFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks-1.0.xsd"); 
			if (theSmooksFactory != null) {
				return theSmooksFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SmooksFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksFactoryImpl() {
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
			case SmooksPackage.CONDITION_TYPE: return createConditionType();
			case SmooksPackage.SMOOKS10_DOCUMENT_ROOT: return createSmooks10DocumentRoot();
			case SmooksPackage.IMPORT_TYPE: return createImportType();
			case SmooksPackage.PARAM_TYPE: return createParamType();
			case SmooksPackage.PROFILES_TYPE: return createProfilesType();
			case SmooksPackage.PROFILE_TYPE: return createProfileType();
			case SmooksPackage.RESOURCE_CONFIG_TYPE: return createResourceConfigType();
			case SmooksPackage.RESOURCE_TYPE: return createResourceType();
			case SmooksPackage.SMOOKS_RESOURCE_LIST_TYPE: return createSmooksResourceListType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConditionType createConditionType() {
		ConditionTypeImpl conditionType = new ConditionTypeImpl();
		return conditionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DocumentRoot createSmooks10DocumentRoot() {
		DocumentRootImpl smooks10DocumentRoot = new DocumentRootImpl();
		return smooks10DocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ImportType createImportType() {
		ImportTypeImpl importType = new ImportTypeImpl();
		return importType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParamType createParamType() {
		ParamTypeImpl paramType = new ParamTypeImpl();
		return paramType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfilesType createProfilesType() {
		ProfilesTypeImpl profilesType = new ProfilesTypeImpl();
		return profilesType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfileType createProfileType() {
		ProfileTypeImpl profileType = new ProfileTypeImpl();
		return profileType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceConfigType createResourceConfigType() {
		ResourceConfigTypeImpl resourceConfigType = new ResourceConfigTypeImpl();
		return resourceConfigType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceType createResourceType() {
		ResourceTypeImpl resourceType = new ResourceTypeImpl();
		return resourceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksResourceListType createSmooksResourceListType() {
		SmooksResourceListTypeImpl smooksResourceListType = new SmooksResourceListTypeImpl();
		return smooksResourceListType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SmooksPackage getSmooksPackage() {
		return (SmooksPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SmooksPackage getPackage() {
		return SmooksPackage.eINSTANCE;
	}

} //SmooksFactoryImpl
