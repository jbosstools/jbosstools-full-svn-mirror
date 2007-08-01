package org.jboss.ide.eclipse.archives.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.archives.test.util.FileIOUtil;
import org.osgi.framework.Bundle;

public class MarshallUnmarshallTest extends TestCase {
	private Bundle bundle;
	private IPath bundlePath;
	private IPath archiveDescriptors;
	protected void setUp() {
		if( bundlePath == null ) {
			try {
				bundle = ArchivesTest.getDefault().getBundle();
				URL bundleURL = FileLocator.toFileURL(bundle.getEntry(""));
				bundlePath = new Path(bundleURL.getFile());
				archiveDescriptors = bundlePath.append("inputs").append("archiveDescriptors");
			} catch( IOException ioe) {}
		}
	}

	
	public void tearDown() {
		FileIOUtil.clearFolder(bundlePath.append("tmp").toFile().listFiles());
	}
	
	public void testUnmarshall() {
		// unmarshall from file
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(archiveDescriptors.append("descriptor1.xml").toFile());
		} catch( Exception e ) {
			fail(e.getMessage());
		}
		XbPackages packs = XMLBinding.unmarshal(fis, new NullProgressMonitor());
		assertDescriptor1Accurate(packs);
		
		// unmarshall from string
		try {
			String content = fileAsString(archiveDescriptors.append("descriptor1.xml").toFile());
			packs = XMLBinding.unmarshal(content, new NullProgressMonitor());
			assertDescriptor1Accurate(packs);
		} catch( Exception e ) {
			fail(e.getMessage());
		}

	}
	
	public String fileAsString(File f) throws Exception {
		FileInputStream fis = new FileInputStream(f);
		int x= fis.available();
		byte b[]= new byte[x];
		fis.read(b);
		return new String(b);
	}
	
	public void testMarshall() {
		IPath tmpFolder = bundlePath.append("tmp");
		
			XbPackages packs = new XbPackages();
			XbPackage pack1 = new XbPackage();
			pack1.setName("TestProject.jar");
			pack1.setPackageType("jar");
			pack1.setToDir("/some/external/path");
			pack1.setExploded(false);
			pack1.setInWorkspace(false);
			packs.addChild(pack1);

			XbPackage pack2 = new XbPackage();
			pack2.setName("TestProject2.jar");
			pack2.setPackageType("jar");
			pack2.setToDir("/SomeProject");
			pack2.setExploded(true);
			pack2.setInWorkspace(true);
			packs.addChild(pack2);

			XbFolder folder1 = new XbFolder();
			folder1.setName("folder");
			pack1.addChild(folder1);
			
			XbFolder folder2 = new XbFolder();
			folder2.setName("folder2");
			pack1.addChild(folder2);
			
			XbFolder inner1 = new XbFolder();
			inner1.setName("inner1");
			folder2.addChild(inner1);
			
			XbFileSet fs = new XbFileSet();
			fs.setDir("/some/global/path");
			fs.setIncludes("**/*.xml");
			fs.setInWorkspace(false);
			inner1.addChild(fs);
			
			assertDescriptor1Accurate(packs);

			String packsAsString = XMLBinding.serializePackages(packs, new NullProgressMonitor());
			XbPackages packsFromString = XMLBinding.unmarshal(packsAsString, new NullProgressMonitor());
			assertDescriptor1Accurate(packsFromString);

			try {
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tmpFolder.append("marshallTest.xml").toFile()));
				XMLBinding.marshall(packs, writer, new NullProgressMonitor());
				writer.close();
				
				XbPackages packsFromFile = XMLBinding.unmarshal(new FileInputStream(tmpFolder.append("marshallTest.xml").toFile()), new NullProgressMonitor());
				assertDescriptor1Accurate(packsFromFile);
			} catch(Exception e) {
				fail(e.getMessage());
			}

	}
	
	void assertDescriptor1Accurate(XbPackages packs) {
		assertEquals(2, packs.getAllChildren().size());
		assertEquals(2, packs.getChildren(XbPackage.class).size());
		
		XbPackage pack1 = (XbPackage)packs.getAllChildren().get(0);
		XbPackage pack2 = (XbPackage)packs.getAllChildren().get(1);
		
		// pack 1
		assertEquals("TestProject.jar", pack1.getName());
		assertEquals("/some/external/path", pack1.getToDir());
		assertEquals("jar", pack1.getPackageType());
		assertFalse(pack1.isExploded());
		assertFalse(pack1.isInWorkspace());
		assertEquals(2, pack1.getAllChildren().size());
		assertEquals(2, pack1.getChildren(XbFolder.class).size());
		
		XbFolder folder1 = (XbFolder)pack1.getAllChildren().get(0);
		XbFolder folder2 = (XbFolder)pack1.getAllChildren().get(1);
		
		assertEquals(0, folder1.getAllChildren().size());
		assertEquals("folder", folder1.getName());
		
		assertEquals(1, folder2.getAllChildren().size());
		assertEquals(1, folder2.getChildren(XbFolder.class).size());
		assertEquals("folder2", folder2.getName());
		
		XbFolder innerFolder = (XbFolder)folder2.getAllChildren().get(0);
		assertEquals("inner1", innerFolder.getName());
		assertEquals(1, innerFolder.getAllChildren().size());
		assertEquals(1, innerFolder.getChildren(XbFileSet.class).size());
		
		XbFileSet fs = (XbFileSet)innerFolder.getAllChildren().get(0);
		assertEquals(fs.getIncludes(), "**/*.xml");
		assertEquals(fs.getDir(), "/some/global/path");
		assertFalse(fs.isInWorkspace());

		// pack 2
		assertEquals(0, pack2.getAllChildren().size());
		assertEquals("TestProject2.jar", pack2.getName());
		assertEquals("/SomeProject", pack2.getToDir());
		assertEquals("jar", pack2.getPackageType());
		assertTrue(pack2.isExploded());
		assertTrue(pack2.isInWorkspace());
	}
}
