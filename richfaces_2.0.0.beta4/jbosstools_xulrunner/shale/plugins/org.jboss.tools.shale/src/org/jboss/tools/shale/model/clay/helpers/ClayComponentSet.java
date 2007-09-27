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
package org.jboss.tools.shale.model.clay.helpers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.event.*;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.jsf.web.JSFWebHelper;
import org.jboss.tools.shale.ShaleModelPlugin;
import org.jboss.tools.shale.model.clay.ShaleClayConstants;
import org.jboss.tools.shale.model.dialog.ShaleDialogConstants;

public class ClayComponentSet implements XModelTreeListener{
	static String ID = "ClayComponentSet";	

	public static synchronized ClayComponentSet getInstance(XModel model) {
    	ClayComponentSet instance = (ClayComponentSet)model.getManager(ID);
        if (instance == null) {
        	instance = new ClayComponentSet();
        	instance.setModel(model);
        	model.addManager(ID, instance);
        	model.addModelTreeListener(instance);
        }
        return instance;
    }

	XModel model;
	Set<XModelObject> files = new HashSet<XModelObject>();
	Map<String,XModelObject> components = new HashMap<String,XModelObject>();
	Set<IClayComponentSetListener> listeners = new HashSet<IClayComponentSetListener>();
	
	void setModel(XModel model) {
		this.model = model;
		update();
	}
	
	public Map<String,XModelObject> getDefinitions() {
		return components;
	}
	
	public void addClayComponentSetListener(IClayComponentSetListener listener) {
		listeners.add(listener);
	}

	public void removeClayComponentSetListener(IClayComponentSetListener listener) {
		listeners.remove(listener);
	}

	public void nodeChanged(XModelTreeEvent event) {
		processEvent(event);
	}

	public void structureChanged(XModelTreeEvent event) {
		processEvent(event);
	}
	
	private void processEvent(XModelTreeEvent event) {
		if(isRelevant(event)) update();
	}
	
	public boolean isRelevant(XModelTreeEvent event) {
		XModelObject source = event.getModelObject();
		String sourceEntity = source.getModelEntity().getName();
		if(sourceEntity.startsWith("WebApp")) return true;
		if(event.kind() == XModelTreeEvent.CHILD_ADDED) {
			XModelObject a = (XModelObject)event.getInfo();
			String addedEntity = a.getModelEntity().getName();
			if(addedEntity.equals(ShaleClayConstants.COMPONENT_ENTITY)) {
				return true;
			} else if(addedEntity.equals(ShaleClayConstants.ENT_SHALE_CLAY)) {
				return true;
			}
		} else if(event.kind() == XModelTreeEvent.CHILD_REMOVED) {
			if(sourceEntity.equals(ShaleClayConstants.ENT_SHALE_CLAY)) return true;
		} else if(event.kind() == XModelTreeEvent.NODE_CHANGED) {
			if(sourceEntity.equals(ShaleClayConstants.COMPONENT_ENTITY)) return true;
		}
		return false;
	}
	
	public void update() {
		files = getTileFiles(model);
		updateComponents();
	}
	
	Set<XModelObject> getTileFiles(XModel model) {
		ArrayList<XModelObject> list = new ArrayList<XModelObject>();
		Set<XModelObject> set = new HashSet<XModelObject>();
		JSFWebHelper.getConfigFiles(list, set, model, ShaleDialogConstants.REGISTRATION_DATA);
		XModelObject o = model.getByPath("FileSystems/lib-shale-clay.jar/META-INF/clay-config.xml");
		if(o != null) list.add(o);
		return set;
	}
	
	void updateComponents() {
		Map<String,XModelObject> d = new HashMap<String,XModelObject>();
		Iterator<XModelObject> it = files.iterator();
		while(it.hasNext()) {
			XModelObject o = it.next();
			XModelObject[] cs = o.getChildren(ShaleClayConstants.ENT_SHALE_CLAY);
			for (int i = 0; i < cs.length; i++) d.put(cs[i].getAttributeValue("name"), cs[i]);
		}
		Map<String,XModelObject> old = components;
		components = d;
		Set<XModelObject> removed = new HashSet<XModelObject>();
		Set<XModelObject> added = new HashSet<XModelObject>();
		Iterator<String> its = old.keySet().iterator();
		while(its.hasNext()) {
			String key = its.next();
			if(!d.containsKey(key)) removed.add(old.get(key));
		}
		its = d.keySet().iterator();
		while(its.hasNext()) {
			String key = its.next().toString();
			if(!old.containsKey(key)) added.add(d.get(key));
		}
		if(!removed.isEmpty() || !added.isEmpty()) {
			IClayComponentSetListener[] ls = listeners.toArray(new IClayComponentSetListener[0]);
			for (int i = 0; i < ls.length; i++) {
				try {
					ls[i].componentsChanged(removed, added);
				} catch (Exception e) {
					ShaleModelPlugin.getPluginLog().logError(e);
				}
			}
		}
	}

}
