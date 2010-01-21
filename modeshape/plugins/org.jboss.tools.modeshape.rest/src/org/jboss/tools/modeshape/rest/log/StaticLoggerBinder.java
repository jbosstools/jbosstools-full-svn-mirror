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
package org.jboss.tools.modeshape.rest.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public final class StaticLoggerBinder implements LoggerFactoryBinder {

    // ===========================================================================================================================
    // Class Fields
    // ===========================================================================================================================

    /**
     * The class name of the logger factory.
     */
    private static final String LOGGER_FACTORY_CLASS_NAME = EclipseLoggerFactory.class.getName();

    /**
     * The unique instance of this class.
     */
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    // ===========================================================================================================================
    // Class Methods
    // ===========================================================================================================================

    /**
     * @return the static instance of the logger
     */
    public static final StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }

    // ===========================================================================================================================
    // Fields
    // ===========================================================================================================================

    /**
     * The logger factory used.
     */
    private final ILoggerFactory loggerFactory = new EclipseLoggerFactory();

    // ===========================================================================================================================
    // Methods
    // ===========================================================================================================================

    /**
     * {@inheritDoc}
     * 
     * @see org.slf4j.spi.LoggerFactoryBinder#getLoggerFactory()
     */
    @Override
    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.slf4j.spi.LoggerFactoryBinder#getLoggerFactoryClassStr()
     */
    @Override
    public String getLoggerFactoryClassStr() {
        return LOGGER_FACTORY_CLASS_NAME;
    }

}
