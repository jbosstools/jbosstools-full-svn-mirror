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
import org.jboss.tools.common.meta.action.impl.AbstractHandler;
/**
 * TODO check if this class is still needed
 * @author glory
 *
 */
public class OpenObjectEditorHandler extends AbstractHandler {
    private static String wizardname = "XXX";
    protected static SpecialWizard wizard = null;

    public OpenObjectEditorHandler() {
        if(wizard == null) wizard = SpecialWizardFactory.createSpecialWizard(wizardname);
    }

    public boolean isEnabled(XModelObject object) {
        return (object != null && wizard != null);
    }

    public void executeHandler(XModelObject object, Properties p) throws XModelException {
        if(!isEnabled(object)) return;
        wizard.setObject(object);
        wizard.execute();
    }

}
