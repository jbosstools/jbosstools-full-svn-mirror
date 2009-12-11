/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.calc.provider;


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
import org.jboss.tools.smooks.model.calc.CalcPackage;
import org.jboss.tools.smooks.model.calc.Counter;
import org.jboss.tools.smooks.model.smooks.provider.ElementVisitorItemProvider;
import org.jboss.tools.smooks.model.xsl.provider.Xsl1EditPlugin;

/**
 * This is the item provider adapter for a {@link org.jboss.tools.smooks.model.calc.Counter} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CounterItemProvider
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
	public CounterItemProvider(AdapterFactory adapterFactory) {
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

			addStartExpressionPropertyDescriptor(object);
			addAmountExpressionPropertyDescriptor(object);
			addResetConditionPropertyDescriptor(object);
			addAmountPropertyDescriptor(object);
			addBeanIdPropertyDescriptor(object);
			addCountOnElementPropertyDescriptor(object);
			addCountOnElementNSPropertyDescriptor(object);
			addDirectionPropertyDescriptor(object);
			addExecuteAfterPropertyDescriptor(object);
			addStartPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Start Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Counter_startExpression_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_startExpression_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__START_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Amount Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAmountExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Counter_amountExpression_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_amountExpression_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__AMOUNT_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Reset Condition feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addResetConditionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Counter_resetCondition_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_resetCondition_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__RESET_CONDITION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Amount feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAmountPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Counter_amount_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_amount_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__AMOUNT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
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
				 getString("_UI_Counter_beanId_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_beanId_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__BEAN_ID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Count On Element feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCountOnElementPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Counter_countOnElement_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_countOnElement_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__COUNT_ON_ELEMENT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Count On Element NS feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCountOnElementNSPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Counter_countOnElementNS_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_countOnElementNS_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__COUNT_ON_ELEMENT_NS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Direction feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDirectionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Counter_direction_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_direction_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__DIRECTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Execute After feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addExecuteAfterPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Counter_executeAfter_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_executeAfter_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__EXECUTE_AFTER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Counter_start_feature"), //$NON-NLS-1$
				 getString("_UI_PropertyDescriptor_description", "_UI_Counter_start_feature", "_UI_Counter_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				 CalcPackage.Literals.COUNTER__START,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns Counter.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Counter")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Counter)object).getTargetProfile();
		return label == null || label.length() == 0 ?
			getString("_UI_Counter_type") : //$NON-NLS-1$
			getString("_UI_Counter_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
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

		switch (notification.getFeatureID(Counter.class)) {
			case CalcPackage.COUNTER__START_EXPRESSION:
			case CalcPackage.COUNTER__AMOUNT_EXPRESSION:
			case CalcPackage.COUNTER__RESET_CONDITION:
			case CalcPackage.COUNTER__AMOUNT:
			case CalcPackage.COUNTER__BEAN_ID:
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT:
			case CalcPackage.COUNTER__COUNT_ON_ELEMENT_NS:
			case CalcPackage.COUNTER__DIRECTION:
			case CalcPackage.COUNTER__EXECUTE_AFTER:
			case CalcPackage.COUNTER__START:
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
		return Xsl1EditPlugin.INSTANCE;
	}

}
