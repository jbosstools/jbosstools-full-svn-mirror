package org.jboss.ide.eclipse.archives.core.model;



public interface IExtensionManager {
	public IArchiveType[] getArchiveTypes();
	public IArchiveType   getArchiveType(String id);
}
