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

import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.EditorMultiViewElement;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

public class HqlMultiViewElement extends EditorMultiViewElement {

    private SessionFactoryNode node;

    private HqlEditor editor;
    
    public HqlMultiViewElement(SessionFactoryNode node) {
        this.node = node;
    }
    
    public HqlEditor getEditor() {
        if (editor == null) {
            editor = new HqlEditor(node);
        }
        return editor;
    }

    protected ProxyLookup createLookup() {
        return new ProxyLookup(new Lookup[] { editor.getLookup() });
    }
        
}
