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
package org.jboss.tools.struts.model.handlers.page;

import java.util.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.struts.*;

public class HideLinkHandler extends AbstractHandler implements StrutsConstants {

    public HideLinkHandler() {}

    public boolean isEnabled(XModelObject object) {
        if(object == null || !object.isObjectEditable()) return false;
        if(!ENT_PROCESSITEMOUT.equals(object.getModelEntity().getName())) return false;
///        if(!TYPE_LINK.equals(object.getAttributeValue(ATT_TYPE))) return false;
        String pn = action.getProperty("property");
        String pv = action.getProperty("value");
        return !pv.equals(object.getAttributeValue(pn));
    }

    public void executeHandler(XModelObject object, Properties p) throws XModelException {
        if(!isEnabled(object)) return;
        String pn = action.getProperty("property");
        String pv = action.getProperty("value");
        object.setAttributeValue(pn, pv);
    }

}
