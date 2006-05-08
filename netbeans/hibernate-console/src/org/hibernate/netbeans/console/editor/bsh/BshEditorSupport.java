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
