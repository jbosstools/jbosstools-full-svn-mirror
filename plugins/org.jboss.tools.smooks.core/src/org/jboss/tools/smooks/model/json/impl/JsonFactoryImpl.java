/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.json.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.jboss.tools.smooks.model.json.DocumentRoot;
import org.jboss.tools.smooks.model.json.JsonFactory;
import org.jboss.tools.smooks.model.json.JsonPackage;
import org.jboss.tools.smooks.model.json.Key;
import org.jboss.tools.smooks.model.json.KeyMap;
import org.jboss.tools.smooks.model.json.Reader;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class JsonFactoryImpl extends EFactoryImpl implements JsonFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static JsonFactory init() {
		try {
			JsonFactory theJsonFactory = (JsonFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/json-1.1.xsd"); 
			if (theJsonFactory != null) {
				return theJsonFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new JsonFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JsonFactoryImpl() {
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
			case JsonPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case JsonPackage.KEY: return createKey();
			case JsonPackage.KEY_MAP: return createKeyMap();
			case JsonPackage.READER: return createReader();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
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
	public Key createKey() {
		KeyImpl key = new KeyImpl();
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public KeyMap createKeyMap() {
		KeyMapImpl keyMap = new KeyMapImpl();
		return keyMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Reader createReader() {
		ReaderImpl reader = new ReaderImpl();
		return reader;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JsonPackage getJsonPackage() {
		return (JsonPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static JsonPackage getPackage() {
		return JsonPackage.eINSTANCE;
	}

} //JsonFactoryImpl
