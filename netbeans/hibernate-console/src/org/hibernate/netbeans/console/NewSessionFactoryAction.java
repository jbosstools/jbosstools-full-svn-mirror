package org.hibernate.netbeans.console;

import java.awt.Dialog;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.ErrorManager;
import org.openide.NotifyDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 * @author leon
 */
public class NewSessionFactoryAction extends CallableSystemAction {
    
    public NewSessionFactoryAction() {
    }

    public String getName() {
        return NbBundle.getMessage(NewSessionFactoryAction.class, "LBL_NewSessionFactory");
    }

    public HelpCtx getHelpCtx() {
        return null;
    }

    public void performAction() {
        try {
            SessionFactoryConfigurationPanel cfgPanel = new SessionFactoryConfigurationPanel(null);
            DialogDescriptor dialDesc = new DialogDescriptor(cfgPanel, "New Session Factory", true, DialogDescriptor.OK_CANCEL_OPTION, null, null);
            Dialog dial = DialogDisplayer.getDefault().createDialog(dialDesc);
            dial.setVisible(true);
            if (DialogDescriptor.OK_OPTION.equals(dialDesc.getValue())) {
                SessionFactoryDescriptor descriptor = cfgPanel.updateOrCreateDescriptor();
                descriptor.persist();
            }
        } catch (Exception ex) {
            ErrorManager.getDefault().notify(ErrorManager.ERROR, ex);
        }
    }

    protected boolean asynchronous() {
        return false;
    }

}
