/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.eclipse.bpel.ui.adapters;

import java.util.ArrayList;

import org.eclipse.bpel.model.Assign;
import org.eclipse.bpel.model.adapters.AbstractStatefulAdapter;
import org.eclipse.bpel.model.impl.AssignImpl;
import org.eclipse.bpel.ui.util.BPELUtil;
import org.eclipse.core.resources.IMarker;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A UI adapter for model objects that are not activities, but may have error markers
 * created on them by the validator. The error notifications are forwared to the
 * containing parent activity. For example, an <assign> has one or more <copy>
 * elements, each of which have <from> and <to> elements, and so on.
 * 
 * @see https://jira.jboss.org/browse/JBIDE-7497
 * @author Bob Brodt
 * @date Nov 5, 2010
 */
public class MarkerDelegateAdapter extends AbstractStatefulAdapter implements IMarkerHolder, AdapterNotification {
	
	static IMarker [] EMPTY_MARKERS = {};
	
	/** (non-Javadoc)
	 * @see org.eclipse.bpel.ui.adapters.IMarkerHolder#getMarkers(java.lang.Object)
	 *
	 * https://jira.jboss.org/browse/JBIDE-7526
	 * We don't own any markers since we delegate to MarkerHolderAdapter
	 * TODO: do we need to get markers from parent adapter?
	 */
	public IMarker[] getMarkers (Object object) {
		
		return EMPTY_MARKERS;
	}
	
	protected boolean isMarkerEvent(Notification notification) {
		int type = notification.getEventType();
		if (type==NOTIFICATION_MARKERS_STALE || 
			type==NOTIFICATION_MARKER_ADDED ||
			type==NOTIFICATION_MARKER_DELETED) {
			return true;
		}
		return false;
	}

	
	@Override
	public void notifyChanged(Notification notification) {
		if (isMarkerEvent(notification) && target instanceof EObject) {
			EObject parent = ((EObject)target).eContainer();
			if (parent != null)
				parent.eNotify(notification);
		}
	}
}
