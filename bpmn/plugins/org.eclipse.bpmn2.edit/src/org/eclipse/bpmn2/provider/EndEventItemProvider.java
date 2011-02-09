/**
 * <copyright>
 * 
 * Copyright (c) 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Reiner Hille-Doering (SAP AG) - initial API and implementation and/or initial documentation
 * 
 * </copyright>
 *
 */
package org.eclipse.bpmn2.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.TerminateEventDefinition;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link org.eclipse.bpmn2.EndEvent} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class EndEventItemProvider extends ThrowEventItemProvider implements
        IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider,
        IItemLabelProvider, IItemPropertySource {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EndEventItemProvider(AdapterFactory adapterFactory) {
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
     * This returns EndEvent.png.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated not
     */
    @Override
    public Object getImage(Object object) {
        try {
            return overlayImage(object,
                    getResourceLocator().getImage(getImagePath((EndEvent) object)));
        } catch (java.util.MissingResourceException ignore) {
        }
        try {
            return overlayImage(object, getResourceLocator().getImage("full/obj16/EndEvent.png"));
        } catch (java.util.MissingResourceException e) {
            return overlayImage(object, getResourceLocator().getImage("full/obj16/EndEvent.gif"));
        }
    }

    private boolean isMessageEvent(EndEvent endEvent) {
        List<EventDefinition> eventDefinitions = endEvent.getEventDefinitions();
        List<EventDefinition> eventDefinitionRefs = endEvent.getEventDefinitionRefs();
        if (eventDefinitions.size() + eventDefinitionRefs.size() != 1)
            return false;
        if (!eventDefinitions.isEmpty())
            return eventDefinitions.get(0) instanceof MessageEventDefinition;
        if (!eventDefinitionRefs.isEmpty())
            return eventDefinitionRefs.get(0) instanceof MessageEventDefinition;
        return false;
    }

    private boolean isTerminateEvent(EndEvent endEvent) {
        List<EventDefinition> eventDefinitions = endEvent.getEventDefinitions();
        List<EventDefinition> eventDefinitionRefs = endEvent.getEventDefinitionRefs();
        if (eventDefinitions.size() + eventDefinitionRefs.size() != 1)
            return false;
        if (!eventDefinitions.isEmpty())
            return eventDefinitions.get(0) instanceof TerminateEventDefinition;
        if (!eventDefinitionRefs.isEmpty())
            return eventDefinitionRefs.get(0) instanceof TerminateEventDefinition;
        return false;
    }

    private String getImagePath(EndEvent endEvent) {
        if (isMessageEvent(endEvent)) {
            return "added/obj16/events_16px_end_message.png";
        } else if (isTerminateEvent(endEvent)) {
            return "added/obj16/events_16px_end_terminate.png";
        } else {
            return "added/obj16/events_16px_end_none.png";
        }
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((EndEvent) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_EndEvent_type")
                : getString("_UI_EndEvent_type") + " " + label;
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

}
