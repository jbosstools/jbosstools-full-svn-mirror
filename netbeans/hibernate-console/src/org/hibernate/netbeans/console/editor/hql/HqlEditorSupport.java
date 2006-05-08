package org.hibernate.netbeans.console.editor.hql;

import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.NeverGetsModifiedEditorSupport;
import org.netbeans.modules.editor.plain.PlainKit;
import org.openide.loaders.DataObject;

/**
 * @author leon
 */
public class HqlEditorSupport extends NeverGetsModifiedEditorSupport {

    private SessionFactoryNode node;

    public HqlEditorSupport(SessionFactoryNode node) {
        this(createDataObject(node, "_editor.hql"));
        this.node = node;
    }

    public HqlEditorSupport(DataObject obj) {
        super(obj);
    }

    protected StyledDocument createStyledDocument(EditorKit kit) {
        return new HqlDocument(node, kit.getClass());
    }

    protected EditorKit createEditorKit() {
        return new HqlEditorKit();
    }

}
