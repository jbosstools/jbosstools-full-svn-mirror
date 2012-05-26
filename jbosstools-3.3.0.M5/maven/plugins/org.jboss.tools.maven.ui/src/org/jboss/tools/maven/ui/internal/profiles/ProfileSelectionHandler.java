/*************************************************************************************
 * Copyright (c) 2009-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.maven.ui.internal.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.internal.IMavenConstants;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.maven.core.MavenCoreActivator;
import org.jboss.tools.maven.core.profiles.IProfileManager;
import org.jboss.tools.maven.core.profiles.ProfileState;
import org.jboss.tools.maven.core.profiles.ProfileStatus;
import org.jboss.tools.maven.ui.Activator;
import org.jboss.tools.maven.ui.Messages;

/**
 * Handles profile selection commands.
 */
public class ProfileSelectionHandler extends AbstractHandler {

	/**
	 * Opens the Maven profile selection Dialog window.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		Set<IMavenProjectFacade> facades = getSelectedMavenProjects(event);

		if (facades.isEmpty()) {
			display(window, Messages.ProfileSelectionHandler_Select_some_maven_projects);
			return null;
		}

		final IProfileManager profileManager = MavenCoreActivator.getDefault().getProfileManager();

		GetProfilesJob getProfilesJob = new GetProfilesJob(facades, profileManager);
		getProfilesJob.addJobChangeListener(onProfilesFetched(getProfilesJob, facades, profileManager, window.getShell()));
		getProfilesJob.setUser(true);
		getProfilesJob.schedule();
		return Status.OK_STATUS;
	}

	private IJobChangeListener onProfilesFetched(final GetProfilesJob getProfilesJob, final Set<IMavenProjectFacade> facades, final IProfileManager profileManager, final Shell shell) {
		return new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				if (getProfilesJob.getResult().isOK()) {
					shell.getDisplay().syncExec(new Runnable() {
						
						public void run() {
							List<ProfileSelection> sharedProfiles = getProfilesJob.getSharedProfiles();
							Map<IMavenProjectFacade, List<ProfileStatus>> allProfiles = getProfilesJob.getAllProfiles();
							final SelectProfilesDialog dialog = new SelectProfilesDialog(shell, 
									facades, 
									sharedProfiles);
							if(dialog.open() == Dialog.OK) {
								Job job = new UpdateProfilesJob(allProfiles, sharedProfiles, profileManager, dialog);
								job.setRule( MavenPlugin.getProjectConfigurationManager().getRule());
								job.schedule();
							}		
						}
					});

				}
			}
		};
	}

	private void display(IWorkbenchWindow window, String message) {
		MessageDialog.openInformation(
				window.getShell(),
				Messages.SelectProfilesDialog_Select_Maven_profiles,
				message);
	}

	/**
	 * Returns an IMavenProjectFacade from the selected IResource, or from the
	 * active editor
	 * 
	 * @param event
	 * @return the selected IMavenProjectFacade
	 */
	private Set<IMavenProjectFacade> getSelectedMavenProjects(ExecutionEvent event) {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IProject[] projects = getSelectedProjects(selection);
		Set<IMavenProjectFacade> facades = new HashSet<IMavenProjectFacade>();
		try {
			if (projects.length == 0) {
				IEditorInput input = HandlerUtil.getActiveEditorInput(event);
				if (input instanceof IFileEditorInput) {
					IFileEditorInput fileInput = (IFileEditorInput) input;
					projects = new IProject[] { fileInput.getFile().getProject() };
				}
			}
			IProgressMonitor monitor = new NullProgressMonitor();
			for (IProject p : projects) {
				if (p != null && p.isAccessible() && p.hasNature(IMavenConstants.NATURE_ID)) {
					IFile pom = p.getFile(IMavenConstants.POM_FILE_NAME);
					IMavenProjectFacade facade = MavenPlugin.getMavenProjectRegistry().create(pom, true, monitor);
					facades.add(facade);
				}
			}
		} catch (CoreException e) {
			Activator.log(e);
		}

		return facades;
	}

	private IProject[] getSelectedProjects(ISelection selection) {
		Set<IProject> projects = new HashSet<IProject>();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Iterator<?> it = structuredSelection.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				if (o instanceof IResource) {
					projects.add(((IResource) o).getProject());
				} else if (o instanceof IWorkingSet) {
					IAdaptable[] elements = ((IWorkingSet) o).getElements();
					if (elements != null) {
						for (IAdaptable e : elements) {
							IProject p = (IProject) e.getAdapter(IProject.class);
							if (p != null) {
								projects.add(p);
							}
						}
					}
				} else if (o instanceof IAdaptable) {
					IProject p = (IProject)((IAdaptable) o).getAdapter(IProject.class);
					if (p != null) {
						projects.add(p);
					}
				}
			}
		}
		IProject[] array = new IProject[projects.size()];
		projects.toArray(array);
		return array;
	}

	class GetProfilesJob extends Job {

		private IProfileManager profileManager;
		private Set<IMavenProjectFacade> facades;
		private Map<IMavenProjectFacade, List<ProfileStatus>> allProfiles;
		private List<ProfileSelection> sharedProfiles;

		private GetProfilesJob(final Set<IMavenProjectFacade> facades, IProfileManager profileManager) {
			super(Messages.ProfileSelectionHandler_Loading_maven_profiles);
			this.facades = facades;
			this.profileManager = profileManager;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				this.allProfiles = getAllProfiles(facades, profileManager);
				this.sharedProfiles = getSharedProfiles(allProfiles);
			} catch (CoreException e) {
				return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						Messages.ProfileSelectionHandler_Unable_to_open_profile_dialog, e);
			}
			return Status.OK_STATUS;
		}

		private List<ProfileSelection> getSharedProfiles(
				Map<IMavenProjectFacade, List<ProfileStatus>> projectProfilesMap) {

			List<ProfileStatus> currentSelection = null;
			List<List<ProfileStatus>> projectProfiles = new ArrayList<List<ProfileStatus>>(projectProfilesMap.values());
			int smallestSize = Integer.MAX_VALUE;
			for (List<ProfileStatus> profiles : projectProfiles) {
				int size = profiles.size();
				if (size < smallestSize) {
					smallestSize = size;
					currentSelection = profiles;
				}
			}
			projectProfiles.remove(currentSelection);

			// Init the smallest profiles selection possible
			List<ProfileSelection> selection = new ArrayList<ProfileSelection>();
			for (ProfileStatus p : currentSelection) {
				ProfileSelection ps = new ProfileSelection();
				ps.setId(p.getId());
				ps.setActivationState(p.getActivationState());
				ps.setAutoActive(p.isAutoActive());
				ps.setSource(p.getSource());
				ps.setSelected(p.isUserSelected());
				selection.add(ps);
			}

			if (!projectProfiles.isEmpty()) {
				// Restrict to the common profiles only
				Iterator<ProfileSelection> ite = selection.iterator();

				while (ite.hasNext()) {
					ProfileSelection p = ite.next();
					for (List<ProfileStatus> statuses : projectProfiles) {
						ProfileStatus s = hasProfile(p.getId(), statuses);
						if (s == null) {
							// remove any non-common profile selection
							ite.remove();
							break;
						}
						// reset non common settings
						if (p.getAutoActive() != null && !p.getAutoActive().equals(s.isAutoActive())) {
							p.setAutoActive(null);
						}
						if (p.getSource() != null && !p.getSource().equals(s.getSource())) {
							p.setSource(Messages.ProfileSelectionHandler_multiple_definitions);
						}
						if (p.getSelected() != null && !p.getSelected().equals(s.isUserSelected())) {
							p.setSelected(null);
						}
						if (p.getActivationState() != null && !p.getActivationState().equals(s.getActivationState())) {
							p.setActivationState(null);
							p.setAutoActive(null);
						}
					}
				}
			}

			return selection;
		}

		private ProfileStatus hasProfile(String id, List<ProfileStatus> statuses) {
			for (ProfileStatus p : statuses) {
				if (id.equals(p.getId())) {
					return p;
				}
			}
			return null;
		}

		private Map<IMavenProjectFacade, List<ProfileStatus>> getAllProfiles(final Set<IMavenProjectFacade> facades,
				final IProfileManager profileManager) throws CoreException {
			Map<IMavenProjectFacade, List<ProfileStatus>> allProfiles =
					new HashMap<IMavenProjectFacade, List<ProfileStatus>>(facades.size());
			IProgressMonitor monitor = new NullProgressMonitor();
			for (IMavenProjectFacade facade : facades) {
				allProfiles.put(facade, profileManager.getProfilesStatuses(facade, monitor));
			}
			return allProfiles;
		}

		public List<ProfileSelection> getSharedProfiles() {
			return sharedProfiles;
		}
		
		public Map<IMavenProjectFacade, List<ProfileStatus>> getAllProfiles() {
			return allProfiles;
		}
	}

	class UpdateProfilesJob extends WorkspaceJob {

		private Map<IMavenProjectFacade, List<ProfileStatus>> allProfiles;
		private List<ProfileSelection> sharedProfiles;
		private IProfileManager profileManager;
		private SelectProfilesDialog dialog;

		private UpdateProfilesJob(Map<IMavenProjectFacade, List<ProfileStatus>> allProfiles,
				List<ProfileSelection> sharedProfiles, IProfileManager profileManager, SelectProfilesDialog dialog) {
			super(Messages.ProfileManager_Updating_maven_profiles);
			this.allProfiles = allProfiles;
			this.sharedProfiles = sharedProfiles;
			this.profileManager = profileManager;
			this.dialog = dialog;
		}

		public IStatus runInWorkspace(IProgressMonitor monitor) {
			try {
				SubMonitor progress = SubMonitor.convert(monitor, Messages.ProfileManager_Updating_maven_profiles, 100);
				SubMonitor subProgress = SubMonitor.convert(progress.newChild(5), allProfiles.size() * 100);
				for (Map.Entry<IMavenProjectFacade, List<ProfileStatus>> entry : allProfiles.entrySet()) {
					if (progress.isCanceled()) {
						throw new OperationCanceledException();
					}
					IMavenProjectFacade facade = entry.getKey();
					List<String> activeProfiles = getActiveProfiles(sharedProfiles, entry.getValue());

					profileManager.updateActiveProfiles(facade, activeProfiles,
							dialog.isOffline(), dialog.isForceUpdate(), subProgress.newChild(100));
				}
			} catch (CoreException ex) {
				Activator.log(ex);
				return ex.getStatus();
			}
			return Status.OK_STATUS;
		}

		private List<String> getActiveProfiles(
				List<ProfileSelection> sharedProfiles,
				List<ProfileStatus> availableProfiles) {
			List<String> ids = new ArrayList<String>();

			for (ProfileStatus st : availableProfiles) {
				ProfileSelection selection = findSelectedProfile(st.getId(), sharedProfiles);
				String id = null;
				boolean isDisabled = false;
				if (selection == null) {
					// was not displayed. Use existing value.
					if (st.isUserSelected()) {
						id = st.getId();
						isDisabled = st.getActivationState().equals(ProfileState.Disabled);
					}
				} else {
					if (null == selection.getSelected()) {
						// Value was displayed but its state is unknown, use
						// previous state
						if (st.isUserSelected()) {
							id = st.getId();
							isDisabled = st.getActivationState().equals(ProfileState.Disabled);
						}
					} else {
						// Value was displayed and is consistent
						if (Boolean.TRUE.equals(selection.getSelected())) {
							id = selection.getId();
							isDisabled = selection.getActivationState().equals(ProfileState.Disabled);
						}
					}
				}

				if (id != null) {
					if (isDisabled) {
						id = "!" + id; //$NON-NLS-1$
					}
					ids.add(id);
				}
			}
			return ids;
		}

		private ProfileSelection findSelectedProfile(String id,
				List<ProfileSelection> sharedProfiles) {
			for (ProfileSelection sel : sharedProfiles) {
				if (id.equals(sel.getId())) {
					return sel;
				}
			}
			return null;
		}
	}
}
