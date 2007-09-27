package org.jboss.ide.eclipse.archives.test.model;

import java.io.File;

import junit.framework.TestCase;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.archives.core.model.ArchiveNodeFactory;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta.INodeDelta;
import org.jboss.ide.eclipse.archives.core.model.other.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.test.Activator;
import org.jboss.ide.eclipse.archives.test.util.JavaProjectHelper;
import org.jboss.ide.eclipse.archives.test.util.TestFileUtil;

/**
 * This class tests the model and events upon model changes
 * @author rstryker
 *
 */
public class RegisteredModelTest extends TestCase {
	private boolean initialized, finished;
	private IJavaProject testPackagesProject;
	private String testPackagesProjectRoot;
	public void setUp() {
		if (!initialized) {
			try {
				initialized = true;
				testPackagesProject = JavaProjectHelper.createJavaProject(
					"testPackagesProject", new String[] { "/src" }, "/bin");
				
				testPackagesProjectRoot = Activator.getDefault().getExampleProject();
			   TestFileUtil.copyDirectory (new File(testPackagesProjectRoot), testPackagesProject.getProject().getLocation().toFile(), true);
			   testPackagesProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
			} catch( Exception e ) {
				e.printStackTrace();
			}
		} 
	}
	
	
	public void testInitialized() {
		assertTrue(initialized);
		assertNotNull(testPackagesProject);
		assertNotNull(testPackagesProjectRoot);
		
		IPath projectLoc = testPackagesProject.getProject().getLocation();
		File archiveFile = projectLoc.append(ArchivesModel.PROJECT_PACKAGES_FILE).toFile();
		
		assertNull(ArchivesModel.instance().getRoot(projectLoc));
		assertFalse(archiveFile.exists());
		ArchivesModel.instance().registerProject(projectLoc, new NullProgressMonitor());
		assertNotNull(ArchivesModel.instance().getRoot(projectLoc));
		ArchivesModel.instance().saveModel(projectLoc, new NullProgressMonitor());
		archiveFile = projectLoc.append(ArchivesModel.PROJECT_PACKAGES_FILE).toFile();
		assertTrue(archiveFile.exists());
	}
	
	
	public void testAttach() {
		IPath projectLoc = testPackagesProject.getProject().getLocation();
		IArchiveModelNode model = ArchivesModel.instance().getRoot(projectLoc);
		
		// create a quick tree and attach it
		IArchive archive = ArchiveNodeFactory.createArchive();
		archive.setDestinationPath(new Path(Activator.getDefault().getOutputFolder()));
		
		IArchiveFileSet fs = ArchiveNodeFactory.createFileset(); 
		fs.setSourcePath(new Path(Activator.getDefault().getDummyFolder()));
		fs.setIncludesPattern("**");
		
		archive.addChild(fs);
		AddedModelListener listener = new AddedModelListener();
		ArchivesModel.instance().addModelListener(listener);
		
		ArchivesModel.instance().attach(model, archive, new NullProgressMonitor());
		assertTrue(listener.getReached());
		assertTrue(listener.getValid());
		ArchivesModel.instance().removeModelListener(listener);

	}
	
	protected class AddedModelListener implements IArchiveModelListener {
		private boolean reached = false;
		private boolean valid = false;
		public void modelChanged(IArchiveNodeDelta delta) {
			reached = true;
			
			assertTrue((IArchiveNodeDelta.CHILD_ADDED & delta.getKind()) != 0);
			
			IArchiveNodeDelta[] added = delta.getAddedChildrenDeltas();
			IArchiveNodeDelta[] removed = delta.getRemovedChildrenDeltas();
			IArchiveNodeDelta[] changedDesc = delta.getChangedDescendentDeltas();
			IArchiveNodeDelta[] all = delta.getAllAffectedChildren();
			
			assertEquals(1, added.length);
			assertEquals(0, removed.length);
			assertEquals(0, changedDesc.length);
			assertEquals(1, all.length);
			
			assertNull(delta.getPreNode());
			assertNotNull(delta.getPostNode());

			IArchiveNodeDelta[] added2 = added[0].getAddedChildrenDeltas();
			assertEquals(1, added2.length);
			assertEquals(IArchiveNodeDelta.ADDED, added2[0].getKind());
			valid = true;
		}
		public boolean getReached() { return reached; }
		public boolean getValid() { return valid; }
	}
	
	

	public void testAttributeChange() {
		IPath projectLoc = testPackagesProject.getProject().getLocation();
		IArchiveModelNode model = ArchivesModel.instance().getRoot(projectLoc);
		
		assertEquals(1, model.getAllChildren().length);
		IArchive archive = (IArchive)model.getAllChildren()[0];
		assertEquals(1, archive.getAllChildren().length);
		IArchiveFileSet fs = (IArchiveFileSet)archive.getAllChildren()[0];
		
		AttributeChangedModelListener attListener = new AttributeChangedModelListener();
		ArchivesModel.instance().addModelListener(attListener);
		fs.setExcludesPattern("BLAH.TXT");

		ArchivesModel.instance().saveModel(projectLoc, new NullProgressMonitor());
		assertTrue(attListener.getReached());
		assertTrue(attListener.getValid());
		ArchivesModel.instance().removeModelListener(attListener);
	}

	protected class AttributeChangedModelListener implements IArchiveModelListener {
		private boolean reached = false;
		private boolean valid = false;
		public void modelChanged(IArchiveNodeDelta delta) {
			reached = true;
			
			assertEquals(IArchiveNodeDelta.DESCENDENT_CHANGED, delta.getKind());
			assertEquals(1, delta.getAllAffectedChildren().length);
			IArchiveNodeDelta archive = (IArchiveNodeDelta)delta.getAllAffectedChildren()[0];
			assertEquals(IArchiveNodeDelta.DESCENDENT_CHANGED, archive.getKind());
			assertEquals(1, archive.getAllAffectedChildren().length);
			IArchiveNodeDelta fs = archive.getAllAffectedChildren()[0];
			assertEquals(IArchiveNodeDelta.ATTRIBUTE_CHANGED, fs.getKind());
			String[] atts = fs.getAttributesWithDeltas();
			assertEquals(1, atts.length);
			assertEquals(IArchiveFileSet.EXCLUDES_ATTRIBUTE, atts[0]);
			INodeDelta nd = fs.getAttributeDelta(atts[0]);
			String before = (String)nd.getBefore();
			String after = (String)nd.getAfter();
			assertEquals(null, before);
			assertEquals("BLAH.TXT", after);
			
			valid = true;
		}
		public boolean getReached() { return reached; }
		public boolean getValid() { return valid; }
	}
	

	
	public void testPropertyChange() {
		IPath projectLoc = testPackagesProject.getProject().getLocation();
		IArchiveModelNode model = ArchivesModel.instance().getRoot(projectLoc);
		
		assertEquals(1, model.getAllChildren().length);
		IArchive archive = (IArchive)model.getAllChildren()[0];
		assertEquals(1, archive.getAllChildren().length);
		IArchiveFileSet fs = (IArchiveFileSet)archive.getAllChildren()[0];
		
		
		// Test adding of a property
		PropertyChangedModelListener attListener = new PropertyChangedModelListener(IArchiveNodeDelta.PROPERTY_ADDED, "test", null, "result");
		ArchivesModel.instance().addModelListener(attListener);
		fs.setProperty("test", "result");
		ArchivesModel.instance().saveModel(projectLoc, new NullProgressMonitor());
		assertTrue(attListener.getReached());
		assertTrue(attListener.getValid());
		ArchivesModel.instance().removeModelListener(attListener);
		
		
		// test changing a property
		attListener = new PropertyChangedModelListener(IArchiveNodeDelta.PROPERTY_CHANGED, "test", "result", "blah");
		ArchivesModel.instance().addModelListener(attListener);
		fs.setProperty("test", "blah");
		ArchivesModel.instance().saveModel(projectLoc, new NullProgressMonitor());
		assertTrue(attListener.getReached());
		assertTrue(attListener.getValid());
		ArchivesModel.instance().removeModelListener(attListener);

		// test removed
		attListener = new PropertyChangedModelListener(IArchiveNodeDelta.PROPERTY_REMOVED, "test", "blah", null);
		ArchivesModel.instance().addModelListener(attListener);
		fs.setProperty("test", null);
		ArchivesModel.instance().saveModel(projectLoc, new NullProgressMonitor());
		assertTrue(attListener.getReached());
		assertTrue(attListener.getValid());
		ArchivesModel.instance().removeModelListener(attListener);

	}

	protected class PropertyChangedModelListener implements IArchiveModelListener {
		private boolean reached = false;
		private boolean valid = false;
		private int type;
		private String key, before, after;
		public PropertyChangedModelListener(int type, String key, String before, String after) {
			this.type = type;
			this.key = key;
			this.before = before;
			this.after = after;
		}
		public void modelChanged(IArchiveNodeDelta delta) {
			reached = true;
			
			assertEquals(IArchiveNodeDelta.DESCENDENT_CHANGED, delta.getKind());
			assertEquals(1, delta.getAllAffectedChildren().length);
			IArchiveNodeDelta archive = (IArchiveNodeDelta)delta.getAllAffectedChildren()[0];
			assertEquals(IArchiveNodeDelta.DESCENDENT_CHANGED, archive.getKind());
			assertEquals(1, archive.getAllAffectedChildren().length);
			IArchiveNodeDelta fs = archive.getAllAffectedChildren()[0];
			
			
			assertEquals(this.type, fs.getKind());
			String keys[] = fs.getPropertiesWithDeltas();
			assertEquals(1, keys.length);
			assertEquals(key, keys[0]);
			INodeDelta nd = fs.getPropertyDelta(keys[0]);
			assertEquals(before, nd.getBefore());
			assertEquals(after, nd.getAfter());
			
			valid = true;
		}
		public boolean getReached() { return reached; }
		public boolean getValid() { return valid; }
	}
	public void testFinal() {
		finished = true;
	}
	
	public void tearDown() {
		if( finished ) {
			try {
				testPackagesProject.getProject().delete(true, new NullProgressMonitor());
			}  catch( Exception e ) {
				e.printStackTrace();
			}
		}
	}
}
