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
package org.jboss.ide.eclipse.packages.test;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.ide.eclipse.core.test.util.JavaProjectHelper;
import org.jboss.ide.eclipse.core.test.util.TestFileUtil;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageReference;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageReferenceImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.packages.core.model.types.IPackageType;
import org.jboss.ide.eclipse.packages.core.model.types.JARPackageType;

import de.schlichtherle.io.File;

public class PackagesEARTest extends TestCase {

	private IJavaProject testPackagesProject;
	private XbPackages packagesElement;
	private String testPackagesProjectRoot;
	private static boolean initialized = false;
	
	public PackagesEARTest (String testName)
	{
		super(testName);
	}
	
	public static Test suite ()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new PackagesEARTest("testCorrectBinding"));
		suite.addTest(new PackagesEARTest("testModel"));
		suite.addTest(new PackagesEARTest("testSave"));
		suite.addTest(new PackagesEARTest("testBuild"));
		suite.addTest(new PackagesEARTest("testJARDefaultConfig"));
		suite.addTest(new PackagesEARTest("testPathAppend"));
		suite.addTest(new PackagesEARTest("testPackageReference"));
		suite.addTest(new PackagesEARTest("testProperties"));
		return suite;
	}
	
	protected void setUp() throws Exception {
		if (!initialized)
		{
			testPackagesProject = JavaProjectHelper.createJavaProject(
				"testPackagesProject", new String[] { "/src" }, "/bin");
			
			testPackagesProjectRoot = PackagesTestPlugin.getDefault().getBaseDir();
			testPackagesProjectRoot += File.separator + "testPackagesProject";
		      
		   TestFileUtil.copyDirectory (new File(testPackagesProjectRoot), testPackagesProject.getProject().getLocation().toFile(), true);
		   testPackagesProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		   
		} else {
			
			IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("testPackagesProject");
			testPackagesProject = JavaCore.create(project);
		}

	   packagesElement = XMLBinding.unmarshal(
			testPackagesProject.getProject().getFile(PackagesModel.PROJECT_PACKAGES_FILE).getContents(), new NullProgressMonitor());
	}
	
	public void testCorrectBinding ()
	{
		assertNotNull(packagesElement);
		
		List packages = packagesElement.getPackages();
		assertNotNull(packages);
		assertEquals(packages.size(), 3);
		
		Properties properties = packagesElement.getProperties().getProperties();
		assertNotNull(properties);
		assertEquals(properties.get("test-property"), "test-value");
		
		assertTrue(packages.get(0) instanceof XbPackage);
		XbPackage pkg = (XbPackage) packages.get(0);
		
		assertEquals(pkg.getName(), "MyApp.ear");
		assertEquals(pkg.getToDir(), ".");
		assertEquals(pkg.getPackageType(), "j2ee.ear");
		assertNotNull(pkg.getFolders());
		assertNull(pkg.getPackages());
		assertNull(pkg.getFileSets());
		assertEquals(pkg.getFolders().size(), 2);
		
		assertTrue(pkg.getFolders().get(0) instanceof XbFolder);
		assertTrue(pkg.getFolders().get(1) instanceof XbFolder);
		XbFolder pkgsFolder = (XbFolder) pkg.getFolders().get(0);
		XbFolder metaInfFolder = (XbFolder) pkg.getFolders().get(1);
		
		assertEquals(pkgsFolder.getName(), "packages");
		assertEquals(metaInfFolder.getName(), "META-INF");
		assertEquals(pkgsFolder.getPackages().size(), 2);
		assertEquals(metaInfFolder.getFileSets().size(), 1);
		
		assertTrue(pkgsFolder.getPackages().get(0) instanceof XbPackage);
		assertTrue(pkgsFolder.getPackages().get(1) instanceof XbPackage);
		assertTrue(metaInfFolder.getFileSets().get(0) instanceof XbFileSet);
		
		XbPackage ejbJar = (XbPackage) pkgsFolder.getPackages().get(0);
		XbPackage war = (XbPackage) pkgsFolder.getPackages().get(1);
		XbFileSet metaInfFiles = (XbFileSet) metaInfFolder.getFileSets().get(0);
		
		assertEquals(ejbJar.getName(), "MyEJBs.jar");
		assertFalse(ejbJar.isExploded());
		assertEquals(ejbJar.getPackageType(), "j2ee.ejbjar");
		assertNull(ejbJar.getRef(), null);
		assertNull(ejbJar.getToDir());
		assertEquals(metaInfFiles.getFile(), "descriptors/application.xml");
		assertTrue(metaInfFiles.isInWorkspace());
		
		assertEquals(ejbJar.getFileSets().size(), 1);
		assertEquals(ejbJar.getFolders().size(), 1);
		assertTrue(ejbJar.getFileSets().get(0) instanceof XbFileSet);
		assertTrue(ejbJar.getFolders().get(0) instanceof XbFolder);
		
		XbFileSet ejbJarFileset = (XbFileSet) ejbJar.getFileSets().get(0);
		XbFolder ejbJarMetaInf = (XbFolder) ejbJar.getFolders().get(0);
		
		assertEquals(ejbJarFileset.getDir(), "bin");
		assertEquals(ejbJarFileset.getIncludes(), "com/myapp/ejb/**/*.class");
		assertNull(ejbJarFileset.getExcludes());
		assertNull(ejbJarFileset.getFile());
		assertNull(ejbJarFileset.getToFile());
		
		assertEquals(ejbJarMetaInf.getName(), "META-INF");
		assertEquals(ejbJarMetaInf.getFileSets().size(), 2);
		assertTrue(ejbJarMetaInf.getFileSets().get(0) instanceof XbFileSet);
		assertTrue(ejbJarMetaInf.getFileSets().get(1) instanceof XbFileSet);
		XbFileSet metaInfFileSet1 = (XbFileSet) ejbJarMetaInf.getFileSets().get(0);
		XbFileSet metaInfFileSet2 = (XbFileSet) ejbJarMetaInf.getFileSets().get(1);
		
		assertEquals(metaInfFileSet1.getFile(), "descriptors/jboss.xml");
		assertEquals(metaInfFileSet2.getFile(), "descriptors/ejb-jar.xml");
		assertNull(metaInfFileSet1.getDir());
		assertNull(metaInfFileSet1.getIncludes());
		assertNull(metaInfFileSet1.getExcludes());
		assertNull(metaInfFileSet1.getToFile());
		assertNull(metaInfFileSet2.getDir());
		assertNull(metaInfFileSet2.getIncludes());
		assertNull(metaInfFileSet2.getExcludes());
		assertNull(metaInfFileSet2.getToFile());
		
		assertEquals(war.getName(), "MyApp.war");
		assertEquals(war.getPackageType(), "j2ee.war");
		assertFalse(war.isExploded());
		assertEquals(war.getToDir(), ".");
		assertNotNull(war.getFolders());
		assertEquals(war.getFolders().size(), 1);
		assertTrue(war.getFolders().get(0) instanceof XbFolder);
		
		XbFolder webInf = (XbFolder) war.getFolders().get(0);
		assertEquals(webInf.getName(), "WEB-INF");
		assertNotNull(webInf.getFolders());
		assertEquals(webInf.getFolders().size(), 2);
		assertNotNull(webInf.getFileSets());
		assertEquals(webInf.getFileSets().size(), 1);
		assertTrue(webInf.getFolders().get(0) instanceof XbFolder);
		assertTrue(webInf.getFolders().get(1) instanceof XbFolder);
		assertTrue(webInf.getFileSets().get(0) instanceof XbFileSet);
		
		XbFolder libFolder = (XbFolder) webInf.getFolders().get(0);
		XbFolder classesFolder = (XbFolder) webInf.getFolders().get(1);
		XbFileSet descriptors = (XbFileSet) webInf.getFileSets().get(0);
		
		assertEquals(libFolder.getName(), "lib");
		assertEquals(classesFolder.getName(), "classes");
		assertEquals(descriptors.getDir(), "descriptors");
		assertEquals(descriptors.getIncludes(), "*web.xml");
		assertNotNull(libFolder.getFileSets());
		assertEquals(libFolder.getFileSets().size(), 1);
		assertNotNull(classesFolder.getFileSets());
		assertEquals(classesFolder.getFileSets().size(), 1);
		
		assertTrue(libFolder.getFileSets().get(0) instanceof XbFileSet);
		assertTrue(classesFolder.getFileSets().get(0) instanceof XbFileSet);
		XbFileSet libFileset = (XbFileSet) libFolder.getFileSets().get(0);
		XbFileSet classesFileset = (XbFileSet) classesFolder.getFileSets().get(0);
		
		assertEquals(libFileset.getDir(), "lib");
		assertEquals(libFileset.getIncludes(), "**/*.jar");
		assertEquals(classesFileset.getDir(), "bin");
		assertEquals(classesFileset.getIncludes(), "com/myapp/web/**/*.class");
	}

	private void assertFileset (IPackageFileSet fileset, String srcPath, String[] filePaths, String includes, String excludes)
	{
		assertEquals(fileset.getSourceContainer(), testPackagesProject.getProject().getFolder(srcPath));
		
		if (includes != null)
			assertEquals(fileset.getIncludesPattern(), includes);
		if (excludes != null)
			assertEquals(fileset.getExcludesPattern(), excludes);
		
		IFile matchingFiles[] = fileset.findMatchingFiles();
		assertNotNull(matchingFiles);
		assertEquals(matchingFiles.length, 2);
		List matchingFileList = Arrays.asList(matchingFiles);
		
		for (int i = 0; i < filePaths.length; i++)
		{
			IFile file = testPackagesProject.getProject().getFile(filePaths[i]);
			assertTrue(matchingFileList.contains(file));
			assertTrue(fileset.matchesFile(file));	
		}
	}
	
	public void testModel ()
	{
		List packages = PackagesModel.instance().getProjectPackages(testPackagesProject.getProject());
		if (packages == null) {
			PackagesModel.instance().registerProject(testPackagesProject.getProject(), new NullProgressMonitor());
			packages = PackagesModel.instance().getProjectPackages(testPackagesProject.getProject());
		}
		
		assertNotNull(packages);
		assertEquals(packages.size(), 3);
		
		IPackage earPackage = (IPackage) packages.get(0);
		assertEquals(earPackage.getProject(), testPackagesProject.getProject());
		assertEquals(earPackage.getName(), "MyApp.ear");
		assertEquals(earPackage.getNodeType(), IPackageNode.TYPE_PACKAGE);
//		assertEquals(earPackage.getPackageType(), "j2ee.ear");
		
		IPackageFolder earPackageFolders[] = earPackage.getFolders();
		assertNotNull(earPackageFolders);
		assertEquals(earPackageFolders.length, 2);
		
		IPackageFolder pkgsFolder = earPackageFolders[0];
		assertEquals(pkgsFolder.getName(), "packages");
		
		IPackage earPackages[] = pkgsFolder.getPackages();
		assertNotNull(earPackages);
		assertEquals(earPackages.length, 2);
		
		IPackage ejbJarPackage = earPackages[0];
		IPackage warPackage = earPackages[1];
		assertEquals(ejbJarPackage.getProject(), testPackagesProject.getProject());
		assertEquals(ejbJarPackage.getName(), "MyEJBs.jar");
		assertEquals(ejbJarPackage.getNodeType(), IPackageNode.TYPE_PACKAGE);
//		assertEquals(ejbJarPackage.getPackageType(), "j2ee.ejbjar");
		
		IPackageFileSet ejbJarFilesets[] = ejbJarPackage.getFileSets();
		IPackageFolder ejbJarFolders[] = ejbJarPackage.getFolders();
		assertNotNull(ejbJarFilesets);
		assertNotNull(ejbJarFolders);
		assertEquals(ejbJarFilesets.length, 1);
		assertEquals(ejbJarFolders.length, 1);
		
		assertFileset(ejbJarFilesets[0], "bin",
			new String[] { "bin/com/myapp/ejb/TestClass1.class","bin/com/myapp/ejb/subpackage/TestClass2.class"},
			"com/myapp/ejb/**/*.class", null);
		
		IPackageFolder metaInf = ejbJarFolders[0];
		assertEquals(metaInf.getName(), "META-INF");
		IPackageFileSet metaInfFilesets[] = metaInf.getFileSets();
		assertNotNull(metaInfFilesets);
		assertEquals(metaInfFilesets.length, 2);
		
		assertEquals(warPackage.getProject(), testPackagesProject.getProject());
		assertEquals(warPackage.getName(), "MyApp.war");
		assertEquals(warPackage.getNodeType(), IPackageNode.TYPE_PACKAGE);
//		assertEquals(warPackage.getPackageType(), "j2ee.war");
		
		IPackageFolder[] warFolders = warPackage.getFolders();
		assertNotNull(warFolders);
		assertEquals(warFolders.length, 1);
		
		IPackageFolder webInf = warFolders[0];
		assertEquals(webInf.getName(), "WEB-INF");
		IPackageFolder[] webInfFolders = webInf.getFolders();
		IPackageFileSet[] webInfFilesets = webInf.getFileSets();
		assertNotNull(webInfFolders);
		assertNotNull(webInfFilesets);
		assertEquals(webInfFolders.length, 2);
		assertEquals(webInfFilesets.length, 1);
		
		assertFileset(webInfFilesets[0], "descriptors", new String[] { "descriptors/web.xml","descriptors/jboss-web.xml"}, "*web.xml", null);
	}
	
	public void testSave()
	{	
		StringWriter writer = new StringWriter();
		XMLBinding.marshal(packagesElement, writer, new NullProgressMonitor());
		
		System.out.println(writer.getBuffer().toString());
		assertTrue(writer.getBuffer().length() > 0);
	}
	
	public void testBuild ()
	{
		NullProgressMonitor nullMonitor = new NullProgressMonitor();
		List packages = PackagesModel.instance().getProjectPackages(testPackagesProject.getProject());
		assertNotNull(packages);
		assertTrue(packages.size() > 0);
		
		IPackage pkg = (IPackage) packages.get(0);
		assertNotNull(pkg);
		
		PackagesCore.buildPackage(pkg, nullMonitor);
		IFile packageFile = pkg.getPackageFile();
		
		assertTrue(packageFile.exists());
		assertEquals(packageFile.getName(), "MyApp.ear");
		assertEquals(packageFile.getParent(), pkg.getDestinationContainer());
		
		File packageZipFile = new File(packageFile.getRawLocation().toString());
		assertTrue(packageZipFile.exists());
		
		File[] children = (File[]) packageZipFile.listFiles();
		assertEquals(children.length, 2);
		File metaInfFolder = null, packagesFolder = null;
		
		for (int i = 0; i < 2; i++)
		{
			if (children[i].getName().equals("META-INF"))
				metaInfFolder = children[i];
			else if (children[i].getName().equals("packages"))
				packagesFolder = children[i];
		}
		
		assertNotNull(metaInfFolder);
		assertNotNull(packagesFolder);
		
		children = (File[]) metaInfFolder.listFiles();
		assertEquals(children.length, 1);
		
		File applicationXml = children[0];
		assertEquals(applicationXml.getName(), "application.xml");
		
		children = (File []) packagesFolder.listFiles();
		assertEquals(children.length, 2);
		
	}
	
	public void testJARDefaultConfig ()
	{
		NullProgressMonitor nullMonitor = new NullProgressMonitor();
		IPackageType jarPackageType = PackagesCore.getPackageType(JARPackageType.TYPE_ID);
		
		assertTrue(jarPackageType instanceof JARPackageType);
		
		IPackage jar = jarPackageType.createDefaultConfiguration(testPackagesProject.getProject(), nullMonitor);
		
		assertEquals(jar.getName(), "testPackagesProject.jar");
		assertEquals(jar.getPackageType(), jarPackageType);
		IPackageFileSet filesets[] = jar.getFileSets();
		
		assertEquals(filesets.length, 1);
		IPackageFileSet classes = filesets[0];
		
		assertEquals(classes.getIncludesPattern(), "**/*");
		
//		try {
//			testPackagesProject.getProject().build(IncrementalProjectBuilder.FULL_BUILD, nullMonitor);
//		} catch (CoreException e) {
//			fail(e.getMessage());
//		}
		
		PackagesCore.attach(jar, nullMonitor);
		PackagesCore.buildPackage(jar, nullMonitor);
		
		IFile jarFile = jar.getPackageFile();
		assertTrue(jarFile.exists());
		
	}
	
	public void testPathAppend ()
	{
		String sPath = "Project1/filesystem/C:/Users/Marshall/.vimrc";
		PackageReferenceImpl.RefAttributes attrs = PackageReferenceImpl.getRefAttributes(sPath);
		
		assertEquals(attrs.projectName, "Project1");
		assertEquals(attrs.locationType, PackageReferenceImpl.RefAttributes.FILESYSTEM);
		assertEquals(attrs.packagePath, new Path("C:/Users/Marshall/.vimrc"));
		System.out.println(attrs.packagePath);
	}
	
	public void testPackageReference ()
	{
		List packages = PackagesModel.instance().getProjectPackages(testPackagesProject.getProject());
		
		assertNotNull(packages);
		assertEquals(packages.size(), 4);
		
		IPackage testRef = (IPackage) packages.get(1);
		IPackage testRef2 = (IPackage) packages.get(2);
		IPackageNode refNodes[] = testRef2.getAllChildren();
		
		assertNotNull(refNodes);
		assertEquals(refNodes.length, 1);
		assertEquals(refNodes[0].getNodeType(), IPackageNode.TYPE_PACKAGE_REFERENCE);
		
		IPackageReference ref = (IPackageReference) refNodes[0];
		assertEquals(ref.getPackage(), testRef);
		assertEquals(ref.getParent(), testRef2);
		
		IPackageReference testRef3 = testRef.createReference(true);
		PackageReferenceImpl refImpl = (PackageReferenceImpl) testRef3;
		assertEquals(refImpl.getDelegate().getRef(), "testPackagesProject/workspace/testPackagesProject/testRef.jar");
	}
	
	public void testProperties ()
	{
		List packages = PackagesModel.instance().getProjectPackages(testPackagesProject.getProject());
		IPackage pkg1 = (IPackage) packages.get(0);
		
		String value = pkg1.getProperty("Yo");
		assertNull(value);
	}
}
