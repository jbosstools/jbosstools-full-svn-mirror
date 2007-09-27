package org.jboss.ide.eclipse.archives.test.io;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.archives.test.Activator;

public class MarshallerUnmarshallerTest extends TestCase {
	public void setUp() {
		try {
			XMLBinding.init();
		} catch( Exception e ) {
			fail(e.getLocalizedMessage());
		}
	}
	
	public void tearDown() {
	}
	
	protected String getContents(InputStream is) {
		StringBuffer sb = new StringBuffer();
		DataInputStream dis = new DataInputStream(is);
		try {
			sb.append(dis.readLine());
			while(dis.available() != 0) {
				sb.append("\n" + dis.readLine());
			}
		} catch( Exception e ) {}
		try {
			dis.close();
		} catch( Exception e ) {}
		return sb.toString();
	}
	
	
	public void testUnmarshall() {
		try {
			InputStream is = FileLocator.openStream(Activator.getDefault().getBundle(), new Path("archiveFiles/marshallerUnmarshaller.xml"), false);
			XbPackages packages = XMLBinding.unmarshal(is, new NullProgressMonitor());
			is.close();
			assertNotNull(packages);
			
			assertEquals(1, packages.getAllChildren().size());
			assertEquals(1, packages.getChildren(XbPackage.class).size());
			assertEquals(0, packages.getProperties().getProperties().size());
			
			XbPackage pack = (XbPackage)packages.getChildren(XbPackage.class).get(0);
			assertEquals("pack1", pack.getId());
			assertEquals("newproj.jar", pack.getName());
			assertEquals("jar", pack.getPackageType());
			assertFalse(pack.isInWorkspace());
			assertTrue(pack.isExploded());
			
			assertEquals(2, pack.getAllChildren().size());
			assertEquals(1, pack.getChildren(XbFolder.class).size());
			assertEquals(1, pack.getChildren(XbFileSet.class).size());
			
			XbFileSet fs = (XbFileSet)pack.getChildren(XbFileSet.class).get(0);
			assertEquals("garbageBaseDir", fs.getDir());
			assertEquals("*", fs.getIncludes());
			assertEquals(null, fs.getExcludes());
			assertEquals(false, fs.isInWorkspace());
			
			XbFolder folder = (XbFolder)pack.getChildren(XbFolder.class).get(0);
			assertEquals("abcde", folder.getName());
			
			assertEquals(2, folder.getAllChildren().size());
			assertEquals(2, folder.getChildren(XbPackage.class).size());
			
			XbPackage innerPack = (XbPackage)folder.getChildren(XbPackage.class).get(0);
			XbPackage innerExploded = (XbPackage)folder.getChildren(XbPackage.class).get(1);

			assertEquals("innerPackage.jar", innerPack.getName());
			assertEquals("jar", innerPack.getPackageType());
			assertFalse(innerPack.isExploded());
			assertTrue(innerPack.isInWorkspace());
			assertEquals(1, innerPack.getAllChildren().size());
			assertEquals(1, innerPack.getChildren(XbFileSet.class).size());
			
			XbFileSet innerFS = (XbFileSet)innerPack.getChildren(XbFileSet.class).get(0);
			assertEquals("SomeFileSetDir", innerFS.getDir());
			assertEquals("**", innerFS.getIncludes());
			assertFalse(innerFS.isInWorkspace());
			
			
			
			// inner exploded
			assertEquals("innerExploded.jar", innerExploded.getName());
			assertEquals("jar", innerExploded.getPackageType());
			assertTrue(innerExploded.isExploded());
			assertTrue(innerExploded.isInWorkspace());
			assertEquals(0, innerExploded.getAllChildren().size());
		} catch( IOException ioe ) {
			fail(ioe.getLocalizedMessage());
		}
		
	}

	
	public void testMarshall() {
		try {
			InputStream is = FileLocator.openStream(Activator.getDefault().getBundle(), new Path("archiveFiles/marshallerUnmarshaller.xml"), false);
			String contents = getContents(is);
			is.close();
			
			is = FileLocator.openStream(Activator.getDefault().getBundle(), new Path("archiveFiles/marshallerUnmarshaller.xml"), false);
			XbPackages packages = XMLBinding.unmarshal(is, new NullProgressMonitor());
			assertNotNull(packages);
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(bytes);
			XMLBinding.marshal(packages, writer, new NullProgressMonitor());
			writer.close();
			
			String tmp = new String(bytes.toByteArray());
			assertEquals(contents, tmp);
			
			XbPackage pack = (XbPackage)packages.getPackages().get(0);
			pack.setName("somethingNew.jar");
			bytes = new ByteArrayOutputStream();
			writer = new OutputStreamWriter(bytes);
			XMLBinding.marshal(packages, writer, new NullProgressMonitor());
			writer.close();
			
			String fixed = contents.replace("newproj.jar", "somethingNew.jar");
			assertEquals(fixed, new String(bytes.toByteArray()));
		} catch( IOException ioe ) {
			fail(ioe.getLocalizedMessage());
		}
		
	}

}
