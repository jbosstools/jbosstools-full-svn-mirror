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

import org.modeshape.common.util.CheckArg;
import org.modeshape.web.jcr.rest.client.domain.Server;

/**
 * The <code>ServerRegistryEvent</code> class is the event that is broadcast from the {@link ServerManager server manager} when a
 * server is added, removed, or changed.
 */
public final class ServerRegistryEvent {

    // ===========================================================================================================================
    // Constants
    // ===========================================================================================================================

    /**
     * The status severity levels.
     */
    private enum Type {
        /**
         * Indicates that a new server was added to the server registry.
         */
        NEW,

        /**
         * Indicates that a server was removed from the server registry.
         */
        REMOVE,

        /**
         * Indicates that properties of an existing server in the registry has been changed.
         */
        UPDATE
    }

    // ===========================================================================================================================
    // Class Methods
    // ===========================================================================================================================

    /**
     * @param serverManager the server manager sourcing this event (never <code>null</code>)
     * @param newServer the server that was added to the server registry (never <code>null</code>)
     * @return the event (never <code>null</code>)
     * @see Type#NEW
     */
    public static ServerRegistryEvent createNewEvent( ServerManager serverManager,
                                                      Server newServer ) {
        CheckArg.isNotNull(serverManager, "serverManager");
        CheckArg.isNotNull(newServer, "newServer");
        return new ServerRegistryEvent(serverManager, Type.NEW, newServer);
    }

    /**
     * @param serverManager the server manager sourcing this event (never <code>null</code>)
     * @param removedServer the server removed from the server registry (never <code>null</code>)
     * @return the event (never <code>null</code>)
     * @see Type#REMOVE
     */
    public static ServerRegistryEvent createRemoveEvent( ServerManager serverManager,
                                                         Server removedServer ) {
        CheckArg.isNotNull(serverManager, "serverManager");
        CheckArg.isNotNull(removedServer, "removedServer");
        return new ServerRegistryEvent(serverManager, Type.REMOVE, removedServer);
    }

    /**
     * @param serverManager the server manager sourcing this event (never <code>null</code>)
     * @param previousServerVersion the server being updated (never <code>null</code>)
     * @param newServerVersion the updated version of the server (never <code>null</code>)
     * @return the event (never <code>null</code>)
     * @see Type#UPDATE
     */
    public static ServerRegistryEvent createUpdateEvent( ServerManager serverManager,
                                                         Server previousServerVersion,
                                                         Server newServerVersion ) {
        CheckArg.isNotNull(serverManager, "serverManager");
        CheckArg.isNotNull(previousServerVersion, "previousServerVersion");
        CheckArg.isNotNull(newServerVersion, "newServerVersion");

        ServerRegistryEvent event = new ServerRegistryEvent(serverManager, Type.UPDATE, previousServerVersion);
        event.updatedServer = newServerVersion;
        return event;
    }

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The server being added, removed, or updated.
     */
    private final Server server;

    /**
     * The server manager in charge of the server registry the event is associated with.
     */
    private final ServerManager serverManager;

    /**
     * The event type.
     */
    private final Type type;

    /**
     * The server that is replacing an existing server. Will be <code>null</code> for all types except {@link Type#UPDATE update}.
     */
    private Server updatedServer;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * @param serverManager the server manager sourcing this event
     * @param type the event type
     * @param server the server being added, removed, or updated
     */
    private ServerRegistryEvent( ServerManager serverManager,
                                 Type type,
                                 Server server ) {
        assert (serverManager != null);
        assert (type != null);
        assert (server != null);
        
        this.serverManager = serverManager;
        this.type = type;
        this.server = server;
    }

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * @return the added, removed, or the old version of the server that has been updated
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * @return the server manager sourcing this event
     */
    public ServerManager getServerManager() {
        return this.serverManager;
    }

    /**
     * @return the new version of an existing server that has been updated
     * @throws UnsupportedOperationException if method is called when the type is not an update
     * @see Type#UPDATE
     */
    public Server getUpdatedServer() {
        if (this.type != Type.UPDATE) {
            throw new UnsupportedOperationException();
        }

        return this.updatedServer;
    }

    /**
     * @return <code>true</code> if the event is adding a new server to the registry
     * @see Type#NEW
     */
    public boolean isNew() {
        return (this.type == Type.NEW);
    }

    /**
     * @return <code>true</code> if the event is removing a server from the registry
     * @see Type#REMOVE
     */
    public boolean isRemove() {
        return (this.type == Type.REMOVE);
    }

    /**
     * @return <code>true</code> if the event is updating properties of an existing server in the registry
     * @see Type#UPDATE
     */
    public boolean isUpdate() {
        return (this.type == Type.UPDATE);
    }

}
