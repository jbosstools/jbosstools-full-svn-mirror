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

package org.hibernate.netbeans.console.editor.bsh;

import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.NeverGetsModifiedEditorSupport;

/**
 * @author leon
 */
public class BshEditorSupport extends NeverGetsModifiedEditorSupport {
    
    private SessionFactoryNode node;
    
    public BshEditorSupport(SessionFactoryNode node) {
        super(createDataObject(node, "_editor.hbsh"));
        this.node = node;
    }

    protected EditorKit createEditorKit() {
        return new BshEditorKit();
    }

    protected StyledDocument createStyledDocument(EditorKit kit) {
        return new BshDocument(node, kit.getClass());
    }
    


}
