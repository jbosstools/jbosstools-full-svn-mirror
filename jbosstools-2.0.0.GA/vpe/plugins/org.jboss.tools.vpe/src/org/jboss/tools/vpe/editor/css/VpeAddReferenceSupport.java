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
package org.jboss.tools.vpe.editor.css;

import java.util.*;

import org.eclipse.core.resources.IFile;

import org.jboss.tools.common.meta.action.XActionInvoker;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.meta.constraint.XAttributeConstraintL;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.jst.web.project.WebProject;
import org.jboss.tools.jst.web.tld.TaglibMapping;

public class VpeAddReferenceSupport extends SpecialWizardSupport {

	public static boolean add(IFile file, ResourceReference css, ResourceReference[] list, String entity) {
		return run(file, css, list, "CreateActions.AddItem", entity);
	}
	
	public static boolean edit(IFile file, ResourceReference css, ResourceReference[] list, String entity) {
		return run(file, css, list, "EditActions.EditItem", entity);
	}
	
	private static boolean run(IFile file, ResourceReference css, ResourceReference[] list, String action, String entity) {
		XModel model = PreferenceModelUtilities.getPreferenceModel();
		XModelObject object = model.createModelObject(entity, null);
		object.setAttributeValue("location", css.getLocation());
		if(object.getAttributeValue("prefix") != null) {
			object.setAttributeValue("prefix", css.getProperties());
		}
		Properties p = new Properties();
		p.put("scope",Integer.valueOf(css.getScope()));
		p.put("list", list);
		if(file != null) p.put("file", file);
		XActionInvoker.invoke(action, object, p);
		boolean ok = "true".equals(p.getProperty("okPressed"));
		if(ok) {
			css.setLocation(object.getAttributeValue("location"));
			Integer scope = (Integer)p.get("scope");
			css.setScope(scope.intValue());
			String properties = object.getAttributeValue("prefix");
			if(properties != null) css.setProperties(properties);
		}
		return ok;
	}
	
	IFile file = null;
	String initialLocation;
	String initialPrefix;
	ResourceReference[] list;	
	String[] scopeNames;
	
	protected void reset() {
		initialLocation = getTarget().getAttributeValue("location");
		setAttributeValue(0, "location", initialLocation);
		initialPrefix = getTarget().getAttributeValue("prefix");
		if(initialPrefix != null) {
			setAttributeValue(0, "prefix", initialPrefix);
		}
		scopeNames = ((XAttributeConstraintL)getTarget().getModelEntity().getAttribute("scope").getConstraint()).getValues();
		int scopeIndex = ((Integer)getProperties().get("scope")).intValue();
		String scope = scopeNames[scopeIndex];
		setAttributeValue(0, "scope", scope);
		list = (ResourceReference[])getProperties().get("list");
		file = (IFile)getProperties().get("file");
		setURIList();
	}
	
	void setURIList() {
		if(file == null) return;
		if(getEntityData()[0].getModelEntity().getName().startsWith("VPETLD")) {
			Set set = new TreeSet();
			IModelNature n = EclipseResourceUtil.getModelNature(file.getProject());
			if(n == null) return;
			XModel model = n.getModel();
			TaglibMapping taglibs = WebProject.getInstance(model).getTaglibMapping();
			Map map = taglibs.getTaglibObjects();
			Iterator it = map.keySet().iterator();
			while(it.hasNext()) {
				String s = it.next().toString();
				set.add(taglibs.resolveURI(s));
			}
			String[] uris = (String[])set.toArray(new String[0]);
 			setValueList(0, "location", uris);
		}
	}

	public void action(String name) throws Exception {
		if(OK.equals(name)) {
			execute();
			setFinished(true);
			getProperties().setProperty("okPressed", "true");
		} else if(CANCEL.equals(name)) {
			setFinished(true);
		}
	}
	
	protected void execute() throws Exception {
		Properties p0 = extractStepData(0);
		getTarget().setAttributeValue("location", p0.getProperty("location"));
		if(p0.containsKey("prefix")) {
			getTarget().setAttributeValue("prefix", p0.getProperty("prefix"));
		}
		int scope = getSelectedScope(p0); 
		getProperties().put("scope", Integer.valueOf(scope));
	}
	
	int getSelectedScope(Properties p0) {
		String scopeName = p0.getProperty("scope");
		for (int i = 0; i < scopeNames.length; i++) {
			if(scopeNames[i].equals(scopeName)) return i;
		}
		return 0;
	}

}
