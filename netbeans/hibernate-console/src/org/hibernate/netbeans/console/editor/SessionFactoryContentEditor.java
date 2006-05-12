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

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.netbeans.editor.Coloring;
import org.netbeans.editor.Utilities;
import org.openide.ErrorManager;
import org.openide.text.CloneableEditor;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 * @author leon
 */
public abstract class SessionFactoryContentEditor extends CloneableEditor {
    
    private CloneableEditorSupport support;
    
    private SessionFactoryNode node;
    
    private static final Coloring ERROR_COLORING;
    
    protected EditorContentHelper editorContentHelper;

    static {
        ERROR_COLORING = new Coloring(
                new Font("Monospaced", Font.PLAIN, 11), Color.WHITE, Color.RED);
    }

    public SessionFactoryContentEditor(SessionFactoryNode node, CloneableEditorSupport support) {
        super(support);
        this.support = support;
        this.node = node;
        this.editorContentHelper = new EditorContentHelper(getEditorPane());
    }

    public String getCurrentQuery() {
        return editorContentHelper.getCurrentQuery();
    }

    public void save() {
        try {
            support.saveDocument();
        }  catch (IOException ex) {
            ErrorManager.getDefault().notify(ex);
        }
    }


    public void showWarning(String txt) {
        Utilities.setStatusText(getEditorPane(), txt, ERROR_COLORING);
    }

    public Lookup getLookup() {
        Lookup l = super.getLookup();
        return new ProxyLookup(new Lookup[] { node.getLookup(), l });
    }
    
    
}
