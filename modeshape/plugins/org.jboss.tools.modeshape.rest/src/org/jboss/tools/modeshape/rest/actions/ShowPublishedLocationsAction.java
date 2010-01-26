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

import static org.jboss.tools.modeshape.rest.IUiConstants.DELETE_SERVER_IMAGE;
import java.util.Set;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.modeshape.rest.Activator;
import org.jboss.tools.modeshape.rest.PublishedResourceHelper;
import org.jboss.tools.modeshape.rest.RestClientI18n;
import org.jboss.tools.modeshape.rest.ServerManager;
import org.jboss.tools.modeshape.rest.dialogs.PublishedLocationsDialog;
import org.modeshape.web.jcr.rest.client.Status;
import org.modeshape.web.jcr.rest.client.Status.Severity;
import org.modeshape.web.jcr.rest.client.domain.Workspace;

/**
 * The <code>PublishAction</code> controls the publishing of one or more {@link org.eclipse.core.resources.IResource}s to a
 * ModeShape repository.
 */
public final class ShowPublishedLocationsAction extends Action implements IObjectActionDelegate {

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The current workspace selection.
     */
    private IStructuredSelection selection;

    /**
     * The active part's Shell.
     */
    private Shell shell;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    public ShowPublishedLocationsAction() {
        super(RestClientI18n.deleteServerActionText.text(), Activator.getDefault().getImageDescriptor(DELETE_SERVER_IMAGE));
        setToolTipText(RestClientI18n.deleteServerActionToolTip.text());
        setEnabled(false);
    }

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    @Override
    public void run( IAction action ) {
        assert ((this.selection != null) && (this.selection.size() == 1));
        assert (this.selection.getFirstElement() instanceof IFile);

        // open dialog
        ServerManager serverManager = Activator.getDefault().getServerManager();
        PublishedResourceHelper resourceHelper = new PublishedResourceHelper(serverManager);

        try {
            Set<Workspace> workspaces = resourceHelper.getPublishedOnWorkspaces((IFile)this.selection.getFirstElement());
            new PublishedLocationsDialog(this.shell, serverManager, (IFile)this.selection.getFirstElement(), workspaces).open();
        } catch (Exception e) {
            Activator.getDefault().log(new Status(Severity.ERROR, RestClientI18n.showPublishedLocationsErrorMsg.text(), e));
            MessageDialog.openError(this.shell,
                                    RestClientI18n.errorDialogTitle.text(),
                                    RestClientI18n.showPublishedLocationsErrorMsg.text());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
     *      org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged( IAction action,
                                  ISelection selection ) {
        if (selection instanceof IStructuredSelection) {
            this.selection = (IStructuredSelection)selection;
        } else {
            this.selection = null;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
     */
    @Override
    public void setActivePart( IAction action,
                               IWorkbenchPart targetPart ) {
        this.shell = targetPart.getSite().getShell();
    }

}
