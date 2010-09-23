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
package org.jboss.tools.common.model.undo;

import org.jboss.tools.common.model.*;

public class XChangeUndo extends XChangeSetUndo {

    public XChangeUndo(XModelObject object, String attr, String oldvalue) {
        super(object, new String[][]{{attr, oldvalue, ""}}); //$NON-NLS-1$
    }

    protected String createDescription(XModelObject o) {
        return "'" + attr[0][0] + "' of " + //$NON-NLS-1$ //$NON-NLS-2$
               o.getAttributeValue(XModelObjectConstants.ATTR_ELEMENT_TYPE) + " " + //$NON-NLS-1$
               o.getModelEntity().getRenderer().getTitle(o);
    }

}

