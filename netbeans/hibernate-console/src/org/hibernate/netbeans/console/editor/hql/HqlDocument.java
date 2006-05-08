package org.hibernate.netbeans.console.editor.hql;

import org.hibernate.netbeans.console.SessionFactoryNode;
import org.netbeans.modules.editor.NbEditorDocument;

/**
 * @author leon
 */
public class HqlDocument extends NbEditorDocument {

    private SessionFactoryNode sfNode;

    public HqlDocument(SessionFactoryNode sfNode, Class kitClass) {
        super(kitClass);
        this.sfNode = sfNode;
    }

    public SessionFactoryNode getSessionFactoryNode() {
        return sfNode;
    }
}
