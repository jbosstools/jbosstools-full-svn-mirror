package org.hibernate.netbeans.console.editor;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import org.hibernate.netbeans.console.SessionFactoryNode;
import org.netbeans.editor.Coloring;
import org.netbeans.editor.Utilities;
import org.openide.ErrorManager;
import org.openide.text.CloneableEditor;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.Lookup;
import org.openide.util.lookup.ProxyLookup;

/**
 * @author leon
 */
public abstract class SessionFactoryContentEditor extends CloneableEditor {
    
    private CloneableEditorSupport support;
    
    private SessionFactoryNode node;
    
    private static final Coloring ERROR_COLORING;
    
    protected EditorContentHelper editorContentHelper;

    static {
        ERROR_COLORING = new Coloring(
                new Font("Monospaced", Font.PLAIN, 11), Color.WHITE, Color.RED);
    }

    public SessionFactoryContentEditor(SessionFactoryNode node, CloneableEditorSupport support) {
        super(support);
        this.support = support;
        this.node = node;
        this.editorContentHelper = new EditorContentHelper(getEditorPane());
    }

    public String getCurrentQuery() {
        return editorContentHelper.getCurrentQuery();
    }

    public void save() {
        try {
            support.saveDocument();
        }  catch (IOException ex) {
            ErrorManager.getDefault().notify(ex);
        }
    }


    public void showWarning(String txt) {
        Utilities.setStatusText(getEditorPane(), txt, ERROR_COLORING);
    }

    public Lookup getLookup() {
        Lookup l = super.getLookup();
        return new ProxyLookup(new Lookup[] { node.getLookup(), l });
    }
    
    
}
