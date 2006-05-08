package org.hibernate.netbeans.console.editor.bsh;

import java.awt.Image;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.EditorMultiViewDescription;
import org.hibernate.netbeans.console.editor.EditorMultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class BshMultiViewDescription extends  EditorMultiViewDescription {
    
    private BshMultiViewElement element;
    
    public BshMultiViewDescription(SessionFactoryNode node) {
        super(node);
    }

    public String getDisplayName() {
        return "BeanShell";
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public String preferredID() {
        return "Bsh_" + node.getDescriptor().getName();
    }

    public MultiViewElement createElement() {
        element = new BshMultiViewElement(node);
        return element;
    }
    
    public EditorMultiViewElement getEditorElement() {
        return element;
    }
    
}
