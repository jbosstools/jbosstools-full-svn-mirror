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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.netbeans.editor.CharSeq;
import org.netbeans.modules.editor.NbEditorDocument;
import org.openide.ErrorManager;

/**
 * @author leon
 */
public class BshDocument extends NbEditorDocument {

    private SessionFactoryNode sfNode;

    public BshDocument(SessionFactoryNode sfNode, Class kitClass) {
        super(kitClass);
        this.sfNode = sfNode;
    }

    public SessionFactoryNode getSessionFactoryNode() {
        return sfNode;
    }

    protected void preInsertCheck(int offset, String text, AttributeSet a) throws BadLocationException {
        Element el = getCharacterElement(offset);
        Element parent = el.getParentElement();
        while (parent != null) {
            System.out.println("Parent is: " + parent);
            parent = parent.getParentElement();
        }
    }
    
}
