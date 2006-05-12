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

package org.hibernate.netbeans.console.explorer;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class ShowSessionFactoriesExplorerAction extends AbstractAction implements Serializable {

    private final static long serialVersionUID = 1;

    public ShowSessionFactoriesExplorerAction() {
        super(NbBundle.getMessage(ShowSessionFactoriesExplorerAction.class, "CTL_SessionFactoriesAction"));
    }
    
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = SessionFactoriesExplorer.findInstance();
        win.open();
        win.requestActive();
    }
    
}
