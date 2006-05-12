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

import java.awt.Image;
import java.beans.BeanInfo;
import java.io.Serializable;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.EditorMultiViewDescription;
import org.hibernate.netbeans.console.editor.EditorMultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class HqlMultiViewDescription extends EditorMultiViewDescription {
    
    private String id;

    private HqlMultiViewElement element;
    
    public HqlMultiViewDescription(SessionFactoryNode node) {
        super(node);
        this.id = "Error_" + node.getDescriptor().getStorageFile().getName();
    }

    public String getDisplayName() {
        return org.openide.util.NbBundle.getBundle(HqlMultiViewDescription.class).getString("LBL_QueryMultiView");
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public String preferredID() {
        return id;
    }

    public MultiViewElement createElement() {
        element = new HqlMultiViewElement(node);
        return element;
    }
    
    public void convertHql2Java() {
        element.getEditor().convertHql2Java();
    }

    public void convertJava2Hql() {
        element.getEditor().convertJava2Hql();
    }

    public EditorMultiViewElement getEditorElement() {
        return element;
    }

        
}
