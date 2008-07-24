/******************************************************************************* 
* Copyright (c) 2007 Red Hat, Inc.
* Distributed under license by Red Hat, Inc. All rights reserved.
* This program is made available under the terms of the
* Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*     Red Hat, Inc. - initial API and implementation
******************************************************************************/
package org.jboss.tools.vpe.editor;

import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;

/**
 * Class created to store information about source editor update event.
 * This class store links on parameters which using for call VpeController.notifyChanged method
 * 
 * @author mareshkau
 *
 */
public class VpeEventBean {
	
	private  INodeNotifier notifier;
	
	private int eventType;
	
	private Object feature;
	
	private Object oldValue;
	
	private Object newValue;
	
	private int pos;

	/**
	 * @param eventType
	 * @param feature
	 * @param newValue
	 * @param notifier
	 * @param oldValue
	 * @param pos
	 */
	public VpeEventBean(INodeNotifier notifier,int eventType, Object feature, Object oldValue,
			 Object newValue, int pos) {
		this.eventType = eventType;
		this.feature = feature;
		this.newValue = newValue;
		this.notifier = notifier;
		this.oldValue = oldValue;
		this.pos = pos;
	}

	/**
	 * @return the notifier
	 */
	public INodeNotifier getNotifier() {
		return notifier;
	}

	/**
	 * @return the eventType
	 */
	public int getEventType() {
		return eventType;
	}

	/**
	 * @return the feature
	 */
	public Object getFeature() {
		return feature;
	}

	/**
	 * @return the oldValue
	 */
	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * @return the newValue
	 */
	public Object getNewValue() {
		return newValue;
	}

	/**
	 * @return the pos
	 */
	public int getPos() {
		return pos;
	}
	
}
