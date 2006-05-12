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

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JToolBar;
import javax.swing.text.Document;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.openide.awt.UndoRedo;
import org.openide.text.CloneableEditor;
import org.openide.text.NbDocument.CustomToolbar;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * @author leon
 */
public abstract class EditorMultiViewElement implements MultiViewElement {

    private JComponent toolBar;

    private CloneableEditor editor;
    
    private boolean loading = false;
    
    protected MultiViewElementCallback callback;
    
    public EditorMultiViewElement() {
    }

    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    public void componentActivated() {
    }

    public void componentClosed() {
    }

    public void componentDeactivated() {
    }

    public void componentHidden() {
    }

    public void componentOpened() {
    }

    public void componentShowing() {
    }

    public Action[] getActions() {
        return callback.createDefaultActions();
    }

    public final Lookup getLookup() {
        return new ProxyLookup(new Lookup[] { Lookups.singleton(this), createLookup() });
    }

    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }
    
    public final JComponent getToolbarRepresentation() {
        return getToolbarRepresentation(doGetEditor());
    }
    
    public final JComponent getVisualRepresentation() {
        return doGetEditor();
    }

    private CloneableEditor doGetEditor() {
        // XXX - workaround for http://editor.netbeans.org/issues/show_bug.cgi?id=75596
        if (loading) {
            return null;
        }
        if (editor == null) {
            loading = true;
            editor = getEditor();
            loading = false;
        }
        return editor;
    }
    
    public final UndoRedo getUndoRedo() {
        return doGetEditor().getUndoRedo();
    }        

    private synchronized JComponent getToolbarRepresentation(CloneableEditor editor) {
        if (toolBar == null) {
            final JEditorPane editorPane = editor.getEditorPane();
            if (editorPane != null) {
                Document doc = editorPane.getDocument();
                if (doc instanceof CustomToolbar) {
                    toolBar = ((CustomToolbar) doc).createToolbar(editorPane);
                }
            }
            if (toolBar == null) {
                toolBar = new JToolBar();
            }
        }
        return toolBar;
    }
    
    protected abstract ProxyLookup createLookup();
    
    public abstract SessionFactoryContentEditor getEditor();
    
}
