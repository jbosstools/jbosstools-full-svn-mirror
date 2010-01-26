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
package org.jboss.tools.modeshape.rest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.modeshape.common.util.CheckArg;
import org.modeshape.web.jcr.rest.client.Status;
import org.modeshape.web.jcr.rest.client.Status.Severity;
import org.modeshape.web.jcr.rest.client.domain.Repository;
import org.modeshape.web.jcr.rest.client.domain.Server;
import org.modeshape.web.jcr.rest.client.domain.Workspace;

/**
 * The <code>PublishedResourceHelper</code> knows how to get and set the property on a resource that indicates it has been
 * published to one or more workspaces.
 */
public final class PublishedResourceHelper {

    // ===========================================================================================================================
    // Constants
    // ===========================================================================================================================

    /**
     * Delimiter between a workspace's properties.
     */
    private static final String ID_DELIM = "$";

    /**
     * Delimiter between workspaces.
     */
    private static final String DELIM = "|";

    /**
     * The name of the persisted file property indicating if the resource has been published. This property will only exist if the
     * file has been published to at least one repository. The value of the property is a list of repository workspaces where this
     * file has been published.
     */
    private static QualifiedName PUBLISHED_RESOURCE_PROPERTY = new QualifiedName(IUiConstants.PLUGIN_ID, "publishedLocations");

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The server manager used by the helper to obtain workspaces.
     */
    private final ServerManager serverManager;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * @param serverManager the server manager used by this helper (never <code>null</code>)
     */
    public PublishedResourceHelper( ServerManager serverManager ) {
        CheckArg.isNotNull(serverManager, "serverManager");
        this.serverManager = serverManager;
    }

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * @param file the file that was just published (never <code>null</code>)
     * @param workspace the workspace where the file was published (never <code>null</code>)
     * @throws Exception if there is a problem setting the property
     */
    public void addPublishedProperty( IFile file,
                                      Workspace workspace ) throws Exception {
        CheckArg.isNotNull(file, "file");
        CheckArg.isNotNull(workspace, "workspace");

        Set<Workspace> workspaces = getPublishedOnWorkspaces(file);
        workspaces.add(workspace);

        // set new value
        setPublishedOnPropertyValue(file, workspaces);
    }

    /**
     * @param workspaces the workspaces used to create the property value
     * @return the property value
     */
    private String createPublishedPropertyValue( Set<Workspace> workspaces ) {
        StringBuilder value = new StringBuilder();

        for (Workspace workspace : workspaces) {
            value.append(createWorkspaceId(workspace)).append(DELIM);
        }

        return value.toString();
    }

    /**
     * @param workspace the workspace whose identifier is being created
     * @return the ID
     */
    private String createWorkspaceId( Workspace workspace ) {
        StringBuilder result = new StringBuilder();
        result.append(workspace.getServer().getUrl()).append(ID_DELIM).append(workspace.getServer().getUser()).append(ID_DELIM);
        result.append(workspace.getRepository().getName()).append(ID_DELIM);
        result.append(workspace.getName());

        return result.toString();
    }

    /**
     * @param file the file whose <code>Workspace</code>s it has been published on is being requested (never <code>null</code>)
     * @return the workspaces (never <code>null</code>)
     * @throws Exception if there is a problem reading one of the file's persistent properties or a problem with the server
     *         manager
     */
    public Set<Workspace> getPublishedOnWorkspaces( IFile file ) throws Exception {
        CheckArg.isNotNull(file, "file");

        Set<Workspace> publishedOnWorkspaces = null;
        String value = file.getPersistentProperty(PUBLISHED_RESOURCE_PROPERTY);

        if (value == null) {
            publishedOnWorkspaces = new HashSet<Workspace>(1);
        } else {
            StringTokenizer wsTokenizer = new StringTokenizer(value, DELIM);
            publishedOnWorkspaces = new HashSet<Workspace>(wsTokenizer.countTokens());

            while (wsTokenizer.hasMoreTokens()) {
                StringTokenizer propsTokenizer = new StringTokenizer(wsTokenizer.nextToken(), ID_DELIM);

                PARSE_WORKSPACE: while (propsTokenizer.hasMoreTokens()) {
                    String url = propsTokenizer.nextToken();
                    String user = propsTokenizer.nextToken();
                    Server server = this.serverManager.findServer(url, user);

                    if ((server != null) && this.serverManager.ping(server).isOk()) {
                        Collection<Repository> repositories = this.serverManager.getRepositories(server);

                        if (!repositories.isEmpty()) {
                            String repositoryName = propsTokenizer.nextToken();

                            for (Repository repository : repositories) {
                                if (repository.getName().equals(repositoryName)) {
                                    Collection<Workspace> workspaces = this.serverManager.getWorkspaces(repository);

                                    if (!workspaces.isEmpty()) {
                                        String workspaceName = propsTokenizer.nextToken();

                                        for (Workspace workspace : workspaces) {
                                            if (workspace.getName().equals(workspaceName)) {
                                                publishedOnWorkspaces.add(workspace);
                                                break PARSE_WORKSPACE;
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    } else {
                        // this will remove workspace as being one that the file has been published on
                        break PARSE_WORKSPACE;
                    }
                }
            }
        }

        return publishedOnWorkspaces;
    }

    /**
     * @param file the file whose published status is being requested (never <code>null</code>)
     * @return <code>true</code> if the file has been published to a repository
     */
    public boolean isPublished( IFile file ) {
        CheckArg.isNotNull(file, "file");

        try {
            return !getPublishedOnWorkspaces(file).isEmpty();
        } catch (Exception e) {
            Activator.getDefault().log(new Status(Severity.ERROR, RestClientI18n.publishedResourcePropertyErrorMsg.text(file), e));
        }

        return false;
    }

    /**
     * @param file the file that was just unpublished (never <code>null</code>)
     * @param workspace the workspace where the file was unpublished (never <code>null</code>)
     * @throws Exception if there is a problem changing the property value
     */
    public void removePublishedProperty( IFile file,
                                         Workspace workspace ) throws Exception {
        CheckArg.isNotNull(file, "file");
        CheckArg.isNotNull(workspace, "workspace");

        Set<Workspace> workspaces = getPublishedOnWorkspaces(file);
        workspaces.remove(workspace);

        // set new value
        setPublishedOnPropertyValue(file, workspaces);
    }

    /**
     * @param file the file whose property is being cleared
     * @throws CoreException if there was a problem removing the property
     */
    public void clear( IFile file ) throws CoreException {
        file.setPersistentProperty(PUBLISHED_RESOURCE_PROPERTY, null);
    }

    /**
     * @param file the file whose property is being set
     * @param workspaces the workspaces the file has been published to or <code>null</code> if the file has not been published
     * @throws CoreException if there was a problem changing the property value
     */
    private void setPublishedOnPropertyValue( IFile file,
                                              Set<Workspace> workspaces ) throws CoreException {
        if ((workspaces == null) || workspaces.isEmpty()) {
            clear(file);
        } else {
            String value = createPublishedPropertyValue(workspaces);
            file.setPersistentProperty(PUBLISHED_RESOURCE_PROPERTY, value);
        }
    }

}
