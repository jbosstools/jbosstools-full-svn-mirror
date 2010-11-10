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
package org.jboss.tools.modeshape.rest;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.jcr.nodetype.NodeType;
import org.modeshape.web.jcr.rest.client.IRestClient;
import org.modeshape.web.jcr.rest.client.Status;
import org.modeshape.web.jcr.rest.client.domain.QueryRow;
import org.modeshape.web.jcr.rest.client.domain.Repository;
import org.modeshape.web.jcr.rest.client.domain.Server;
import org.modeshape.web.jcr.rest.client.domain.Workspace;

/**
 * The <code>MockRestExecutor</code> class is a test <code>IRestClient</code> implementation that does nothing.
 */
public final class MockRestClient implements IRestClient {

    /**
     * {@inheritDoc}
     * 
     * @throws UnsupportedOperationException if this method is called
     * @see org.modeshape.web.jcr.rest.client.IRestClient#getNodeTypes(org.modeshape.web.jcr.rest.client.domain.Repository)
     */
    public Map<String, NodeType> getNodeTypes( Repository repository ) throws Exception {
        return null;
    }

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

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.web.jcr.rest.client.IRestClient#query(org.modeshape.web.jcr.rest.client.domain.Workspace,
     *      java.lang.String, java.lang.String)
     */
    public List<QueryRow> query( Workspace arg0,
                                 String arg1,
                                 String arg2 ) throws Exception {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.web.jcr.rest.client.IRestClient#query(org.modeshape.web.jcr.rest.client.domain.Workspace,
     *      java.lang.String, java.lang.String, int, int)
     */
    public List<QueryRow> query( Workspace arg0,
                                 String arg1,
                                 String arg2,
                                 int arg3,
                                 int arg4 ) throws Exception {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.modeshape.web.jcr.rest.client.IRestClient#query(org.modeshape.web.jcr.rest.client.domain.Workspace,
     *      java.lang.String, java.lang.String, int, int, java.util.Map)
     */
    public List<QueryRow> query( Workspace arg0,
                                 String arg1,
                                 String arg2,
                                 int arg3,
                                 int arg4,
                                 Map<String, String> arg5 ) throws Exception {
        return null;
    }

}
