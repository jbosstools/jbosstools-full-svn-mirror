/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.rules10;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * Smooks Rule Bases Configuration
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.rules10.Rules10Factory
 * @model kind="package"
 * @generated
 */
public interface Rules10Package extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "rules10"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/rules-1.0.xsd"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "rules"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	Rules10Package eINSTANCE = org.jboss.tools.smooks.model.rules10.impl.Rules10PackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.rules10.impl.Rules10DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.rules10.impl.Rules10DocumentRootImpl
	 * @see org.jboss.tools.smooks.model.rules10.impl.Rules10PackageImpl#getRules10DocumentRoot()
	 * @generated
	 */
	int RULES10_DOCUMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULES10_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULES10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULES10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Rule Bases</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULES10_DOCUMENT_ROOT__RULE_BASES = 3;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULES10_DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.rules10.impl.RuleBaseImpl <em>Rule Base</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.rules10.impl.RuleBaseImpl
	 * @see org.jboss.tools.smooks.model.rules10.impl.Rules10PackageImpl#getRuleBase()
	 * @generated
	 */
	int RULE_BASE = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASE__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASE__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASE__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASE__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASE__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASE__NAME = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASE__PROVIDER = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Src</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASE__SRC = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Rule Base</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASE_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.rules10.impl.RuleBasesTypeImpl <em>Rule Bases Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.rules10.impl.RuleBasesTypeImpl
	 * @see org.jboss.tools.smooks.model.rules10.impl.Rules10PackageImpl#getRuleBasesType()
	 * @generated
	 */
	int RULE_BASES_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASES_TYPE__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASES_TYPE__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASES_TYPE__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASES_TYPE__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASES_TYPE__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Group</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASES_TYPE__GROUP = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Rule Base</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASES_TYPE__RULE_BASE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Rule Bases Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RULE_BASES_TYPE_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot
	 * @generated
	 */
	EClass getRules10DocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot#getMixed()
	 * @see #getRules10DocumentRoot()
	 * @generated
	 */
	EAttribute getRules10DocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot#getXMLNSPrefixMap()
	 * @see #getRules10DocumentRoot()
	 * @generated
	 */
	EReference getRules10DocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot#getXSISchemaLocation()
	 * @see #getRules10DocumentRoot()
	 * @generated
	 */
	EReference getRules10DocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot#getRuleBases <em>Rule Bases</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Rule Bases</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.Rules10DocumentRoot#getRuleBases()
	 * @see #getRules10DocumentRoot()
	 * @generated
	 */
	EReference getRules10DocumentRoot_RuleBases();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.rules10.RuleBase <em>Rule Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule Base</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.RuleBase
	 * @generated
	 */
	EClass getRuleBase();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.rules10.RuleBase#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.RuleBase#getName()
	 * @see #getRuleBase()
	 * @generated
	 */
	EAttribute getRuleBase_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.rules10.RuleBase#getProvider <em>Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Provider</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.RuleBase#getProvider()
	 * @see #getRuleBase()
	 * @generated
	 */
	EAttribute getRuleBase_Provider();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.rules10.RuleBase#getSrc <em>Src</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Src</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.RuleBase#getSrc()
	 * @see #getRuleBase()
	 * @generated
	 */
	EAttribute getRuleBase_Src();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.rules10.RuleBasesType <em>Rule Bases Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rule Bases Type</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.RuleBasesType
	 * @generated
	 */
	EClass getRuleBasesType();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.rules10.RuleBasesType#getGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Group</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.RuleBasesType#getGroup()
	 * @see #getRuleBasesType()
	 * @generated
	 */
	EAttribute getRuleBasesType_Group();

	/**
	 * Returns the meta object for the containment reference list '{@link org.jboss.tools.smooks.model.rules10.RuleBasesType#getRuleBase <em>Rule Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rule Base</em>'.
	 * @see org.jboss.tools.smooks.model.rules10.RuleBasesType#getRuleBase()
	 * @see #getRuleBasesType()
	 * @generated
	 */
	EReference getRuleBasesType_RuleBase();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	Rules10Factory getRules10Factory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.rules10.impl.Rules10DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.rules10.impl.Rules10DocumentRootImpl
		 * @see org.jboss.tools.smooks.model.rules10.impl.Rules10PackageImpl#getRules10DocumentRoot()
		 * @generated
		 */
		EClass RULES10_DOCUMENT_ROOT = eINSTANCE.getRules10DocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULES10_DOCUMENT_ROOT__MIXED = eINSTANCE.getRules10DocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULES10_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getRules10DocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULES10_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getRules10DocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Rule Bases</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULES10_DOCUMENT_ROOT__RULE_BASES = eINSTANCE.getRules10DocumentRoot_RuleBases();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.rules10.impl.RuleBaseImpl <em>Rule Base</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.rules10.impl.RuleBaseImpl
		 * @see org.jboss.tools.smooks.model.rules10.impl.Rules10PackageImpl#getRuleBase()
		 * @generated
		 */
		EClass RULE_BASE = eINSTANCE.getRuleBase();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE_BASE__NAME = eINSTANCE.getRuleBase_Name();

		/**
		 * The meta object literal for the '<em><b>Provider</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE_BASE__PROVIDER = eINSTANCE.getRuleBase_Provider();

		/**
		 * The meta object literal for the '<em><b>Src</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE_BASE__SRC = eINSTANCE.getRuleBase_Src();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.rules10.impl.RuleBasesTypeImpl <em>Rule Bases Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.rules10.impl.RuleBasesTypeImpl
		 * @see org.jboss.tools.smooks.model.rules10.impl.Rules10PackageImpl#getRuleBasesType()
		 * @generated
		 */
		EClass RULE_BASES_TYPE = eINSTANCE.getRuleBasesType();

		/**
		 * The meta object literal for the '<em><b>Group</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RULE_BASES_TYPE__GROUP = eINSTANCE.getRuleBasesType_Group();

		/**
		 * The meta object literal for the '<em><b>Rule Base</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RULE_BASES_TYPE__RULE_BASE = eINSTANCE.getRuleBasesType_RuleBase();

	}

} //Rules10Package
