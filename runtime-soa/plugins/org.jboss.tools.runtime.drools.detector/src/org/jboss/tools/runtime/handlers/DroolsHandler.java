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
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.drools.eclipse.util.DroolsRuntime;
import org.drools.eclipse.util.DroolsRuntimeManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.tools.runtime.core.model.AbstractRuntimeDetector;
import org.jboss.tools.runtime.core.model.RuntimeDefinition;

public class DroolsHandler extends AbstractRuntimeDetector {

	private static final String DROOLS = "DROOLS"; // NON-NLS-1$
	private static final String SOA_P = "SOA-P"; //$NON-NLS-1$

	@Override
	public void initializeRuntimes(List<RuntimeDefinition> serverDefinitions) {
		DroolsRuntime[] existingRuntimes = DroolsRuntimeManager
				.getDroolsRuntimes();
		List<DroolsRuntime> droolsRuntimes = new ArrayList<DroolsRuntime>();
		if (existingRuntimes != null) {
			for (DroolsRuntime runtime : existingRuntimes) {
				droolsRuntimes.add(runtime);
			}
		}
		initializeInternal(serverDefinitions, droolsRuntimes);
		if (droolsRuntimes.size() > 0) {
			DroolsRuntime[] dra = droolsRuntimes.toArray(new DroolsRuntime[0]);
			DroolsRuntimeManager.setDroolsRuntimes(dra);
		}

	}

	private void initializeInternal(List<RuntimeDefinition> runtimeDefinitions,
			List<DroolsRuntime> droolsRuntimes) {
		for (RuntimeDefinition runtimeDefinition : runtimeDefinitions) {
			String type = runtimeDefinition.getType();
			if (runtimeDefinition.isEnabled() && !droolsExists(runtimeDefinition)) {
				if (DROOLS.equals(type)) {
					File droolsRoot = runtimeDefinition.getLocation(); //$NON-NLS-1$
					if (droolsRoot.isDirectory()) {
						DroolsRuntime runtime = new DroolsRuntime();
						runtime.setName("Drools " + runtimeDefinition.getVersion() + " - " + serverDefinition.getName()); //$NON-NLS-1$
						runtime.setPath(droolsRoot.getAbsolutePath());
						DroolsRuntimeManager.recognizeJars(runtime);
						runtime.setDefault(true);
						droolsRuntimes.add(runtime);
					}
				}
			}
			initializeInternal(runtimeDefinition.getIncludedServerDefinitions(),
					droolsRuntimes);
		}
	}

	/**
	 * @param serverDefinition
	 * @return
	 */
	private static boolean droolsExists(RuntimeDefinition runtimeDefinition) {
		DroolsRuntime[] droolsRuntimes = DroolsRuntimeManager
				.getDroolsRuntimes();
		for (DroolsRuntime dr : droolsRuntimes) {
			String location = dr.getPath();
			if (location != null
					&& location.equals(runtimeDefinition.getLocation()
							.getAbsolutePath())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public RuntimeDefinition getRuntimeDefinition(File root,
			IProgressMonitor monitor) {
		if (monitor.isCanceled() || root == null) {
			return null;
		}
		String[] files = root.list(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				if (name.startsWith("drools-api") && name.endsWith(".jar")) {
					return true;
				}
				return false;
			}
		});
		if (files != null && files.length > 0) {
			String version = getImplementationVersion(root, files[0]);
			if (version != null) {
				version = version.substring(0, 3);
				return new RuntimeDefinition(root.getName(), version, DROOLS,
						root.getAbsoluteFile());
			}
		}
		return null;
	}

	@Override
	public boolean exists(RuntimeDefinition runtimeDefinition) {
		if (runtimeDefinition == null || runtimeDefinition.getLocation() == null) {
			return false;
		}
		return droolsExists(runtimeDefinition);
	}
	
	@Override
	public void computeIncludedRuntimeDefinition(
			RuntimeDefinition runtimeDefinition) {
		calculateIncludedServerDefinition(runtimeDefinition);
	}

	@Deprecated /* Does this belong here? Static with no callers? */
	public static void calculateIncludedServerDefinition(
			RuntimeDefinition runtimeDefinition) {
		if (runtimeDefinition == null
				|| !SOA_P.equals(runtimeDefinition.getType())) {
			return;
		}
		File droolsRoot = runtimeDefinition.getLocation(); //$NON-NLS-1$
		if (droolsRoot.isDirectory()) {
			String name = "Drools - " + runtimeDefinition.getName(); //$NON-NLS-1$
			RuntimeDefinition sd = new RuntimeDefinition(name,
					runtimeDefinition.getVersion(), DROOLS, droolsRoot);
			sd.setParent(runtimeDefinition);
			runtimeDefinition.getIncludedServerDefinitions().add(sd);
		}
	}

}
