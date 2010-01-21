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
package org.jboss.tools.modeshape.rest;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import org.modeshape.web.jcr.rest.client.IRestClient;
import org.modeshape.web.jcr.rest.client.Status;
import org.modeshape.web.jcr.rest.client.domain.Repository;
import org.modeshape.web.jcr.rest.client.domain.Server;
import org.modeshape.web.jcr.rest.client.domain.Workspace;

/**
 * The <code>MockRestExecutor</code> class is a test <code>IRestClient</code> implementation that does nothing.
 */
public final class MockRestClient implements IRestClient {

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.web.jcr.rest.client.IRestClient#getRepositories(org.modeshape.web.jcr.rest.client.domain.Server)
     */
    public Collection<Repository> getRepositories( Server server ) throws Exception {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.web.jcr.rest.client.IRestClient#getUrl(java.io.File, java.lang.String,
     *      org.modeshape.web.jcr.rest.client.domain.Workspace)
     */
    public URL getUrl( File file,
                       String path,
                       Workspace workspace ) throws Exception {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.web.jcr.rest.client.IRestClient#getWorkspaces(org.modeshape.web.jcr.rest.client.domain.Repository)
     */
    public Collection<Workspace> getWorkspaces( Repository repository ) throws Exception {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.web.jcr.rest.client.IRestClient#publish(org.modeshape.web.jcr.rest.client.domain.Workspace,
     *      java.lang.String, java.io.File)
     */
    public Status publish( Workspace workspace,
                           String path,
                           File file ) {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.web.jcr.rest.client.IRestClient#unpublish(org.modeshape.web.jcr.rest.client.domain.Workspace,
     *      java.lang.String, java.io.File)
     */
    public Status unpublish( Workspace workspace,
                             String path,
                             File file ) {
        return null;
    }

}
