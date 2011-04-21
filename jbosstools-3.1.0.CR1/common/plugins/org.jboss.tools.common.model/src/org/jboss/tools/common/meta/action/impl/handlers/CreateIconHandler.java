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

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.model.icons.impl.*;

public class CreateIconHandler extends DefaultCreateHandler {

    public CreateIconHandler() {}

    public void executeHandler(XModelObject object, Properties prop) throws XModelException {
        if(!isEnabled(object) || data == null) return;
        XEntityData[] es = (XEntityData[])data;
        String entity = es[0].getModelEntity().getName();
        Properties p = extractProperties(es[0]);
        String fn = p.getProperty("path"); //$NON-NLS-1$
        String im = new XStudioIcons().getImageString(fn);
        p.setProperty("image", im); //$NON-NLS-1$
        XModelObject icon = object.getModel().createModelObject(entity, p);
        addCreatedObject(object, icon, p);
    }



}
