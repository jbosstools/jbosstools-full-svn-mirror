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
package org.jboss.tools.common.meta.action.impl.handlers;

import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.meta.action.impl.AbstractHandler;

public class UndoRedoHandler extends AbstractHandler {

    public UndoRedoHandler() {}

    public XEntityData[] getEntityData(XModelObject object) {
        return new XEntityData[0];
    }

    public void executeHandler(XModelObject object, java.util.Properties p) throws XModelException {
        if(!isEnabled(object)) return;
        //obsolete
    }

    public boolean isEnabled(XModelObject object) {
        if(object == null) return false;
        return object.getModel().getRoot().isModified();
    }

}
