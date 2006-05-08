package org.hibernate.netbeans.console.explorer;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class ShowSessionFactoriesExplorerAction extends AbstractAction implements Serializable {

    private final static long serialVersionUID = 1;

    public ShowSessionFactoriesExplorerAction() {
        super(NbBundle.getMessage(ShowSessionFactoriesExplorerAction.class, "CTL_SessionFactoriesAction"));
    }
    
    public void actionPerformed(ActionEvent evt) {
        TopComponent win = SessionFactoriesExplorer.findInstance();
        win.open();
        win.requestActive();
    }
    
}
