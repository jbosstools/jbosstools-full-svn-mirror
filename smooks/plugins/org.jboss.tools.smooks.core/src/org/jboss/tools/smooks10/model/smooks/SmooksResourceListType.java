/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * List of Smooks Resource Configuration.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getProfiles <em>Profiles</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getAbstractResourceConfigGroup <em>Abstract Resource Config Group</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getAbstractResourceConfig <em>Abstract Resource Config</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getDefaultSelector <em>Default Selector</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getDefaultSelectorNamespace <em>Default Selector Namespace</em>}</li>
 *   <li>{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getDefaultTargetProfile <em>Default Target Profile</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getSmooksResourceListType()
 * @model extendedMetaData="name='smooks-resource-list_._type' kind='elementOnly'"
 * @generated
 */
public interface SmooksResourceListType extends AbstractType {
	/**
	 * Returns the value of the '<em><b>Profiles</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profiles</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profiles</em>' containment reference.
	 * @see #setProfiles(ProfilesType)
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getSmooksResourceListType_Profiles()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='profiles' namespace='##targetNamespace'"
	 * @generated
	 */
	ProfilesType getProfiles();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getProfiles <em>Profiles</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profiles</em>' containment reference.
	 * @see #getProfiles()
	 * @generated
	 */
	void setProfiles(ProfilesType value);

	/**
	 * Returns the value of the '<em><b>Abstract Resource Config Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abstract Resource Config Group</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Abstract Resource Config Group</em>' attribute list.
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getSmooksResourceListType_AbstractResourceConfigGroup()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true"
	 *        extendedMetaData="kind='group' name='abstract-resource-config:group' namespace='##targetNamespace'"
	 * @generated
	 */
	FeatureMap getAbstractResourceConfigGroup();

	/**
	 * Returns the value of the '<em><b>Abstract Resource Config</b></em>' containment reference list.
	 * The list contents are of type {@link org.jboss.tools.smooks10.model.smooks.AbstractResourceConfig}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abstract Resource Config</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Abstract Resource Config</em>' containment reference list.
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getSmooksResourceListType_AbstractResourceConfig()
	 * @model containment="true" required="true" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='abstract-resource-config' namespace='##targetNamespace' group='abstract-resource-config:group'"
	 * @generated
	 */
	EList<AbstractResourceConfig> getAbstractResourceConfig();

	/**
	 * Returns the value of the '<em><b>Default Selector</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Selector</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Selector</em>' attribute.
	 * @see #setDefaultSelector(String)
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getSmooksResourceListType_DefaultSelector()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='default-selector'"
	 * @generated
	 */
	String getDefaultSelector();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getDefaultSelector <em>Default Selector</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Selector</em>' attribute.
	 * @see #getDefaultSelector()
	 * @generated
	 */
	void setDefaultSelector(String value);

	/**
	 * Returns the value of the '<em><b>Default Selector Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Selector Namespace</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Selector Namespace</em>' attribute.
	 * @see #setDefaultSelectorNamespace(String)
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getSmooksResourceListType_DefaultSelectorNamespace()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='default-selector-namespace'"
	 * @generated
	 */
	String getDefaultSelectorNamespace();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getDefaultSelectorNamespace <em>Default Selector Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Selector Namespace</em>' attribute.
	 * @see #getDefaultSelectorNamespace()
	 * @generated
	 */
	void setDefaultSelectorNamespace(String value);

	/**
	 * Returns the value of the '<em><b>Default Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Target Profile</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Target Profile</em>' attribute.
	 * @see #setDefaultTargetProfile(String)
	 * @see org.jboss.tools.smooks10.model.smooks.SmooksPackage#getSmooksResourceListType_DefaultTargetProfile()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='default-target-profile'"
	 * @generated
	 */
	String getDefaultTargetProfile();

	/**
	 * Sets the value of the '{@link org.jboss.tools.smooks10.model.smooks.SmooksResourceListType#getDefaultTargetProfile <em>Default Target Profile</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Target Profile</em>' attribute.
	 * @see #getDefaultTargetProfile()
	 * @generated
	 */
	void setDefaultTargetProfile(String value);

} // SmooksResourceListType
