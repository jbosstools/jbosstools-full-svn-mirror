/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.validation10;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;


/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Smooks Validation Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.validation10.Validation10Factory
 * @model kind="package"
 * @generated
 */
public interface Validation10Package extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "validation10";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/validation-1.0.xsd";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "validation";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Validation10Package eINSTANCE = org.jboss.tools.smooks.model.validation10.impl.Validation10PackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.validation10.impl.Validation10DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10DocumentRootImpl
	 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10PackageImpl#getValidation10DocumentRoot()
	 * @generated
	 */
	int VALIDATION10_DOCUMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION10_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Rule</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION10_DOCUMENT_ROOT__RULE = 3;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VALIDATION10_DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.validation10.impl.RuleTypeImpl <em>Rule Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.validation10.impl.RuleTypeImpl
	 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10PackageImpl#getRuleType()
	 * @generated
	 */
	int RULE_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Execute On</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE__EXECUTE_ON = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Execute On NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE__EXECUTE_ON_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE__NAME = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>On Fail</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE__ON_FAIL = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Rule Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_TYPE_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.validation10.OnFail <em>On Fail</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.validation10.OnFail
	 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10PackageImpl#getOnFail()
	 * @generated
	 */
	int ON_FAIL = 2;

	/**
	 * The meta object id for the '<em>On Fail Object</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.validation10.OnFail
	 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10PackageImpl#getOnFailObject()
	 * @generated
	 */
	int ON_FAIL_OBJECT = 3;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot
	 * @generated
	 */
	EClass getValidation10DocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot#getMixed()
	 * @see #getValidation10DocumentRoot()
	 * @generated
	 */
	EAttribute getValidation10DocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot#getXMLNSPrefixMap()
	 * @see #getValidation10DocumentRoot()
	 * @generated
	 */
	EReference getValidation10DocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot#getXSISchemaLocation()
	 * @see #getValidation10DocumentRoot()
	 * @generated
	 */
	EReference getValidation10DocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot#getRule <em>Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Rule</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.Validation10DocumentRoot#getRule()
	 * @see #getValidation10DocumentRoot()
	 * @generated
	 */
	EReference getValidation10DocumentRoot_Rule();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.validation10.RuleType <em>Rule Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule Type</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.RuleType
	 * @generated
	 */
	EClass getRuleType();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.validation10.RuleType#getExecuteOn <em>Execute On</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute On</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.RuleType#getExecuteOn()
	 * @see #getRuleType()
	 * @generated
	 */
	EAttribute getRuleType_ExecuteOn();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.validation10.RuleType#getExecuteOnNS <em>Execute On NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute On NS</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.RuleType#getExecuteOnNS()
	 * @see #getRuleType()
	 * @generated
	 */
	EAttribute getRuleType_ExecuteOnNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.validation10.RuleType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.RuleType#getName()
	 * @see #getRuleType()
	 * @generated
	 */
	EAttribute getRuleType_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.validation10.RuleType#getOnFail <em>On Fail</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>On Fail</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.RuleType#getOnFail()
	 * @see #getRuleType()
	 * @generated
	 */
	EAttribute getRuleType_OnFail();

	/**
	 * Returns the meta object for enum '{@link org.jboss.tools.smooks.model.validation10.OnFail <em>On Fail</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>On Fail</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.OnFail
	 * @generated
	 */
	EEnum getOnFail();

	/**
	 * Returns the meta object for data type '{@link org.jboss.tools.smooks.model.validation10.OnFail <em>On Fail Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>On Fail Object</em>'.
	 * @see org.jboss.tools.smooks.model.validation10.OnFail
	 * @model instanceClass="validation10.OnFail"
	 *        extendedMetaData="name='onFail:Object' baseType='onFail'"
	 * @generated
	 */
	EDataType getOnFailObject();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Validation10Factory getValidation10Factory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.validation10.impl.Validation10DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10DocumentRootImpl
		 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10PackageImpl#getValidation10DocumentRoot()
		 * @generated
		 */
		EClass VALIDATION10_DOCUMENT_ROOT = eINSTANCE.getValidation10DocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VALIDATION10_DOCUMENT_ROOT__MIXED = eINSTANCE.getValidation10DocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALIDATION10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getValidation10DocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALIDATION10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getValidation10DocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Rule</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VALIDATION10_DOCUMENT_ROOT__RULE = eINSTANCE.getValidation10DocumentRoot_Rule();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.validation10.impl.RuleTypeImpl <em>Rule Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.validation10.impl.RuleTypeImpl
		 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10PackageImpl#getRuleType()
		 * @generated
		 */
		EClass RULE_TYPE = eINSTANCE.getRuleType();

		/**
		 * The meta object literal for the '<em><b>Execute On</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE_TYPE__EXECUTE_ON = eINSTANCE.getRuleType_ExecuteOn();

		/**
		 * The meta object literal for the '<em><b>Execute On NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE_TYPE__EXECUTE_ON_NS = eINSTANCE.getRuleType_ExecuteOnNS();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE_TYPE__NAME = eINSTANCE.getRuleType_Name();

		/**
		 * The meta object literal for the '<em><b>On Fail</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE_TYPE__ON_FAIL = eINSTANCE.getRuleType_OnFail();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.validation10.OnFail <em>On Fail</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.validation10.OnFail
		 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10PackageImpl#getOnFail()
		 * @generated
		 */
		EEnum ON_FAIL = eINSTANCE.getOnFail();

		/**
		 * The meta object literal for the '<em>On Fail Object</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.validation10.OnFail
		 * @see org.jboss.tools.smooks.model.validation10.impl.Validation10PackageImpl#getOnFailObject()
		 * @generated
		 */
		EDataType ON_FAIL_OBJECT = eINSTANCE.getOnFailObject();

	}

} //Validation10Package
