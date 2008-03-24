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
package org.jboss.tools.struts.model.handlers;

import java.util.Properties;

import org.eclipse.core.resources.IResource;

import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.struts.messages.StrutsUIMessages;
import org.jboss.tools.struts.model.helpers.StrutsGenerator;

public class GenFormBeanCodeSupport extends GenBaseSupport {

    public GenFormBeanCodeSupport() {
        steps = new Step[] {new StepFormBean(), new StepGen(), new StepFinish()};
    }

    protected void reset() {
        formbean_base = getAttributeValue(0, "base class");
        super.reset();
    }
    
    protected void generate() throws Exception {
        XModelObject fs = target.getModel().getByPath("FileSystems/"+output_fs); //$NON-NLS-1$
        StrutsGenerator gen = createStrutsGenerator(fs, StrutsGenerator.OVER_ASK);
        String s = gen.generateFormBean(target, formbean_base, formbean_imports, formbean_props);
        if (s != null) {
            writeLog("Generated form bean: ", s); //$NON-NLS-1$
            message = StrutsUIMessages.FORM_BEAN_GENERATED;
            XModelObjectLoaderUtil.getObjectLoader(fs).update(fs);
			EclipseResourceUtil.getResource(fs).refreshLocal(IResource.DEPTH_INFINITE, null);
        } else {
            message = StrutsUIMessages.FORM_BEAN_ISNOT_GENERATED;
        }
    }

    class StepFormBean implements Step {

        public String[] getActionNames() {
            return new String[] {GENERATE, CANCEL, HELP};
        }

        public String getMessage() {
            return null;
        }

        public String getTitle() {
            return "FormBean";
        }

        public int prepareStep(XModelObject object) {
            ////setAttributeValue(0, "imports", formbean_imports);
            setAttributeValue(0, "formbean class", "" + object.getAttributeValue(ATT_TYPE));
			prepareOutputFS();
			if(getTarget().getChildren(ENT_SETPROPERTY).length == 0)
			  setAttributeValue(0, "properties", "false");
            return 0;
        }

        public synchronized int doStep(XModelObject object) throws XModelException {
            Properties p2 = extractStepData(0);
            String oc = object.getAttributeValue(ATT_TYPE);
            String nc = p2.getProperty("formbean class");
            if(nc != null && nc.length() > 0 && !nc.equals(oc))
              object.getModel().changeObjectAttribute(object, ATT_TYPE, nc);
            ////output_fs = p2.getProperty("output path");
            formbean_base = p2.getProperty("base class");
            ////formbean_imports = p2.getProperty("imports");
            formbean_props = "true".equals(p2.getProperty("properties"));
            return 1;
        }

        public int undoStep(XModelObject object) {
            return 0;
        }

    }

}
