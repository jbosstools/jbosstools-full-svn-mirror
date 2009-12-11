/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.xsl.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.xsl.*;


/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XslFactoryImpl extends EFactoryImpl implements XslFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static XslFactory init() {
		try {
			XslFactory theXslFactory = (XslFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/xsl-1.1.xsd");  //$NON-NLS-1$
			if (theXslFactory != null) {
				return theXslFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new XslFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XslFactoryImpl() {
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
			case XslPackage.BIND_TO: return createBindTo();
			case XslPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case XslPackage.INLINE: return createInline();
			case XslPackage.OUTPUT_TO: return createOutputTo();
			case XslPackage.TEMPLATE: return createTemplate();
			case XslPackage.USE: return createUse();
			case XslPackage.XSL: return createXsl();
			default:
				throw new IllegalArgumentException(Messages.XslFactoryImpl_Error_Class_Not_Valid + eClass.getName() + Messages.XslFactoryImpl_Error_Not_Valid_Classifier);
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
			case XslPackage.INLINE_DIRECTIVE:
				return createInlineDirectiveFromString(eDataType, initialValue);
			case XslPackage.INLINE_DIRECTIVE_OBJECT:
				return createInlineDirectiveObjectFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException(Messages.XslFactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.XslFactoryImpl_Error_Not_Valid_Classifier);
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
			case XslPackage.INLINE_DIRECTIVE:
				return convertInlineDirectiveToString(eDataType, instanceValue);
			case XslPackage.INLINE_DIRECTIVE_OBJECT:
				return convertInlineDirectiveObjectToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException(Messages.XslFactoryImpl_Error_Datatype_Not_Valid + eDataType.getName() + Messages.XslFactoryImpl_Error_Not_Valid_Classifier);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BindTo createBindTo() {
		BindToImpl bindTo = new BindToImpl();
		return bindTo;
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
	public Inline createInline() {
		InlineImpl inline = new InlineImpl();
		return inline;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputTo createOutputTo() {
		OutputToImpl outputTo = new OutputToImpl();
		return outputTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Template createTemplate() {
		TemplateImpl template = new TemplateImpl();
		return template;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Use createUse() {
		UseImpl use = new UseImpl();
		return use;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Xsl createXsl() {
		XslImpl xsl = new XslImpl();
		return xsl;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InlineDirective createInlineDirectiveFromString(EDataType eDataType, String initialValue) {
		InlineDirective result = InlineDirective.get(initialValue);
		if (result == null) throw new IllegalArgumentException(Messages.XslFactoryImpl_Error_Value + initialValue + Messages.XslFactoryImpl_Error_Not_Valid_Enumerator + eDataType.getName() + Messages.XslFactoryImpl_9);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInlineDirectiveToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InlineDirective createInlineDirectiveObjectFromString(EDataType eDataType, String initialValue) {
		return createInlineDirectiveFromString(XslPackage.Literals.INLINE_DIRECTIVE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInlineDirectiveObjectToString(EDataType eDataType, Object instanceValue) {
		return convertInlineDirectiveToString(XslPackage.Literals.INLINE_DIRECTIVE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XslPackage getXslPackage() {
		return (XslPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static XslPackage getPackage() {
		return XslPackage.eINSTANCE;
	}

} //XslFactoryImpl
