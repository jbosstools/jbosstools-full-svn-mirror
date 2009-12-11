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
import org.jboss.tools.smooks.model.json.JsonDocumentRoot;
import org.jboss.tools.smooks.model.json.JsonFactory;
import org.jboss.tools.smooks.model.json.JsonPackage;
import org.jboss.tools.smooks.model.json.JsonReader;
import org.jboss.tools.smooks.model.json.Key;
import org.jboss.tools.smooks.model.json.KeyMap;

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
			JsonFactory theJsonFactory = (JsonFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.milyn.org/xsd/smooks/json-1.1.xsd");  //$NON-NLS-1$
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
			case JsonPackage.JSON_DOCUMENT_ROOT: return createJsonDocumentRoot();
			case JsonPackage.KEY: return createKey();
			case JsonPackage.KEY_MAP: return createKeyMap();
			case JsonPackage.JSON_READER: return createJsonReader();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JsonDocumentRoot createJsonDocumentRoot() {
		JsonDocumentRootImpl jsonDocumentRoot = new JsonDocumentRootImpl();
		return jsonDocumentRoot;
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
	public JsonReader createJsonReader() {
		JsonReaderImpl jsonReader = new JsonReaderImpl();
		return jsonReader;
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
