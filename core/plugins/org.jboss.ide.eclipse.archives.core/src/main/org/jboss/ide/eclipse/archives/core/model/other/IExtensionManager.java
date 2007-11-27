package org.jboss.ide.eclipse.archives.core.model.other;

import org.jboss.ide.eclipse.archives.core.model.IArchiveType;


public interface IExtensionManager {
	public IArchiveType[] getArchiveTypes();
	public IArchiveType   getArchiveType(String id);
}
