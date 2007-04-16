package org.jboss.ide.eclipse.packages.core.model.events;

import org.jboss.ide.eclipse.packages.core.model.IPackagesBuildListener;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelDelta;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelListener;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;

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
	
	public static void fireDelta(IPackagesModelDelta delta) {
		IPackagesModelListener[] listeners = PackagesCore.getInstance().getModelListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].modelChanged(delta);
	}
}
