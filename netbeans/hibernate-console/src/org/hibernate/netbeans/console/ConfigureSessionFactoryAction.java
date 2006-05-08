package org.hibernate.netbeans.console;

import org.openide.ErrorManager;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

/**
 * @author leon
 */
public class ConfigureSessionFactoryAction extends CookieAction {
    
    public ConfigureSessionFactoryAction() {
    }
    
    protected int mode() {
        return MODE_EXACTLY_ONE;
    }
    
    public Class[] cookieClasses() {
        return new Class[] { SessionFactoryCookie.class };
    }
    
    public void performAction(Node[] activatedNodes) {
        if (activatedNodes.length != 1) {
            return;
        }
        Node n = activatedNodes[0];
        SessionFactoryCookie c = (SessionFactoryCookie) n.getCookie(SessionFactoryCookie.class);
        c.configure();
    }
    
    public String getName() {
        return NbBundle.getMessage(ConfigureSessionFactoryAction.class, "LBL_Configure");
    }
    
    public HelpCtx getHelpCtx() {
        return null;
    }

    public boolean asynchronous() {
        return true;
    }
    
}
