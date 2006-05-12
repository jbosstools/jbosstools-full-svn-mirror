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

import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.openide.actions.OpenAction;
import org.openide.nodes.Node;

/**
 * @author leon
 */
public class OpenReloadAction extends OpenAction {
    
    public OpenReloadAction() {
    }

    public String getName() {
        return "Open / Reload";
    }

    protected void performAction(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            SessionFactoryCookie c = (SessionFactoryCookie) n.getCookie(SessionFactoryCookie.class);
            if (c != null) {
                if (c.isSessionOpen()) {
                    c.reload();
                } else {
                    c.open();
                }
            }
        }
    }


}
