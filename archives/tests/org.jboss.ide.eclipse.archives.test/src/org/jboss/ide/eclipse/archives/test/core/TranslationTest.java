package org.jboss.ide.eclipse.archives.test.core;

import junit.framework.TestCase;

import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.ArchivesCoreMessages;
import org.jboss.ide.eclipse.archives.core.WorkspaceArchivesCore;
import org.jboss.ide.eclipse.archives.core.ant.AntArchivesCore;

public class TranslationTest extends TestCase {
	private static WorkspaceArchivesCore workspaceCore;
	private static AntArchivesCore antCore;
	
	public void testArchivesCore() {
		ArchivesCore core = ArchivesCore.getInstance();
		assertEquals(ArchivesCore.WORKSPACE, core.getRunType());
		assertEquals(WorkspaceArchivesCore.class, core.getClass());
		workspaceCore = (WorkspaceArchivesCore)core;
		
		assertNotNull(ArchivesCoreMessages.RefreshProjectFailed);
		String workspaceBind = ArchivesCore.bind(ArchivesCoreMessages.RefreshProjectFailed, "Test");
		assertNotNull(workspaceBind);

		AntArchivesCore core2 = new AntArchivesCore();
		ArchivesCore.setInstance(core2);
		assertEquals(ArchivesCore.STANDALONE, ArchivesCore.getInstance().getRunType());
		antCore = core2;

		String antBind = ArchivesCore.bind(ArchivesCoreMessages.RefreshProjectFailed, "Test");
		assertNotNull(antBind);
		assertEquals(workspaceBind, antBind);
		ArchivesCore.setInstance(core);
		
	}

	public void testAntStartup() {
		// TODO Test to make sure a launch config starting with ant can also bind such strings
		// SEE JMX CODE for example
	}
}
