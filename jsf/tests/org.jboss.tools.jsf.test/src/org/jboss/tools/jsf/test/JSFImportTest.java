package org.jboss.tools.jsf.test;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.common.model.XModelFactory;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.jsf.ui.operation.JSFProjectAdoptOperation;
import org.jboss.tools.jsf.web.helpers.context.ImportProjectWizardContext;
import org.jboss.tools.jst.web.context.*;
import org.jboss.tools.test.util.JUnitUtils;
import org.osgi.framework.Bundle;

import junit.framework.TestCase;

public class JSFImportTest extends TestCase {
	File temp = null;
	File projectLocation = null;

	public void setUp() throws Exception {
		init("org.jboss.tools.jsf.test", "/projects", "JSFKickStartOldFormat");
	}
	
	public void testImportWithoutLinks() {
		if(projectLocation == null) return;
		ImportWebDirProjectContext context = new ImportProjectWizardContext(XModelFactory.getDefaultInstance().getRoot());
		
		File webxml = new File(projectLocation, "WebContent/WEB-INF/web.xml");
		context.setWebXmlLocation(webxml.getAbsolutePath());
		context.setProjectName(projectLocation.getName());
		context.setApplicationName(projectLocation.getName());
		//context.setLinkingToProjectOutsideWorkspace(false);
		
		
		JSFProjectAdoptOperation operation = new JSFProjectAdoptOperation(context);
		try {
			operation.run(new NullProgressMonitor());
		} catch (Exception ex) {
			JUnitUtils.fail("Error in import operation", ex);
		}
		
		IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject("JSFKickStartOldFormat");
		assertTrue("Project was not loaded", p != null);
		
		File f = new File(p.getLocation().toFile(), ".project");
		assertTrue("File .project is not found at project location", f.exists());
		
		assertTrue("Eclipse project is not located at imported project location",
			p.getLocation().toFile().equals(projectLocation));

		try {
			p.delete(false, true, new NullProgressMonitor());
		} catch (CoreException e) {
			JUnitUtils.fail("Error in removing project", e);
		}
	
	}

	private void init(String bundleName, String projectPath, String name) throws Exception {
		Bundle bundle = Platform.getBundle(bundleName);
		URL url = null;
		try {
			url = FileLocator.resolve(bundle.getEntry(projectPath));
		} catch (Exception e) {
			throw new Exception("Cannot find project " + name + " in " + bundleName); 
		}
		String location = url.getFile();
		File original = new File(location, name);
		temp = new File(original.getParent(), "ws");
		temp.mkdirs();
		projectLocation = new File(temp, original.getName());
		projectLocation.mkdirs();
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathname) {
				String n = pathname.getName();
				if(n.startsWith(".")) return false;
				return true;
			}
		};
		FileUtil.copyDir(original, projectLocation, true, true, true, filter);
	}
	
	protected void tearDown() throws Exception {
		clean();
	}

	private void clean() {
		if(temp != null && temp.isDirectory()) {
			FileUtil.clear(temp);
			temp.delete();
			temp = null;
		}
	}

}
