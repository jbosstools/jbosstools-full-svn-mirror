package org.jboss.ide.eclipse.packages.test.build;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.model.AbstractPackagesBuildListener;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageNodeImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.project.PackagesBuilder;
import org.jboss.ide.eclipse.packages.core.project.build.PackageBuildDelegate;

import de.schlichtherle.io.File;

public class IncrementalBuilderTest extends BuildTest {
	
	public IncrementalBuilderTest (String testName)
	{
		super(testName);
	}
	
	public static Test suite ()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new IncrementalBuilderTest("testSimpleJar_buildWithAPI"));
		suite.addTest(new IncrementalBuilderTest("testSimpleJar_changeFile"));
		suite.addTest(new IncrementalBuilderTest("testSimpleJar_addFile"));
		suite.addTest(new IncrementalBuilderTest("testSimpleJar_removeFile"));
		suite.addTest(new IncrementalBuilderTest("testSimpleJar_changeFilesetPattern_addFile"));
		suite.addTest(new IncrementalBuilderTest("testSimpleJar_changeFilesetPattern_removeFile"));
		suite.addTest(new IncrementalBuilderTest("testExplodedJar"));
		suite.addTest(new IncrementalBuilderTest("testSimpleJar_changeToExploded"));
		suite.addTest(new IncrementalBuilderTest("testSimpleJar_changeToCompressed"));
		suite.addTest(new IncrementalBuilderTest("testBaseFile"));
		suite.addTest(new IncrementalBuilderTest("testOverlappingFilesets"));
		return suite;
	}
	
	private class SimpleJarListener extends AbstractPackagesBuildListener
	{	
		public boolean startedBuildingPackage = false, startedCollecting = false;
		public boolean finishedBuildingPackage = false, finishedCollecting = false;
		
		public void startedBuildingPackage(IPackage pkg) {
			startedBuildingPackage = true;
			finishedBuildingPackage = false;
			assertEquals(pkg, simpleJar);
		}
		
		public void startedCollectingFileSet(IPackageFileSet fileset) {
			startedCollecting = true;
			assertEquals(fileset, simpleJarFileset);
		}
		
		public void finishedCollectingFileSet(IPackageFileSet fileset) {
			finishedCollecting = true;
			assertEquals(fileset, simpleJarFileset);
		}
		
		public void finishedBuildingPackage (IPackage pkg) {
			assertTrue(startedBuildingPackage);
			assertTrue(startedCollecting);
			assertTrue(finishedCollecting);
			
			assertTrue(simpleJar.getPackageFile().exists());
			assertTestXmlContents (testXml_originalContents);
			
			startedBuildingPackage = startedCollecting = finishedCollecting = false;
			finishedBuildingPackage = true;
		}
	}
	
	public void testSimpleJar_buildWithAPI ()
	{
		SimpleJarListener listener = new SimpleJarListener();
		
		PackagesModel.instance().addPackagesBuildListener(listener);

		buildDelegate.buildSinglePackage(simpleJar, nullMonitor);
		assertTrue(listener.finishedBuildingPackage);
		
		assertTrue(ProjectUtil.projectHasBuilder(project, PackagesBuilder.BUILDER_ID));
		
		PackagesModel.instance().removePackagesBuildListener(listener);
	}
	
	public void testSimpleJar_changeFile ()
	{	
		setContents(testXmlFile, testXml_newContents);
		
		waitForAutoBuild();
		
		assertTestXmlContents(testXml_newContents);
		
		assertTrue (refJarFile.exists());
		
		File libFolderFile = findFile(refJarFile, "lib");
		assertNotNull (libFolderFile);
		
		File nestedSimpleJarFile = findFile(libFolderFile, "simple.jar");
		assertNotNull(nestedSimpleJarFile);
		
		File nestedTestXmlFile = findFile(nestedSimpleJarFile, "test.xml");
		assertNotNull(nestedTestXmlFile);
		
		assertFileContents(nestedTestXmlFile, testXml_newContents);
	}
	
	public void testSimpleJar_addFile ()
	{
		IFile addedXMLFile = project.getFile("added.xml");
		setContents(addedXMLFile, addedXml_contents);
		
		waitForAutoBuild();
	
		assertFileContents (simpleJar, "added.xml", addedXml_contents);
	}
	
	public void testSimpleJar_removeFile ()
	{
		IFile addedXML = project.getFile("added.xml");
		
		try {
			addedXML.delete(true, nullMonitor);
		} catch (CoreException e) {
			fail(e.getMessage());
		}
		
		waitForAutoBuild();
		
		assertFalse(addedXML.exists());
		
		File addedXMLFile = findFile(simpleJarFile, "added.xml");
		
		assertNull(addedXMLFile);
	}
	
	public void testSimpleJar_changeFilesetPattern_addFile ()
	{
		IFile nestedXMLFile = project.getFile(new Path("dir1/dir2/nested.xml"));
		setContents(nestedXMLFile, nestedXml_contents);
		
		File dir1 = findFile(simpleJarFile, "dir1");
		
		assertNull(dir1);
		waitForAutoBuild();
		
		simpleJarFileset.setIncludesPattern("**/*.xml");
		((PackageNodeImpl)simpleJarFileset).flagAsChanged();
		
		dir1 = findFile(simpleJarFile, "dir1");
		assertNotNull(dir1);
		assertEquals(dir1.getName(), "dir1");
		
		File dir2 = (File) dir1.listFiles()[0];
		assertNotNull(dir2);
		assertEquals(dir2.getName(), "dir2");
		
		File nestedXMLFile2 = (File) dir2.listFiles()[0];
		assertNotNull(nestedXMLFile2);
		assertEquals(nestedXMLFile2.getName(), "nested.xml");
		
		assertFileContents (nestedXMLFile2, nestedXml_contents);
	}
	
	public void testSimpleJar_changeFilesetPattern_removeFile ()
	{
		simpleJarFileset.setIncludesPattern("*.xml");
		((PackageNodeImpl)simpleJarFileset).flagAsChanged();
		
		waitForAutoBuild();
		
		File dir1 = findFile(simpleJarFile, "dir1");
		assertNull(dir1);
	}
	
	public void testExplodedJar ()
	{
		PackageBuildDelegate.instance().buildSinglePackage(explodedJar, nullMonitor);
		
		assertTrue(explodedJarFile.exists());
		assertTrue(explodedJarFile.getDelegate().isDirectory());
	}
	
	public void testSimpleJar_changeToExploded ()
	{
		simpleJar.setExploded(true);
		((PackageNodeImpl)simpleJar).flagAsChanged();
		
		assertTrue(simpleJarFile.getDelegate().isDirectory());
	}
	
	public void testSimpleJar_changeToCompressed ()
	{
		simpleJar.setExploded(false);
		((PackageNodeImpl)simpleJar).flagAsChanged();
		
		assertTrue(simpleJarFile.getDelegate().isFile());
	}
	
	public void testBaseFile ()
	{
		IPath projectPath = project.getFullPath();
		IPath filePath = projectPath.append("ref.jar").append("lib").append("simple.jar").append("test.xml");
		
		IPath basePath = PackagesCore.getBaseFile(filePath);
		
		assertEquals (basePath.segmentCount(), 2);
		assertEquals (basePath.segment(0), project.getName());
		assertEquals (basePath.segment(1), "ref.jar");
		
		filePath = projectPath.append("exploded.jar").append("test.xml");
		basePath = PackagesCore.getBaseFile(filePath);
		
		assertEquals (basePath.segmentCount(), 3);
		assertEquals (basePath.segment(0), project.getName());
		assertEquals (basePath.segment(1), "exploded.jar");
		assertEquals (basePath.segment(2), "test.xml");
	}
	
	public void testOverlappingFilesets ()
	{
		IPackageFileSet overlap = PackagesCore.createPackageFileSet(project);
		overlap.setSourceContainer(project);
		overlap.setIncludesPattern("test.xml");
		simpleJar.addChild(overlap);
		
		simpleJarFileset.setIncludesPattern("**/nested.xml");
		((PackageNodeImpl)simpleJarFileset).flagAsChanged();
		
		File testXML = findFile (simpleJarFile, "test.xml");
		assertNotNull(testXML);
		assertTrue(testXML.exists());
		
		simpleJar.removeChild(simpleJarFileset);
		assertTrue(testXML.exists());
	}
}
