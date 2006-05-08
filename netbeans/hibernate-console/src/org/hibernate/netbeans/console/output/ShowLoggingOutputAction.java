package org.hibernate.netbeans.console.output;

import java.awt.event.ActionEvent;
import java.io.Serializable;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class ShowLoggingOutputAction extends AbstractAction implements Serializable {

    private final static long serialVersionUID = 1;
    
    public ShowLoggingOutputAction() {
        super(NbBundle.getMessage(ShowLoggingOutputAction.class, "CTL_LoggingAction"));
    }
    
    public void actionPerformed(ActionEvent evt) {
        LoggingOutput.showLoggingWindow(true);
    }

}
