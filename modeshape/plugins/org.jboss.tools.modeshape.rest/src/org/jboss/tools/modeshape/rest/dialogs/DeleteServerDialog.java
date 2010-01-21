/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.
 *
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 */
package org.jboss.tools.modeshape.rest.dialogs;

import static org.jboss.tools.modeshape.rest.IUiConstants.ModeShape_IMAGE_16x;
import java.util.Collection;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.modeshape.rest.Activator;
import org.jboss.tools.modeshape.rest.RestClientI18n;
import org.jboss.tools.modeshape.rest.Utils;
import org.modeshape.common.util.CheckArg;
import org.modeshape.web.jcr.rest.client.domain.IModeShapeObject;
import org.modeshape.web.jcr.rest.client.domain.Server;

/**
 * The <code>DeleteServerDialog</code> class provides a UI for deleting a {@link Server server}.
 */
public final class DeleteServerDialog extends MessageDialog {

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * Collection of servers which will be deleted.
     */
    private final Collection<Server> serversBeingDeleted;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * @param parentShell the dialog parent
     * @param serversBeingDeleted the servers being deleted (never <code>null</code>)
     */
    public DeleteServerDialog( Shell parentShell,
                               Collection<Server> serversBeingDeleted ) {
        super(parentShell, RestClientI18n.deleteServerDialogTitle.text(), Activator.getDefault().getImage(ModeShape_IMAGE_16x), null,
              MessageDialog.QUESTION, new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, 0);
        
        CheckArg.isNotNull(serversBeingDeleted, "serversBeingDeleted");
        this.serversBeingDeleted = serversBeingDeleted;

        // make sure dialog is resizable
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.MessageDialog#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell( Shell shell ) {
        super.configureShell(shell);

        // now set message
        String msg;

        if (this.serversBeingDeleted.size() == 1) {
            Server server = this.serversBeingDeleted.iterator().next();
            msg = RestClientI18n.deleteServerDialogOneServerMsg.text(server.getName(), server.getUser());
        } else {
            msg = RestClientI18n.deleteServerDialogMultipleServersMsg.text(this.serversBeingDeleted.size());
        }

        this.message = msg;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.MessageDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createCustomArea( Composite parent ) {
        if (this.serversBeingDeleted.size() != 1) {
            List serverList = new List(parent, SWT.NONE);
            serverList.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
            GridData gd = new GridData(SWT.LEFT, SWT.CENTER, true, true);
            gd.horizontalIndent = 40;
            serverList.setLayoutData(gd);

            for (IModeShapeObject server : this.serversBeingDeleted) {
                serverList.add(server.getName());
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#initializeBounds()
     */
    @Override
    protected void initializeBounds() {
        super.initializeBounds();
        Utils.centerAndSizeShellRelativeToDisplay(getShell(), 75, 75);
    }

}
