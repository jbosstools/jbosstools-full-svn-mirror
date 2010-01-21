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
package org.jboss.tools.modeshape.rest.actions;

import static org.jboss.tools.modeshape.rest.IUiConstants.ModeShape_IMAGE_16x;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.jboss.tools.modeshape.rest.Activator;
import org.jboss.tools.modeshape.rest.RestClientI18n;
import org.jboss.tools.modeshape.rest.Utils;
import org.jboss.tools.modeshape.rest.jobs.PublishJob.Type;
import org.jboss.tools.modeshape.rest.wizards.PublishWizard;
import org.modeshape.web.jcr.rest.client.Status;
import org.modeshape.web.jcr.rest.client.Status.Severity;

/**
 * The <code>BasePublishingAction</code> is a base class for all publishing actions.
 */
public abstract class BasePublishingAction implements IObjectActionDelegate {

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

    /**
     * Indicates if this is a publishing or unpublishing action.
     */
    private final Type type;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * @param type indicates the type of action
     */
    public BasePublishingAction( Type type ) {
        this.type = type;
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
    @SuppressWarnings( "unchecked" )
    public void run( IAction action ) {
        assert (this.selection != null);
        assert (!this.selection.isEmpty());

        List<IResource> resources;

        if (this.selection.size() == 1) {
            resources = Collections.singletonList((IResource)this.selection.getFirstElement());
        } else {
            resources = this.selection.toList();
        }

        // run wizard
        try {
            WizardDialog dialog = new WizardDialog(shell, new PublishWizard(this.type, resources,
                                                                            Activator.getDefault().getServerManager())) {
                /**
                 * {@inheritDoc}
                 * 
                 * @see org.eclipse.jface.dialogs.Dialog#initializeBounds()
                 */
                @Override
                protected void initializeBounds() {
                    super.initializeBounds();
                    getShell().setImage(Activator.getDefault().getImage(ModeShape_IMAGE_16x));
                    Utils.centerAndSizeShellRelativeToDisplay(getShell(), 75, 75);
                }
            };

            dialog.open();
        } catch (CoreException e) {
            String msg = null;

            if (this.type == Type.PUBLISH) {
                msg = RestClientI18n.basePublishingActionPublishingWizardErrorMsg.text();
            } else {
                msg = RestClientI18n.basePublishingActionUnpublishingWizardErrorMsg.text();
            }

            Activator.getDefault().log(new Status(Severity.ERROR, msg, e));
            MessageDialog.openError(this.shell, RestClientI18n.errorDialogTitle.text(), msg);
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
