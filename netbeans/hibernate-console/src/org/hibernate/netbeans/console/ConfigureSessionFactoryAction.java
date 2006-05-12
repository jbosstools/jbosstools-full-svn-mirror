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

import org.openide.ErrorManager;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * @author leon
 */
public class ConfigureSessionFactoryAction extends CookieAction {
    
    public ConfigureSessionFactoryAction() {
    }
    
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }
    
    public Class[] cookieClasses() {
        return new Class[] { SessionFactoryCookie.class };
    }
    
    public void performAction(Node[] activatedNodes) {
        if (activatedNodes.length != 1) {
            return;
        }
        Node n = activatedNodes[0];
        SessionFactoryCookie c = (SessionFactoryCookie) n.getCookie(SessionFactoryCookie.class);
        c.configure();
    }
    
    public String getName() {
        return NbBundle.getMessage(ConfigureSessionFactoryAction.class, "LBL_Configure");
    }
    
    public HelpCtx getHelpCtx() {
        return null;
    }

    public boolean asynchronous() {
        return true;
    }
    
}
