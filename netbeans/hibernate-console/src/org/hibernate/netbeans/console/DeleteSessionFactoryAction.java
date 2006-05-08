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
public class DeleteSessionFactoryAction extends CookieAction {
    
    public DeleteSessionFactoryAction() {
    }

    public int mode() {
        return MODE_ALL;
    }

    public Class[] cookieClasses() {
        return new Class[] { SessionFactoryCookie.class };
    }

    public void performAction(Node[] activatedNodes) {
        String msg;
        if (activatedNodes.length > 1) {
            msg = "Are you sure you want to delete the selected session factories?";
        } else {
            msg = "Are you sure you want to delete the selected session factory?";
        }
        NotifyDescriptor nd = new NotifyDescriptor.Confirmation(msg, NotifyDescriptor.YES_NO_OPTION);
        if (NotifyDescriptor.OK_OPTION.equals(DialogDisplayer.getDefault().notify(nd))) {
            for (Node node : activatedNodes) {
                SessionFactoryCookie cookie = (SessionFactoryCookie) node.getCookie(SessionFactoryCookie.class);
                cookie.delete();
            }
        }
    }

    public String getName() {
        return NbBundle.getMessage(DeleteSessionFactoryAction.class, "LBL_Delete");
    } 

    public HelpCtx getHelpCtx() {
        return null;
    }
    
}
