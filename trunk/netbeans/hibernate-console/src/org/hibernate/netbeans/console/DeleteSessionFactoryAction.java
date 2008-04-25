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

package org.hibernate.netbeans.console;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * @author leon
 */
public class DeleteSessionFactoryAction extends CookieAction {
    
    public DeleteSessionFactoryAction() {
    }

    public int mode() {
        return MODE_ALL;
    }

    public Class[] cookieClasses() {
        return new Class[] { SessionFactoryCookie.class };
    }

    public void performAction(Node[] activatedNodes) {
        String msg;
        if (activatedNodes.length > 1) {
            msg = "Are you sure you want to delete the selected session factories?";
        } else {
            msg = "Are you sure you want to delete the selected session factory?";
        }
        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(msg, NotifyDescriptor.YES_NO_OPTION);
        if (NotifyDescriptor.OK_OPTION.equals(DialogDisplayer.getDefault().notify(nd))) {
            for (Node node : activatedNodes) {
                SessionFactoryCookie cookie = (SessionFactoryCookie) node.getCookie(SessionFactoryCookie.class);
                cookie.delete();
            }
        }
    }

    public String getName() {
        return NbBundle.getMessage(DeleteSessionFactoryAction.class, "LBL_Delete");
    } 

    public HelpCtx getHelpCtx() {
        return null;
    }
    
}
