package org.hibernate.netbeans.console.editor.hql;

import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.SessionFactoryContentEditor;

/**
 * @author leon
 */
public class HqlEditor extends SessionFactoryContentEditor {
    
    public HqlEditor(SessionFactoryNode node) {
        super(node, new HqlEditorSupport(node));
    }

    void convertHql2Java() {
        editorContentHelper.convertHql2Java();
    }
    
    void convertJava2Hql() {
        editorContentHelper.convertJava2Hql();
    }
    
}
