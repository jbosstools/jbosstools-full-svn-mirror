/*************************************************************************************
 * Copyright (c) 2010-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.runtime.ui;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.internal.preferences.PrefsMessages;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jboss.tools.runtime.core.JBossRuntimeLocator;
import org.jboss.tools.runtime.core.RuntimeCoreActivator;
import org.jboss.tools.runtime.core.model.IRuntimeDetector;
import org.jboss.tools.runtime.core.model.RuntimePath;
import org.jboss.tools.runtime.core.model.RuntimeDefinition;
import org.jboss.tools.runtime.ui.dialogs.SearchRuntimePathDialog;
import org.jboss.tools.runtime.ui.preferences.RuntimePreferencePage;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author snjeza
 * 
 */
public class RuntimeUIActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.runtime.ui"; //$NON-NLS-1$

	// The shared instance
	private static RuntimeUIActivator plugin;

	private static IEclipsePreferences prefs;

	public static final String LASTPATH = "lastPath";

	public static final String RUNTIME_PATHS = "runtimePaths";

	public static final String PATH = "path";

	public static final String RUNTIME_PATH = "runtimePath";

	public static final String SCAN_ON_EVERY_STAERTUP = "scanOnEveryStartup";
	
	public static final String TIMESTAMP = "timestamp";

	private static final String SERVER_DEFINITIONS = "serverDefinitions";
	
	private static final String SERVER_DEFINITION = "serverDefinition";

	private static final String NAME = "name";
	
	private static final String INCLUDED_DEFINITION = "included";

	private static final String VERSION = "version";

	private static final String TYPE = "type";

	private static final String LOCATION = "location";

	private static final String DESCRIPTION = "description";

	private static final String ENABLED = "enabled";
	
	public static final String FIRST_START = "firstStart"; //$NON-NLS-1$

	public static final String PREFERENCES_VERSION = "version"; //$NON-NLS-1$
	
	private static final String RUNTIME_PREFERENCES_VERSION = "2"; //$NON-NLS-1$

	private Set<RuntimePath> runtimePaths = new HashSet<RuntimePath>();
	
	private List<RuntimeDefinition> serverDefinitions;
	
	private ListenerList runtimePathChangeChangeListeners;
	/**
	 * The constructor
	 */
	public RuntimeUIActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		runtimePaths = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		saveRuntimePreferences();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static RuntimeUIActivator getDefault() {
		return plugin;
	}

	public static void log(Throwable e) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, e
				.getLocalizedMessage(), e);
		RuntimeUIActivator.getDefault().getLog().log(status);
	}
	
	public static void log(Throwable e, String message) {
		IStatus status = new Status(IStatus.ERROR, PLUGIN_ID, message, e);
		RuntimeUIActivator.getDefault().getLog().log(status);
	}
	
	public static CheckboxTreeViewer createRuntimeViewer(final Set<RuntimePath> runtimePaths2, Composite composite, int heightHint) {
		GridData gd;
		CheckboxTreeViewer viewer = new CheckboxTreeViewer(composite, SWT.V_SCROLL
				| SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		
		Tree tree = viewer.getTree();
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		GC gc = new GC( composite);
		FontMetrics fontMetrics = gc.getFontMetrics( );
		gc.dispose( );
		gd.minimumHeight = Dialog.convertHeightInCharsToPixels(fontMetrics, heightHint);
		tree.setLayoutData(gd);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);

		String[] columnNames = new String[] { "Name", "Version", "Type", "Location"};
		int[] columnWidths = new int[] {300, 100, 50, 200};
		
		for (int i = 0; i < columnNames.length; i++) {
			TreeViewerColumn tc = new TreeViewerColumn(viewer, SWT.NONE);
			tc.getColumn().setText(columnNames[i]);
			tc.getColumn().setWidth(columnWidths[i]);
		}

		viewer.setLabelProvider(new RuntimeLabelProvider());
		List<RuntimeDefinition> serverDefinitions = new ArrayList<RuntimeDefinition>();
		for (RuntimePath runtimePath:runtimePaths2) {
			serverDefinitions.addAll(runtimePath.getRuntimeDefinitions());
		}
		viewer.setContentProvider(new RuntimeContentProvider(serverDefinitions));
		viewer.setInput(serverDefinitions);
		for (RuntimeDefinition definition:serverDefinitions) {
			viewer.setChecked(definition, definition.isEnabled());
		}
		return viewer;
	}
	
	public static void refreshRuntimes(Shell shell, final Set<RuntimePath> runtimePaths, final CheckboxTreeViewer viewer, boolean needRefresh, int heightHint) {
		IRunnableWithProgress op = new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException,
					InterruptedException {
				JBossRuntimeLocator locator = new JBossRuntimeLocator();
				for (RuntimePath runtimePath : runtimePaths) {
					List<RuntimeDefinition> serverDefinitions = locator
							.searchForRuntimes(runtimePath.getPath(), monitor);
					runtimePath.getRuntimeDefinitions().clear();
					for (RuntimeDefinition serverDefinition : serverDefinitions) {
						serverDefinition.setRuntimePath(runtimePath);
					}
					runtimePath.getRuntimeDefinitions()
							.addAll(serverDefinitions);
				}
			}
		};
		try {
			SearchRuntimePathDialog dialog = new SearchRuntimePathDialog(shell, runtimePaths, needRefresh, heightHint);
			dialog.run(true, true, op);
			if (viewer != null) {
				dialog.getShell().addDisposeListener(new DisposeListener() {

					@Override
					public void widgetDisposed(DisposeEvent e) {
						viewer.setInput(null);
						List<RuntimeDefinition> serverDefinitions = new ArrayList<RuntimeDefinition>();
						for (RuntimePath runtimePath : runtimePaths) {
							serverDefinitions.addAll(runtimePath
									.getRuntimeDefinitions());
							viewer.setInput(serverDefinitions);
							for (RuntimeDefinition serverDefinition : serverDefinitions) {
								runtimeExists(serverDefinition);
								viewer.setChecked(serverDefinition,
										serverDefinition.isEnabled());
							}
						}
					}
				});
			}
		} catch (InvocationTargetException e1) {
			RuntimeUIActivator.log(e1);
		} catch (InterruptedException e1) {
			// ignore
		}
	}

	public static boolean runtimeExists(RuntimeDefinition serverDefinition) {
		Set<IRuntimeDetector> detectors = RuntimeCoreActivator.getDefault().getRuntimeDetectors();
		for (IRuntimeDetector detector:detectors) {
			if (detector.isEnabled() && detector.exists(serverDefinition)) {
				return true;
			}
		}
		return false;
	}
	
	public static void refreshPreferencePage(Shell shell) {
		Shell mainShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();
		if (shell != null && !shell.isDisposed()) {
			shell.close();
		}
		shell = Display.getCurrent().getActiveShell();
		if (shell != mainShell && shell != null) {
			shell.close();
		}
		PreferenceDialog preferenceDialog = PreferencesUtil
				.createPreferenceDialogOn(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(),
						RuntimePreferencePage.ID, null, null);
		preferenceDialog.open();
	}

	public void saveRuntimePreferences() {
		saveRuntimePaths();
		RuntimeCoreActivator.getDefault().saveEnabledDetectors();
	}
	
	private void initRuntimePaths() throws WorkbenchException {
		runtimePaths = new HashSet<RuntimePath>();
		String runtimes = getPreferences().get(RUNTIME_PATHS, null);
		if (runtimes == null || runtimes.isEmpty()) {
			return;
		}
		Reader reader = new StringReader(runtimes);
		XMLMemento memento = XMLMemento.createReadRoot(reader);
		String preferencesVersion = memento.getString(PREFERENCES_VERSION);
		boolean computeIncluded = preferencesVersion == null;
		IMemento[] nodes = memento.getChildren(RUNTIME_PATH);
		for (IMemento node:nodes) {
			String path = node.getString(PATH);
			boolean scanOnEveryStartup = node.getBoolean(SCAN_ON_EVERY_STAERTUP);
			String tsString = node.getString(TIMESTAMP);
			Long timestamp = null;
			try {
				timestamp = new Long(tsString);
			} catch (NumberFormatException e) {
				// ignore
			}
			RuntimePath runtimePath = new RuntimePath(path);
			runtimePath.setScanOnEveryStartup(scanOnEveryStartup);
			if (timestamp != null) {
				runtimePath.setTimestamp(timestamp);
			}
			IMemento serverDefinitionsNode = node.getChild(SERVER_DEFINITIONS);
			IMemento[] sdNodes = serverDefinitionsNode.getChildren(SERVER_DEFINITION);
			for (IMemento sdNode:sdNodes) {
				RuntimeDefinition serverDefinition = createServerDefinition(sdNode);
				serverDefinition.setRuntimePath(runtimePath);
				IMemento includedDefinition = sdNode.getChild(INCLUDED_DEFINITION);
				if (includedDefinition != null) {
					IMemento[] includedNodes = includedDefinition
							.getChildren(SERVER_DEFINITION);
					for (IMemento includedNode : includedNodes) {
						RuntimeDefinition included = createServerDefinition(includedNode);
						included.setRuntimePath(runtimePath);
						included.setParent(serverDefinition);
						serverDefinition.getIncludedServerDefinitions().add(
								included);
					}
				}
				runtimePath.getRuntimeDefinitions().add(serverDefinition);
			}
			runtimePaths.add(runtimePath);
		}
		if (computeIncluded) {
			for(RuntimeDefinition definition:getServerDefinitions()) {
				Set<IRuntimeDetector> detectors = RuntimeCoreActivator.getDefault().getRuntimeDetectors();
				for (IRuntimeDetector detector:detectors) {
					detector.computeIncludedRuntimeDefinition(definition);
				}
			}
		}
	}

	private RuntimeDefinition createServerDefinition(IMemento node) {
		String name = node.getString(NAME);
		String version = node.getString(VERSION);
		String type = node.getString(TYPE);
		String location = node.getString(LOCATION);
		String description = node.getString(DESCRIPTION);
		boolean enabled = node.getBoolean(ENABLED);
		RuntimeDefinition serverDefinition = 
			new RuntimeDefinition(name, version, type, new File(location));
		serverDefinition.setDescription(description);
		serverDefinition.setEnabled(enabled);
		return serverDefinition;
	}

	private static IEclipsePreferences getPreferences() {
		if (prefs == null) {
			prefs = ConfigurationScope.INSTANCE.getNode(PLUGIN_ID);
		}
		return prefs;
	}
	
	public void saveRuntimePaths() {
		if (runtimePaths == null) {
			return;
		}
		XMLMemento memento = XMLMemento.createWriteRoot(RUNTIME_PATHS);
		Writer writer = null;
		try {
			memento.putString(PREFERENCES_VERSION, RUNTIME_PREFERENCES_VERSION);
			for (RuntimePath runtimePath:runtimePaths) {
				IMemento runtimePathNode = memento.createChild(RUNTIME_PATH);
				runtimePathNode.putString(PATH, runtimePath.getPath());
				runtimePathNode.putBoolean(SCAN_ON_EVERY_STAERTUP, runtimePath.isScanOnEveryStartup());
				runtimePathNode.putString(TIMESTAMP, String.valueOf(runtimePath.getTimestamp()));
				IMemento serverDefinitionsNode = runtimePathNode.createChild(SERVER_DEFINITIONS);
				List<RuntimeDefinition> definitions = runtimePath.getRuntimeDefinitions();
				putDefinitions(serverDefinitionsNode, definitions);	
			}
			writer = new StringWriter();
			memento.save(writer);
			writer.flush();
			String runtimes = writer.toString();
			getPreferences().put(RUNTIME_PATHS, runtimes);
			getPreferences().flush();
			if (runtimePathChangeChangeListeners != null) {
				Object[] listeners = runtimePathChangeChangeListeners.getListeners();
				for (Object listener:listeners ) {
					IRuntimePathChangeListener runtimePathChangeChangeListener = (IRuntimePathChangeListener) listener;
					runtimePathChangeChangeListener.changed();
				}
			}
		} catch (Exception e) {
			log(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	private void putDefinitions(IMemento serverDefintionsNode,
			List<RuntimeDefinition> definitions) {
		for (RuntimeDefinition serverDefinition:definitions) {
			IMemento sdNode = serverDefintionsNode.createChild(SERVER_DEFINITION);
			putServerDefinition(serverDefinition, sdNode);
			IMemento includedNodes = sdNode.createChild(INCLUDED_DEFINITION);
			for (RuntimeDefinition included:serverDefinition.getIncludedServerDefinitions()) {
				IMemento includedNode = includedNodes.createChild(SERVER_DEFINITION);
				putServerDefinition(included, includedNode);
			}
		}
	}

	private void putServerDefinition(RuntimeDefinition serverDefinition,
			IMemento node) {
		node.putString(NAME, serverDefinition.getName());
		node.putString(VERSION, serverDefinition.getVersion());
		node.putString(TYPE, serverDefinition.getType());
		node.putString(LOCATION, serverDefinition.getLocation().getAbsolutePath());
		node.putString(DESCRIPTION, serverDefinition.getDescription());
		node.putBoolean(ENABLED, serverDefinition.isEnabled());
	}

	public Set<RuntimePath> getRuntimePaths() {
		if (runtimePaths == null) {
			try {
				initRuntimePaths();
			} catch (WorkbenchException e) {
				log(e);
				runtimePaths = new HashSet<RuntimePath>();
			}
		}
		return runtimePaths;
	}
	
	public List<RuntimeDefinition> getServerDefinitions() {
		if (serverDefinitions == null) {
			serverDefinitions = new ArrayList<RuntimeDefinition>();
		} else {
			serverDefinitions.clear();
		}
		for (RuntimePath runtimePath:getRuntimePaths()) {
			serverDefinitions.addAll(runtimePath.getRuntimeDefinitions());
		}
		return serverDefinitions;
	}

	public Set<IRuntimeDetector> getRuntimeDetectors() {
		return RuntimeCoreActivator.getDefault().getRuntimeDetectors();
	}

	public void initDefaultRuntimePreferences() {
		runtimePaths = new HashSet<RuntimePath>();
	}
	
	public static void setTimestamp(Set<RuntimePath> runtimePaths2) {
		for (RuntimePath runtimePath : runtimePaths2) {
			String path = runtimePath.getPath();
			if (path != null && !path.isEmpty()) {
				File directory = new File(path);
				if (directory.isDirectory()) {
					runtimePath.setTimestamp(directory.lastModified());
				}
			}
		}
	}

	public void refreshRuntimePreferences() {
		runtimePaths = null;
	}
	
	public static boolean runtimeCreated(RuntimeDefinition serverDefinition) {
		Set<IRuntimeDetector> detectors = getDefault().getRuntimeDetectors();
		boolean created = false;
		for (IRuntimeDetector detector:detectors) {
			if (!detector.isEnabled()) {
				continue;
			}
			if (detector.exists(serverDefinition)) {
				List<RuntimeDefinition> includedDefinitions = serverDefinition.getIncludedServerDefinitions();
				boolean includedCreated = true;
				for (RuntimeDefinition includedDefinition:includedDefinitions) {
					if (!runtimeCreated(includedDefinition)) {
						includedCreated = false;
						break;
					}
				}
				if (includedCreated) {
					created = true;
					break;
				}
			}
		}
		return (created);
	}
	
	public void addRuntimePathChangeListener(IRuntimePathChangeListener listener) {
		if (runtimePathChangeChangeListeners == null)
			runtimePathChangeChangeListeners = new ListenerList();
		runtimePathChangeChangeListeners.add(listener);
	}
	
	public void removeRuntimePathChangeListener(IRuntimePathChangeListener listener) {
		if (runtimePathChangeChangeListeners == null)
			return;
		runtimePathChangeChangeListeners.remove(listener);
		if (runtimePathChangeChangeListeners.size() == 0)
			runtimePathChangeChangeListeners = null;
	}
}
