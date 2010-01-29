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

import net.jcip.annotations.Immutable;
import org.modeshape.web.jcr.rest.client.domain.Server;

/**
 * The <code>PersistedServer</code> class adds the concept of allowing a server's password to be persisted or not.
 */
@Immutable
public final class PersistedServer extends Server {

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * Indicates if the password should be stored locally when the server is persisted.
     */
    private final boolean persistPassword;

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * Constructs a new <code>PersistedServer</code>.
     * 
     * @param url the server URL (never <code>null</code>)
     * @param user the server user (never <code>null</code>)
     * @param password the server password (may be <code>null</code>)
     * @param persistPassword <code>true</code> if the password should be stored
     * @throws IllegalArgumentException if URL or user is <code>null</code> or empty
     */
    public PersistedServer( String url,
                      String user,
                      String password,
                      boolean persistPassword ) {
        super(url, user, password);
        this.persistPassword = persistPassword;
    }

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     *
     * @see org.modeshape.web.jcr.rest.client.domain.Server#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if (super.equals(obj)) {
            return (this.persistPassword == ((PersistedServer)obj).persistPassword);
        }
        
        return false;
    }

    /**
     * @return persistPassword <code>true</code> if the password is being persisted
     */
    public boolean isPasswordBeingPersisted() {
        return this.persistPassword;
    }

}
