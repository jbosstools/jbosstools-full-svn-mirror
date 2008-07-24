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
package org.jboss.tools.jst.web.ui.action.adf;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.jboss.tools.common.meta.action.impl.AbstractHandler;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;

public class AddADFSupportHandler extends AbstractHandler {
	AddADFSupportHelper helper = new AddADFSupportHelper();

    public boolean isEnabled(XModelObject object) {
    	helper.setObject(object);
        return helper.isEnabled();
    }
	
    public void executeHandler(XModelObject object, Properties p) throws XModelException {
    	if(!isEnabled(object)) return;
    	try {
    		helper.execute();
    	} catch (InvocationTargetException e1) {
    		throw new XModelException(e1);
    	} catch (InterruptedException e2) {
    		throw new XModelException(e2);
    	}
    }


}
