package org.jboss.ide.eclipse.packages.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.core.test.util.JavaProjectHelper;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.core.util.ResourceUtil;
import org.jboss.ide.eclipse.packages.core.model.AbstractPackagesBuildListener;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageBuildDelegate;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.model.types.JARPackageType;
import org.jboss.ide.eclipse.packages.core.project.PackagesBuilder;

import de.schlichtherle.io.FileInputStream;

public class PackagesBuildTest extends TestCase{

	private IProject project;
	private IJavaProject javaProject;
	
	private PackageBuildDelegate buildDelegate;
	private NullProgressMonitor nullMonitor = new NullProgressMonitor();
	
	private IPackage simpleJar;
	private IFile testXmlFile;
	private IPackageFileSet simpleJarFileset;
	
	private static boolean initialized = false;
	
	public PackagesBuildTest (String testName)
	{
		super(testName);
	}
	
	protected void setUp() throws Exception {
		if (!initialized)
		{
			javaProject = JavaProjectHelper.createJavaProject("buildProject", new String[] { "src" }, "/bin");
			project = javaProject.getProject();
			
			buildDelegate = new PackageBuildDelegate(project);
			testXmlFile = project.getFile("test.xml");
			
			simpleJar = PackagesCore.createDetachedPackage(project, true);
			simpleJar.setName("simple.jar");
			simpleJar.setPackageType(PackagesCore.getPackageType(JARPackageType.TYPE_ID));
			simpleJar.setDestinationContainer(project);
	
			setContents(testXmlFile, testXml_originalContents);
			
			simpleJarFileset = PackagesCore.createDetachedPackageFileSet(project);
			simpleJarFileset.setIncludesPattern("*.xml");
			simpleJarFileset.setSourceContainer(project);
			
			simpleJar.addChild(simpleJarFileset);
			
			PackagesModel.instance().attach(simpleJar, nullMonitor);
			initialized = true;
		} else{
			
			project = ResourcesPlugin.getWorkspace().getRoot().getProject("buildProject");
			javaProject = JavaCore.create(project);
			
			buildDelegate = new PackageBuildDelegate(project);
			testXmlFile = project.getFile("test.xml");
			
			List packages = PackagesModel.instance().getProjectPackages(project);
			simpleJar = (IPackage) packages.get(0);
			simpleJarFileset = (IPackageFileSet) simpleJar.getChildren(IPackageNode.TYPE_PACKAGE_FILESET)[0];
		}
	}
	
	private void setContents(IFile file, String contents)
	{
		ByteArrayInputStream stream = new ByteArrayInputStream(contents.getBytes());
		
		try {
			if (file.exists())
			{
				file.setContents(stream, false, false, nullMonitor);
			} else {
				ResourceUtil.safeCreateFile(file, stream, false, nullMonitor);
			}
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}
	
	private static String testXml_originalContents = "<testXML><text>YoYo</text></testXML>";
	private static String testXml_newContents = "<testXML><text>YoYo - Revision 2</text></testXML>";
	private static String addedXml_contents = "<added><text>added content</text></added>";
	private static String nestedXml_contents = "<nested<text>nested content</text></nested>";
	
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

	private void assertTestXmlContents (String contents)
	{
		assertFileContents(simpleJar, "test.xml", contents);
	}
	
	private de.schlichtherle.io.File getPackageFile (IPackage pkg)
	{
		return new de.schlichtherle.io.File(pkg.getPackageFile().getRawLocation().toFile());
	}
	
	private File findFile (de.schlichtherle.io.File jarFile, String name)
	{
		File subFiles[] = jarFile.listFiles();
		assertNotNull(subFiles);
		
		File file = null;
		for (int i = 0; i < subFiles.length; i++)
		{
			if (subFiles[i].getName().equals(name)) {
				file = subFiles[i]; break;
			}
		}
		
		return file;
	}
	
	private void assertFileContents (File file, String contents)
	{
		assertNotNull(file);
		assertEquals(file.length(), contents.length());
		
		byte bytes[] = new byte[0];
		try {
			FileInputStream stream = new FileInputStream(file);
			bytes = new byte[(int)file.length()];
			stream.read(bytes);
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		String fileContents = new String(bytes);
		assertEquals(fileContents, contents);
	}
	
	private void assertFileContents (IPackage jar, String fileName, String contents)
	{
		de.schlichtherle.io.File jarFile = getPackageFile(jar);
		assertTrue(jarFile.exists());
		assertTrue(jarFile.isArchive());

		File file = findFile (jarFile, fileName);
		assertFileContents (file, contents);
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
	
	private void waitForBuilder ()
	{
		//	 wait for incremental builder to finish
		try {
			Thread.sleep(1000 * 3);
			while (PackageBuildDelegate.isBuilding())
			{
				Thread.sleep(300);
			}
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}	
	}
	
	public void testSimpleJar_changeFile ()
	{	
		setContents(testXmlFile, testXml_newContents);
		
		waitForBuilder();
		
		assertTestXmlContents(testXml_newContents);
	}
	
	public void testSimpleJar_addFile ()
	{
		IFile addedXMLFile = project.getFile("added.xml");
		setContents(addedXMLFile, addedXml_contents);
		
		waitForBuilder();
	
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
		
		waitForBuilder();
		
		assertFalse(addedXML.exists());
		
		de.schlichtherle.io.File jarFile = getPackageFile(simpleJar);
		File addedXMLFile = findFile(jarFile, "added.xml");
		
		assertNull(addedXMLFile);
	}
	
	public void testSimpleJar_changeFilesetPattern ()
	{
		IFile nestedXMLFile = project.getFile(new Path("dir1/dir2/nested.xml"));
		setContents(nestedXMLFile, nestedXml_contents);
		
		de.schlichtherle.io.File jarFile = getPackageFile(simpleJar);
		File dir1 = findFile(jarFile, "dir1");
		
		assertNull(dir1);
		
		simpleJarFileset.setIncludesPattern("**/*.xml");
		
		waitForBuilder();
		
		dir1 = findFile(jarFile, "dir1");
		assertNotNull(dir1);
		assertEquals(dir1.getName(), "dir1");
		
		File dir2 = dir1.listFiles()[0];
		assertNotNull(dir2);
		assertEquals(dir2.getName(), "dir2");
		
		File nestedXMLFile2 = dir2.listFiles()[0];
		assertNotNull(nestedXMLFile2);
		assertEquals(nestedXMLFile2.getName(), "nested.xml");
		
		assertFileContents (nestedXMLFile2, nestedXml_contents);
	}
	
}
