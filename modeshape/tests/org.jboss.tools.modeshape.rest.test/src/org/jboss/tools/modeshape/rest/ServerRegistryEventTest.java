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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertThat;
import org.jboss.tools.modeshape.rest.ServerManager;
import org.jboss.tools.modeshape.rest.ServerRegistryEvent;
import org.junit.Before;
import org.junit.Test;
import org.modeshape.web.jcr.rest.client.domain.Server;

public final class ServerRegistryEventTest {

    // ===========================================================================================================================
    // Constants
    // ===========================================================================================================================

    private static Server SERVER = new Server("url", "user", "pswd");

    private static Server UPDATED_SERVER = new Server("newurl", "newuser", "newpswd");

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    private ServerManager serverManager;

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    @Before
    public void beforeEach() {
        this.serverManager = new ServerManager(null, new MockRestClient());
    }

    // ===========================================================================================================================
    // Tests
    // ===========================================================================================================================

    @Test
    public void shouldHaveNewType() {
        assertThat(ServerRegistryEvent.createNewEvent(this.serverManager, SERVER).isNew(), is(true));
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullServerManagerWhenCreatingNewEvent() {
        ServerRegistryEvent.createNewEvent(null, SERVER);
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullServerWhenCreatingNewEvent() {
        ServerRegistryEvent.createNewEvent(this.serverManager, null);
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotBeAllowedToGetUpdatedServerOnNewEvent() {
        ServerRegistryEvent.createNewEvent(this.serverManager, SERVER).getUpdatedServer();
    }

    @Test
    public void shouldGetSameServerOnNewEvent() {
        assertThat(ServerRegistryEvent.createNewEvent(this.serverManager, SERVER).getServer(), sameInstance(SERVER));
    }

    @Test
    public void shouldHaveRemoveType() {
        assertThat(ServerRegistryEvent.createRemoveEvent(this.serverManager, SERVER).isRemove(), is(true));
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullServerManagerWhenCreatingRemoveEvent() {
        ServerRegistryEvent.createRemoveEvent(null, SERVER);
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullServerWhenCreatingRemoveEvent() {
        ServerRegistryEvent.createRemoveEvent(this.serverManager, null);
    }

    @Test( expected = UnsupportedOperationException.class )
    public void shouldNotBeAllowedToGetUpdatedServerOnRemoveEvent() {
        ServerRegistryEvent.createRemoveEvent(this.serverManager, SERVER).getUpdatedServer();
    }

    @Test
    public void shouldGetSameServerOnRemoveEvent() {
        assertThat(ServerRegistryEvent.createRemoveEvent(this.serverManager, SERVER).getServer(), sameInstance(SERVER));
    }

    @Test
    public void shouldHaveUpdateType() {
        assertThat(ServerRegistryEvent.createUpdateEvent(this.serverManager, SERVER, UPDATED_SERVER).isUpdate(), is(true));
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullServerManagerWhenCreatingUpdateEvent() {
        ServerRegistryEvent.createUpdateEvent(null, SERVER, UPDATED_SERVER);
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullServersWhenCreatingUpdateEvent() {
        ServerRegistryEvent.createUpdateEvent(this.serverManager, null, null);
    }

    @Test
    public void shouldGetSameServerOnUpdateEvent() {
        assertThat(ServerRegistryEvent.createUpdateEvent(this.serverManager, SERVER, UPDATED_SERVER).getServer(),
                   sameInstance(SERVER));
    }

    @Test
    public void shouldGetSameUpdatedServerOnUpdateEvent() {
        assertThat(ServerRegistryEvent.createUpdateEvent(this.serverManager, SERVER, UPDATED_SERVER).getUpdatedServer(),
                   sameInstance(UPDATED_SERVER));
    }

}
