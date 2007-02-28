package org.jboss.ide.eclipse.packages.test.build;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.project.build.BuildFileOperations;
import org.jboss.ide.eclipse.packages.core.project.build.TruezipUtil;

import de.schlichtherle.io.File;

public class FileOpsTest extends BuildTest {

	private BuildFileOperations ops;
	
	public FileOpsTest (String testName)
	{
		super(testName);
	}
	
	public static Test suite ()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new FileOpsTest("testRemoveFile"));
		suite.addTest(new FileOpsTest("testUpdateFile_noStamps"));
		suite.addTest(new FileOpsTest("testRemovePackageRef"));
		suite.addTest(new FileOpsTest("testRemoveFolder"));
		
		return suite;
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		this.ops = buildDelegate.getFileOperations();
	}
	
	public void testRemoveFile ()
	{
		ops.removeFileFromFilesets(testXmlFile, new IPackageFileSet[] { simpleJarFileset });
		TruezipUtil.umount(simpleJar);
		
		assertNull(findFile(simpleJarFile, "test.xml"));
		
		File libFile = findFile(refJarFile, "lib");
		File simpleJarInRefJar = findFile(libFile, "simple.jar");
		
		assertNull(findFile(simpleJarInRefJar, "test.xml"));
	}
	
	public void testUpdateFile_noStamps ()
	{
		ops.updateFileInFilesets(testXmlFile, new IPackageFileSet[] { simpleJarFileset }, false);
		
		assertNotNull(findFile(simpleJarFile, "test.xml"));
		File libFile = findFile(refJarFile, "lib");
		File simpleJarInRefJar = findFile(libFile, "simple.jar");
		
		assertNotNull(findFile(simpleJarInRefJar, "test.xml"));
	}

	public void testRemovePackageRef ()
	{
		ops.removePackageRef(simpleJarRef);
		File libFile = findFile(refJarFile, "lib");
		File simpleJarInRefJar = findFile(libFile, "simple.jar");
		
		assertNull(simpleJarInRefJar);
	}
	
	public void testRemoveFolder ()
	{
		ops.removeFolder(libFolder);
		
		assertNull(findFile(refJarFile, "lib"));
	}
}
