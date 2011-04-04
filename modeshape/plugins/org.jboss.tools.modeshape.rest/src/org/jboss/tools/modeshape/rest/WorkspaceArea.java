package org.jboss.tools.modeshape.rest;

import org.modeshape.common.util.CheckArg;
import org.modeshape.web.jcr.rest.client.domain.Workspace;

/**
 * The <code>WorkspaceArea</code> represents a known area in a ModeShape repository where sequencing of resources occurs.
 */
public final class WorkspaceArea {

    /**
     * The path within the workspace where this area is found (never <code>null</code>).
     */
    private final String path;

    /**
     * An optional workspace area title (can be <code>null</code> or empty).
     */
    private final String title;

    /**
     * The workspace where this area is found (never <code>null</code>).
     */
    private final Workspace workspace;

    /**
     * @param workspace the workspace where this area is found (never <code>null</code>)
     * @param path the workspace path where this area is found (never <code>null</code>)
     * @param title the workspace area title (can be <code>null</code> or empty)
     */
    public WorkspaceArea( Workspace workspace,
                          String path,
                          String title ) {
        CheckArg.isNotNull(workspace, "workspace"); //$NON-NLS-1$
        CheckArg.isNotEmpty(path, "path"); //$NON-NLS-1$

        this.workspace = workspace;
        this.path = path;
        this.title = title;
    }

    /**
     * @return path the workspace path where this area is found (never <code>null</code>)
     */
    public String getPath() {
        return this.path;
    }

    /**
     * @return title the workspace area title (can be <code>null</code> or empty)
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return workspace the workspace where this area is found (never <code>null</code>)
     */
    public Workspace getWorkspace() {
        return this.workspace;
    }

}
