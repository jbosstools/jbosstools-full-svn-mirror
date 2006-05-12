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

package org.hibernate.netbeans.console.editor.hql;

import java.io.Serializable;
import org.hibernate.netbeans.console.editor.EditorContentAction;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class Hql2JavaStringAction extends EditorContentAction implements Serializable {

    private final static long serialVersionUID = 1;

    public String getName() {
        return NbBundle.getMessage(Hql2JavaStringAction.class, "CTL_ConvertHql2Java");
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    protected void performAction(HqlEditorCookie c, String query) {
        c.convertHql2Java();
    }
    
}
