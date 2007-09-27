package org.jboss.ide.eclipse.packages.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.jboss.ide.eclipse.core.test.util.JavaProjectHelper;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbPackages;

public class NewProjectTest extends TestCase {

	private IJavaProject newProject;
	
	public NewProjectTest (String name)
	{
		super(name);
	}
	
	public static Test suite ()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new NewProjectTest("testXbConsistency"));
		suite.addTest(new NewProjectTest("testEclipseModelConsistency"));
		
		return suite;
	}
	
	public void setUp () throws Exception
	{
		newProject = JavaProjectHelper.createJavaProject("newProject", new String[] { "src" }, "/bin");
	}
	
	public void testXbConsistency ()
	{
		XbPackages packages = new XbPackages();
		XbPackage pkg = new XbPackage();
		packages.addChild(pkg);
		
		assertTrue(packages.getAllChildren().contains(pkg));
		
		pkg.setName("test.jar");
		assertEquals(pkg.getName(), "test.jar");
		
		try {
			NullProgressMonitor nullMonitor = new NullProgressMonitor();
			File packagesFile = new File(newProject.getProject().getLocation().toFile(), ".packages");
			FileWriter xmlWriter = new FileWriter(packagesFile);
			XMLBinding.marshal(packages, xmlWriter, nullMonitor);
			xmlWriter.close();
			
			FileInputStream in = new FileInputStream(packagesFile);
			XbPackages packages2 = XMLBinding.unmarshal(in, nullMonitor);
			
			assertNotNull(packages2);
			assertTrue(packages2.hasChildren());
			assertEquals(packages2.getAllChildren().size(), 1);
			assertEquals(packages2.getChildren(XbPackage.class).size(), 1);
			
			XbPackage pkg2 = (XbPackage) packages2.getChildren(XbPackage.class).get(0);
			assertEquals(pkg2.getName(), pkg.getName());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void testEclipseModelConsistency ()
	{
		NullProgressMonitor nullMonitor = new NullProgressMonitor();
		
		testXbConsistency();
		IFile packagesFile = newProject.getProject().getFile(".packages");
		
		try {
			packagesFile.refreshLocal(IResource.DEPTH_ONE, nullMonitor);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PackagesModel.instance().registerProject(newProject.getProject(), nullMonitor);
		List packages = PackagesModel.instance().getProjectPackages(newProject.getProject());
		
		assertNotNull(packages);
		assertEquals(packages.size(), 1);
		assertTrue(packages.get(0) instanceof IPackage);
		
		IPackage pkg = (IPackage) packages.get(0);
		assertEquals(pkg.getName(), "test.jar");
	}
}
