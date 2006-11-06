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
package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;

/**
 * The super type of all package nodes (IPackage, IPackageFileSet, IPackageFolder)
 * 
 * Each node in a package may have arbitrary properties that can be reflected upon by other plug-ins
 * 
 * @author <a href="marshall@jboss.org">Marshall Culpepper</a>
 * @version $Revision$
 */
public interface IPackageNode extends IAdaptable {

	public static final int TYPE_PACKAGE = 0;
	public static final int TYPE_PACKAGE_FILESET = 1;
	public static final int TYPE_PACKAGE_FOLDER = 2;
	
	public IPackageNode getParent();
	public IPackageNode[] getChildren(int type);
	public IPackageNode[] getAllChildren();
	public boolean hasChildren();
	public boolean hasChild(IPackageNode child);
	
	public int getNodeType();
	
	public String getProperty(String property);
	
	public IProject getProject();
	
	public boolean accept(IPackageNodeVisitor visitor);
	public boolean accept(IPackageNodeVisitor visitor, boolean depthFirst);
	
	public IPackageNodeWorkingCopy createWorkingCopy();
	
	public void addChild(IPackageNode child);
	
	public void removeChild(IPackageNode child);
}
