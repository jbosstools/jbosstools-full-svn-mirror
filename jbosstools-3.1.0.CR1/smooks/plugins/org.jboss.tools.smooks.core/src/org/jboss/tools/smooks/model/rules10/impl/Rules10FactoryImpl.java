/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.rules10.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.rules10.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class Rules10FactoryImpl extends EFactoryImpl implements Rules10Factory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static Rules10Factory init() {
		try {
			Rules10Factory theRules10Factory = (Rules10Factory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/rules-1.0.xsd"); 
			if (theRules10Factory != null) {
				return theRules10Factory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new Rules10FactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rules10FactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case Rules10Package.RULES10_DOCUMENT_ROOT: return createRules10DocumentRoot();
			case Rules10Package.RULE_BASE: return createRuleBase();
			case Rules10Package.RULE_BASES_TYPE: return createRuleBasesType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rules10DocumentRoot createRules10DocumentRoot() {
		Rules10DocumentRootImpl rules10DocumentRoot = new Rules10DocumentRootImpl();
		return rules10DocumentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuleBase createRuleBase() {
		RuleBaseImpl ruleBase = new RuleBaseImpl();
		return ruleBase;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RuleBasesType createRuleBasesType() {
		RuleBasesTypeImpl ruleBasesType = new RuleBasesTypeImpl();
		return ruleBasesType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Rules10Package getRules10Package() {
		return (Rules10Package)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	public static Rules10Package getPackage() {
		return Rules10Package.eINSTANCE;
	}

} //Rules10FactoryImpl
