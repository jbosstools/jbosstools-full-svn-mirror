/*
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
package org.jboss.tools.modeshape.rest.actions;

import static org.jboss.tools.modeshape.rest.IUiConstants.EDIT_SERVER_IMAGE;
import static org.jboss.tools.modeshape.rest.IUiConstants.ModeShape_IMAGE_16x;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.BaseSelectionListenerAction;
import org.jboss.tools.modeshape.rest.Activator;
import org.jboss.tools.modeshape.rest.PersistedServer;
import org.jboss.tools.modeshape.rest.RestClientI18n;
import org.jboss.tools.modeshape.rest.ServerManager;
import org.jboss.tools.modeshape.rest.wizards.ServerWizard;

/**
 * The <code>EditServerAction</code> runs a UI that allows {@link PersistedServer server} properties to be changed.
 */
public final class EditServerAction extends BaseSelectionListenerAction {

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The selected server being edited.
     */
    private PersistedServer serverBeingEdited;

    /**
     * The server manager used to create and edit servers.
     */
    private final ServerManager serverManager;

    /**
     * The shell used to display the dialog that edits and creates servers.
     */
    private final Shell shell;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * @param shell the parent shell used to display the dialog
     * @param serverManager the server manager to use when creating and editing servers
     */
    public EditServerAction( Shell shell,
                             ServerManager serverManager ) {
        super(RestClientI18n.editServerActionText.text());
        setToolTipText(RestClientI18n.editServerActionToolTip.text());
        setImageDescriptor(Activator.getDefault().getImageDescriptor(EDIT_SERVER_IMAGE));
        setEnabled(false);

        this.shell = shell;
        this.serverManager = serverManager;
    }

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        ServerWizard wizard = new ServerWizard(this.serverManager, this.serverBeingEdited);
        WizardDialog dialog = new WizardDialog(this.shell, wizard) {
            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.wizard.WizardDialog#configureShell(org.eclipse.swt.widgets.Shell)
             */
            @Override
            protected void configureShell( Shell newShell ) {
                super.configureShell(newShell);
                newShell.setImage(Activator.getDefault().getImage(ModeShape_IMAGE_16x));
            }
        };

        dialog.open();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.actions.BaseSelectionListenerAction#updateSelection(org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    protected boolean updateSelection( IStructuredSelection selection ) {
        // disable if empty selection or multiple objects selected
        if (selection.isEmpty() || (selection.size() > 1)) {
            this.serverBeingEdited = null;
            return false;
        }

        Object obj = selection.getFirstElement();

        // enable if server is selected
        if (obj instanceof PersistedServer) {
            this.serverBeingEdited = (PersistedServer)obj;
            return true;
        }

        // disable if non-server is selected
        this.serverBeingEdited = null;
        return false;
    }

}
