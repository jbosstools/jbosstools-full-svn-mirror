/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.esbrouting;

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
 * JBoss ESB Smooks Routing Components
 * <!-- end-model-doc -->
 * @see org.jboss.tools.smooks.model.esbrouting.EsbroutingFactory
 * @model kind="package"
 * @generated
 */
public interface EsbroutingPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "esbrouting"; //$NON-NLS-1$

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.jboss.org/xsd/jbossesb/smooks/routing-1.0.xsd"; //$NON-NLS-1$

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "esb"; //$NON-NLS-1$

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EsbroutingPackage eINSTANCE = org.jboss.tools.smooks.model.esbrouting.impl.EsbroutingPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.esbrouting.impl.ESBRoutingDocumentRootImpl <em>ESB Routing Document Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.esbrouting.impl.ESBRoutingDocumentRootImpl
	 * @see org.jboss.tools.smooks.model.esbrouting.impl.EsbroutingPackageImpl#getESBRoutingDocumentRoot()
	 * @generated
	 */
	int ESB_ROUTING_DOCUMENT_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESB_ROUTING_DOCUMENT_ROOT__MIXED = 0;

	/**
	 * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESB_ROUTING_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

	/**
	 * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESB_ROUTING_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Route Bean</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESB_ROUTING_DOCUMENT_ROOT__ROUTE_BEAN = 3;

	/**
	 * The number of structural features of the '<em>ESB Routing Document Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESB_ROUTING_DOCUMENT_ROOT_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl <em>Route Bean</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl
	 * @see org.jboss.tools.smooks.model.esbrouting.impl.EsbroutingPackageImpl#getRouteBean()
	 * @generated
	 */
	int ROUTE_BEAN = 1;

	/**
	 * The feature id for the '<em><b>Mixed</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__MIXED = SmooksPackage.ELEMENT_VISITOR__MIXED;

	/**
	 * The feature id for the '<em><b>Any</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__ANY = SmooksPackage.ELEMENT_VISITOR__ANY;

	/**
	 * The feature id for the '<em><b>Any Attribute</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__ANY_ATTRIBUTE = SmooksPackage.ELEMENT_VISITOR__ANY_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__CONDITION = SmooksPackage.ELEMENT_VISITOR__CONDITION;

	/**
	 * The feature id for the '<em><b>Target Profile</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__TARGET_PROFILE = SmooksPackage.ELEMENT_VISITOR__TARGET_PROFILE;

	/**
	 * The feature id for the '<em><b>Bean Id Ref</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__BEAN_ID_REF = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Message Payload Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Route Before</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__ROUTE_BEFORE = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Route On Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__ROUTE_ON_ELEMENT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Route On Element NS</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__ROUTE_ON_ELEMENT_NS = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>To Service Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__TO_SERVICE_CATEGORY = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>To Service Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN__TO_SERVICE_NAME = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 6;

	/**
	 * The number of structural features of the '<em>Route Bean</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_BEAN_FEATURE_COUNT = SmooksPackage.ELEMENT_VISITOR_FEATURE_COUNT + 7;


	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot <em>ESB Routing Document Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>ESB Routing Document Root</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot
	 * @generated
	 */
	EClass getESBRoutingDocumentRoot();

	/**
	 * Returns the meta object for the attribute list '{@link org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot#getMixed <em>Mixed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Mixed</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot#getMixed()
	 * @see #getESBRoutingDocumentRoot()
	 * @generated
	 */
	EAttribute getESBRoutingDocumentRoot_Mixed();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot#getXMLNSPrefixMap()
	 * @see #getESBRoutingDocumentRoot()
	 * @generated
	 */
	EReference getESBRoutingDocumentRoot_XMLNSPrefixMap();

	/**
	 * Returns the meta object for the map '{@link org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>XSI Schema Location</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot#getXSISchemaLocation()
	 * @see #getESBRoutingDocumentRoot()
	 * @generated
	 */
	EReference getESBRoutingDocumentRoot_XSISchemaLocation();

	/**
	 * Returns the meta object for the containment reference '{@link org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot#getRouteBean <em>Route Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Route Bean</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.ESBRoutingDocumentRoot#getRouteBean()
	 * @see #getESBRoutingDocumentRoot()
	 * @generated
	 */
	EReference getESBRoutingDocumentRoot_RouteBean();

	/**
	 * Returns the meta object for class '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean <em>Route Bean</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Route Bean</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.RouteBean
	 * @generated
	 */
	EClass getRouteBean();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getBeanIdRef <em>Bean Id Ref</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Bean Id Ref</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.RouteBean#getBeanIdRef()
	 * @see #getRouteBean()
	 * @generated
	 */
	EAttribute getRouteBean_BeanIdRef();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getMessagePayloadLocation <em>Message Payload Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Message Payload Location</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.RouteBean#getMessagePayloadLocation()
	 * @see #getRouteBean()
	 * @generated
	 */
	EAttribute getRouteBean_MessagePayloadLocation();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#isRouteBefore <em>Route Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route Before</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.RouteBean#isRouteBefore()
	 * @see #getRouteBean()
	 * @generated
	 */
	EAttribute getRouteBean_RouteBefore();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getRouteOnElement <em>Route On Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route On Element</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.RouteBean#getRouteOnElement()
	 * @see #getRouteBean()
	 * @generated
	 */
	EAttribute getRouteBean_RouteOnElement();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getRouteOnElementNS <em>Route On Element NS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route On Element NS</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.RouteBean#getRouteOnElementNS()
	 * @see #getRouteBean()
	 * @generated
	 */
	EAttribute getRouteBean_RouteOnElementNS();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getToServiceCategory <em>To Service Category</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>To Service Category</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.RouteBean#getToServiceCategory()
	 * @see #getRouteBean()
	 * @generated
	 */
	EAttribute getRouteBean_ToServiceCategory();

	/**
	 * Returns the meta object for the attribute '{@link org.jboss.tools.smooks.model.esbrouting.RouteBean#getToServiceName <em>To Service Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>To Service Name</em>'.
	 * @see org.jboss.tools.smooks.model.esbrouting.RouteBean#getToServiceName()
	 * @see #getRouteBean()
	 * @generated
	 */
	EAttribute getRouteBean_ToServiceName();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	EsbroutingFactory getEsbroutingFactory();

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
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.esbrouting.impl.ESBRoutingDocumentRootImpl <em>ESB Routing Document Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.esbrouting.impl.ESBRoutingDocumentRootImpl
		 * @see org.jboss.tools.smooks.model.esbrouting.impl.EsbroutingPackageImpl#getESBRoutingDocumentRoot()
		 * @generated
		 */
		EClass ESB_ROUTING_DOCUMENT_ROOT = eINSTANCE.getESBRoutingDocumentRoot();

		/**
		 * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ESB_ROUTING_DOCUMENT_ROOT__MIXED = eINSTANCE.getESBRoutingDocumentRoot_Mixed();

		/**
		 * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESB_ROUTING_DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getESBRoutingDocumentRoot_XMLNSPrefixMap();

		/**
		 * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESB_ROUTING_DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getESBRoutingDocumentRoot_XSISchemaLocation();

		/**
		 * The meta object literal for the '<em><b>Route Bean</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESB_ROUTING_DOCUMENT_ROOT__ROUTE_BEAN = eINSTANCE.getESBRoutingDocumentRoot_RouteBean();

		/**
		 * The meta object literal for the '{@link org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl <em>Route Bean</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.jboss.tools.smooks.model.esbrouting.impl.RouteBeanImpl
		 * @see org.jboss.tools.smooks.model.esbrouting.impl.EsbroutingPackageImpl#getRouteBean()
		 * @generated
		 */
		EClass ROUTE_BEAN = eINSTANCE.getRouteBean();

		/**
		 * The meta object literal for the '<em><b>Bean Id Ref</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_BEAN__BEAN_ID_REF = eINSTANCE.getRouteBean_BeanIdRef();

		/**
		 * The meta object literal for the '<em><b>Message Payload Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION = eINSTANCE.getRouteBean_MessagePayloadLocation();

		/**
		 * The meta object literal for the '<em><b>Route Before</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_BEAN__ROUTE_BEFORE = eINSTANCE.getRouteBean_RouteBefore();

		/**
		 * The meta object literal for the '<em><b>Route On Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_BEAN__ROUTE_ON_ELEMENT = eINSTANCE.getRouteBean_RouteOnElement();

		/**
		 * The meta object literal for the '<em><b>Route On Element NS</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_BEAN__ROUTE_ON_ELEMENT_NS = eINSTANCE.getRouteBean_RouteOnElementNS();

		/**
		 * The meta object literal for the '<em><b>To Service Category</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_BEAN__TO_SERVICE_CATEGORY = eINSTANCE.getRouteBean_ToServiceCategory();

		/**
		 * The meta object literal for the '<em><b>To Service Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_BEAN__TO_SERVICE_NAME = eINSTANCE.getRouteBean_ToServiceName();

	}

} //EsbroutingPackage
