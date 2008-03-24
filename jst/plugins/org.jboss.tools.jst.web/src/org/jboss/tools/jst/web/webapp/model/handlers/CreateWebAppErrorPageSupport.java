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
package org.jboss.tools.jst.web.webapp.model.handlers;

import java.util.Properties;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.common.meta.action.XAttributeData;
import org.jboss.tools.common.meta.action.impl.*;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultCreateHandler;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultEditHandler;
import org.jboss.tools.common.meta.key.WizardKeys;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.util.XModelObjectLoaderUtil;
import org.jboss.tools.jst.web.messages.xpl.WebUIMessages;
import org.jboss.tools.jst.web.webapp.model.WebAppConstants;

public class CreateWebAppErrorPageSupport extends SpecialWizardSupport {

	public void reset() {
		if(action != null && action.getName().equals("Edit")) { //$NON-NLS-1$
			initStepData(0, getTarget());
		}
	}

    public String[] getActionNames(int stepId) {
        return new String[]{FINISH, CANCEL, HELP};
    }

	public void action(String name) throws XModelException {
		if(FINISH.equals(name)) {
			execute();
			setFinished(true);
		} else if(CANCEL.equals(name)) {
			setFinished(true);
		} else if(HELP.equals(name)) {
			help();
		}
	}
	
	void execute() throws XModelException {
		Properties p = extractStepData(0);
		if(action != null && action.getName().equals("Edit")) { //$NON-NLS-1$
			edit(p);
		} else {
			create(p);
		}
		getProperties().setProperty("done", "true"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	void create(Properties p) throws XModelException {
        XModelObject c = XModelObjectLoaderUtil.createValidObject(getTarget().getModel(), getEntityData()[0].getModelEntity().getName(), p);
        DefaultCreateHandler.addCreatedObject(getTarget(), c, getProperties());
	}
	
	void edit(Properties p) throws XModelException {
		DefaultEditHandler.edit(getTarget(), p);
	}

	public String getFocusAttribute(int stepId) {
		String a = getProperties().getProperty("focusAttribute"); //$NON-NLS-1$
		return (a != null) ? a : super.getFocusAttribute(stepId);
	}
    protected WebAppErrorPage validator = new WebAppErrorPage();
    
    public WizardDataValidator getValidator(int step) {
    	validator.setSupport(this, step);
		return validator;    	
    }
    
    class WebAppErrorPage extends DefaultWizardDataValidator {
    	
    	public void validate(Properties data) {
    		message = null;
    		String errorCode = data.getProperty(WebAppConstants.ERROR_CODE);
    		String exceptionType = data.getProperty(WebAppConstants.EXCEPTION_TYPE);
    		boolean isErrorCodeSet = (errorCode != null && errorCode.length() > 0);
    		boolean isExceptionTypeSet = (exceptionType != null && exceptionType.length() > 0);
    		if(!isErrorCodeSet && !isExceptionTypeSet) {
    			message = NLS.bind(WebUIMessages.EITHER_OR_MUST_BE_SET, getDisplayName(WebAppConstants.ERROR_CODE), getDisplayName(WebAppConstants.EXCEPTION_TYPE));  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    		} else if(isErrorCodeSet && isExceptionTypeSet) {
    			message = NLS.bind(WebUIMessages.YOU_MAY_SET_ONLY_ONE, getDisplayName(WebAppConstants.ERROR_CODE), getDisplayName(WebAppConstants.EXCEPTION_TYPE)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    		}
    		if(message != null) return;
    		super.validate(data);
    	}
    	
    	String getDisplayName(String attr) {
    		XAttributeData d = findAttribute(getId(), attr);
    		return (d == null) ? attr : WizardKeys.getAttributeDisplayName(d, true);
    	}

    }

}
