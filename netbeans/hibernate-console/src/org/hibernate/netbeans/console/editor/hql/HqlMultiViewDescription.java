package org.hibernate.netbeans.console.editor.hql;

import java.awt.Image;
import java.beans.BeanInfo;
import java.io.Serializable;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.hibernate.netbeans.console.editor.EditorMultiViewDescription;
import org.hibernate.netbeans.console.editor.EditorMultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.openide.util.HelpCtx;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class HqlMultiViewDescription extends EditorMultiViewDescription {
    
    private String id;

    private HqlMultiViewElement element;
    
    public HqlMultiViewDescription(SessionFactoryNode node) {
        super(node);
        this.id = "Error_" + node.getDescriptor().getStorageFile().getName();
    }

    public String getDisplayName() {
        return org.openide.util.NbBundle.getBundle(HqlMultiViewDescription.class).getString("LBL_QueryMultiView");
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public String preferredID() {
        return id;
    }

    public MultiViewElement createElement() {
        element = new HqlMultiViewElement(node);
        return element;
    }
    
    public void convertHql2Java() {
        element.getEditor().convertHql2Java();
    }

    public void convertJava2Hql() {
        element.getEditor().convertJava2Hql();
    }

    public EditorMultiViewElement getEditorElement() {
        return element;
    }

        
}
