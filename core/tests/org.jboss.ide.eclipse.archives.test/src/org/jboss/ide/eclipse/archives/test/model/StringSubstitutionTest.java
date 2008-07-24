package org.jboss.ide.eclipse.archives.test.model;

import junit.framework.TestCase;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.other.internal.WorkspaceVFS;

public class StringSubstitutionTest extends TestCase {

	private static final String ONE = "/this/is/a/test";
	private static final String TWO = "${nonexistant}/this/is/a/test";

	public void testNoVariables() {
		WorkspaceVFS vfs = (WorkspaceVFS)ArchivesCore.getInstance().getVFS();
		try {
			String out1 = vfs.performStringSubstitution(ONE, true);
			assertEquals(ONE, out1);
		} catch( CoreException ce ) {
			fail();
		}
	}

	public void testVariableNotSet() {
		WorkspaceVFS vfs = (WorkspaceVFS)ArchivesCore.getInstance().getVFS();
		try {
			vfs.performStringSubstitution(TWO, true);
		} catch( CoreException ce ) {
			return;
		}
		fail();
	}

	public void testVariableNotSet2() {
		WorkspaceVFS vfs = (WorkspaceVFS)ArchivesCore.getInstance().getVFS();
		try {
			String out2 = vfs.performStringSubstitution(TWO, false);
			assertEquals(TWO, out2);
		} catch( CoreException ce ) {
			fail();
		}
	}

	public void testSetVariable() {
		try {
			ResourcesPlugin.getWorkspace().getPathVariableManager().setValue("test_variable", new Path("/here"));
			WorkspaceVFS vfs = (WorkspaceVFS)ArchivesCore.getInstance().getVFS();
			String out = vfs.performStringSubstitution("${test_variable}", true);
			assertEquals("/here", out);
			ResourcesPlugin.getWorkspace().getPathVariableManager().setValue("test_variable", null);
		} catch( CoreException ce ) {
			fail();
		}
	}
}
