package org.jboss.tools.ws.core.classpath;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ClasspathContainerInitializer;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.jboss.tools.ws.core.messages.JbossWSCoreMessages;

/**
 * @author Grid Qian
 */
public class JbossWSRuntimeClassPathInitializer extends
		ClasspathContainerInitializer {

	public JbossWSRuntimeClassPathInitializer() {
	}

	private String segment;

	@Override
	public void initialize(IPath containerPath, IJavaProject project)
			throws CoreException {

		if (containerPath.segment(0).equals(
				JbossWSCoreMessages.JBossWS_Runtime_Lib)) {
			JbossWSRuntimeClasspathContainer container = new JbossWSRuntimeClasspathContainer(
					containerPath);
			segment = containerPath.segment(1);
			JavaCore.setClasspathContainer(containerPath,
					new IJavaProject[] { project },
					new IClasspathContainer[] { container }, null);
		}
	}

	public IClasspathEntry[] getEntries(IPath path) {
		return new JbossWSRuntimeClasspathContainer(path).getClasspathEntries();
	}

	public class JbossWSRuntimeClasspathContainer implements
			IClasspathContainer {
		private IPath path;
		private IClasspathEntry[] entries = null;

		public JbossWSRuntimeClasspathContainer(IPath path) {
			this.path = path;
		}

		public String getDescription() {
			return JbossWSCoreMessages.JBossWS_Runtime;
		}

		public int getKind() {
			return IClasspathContainer.K_APPLICATION;
		}

		public IPath getPath() {
			return path;
		}

		public IClasspathEntry[] getClasspathEntries() {
			if (entries == null) {
				ArrayList<IClasspathEntry> entryList = new ArrayList<IClasspathEntry>();
				JbossWSRuntime jbws = JbossWSRuntimeManager.getInstance()
						.findRuntimeByName(segment);
				if (jbws.isUserConfigClasspath()) {
					for (String jar : jbws.getLibraries()) {
						entryList.add(getEntry(new Path(jar)));
					}
					entries = entryList.toArray(new IClasspathEntry[entryList
							.size()]);
				} else {
					IPath wsPath = null;
					if (jbws != null) {
						wsPath = new Path(jbws.getHomeDir());
					}
					if (wsPath != null) {
						IPath libPath = wsPath
								.append(JbossWSCoreMessages.Dir_Lib);
						entryList.addAll(Arrays.asList(getEntries(libPath,
								entryList)));
						libPath = wsPath.append(JbossWSCoreMessages.Dir_Client);
						entryList.addAll(Arrays.asList(getEntries(libPath,
								entryList)));
						entries = entryList
								.toArray(new IClasspathEntry[entryList.size()]);
					}
				}
				if (entries == null)
					return new IClasspathEntry[0];
			}
			return entries;
		}

		protected IClasspathEntry getEntry(IPath path) {
			return JavaRuntime.newArchiveRuntimeClasspathEntry(path)
					.getClasspathEntry();
		}

		protected IClasspathEntry[] getEntries(IPath folder,
				ArrayList<IClasspathEntry> entryList) {
			String[] files = folder.toFile().list();
			ArrayList<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
			for (int i = 0; i < files.length; i++) {
				if (files[i].endsWith(".jar")
						&& filterJars(files[i], entryList)) {
					list.add(getEntry(folder.append(files[i])));
				} else if (folder.append(files[i]).toFile().isDirectory()) {
					list.addAll(Arrays.asList(getEntries(folder
							.append(files[i]), entryList)));
				}
			}
			return list.toArray(new IClasspathEntry[list.size()]);
		}

		public void removeEntry(String jarName) {
			if (entries == null) {
				return;
			}
			IClasspathEntry[] newEntries = new IClasspathEntry[entries.length - 1];
			int i = 0;
			for (IClasspathEntry entry : entries) {
				if (!entry.toString().contains(jarName)) {
					newEntries[i++] = entry;
				}
			}
			entries = newEntries;
		}

	}

	public boolean filterJars(String jarName, ArrayList<IClasspathEntry> list) {
		for (IClasspathEntry entry : list) {
			if (entry.getPath().lastSegment().equals(jarName)) {
				return false;
			}
		}
		return true;
	}

}
