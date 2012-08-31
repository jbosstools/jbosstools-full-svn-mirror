/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.runtime.handlers;

import java.io.File;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.tools.esb.core.runtime.JBossESBRuntime;
import org.jboss.tools.esb.core.runtime.JBossRuntimeManager;
import org.jboss.tools.runtime.core.model.AbstractRuntimeDetector;
import org.jboss.tools.runtime.core.model.RuntimeDefinition;

public class EsbHandler extends AbstractRuntimeDetector {

	private static final String DEFAULT_CONFIGURATION = "default";
	private static final String ESB = "ESB"; //$NON-NLS-1$
	
	public void initializeRuntimes(List<RuntimeDefinition> runtimeDefinitions) {
		for (RuntimeDefinition runtimeDefinition : runtimeDefinitions) {
			String type = runtimeDefinition.getType();
			if (runtimeDefinition.isEnabled() && !esbExists(runtimeDefinition)) {
				if (ESB.equals(type)) {
					JBossESBRuntime runtime = new JBossESBRuntime();
					runtime.setName("ESB - " + runtimeDefinition.getName()); //$NON-NLS-1$
					runtime.setHomeDir(runtimeDefinition.getLocation()
							.getAbsolutePath());
					runtime.setConfiguration(DEFAULT_CONFIGURATION);
					runtime.setVersion(runtimeDefinition.getVersion());
					JBossRuntimeManager.getInstance().addRuntime(runtime);
				}
			}
			initializeRuntimes(runtimeDefinition.getIncludedServerDefinitions());
		}
	}

	/**
	 * @param serverDefinition
	 * @return
	 */
	private static boolean esbExists(RuntimeDefinition runtimeDefinition) {
		JBossESBRuntime[] runtimes = JBossRuntimeManager.getInstance().getRuntimes();
		for (JBossESBRuntime runtime:runtimes) {
			String location = runtime.getHomeDir();
			if (location != null && location.equals(runtimeDefinition.getLocation().getAbsolutePath())) {
				return true;
			}
		}
		return false;
	}

	public RuntimeDefinition getServerDefinition(File root,
			IProgressMonitor monitor) {
		if (monitor.isCanceled() || root == null) {
			return null;
		}
		// standalone ESB runtime
		return null;
	}

	@Override
	public boolean exists(RuntimeDefinition serverDefinition) {
		if (serverDefinition == null || serverDefinition.getLocation() == null) {
			return false;
		}
		return esbExists(serverDefinition);
	}
	
	@Override
	public String getVersion(RuntimeDefinition serverDefinition) {
		if (serverDefinition == null || serverDefinition.getLocation() == null) {
			return null;
		}
		return JBossRuntimeManager.getInstance().getVersion(serverDefinition.getLocation().getAbsolutePath(), DEFAULT_CONFIGURATION);
	}
}
