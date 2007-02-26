package org.jboss.ide.eclipse.packages.test.build;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.core.test.util.JavaProjectHelper;
import org.jboss.ide.eclipse.core.util.ResourceUtil;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.model.types.JARPackageType;
import org.jboss.ide.eclipse.packages.core.project.build.PackageBuildDelegate;

import de.schlichtherle.io.File;
import de.schlichtherle.io.FileInputStream;

import junit.framework.TestCase;

public class BuildTest extends TestCase {
	protected IProject project;
	protected IJavaProject javaProject;
	
	protected PackageBuildDelegate buildDelegate;
	protected NullProgressMonitor nullMonitor = new NullProgressMonitor();
	
	protected IPackage simpleJar, refJar, explodedJar;
	protected IFile testXmlFile;
	protected IPackageFileSet simpleJarFileset, explodedJarFileset;
	protected IPackageFolder libFolder;
	
	protected static boolean initialized = false;

	protected static String testXml_originalContents = "<testXML><text>YoYo</text></testXML>";
	protected static String testXml_newContents = "<testXML><text>YoYo - Revision 2</text></testXML>";
	protected static String addedXml_contents = "<added><text>added content</text></added>";
	protected static String nestedXml_contents = "<nested<text>nested content</text></nested>";
	
	public BuildTest (String testName)
	{
		super(testName);
	}
	
	protected void setUp() throws Exception {
		if (!initialized)
		{
			javaProject = JavaProjectHelper.createJavaProject("buildProject", new String[] { "src" }, "/bin");
			project = javaProject.getProject();
			
			buildDelegate = PackageBuildDelegate.instance();
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

			refJar = PackagesCore.createDetachedPackage(project, true);
			refJar.setName("ref.jar");
			refJar.setPackageType(PackagesCore.getPackageType(JARPackageType.TYPE_ID));
			refJar.setDestinationContainer(project);
			
			libFolder = PackagesCore.createDetachedFolder(project);
			libFolder.setName("lib");
			refJar.addChild(libFolder);
			
			libFolder.addChild(simpleJar.createReference(false));
			
			PackagesModel.instance().attach(refJar, nullMonitor);
	
			explodedJar = PackagesCore.createDetachedPackage(project, true);
			explodedJar.setName("exploded.jar");
			explodedJar.setExploded(true);
			explodedJar.setPackageType(PackagesCore.getPackageType(JARPackageType.TYPE_ID));
			explodedJar.setDestinationContainer(project);
			
			explodedJarFileset = PackagesCore.createDetachedPackageFileSet(project);
			explodedJarFileset.setIncludesPattern("*.xml");
			explodedJarFileset.setSourceProject(project);
			
			explodedJar.addChild(explodedJarFileset);
			
			PackagesModel.instance().attach(explodedJar, nullMonitor);
			
			initialized = true;
		} else{
			
			project = ResourcesPlugin.getWorkspace().getRoot().getProject("buildProject");
			javaProject = JavaCore.create(project);
			
			buildDelegate = PackageBuildDelegate.instance();
			testXmlFile = project.getFile("test.xml");
			
			List packages = PackagesModel.instance().getProjectPackages(project);
			simpleJar = (IPackage) packages.get(0);
			simpleJarFileset = simpleJar.getFileSets()[0];
			
			refJar = (IPackage) packages.get(1);
			libFolder = refJar.getFolders()[0];
			
			explodedJar = (IPackage) packages.get(2);
			explodedJarFileset = explodedJar.getFileSets()[0];
		}
	}
	
	protected void setContents(IFile file, String contents)
	{
		ByteArrayInputStream stream = new ByteArrayInputStream(contents.getBytes());
		
		try {
			if (file.exists())
			{
				file.setContents(stream, false, false, nullMonitor);
			} else {
				ResourceUtil.safeCreateFile(file, stream, false, nullMonitor);
			}

			stream.close();
		} catch (CoreException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}
	
	protected void assertTestXmlContents (String contents)
	{
		assertFileContents(simpleJar, "test.xml", contents);
	}
	
	protected File getPackageFile (IPackage pkg)
	{
		return new File(pkg.getPackageFile().getRawLocation().toFile());
	}
	
	protected File findFile (File jarFile, String name)
	{
		File subFiles[] = (File [])  jarFile.listFiles();
		if (subFiles == null || subFiles.length == 0)
			return null;
		
		File file = null;
		for (int i = 0; i < subFiles.length; i++)
		{
			if (subFiles[i].getName().equals(name)) {
				file = subFiles[i]; break;
			}
		}
		
		return file;
	}
	
	protected void assertFileContents (File file, String contents)
	{
		assertNotNull(file);
		assertEquals(file.length(), contents.length());
		
		byte bytes[] = new byte[0];
		try {
			FileInputStream stream = new FileInputStream(file);
			bytes = new byte[(int)file.length()];
			stream.read(bytes);
			stream.close();
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
		String fileContents = new String(bytes);
		assertEquals(fileContents, contents);
	}
	
	protected void assertFileContents (IPackage jar, String fileName, String contents)
	{
		File jarFile = getPackageFile(jar);
		assertTrue(jarFile.exists());
		assertTrue(jarFile.isArchive());

		File file = findFile (jarFile, fileName);
		assertFileContents (file, contents);
	}
}
