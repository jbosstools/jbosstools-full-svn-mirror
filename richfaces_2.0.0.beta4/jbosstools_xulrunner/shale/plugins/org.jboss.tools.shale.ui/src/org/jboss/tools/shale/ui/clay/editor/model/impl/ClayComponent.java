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

import java.util.*;
import org.eclipse.swt.graphics.*;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.shale.ui.clay.editor.model.*;

public class ClayComponent extends ClayElement implements IComponent {
	private boolean expanded = true;
	private boolean hidden = false;
	
	public void collapse() {
		expanded = false;
		ILink link;
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);
			((ClayComponent) link.getFromComponent()).fireLinkRemove(link, 0);
			link.getFromComponent().hide();
		}
	}

	public void hide() {
		hidden = true;
		expanded = true;
		ILink link;
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);

			link.getFromComponent().hide();
			((ClayComponent) link.getFromComponent()).fireLinkRemove(link, 0);
		}
		getClayModel().setHidden(this);
		((ClayModel) getClayModel()).fireComponentRemove(this, 0);
	}

	public void expand() {
		expanded = true;
		ILink link;
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);
			link.getFromComponent().visible();
		}

		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);
			((ClayComponent) link.getFromComponent()).fireLinkAdd(link);
		}
	}

	public void visible() {
		hidden = false;
		ILink link;
		getClayModel().setVisible(this);
		((ClayModel) getClayModel()).fireComponentAdd(this);
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);

			link.getFromComponent().visible();
		}
		for (int i = 0; i < inputLinks.size(); i++) {
			link = (ILink) inputLinks.get(i);

			((ClayComponent) link.getFromComponent()).fireLinkAdd(link);
		}
	}
	
	public List getVisibleInputLinks() {
		if(expanded && !hidden)return inputLinks;
		else return Collections.EMPTY_LIST;
	}
	
	public boolean isExpanded() {
		if(inputLinks.size() > 0) return expanded;
		else return false;
	}
	
	public boolean isCollapsed() {
		return !expanded;
	}
	
	public boolean hasErrors() {
		return getClayModel().getHelper().hasErrors((XModelObject)getSource());
	}
	
	public boolean isHidden() {
		return hidden;
	}
	
	public static final Color DEFAULT_FOREGROUND_COLOR = new Color(null, 0x00, 0x00, 0x00);
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(null, 0xE4, 0xE4, 0xE4);
	//public static String EXTENDS_PROPERTY = "extends";
	protected Color headerForegroundColor = DEFAULT_FOREGROUND_COLOR;
	protected Color headerBackgroundColor = DEFAULT_BACKGROUND_COLOR;
	Vector<IComponentListener> componentListeners = new Vector<IComponentListener>();
	Vector<ILink> inputLinks = new Vector<ILink>();
	public ILink link = null;

	public void dispose() {
		super.dispose();
		if (componentListeners != null)
			componentListeners.clear();
		componentListeners = null;
		if (inputLinks != null)
			inputLinks.clear();
		inputLinks = null;
	}

	public void addInputLink(ILink link) {
		if (!inputLinks.contains(link))
			inputLinks.add(link);
	}

	public void removeInputLink(ILink link) {
		inputLinks.remove(link);
	}

	protected Image icon = null;

	public ClayComponent(IClayModel model, XModelObject groupNode) {
		super(model, groupNode);
		icon = EclipseResourceUtil.getImage(groupNode);
		XModelObject[] objects = model.getHelper().getOutputs(groupNode);
		if (objects.length > 0) {
			link = new Link(this, objects[0]);
		}
	}
   
	public Image getImage() {
		return icon;
	}

	public String getExtends() {
		if (link != null && link.getToComponent() != null) {
			return link.getToComponent().getName();
		}
		return "";
	}

	boolean selected = false;

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean set) {
		boolean oldValue = selected;
		selected = set;
		this.propertyChangeSupport
				.firePropertyChange("selected", oldValue, set);
		if (set)
			this.getClayModel().setSelectedComponent(this);
	}

	public void clearSelection() {
		IClayElementList<IComponent> list = getClayModel().getComponentList();
		for (int i = 0; i < list.size(); i++) {
			IComponent activity = (IComponent) list.get(i);
			activity.setSelected(false);
		}
	}

	public ILink[] getLinks() {
		return getInputLinks();
	}

	public ILink[] getInputLinks() {
		return (ILink[]) inputLinks.toArray();
	}

	public Vector getListInputLinks() {
		return inputLinks;
	}

	public ILink getLink() {
		return link;
	}

	// Unit messages
	public void fireComponentChange() {
		if (componentListeners.size() == 0)
			return;
		Vector targets = (Vector) componentListeners.clone();
		for (int i = 0; i < targets.size(); i++) {
			IComponentListener listener = (IComponentListener) targets.get(i);
			if (listener != null) {
				listener.componentChange();
			}
		}
	}

	public void fireLinkAdd(ILink link) {
		Vector listeners = (Vector) this.componentListeners.clone();
		for (int i = 0; i < listeners.size(); i++) {
			IComponentListener listener = (IComponentListener) listeners.get(i);
			if (listener != null && listener.isComponentListenerEnable())
				((IComponentListener) listeners.get(i)).linkAdd(link);
		}
	}

	public void fireLinkRemove(ILink link, int index) {
		Vector listeners = (Vector) this.componentListeners.clone();
		for (int i = 0; i < listeners.size(); i++) {
			IComponentListener listener = (IComponentListener) listeners
					.get(i);
			if (listener != null && listener.isComponentListenerEnable())
				((IComponentListener) listeners.get(i)).linkRemove(link);
		}
	}

	// remove state from model
	public void addComponentListener(IComponentListener listener) {
		componentListeners.add(listener);
	}

   public void removeComponentListener(IComponentListener listener) {
   	componentListeners.remove(listener);
   }

   public void removeFromClayModel() {}
   
   public void remove(){
   		if(link != null){
   			ILink l = link;
   			link.getToComponent().removeInputLink(link);
   			link.remove();
   			link = null;
   			fireLinkRemove(l,0);
   		}
   }

	public void nodeChanged(Object eventData) {
		fireComponentChange();
		this.propertyChangeSupport.firePropertyChange("name", "", this
				.getSourceProperty("name"));
	}

	public void addLink(ILink link) {
		this.link = link;
		if (!link.setTarget())
			link = null;
	}
   
	public void removeLink(ILink lin) {
		ILink l = link;
		link.getToComponent().removeInputLink(link);
		link.remove();
		link = null;
		fireLinkRemove(l, 0);
	}

	public void structureChanged(Object eventData) {}

	public void nodeAdded(Object eventData) {
   		XModelObject[] objects = getClayModel().getHelper().getOutputs(source);
   		if(objects.length > 0 && link == null){
   			link = new Link(this,objects[0]);
   			if(!link.setTarget()) link = null;
   		}
   }

	public void nodeRemoved(Object eventData) {
		XModelTreeEvent event = (XModelTreeEvent) eventData;
		IClayElement removedLink = this.getFromMap(event.getInfo());
		if (removedLink == link) {
			ILink l = link;
			link.getToComponent().removeInputLink(link);
			link.remove();
			link = null;
			fireLinkRemove(l, 0);
		}
	}

	public boolean isElementListListenerEnable() {
		return true;
	}

	public void setElementListListenerEnable(boolean set) {}
   
	public boolean isConfirmed() {
		return !clayModel.getHelper().isUnconfirmedItem(source);
	}

	public boolean isAnotherFile() {
		return clayModel.getHelper().isNotDefinedInThisFile(source);
	}

}
