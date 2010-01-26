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
package org.jboss.tools.modeshape.rest.actions;

import org.jboss.tools.modeshape.rest.jobs.PublishJob.Type;

/**
 * The <code>PublishAction</code> controls the publishing of one or more {@link org.eclipse.core.resources.IResource}s to a 
 * repository.
 */
public final class PublishAction extends BasePublishingAction {

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    public PublishAction() {
        super(Type.PUBLISH);
    }

}
