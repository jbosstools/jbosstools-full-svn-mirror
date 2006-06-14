/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
            DialogDescriptor dialDesc = new DialogDescriptor(
                    cfgPanel, 
                    "New Session Factory", 
                    true, 
                    DialogDescriptor.OK_CANCEL_OPTION, 
                    null,
                    DialogDescriptor.DEFAULT_ALIGN,
                    SessionFactoryConfigurationPanel.HELP_CTX,
                    null);
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
