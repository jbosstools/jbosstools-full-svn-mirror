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
package org.jboss.tools.seam.pages.xml.model.helpers;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.event.*;
import org.jboss.tools.seam.pages.xml.SeamPagesXMLPlugin;
import org.jboss.tools.seam.pages.xml.model.SeamPagesConstants;
import org.jboss.tools.seam.pages.xml.model.impl.SeamPagesDiagramImpl;

public class SeamPagesPageRefUpdateManager implements XModelTreeListener, Runnable, SeamPagesConstants {

    public static SeamPagesPageRefUpdateManager getInstance(XModel model) {
		SeamPagesPageRefUpdateManager instance = (SeamPagesPageRefUpdateManager)model.getManager("JSFPageUpdateManager");
        if(instance == null) {
            instance = new SeamPagesPageRefUpdateManager();
            instance.model = model;
            instance.init();
			model.addManager("SeamPagesPageRefUpdateManager", instance);
            model.addModelTreeListener(instance);
        }
        return instance;
    }

    private static long id = 0;

    private XModel model;
    private Map<String,PageLinks> pages = new HashMap<String,PageLinks>();
    private int lock = 0;
    protected boolean stopped = false;
    
	PageUpdateRunnable runnable = new PageUpdateRunnable();

	public SeamPagesPageRefUpdateManager() {}
    
    private void init() {
    }

    public void updatePage(SeamPagesDiagramHelper h, XModelObject page) {
        PageLinks pl = getPageLinks(page);
        if(pl == null) return;
        pl.h = h;
        pl.update();
    }

    PageLinks getPageLinks(XModelObject page) {
        String pid = page.get("_page_id");
        if(pid == null) {
            pid = "" + (++id);
            page.set("_page_id", pid);
        }
        PageLinks pl = pages.get(pid);
        if(pl == null) {
            pl = new PageLinks();
            pl.setPage(page);
            pages.put(pid, pl);
        }
        return pl;
    }

    public void updateAll() {
        if(isLocked()) return;
        lock();
        try {
            String[] ks = pages.keySet().toArray(new String[0]);
            for (int i = 0; i < ks.length; i++) {
                PageLinks pl = pages.get(ks[i]);
                if(pl == null) continue;
                if(!pl.page.isActive()) {
                    pages.remove(ks[i]);
                } else {
                    pl.update();
                }
            }
        } finally {
            unlock();
        }
    }
    
    public boolean isLocked() {
    	return lock > 0;
    }

    public void lock() {
        lock++;
    }

    public void unlock() {
        lock--;
    }

    public void structureChanged(XModelTreeEvent event) {
        XModel model = event.getModelObject().getModel();
        if (event.kind() == XModelTreeEvent.STRUCTURE_CHANGED &&
                event.getModelObject() == model.getRoot()) {
            model.removeModelTreeListener(this);
			SeamPagesPageRefUpdateManager instance = (SeamPagesPageRefUpdateManager)model.getManager("JSFPageUpdateManager");
			if(instance != null) { 
				instance.stopped = true;
				model.removeManager("JSFPageUpdateManager");
			}
            return;
        } else if(event.kind() == XModelTreeEvent.CHILD_ADDED) {
            onChildAdded(event);
        }
        if(isLocked()) return;
        XJob.addRunnable(runnable);
    }

    private void onChildAdded(XModelTreeEvent event) {
        XModelObject c = (XModelObject)event.getInfo();
        if(!c.getModelEntity().getName().equals("JSFProcessGroup")) return;
        SeamPagesDiagramImpl pi = (SeamPagesDiagramImpl)event.getModelObject();
        PageLinks pl = getPageLinks(c);
        if(pl == null || pi.getHelper() == null) return;
        pl.h = pi.getHelper();
    }

    public void nodeChanged(XModelTreeEvent event) {
        if(isLocked()) return;
        XJob.addRunnable(runnable);
    }

    public void run() {
        while(!stopped) {
            synchronized(this) {
                try {
                	wait();
                } catch (InterruptedException e) {
                	//ignore
                }
            }
            try {
            	Thread.sleep(250);
            } catch (InterruptedException e) {
            	//ignore
            }
            if(stopped) break;
            if(!isLocked()) {
                try {
                	updateAll();
                } catch (Exception t) {
        			SeamPagesXMLPlugin.log("Error while running page update", t);
                }
            }
        }
    }
    
    class PageUpdateRunnable implements XJob.XRunnable {

		public String getId() {
			return "Seam Pages Reference Update -" + XModelConstants.getWorkspace(model);
		}

		public void run() {
            if(!isLocked()) {
               	updateAll();
            }
		}
    	
    }
}

class PageLinks implements SeamPagesConstants {
	SeamPagesDiagramHelper h;
    XModelObject page;
    XModelObject jsp;
    boolean confirmed = false;
    long jspTimeStamp;
    long pageTimeStamp;
    
    public void setPage(XModelObject page) {
    	this.page = page;
    }

    public void update() {
        if(page.getParent() == null) return;
//        boolean modified = (pageTimeStamp != page.getTimeStamp());
        pageTimeStamp = page.getTimeStamp();

        String path = page.getAttributeValue(ATTR_PATH);
        if(path == null) {
            jsp = null;
            setConfirmed(false);
            jspTimeStamp = -1;
            return;
        }
		boolean isPattern = SeamPagesDiagramStructureHelper.instance.isPattern(page);
		if(path.indexOf('?') >= 0) {
			path = path.substring(0, path.indexOf('?'));
		}
        XModelObject jsp1 = (isPattern) ? null : page.getModel().getByPath(path);
        if(jsp == null || !jsp.isActive() || jsp != jsp1) {
            jsp = jsp1;
            setConfirmed(jsp != null);
            jspTimeStamp = -1;
        }
    }

    private void setConfirmed(boolean b) {
        confirmed = b;
        if(confirmed == ("true".equals(page.get("confirmed")))) return;
        page.setAttributeValue("confirmed", "" + confirmed);
    }

}
