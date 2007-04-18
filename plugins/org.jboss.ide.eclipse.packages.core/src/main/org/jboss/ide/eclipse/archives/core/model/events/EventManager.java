package org.jboss.ide.eclipse.archives.core.model.events;

import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.IPackagesBuildListener;
import org.jboss.ide.eclipse.archives.core.model.PackagesCore;

public class EventManager {
	public static void fireBuildStarted() {
		IPackagesBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].buildStarted();
	}

	public static void fireBuildEnded() {
		IPackagesBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].buildEnded();
	}
	
	public static void fireDelta(IArchiveNodeDelta delta) {
		IArchiveModelListener[] listeners = PackagesCore.getInstance().getModelListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].modelChanged(delta);
	}
}
