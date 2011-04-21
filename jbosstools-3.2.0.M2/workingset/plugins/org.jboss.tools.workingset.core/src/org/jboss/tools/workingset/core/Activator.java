package org.jboss.tools.workingset.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandlers.StatusManager;
import org.jboss.tools.workingset.internal.core.NameToWorkingSet;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.jboss.tools.workingset.core"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private class ResourceChangeListener implements IResourceChangeListener {
		public void resourceChanged(IResourceChangeEvent event) {
			if (ntws == null || !getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_ENABLE)) {
				return;
			}

			IResourceDelta delta = event.getDelta();
			IResourceDelta[] affectedChildren = delta.getAffectedChildren(
					IResourceDelta.ADDED, IResource.PROJECT);
			if (affectedChildren.length > 0) {
				IResource[] res = new IResource[affectedChildren.length];
				for (int i = 0; i < affectedChildren.length; i++) {
					res[i] = affectedChildren[i].getResource();					
				}
				updateWorkingsets(res);
			} /*
			 * else { Since projects can change name and we don't know the
			 * previous 'grouping' we don't update this. Maybe solvable by
			 * simply keeping track on where we put them ? affectedChildren=
			 * delta.getAffectedChildren(IResourceDelta.CHANGED,
			 * IResource.PROJECT); for (int i= 0; i < affectedChildren.length;
			 * i++) { IResourceDelta projectDelta= affectedChildren[i]; if
			 * ((projectDelta.getFlags() & IResourceDelta.DESCRIPTION) != 0) {
			 * updateWorkingsets(new IResourceDelta[] { projectDelta }); // one
			 * is enough return; } } }
			 */
		}
	}

	private IResourceChangeListener resourceChangeListener;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	NameToWorkingSet ntws = null;

	protected void updateWorkingsets() {
		updateWorkingsets(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		
	}
	public void updateWorkingsets(IResource[] affectedChildren) {
		if (ntws == null) {
			return;
		}


		Map<String, Set<IProject>> groupings = findGroupings(affectedChildren);

		IWorkingSetManager wsManager = getWorkbench().getWorkingSetManager();

		for (String name : groupings.keySet()) {
			final IWorkingSet workingSet = wsManager.getWorkingSet(name);
			final Set<IProject> projects = groupings.get(name);

			if (workingSet != null) {
				IAdaptable[] elements = workingSet.getElements();
				for (int j = 0; j < elements.length; j++) {
					IAdaptable element = elements[j];
					if (element == null) {
						continue;
					}
					IResource resource = (IResource) element
							.getAdapter(IResource.class);
					if (resource != null
							&& resource.getType() == IResource.PROJECT) {
						projects.remove(resource);
						if (projects.isEmpty()) {
							break;
						}
					}
				}
				if (!projects.isEmpty()) {
					System.out.println("Adding " + projects + " to "
							+ workingSet.getName());
					SafeRunner.run(new ISafeRunnable() {

						public void run() throws Exception {
							IAdaptable[] adaptedNewElements = workingSet
									.adaptElements(projects
											.toArray(new IAdaptable[projects
													.size()]));
							if (adaptedNewElements.length > 0) {
								IAdaptable[] elements = workingSet
										.getElements();
								workingSet.setElements(concat(elements,
										adaptedNewElements));
							}
						}

						public void handleException(Throwable exception) {
							StatusManager.getManager().handle(
									new Status(IStatus.WARNING, PLUGIN_ID,
											"Problem creating workingset "
													+ exception.toString()));

						}
					});

				}
			} else {
				System.out.println("Adding " + projects + " to new" + name);

				IWorkingSet newWs = wsManager.createWorkingSet(name, projects
						.toArray(new IProject[projects.size()]));
				newWs.setId("org.eclipse.jdt.ui.JavaWorkingSetPage"); //$NON-NLS-1$
				wsManager.addWorkingSet(newWs);
			}
		}
	}

	private Map<String, Set<IProject>> findGroupings(
			IResource[] affectedResources) {
		Map<String, Set<IProject>> groupings = new HashMap<String, Set<IProject>>();

		for (int i = 0; i < affectedResources.length; i++) {
			
			IProject prj = (IProject) affectedResources[i];

			String projectName = prj.getName();
			System.out.println("Examing " + projectName);
			String[] candidates = ntws.getWorkingSetNames(projectName);

			for (String candidate : candidates) {
				Set<IProject> set = groupings.get(candidate);
				if (set == null)
					set = new HashSet<IProject>();
				set.add(prj);
				groupings.put(candidate, set);
			}
		}
		return groupings;
	}

	@SuppressWarnings("unchecked")
	static <T> T[] concat(T[] a, T[] b) {
		final int alen = a.length;
		final int blen = b.length;
		final T[] result = (T[]) java.lang.reflect.Array.newInstance(a
				.getClass().getComponentType(), alen + blen);
		System.arraycopy(a, 0, result, 0, alen);
		System.arraycopy(b, 0, result, alen, blen);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		IPropertyChangeListener propertyListener = new IPropertyChangeListener() {
			
			public void propertyChange(PropertyChangeEvent event) {
				if(event.getProperty().equals(PreferenceConstants.P_PATTERNS)) {
					ntws = null;
					
					NameToWorkingSet x = new NameToWorkingSet();

					String newValue = (String) event.getNewValue();
					
					String[] lines = newValue.split(";"); //$NON-NLS-1$
					for (int i = 0; i < lines.length; i++) {
						String[] string = lines[i].split(","); //$NON-NLS-1$
						try {
							x.add(string[0], string[1], Boolean.parseBoolean(string[2]));
						} catch(Exception e) {
							// ignore
						}
					}
					ntws = x;
					
					updateWorkingsets();
				}
				
			}
		};
		
		getDefault().getPreferenceStore().addPropertyChangeListener(propertyListener);
		
		resourceChangeListener = new ResourceChangeListener();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				resourceChangeListener, IResourceChangeEvent.POST_CHANGE);

		
	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
