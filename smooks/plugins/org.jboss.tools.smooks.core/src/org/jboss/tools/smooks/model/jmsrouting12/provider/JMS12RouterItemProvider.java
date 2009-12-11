/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.jmsrouting12.provider;


import java.util.Collection;
import java.util.List;


import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.jboss.tools.smooks.model.jmsrouting12.JMS12Router;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Factory;
import org.jboss.tools.smooks.model.jmsrouting12.Jmsrouting12Package;
import org.jboss.tools.smooks.model.smooks.provider.ElementVisitorItemProvider;


/**
 * This is the item provider adapter for a {@link org.jboss.tools.smooks.model.jmsrouting12.JMS12Router} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class JMS12RouterItemProvider
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
	public JMS12RouterItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List getPropertyDescriptors(Object object) {
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
				 getString("_UI_JMS12Router_beanId_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_JMS12Router_beanId_feature", "_UI_JMS12Router_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 Jmsrouting12Package.Literals.JMS12_ROUTER__BEAN_ID,
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
				 getString("_UI_JMS12Router_destination_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_JMS12Router_destination_feature", "_UI_JMS12Router_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 Jmsrouting12Package.Literals.JMS12_ROUTER__DESTINATION,
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
				 getString("_UI_JMS12Router_executeBefore_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_JMS12Router_executeBefore_feature", "_UI_JMS12Router_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 Jmsrouting12Package.Literals.JMS12_ROUTER__EXECUTE_BEFORE,
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
				 getString("_UI_JMS12Router_routeOnElement_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_JMS12Router_routeOnElement_feature", "_UI_JMS12Router_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 Jmsrouting12Package.Literals.JMS12_ROUTER__ROUTE_ON_ELEMENT,
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
				 getString("_UI_JMS12Router_routeOnElementNS_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_JMS12Router_routeOnElementNS_feature", "_UI_JMS12Router_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 Jmsrouting12Package.Literals.JMS12_ROUTER__ROUTE_ON_ELEMENT_NS,
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
	public Collection getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(Jmsrouting12Package.Literals.JMS12_ROUTER__MESSAGE);
			childrenFeatures.add(Jmsrouting12Package.Literals.JMS12_ROUTER__CONNECTION);
			childrenFeatures.add(Jmsrouting12Package.Literals.JMS12_ROUTER__SESSION);
			childrenFeatures.add(Jmsrouting12Package.Literals.JMS12_ROUTER__JNDI);
			childrenFeatures.add(Jmsrouting12Package.Literals.JMS12_ROUTER__HIGH_WATER_MARK);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns JMS12Router.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/JMS12Router")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getText(Object object) {
		String label = ((JMS12Router)object).getTargetProfile();
		return label == null || label.length() == 0 ?
			getString("_UI_JMS12Router_type") : //$NON-NLS-1$
			getString("_UI_JMS12Router_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(JMS12Router.class)) {
			case Jmsrouting12Package.JMS12_ROUTER__BEAN_ID:
			case Jmsrouting12Package.JMS12_ROUTER__DESTINATION:
			case Jmsrouting12Package.JMS12_ROUTER__EXECUTE_BEFORE:
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT:
			case Jmsrouting12Package.JMS12_ROUTER__ROUTE_ON_ELEMENT_NS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case Jmsrouting12Package.JMS12_ROUTER__MESSAGE:
			case Jmsrouting12Package.JMS12_ROUTER__CONNECTION:
			case Jmsrouting12Package.JMS12_ROUTER__SESSION:
			case Jmsrouting12Package.JMS12_ROUTER__JNDI:
			case Jmsrouting12Package.JMS12_ROUTER__HIGH_WATER_MARK:
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
	protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(Jmsrouting12Package.Literals.JMS12_ROUTER__MESSAGE,
				 Jmsrouting12Factory.eINSTANCE.createMessage()));

		newChildDescriptors.add
			(createChildParameter
				(Jmsrouting12Package.Literals.JMS12_ROUTER__CONNECTION,
				 Jmsrouting12Factory.eINSTANCE.createConnection()));

		newChildDescriptors.add
			(createChildParameter
				(Jmsrouting12Package.Literals.JMS12_ROUTER__SESSION,
				 Jmsrouting12Factory.eINSTANCE.createSession()));

		newChildDescriptors.add
			(createChildParameter
				(Jmsrouting12Package.Literals.JMS12_ROUTER__JNDI,
				 Jmsrouting12Factory.eINSTANCE.createJndi()));

		newChildDescriptors.add
			(createChildParameter
				(Jmsrouting12Package.Literals.JMS12_ROUTER__HIGH_WATER_MARK,
				 Jmsrouting12Factory.eINSTANCE.createHighWaterMark()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceLocator getResourceLocator() {
		return Jmsrouting12EditPlugin.INSTANCE;
	}

}
