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

package org.hibernate.netbeans.console.editor;

import java.awt.Image;
import java.beans.BeanInfo;
import java.io.Serializable;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public abstract class EditorMultiViewDescription implements MultiViewDescription, Serializable {
    
    protected SessionFactoryNode node;
    
    public EditorMultiViewDescription(SessionFactoryNode node) {
        this.node = node;
    }
    
    public String getCurrentQuery() {
        if (getEditorElement() == null) {
            return null;
        }
        return getEditorElement().getEditor().getCurrentQuery();
    }


    public Image getIcon() {
        return node.getIcon(BeanInfo.ICON_COLOR_16x16);
    }


    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    
    public void save() {
        if (getEditorElement() == null) {
            return;
        }
        getEditorElement().getEditor().save();
    }


    public void showWarning(String txt) {
        if (getEditorElement() == null) {
            return;
        }
        getEditorElement().getEditor().showWarning(txt);
    }
    
    public abstract EditorMultiViewElement getEditorElement();
    
}
