package org.hibernate.netbeans.console;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * @author leon
 */
public class CopySessionFactoryAction extends CookieAction {
    
    public CopySessionFactoryAction() {
    }

    public int mode() {
        return MODE_EXACTLY_ONE;
    }

    public Class[] cookieClasses() {
        return new Class[] { SessionFactoryCookie.class };
    }

    public void performAction(Node[] activatedNodes) {
        SessionFactoryCookie cookie = (SessionFactoryCookie) activatedNodes[0].getCookie(SessionFactoryCookie.class);
        cookie.copy();
    }

    public String getName() {
        return NbBundle.getMessage(DeleteSessionFactoryAction.class, "LBL_Copy");
    } 

    public HelpCtx getHelpCtx() {
        return null;
    }
    
}
