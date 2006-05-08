package org.hibernate.netbeans.console.editor.bsh;

import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.SessionFactoryContentEditor;

/**
 * @author leon
 */
public class BshEditor extends SessionFactoryContentEditor {
    
    public BshEditor(SessionFactoryNode node) {
        super(node, new BshEditorSupport(node));
    }
    
}
