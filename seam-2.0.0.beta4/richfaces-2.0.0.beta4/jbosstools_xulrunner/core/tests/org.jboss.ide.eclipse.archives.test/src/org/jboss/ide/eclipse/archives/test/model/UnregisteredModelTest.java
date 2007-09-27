package org.jboss.ide.eclipse.archives.test.model;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.ArchiveNodeFactory;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelNode;
import org.jboss.ide.eclipse.archives.test.Activator;

public class UnregisteredModelTest extends TestCase {
	public void testGeneral() {
		IArchive archive = ArchiveNodeFactory.createArchive();
		archive.setName("unregistered.jar");
		archive.setDestinationPath(new Path(Activator.getDefault().getOutputFolder()));
		
		IArchiveFileSet fs = ArchiveNodeFactory.createFileset(); 
		fs.setSourcePath(new Path(Activator.getDefault().getDummyFolder()));
		fs.setIncludesPattern("**");

		Object root = fs.getRoot();
		
		assertFalse(root instanceof IArchiveModelNode);
		assertNotNull(root);
	}
}
