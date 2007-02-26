package org.jboss.ide.eclipse.packages.test.build;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.project.build.BuildFileOperations;

import de.schlichtherle.io.ArchiveException;
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
		
		return suite;
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		this.ops = buildDelegate.getFileOperations();
	}
	
	public void testRemoveFile ()
	{
		ops.removeFileFromFilesets(testXmlFile, new IPackageFileSet[] { simpleJarFileset });
		File simpleJarFile = getPackageFile(simpleJar);
		
		try {
			File.update(simpleJarFile);
		} catch (ArchiveException e) {
			fail(e.getMessage());
		}
		
		File refJarFile = getPackageFile(refJar);
		
		assertNull(findFile(simpleJarFile, "test.xml"));
		File simpleJarInRefJar = findFile(refJarFile, "simple.jar");
		
		assertNull(findFile(simpleJarInRefJar, "test.xml"));
	}
	
	public void testUpdateFile_noStamps ()
	{
		ops.updateFileInFilesets(testXmlFile, new IPackageFileSet[] { simpleJarFileset }, false);
		
		File simpleJarFile = getPackageFile(simpleJar);
		File refJarFile = getPackageFile(refJar);
		
		assertNotNull(findFile(simpleJarFile, "test.xml"));
		File simpleJarInRefJar = findFile(refJarFile, "simple.jar");
		
		assertNotNull(findFile(simpleJarInRefJar, "test.xml"));
	}

}
