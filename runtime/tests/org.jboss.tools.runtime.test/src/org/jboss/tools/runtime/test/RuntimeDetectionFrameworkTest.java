/*************************************************************************************
 * Copyright (c) 2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.runtime.test;

import java.io.File;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.jboss.tools.runtime.core.JBossRuntimeLocator;
import org.jboss.tools.runtime.core.RuntimeCoreActivator;
import org.jboss.tools.runtime.core.model.AbstractRuntimeDetector;
import org.jboss.tools.runtime.core.model.IRuntimeDetector;
import org.jboss.tools.runtime.core.model.InvalidRuntimeDetector;
import org.jboss.tools.runtime.core.model.RuntimeDefinition;
import org.jboss.tools.runtime.core.model.RuntimePath;
import org.jboss.tools.runtime.handlers.TestHandler1;
import org.jboss.tools.runtime.ui.RuntimeUIActivator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

/**
 * 
 * @author rob stryker
 * 
 */
public class RuntimeDetectionFrameworkTest extends TestCase {
	private final static String seamVersionAttributeName = "Seam-Version";

	@BeforeClass
	public static void create() {
		RuntimeCoreActivator.getDefault();
		RuntimeUIActivator.getDefault();
	}

	@Test
	public void testInvalidDetectors() {
		Set<IRuntimeDetector> detectors = RuntimeCoreActivator.getDeclaredRuntimeDetectors();
		IRuntimeDetector invalidDetector = null;
		for (IRuntimeDetector detector:detectors) {
			if (detector instanceof InvalidRuntimeDetector) {
				invalidDetector = detector;
			}
		}
		assertFalse("Invalid detector is enabled.", invalidDetector.isEnabled());
	}
	
	private String displayRuntimes(Set<RuntimePath> paths) {
		String s = "";
		for(RuntimePath path:paths) {
			s += path.getPath() + "\n";
		}
		return s;
	}
	
	@Test
	public void testLoadSaveRuntimePaths() {
		String path = "test/path/one";
		Set<RuntimePath> runtimePaths = RuntimeUIActivator.getDefault().getRuntimePaths();
		assertEquals(displayRuntimes(runtimePaths), 0, runtimePaths.size());
		RuntimePath runtimePath = new RuntimePath(path);
		runtimePath.setScanOnEveryStartup(false);
		runtimePaths.add(runtimePath);
		RuntimeUIActivator.getDefault().saveRuntimePaths();
		restartBundle();
		runtimePaths = RuntimeUIActivator.getDefault().getRuntimePaths();
		assertEquals(1, runtimePaths.size());
		runtimePaths.clear();
		RuntimeUIActivator.getDefault().saveRuntimePaths();
		restartBundle();
		runtimePaths = RuntimeUIActivator.getDefault().getRuntimePaths();
		assertEquals(0, runtimePaths.size());
	}
	
	private void restartBundle() {
		Bundle bundle = Platform.getBundle(RuntimeUIActivator.PLUGIN_ID);
		try {
			// reload prefs
			bundle.stop();
			bundle.start();
		} catch(BundleException be) {
			
		}
	}
	
	@Before
	public void setUp() {
		IPath stateLoc = RuntimeTestActivator.getDefault().getStateLocation();
		stateLoc.append("a").toFile().mkdirs();
		stateLoc.append("b").toFile().mkdirs();
		stateLoc.append("c").toFile().mkdirs();
	}
	
	@After
	public void tearDown() {
		IPath stateLoc = RuntimeTestActivator.getDefault().getStateLocation();
		stateLoc.append("a").toFile().delete();
		stateLoc.append("b").toFile().delete();
		stateLoc.append("c").toFile().delete();
	}
	
	@Test
	public void testInitializationPaths() {
		IPath p = RuntimeTestActivator.getDefault().getStateLocation();
		String path = p.toFile().getAbsolutePath();
		
		// Create our path
		RuntimePath runtimePath = new RuntimePath(path);
		JBossRuntimeLocator locator = new JBossRuntimeLocator();
		
		// Use the locator to find our runtime defs.
		List<RuntimeDefinition> runtimeDefinitions = locator
				.searchForRuntimes(runtimePath.getPath(), new NullProgressMonitor());
		assertEquals(3, runtimeDefinitions.size());
		
		
		// initialize them
		Set<IRuntimeDetector> detectors = RuntimeCoreActivator.getRuntimeDetectors();
		for( IRuntimeDetector detector:detectors) {
			if (detector.isEnabled()) {
				detector.initializeRuntimes(runtimeDefinitions);
			}
		}
		
		String[] initialized = TestHandler1.getInstance().getInited();
		assertEquals(3, initialized.length);
		
	}
}
