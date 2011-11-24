package org.jboss.tools.profiler.internal.ui.launch;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.jboss.tools.profiler.internal.ui.JBossProfilerUiPlugin;

public class LaunchListener implements ILaunchListener, IDebugEventSetListener {
	private List<ILaunch> managedLaunches;

	public LaunchListener() {
		managedLaunches = new ArrayList<ILaunch>();
	}

	public void manage(ILaunch launch) {
		if (managedLaunches.size() == 0)
			hookListener(true);
		if (!managedLaunches.contains(launch))
			managedLaunches.add(launch);
	}

	/**
	 * @see org.eclipse.debug.core.ILaunchesListener#launchesRemoved(org.eclipse.debug.core.ILaunch)
	 */
	public void launchRemoved(ILaunch launch) {
		update(launch, true);
	}

	/**
	 * @see org.eclipse.debug.core.ILaunchesListener#launchesAdded(org.eclipse.debug.core.ILaunch)
	 */
	public void launchAdded(ILaunch launch) {
	}

	/**
	 * @see org.eclipse.debug.core.ILaunchesListener#launchesChanged(org.eclipse.debug.core.ILaunch)
	 */
	public void launchChanged(ILaunch launch) {
	}

	private void update(ILaunch launch, boolean remove) {
		if (managedLaunches.contains(launch)) {
			if (remove || launch.isTerminated()) {
				managedLaunches.remove(launch);
				if (managedLaunches.size() == 0) {
					hookListener(false);
				}
			}
		}
	}

	private void hookListener(boolean add) {
		DebugPlugin debugPlugin = DebugPlugin.getDefault();
		ILaunchManager launchManager = debugPlugin.getLaunchManager();
		if (add) {
			launchManager.addLaunchListener(this);
			debugPlugin.addDebugEventListener(this);
		} else {
			launchManager.removeLaunchListener(this);
			debugPlugin.removeDebugEventListener(this);
		}
	}

	public void shutdown() {
		hookListener(false);
	}

	/**
	 * @see org.eclipse.debug.core.IDebugEventSetListener#handleDebugEvents(org.eclipse.debug.core.DebugEvent)
	 */
	public void handleDebugEvents(DebugEvent[] events) {
		for (int i = 0; i < events.length; i++) {
			DebugEvent event = events[i];
			Object source = event.getSource();
			if (source instanceof IProcess
					&& event.getKind() == DebugEvent.TERMINATE) {
				IProcess process = (IProcess) source;
				ILaunch launch = process.getLaunch();
				if (launch != null) {
					try {
						launchTerminated(launch, process.getExitValue());
					} catch (DebugException e) {
						// ignore
					}
				}
			}
		}
	}

	private void launchTerminated(final ILaunch launch, int returnValue) {
		if (managedLaunches.contains(launch)) {
			update(launch, true);

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {

					File log = getMostRecentLogFile(launch
							.getLaunchConfiguration());
					if (log != null && log.exists()) {

						boolean open = MessageDialog
								.openQuestion(
										JBossProfilerUiPlugin
												.getActiveWorkbenchShell(),
										"JBoss Profiler",
										"Launch with JBoss Profiler completed. \nDo you want to generate and open the latest snapshot generated ?");
						if (open) {
							File generateReport = generateReport(log, launch
									.getLaunchConfiguration());
							if (generateReport != null) {
								openInEditor(new File(generateReport,
										"index.html"));
							} // else something went wrong generating the
								// report. Failing gracefully
							// and expect the error log or generation to show
							// possible cause.
						}
					}

				}

			});
		}
	}

	private void openInEditor(File u) {

		// TODO: should put this in a job ...
		int count = 0;
		while (!u.exists() && count <= 10) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// ignore
			}
		}

		IWorkbenchBrowserSupport support = PlatformUI.getWorkbench()
				.getBrowserSupport();
		IWebBrowser browser;
		try {
			browser = support.createBrowser(null);
		} catch (PartInitException e) {
			JBossProfilerUiPlugin.getDefault().logErrorMessage(
					"Could not create browser.", e);
			return;
		}

		try {
			browser.openURL(u.toURL());
		} catch (PartInitException e) {
			JBossProfilerUiPlugin.getDefault().logErrorMessage(
					"Could not open browser.", e);
		} catch (MalformedURLException e) {
			JBossProfilerUiPlugin.getDefault().logErrorMessage(
					"Could not open browser.", e);
		}
	}

	private File generateReport(File log, ILaunchConfiguration configuration) {
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		if (vmInstall != null) {
			IVMRunner vmRunner = vmInstall.getVMRunner(ILaunchManager.RUN_MODE);
			if (vmRunner != null) {
				String[] classPath = null;
				try {
					classPath = new String[] { LaunchUtils
							.getProfilerClientJar(configuration) };

					if (classPath != null) {
						VMRunnerConfiguration vmConfig = new VMRunnerConfiguration(
								"org.jboss.profiler.client.cmd.Client",
								classPath);
						File destDir = new File(log.getParent(), log.getName()
								+ "-report");
						vmConfig.setProgramArguments(new String[] { "load",
								log.getAbsolutePath(),
								destDir.getAbsolutePath() });

						ILaunch launch = new Launch(null,
								ILaunchManager.RUN_MODE, null);

						vmRunner.run(vmConfig, launch, null);

						return destDir;
					}
				} catch (CoreException e) {
					JBossProfilerUiPlugin.getDefault().logErrorMessage(
							"Problem generating snapshot report", e);
				}
			}

		}
		return null;
	}

	/**
	 * Returns latest .jps file for Profile Launch Configuration.
	 * 
	 * @returns log file or null
	 */
	static File getMostRecentLogFile(ILaunchConfiguration configuration) {
		File latest = null;
		File saveLocation;
		try {
			saveLocation = new File(LaunchUtils.getSaveLocation(configuration));
		} catch (CoreException e) {
			JBossProfilerUiPlugin.getDefault().logErrorMessage(
					"Could not get save location from "
							+ configuration.getName(), e);
			return null;
		}

		refreshWorkspace(saveLocation);
		File[] children = saveLocation.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".jps");
			}
		});
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				if (!children[i].isDirectory()) { //$NON-NLS-1$
					if (latest == null
							|| latest.lastModified() < children[i]
									.lastModified())
						latest = children[i];
				}
			}
		}
		return latest;
	}

	/**
	 * Refresh the save location to make sure it is shown in the UI.
	 * 
	 * @param saveLocation
	 */
	private static void refreshWorkspace(File saveLocation) {
		IContainer[] findContainersForLocationURI = JBossProfilerUiPlugin
				.getWorkspace().getRoot().findContainersForLocationURI(
						saveLocation.toURI());
		for (int i = 0; i < findContainersForLocationURI.length; i++) {
			IContainer iContainer = findContainersForLocationURI[i];
			try {
				iContainer.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				// ignore since the refresh is just to make sure Eclipse knows
				// about the newly generated file.
			}
		}
	}

}
