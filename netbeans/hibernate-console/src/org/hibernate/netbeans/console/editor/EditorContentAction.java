package org.hibernate.netbeans.console.editor;

import javax.swing.ImageIcon;
import org.hibernate.netbeans.console.Icons;
import org.hibernate.netbeans.console.editor.hql.HqlEditorCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.actions.CookieAction;

/**
 * @author leon
 */
public class EditorContentAction extends CookieAction {
    
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
        setIcon(new ImageIcon(Utilities.loadImage(Icons.RUN)));
    }
    
    protected boolean asynchronous() {
        return false;
    }

    protected boolean enable(Node[] activatedNodes) {
        return super.enable(activatedNodes);
    }
    
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length != 1) {
            return;
        }
        Node n = activatedNodes[0];
        EditorContentCookie ecc = (EditorContentCookie) n.getCookie(HqlEditorCookie.class);
        if (ecc != null) {
            ecc.doRunWithContent(ecc.getActiveContent());
        }
    }

    protected Class[] cookieClasses() {
        return new Class[] { EditorContentCookie.class };
    }
    
    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    public String getName() {
        return NbBundle.getMessage(EditorContentAction.class, "CTL_Execute");
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }
    
}
