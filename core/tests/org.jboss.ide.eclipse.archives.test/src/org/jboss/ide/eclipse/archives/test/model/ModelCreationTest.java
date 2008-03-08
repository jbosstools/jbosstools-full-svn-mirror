/**
 * JBoss, a Division of Red Hat
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
package org.jboss.ide.eclipse.archives.test.model;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.IArchiveBuildListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListenerManager;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.archives.core.util.ModelUtil;

/**
 * @author rob.stryker <rob.stryker@redhat.com>
 *
 */
public class ModelCreationTest extends TestCase {
	public void testModelCreation1() {
		XbPackages packs = new XbPackages();
		XbPackage pack = new XbPackage();
		packs.addChild(pack);
		ArchiveModelNode model = getModel(packs);
		ModelUtil.fillArchiveModel(packs, model);
		// passes for now. 
	}
	
	protected ArchiveModelNode getModel(XbPackages packs) {
		IArchiveModelListenerManager manager = new IArchiveModelListenerManager() {
			public void addBuildListener(IArchiveBuildListener listener) {
			}
			public void addModelListener(IArchiveModelListener listener) {
			}
			public IArchiveBuildListener[] getBuildListeners() {
				return new IArchiveBuildListener[] {};
			}
			public IArchiveModelListener[] getModelListeners() {
				return new IArchiveModelListener[] {};
			}
			public void removeBuildListener(IArchiveBuildListener listener) {
			}
			public void removeModelListener(IArchiveModelListener listener) {
			}
		};
		IPath project = new Path("test").append("two");
		ArchiveModelNode node = new ArchiveModelNode(project, packs, manager);
		return node;
	}
}
