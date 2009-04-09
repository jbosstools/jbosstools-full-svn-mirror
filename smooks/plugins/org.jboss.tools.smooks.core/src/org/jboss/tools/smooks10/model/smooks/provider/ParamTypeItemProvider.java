/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks10.model.smooks.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.jboss.tools.smooks10.model.smooks.ParamType;
import org.jboss.tools.smooks10.model.smooks.SmooksPackage;

/**
 * This is the item provider adapter for a {@link org.jboss.tools.smooks10.model.smooks.ParamType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ParamTypeItemProvider
	extends ItemProviderAdapter
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
	public ParamTypeItemProvider(AdapterFactory adapterFactory) {
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

			addNamePropertyDescriptor(object);
			addTypePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ParamType_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ParamType_name_feature", "_UI_ParamType_type"),
				 SmooksPackage.Literals.PARAM_TYPE__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ParamType_type_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ParamType_type_feature", "_UI_ParamType_type"),
				 SmooksPackage.Literals.PARAM_TYPE__TYPE,
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
			childrenFeatures.add(XMLTypePackage.Literals.ANY_TYPE__MIXED);
			childrenFeatures.add(XMLTypePackage.Literals.ANY_TYPE__ANY_ATTRIBUTE);
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
	 * This returns ParamType.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ParamType"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ParamType)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_ParamType_type") :
			getString("_UI_ParamType_type") + " " + label;
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

		switch (notification.getFeatureID(ParamType.class)) {
			case SmooksPackage.PARAM_TYPE__NAME:
			case SmooksPackage.PARAM_TYPE__TYPE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case SmooksPackage.PARAM_TYPE__MIXED:
			case SmooksPackage.PARAM_TYPE__ANY_ATTRIBUTE:
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
				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
				 FeatureMapUtil.createEntry
					(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__COMMENT,
					 "")));

		newChildDescriptors.add
			(createChildParameter
				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
				 FeatureMapUtil.createEntry
					(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT,
					 "")));

//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__PROCESSING_INSTRUCTION,
//					 XMLTypeFactory.eINSTANCE.createProcessingInstruction())));

		newChildDescriptors.add
			(createChildParameter
				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
				 FeatureMapUtil.createEntry
					(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA,
					 "")));

//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__CONDITION,
//					 SmooksFactory.eINSTANCE.createConditionType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__IMPORT,
//					 SmooksFactory.eINSTANCE.createImportType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__PARAM,
//					 SmooksFactory.eINSTANCE.createParamType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__PROFILE,
//					 SmooksFactory.eINSTANCE.createProfileType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__PROFILES,
//					 SmooksFactory.eINSTANCE.createProfilesType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__RESOURCE,
//					 SmooksFactory.eINSTANCE.createResourceType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__RESOURCE_CONFIG,
//					 SmooksFactory.eINSTANCE.createResourceConfigType())));
//
		// newChildDescriptors.add
		// (createChildParameter
		// (XMLTypePackage.Literals.ANY_TYPE__MIXED,
		// FeatureMapUtil.createEntry
//		 (SmooksPackage.Literals.DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST,
		// SmooksFactory.eINSTANCE.createSmooksResourceListType())));
		// ExtendedMetaData.INSTANCE
		// .demandFeature("http://www.milyn.org/xsd/smooks-1.0.xsd",
		// "binding", true);
		newChildDescriptors.add
		(createChildParameter
			(XMLTypePackage.Literals.ANY_TYPE__MIXED,
			 FeatureMapUtil.createEntry
				(ExtendedMetaData.INSTANCE
						.demandFeature("http://www.milyn.org/xsd/smooks-1.0.xsd",
								"binding", true), EcoreUtil
						.create(XMLTypePackage.Literals.ANY_TYPE))));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return Smooks_1_0EditPlugin.INSTANCE;
	}

}
