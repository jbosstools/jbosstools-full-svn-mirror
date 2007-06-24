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
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.event.*;
import org.jboss.tools.shale.model.clay.ShaleClayConstants;

public class ClayUpdateManager implements XModelTreeListener, ShaleClayConstants {
	static String ID = "org.jboss.tools.shale.model.clay.helpers.ClayUpdateManager";

    public static synchronized ClayUpdateManager getInstance(XModel model) {
    	ClayUpdateManager instance = (ClayUpdateManager)model.getManager(ID);
        if (instance == null) {
        	instance = new ClayUpdateManager();
        	model.addManager(ID, instance);
        	model.addModelTreeListener(instance);
        }
        return instance;
    }
    
    Set<ClayUpdateHelper> binds = new HashSet<ClayUpdateHelper>();

    public void register(String path, ClayUpdateHelper node) {
    	binds.add(node);
    }
    public void unregister(ClayUpdateHelper node) {
    	binds.remove(node);
    }

	public void nodeChanged(XModelTreeEvent event) {
		String entity = event.getModelObject().getModelEntity().getName();
		if(entity.equals(COMPONENT_ENTITY)) fireChange();
		else if(entity.startsWith("WebApp")) fireChange();
		
	}

	public void structureChanged(XModelTreeEvent event) {
		String sourceEntity = event.getModelObject().getModelEntity().getName();
		if(event.kind() == XModelTreeEvent.CHILD_REMOVED) {
			if(sourceEntity.equals(ENT_SHALE_CLAY)) fireChange();
			if(event.getInfo() instanceof XModelObject) {
//				XModelObject o = (XModelObject)event.getInfo();
//				String entity = o.getModelEntity().getName();
			}
		} else if(event.kind() == XModelTreeEvent.CHILD_ADDED) {
			XModelObject o = (XModelObject)event.getInfo();
			String entity = o.getModelEntity().getName();
			if(entity.equals(COMPONENT_ENTITY)) fireChange();
		}
		
	}
	
	void fireChange() {
		ClayUpdateHelper[] h = binds.toArray(new ClayUpdateHelper[0]);
		for (int i = 0 ; i < h.length; i++) h[i].update();
	}

}
