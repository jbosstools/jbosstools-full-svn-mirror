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
import org.jboss.tools.common.model.util.XModelTreeListenerSWTSync;
import org.jboss.tools.common.model.ui.action.XModelObjectActionList;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.xml.sax.SAXException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.XModelTreeEvent;
import org.jboss.tools.common.model.event.XModelTreeListener;
import org.jboss.tools.shale.model.clay.ClayPreference;
import org.jboss.tools.shale.model.clay.helpers.ClayStructureHelper;
import org.jboss.tools.shale.ui.ShaleUiPlugin;
import org.jboss.tools.shale.ui.clay.editor.model.*;

public class ClayModel extends ClayElement implements IClayModel, PropertyChangeListener, XModelTreeListener {
	Vector<IComponent> visibleComponents = new Vector<IComponent>();
	static final int DEFAULT_VERTICAL_SPACING = 20;
	static final int DEFAULT_HORIZONTAL_SPACING = 185;
	
	public Vector<IComponent> getVisibleComponentList() {
		return visibleComponents;
	}
	public void setHidden(IComponent component) {
		visibleComponents.remove(component);
		fireComponentRemove(component,0);
	}
	public void setVisible(IComponent component) {
		visibleComponents.add(component);
		fireComponentAdd(component);
	}
	protected Vector<IClayModelListener> strutsModelListeners = new Vector<IClayModelListener>();
	protected Vector errors = new Vector();
	protected ClayHashtable map = new ClayHashtable();

	protected ClayElementList<IComponent> componentList = new ComponentList();

	protected ClayStructureHelper helper = ClayStructureHelper.instance;
	protected ClayOptions options;

	protected boolean modified = false;

	public ClayModel() {
		try {
			setName("Struts Model");
		} catch (Exception ex) {
			ShaleUiPlugin.log(ex);
		}
	}
   
    public void dispose() {
		this.disconnectFromModel();
		if (map != null)
			map.dispose();
		map = null;
		if (strutsModelListeners != null)
			strutsModelListeners.clear();
		strutsModelListeners = null;
		if (errors != null)
			errors.clear();
		errors = null;
		if (componentList != null)
			componentList.dispose();
		componentList = null;
		if (options != null)
			options.dispose();
		options = null;
	}
   
	public boolean isBorderPaint() {
		return false;
	}

	public IClayOptions getOptions() {
		return options;
	}

	public ClayModel(Object data) throws SAXException, Exception {
		this();
		setData(data);
		map.setData((XModelObject) data);
	}

	public void updateLinks() {
		for (int i = 0; i < getComponentList().size(); i++) {
			IComponent component = (IComponent) getComponentList().get(i);
			if (component.getLink() != null)
				((ILink) component.getLink()).setTarget();
		}
	}

	public Object get(String name) {
		return null;
	}

	public void put(String name, Object value) {}

	public ClayStructureHelper getHelper(){
		return helper;
	}

	public int getProcessItemCounter() {
		return componentList.size();
	}

	public IComponent getComponent(int index) {
		return (IComponent) componentList.get(index);
	}

	public IComponent getComponent(String groupName) {
		return (IComponent) componentList.get(groupName);
	}

	public IComponent getComponent(Object source) {
		IComponent[] is = (IComponent[]) componentList.elements
				.toArray(new IComponent[0]);
		for (int i = 0; i < is.length; i++)
			if (is[i].getSource() == source)
				return is[i];
		return null;
	}

   // Module removers
	public void removeGroup(String moduleName) {}

	public void removeGroup(IComponent removeProcessItem) {
	}

	public void propertyChange(PropertyChangeEvent pce) {
	}

	IComponent selectedComponent = null;

	public void setSelectedComponent(IComponent c) {
		IComponent oldValue = selectedComponent;
		selectedComponent = c;
		propertyChangeSupport.firePropertyChange("selectedProcessItem",
				oldValue, c);
	}

	public IComponent getSelectedComponent() {
		return selectedComponent;
	}

	public String getText() {
		return "";
	}

	XModelTreeListenerSWTSync listener = null;

	public void setData(Object data) throws Exception {
		source = helper.getProcess((XModelObject) data);
		if (source == null) {
			return;
		}
		map.put(source.getPath(), this);
		componentList = new ComponentList(source);
		listener = new XModelTreeListenerSWTSync(this);
		source.getModel().addModelTreeListener(listener);
		options = new ClayOptions();
	}
   
	public void disconnectFromModel() {
		if (listener != null)
			source.getModel().removeModelTreeListener(listener);
		options.disconnectFromModel();
		map.disconnectFromModel();
	}

	public boolean isEditable() {
		return source != null
				&& source.getModelEntity().isEditable(source, "body");
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean set) {
		boolean oldValue = modified;
		modified = set;
		propertyChangeSupport
				.firePropertyChange("modified", oldValue, modified);
	}

	// fire events
	public void fireProcessChanged() {
		if (strutsModelListeners == null)
			return;
		Vector targets = (Vector) strutsModelListeners.clone();
		for (int i = 0; i < targets.size(); i++) {
			IClayModelListener listener = (IClayModelListener) targets.get(i);
			if (listener != null) {
				listener.processChanged();
			}
		}
		setModified(true);
	}

	public void fireComponentAdd(IComponent newComponent) {
		Vector targets = (Vector) strutsModelListeners.clone();
		for (int i = 0; i < targets.size(); i++) {
			IClayModelListener listener = (IClayModelListener) targets.get(i);
			if (listener != null) {
				listener.componentAdd(newComponent);
			}
		}
		setModified(true);
	}

	public void fireComponentRemove(IComponent newComponent, int index) {
		Vector targets = (Vector) strutsModelListeners.clone();
		for (int i = 0; i < targets.size(); i++) {
			IClayModelListener listener = (IClayModelListener) targets.get(i);
			if (listener != null) {
				listener.componentRemove(newComponent);
			}
		}
		setModified(true);
	}

	public void fireLinkAdd(ILink newLink) {
		Vector targets = (Vector) strutsModelListeners.clone();
		for (int i = 0; i < targets.size(); i++) {
			IClayModelListener listener = (IClayModelListener) targets.get(i);
			if (listener != null) {
				listener.linkAdd(newLink);
			}
		}
		setModified(true);
	}

	public void fireLinkRemove(ILink newLink) {
		Vector targets = (Vector) strutsModelListeners.clone();
		for (int i = 0; i < targets.size(); i++) {
			IClayModelListener listener = (IClayModelListener) targets.get(i);
			if (listener != null) {
				listener.linkRemove(newLink);
			}
		}
		setModified(true);
	}

	public void addClayModelListener(IClayModelListener listener) {
		strutsModelListeners.add(listener);
	}

	public void removeClayModelListener(IClayModelListener listener) {
		if (strutsModelListeners != null)
			strutsModelListeners.remove(listener);
	}

	public void remove() {}

	public IClayElementList<IComponent> getComponentList() {
		return componentList;
	}

	public void nodeChanged(XModelTreeEvent event) {
		try {
			if (map == null)
				return;
			fireProcessChanged();
			IClayElement element = (ClayElement) map.get(event.getInfo());
			if (element != null
					&& !event.getModelObject().getPath()
							.equals(event.getInfo())) {
				updateCash((String) event.getInfo());
			}
			String path = event.getModelObject().getPath();
			element = (path == null) ? null : (IClayElement) map.get(path);
			if (element == null) {
				return;
			}
			element.nodeChanged(event);
		} catch (Exception exc) {
			ShaleUiPlugin.log(exc);
		}
	}

	public void structureChanged(XModelTreeEvent event) {
		ClayElement element;
		try {
			Object obj = event.getModelObject().getPath();
			if (obj == null)
				return;
			if (map == null)
				return;
			element = (ClayElement) map.get(obj);
			if (element == null) {
				return;
			}
			if (event.kind() == XModelTreeEvent.STRUCTURE_CHANGED) {
				element.structureChanged(event);
			} else if (event.kind() == XModelTreeEvent.CHILD_ADDED) {
				element.nodeAdded(event);
			} else if (event.kind() == XModelTreeEvent.CHILD_REMOVED) {
				element.nodeRemoved(event);
			}
		} catch (Exception exc) {
			ShaleUiPlugin.log(exc);
		}
	}

	public void putToMap(String key,IClayElement value) {
		getMap().put(key,value);
	}

	public void removeFromMap(Object key) {
		getMap().remove(key);
	}

	public IClayElement getFromMap(Object key) {
		return getMap().get(key);
	}

	public class ComponentPropertyChangeListener implements
			PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent event) {
			IComponent processItem = (IComponent) event.getSource();
			if (event.getPropertyName().equals("selected")) {
				if (((Boolean) event.getNewValue()).booleanValue())
					setSelectedComponent(processItem);
			}
		}
	}

	public class ComponentList extends ClayElementList<IComponent> {
		protected ComponentList() {
		}

		public ComponentList(XModelObject processItemSource) {
			super(ClayModel.this, ClayModel.this.source);
			if (((XModelObject) ClayModel.this.getSource()).getPath() == null)
				return;

			XModelObject[] componentNodeList = getHelper().getItems(
					processItemSource);

			for (int i = 0; i < componentNodeList.length; i++) {
				IComponent newComponent = new ClayComponent(ClayModel.this,
						componentNodeList[i]);
				newComponent.addPropertyChangeListener("selected",
						new ComponentPropertyChangeListener());
				add(newComponent);
				visibleComponents.add(newComponent);
			}
		}

		public void structureChanged(Object eventData) {
		}

		public void nodeAdded(Object eventData) {
			XModelTreeEvent event = (XModelTreeEvent) eventData;
			IComponent newProcessItem = new ClayComponent(ClayModel.this,
					((XModelObject) event.getInfo()));
			this.add(newProcessItem);
			visibleComponents.add(newProcessItem);
			fireComponentAdd(newProcessItem);
		}

		public void nodeRemoved(Object eventData) {
			XModelTreeEvent event = (XModelTreeEvent) eventData;
			IClayElement removedProcessItem = this.getFromMap(event.getInfo());
			int index = this.indexOf(removedProcessItem);
			removedProcessItem.remove();
			this.remove(removedProcessItem);
			visibleComponents.remove(removedProcessItem);
			this.removeFromMap(((XModelTreeEvent) eventData).getInfo());
			fireComponentRemove((IComponent) removedProcessItem, index);
			clearCash((String) event.getInfo());
		}
	}

   public ClayHashtable getMap() {
      return map;
   }

   public IClayElement findElement(String key){
      return map.get(key);
   }

	public class ClayHashtable  implements XModelTreeListener{
		Hashtable<String,IClayElement> map = new Hashtable<String,IClayElement>();
		XModelObject source;
		String name;

		public void dispose() {
			disconnectFromModel();
			if (map!=null) map.clear();
			map = null;
		}

		public void put(String key,IClayElement value) {
			map.put(key,value);
		}
      
		public void setData(XModelObject data) {
			source = data;
			source.getModel().addModelTreeListener(ClayHashtable.this);
			name = source.getAttributeValue("name");
		}

		public void disconnectFromModel() {
			source.getModel().removeModelTreeListener(ClayHashtable.this);
		}

		public IClayElement get(Object key) {
			return (IClayElement) map.get(key);
		}

		public void remove(Object key) {
			map.remove(key);
		}

		public void nodeChanged(XModelTreeEvent event) {
			String path;
			ClayElement element;

			if (!source.getAttributeValue("name").equals(name)) {
				name = source.getAttributeValue("name");
				Enumeration keys = map.keys();
				while (keys.hasMoreElements()) {
					path = (String) keys.nextElement();
					element = (ClayElement) map.get(path);
					if (element != null) {
						if (element.getSource() != null) {
							map.remove(path);
							map.put(((XModelObject) element.getSource())
									.getPath(), element);
						}
					}
				}
			}
		}

		public void structureChanged(XModelTreeEvent event) {
		}
      
	}

	protected void clearCash(String path) {
		updateCash(path, true);
	}

	protected void updateCash(String path) {
		updateCash(path, false);
	}

	protected void updateCash(String path, boolean clear) {
		String rpath = path + "/";
		Object[] ks = map.map.keySet().toArray();
		for (int i = 0; i < ks.length; i++) {
			if (!ks[i].equals(path) && !ks[i].toString().startsWith(rpath))
				continue;
			IClayElement n = (IClayElement) map.map.get(ks[i]);
			map.map.remove(ks[i]);
			if (clear)
				continue;
			XModelObject o = (XModelObject) n.getSource();
			if (!o.isActive())
				continue;
			map.map.put(o.getPath(), n);
		}
	}

	public Menu getPopupMenu(Control control, Object environment) {
		if (source == null)
			return null;
		if (source.getModelEntity().getActionList().getActionItems().length != 0) {
			XModelObjectActionList l = new XModelObjectActionList(source
					.getModelEntity().getActionList(), source, null,
					environment);

			Menu menu = l.createMenu(control);
			return menu;
		}
		return null;
	}

	public Menu getPopupMenu(Control control) {
		return getPopupMenu(control, null);
	}

	public boolean isConfirmed() {
		return true;
	}
   
	class ClayOptions implements XModelTreeListener, IClayOptions{
		XModelObject optionsObject = ModelUtilities.getPreferenceModel().getByPath(ClayPreference.CLAY_DIAGRAM_PATH);
		XModelTreeListenerSWTSync optionsListener = new XModelTreeListenerSWTSync(this);
		Font componentNameFont = null;
	
		public ClayOptions(){
			optionsObject.getModel().addModelTreeListener(optionsListener);
		}
   	 
		public void dispose() {
			disconnectFromModel();
			if (componentNameFont!=null && componentNameFont.isDisposed()) componentNameFont.dispose();
			componentNameFont = null;
		}
   	 
   	 public int getVerticalSpacing() {
			String str = ClayPreference.VERTICAL_SPACING.getValue();
			if (str == null)
				return DEFAULT_VERTICAL_SPACING;
			if (str.indexOf("default") >= 0)
				return DEFAULT_VERTICAL_SPACING;
			try {
				return new Integer(str).intValue();
			} catch (Exception ex) {
				return DEFAULT_VERTICAL_SPACING;
			}
		}

		public int getHorizontalSpacing() {
			String str = ClayPreference.HORIZONTAL_SPACING.getValue();
			if (str == null)
				return DEFAULT_HORIZONTAL_SPACING;
			if (str.indexOf("default") >= 0)
				return DEFAULT_HORIZONTAL_SPACING;
			try {
				return new Integer(str).intValue();
			} catch (Exception ex) {
				return DEFAULT_HORIZONTAL_SPACING;
			}
		}

		public String getAlignment() {
			String str = ClayPreference.ALIGNMENT.getValue();
			return str;
		}

		public boolean isAnimateLayout() {
			String str = ClayPreference.ANIMATION.getValue();
			return (str != null && str.equals("yes"));
		}

		public Font getComponentNameFont() {
			String name;
			int size = 8, style = 1;
			int pos, pos2, pos3;
			String str = ClayPreference.COMPONENT_NAME_FONT.getValue();
			pos = str.indexOf(",");
			if (pos < 0)
				name = str;
			else {
				name = str.substring(0, pos);
				pos2 = str.indexOf("size=");
				if (pos2 >= 0) {
					pos3 = str.indexOf(",", pos2);
					if (pos3 < 0)
						size = new Integer(str
								.substring(pos2 + 5, str.length())).intValue();
					else
						size = new Integer(str.substring(pos2 + 5, pos3))
								.intValue();
				}
				pos2 = str.indexOf("style=");
				if (pos2 >= 0) {
					pos3 = str.indexOf(",", pos2);
					if (pos3 < 0)
						style = new Integer(str.substring(pos2 + 6, str
								.length())).intValue();
					else
						style = new Integer(str.substring(pos2 + 6, pos3))
								.intValue();
				}
			}
			if (componentNameFont == null) {
				componentNameFont = new Font(null, name, size, style);
			} else {
				if (!componentNameFont.getFontData()[0].getName().equals(name)
						|| componentNameFont.getFontData()[0].getHeight() != size
						|| componentNameFont.getFontData()[0].getStyle() != style) {
					componentNameFont = new Font(null, name, size, style);
				}
			}
			return componentNameFont;
		}

		public void disconnectFromModel() {
			optionsObject.getModel().removeModelTreeListener(optionsListener);
			if (optionsListener != null)
				optionsListener.dispose();
			optionsListener = null;
		}

		public void nodeChanged(XModelTreeEvent event) {
			fireProcessChanged();
			ClayComponent component;
			for (int i = 0; i < getComponentList().size(); i++) {
				component = (ClayComponent) getComponentList().get(i);
				component.fireComponentChange();
			}
		}
   	 
		public void structureChanged(XModelTreeEvent event) {}
	}

}
