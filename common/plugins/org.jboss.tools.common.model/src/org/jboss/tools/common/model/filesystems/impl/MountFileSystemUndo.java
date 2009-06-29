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

import java.text.MessageFormat;
import org.jboss.tools.common.model.*;
public class MountFileSystemUndo extends UnmountFileSystemUndo {

    public MountFileSystemUndo(XModelObject fs) {
        super(fs);
        description = MessageFormat.format("Mount file system {0}", p.getProperty(XModelObjectConstants.ATTR_NAME_LOCATION));
    }

    protected void doUndo() {
        super.doRedo();
    }

    protected void doRedo() {
        super.doUndo();
    }

    protected String getActionIcon() {
        return "images/actions/new.gif"; //$NON-NLS-1$
    }

}