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

import java.beans.*;
import java.util.*;
import org.jboss.tools.common.model.ui.action.XModelObjectActionList;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.jboss.tools.common.meta.XAttribute;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.shale.ui.clay.editor.model.*;

public class ClayElement implements IClayElement {
	protected String name = "";
	protected boolean visible = false;
	protected boolean hidden = false;
	protected boolean deleted = false;
	protected IClayElement parent;
	protected IClayModel clayModel;

	protected String iconPath;

	protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	protected VetoableChangeSupport vetoableChangeSupport = new VetoableChangeSupport(this);

	protected XModelObject source;
	protected Hashtable map = new Hashtable();

	public ClayElement() {}

	public ClayElement(IClayElement parent) {
		this.parent = parent;
		clayModel = getClayModel();
	}

	public ClayElement(IClayElement parent, XModelObject source) {
		this.parent = parent;
		this.source = source;
		clayModel = getClayModel();
		((ClayModel)clayModel).putToMap(source.getPath(),this);
	}

	public void dispose() {
		vetoableChangeSupport = null;
		propertyChangeSupport = null;
		if (map != null) map.clear();
		map = null;
	}
	   
	public Object getSource() {
		return source;
	}

	public void setSource(Object obj) {
		source = (XModelObject)obj;
	}

	public String getText() {
		return "";
	}

	public IClayElement getRoot() {
		IClayElement current = this;
		while(current.getParentClayElement() != null) {
			current = current.getParentClayElement();
		}
		return current;
	}

	public IClayModel getClayModel() {
		IClayElement model = getRoot();
		return (model instanceof IClayElement) ? (IClayModel)model : null;
	}

	public String getJSFElementPath() {
		IClayElement current = this;
		String path = current.getName();
		while(current.getParentClayElement()!=null) {
			current = current.getParentClayElement();
			path = current.getName() + "/" + path;
		}
		return path;
	}

	public void updateModelModifiedProperty(Object oldValue,Object newValue) {
		if(getClayModel() == null) return;
		boolean modified = (oldValue == null) ? newValue != null : !oldValue.equals(newValue);
		getClayModel().setModified(modified);
	}

	public void updateModelModifiedProperty(int oldValue,int newValue) {
		if(getClayModel() == null) return;
		getClayModel().setModified(newValue != oldValue);
	}

	public void updateModelModifiedProperty(boolean oldValue,boolean newValue) {
		if(getClayModel() == null) return;
		getClayModel().setModified(newValue != oldValue);
	}

	public IClayElement getParentClayElement() {
		return parent;
	}

	public void setParentClayElement(IClayElement element) {
		IClayElement oldValue = parent;
		parent = element;
		clayModel = getClayModel();
		propertyChangeSupport.firePropertyChange("parent",oldValue,element);
		updateModelModifiedProperty(oldValue,element);
	}

	public String getName() {
		return source.getAttributeValue(NAME_PROPERTY);
	}

//	public String getTarget() {
//		return source.getAttributeValue(TARGET_PROPERTY);
//	}

	public void setName(String name) throws PropertyVetoException {
		String oldValue = this.name;
		vetoableChangeSupport.fireVetoableChange("name",oldValue,name);
		this.name = name;
	}

	public void setSourceProperty(String name,Object value) {}

	public Object getSourceProperty(String name) {
		return source.getAttributeValue(name);
	}

	public Object getSourceProperty(int index) {
		return null;
	}

	public int getSourcePropertyCounter() {
		return 0;
	}

	public String[] getSourcePropertyNames() {
		XModelObject mobject = (XModelObject)source;
		XAttribute[] attributes = mobject.getModelEntity().getAttributes();
		String[] attributeNames = new String[attributes.length];
		for(int i = 0; i < attributeNames.length; i++) {
			attributeNames[i] = attributes[i].getName();
		}
		return attributeNames;
	}

	public String[] getSourcePropertyDisplayNames() {
		return getSourcePropertyNames();
	}

	public void remove() {}

	//Support for vetoable change

	public void addVetoableChangeListener(VetoableChangeListener l) {
		vetoableChangeSupport.addVetoableChangeListener(l);
	}

	public void removeVetoableChangeListener(VetoableChangeListener l) {
		vetoableChangeSupport.removeVetoableChangeListener(l);
	}

	public void addVetoableChangeListener(String propertyName,VetoableChangeListener l) {
		vetoableChangeSupport.addVetoableChangeListener(propertyName,l);
	}

	public void removeVetoableChangeListener(String propertyName,VetoableChangeListener l) {
		vetoableChangeSupport.removeVetoableChangeListener(propertyName,l);
	}

	//Support for unvetoable change

	public void addPropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(l);
	}

	public void addPropertyChangeListener(String propertyName,PropertyChangeListener l) {
		propertyChangeSupport.addPropertyChangeListener(propertyName,l);
	}

	public void removePropertyChangeListener(String propertyName,PropertyChangeListener l) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,l);
	}

	public Object clone() {
		ClayElement newElement = new ClayElement();
		newElement.source = source.copy();
		return newElement;
	}

	public Enumeration children() {
		return null;
	}

	public boolean isLeaf() {
		return true;
	}

	public void removeAllListeners() {
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	public void structureChanged(Object eventData) {}

	public void nodeChanged(Object eventData) {}

	public void nodeAdded(Object eventData) {}

	public void nodeRemoved(Object eventData) {}

	public ClayModel.ClayHashtable getMap() {
		return ((ClayModel)getClayModel()).getMap();
	}

	public void removeFromMap(Object key) {
		((ClayModel)getClayModel()).removeFromMap(key);
	}

	public IClayElement getFromMap(Object key) {
		return ((ClayModel)getClayModel()).getFromMap(key);
	}
	   
	public Menu getPopupMenu(Control control, Object environment) {
		if(getSource() == null) return null;
		if(((XModelObject)getSource()).getModelEntity().getActionList().getActionItems().length!=0){
			XModelObjectActionList l = new XModelObjectActionList(((XModelObject)getSource()).getModelEntity().getActionList(), ((XModelObject)getSource()), null, environment);
			return l.createMenu(control);
		}
		return null;
	}

	public Menu getPopupMenu(Control control) {
		return getPopupMenu(control, null);
	}

	public boolean isConfirmed(){
		return false;
	}

}
