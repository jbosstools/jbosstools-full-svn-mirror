/*
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.
 *
 * This software is made available by Red Hat, Inc. under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 */
package org.jboss.tools.modeshape.rest.jobs;

import static org.jboss.tools.modeshape.rest.IUiConstants.PLUGIN_ID;
import static org.jboss.tools.modeshape.rest.IUiConstants.PUBLISHING_JOB_FAMILY;
import static org.jboss.tools.modeshape.rest.RestClientI18n.reconnectJobTaskName;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.jboss.tools.modeshape.rest.Activator;
import org.jboss.tools.modeshape.rest.RestClientI18n;
import org.jboss.tools.modeshape.rest.ServerManager;
import org.jboss.tools.modeshape.rest.Utils;
import org.modeshape.web.jcr.rest.client.Status;
import org.modeshape.web.jcr.rest.client.domain.Server;

/**
 * The <code>ReconnectJob</code> attempts to reconnect to the selected {@link Server server(s)}.
 */
public final class ReconnectJob extends Job {

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The server being reconnected to.
     */
    private final Server server;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * @param server the server being connected to (never <code>null</code>)
     */
    public ReconnectJob( Server server ) {
        super(reconnectJobTaskName.text(server.getShortDescription()));
        this.server = server;
    }

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.jobs.Job#belongsTo(java.lang.Object)
     */
    @Override
    public boolean belongsTo( Object family ) {
        return PUBLISHING_JOB_FAMILY.equals(family);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected IStatus run( IProgressMonitor monitor ) {
        IStatus result = null;
        ServerManager serverManager = Activator.getDefault().getServerManager();

        try {
            String taskName = reconnectJobTaskName.text(this.server.getShortDescription());
            monitor.beginTask(taskName, 1);
            monitor.setTaskName(taskName);
            Status status = serverManager.ping(this.server);
            result = Utils.convert(status);
        } catch (Exception e) {
            String msg = null;

            if (e instanceof InterruptedException) {
                msg = e.getLocalizedMessage();
            } else {
                msg = RestClientI18n.publishJobUnexpectedErrorMsg.text();
            }

            result = new org.eclipse.core.runtime.Status(IStatus.ERROR, PLUGIN_ID, msg, e);
        } finally {
            monitor.done();
            done(result);
        }

        return result;
    }

}
