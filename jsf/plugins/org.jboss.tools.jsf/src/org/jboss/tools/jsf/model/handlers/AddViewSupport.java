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
package org.jboss.tools.jsf.model.handlers;

import java.io.File;
import java.util.Properties;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.common.meta.action.impl.DefaultWizardDataValidator;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;
import org.jboss.tools.common.meta.action.impl.WizardDataValidator;
import org.jboss.tools.common.meta.action.impl.handlers.DefaultCreateHandler;
import org.jboss.tools.common.model.ServiceDialog;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FileSystemImpl;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.model.util.FindObjectHelper;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jsf.JSFModelPlugin;
import org.jboss.tools.jsf.JSFPreference;
import org.jboss.tools.jsf.model.JSFConstants;
import org.jboss.tools.jsf.model.JSFNavigationModel;
import org.jboss.tools.jsf.model.ReferenceGroupImpl;
import org.jboss.tools.jsf.model.helpers.JSFProcessHelper;
import org.jboss.tools.jsf.model.impl.NavigationRuleObjectImpl;
import org.jboss.tools.jsf.web.JSFTemplate;
import org.jboss.tools.jsf.messages.JSFUIMessages;

public class AddViewSupport extends SpecialWizardSupport implements JSFConstants {
	public static String JSF_ADD_VIEW_PATH = JSFPreference.JSF_ADD_VIEW_PATH;
	JSFTemplate templates = new JSFTemplate();
	static String LAST_CREATE_FILE_PREFERENCE = "org.jboss.tools.jsf.lastCreateFileValue";
	XModelObject sample;
	
	public void reset() {
		sample = (XModelObject)getProperties().get("sample");
		if(sample != null) {
			setAttributeValue(0, ATT_FROM_VIEW_ID, sample.getAttributeValue(ATT_PATH));
		}
		templates.updatePageTemplates();
		String[] s = templates.getPageTemplateList();
		setValueList(0, "template", s);
		//take from preferences
		setAttributeValue(0, "template", getDefaultTemplate(s));
		String last = JSFModelPlugin.getDefault().getPluginPreferences().getString(LAST_CREATE_FILE_PREFERENCE);
		if(last == null || last.length() == 0) {
			last = "true";
		} else if(!"true".equals(last)) {
			last = "false"; 
		}
		setAttributeValue(0, "create file", last);
	}
	
	static XModelObject getPreferenceObject() {
		return PreferenceModelUtilities.getPreferenceModel().getByPath(JSF_ADD_VIEW_PATH);
	}
	
	public String getDefaultTemplate(String[] list) {
		if(list.length == 0) return "";
		XModelObject addView = getPreferenceObject();
		String v = (addView == null) ? "" : addView.getAttributeValue("Page Template");
		if(v != null) for (int i = 0; i < list.length; i++) if(v.equals(list[i])) return list[i];
		return list[0];
	}
	
	public String getExtension(String template) {
		if(template != null) {
			int i = template.trim().lastIndexOf('.');
			if(i > 0) {
				return template.trim().substring(i);
			}
		}
		return getExtension();
	}

	public static String getExtension() {
		XModelObject addView = getPreferenceObject();
		String v = (addView == null) ? "" : addView.getAttributeValue("Extension");
		if(v == null || v.length() == 0) return ".jsp";
		if(!v.startsWith(".")) v = "." + v;
		return v;
	}

	public void action(String name) throws XModelException {
		if(FINISH.equals(name)) {
			execute();
			setFinished(true);
		} else if(CANCEL.equals(name)) {
			setFinished(true);
		}
	}

	public String[] getActionNames(int stepId) {
		return new String[]{FINISH, CANCEL, HELP};
	}

	protected void execute() throws XModelException {
		boolean doNotCreateEmptyRule = "yes".equals(JSFPreference.DO_NOT_CREATE_EMPTY_RULE.getValue());
		Properties p = extractStepData(0);
		String path = p.getProperty("from-view-id");
		path = revalidatePath(path, getAttributeValue(0, "template"));
		
		createFile(path);

		JSFNavigationModel m = (JSFNavigationModel)getTarget().getParent();
		String pp = NavigationRuleObjectImpl.toNavigationRulePathPart(path);
		boolean isPattern = JSFProcessHelper.isPattern(path);
		boolean existsR = m.getRuleCount(path) != 0;
		boolean existsV = findView(path) != null;
		
		boolean exists = existsR || existsV;
		if(exists) {
			ServiceDialog d = getTarget().getModel().getService();
			String message = NLS.bind(JSFUIMessages.THE_VIEW_WITH_PATH_IS_ALREADY_CREATED, path);
			if(existsR) {
				message += JSFUIMessages.YOU_WANT_TO_ADD_ADDITIONAL_NAVIGATION_RULE_WITH_SAME_FROM_VIEW_ID;
			} else if(existsV && isPattern) {
				message += JSFUIMessages.YOU_WANT_TO_CREATE_AN_ADDITIONAL_VIEW_WITH_THE_SAME_FROM_VIEW_ID;
			} else {
				message += JSFUIMessages.YOU_WANT_TO_CREATE_A_NAVIGATION_RULE_FOR_THIS_FROM_VIEW_ID;
			}
			int q = d.showDialog(JSFUIMessages.WARNING, message, new String[]{JSFUIMessages.OK, JSFUIMessages.CANCEL}, null, ServiceDialog.WARNING);
			if(q != 0) return;
		}
		/*TRIAL_JSF*/
		XModelObject created = null;
		if(!doNotCreateEmptyRule || (exists && !isPattern) || sample != null) {
			XModelObject rule = m.addRule(path);
			addCasesFromSample(rule);
			m.setModified(true);
			created = getTarget().getChildByPath(pp);
		} else {
			created = getTarget().getModel().createModelObject(JSFConstants.ENT_PROCESS_GROUP, null);
			String ppi = pp;
			if(exists && JSFProcessHelper.isPattern(path)) {
				int index = -1;
				while(getTarget().getChildByPath(ppi + ":" + index) != null) index--;
				ppi = ppi + ":" + index;
			}
			created.setAttributeValue(JSFConstants.ATT_NAME, ppi);
			created.setAttributeValue(JSFConstants.ATT_PATH, path);
			created.setAttributeValue("persistent", "true");
			DefaultCreateHandler.addCreatedObject(getTarget(), created, getProperties());
		}
		
		if(!exists || isPattern) {
			String shape = getShape();
			if(created != null && shape != null) created.setAttributeValue("shape", shape);
		}
		if(created != null) {
			FindObjectHelper.findModelObject(created, FindObjectHelper.IN_EDITOR_ONLY);
		}		
	}
	
	private XModelObject findView(String path) {
		String pp = NavigationRuleObjectImpl.toNavigationRulePathPart(path);
		XModelObject o = getTarget().getChildByPath(pp);
		if(o != null) return o;
		if(!JSFProcessHelper.isPattern(path)) return null;
		XModelObject[] cs = getTarget().getChildren();
		for (int i = 0; i < cs.length; i++) {
			String p = cs[i].getPathPart();
			if(pp.equals(p) || p.startsWith(pp + ":")) return cs[i];			
		}
		return null;
	}
	
	private void addCasesFromSample(XModelObject rule) {
		if(!(sample instanceof ReferenceGroupImpl)) return;
		XModelObject[] rs = ((ReferenceGroupImpl)sample).getReferences();
		if(rs == null || rs.length == 0) return;
		for (int i = 0; i < rs.length; i++) {
			XModelObject[] cs = rs[i].getChildren(ENT_NAVIGATION_CASE);
			for (int j = 0; j < cs.length; j++) {
				rule.addChild(cs[j].copy());
			}
		}
		
	}
	
	private String getShape() {
		String x = getProperties().getProperty("process.mouse.x");
		String y = getProperties().getProperty("process.mouse.y");
		return (x == null || y == null) ? null : x + "," + y + ",0,0";		
	}
	
	public static String revalidatePath(String path) {
		if(path != null) path = path.trim();
		if(path == null || path.length() == 0) return path;
		if(!path.startsWith("/") && !path.startsWith("*")) path = "/" + path;
		if(path.indexOf('*') >= 0) return path;
		if(path.indexOf('.') < 0 && !path.endsWith("/")) {
			path += getExtension();
		}
		return path;
	}
	
	String revalidatePath(String path, String template) {
		if(path != null) path = path.trim();
		if(path == null || path.length() == 0) return path;
		if(!path.startsWith("/") && !path.startsWith("*")) path = "/" + path;
		if(path.indexOf('*') >= 0) return path;
		if(path.indexOf('.') < 0 && !path.endsWith("/")) {
			path += getExtension(template);
		}
		return path;
	}
	
	public boolean canCreateFile(String path) {
		XModelObject fs = getTarget().getModel().getByPath("FileSystems/WEB-ROOT");
		if(fs == null) return false;
		path = revalidatePath(path, getAttributeValue(0, "template"));
		if(path == null || path.length() == 0 || path.indexOf('*') >= 0) return false;
		return isCorrectPath(path) && !fileExists(path);
	}
	
	static String FORBIDDEN_INDICES = "\"\n\t*\\:<>?|";
	
	static boolean isCorrectPath(String path) {
		if(path == null || path.equals("/") || path.indexOf("//") >= 0) return false;
		if(path.endsWith("/") || path.indexOf("../") >= 0) return false;
		if(path.endsWith("..")) return false;
		if(path.equals("*")) return true;
		for (int i = 0; i < FORBIDDEN_INDICES.length(); i++) {
			if(path.indexOf(FORBIDDEN_INDICES.charAt(i)) >= 0) {
				return false;
			}
		}				
		return true;
	}
	
	boolean fileExists(String path) {
		return getTarget().getModel().getByPath(path) != null;
	} 
	
	void createFile(String path) throws XModelException {
		if(!canCreateFile(path)) return;
		String lastCreateFileValue = getAttributeValue(0, "create file");
		JSFModelPlugin.getDefault().getPluginPreferences().setDefault(LAST_CREATE_FILE_PREFERENCE, lastCreateFileValue);
		if(!"true".equals(lastCreateFileValue)) return;
		String template = getAttributeValue(0, "template");
		if(template != null) template = template.trim();
		File fs = (File)templates.getPageTemplates().get(template);
		if(fs == null || !fs.isFile()) throw new XModelException(NLS.bind(JSFUIMessages.TEMPLATE_IS_NOT_FOUND, template));
		String location = ((FileSystemImpl)getTarget().getModel().getByPath("FileSystems/WEB-ROOT")).getAbsoluteLocation();
		location += path;
		File ft = new File(location);
		ft.getParentFile().mkdirs();
		FileUtil.copyFile(fs, ft);
		getTarget().getModel().update();
		try {
			EclipseResourceUtil.getResource(getTarget()).getProject().refreshLocal(IProject.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			throw new XModelException(e);
		}
	}

	protected DefaultWizardDataValidator viewValidator = new ViewValidator();
    
	public WizardDataValidator getValidator(int step) {
		viewValidator.setSupport(this, step);
		return viewValidator;    	
	}
	
	class ViewValidator extends DefaultWizardDataValidator {
		public void validate(Properties data) {
			super.validate(data);
			if(message != null) return;
			String path = revalidatePath(data.getProperty("from-view-id"), data.getProperty("template"));
			if(!isCorrectPath(path)) {
				message = JSFUIMessages.ATTRIBUTE_FROM_VIEW_ID_IS_NOT_CORRECT;
			} 
			if(message != null) return;

			String template = data.getProperty("template");
			if(template != null && isFieldEditorEnabled(0, "template", data)) {
				if(template.trim().length() == 0) {
					message = JSFUIMessages.TEMPLATE_IS_NOT_SPECIFIED;
					return;
				}
				File templateFile = (File)templates.getPageTemplates().get(template.trim());
				if(templateFile == null || !templateFile.isFile()) {
					message = JSFUIMessages.TEMPLATE_DOES_NOT_EXIST;
				}
			}
			if(message != null) return;

			boolean doNotCreateEmptyRule = "yes".equals(JSFPreference.DO_NOT_CREATE_EMPTY_RULE.getValue());
			JSFNavigationModel m = (JSFNavigationModel)getTarget().getParent();
			String pp = NavigationRuleObjectImpl.toNavigationRulePathPart(path);
			boolean exists = m.getRuleCount(path) != 0 || getTarget().getChildByPath(pp) != null;
			if(doNotCreateEmptyRule && exists && !JSFProcessHelper.isPattern(path)) {
				message = JSFUIMessages.THE_VIEW_EXISTS;
			}
			
		}		
	}

	public boolean isFieldEditorEnabled(int stepId, String name, Properties values) {
		String path = values.getProperty("from-view-id");
		boolean c = canCreateFile(path);
		if(name.equals("create file")) {
			return c;
		}
		boolean g = c && "true".equals(values.getProperty("create file"));
		if(name.equals("template")) {
			return g;
		}
		return true;
	}

}
