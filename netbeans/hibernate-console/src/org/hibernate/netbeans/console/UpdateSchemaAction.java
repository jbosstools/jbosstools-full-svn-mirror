/*
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

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;

/**
 * @author leon
 */
public class UpdateSchemaAction extends CookieAction {
    
    /** Creates a new instance of ReloadSessionFactoryAction */
    public UpdateSchemaAction() {
    }

    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    protected Class[] cookieClasses() {
        return new Class[] { SessionFactoryCookie.class };
    }

    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 0 || activatedNodes.length > 1) {
            return;
        }
        SessionFactoryCookie cookie = (SessionFactoryCookie) activatedNodes[0].getCookie(SessionFactoryCookie.class);
        if (cookie != null) {
            cookie.updateSchema();
        }
    }

    public String getName() {
        return "Update schema"; // TODO - fixme
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    protected boolean asynchronous() {
        return false;
    }
    
}
