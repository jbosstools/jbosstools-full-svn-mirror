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


package org.hibernate.netbeans.console.editor.hql;

import java.io.Serializable;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * @author leon
 */
public class JavaString2HqlAction extends CookieAction implements Serializable {

    private final static long serialVersionUID = 1;

    public String getName() {
        return NbBundle.getMessage(JavaString2HqlAction.class, "CTL_ConvertJava2Hql");
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    protected Class[] cookieClasses() {
        return new Class[] { HqlEditorCookie.class };
    }

    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes == null || activatedNodes.length == 0) {
            return;
        }
        HqlEditorCookie c = (HqlEditorCookie) activatedNodes[0].getCookie(HqlEditorCookie.class);
        if (c == null) {
            return;
        }
        c.convertJava2Hql();
    }
    
}
