package org.hibernate.netbeans.console.editor.bsh;

import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.EditorMultiViewElement;
import org.hibernate.netbeans.console.editor.SessionFactoryContentEditor;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 * @author leon
 */
public class BshMultiViewElement extends EditorMultiViewElement {
    
    private SessionFactoryContentEditor editor;
    
    private SessionFactoryNode node;
    
    public BshMultiViewElement(SessionFactoryNode node) {
        this.node = node;
    }

    protected ProxyLookup createLookup() {
        return new ProxyLookup(new Lookup[] { editor.getLookup() });
    }

    public SessionFactoryContentEditor getEditor() {
        if (editor == null) {
            editor = new BshEditor(node);
        }
        return editor;
    }
    
}
