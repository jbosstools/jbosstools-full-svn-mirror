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
package org.jboss.tools.modeshape.rest.wizards;

import static org.jboss.tools.modeshape.rest.IUiConstants.WIZARD_BANNER_IMAGE;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.modeshape.rest.Activator;
import org.jboss.tools.modeshape.rest.RestClientI18n;
import org.jboss.tools.modeshape.rest.ServerManager;
import org.jboss.tools.modeshape.rest.jobs.PublishJob;
import org.jboss.tools.modeshape.rest.jobs.PublishJob.Type;
import org.modeshape.common.util.CheckArg;
import org.modeshape.web.jcr.rest.client.domain.Workspace;

/**
 * The <code>PublishWizard</code> is the wizard that published and unpublishes resources.
 */
public final class PublishWizard extends Wizard {

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The wizard page containing all the controls that allow publishing/unpublishing of resources.
     */
    private final PublishPage page;

    /**
     * The manager in charge of the server registry.
     */
    private final ServerManager serverManager;

    /**
     * Indicates if the wizard will perform a publishing or unpublishing operation.
     */
    private final Type type;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * @param type the publishing or unpublishing indicator (never <code>null</code>)
     * @param resources the resources being published or unpublished (never <code>null</code>)
     * @param serverManager the server manager in charge of the server registry (never <code>null</code>)
     * @throws CoreException if there is a problem processing the resources
     */
    public PublishWizard( Type type,
                          List<IResource> resources,
                          ServerManager serverManager ) throws CoreException {
        CheckArg.isNotNull(type, "type");
        CheckArg.isNotNull(resources, "resources");
        CheckArg.isNotNull(serverManager, "serverManager");

        this.type = type;
        this.page = new PublishPage(type, resources);
        this.serverManager = serverManager;

        setWindowTitle((type == Type.PUBLISH) ? RestClientI18n.publishWizardPublishTitle.text()
                                             : RestClientI18n.publishWizardUnpublishTitle.text());
        setDefaultPageImageDescriptor(Activator.getDefault().getImageDescriptor(WIZARD_BANNER_IMAGE));
    }

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    @Override
    public void addPages() {
        addPage(this.page);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.wizard.Wizard#getDialogSettings()
     */
    @Override
    public IDialogSettings getDialogSettings() {
        IDialogSettings settings = super.getDialogSettings();

        if (settings == null) {
            IDialogSettings temp = Activator.getDefault().getDialogSettings();
            settings = temp.getSection(getClass().getSimpleName());

            if (settings == null) {
                settings = temp.addNewSection(getClass().getSimpleName());
            }

            setDialogSettings(settings);
        }

        return super.getDialogSettings();
    }

    /**
     * @return the server manager (never <code>null</code>)
     */
    protected ServerManager getServerManager() {
        return this.serverManager;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    @Override
    public boolean performFinish() {
        Workspace workspace = this.page.getWorkspace();
        List<IFile> files = this.page.getFiles();
        PublishJob job = new PublishJob(this.type, files, workspace);
        job.schedule();

        return true;
    }

}
