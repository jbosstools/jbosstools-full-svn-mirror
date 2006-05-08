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
