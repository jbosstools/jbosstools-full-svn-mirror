/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.groovy.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.groovy.DocumentRoot;
import org.jboss.tools.smooks.model.groovy.Groovy;
import org.jboss.tools.smooks.model.groovy.GroovyFactory;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;
import org.jboss.tools.smooks.model.groovy.ScriptType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class GroovyFactoryImpl extends EFactoryImpl implements GroovyFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static GroovyFactory init() {
		try {
			GroovyFactory theGroovyFactory = (GroovyFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/groovy-1.1.xsd");  //$NON-NLS-1$
			if (theGroovyFactory != null) {
				return theGroovyFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new GroovyFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroovyFactoryImpl() {
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
			case GroovyPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case GroovyPackage.GROOVY: return createGroovy();
			case GroovyPackage.SCRIPT_TYPE: return createScriptType();
			default:
				throw new IllegalArgumentException(Messages.GroovyFactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.GroovyFactoryImpl_Error_Not_Valid_Classifier);
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
	public Groovy createGroovy() {
		GroovyImpl groovy = new GroovyImpl();
		return groovy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScriptType createScriptType() {
		ScriptTypeImpl scriptType = new ScriptTypeImpl();
		return scriptType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroovyPackage getGroovyPackage() {
		return (GroovyPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static GroovyPackage getPackage() {
		return GroovyPackage.eINSTANCE;
	}

} //GroovyFactoryImpl
