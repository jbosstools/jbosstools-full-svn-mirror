/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.groovy;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.jboss.tools.smooks.model.groovy.impl.GroovyPackageImpl;
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
 * Groovy Scripting Configuration
 * <!-- end-model-doc -->
 * @see groovy.GroovyFactory
 * @model kind="package"
 * @generated
 */
public interface GroovyPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "groovy";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.milyn.org/xsd/smooks/groovy-1.1.xsd";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "g";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	GroovyPackage eINSTANCE = GroovyPackageImpl.init();

	/**
	 * The meta object id for the '{@link SmooksGraphExtDocumentRootImpl.impl.DocumentRootImpl <em>Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see SmooksGraphExtDocumentRootImpl.impl.DocumentRootImpl
	 * @see groovy.impl.GroovyPackageImpl#getDocumentRoot()
	 * @generated
	 */
	int DOCUMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Groovy</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__GROOVY = 3;

	/**
	 * The feature id for the '<em><b>Script</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT__SCRIPT = 4;

	/**
	 * The number of structural features of the '<em>Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOCUMENT_ROOT_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link groovy.impl.GroovyImpl <em>Groovy</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see groovy.impl.GroovyImpl
	 * @see groovy.impl.GroovyPackageImpl#getGroovy()
	 * @generated
	 */
	int GROOVY = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Imports</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__IMPORTS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Param</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__PARAM = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Script</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__SCRIPT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Execute Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__EXECUTE_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Execute On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__EXECUTE_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Execute On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY__EXECUTE_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Groovy</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GROOVY_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The meta object id for the '{@link groovy.impl.ScriptTypeImpl <em>Script Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see groovy.impl.ScriptTypeImpl
	 * @see groovy.impl.GroovyPackageImpl#getScriptType()
	 * @generated
	 */
	int SCRIPT_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT_TYPE__MIXED = XMLTypePackage.ANY_TYPE__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT_TYPE__ANY = XMLTypePackage.ANY_TYPE__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT_TYPE__ANY_ATTRIBUTE = XMLTypePackage.ANY_TYPE__ANY_ATTRIBUTE;

	/**
	 * The number of structural features of the '<em>Script Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCRIPT_TYPE_FEATURE_COUNT = XMLTypePackage.ANY_TYPE_FEATURE_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link groovy.SmooksGraphExtDocumentRoot <em>Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Document Root</em>'.
	 * @see groovy.SmooksGraphExtDocumentRoot
	 * @generated
	 */
	EClass getDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link groovy.SmooksGraphExtDocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see groovy.SmooksGraphExtDocumentRoot#getMixed()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EAttribute getDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link groovy.SmooksGraphExtDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see groovy.SmooksGraphExtDocumentRoot#getXMLNSPrefixMap()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link groovy.SmooksGraphExtDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see groovy.SmooksGraphExtDocumentRoot#getXSISchemaLocation()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link groovy.SmooksGraphExtDocumentRoot#getGroovy <em>Groovy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Groovy</em>'.
	 * @see groovy.SmooksGraphExtDocumentRoot#getGroovy()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Groovy();

	/**
	 * Returns the meta object for the containment reference '{@link groovy.SmooksGraphExtDocumentRoot#getScript <em>Script</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Script</em>'.
	 * @see groovy.SmooksGraphExtDocumentRoot#getScript()
	 * @see #getDocumentRoot()
	 * @generated
	 */
	EReference getDocumentRoot_Script();

	/**
	 * Returns the meta object for class '{@link groovy.Groovy <em>Groovy</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Groovy</em>'.
	 * @see groovy.Groovy
	 * @generated
	 */
	EClass getGroovy();

	/**
	 * Returns the meta object for the attribute '{@link groovy.Groovy#getImports <em>Imports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Imports</em>'.
	 * @see groovy.Groovy#getImports()
	 * @see #getGroovy()
	 * @generated
	 */
	EAttribute getGroovy_Imports();

	/**
	 * Returns the meta object for the containment reference list '{@link groovy.Groovy#getParam <em>Param</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Param</em>'.
	 * @see groovy.Groovy#getParam()
	 * @see #getGroovy()
	 * @generated
	 */
	EReference getGroovy_Param();

	/**
	 * Returns the meta object for the containment reference '{@link groovy.Groovy#getScript <em>Script</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Script</em>'.
	 * @see groovy.Groovy#getScript()
	 * @see #getGroovy()
	 * @generated
	 */
	EReference getGroovy_Script();

	/**
	 * Returns the meta object for the attribute '{@link groovy.Groovy#isExecuteBefore <em>Execute Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute Before</em>'.
	 * @see groovy.Groovy#isExecuteBefore()
	 * @see #getGroovy()
	 * @generated
	 */
	EAttribute getGroovy_ExecuteBefore();

	/**
	 * Returns the meta object for the attribute '{@link groovy.Groovy#getExecuteOnElement <em>Execute On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute On Element</em>'.
	 * @see groovy.Groovy#getExecuteOnElement()
	 * @see #getGroovy()
	 * @generated
	 */
	EAttribute getGroovy_ExecuteOnElement();

	/**
	 * Returns the meta object for the attribute '{@link groovy.Groovy#getExecuteOnElementNS <em>Execute On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Execute On Element NS</em>'.
	 * @see groovy.Groovy#getExecuteOnElementNS()
	 * @see #getGroovy()
	 * @generated
	 */
	EAttribute getGroovy_ExecuteOnElementNS();

	/**
	 * Returns the meta object for class '{@link groovy.ScriptType <em>Script Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Script Type</em>'.
	 * @see groovy.ScriptType
	 * @generated
	 */
	EClass getScriptType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	GroovyFactory getGroovyFactory();

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
		 * The meta object literal for the '{@link SmooksGraphExtDocumentRootImpl.impl.DocumentRootImpl <em>Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see SmooksGraphExtDocumentRootImpl.impl.DocumentRootImpl
		 * @see groovy.impl.GroovyPackageImpl#getDocumentRoot()
		 * @generated
		 */
		EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Groovy</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__GROOVY = eINSTANCE.getDocumentRoot_Groovy();

		/**
		 * The meta object literal for the '<em><b>Script</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DOCUMENT_ROOT__SCRIPT = eINSTANCE.getDocumentRoot_Script();

		/**
		 * The meta object literal for the '{@link groovy.impl.GroovyImpl <em>Groovy</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see groovy.impl.GroovyImpl
		 * @see groovy.impl.GroovyPackageImpl#getGroovy()
		 * @generated
		 */
		EClass GROOVY = eINSTANCE.getGroovy();

		/**
		 * The meta object literal for the '<em><b>Imports</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROOVY__IMPORTS = eINSTANCE.getGroovy_Imports();

		/**
		 * The meta object literal for the '<em><b>Param</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROOVY__PARAM = eINSTANCE.getGroovy_Param();

		/**
		 * The meta object literal for the '<em><b>Script</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GROOVY__SCRIPT = eINSTANCE.getGroovy_Script();

		/**
		 * The meta object literal for the '<em><b>Execute Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROOVY__EXECUTE_BEFORE = eINSTANCE.getGroovy_ExecuteBefore();

		/**
		 * The meta object literal for the '<em><b>Execute On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROOVY__EXECUTE_ON_ELEMENT = eINSTANCE.getGroovy_ExecuteOnElement();

		/**
		 * The meta object literal for the '<em><b>Execute On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GROOVY__EXECUTE_ON_ELEMENT_NS = eINSTANCE.getGroovy_ExecuteOnElementNS();

		/**
		 * The meta object literal for the '{@link groovy.impl.ScriptTypeImpl <em>Script Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see groovy.impl.ScriptTypeImpl
		 * @see groovy.impl.GroovyPackageImpl#getScriptType()
		 * @generated
		 */
		EClass SCRIPT_TYPE = eINSTANCE.getScriptType();

	}

} //GroovyPackage
