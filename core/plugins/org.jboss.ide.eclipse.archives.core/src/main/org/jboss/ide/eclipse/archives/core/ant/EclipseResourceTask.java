package org.jboss.ide.eclipse.archives.core.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

public class EclipseResourceTask extends Task {
	private String eclipsePath;
	private String loc;
	private boolean global = false;
	
	public void execute() throws BuildException {
		if( eclipsePath == null ) 
			log("eclipsePath is null. Skipping " + getLocation(), Project.MSG_WARN);
		else if( loc == null )
			log("loc is null. Skipping " + getLocation(), Project.MSG_WARN);
		else {
			if( global ) {
				ResourceModel.getDefault().addGlobalReference(eclipsePath, loc,this);
			} else if( getOwningTarget() == null || getOwningTarget().getName().equals("")) { 
				ResourceModel.getDefault().addProjectReference(getProject(), eclipsePath, loc, this);
			} else { 
				ResourceModel.getDefault().addTargetReference(getOwningTarget(), eclipsePath, loc, this);
			}
		}
	}
	
	public String getEclipsePath() {
		return eclipsePath;
	}
	public void setEclipsePath(String eclipsePath) {
		this.eclipsePath = eclipsePath;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}

	public boolean isGlobal() {
		return global;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}
		
}
