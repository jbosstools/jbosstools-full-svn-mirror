package org.hibernate.netbeans.console.output;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * @author leon
 */
public class ShowSessionFactoryOutputAction extends AbstractAction implements Serializable {

    private final static long serialVersionUID = 1;

    public ShowSessionFactoryOutputAction() {
        super(NbBundle.getMessage(ShowSessionFactoryOutputAction.class, "CTL_SessionFactoryOutputAction"));
    }
    
    public void actionPerformed(ActionEvent evt) {
//        TopComponent win = SessionFactoryOutput.findInstance();
//        win.open();
//        win.requestActive();
    }
    
}
