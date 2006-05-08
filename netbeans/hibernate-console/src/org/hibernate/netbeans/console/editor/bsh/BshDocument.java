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
