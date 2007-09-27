/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.model.filesystems.impl;

import org.jboss.tools.common.meta.action.impl.handlers.*;
import org.jboss.tools.common.model.*;

public class UnmountFolderHandler extends DefaultRedirectHandler {

    public UnmountFolderHandler() {}

    public boolean isEnabled(XModelObject object) {
        return (object != null && "true".equals(object.get("overlapped")));
    }

    protected XModelObject getTrueSource(XModelObject source) {
        return SelectOverlappedFileSystemHandler.getOverlappedFileSystem(source);
    }

}
