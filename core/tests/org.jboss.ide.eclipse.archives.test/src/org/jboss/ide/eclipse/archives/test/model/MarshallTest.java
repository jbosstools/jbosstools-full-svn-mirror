/**
 * JBoss, a Division of Red Hat
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
package org.jboss.ide.eclipse.archives.test.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbAction;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbProperty;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XMLBinding.XbException;
import org.jboss.ide.eclipse.archives.test.ArchivesTest;
import org.jboss.ide.eclipse.archives.test.util.FileIOUtil;
import org.osgi.framework.Bundle;

/**
 * 
 * This class tests marshalling each type of xb object, 
 * expecting the XMLBinding class (and XB itself) to throw 
 * errors if required fields are missing. 
 * 
 * It also verifies correct models are properly marshalled.
 * 
 * @author rob.stryker <rob.stryker@redhat.com>
 * 
 */
public class MarshallTest extends TestCase {
	private Bundle bundle;
	private IPath bundlePath;
	private IPath expectedOutputs;
	private IPath outputs;
	protected void setUp() {
		if( bundlePath == null ) {
			try {
				bundle = ArchivesTest.getDefault().getBundle();
				URL bundleURL = FileLocator.toFileURL(bundle.getEntry(""));
				bundlePath = new Path(bundleURL.getFile());
				expectedOutputs = bundlePath.append("expectedOutputs");
				outputs = bundlePath.append("output");
			} catch( IOException ioe) {
				fail("Failed to set up " + getClass().getName());
			}
		}
	}

	public void tearDown() {
		FileIOUtil.clearFolder(outputs.toFile().listFiles());
	}
		
	public void testStringWriter() {
		XbPackages packs = new XbPackages();
		try {
			File expected = expectedOutputs.append("emptyPackages.xml").toFile();
			String expectedContents = FileIOUtil.getFileContents(expected);
			String asString = XMLBinding.serializePackages(packs, new NullProgressMonitor());
			assertEquals(asString, expectedContents);
		} catch( XbException xbe ) {
			fail(xbe.getMessage());
		}
	}
	
	public void testFileWriter() {
		XbPackages packs = new XbPackages();
		IPath out = outputs.append("test.xml");
		try {
			File expected = expectedOutputs.append("emptyPackages.xml").toFile();
			String expectedContents = FileIOUtil.getFileContents(expected);

			XMLBinding.marshallToFile(packs, out, new NullProgressMonitor());
			String actualContents = FileIOUtil.getFileContents(out.toFile());
			
			assertEquals(expectedContents, actualContents);
		} catch( XbException xbe ) {
			xbe.printStackTrace();
			fail(xbe.getMessage());
		} catch( IOException ioe ) {
			fail(ioe.getMessage());
		}
	}
	
	protected void write(XbPackages packs, boolean shouldPass) {
		XbException e = null;
		try {
			IPath out = outputs.append("test.xml");
			XMLBinding.marshallToFile(packs, out, new NullProgressMonitor());
		} catch( IOException ioe ) {
			fail("IOException during testWritePackage operation");
		}catch( XbException xbe ) {
			e = xbe;
		} finally {
			if( e == null && !shouldPass) {
				fail("Incomplete Model saved when it should not have been.");
			}
			if( e != null && shouldPass) {
				fail("Model failed to save when it should have saved. " + e.getMessage());
			}
		}
	}
	
	protected void writePackage(String name, String toDir, boolean shouldPass) {
		XbException e = null;
		XbPackages packs = new XbPackages();
		XbPackage pack = new XbPackage();
		pack.setName(name);
		pack.setToDir(toDir);
		packs.addChild(pack);
		write(packs, shouldPass);
	}
	
	public void testWritePackageSuccess() {
		writePackage("someName", "someFile.jar", true);
	}
	public void testWritePackageMissingName() {
		writePackage(null, "someFile.jar", false);
	}

	public void testWritePackageMissingDir() {
		writePackage("someName", null, false);
	}

	protected void writeProperties(String name, String value, boolean shouldPass) {
		XbPackages packs = new XbPackages();
		XbPackage pack = new XbPackage();
		pack.setName("test");
		pack.setToDir("test2");
		XbProperty property = new XbProperty();

		try {
			property.setName(name);
			property.setValue(value);
			pack.getProperties().addProperty(property);
		} catch( NullPointerException npe ) {
			if( shouldPass ) 
				fail("Model failed to save when it should have saved. - " + npe.getMessage());
			return; // success
		}
		
		packs.addChild(pack);
		write(packs, shouldPass);
	}

	public void testWritePropertiesSuccess() {
		writeProperties("name", "val", true); 
	}
	
	public void testWritePropertiesMissingName() {
		writeProperties(null, "val", false);
	}
	
	public void testWritePropertiesMissingValue() {
		writeProperties("name", null, false);
	}
	
	public void writeFolder(String name, boolean shouldPass) {
		XbPackages packs = new XbPackages();
		XbPackage pack = new XbPackage();
		pack.setName("name");
		pack.setToDir("todir");
		packs.addChild(pack);
		XbFolder folder = new XbFolder();
		folder.setName(name);
		pack.addChild(folder);
		write(packs, shouldPass);
	}
	
	public void testWriteFolderSuccess() {
		writeFolder("someFolder", true);
	}
		
	public void testWriteFolderMissingName() {
		writeFolder(null, false);
	}
		
	public void writeFileset(String dir, String includes, boolean shouldSucceed) {
		XbPackages packs = new XbPackages();
		XbPackage pack = new XbPackage();
		pack.setName("name");
		pack.setToDir("todir");
		packs.addChild(pack);
		XbFileSet fs = new XbFileSet();
		fs.setDir(dir);
		fs.setIncludes(includes);
		pack.addChild(fs);
		write(packs, shouldSucceed);
	}
	
	public void testWriteFilesetSuccess() {
		writeFileset("folder", "includes", true);
	}
	
	public void testWriteFilesetMissingFolder() {
		writeFileset(null, "includes", false);
	}
	
	public void testWriteFilesetMissingIncludes() {
		writeFileset("path", null, false);
	}
	
	
	protected void writeAction(String time, String type, boolean shouldSucceed) {
		XbPackages packs = new XbPackages();
		XbPackage pack = new XbPackage();
		pack.setName("name");
		pack.setToDir("todir");
		packs.addChild(pack);
		XbAction act = new XbAction();
		act.setTime(time);
		act.setType(type);
		pack.addChild(act);
		write(packs, shouldSucceed);
	}
	
	public void testWriteActionSuccess() {
		writeAction("preBuild", "ant", true);
	}
	
	public void testWriteActionMissingTime() {
		writeAction(null, "ant", false);
	}
	
	public void testWriteActionMissingType() {
		writeAction("preBuild", null, false);
	}
}
