/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.ui.adapters;

import java.util.ArrayList;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Link;
import org.eclipse.bpel.model.adapters.AbstractStatefulAdapter;
import org.eclipse.bpel.ui.BPELUIPlugin;
import org.eclipse.bpel.ui.IBPELUIConstants;
import org.eclipse.bpel.ui.Messages;
import org.eclipse.bpel.ui.editparts.LinkEditPart;
import org.eclipse.bpel.ui.uiextensionmodel.UiextensionmodelFactory;
import org.eclipse.core.resources.IMarker;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.swt.graphics.Image;


/**
 * @author IBM, Original Contribution.
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Jun 5, 2007
 *
 */
public class LinkAdapter extends AbstractStatefulAdapter implements INamedElement,
	EditPartFactory, ILabeledElement, IMarkerHolder, IExtensionFactory,
	AdapterNotification
{


	/**
	 * @see org.eclipse.bpel.model.adapters.AbstractAdapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	@Override
	public void notifyChanged(Notification notification) {		
		
		super.notifyChanged(notification);
		
		switch (notification.getEventType()) {
			case NOTIFICATION_MARKERS_STALE : 
				fMarkers.clear();
				break;
			case NOTIFICATION_MARKER_ADDED :
				fMarkers.add ( (IMarker) notification.getNewValue() );
				break;
			case NOTIFICATION_MARKER_DELETED :
				fMarkers.remove ( notification.getOldValue() );
				break;								
		}				
	}
	
	ArrayList<IMarker> fMarkers = new ArrayList<IMarker>();
	static IMarker [] EMPTY_MARKERS = {};
	
	
	/** (non-Javadoc)
	 * @see org.eclipse.bpel.ui.adapters.IMarkerHolder#getMarkers(java.lang.Object)
	 */

	public IMarker[] getMarkers (Object object) {
		
		if (fMarkers.size() == 0) {
			return EMPTY_MARKERS;
		}
		return fMarkers.toArray( EMPTY_MARKERS );						
	}
	
		
	
	/**
	 * @see org.eclipse.bpel.ui.adapters.INamedElement#getName(java.lang.Object)
	 */
	public String getName (Object modelObject) {
		return ((Link)modelObject).getName();
	}

	/**
	 * @see org.eclipse.bpel.ui.adapters.INamedElement#setName(java.lang.Object, java.lang.String)
	 */
	public void setName(Object modelObject, String name) {
		((Link)modelObject).setName(name);
	}

	/**
	 * @see org.eclipse.bpel.ui.adapters.INamedElement#isNameAffected(java.lang.Object, org.eclipse.emf.common.notify.Notification)
	 */
	public boolean isNameAffected(Object modelObject, Notification n) {
		return (n.getFeatureID(Link.class) == BPELPackage.LINK__NAME);
	}

	
	
	/**
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart result = new LinkEditPart();
		result.setModel(model);
		return result;
	}


	/**
	 * @see org.eclipse.bpel.ui.adapters.ILabeledElement#getLabel(java.lang.Object)
	 */
	public String getLabel(Object object) {
		String name = getName(object);
		if (name != null) return name;
		return getTypeLabel(object);
	}
	
	/**
	 * @see org.eclipse.bpel.ui.adapters.ILabeledElement#getLargeImage(java.lang.Object)
	 */
	public Image getLargeImage(Object object) {
		return BPELUIPlugin.INSTANCE.getImage(
				IBPELUIConstants.ICON_LINK_32);
	}

	/**
	 * @see org.eclipse.bpel.ui.adapters.ILabeledElement#getSmallImage(java.lang.Object)
	 */
	public Image getSmallImage(Object object) {
		return BPELUIPlugin.INSTANCE.getImage(
				IBPELUIConstants.ICON_LINK_16);
	}

	/**
	 * @see org.eclipse.bpel.ui.adapters.ILabeledElement#getTypeLabel(java.lang.Object)
	 */
	public String getTypeLabel(Object object) {
		return Messages.LinkAdapter_Link_1; 
	}
			
	/**
	 * @see org.eclipse.bpel.ui.adapters.IExtensionFactory#createExtension(org.eclipse.emf.ecore.EObject)
	 */
	public EObject createExtension(EObject object) {
		return UiextensionmodelFactory.eINSTANCE.createLinkExtension();
	}
}
