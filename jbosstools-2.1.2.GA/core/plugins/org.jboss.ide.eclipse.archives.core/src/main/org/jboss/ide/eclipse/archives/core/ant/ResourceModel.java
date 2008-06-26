package org.jboss.ide.eclipse.archives.core.ant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;

public class ResourceModel {
	private static ResourceModel model;
	private static ArchivesCore archivesCore = null;
	public static ResourceModel getDefault() {
		if( model == null ) {
			model = new ResourceModel();
			try {
				archivesCore = new AntArchivesCore();
				ArchivesCore.setInstance(archivesCore);
			} catch( LinkageError le ) { 
				// ignore. the archives task will figure it out later
			}
		}
		return model;
	}
	
	private HashMap globals, globalGroups;
	private HashMap projects, projectGroups;
	private HashMap targets, targetGroups;
	private HashMap tasks;

	public ResourceModel() {
		globals = new HashMap();
		globalGroups = new HashMap();
		projects = new HashMap();
		projectGroups = new HashMap();
		targets = new HashMap();
		targetGroups = new HashMap();
		tasks = new HashMap();
	}
	
	public void addGlobalReference(String path, String loc, Task task) {
		if( !globals.containsKey(path)) {
			globals.put(path, loc);
			task.log("added global-scoped reference for " + path, Project.MSG_VERBOSE);
		} else {
			task.log("eclipse path " + path + " already declared globally. Skipping. " + task.getLocation(), Project.MSG_INFO);
		}
	}
	
	public void addProjectReference(Project project, String path, String loc, Task task) {
		HashMap map = (HashMap)projects.get(project);
		if( map == null ) {
			map = new HashMap();
			projects.put(project, map);
		}
		if( !map.containsKey(path)) {
			map.put(path, loc);
			task.log("added project-scoped reference for " + path, Project.MSG_VERBOSE);
		} else {
			task.log("eclipse path " + path + " already declared in this project. Skipping. " + task.getLocation(), Project.MSG_INFO);
		}
	}

	
	public void addTargetReference(Target target, String path, String loc, Task task) {
		HashMap map = (HashMap)targets.get(target);
		if( map == null ) {
			map = new HashMap();
			targets.put(target, map);
		}
		if( !map.containsKey(path)) {
			map.put(path, loc);
			task.log("added target-scoped reference for " + path + ", target=" + target, Project.MSG_VERBOSE);
		} else {
			task.log("eclipse path " + path + " already declared in this target. Skipping. " + task.getLocation(), Project.MSG_INFO);
		}
	}

	public void addResourceGroup(String id, ArrayList resources, boolean global, Task task) {
		boolean hasTarget = !(task.getOwningTarget() == null || task.getOwningTarget().getName().equals(""));
		HashMap map = global ? globalGroups : hasTarget ? targetGroups : projectGroups;
		String scope = global ? "global" : hasTarget ? "target" : "project";
		HashMap inner = getInnerResourceMap(task, resources);
		
		if( global && map.containsKey(id)) {
			task.log("Cannot add " + id + " with scope " + scope + ". An item with that id has already been added. " + task.getLocation(), Project.MSG_INFO);
		} else if( global ) {
			map.put(id, inner);
			task.log("added " + scope + "-scoped map with id \"" + id + "\" to " + map, Project.MSG_VERBOSE);
		} else {
			Object mainKey = hasTarget ? (Object)task.getOwningTarget() : task.getProject();
			HashMap midMap = (HashMap)map.get(mainKey);
			if( midMap == null ) {
				midMap = new HashMap();
				map.put(mainKey, midMap);
			}
			if( midMap.containsKey(id)) {
				task.log("Cannot add " + id + " with scope " + scope + ". An item with that id has already been added. " + task.getLocation(), Project.MSG_INFO);
			} else {
				midMap.put(id, inner);
				task.log("added " + scope + "-scoped map with id \"" + id + "\" to " + map, Project.MSG_VERBOSE);
			}
		}
	}
	
	protected HashMap getInnerResourceMap(Task task, ArrayList resources) {
        HashMap ret = new HashMap();
		for (Iterator it=resources.iterator(); it.hasNext(); ) {
        	EclipseResource resource = (EclipseResource)it.next();
        	if( resource.getEclipsePath() == null )
        		task.log("eclipsePath is null. skipping " + task.getLocation(), Project.MSG_INFO);
        	else if( resource.getLoc() == null )
        		task.log("loc is null. skipping " + task.getLocation(), Project.MSG_INFO);
        	else {
        		ret.put(resource.getEclipsePath(), resource.getLoc());
        	}
        }
		return ret;
	}
	
	public void setTaskEnvironment(Task task, ArrayList resources, String refs) {
		// add the directly added stuff
		HashMap pathToLoc = new HashMap();
        for (Iterator it=resources.iterator(); it.hasNext(); ) {
        	EclipseResource resource = (EclipseResource)it.next();
        	if( resource.getEclipsePath() == null ) {
        		task.log("eclipsePath is null. skipping " + task.getLocation(), Project.MSG_INFO);
        	} else if( resource.getLoc() == null ) {
        		task.log("loc is null. skipping " + task.getLocation(), Project.MSG_INFO);
        	} else if( pathToLoc.containsKey(resource.getEclipsePath())){
        		task.log("path " + resource.getEclipsePath() + " already added. Ignoring.", Project.MSG_INFO);
        		pathToLoc.put(resource.getEclipsePath(), resource.getLoc());
        	}
        }
        
        // add anything in the current target
        if( task.getOwningTarget() != null ) {
        	HashMap inner = (HashMap)targets.get(task.getOwningTarget());
        	if( inner != null ) {
        		Iterator i = inner.keySet().iterator();
        		while(i.hasNext()) {
        			String key = (String)i.next();
        			if( !pathToLoc.containsKey(key)) {
        				pathToLoc.put(key, inner.get(key));
        			}
        		}
        	}
        }
        
        // add projects
        if( task.getProject() != null ) {
        	HashMap inner = (HashMap)projects.get(task.getProject());
        	if( inner != null ) {
        		Iterator i = inner.keySet().iterator();
        		while(i.hasNext()) {
        			String key = (String)i.next();
        			if( !pathToLoc.containsKey(key)) {
        				pathToLoc.put(key, inner.get(key));
        			}
        		}
        	}
        }
        
        // add globals
		Iterator i = globals.keySet().iterator();
		while(i.hasNext()) {
			String key = (String)i.next();
			if( !pathToLoc.containsKey(key)) {
				pathToLoc.put(key, globals.get(key));
			}
		}
        
		// add referenced groups
		if( refs != null ) {
			String[] refIDs = refs.split(",");
			for( int j = 0; j < refIDs.length; j++ ) { 
				HashMap tmp = getGroup(task, refIDs[j]);
				i = tmp.keySet().iterator();
				while(i.hasNext()) {
					String key = (String)i.next();
					if( pathToLoc.containsKey(key)) 
						task.log("Path " + key + " already included. Overriding. " + task.getLocation(), Project.MSG_INFO);
					pathToLoc.put(key, tmp.get(key));
				}
			}
		}
		tasks.put(task, pathToLoc);
	}

	protected HashMap getGroup(Task task, String id) {
		if( id.equals("")) return new HashMap();
		if( task.getOwningTarget() != null ) {
			Target t = task.getOwningTarget();
			HashMap group = (HashMap)targetGroups.get(t);
			if( group != null && group.containsKey(id)) 
				return (HashMap)group.get(id);
		}

		if( task.getProject() != null ) {
			Project p = task.getProject();
			HashMap group = (HashMap)projectGroups.get(p);
			if( group != null && group.containsKey(id))
					return (HashMap)group.get(id);
		}

		if( globalGroups.containsKey(id) && globalGroups.get(id) != null)
			return (HashMap)globalGroups.get(id);
		task.log("Group refid " + id + " not found. " + task.getLocation(), Project.MSG_INFO);
		return new HashMap();
	}
	
	public HashMap getTaskEnvironment(Task t) {
		HashMap map = (HashMap)tasks.get(t);
		if( map == null ) return new HashMap();
		return (HashMap)(map.clone());
	}
	
	
	public static class EclipseResource {
		private String eclipsePath;
		private String loc;
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
	}

	
}
