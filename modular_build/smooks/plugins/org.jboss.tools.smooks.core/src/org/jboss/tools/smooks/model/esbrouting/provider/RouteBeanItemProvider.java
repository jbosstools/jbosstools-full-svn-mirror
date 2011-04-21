/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.esbrouting.provider;



import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.jboss.tools.smooks.model.esbrouting.EsbroutingPackage;
import org.jboss.tools.smooks.model.esbrouting.RouteBean;
import org.jboss.tools.smooks.model.smooks.provider.ElementVisitorItemProvider;

/**
 * This is the item provider adapter for a {@link org.jboss.tools.smooks.model.esbrouting.RouteBean} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RouteBeanItemProvider
	extends ElementVisitorItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RouteBeanItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addBeanIdRefPropertyDescriptor(object);
			addMessagePayloadLocationPropertyDescriptor(object);
			addRouteBeforePropertyDescriptor(object);
			addRouteOnElementPropertyDescriptor(object);
			addRouteOnElementNSPropertyDescriptor(object);
			addToServiceCategoryPropertyDescriptor(object);
			addToServiceNamePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Bean Id Ref feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBeanIdRefPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RouteBean_beanIdRef_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_RouteBean_beanIdRef_feature", "_UI_RouteBean_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 EsbroutingPackage.Literals.ROUTE_BEAN__BEAN_ID_REF,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Message Payload Location feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMessagePayloadLocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RouteBean_messagePayloadLocation_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_RouteBean_messagePayloadLocation_feature", "_UI_RouteBean_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 EsbroutingPackage.Literals.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Route Before feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRouteBeforePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RouteBean_routeBefore_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_RouteBean_routeBefore_feature", "_UI_RouteBean_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_BEFORE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Route On Element feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRouteOnElementPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RouteBean_routeOnElement_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_RouteBean_routeOnElement_feature", "_UI_RouteBean_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_ON_ELEMENT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Route On Element NS feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRouteOnElementNSPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RouteBean_routeOnElementNS_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_RouteBean_routeOnElementNS_feature", "_UI_RouteBean_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 EsbroutingPackage.Literals.ROUTE_BEAN__ROUTE_ON_ELEMENT_NS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the To Service Category feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addToServiceCategoryPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RouteBean_toServiceCategory_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_RouteBean_toServiceCategory_feature", "_UI_RouteBean_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 EsbroutingPackage.Literals.ROUTE_BEAN__TO_SERVICE_CATEGORY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the To Service Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addToServiceNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RouteBean_toServiceName_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_RouteBean_toServiceName_feature", "_UI_RouteBean_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 EsbroutingPackage.Literals.ROUTE_BEAN__TO_SERVICE_NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns RouteBean.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/RouteBean")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		
		String label = ((RouteBean)object).getToServiceName();
		String categoryName = ((RouteBean)object).getToServiceCategory();
		boolean returnServiceName = false;
		if(label != null && label.length() != 0 && categoryName != null && categoryName.length() != 0){
			returnServiceName  = true;
		}
		if(returnServiceName){
			return getString("_UI_RouteBean_type") + " (" + label+" : " + categoryName + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		}else{
			return getString("_UI_RouteBean_type"); //$NON-NLS-1$
		}
		
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(RouteBean.class)) {
			case EsbroutingPackage.ROUTE_BEAN__BEAN_ID_REF:
			case EsbroutingPackage.ROUTE_BEAN__MESSAGE_PAYLOAD_LOCATION:
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_BEFORE:
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT:
			case EsbroutingPackage.ROUTE_BEAN__ROUTE_ON_ELEMENT_NS:
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_CATEGORY:
			case EsbroutingPackage.ROUTE_BEAN__TO_SERVICE_NAME:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return Esbrouting1EditPlugin.INSTANCE;
	}

}
