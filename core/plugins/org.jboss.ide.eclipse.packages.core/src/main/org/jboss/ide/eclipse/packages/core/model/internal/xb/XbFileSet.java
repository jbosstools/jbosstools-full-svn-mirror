/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.packages.core.model.internal.xb;

public class XbFileSet extends XbPackageNodeWithProperties {

	private String dir, file, toFile, includes, excludes, project;
	private boolean inWorkspace;
	
	public XbFileSet ()
	{
		super();
		inWorkspace = true;
	}
	
	public XbFileSet (XbFileSet fileset)
	{
		super(fileset);
		
		this.dir = fileset.dir == null ? null : new String(fileset.dir);
		this.file = fileset.file == null ? null : new String(fileset.file);
		this.toFile = fileset.toFile == null ? null : new String(fileset.toFile);
		this.includes = fileset.includes == null ? null : new String(fileset.includes);
		this.excludes = fileset.excludes == null ? null : new String(fileset.excludes);
		this.project = fileset.project == null ? null : new String(fileset.project);
		this.inWorkspace = fileset.inWorkspace;
	}
	
	protected Object clone() throws CloneNotSupportedException {
		return new XbFileSet(this);
	}
	
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getExcludes() {
		return excludes;
	}

	public void setExcludes(String excludes) {
		this.excludes = excludes;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getIncludes() {
		return includes;
	}

	public void setIncludes(String includes) {
		this.includes = includes;
	}

	public String getToFile() {
		return toFile;
	}

	public void setToFile(String toFile) {
		this.toFile = toFile;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public boolean isInWorkspace() {
		return inWorkspace;
	}

	public void setInWorkspace(boolean inWorkspace) {
		this.inWorkspace = inWorkspace;
	}
}
