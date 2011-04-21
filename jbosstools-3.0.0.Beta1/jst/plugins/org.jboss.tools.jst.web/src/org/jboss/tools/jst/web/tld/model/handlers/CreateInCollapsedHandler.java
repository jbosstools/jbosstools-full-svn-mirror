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
package org.jboss.tools.jst.web.tld.model.handlers;

import java.util.*;
import org.jboss.tools.common.meta.action.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.meta.action.impl.handlers.*;

public class CreateInCollapsedHandler extends DefaultCreateHandler {

    public CreateInCollapsedHandler() {}

    public void executeHandler(XModelObject object, Properties prop) throws XModelException {
        if("false".equals(object.get("expanded")))
          XActionInvoker.invoke("SetExpanded", object, null);
        super.executeHandler(object, prop);
    }

} 