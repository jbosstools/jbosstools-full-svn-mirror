/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.groovy.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.jboss.tools.smooks.model.groovy.GroovyPackage;
import org.jboss.tools.smooks.model.groovy.ScriptType;

/**
 * This is the item provider adapter for a {@link groovy.ScriptType} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScriptTypeItemProvider
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
	public ScriptTypeItemProvider(AdapterFactory adapterFactory) {
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

		}
		return itemPropertyDescriptors;
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
	 * This returns ScriptType.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ScriptType")); //$NON-NLS-1$
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		return getString("_UI_ScriptType_type"); //$NON-NLS-1$
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

		switch (notification.getFeatureID(ScriptType.class)) {
			case GroovyPackage.SCRIPT_TYPE__MIXED:
			case GroovyPackage.SCRIPT_TYPE__ANY_ATTRIBUTE:
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
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__COMMENT,
//					 "")));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT,
//					 "")));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__PROCESSING_INSTRUCTION,
//					 XMLTypeFactory.eINSTANCE.createProcessingInstruction())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA,
//					 "")));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(GroovyPackage.Literals.DOCUMENT_ROOT__GROOVY,
//					 GroovyFactory.eINSTANCE.createGroovy())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(GroovyPackage.Literals.DOCUMENT_ROOT__SCRIPT,
//					 GroovyFactory.eINSTANCE.createScriptType())));
//
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
//					(SmooksPackage.Literals.DOCUMENT_ROOT__CONDITIONS,
//					 SmooksFactory.eINSTANCE.createConditionsType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__FEATURES,
//					 SmooksFactory.eINSTANCE.createFeaturesType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__HANDLER,
//					 SmooksFactory.eINSTANCE.createHandlerType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__HANDLERS,
//					 SmooksFactory.eINSTANCE.createHandlersType())));
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
//					(SmooksPackage.Literals.DOCUMENT_ROOT__PARAMS,
//					 SmooksFactory.eINSTANCE.createParamsType())));
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
//					(SmooksPackage.Literals.DOCUMENT_ROOT__READER,
//					 SmooksFactory.eINSTANCE.createReaderType())));
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
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__SET_OFF,
//					 SmooksFactory.eINSTANCE.createSetOffType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__SET_ON,
//					 SmooksFactory.eINSTANCE.createSetOnType())));
//
//		newChildDescriptors.add
//			(createChildParameter
//				(XMLTypePackage.Literals.ANY_TYPE__MIXED,
//				 FeatureMapUtil.createEntry
//					(SmooksPackage.Literals.DOCUMENT_ROOT__SMOOKS_RESOURCE_LIST,
//					 SmooksFactory.eINSTANCE.createSmooksResourceListType())));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return GroovyEditPlugin.INSTANCE;
	}

}
