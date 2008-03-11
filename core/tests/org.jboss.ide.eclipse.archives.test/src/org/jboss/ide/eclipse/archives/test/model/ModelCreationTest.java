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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.ArchiveNodeFactory;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelException;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModel;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.archives.core.util.ModelUtil;

/**
 * @author rob.stryker <rob.stryker@redhat.com>
 *
 */
public class ModelCreationTest extends TestCase {
	protected IPath project = new Path("test").append("project");
	TempArchiveModelListener modelListener = createListener();
	protected void setUp() throws Exception {
		modelListener.clearDelta();
	}

	public void testSimpleCreation() {
		createModelNode();
	}
	
	public void testAddToModel() {
		ArchiveModelNode modelNode = createModelNode();
		modelNode.getModel().registerProject(modelNode, new NullProgressMonitor());
		assertEquals(modelNode,modelNode.getModel().getRoot(project));
		assertNotSame(null, modelListener.getDelta());
		assertEquals(IArchiveNodeDelta.NODE_REGISTERED, modelListener.getDelta().getKind());
		modelNode.getModel().registerProject(modelNode, new NullProgressMonitor());
	}
	
	protected ArchiveModelNode createModelNode() {
		try {
			XbPackages packs = new XbPackages();
			XbPackage pack = new XbPackage();
			packs.addChild(pack);
			ArchiveModelNode model = getModel(packs);
			ModelUtil.fillArchiveModel(packs, model);
			assertEquals(project, model.getProjectPath());
			assertEquals(IArchiveNode.TYPE_MODEL_ROOT, model.getNodeType());
			assertEquals(null, model.getParent());
			assertEquals(packs, model.getNodeDelegate());
			assertTrue(model.hasChildren());
			assertEquals(1, model.getAllChildren().length);
			assertEquals(null, ArchivesModel.instance().getRoot(project));
			assertEquals(null, modelListener.getDelta());
			return model;
		} catch( ArchivesModelException ame ) {
			fail(ame.getMessage());
		}
		return null;
	}
	
	public void testAddFolderToModel() {
		try {
			ArchiveModelNode modelNode = createModelNode();
			IArchiveFolder folder = ArchiveNodeFactory.createFolder();
			folder.setName("testFolder");
			modelNode.addChild(folder);
		} catch( ArchivesModelException ame ) {
			return;
		}
		fail();
	}
	
	public void testAddFilesetToModel() {
		try {
			ArchiveModelNode modelNode = createModelNode();
			IArchiveFileSet fs = ArchiveNodeFactory.createFileset();
			fs.setIncludesPattern("*");
			fs.setSourcePath(new Path("blah"));
			modelNode.addChild(fs);
		} catch( ArchivesModelException ame ) {
			return;
		}
		fail();
	}
	

	public void testAddActionToModel() {
		fail();
	}
	
	public void testAddArchiveToModel() {
		try {
			ArchiveModelNode modelNode = createModelNode();
			IArchive archive = ArchiveNodeFactory.createArchive();
			archive.setName("someName.war");
			archive.setDestinationPath(new Path("test"));
			modelNode.addChild(archive);
		} catch( ArchivesModelException ame ) {
			fail();
		}
	}
	
	
	
//	public void testDeltas() {
//		try {
//			ArchiveModelNode model = createModelNode();
//			model.clearDelta();
//			IArchiveFolder folder = ArchiveNodeFactory.createFolder();
//			folder.setName("testFolder");
//			model.addChild(folder);
//			IArchiveNodeDelta delta = model.getDelta();
//			assertEquals(IArchiveNodeDelta.CHILD_ADDED, delta.getKind());
//		} catch( ArchivesModelException ame ) {
//			fail(ame.getMessage());
//		}
//	}
	
	protected ArchiveModelNode getModel(XbPackages packs) {
		IArchiveModel model = new ArchivesModel();
		model.addModelListener(modelListener);
		ArchiveModelNode node = new ArchiveModelNode(project, packs, model);
		return node;
	}
	
	protected TempArchiveModelListener createListener() {
		return new TempArchiveModelListener();
	}
	
	protected class TempArchiveModelListener implements IArchiveModelListener {
		private IArchiveNodeDelta delta;
		public void modelChanged(IArchiveNodeDelta delta) {
			this.delta = delta;
		} 
		public IArchiveNodeDelta getDelta() { return delta; }
		public void clearDelta() { delta = null; }
	}
}
