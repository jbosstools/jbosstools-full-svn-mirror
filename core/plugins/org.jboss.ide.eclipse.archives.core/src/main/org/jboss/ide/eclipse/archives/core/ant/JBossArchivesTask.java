package org.jboss.ide.eclipse.archives.core.ant;

import java.util.ArrayList;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.jboss.ide.eclipse.archives.core.ant.ResourceModel.EclipseResource;

public class JBossArchivesTask extends Task {
	private static boolean linkError = false;
	
	private boolean antOnly = false;
	private boolean outputAntTargetsOnly = false;
    private String processArchive;
    private String eclipseProject;
    private String refs;
    
	public void init() throws BuildException {
	}
	
	public void execute() throws BuildException {
		// parse children and add them to the map
		ResourceModel.getDefault().setTaskEnvironment(this, resources, refs);
		Throwable t = null;
		if( !antOnly && !linkError) {
			try {
				executeTrueZip();
			} catch( LinkageError le ) {
				t = le;
				linkError = true;
				log("Linkage Error using Truezip. Falling back to ant", Project.MSG_WARN);
			}
		} 
		if( antOnly || t != null ) {
			executeAntOnly();
		}
	}
	

	ArrayList<EclipseResource> resources = new ArrayList<EclipseResource>();
	public EclipseResource createEclipseResource() {
		EclipseResource res = new EclipseResource();
		resources.add(res);
		return res;
	}

	
	protected void executeTrueZip() {
		// isolating to another file to avoid ant errors. 
		new IsolatedTruezipExecution(this).execute();
	}
	
	protected void executeAntOnly() {
		new IsolatedAntExecution(this).execute();
	}
	
	public boolean isAntOnly() {
		return antOnly;
	}

	public void setAntOnly(boolean antOnly) {
		this.antOnly = antOnly;
	}

	public boolean isOutputAntTargetsOnly() {
		return outputAntTargetsOnly;
	}

	public void setOutputAntTargetsOnly(boolean outputAntTargetsOnly) {
		this.outputAntTargetsOnly = outputAntTargetsOnly;
	}

	public String getProcessArchive() {
		return processArchive;
	}

	public void setProcessArchive(String processArchive) {
		this.processArchive = processArchive;
	}

	public String getEclipseProject() {
		return eclipseProject;
	}

	public void setEclipseProject(String eclipseProject) {
		this.eclipseProject = eclipseProject;
	}

	public String getRefs() {
		return refs;
	}

	public void setRefs(String environment) {
		this.refs = environment;
	}
}
