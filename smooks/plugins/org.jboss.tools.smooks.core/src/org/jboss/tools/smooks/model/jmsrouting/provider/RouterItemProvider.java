/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting.provider;


import java.util.Collection;
import java.util.List;


import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingFactory;
import org.jboss.tools.smooks.model.jmsrouting.JmsroutingPackage;
import org.jboss.tools.smooks.model.jmsrouting.Router;
import org.jboss.tools.smooks.model.smooks.provider.ElementVisitorItemProvider;

/**
 * This is the item provider adapter for a {@link org.jboss.tools.smooks.model.jmsrouting.Router} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RouterItemProvider
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
	public RouterItemProvider(AdapterFactory adapterFactory) {
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

			addBeanIdPropertyDescriptor(object);
			addDestinationPropertyDescriptor(object);
			addExecuteBeforePropertyDescriptor(object);
			addRouteOnElementPropertyDescriptor(object);
			addRouteOnElementNSPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Bean Id feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBeanIdPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Router_beanId_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Router_beanId_feature", "_UI_JMS_Router_type"),
				 JmsroutingPackage.Literals.ROUTER__BEAN_ID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Destination feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDestinationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Router_destination_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Router_destination_feature", "_UI_JMS_Router_type"),
				 JmsroutingPackage.Literals.ROUTER__DESTINATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Execute Before feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addExecuteBeforePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Router_executeBefore_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Router_executeBefore_feature", "_UI_JMS_Router_type"),
				 JmsroutingPackage.Literals.ROUTER__EXECUTE_BEFORE,
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
				 getString("_UI_Router_routeOnElement_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Router_routeOnElement_feature", "_UI_JMS_Router_type"),
				 JmsroutingPackage.Literals.ROUTER__ROUTE_ON_ELEMENT,
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
				 getString("_UI_Router_routeOnElementNS_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Router_routeOnElementNS_feature", "_UI_JMS_Router_type"),
				 JmsroutingPackage.Literals.ROUTER__ROUTE_ON_ELEMENT_NS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(JmsroutingPackage.Literals.ROUTER__MESSAGE);
			childrenFeatures.add(JmsroutingPackage.Literals.ROUTER__CONNECTION);
			childrenFeatures.add(JmsroutingPackage.Literals.ROUTER__SESSION);
			childrenFeatures.add(JmsroutingPackage.Literals.ROUTER__JNDI);
			childrenFeatures.add(JmsroutingPackage.Literals.ROUTER__HIGH_WATER_MARK);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns Router.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Router"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Router)object).getTargetProfile();
		return label == null || label.length() == 0 ?
			getString("_UI_JMS_Router_type") :
			getString("_UI_JMS_Router_type") + " " + label;
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

		switch (notification.getFeatureID(Router.class)) {
			case JmsroutingPackage.ROUTER__BEAN_ID:
			case JmsroutingPackage.ROUTER__DESTINATION:
			case JmsroutingPackage.ROUTER__EXECUTE_BEFORE:
			case JmsroutingPackage.ROUTER__ROUTE_ON_ELEMENT:
			case JmsroutingPackage.ROUTER__ROUTE_ON_ELEMENT_NS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case JmsroutingPackage.ROUTER__MESSAGE:
			case JmsroutingPackage.ROUTER__CONNECTION:
			case JmsroutingPackage.ROUTER__SESSION:
			case JmsroutingPackage.ROUTER__JNDI:
			case JmsroutingPackage.ROUTER__HIGH_WATER_MARK:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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

		newChildDescriptors.add
			(createChildParameter
				(JmsroutingPackage.Literals.ROUTER__MESSAGE,
				 JmsroutingFactory.eINSTANCE.createMessage()));

		newChildDescriptors.add
			(createChildParameter
				(JmsroutingPackage.Literals.ROUTER__CONNECTION,
				 JmsroutingFactory.eINSTANCE.createConnection()));

		newChildDescriptors.add
			(createChildParameter
				(JmsroutingPackage.Literals.ROUTER__SESSION,
				 JmsroutingFactory.eINSTANCE.createSession()));

		newChildDescriptors.add
			(createChildParameter
				(JmsroutingPackage.Literals.ROUTER__JNDI,
				 JmsroutingFactory.eINSTANCE.createJndi()));

		newChildDescriptors.add
			(createChildParameter
				(JmsroutingPackage.Literals.ROUTER__HIGH_WATER_MARK,
				 JmsroutingFactory.eINSTANCE.createHighWaterMark()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return Jmsrouting1EditPlugin.INSTANCE;
	}

}
