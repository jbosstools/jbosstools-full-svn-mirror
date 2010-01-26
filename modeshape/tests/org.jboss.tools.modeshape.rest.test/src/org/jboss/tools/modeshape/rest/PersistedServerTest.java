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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.jboss.tools.modeshape.rest.PersistedServer;
import org.junit.Test;

public final class PersistedServerTest {

    private static final PersistedServer PERSISTED = new PersistedServer("url", "user", "pswd", true);

    private static final PersistedServer NOT_PERSISTED = new PersistedServer(PERSISTED.getUrl(), PERSISTED.getUser(),
                                                                             PERSISTED.getPassword(), false);

    @Test
    public void shouldNotBeEqualIfDifferentPasswordSettings() {
        assertThat(PERSISTED.equals(NOT_PERSISTED), is(false));
    }

    @Test
    public void shouldHaveSameHashCodeIfDifferentPasswordSettings() {
        assertThat(PERSISTED.hashCode(), is(NOT_PERSISTED.hashCode()));
    }

    @Test
    public void shouldHavePasswordBeingPersisted() {
        assertThat(PERSISTED.isPasswordBeingPersisted(), is(true));
    }

    @Test
    public void shouldNotHavePasswordBeingPersisted() {
        assertThat(NOT_PERSISTED.isPasswordBeingPersisted(), is(false));
    }

}
