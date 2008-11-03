/*******************************************************************************
 * Copyright (c) 2006 Jeff Mesnil
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    "Rob Stryker" <rob.stryker@redhat.com> - Initial implementation
 *******************************************************************************/
package org.jboss.tools.jmx.ui.internal.views.navigator;

import java.util.ArrayList;
import java.util.Iterator;


import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.jboss.tools.jmx.core.IConnectionWrapper;
import org.jboss.tools.jmx.ui.internal.actions.DeleteConnectionAction;
import org.jboss.tools.jmx.ui.internal.actions.DoubleClickAction;
import org.jboss.tools.jmx.ui.internal.actions.MBeanServerConnectAction;
import org.jboss.tools.jmx.ui.internal.actions.MBeanServerDisconnectAction;
import org.jboss.tools.jmx.ui.internal.actions.NewConnectionAction;

/**
 * The action provider as declared in plugin.xml
 * as relates to Common Navigator
 */
public class ActionProvider extends CommonActionProvider {
	private DoubleClickAction doubleClickAction;
	private NewConnectionAction newConnectionAction;
	public ActionProvider() {
		super();
	}

    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        doubleClickAction = new DoubleClickAction();
        newConnectionAction = new NewConnectionAction();
        aSite.getStructuredViewer().addSelectionChangedListener(doubleClickAction);
    }

    public void fillActionBars(IActionBars actionBars) {
        super.fillActionBars(actionBars);
        actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN,
              doubleClickAction);
    }

    public void fillContextMenu(IMenuManager menu) {
    	IConnectionWrapper[] connections = getWrappersFromSelection();
    	if( connections != null ) {
	    	if( !anyConnected(connections) && allControlable(connections))
	    		menu.add(new MBeanServerConnectAction(connections));
	    	else if( allControlable(connections))
	    		menu.add(new MBeanServerDisconnectAction(connections));

	    	menu.add(new DeleteConnectionAction(connections));
    	}

    	// Finish up
    	menu.add(new Separator());
    	menu.add(newConnectionAction);
    }

    protected boolean anyConnected(IConnectionWrapper[] connections) {
    	for( int i = 0; i < connections.length; i++ ) 
    		if( connections[i].isConnected())
    			return true;
    	return false;
    }
    protected boolean allControlable(IConnectionWrapper[] connections) {
    	for( int i = 0; i < connections.length; i++ ) 
    		if( !connections[i].canControl() )
    			return false;
    	return true;
    }
    
    protected IConnectionWrapper[] getWrappersFromSelection() {
    	ArrayList<IConnectionWrapper> list = new ArrayList<IConnectionWrapper>();
    	if( getContext() != null && getContext().getSelection() != null ) {
    		ISelection sel = getContext().getSelection();
    		if( sel instanceof IStructuredSelection ) {
    			Iterator i = ((IStructuredSelection)sel).iterator();
    			Object o;
    			while(i.hasNext()) {
	    			o = i.next();
    				if( o instanceof IConnectionWrapper ) {
    					list.add((IConnectionWrapper)o);
	    			}
    			}
    		}
    	}
    	return list.toArray(new IConnectionWrapper[list.size()]);
    }
}
