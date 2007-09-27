/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.shale.ui.clay.editor.model.impl;

import java.util.Vector;
import org.eclipse.swt.widgets.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.shale.ui.clay.editor.model.*;

public class Link extends ClayElement implements ILink {
	public static final String PATH_PROPERTY = "link shape";
	// public static final String SHAPE_PROPERTY = "shape";
	public static final String HIDDEN_PROPERTY = "hidden";
	Vector<ILinkListener> linkListeners = new Vector<ILinkListener>();
	XModelObject target = null;
	IComponent toComponent;
	   
	public Link(IClayElement parent, XModelObject source) {
		super(parent, source);
	}

	public void dispose() {
		super.dispose();
		if (linkListeners != null)
			linkListeners.clear();
		linkListeners = null;
	}

	public boolean setTarget(){
		target = getClayModel().getHelper().getItemOutputTarget((XModelObject)getSource()); 
		if(target == null) return false;
		if(target.getPath() == null) {
			target = null;
			return false;
		}
		toComponent = (IComponent)clayModel.findElement(target.getPath());
		if(toComponent == null) {
			target = null;
			return false;
		}
		if(toComponent != null) {
			((ClayComponent)toComponent).addInputLink(this);
			((ClayComponent)getParentClayElement()).fireLinkAdd(this);
		}
		return true;
	}

	public XModelObject getTargetModel() {
		return target;
	}

	public Menu getPopupMenu(Control control, Object environment) {
		if(getSource() == null) return null;
		/*if(jsfModel.getHelper().getLinkActionList((XModelObject)parent.getSource()).getActionItems().length!=0){
		  XModelObjectActionList l = new XModelObjectActionList(jsfModel.getHelper().getLinkActionList((XModelObject)parent.getSource()), ((XModelObject)parent.getSource()), null, environment);
		  Menu menu = l.createMenu(control);
		  return menu;
		}*/
		return null;
	}

	public IComponent getToComponent() {
		return toComponent;
	}

	public IComponent getFromComponent() {
		return (ClayComponent)getParentClayElement();
	}

	public void addLinkListener(ILinkListener l) {
		linkListeners.add(l);
	}

	public void removeLinkListener(ILinkListener l) {
		linkListeners.remove(l);
	}

	public void fireLinkChange() {
		Vector targets = (Vector) this.linkListeners.clone();
		for (int i = 0; i < targets.size(); i++) {
			ILinkListener listener = (ILinkListener) targets.get(i);
			if (listener != null)
				listener.linkChange(this);
		}
	}

	public void fireLinkRemove() {
		Vector targets = (Vector) this.linkListeners.clone();
		for (int i = 0; i < targets.size(); i++) {
			ILinkListener listener = (ILinkListener) targets.get(i);
			if (listener != null)
				listener.linkRemove(this);
		}
		((ClayModel) getClayModel()).fireLinkRemove(this);
	}

	   public void nodeChanged(Object eventData) {
		XModelObject newTarget = getClayModel().getHelper()
				.getItemOutputTarget((XModelObject) getSource());
		if (target == null && newTarget != null) {
			((ClayComponent) getFromComponent()).addLink(this);
		} else if (target != newTarget) {
			((ClayComponent) getFromComponent()).removeLink(this);
			((ClayComponent) getFromComponent()).addLink(this);
		} else if (newTarget == null) {
			target = null;
		}
	}

}
