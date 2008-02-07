package org.jboss.ide.eclipse.archives.core.ant;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveType;
import org.jboss.ide.eclipse.archives.core.model.IExtensionManager;

/**
 * The ant API to extensions, specifically archive types.
 * This is a stub and returns functional but bland implementations.
 * @author rob stryker (rob.stryker@redhat.com)
 *
 */
public class AntExtensionManager implements IExtensionManager {

	public IArchiveType getArchiveType(String id) {
		final String typeId = id;
		
		return new IArchiveType  () {
			public String getId() {
				return typeId;
			}
			public String getLabel() {
				return typeId;
			}
			public IArchive createDefaultConfiguration(String projectName, IProgressMonitor monitor) {
				// TODO Auto-generated method stub
				return null;
			}
			public IArchive fillDefaultConfiguration(String projectName, IArchive topLevel, IProgressMonitor monitor) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	public IArchiveType[] getArchiveTypes() {
		return new IArchiveType[0];
	}

}
