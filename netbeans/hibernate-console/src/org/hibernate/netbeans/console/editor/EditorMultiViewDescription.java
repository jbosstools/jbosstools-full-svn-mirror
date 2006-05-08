package org.hibernate.netbeans.console.editor;

import java.awt.Image;
import java.beans.BeanInfo;
import java.io.Serializable;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.netbeans.core.spi.multiview.MultiViewDescription;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public abstract class EditorMultiViewDescription implements MultiViewDescription, Serializable {
    
    protected SessionFactoryNode node;
    
    public EditorMultiViewDescription(SessionFactoryNode node) {
        this.node = node;
    }
    
    public String getCurrentQuery() {
        if (getEditorElement() == null) {
            return null;
        }
        return getEditorElement().getEditor().getCurrentQuery();
    }


    public Image getIcon() {
        return node.getIcon(BeanInfo.ICON_COLOR_16x16);
    }


    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_NEVER;
    }

    
    public void save() {
        if (getEditorElement() == null) {
            return;
        }
        getEditorElement().getEditor().save();
    }


    public void showWarning(String txt) {
        if (getEditorElement() == null) {
            return;
        }
        getEditorElement().getEditor().showWarning(txt);
    }
    
    public abstract EditorMultiViewElement getEditorElement();
    
}
