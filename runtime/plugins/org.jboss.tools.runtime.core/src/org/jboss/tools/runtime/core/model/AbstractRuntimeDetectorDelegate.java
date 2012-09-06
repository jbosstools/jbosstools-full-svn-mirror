package org.jboss.tools.runtime.core.model;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.tools.runtime.core.RuntimeCoreActivator;
import org.jboss.tools.runtime.core.internal.RuntimeDetector;

public abstract class AbstractRuntimeDetectorDelegate implements
		IRuntimeDetectorDelegate {

	@Override
	public void initializeRuntimes(List<RuntimeDefinition> runtimeDefinitions) {
	}

	@Override
	public RuntimeDefinition getRuntimeDefinition(File root,
			IProgressMonitor monitor) {
		return null;
	}

	@Override
	public void computeIncludedRuntimeDefinition(
			RuntimeDefinition runtimeDefinition) {
	}

	@Override
	public String getVersion(RuntimeDefinition runtimeDefinition) {
		return runtimeDefinition.getVersion();
	}

	@Override
	public boolean exists(RuntimeDefinition runtimeDefinition) {
		return false;
	}
	
	/* These methods are only here until I can further 
	 * unravel the situation regarding nested runtimes. 
	 * They WILL BE removed asap. 
	 */
	@Deprecated
	protected boolean isEnabled() {
		IRuntimeDetector d = findMyDetector();
		return d.isEnabled();
	}
	
	@Deprecated	
	protected void setEnabled(boolean enabled) {
		IRuntimeDetector d = findMyDetector();
		((RuntimeDetector)d).setEnabled(enabled);
	}
	
	@Deprecated
	private IRuntimeDetector findMyDetector() {
		Set<IRuntimeDetector> set = RuntimeCoreActivator.getDefault().getRuntimeDetectors();
		Iterator<IRuntimeDetector> i = set.iterator();
		while(i.hasNext()) {
			IRuntimeDetector d = i.next();
			IRuntimeDetectorDelegate mightBeMe = ((RuntimeDetector)d).getDelegate();
			if( mightBeMe == this || this.equals(mightBeMe))
				return d;
		}
		return null;
	}
}
