package org.hibernate.netbeans.console;

import org.hibernate.netbeans.console.output.SessionFactoryOutput;
import org.openide.actions.OpenAction;
import org.openide.nodes.Node;

/**
 * @author leon
 */
public class OpenReloadAction extends OpenAction {
    
    public OpenReloadAction() {
    }

    public String getName() {
        return "Open / Reload";
    }

    protected void performAction(Node[] activatedNodes) {
        for (Node n : activatedNodes) {
            SessionFactoryCookie c = (SessionFactoryCookie) n.getCookie(SessionFactoryCookie.class);
            if (c != null) {
                if (c.isSessionOpen()) {
                    c.reload();
                } else {
                    c.open();
                }
            }
        }
    }


}
