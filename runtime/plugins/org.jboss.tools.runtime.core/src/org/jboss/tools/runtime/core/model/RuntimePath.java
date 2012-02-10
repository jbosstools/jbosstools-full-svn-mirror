/*************************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.runtime.core.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.runtime.core.model.RuntimeDefinition;

/**
 * @author snjeza
 *
 */
public class RuntimePath implements Cloneable {

	private String path;
	private boolean scanOnEveryStartup;
	private List<RuntimeDefinition> serverDefinitions;
	private long timestamp;

	public RuntimePath(String path) {
		this.path = path;
		this.scanOnEveryStartup = false;
		this.serverDefinitions = new ArrayList<RuntimeDefinition>();
		this.timestamp = -1;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isScanOnEveryStartup() {
		return scanOnEveryStartup;
	}

	public void setScanOnEveryStartup(boolean scanOnEveryStartup) {
		this.scanOnEveryStartup = scanOnEveryStartup;
	}

	public List<RuntimeDefinition> getServerDefinitions() {
		return serverDefinitions;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		RuntimePath runtimePath = new RuntimePath(path);
		runtimePath.setScanOnEveryStartup(scanOnEveryStartup);
		runtimePath.serverDefinitions = (List<RuntimeDefinition>) ((ArrayList<RuntimeDefinition>)serverDefinitions).clone();
		return runtimePath;
	}

	public boolean isModified() {
		if (path == null || path.isEmpty()) {
			return false;
		}
		if (timestamp < 0) {
			return true; 
		}
		
		File directory = new File(path); 
		if (! directory.isDirectory()){
			return false;
		}
		try {
			return directory.lastModified() > timestamp;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return "RuntimePath [path=" + path + "]";
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RuntimePath other = (RuntimePath) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

}
