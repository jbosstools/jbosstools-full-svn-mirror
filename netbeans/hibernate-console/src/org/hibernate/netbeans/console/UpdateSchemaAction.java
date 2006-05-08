package org.hibernate.netbeans.console;

import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.actions.CookieAction;

/**
 * @author leon
 */
public class UpdateSchemaAction extends CookieAction {
    
    /** Creates a new instance of ReloadSessionFactoryAction */
    public UpdateSchemaAction() {
    }

    protected int mode() {
        return MODE_EXACTLY_ONE;
    }

    protected Class[] cookieClasses() {
        return new Class[] { SessionFactoryCookie.class };
    }

    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes.length == 0 || activatedNodes.length > 1) {
            return;
        }
        SessionFactoryCookie cookie = (SessionFactoryCookie) activatedNodes[0].getCookie(SessionFactoryCookie.class);
        if (cookie != null) {
            cookie.updateSchema();
        }
    }

    public String getName() {
        return "Update schema"; // TODO - fixme
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    protected boolean asynchronous() {
        return false;
    }
    
}
