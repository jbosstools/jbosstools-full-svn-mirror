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
package org.jboss.tools.shale.model.handlers;

import java.util.*;
import org.jboss.tools.common.meta.action.impl.DefaultWizardDataValidator;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.files.handlers.CreateFileSupport;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.undo.XTransactionUndo;
import org.jboss.tools.common.model.undo.XUndoManager;
import org.jboss.tools.jsf.web.ConfigFilesData;
import org.jboss.tools.jsf.web.JSFWebHelper;
import org.jboss.tools.jst.web.model.helpers.WebAppHelper;

public abstract class AbstractCreateConfigFileSupport extends CreateFileSupport {
	static String REGISTER = "register in web.xml";
	
	public void reset() {
		super.reset();
		initDefaultName();
		initRegister();
	}
	
	protected abstract ConfigFilesData getConfigFilesData();
	
	protected String getDefaultNameBase() {
		ConfigFilesData d = getConfigFilesData();
		if(d.defaultList == null || d.defaultList.length == 0) return null;
		return d.defaultList[0].substring(d.defaultList[0].lastIndexOf('/') + 1);
	}
	
	void initDefaultName() {
		String nameBase = getDefaultNameBase();
		if(nameBase == null) return;
		String entity = action.getProperty("entity");
		XModelObject o = getTarget().getModel().createModelObject(entity, null);
		o.setAttributeValue("name", nameBase);
		XModelObject t = getTargetFolder();
		if(t == null || t.getChildByPath(o.getPathPart()) == null) {
			setAttributeValue(0, "name", nameBase);
			return;
		}
		String name = nameBase;
		int i = 0;
		while(true) {
			name = nameBase + "-" + (++i);
			o.setAttributeValue("name", name);
			if(t.getChildByPath(o.getPathPart()) == null) break;
		}
		setAttributeValue(0, "name", name);
	}
	
	void initRegister() {
		if(checkRegister(getTarget(), getAttributeValue(0, REGISTER)) != null) {
			setAttributeValue(0, REGISTER, "no");
		}
	}

	protected void execute() throws Exception {
		Properties p0 = extractStepData(0);
		XUndoManager undo = getTarget().getModel().getUndoManager();
		XTransactionUndo u = new XTransactionUndo("Create config file in " + getTarget().getAttributeValue("element type")+" "+getTarget().getPresentationString(), XTransactionUndo.ADD);
		undo.addUndoable(u);
		try {
			doExecute(p0);
		} catch (RuntimeException e) {
			undo.rollbackTransactionInProgress();
			throw e;
		} finally {
			u.commit();
		}
	}
	
	private void doExecute(Properties p0) throws Exception {
		Properties p = extractStepData(0);
		String path = p.getProperty("name");
		path = revalidatePath(path);
		XModelObject file = createFile(path);
		if(file == null) return;		
		register(file.getParent(), file, p0);
		final XModelObject q = file;
		open(q);	
	}

	private void register(XModelObject object, XModelObject created, Properties prop) throws Exception {
		boolean register = "yes".equals(getAttributeValue(0, "register in web.xml"));
		if(!register) return;
		String uri = getURI(created);
		JSFWebHelper.registerConfigFile(created.getModel(), uri, getConfigFilesData());
	}
	
	private String getURI(XModelObject file) {
		String result = "/" + FileAnyImpl.toFileName(file);
		XModelObject o = file.getParent();
		while(o != null && o.getFileType() != XModelObject.SYSTEM) {
			result = "/" + o.getAttributeValue("name") + result;
			o = o.getParent();
		}
		if(o == null || !"WEB-ROOT".equals(o.getAttributeValue("name"))) {
			result = "/WEB-INF" + result;
		}
		return result;
	}

	protected DefaultWizardDataValidator createValidator() {
		return new CreateConfigFileValidator(); 
	}
	
	class CreateConfigFileValidator extends CreateFileSupport.Validator {
		public void validate(Properties data) {
			super.validate(data);
			if(message != null) return;
			message = checkRegister(getTarget(), data.getProperty("register in web.xml"));
		}
	}

	private String checkRegister(XModelObject object, String register) {
		if(!"yes".equals(register)) return null;
		XModelObject webxml = WebAppHelper.getWebApp(object.getModel());
		if(webxml == null) return "Config cannot be registered because web.xml is not found.";
		if("yes".equals(webxml.get("isIncorrect"))) return "Config file cannot be registered because web.xml is incorrect.";
		if(!webxml.isObjectEditable()) return "Config file cannot be registered because web.xml is read only.";
		return null;
	}

}