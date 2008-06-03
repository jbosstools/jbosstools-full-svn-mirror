package org.jboss.ide.eclipse.archives.core.ant;

import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.jboss.ide.eclipse.archives.core.ant.ResourceModel.EclipseResource;

public class EclipseResourceGroupTask extends Task {
	private String id;
	private boolean global;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isGlobal() {
		return global;
	}
	public void setGlobal(boolean global) {
		this.global = global;
	}
	
	public void execute() throws BuildException {
		if( id == null || id.equals("")) {
			throw new BuildException("id cannot be null or blank");
		} else {
			ResourceModel.getDefault().addResourceGroup(id, resources, global, this);
		}
	}

	ArrayList<EclipseResource> resources = new ArrayList<EclipseResource>();
	public EclipseResource createEclipseResource() {
		EclipseResource res = new EclipseResource();
		resources.add(res);
		return res;
	}
}







