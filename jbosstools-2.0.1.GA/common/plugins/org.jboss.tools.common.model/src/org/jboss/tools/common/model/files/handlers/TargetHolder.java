package org.jboss.tools.common.model.files.handlers;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.QualifiedName;
import org.jboss.tools.common.meta.action.XAction;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.plugin.ModelPlugin;
import org.jboss.tools.common.model.util.EclipseResourceUtil;

public class TargetHolder {
	protected XAction action;
	XModelObject target;
	String path;
	IResource folder;
	String addPath = "";
	
	public void setAction(XAction action) {
		this.action = action;
	}
	
	public void revalidate(String newPath) {
		if(newPath == path || (newPath != null && newPath.equals(path))) return;
		path = newPath;
		addPath = "";
		folder = (path == null) ? null : ModelPlugin.getWorkspace().getRoot().findMember(path);
		target = EclipseResourceUtil.getObjectByResource(folder);
		if(path != null && (folder == null || !folder.exists())) {
			String p = path.replace('\\', '/');
			String ap = "";
			while(true) {
				int q = p.lastIndexOf('/');
				if(q < 0) break;
				ap = p.substring(q) + ap;
				p = p.substring(0, q);
				folder = ModelPlugin.getWorkspace().getRoot().findMember(p);
				if(folder != null && folder.exists()) {
					addPath = ap;
					if(addPath.endsWith("/")) addPath = addPath.substring(0, addPath.length() - 1);
					target = EclipseResourceUtil.createObjectForResource(folder);
					break;
				}
			}
		} else if(target == null && folder != null && folder.exists()) {
			target = EclipseResourceUtil.createObjectForResource(folder);
		} else if(target != null && "FileSystems".equals(target.getModelEntity().getName())) {
			target = EclipseResourceUtil.findFileSystem(folder, target.getModel());
			if(target == null) target = EclipseResourceUtil.createObjectForResource(folder);
		}
	}

	public void saveLastPath() {
		if(path == null || folder == null) return;
		QualifiedName n = new QualifiedName("", action.getName() + "_lastPath");
		try {
			folder.getProject().setPersistentProperty(n, path);
		} catch (Exception e) {
			ModelPlugin.getPluginLog().logError("CreateFileSuppport:TargetHolder:saveLastPath:" + e.getMessage());
		}
	}
	
}
