package org.jboss.ide.eclipse.archives.core.model;

import org.jboss.ide.eclipse.archives.core.model.other.IArchiveBuildListener;
import org.jboss.ide.eclipse.archives.core.model.other.IArchiveModelListener;

/**
 * Manages Archive Model Listeners, build and model changes, for changes
 * in the model
 * @author rstryker
 *
 */
public interface IArchiveModelListenerManager {

	public void addBuildListener(IArchiveBuildListener listener);
	public void removeBuildListener(IArchiveBuildListener listener);
	public IArchiveBuildListener[] getBuildListeners();
	public void addModelListener(IArchiveModelListener listener);
	public void removeModelListener(IArchiveModelListener listener);
	public IArchiveModelListener[] getModelListeners();

}
