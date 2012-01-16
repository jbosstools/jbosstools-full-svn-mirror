/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.openshift.egit.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.egit.core.EclipseGitProgressTransformer;
import org.eclipse.egit.core.IteratorService;
import org.eclipse.egit.core.op.AddToIndexOperation;
import org.eclipse.egit.core.op.CommitOperation;
import org.eclipse.egit.core.op.ConnectProviderOperation;
import org.eclipse.egit.core.op.FetchOperation;
import org.eclipse.egit.core.op.MergeOperation;
import org.eclipse.egit.core.op.PushOperation;
import org.eclipse.egit.core.op.PushOperationResult;
import org.eclipse.egit.core.op.PushOperationSpecification;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.egit.ui.UIText;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.errors.NotSupportedException;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.IndexDiff;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.lib.UserConfig;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.osgi.util.NLS;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.wst.server.core.IServer;
import org.jboss.tools.openshift.egit.core.internal.EGitCoreActivator;

/**
 * The Class EGitUtils.
 * 
 * @author André Dietisheim
 */
public class EGitUtils {

	//	private static final RefSpec DEFAULT_PUSH_REF_SPEC = new RefSpec("refs/heads/*:refs/remotes/origin/*"); //$NON-NLS-1$
	private static final String DEFAULT_REFSPEC_SOURCE = Constants.HEAD; // HEAD
	private static final String DEFAULT_REFSPEC_DESTINATION = Constants.R_HEADS + Constants.MASTER; // refs/heads/master
	private static final int PUSH_TIMEOUT = 10 * 1024;
	private static final String EGIT_TEAM_PROVIDER_ID = "org.eclipse.egit.core·GitProvider";

	private EGitUtils() {
		// inhibit instantiation
	}

	/**
	 * Returns <code>true</code> if the given project is associated to any team
	 * provider (git, svn, cvs, etc.). Returns <code>false</code> otherwise.
	 * 
	 * @param project
	 *            the project to check
	 * @return <code>true</code> if the project is associated with any team
	 *         provider.
	 */
	public static boolean isShared(IProject project) {
		return RepositoryProvider.getProvider(project) != null;
	}

	/**
	 * Returns <code>true</code> if the given project is associated to the egit
	 * team provider. Returns <code>false</code> otherwise.
	 * 
	 * @param project
	 *            the project to check
	 * @return <code>true</code> if the project is associated with the git team
	 *         provider.
	 */
	public static boolean isSharedWithGit(IProject project) {
		return EGIT_TEAM_PROVIDER_ID.equals(RepositoryProvider.getProvider(project));
	}

	/**
	 * Returns <code>true</code> if the given project exists and has a .git
	 * folder in it.
	 * 
	 * @param project
	 * @return
	 */
	public static boolean hasDotGitFolder(IProject project) {
		if (project == null
				|| !project.exists()) {
			return false;
		}
		return new File(project.getLocation().toOSString(), Constants.DOT_GIT).exists();
	}

	/**
	 * Shares the given project. A repository is created within the given
	 * project and the project is connected to the freshly created repository.
	 * 
	 * @param project
	 * @param monitor
	 * @return
	 * @throws CoreException
	 */
	public static Repository share(IProject project, IProgressMonitor monitor) throws CoreException {
		Repository repository = createRepository(project, monitor);
		connect(project, repository, monitor);
		addToRepository(project, repository, monitor);
		commit(project, monitor);
		// checkout("master", repository);
		return repository;
	}

	/**
	 * Creates a repository for the given project. The repository is created in
	 * the .git directory within the given project. The project is not connected
	 * with the new repository
	 * 
	 * @param project
	 *            the project to create the repository for.
	 * @param monitor
	 *            the monitor to report the progress to
	 * @return
	 * @throws CoreException
	 * 
	 * @see #connect(IProject, Repository, IProgressMonitor)
	 */
	public static Repository createRepository(IProject project, IProgressMonitor monitor) throws CoreException {
		try {
			InitCommand init = Git.init();
			init.setBare(false).setDirectory(project.getLocation().toFile());
			Git git = init.call();
			return git.getRepository();
		} catch (JGitInternalException e) {
			throw new CoreException(EGitCoreActivator.createErrorStatus(
					NLS.bind("Could not initialize a git repository at {0}: {1}",
							getRepositoryPathFor(project),
							e.getMessage()), e));
		}
	}

	public static File getRepositoryPathFor(IProject project) {
		return new File(project.getLocationURI().getPath(), Constants.DOT_GIT);
	}

	public static void addToRepository(IProject project, Repository repository, IProgressMonitor monitor)
			throws CoreException {
		AddToIndexOperation add = new AddToIndexOperation(Collections.singletonList(project));
		add.execute(monitor);
	}

	/**
	 * Connects the given project to the repository within it.
	 * 
	 * @param project
	 *            the project to connect
	 * @param monitor
	 *            the monitor to report progress to
	 * @throws CoreException
	 */
	public static void connect(IProject project, IProgressMonitor monitor) throws CoreException {
		connect(project, getRepositoryPathFor(project), monitor);
	}

	/**
	 * Connects the given project to the given repository.
	 * 
	 * @param project
	 *            the project to connect
	 * @param repository
	 *            the repository to connect the project to
	 * @param monitor
	 *            the monitor to report progress to
	 * @throws CoreException
	 */
	private static void connect(IProject project, Repository repository, IProgressMonitor monitor) throws CoreException {
		connect(project, repository.getDirectory(), monitor);
	}

	private static void connect(IProject project, File repositoryFolder, IProgressMonitor monitor) throws CoreException {
		new ConnectProviderOperation(project, repositoryFolder).execute(monitor);
	}

	/**
	 * Merges the given uri to HEAD in the given repository. The given branch is
	 * the branch in the local repo the fetched HEAD is fetched to.
	 * 
	 * @param branch
	 * @param uri
	 * @param repository
	 * @param monitor
	 * @throws CoreException
	 * @throws InvocationTargetException
	 */
	public static void mergeWithRemote(URIish uri, String branch, Repository repository, IProgressMonitor monitor)
			throws CoreException, InvocationTargetException {
		RefSpec ref = new RefSpec().setSource(Constants.HEAD).setDestination(branch);
		fetch(uri, Collections.singletonList(ref), repository, monitor);
		merge(branch, repository, monitor);
	}

	/**
	 * Merges current master with the given branch. The strategy used is Resolve
	 * since egit does still not support the Recusive stragety.
	 * 
	 * @param branch
	 *            the branch to merge
	 * @param repository
	 *            the repository the branch is in
	 * @param monitor
	 *            the monitor to report the progress to
	 * @return the result of the merge
	 * @throws CoreException
	 * 
	 * @see MergeStrategy#RESOLVE
	 * @link 
	 *       http://www.eclipse.org/forums/index.php/mv/msg/261278/753913/#msg_753913
	 * @link https://bugs.eclipse.org/bugs/show_bug.cgi?id=354099
	 * @link https://bugs.eclipse.org/bugs/show_bug.cgi?id=359951
	 */
	private static MergeResult merge(String branch, Repository repository, IProgressMonitor monitor)
			throws CoreException {
		MergeOperation merge = new MergeOperation(repository, branch, MergeStrategy.RESOLVE.getName());
		merge.execute(monitor);
		return merge.getResult();
	}

	/**
	 * Fetches the source ref(s) (from the given ref spec(s)) from the given uri
	 * to the given destination(s) (in the given ref spec(s)) to the given
	 * repository.
	 * 
	 * @param uri
	 *            the uri to fetch from
	 * @param fetchRefsRefSpecs
	 *            the references with the sources and destinations
	 * @param repository
	 *            the repository to fetch to
	 * @param monitor
	 *            the monitor to report progress to
	 * @return
	 * @throws InvocationTargetException
	 * @throws CoreException
	 */
	private static Collection<Ref> fetch(URIish uri, List<RefSpec> fetchRefsRefSpecs, Repository repository,
			IProgressMonitor monitor)
			throws InvocationTargetException, CoreException {
		FetchOperation fetch = new FetchOperation(repository, uri, fetchRefsRefSpecs, 10 * 1024, false);
		fetch.run(monitor);
		FetchResult result = fetch.getOperationResult();
		return result.getAdvertisedRefs();
	}

	/**
	 * Commits the changes within the given project to it's configured
	 * repository. The project has to be connected to a repository.
	 * 
	 * @param project
	 *            the project whose changes shall be committed
	 * @param monitor
	 *            the monitor to report progress to
	 * @throws CoreException
	 * 
	 * @see #connect(IProject, Repository)
	 */
	private static RevCommit commit(IProject project, String commitMessage, IProgressMonitor monitor)
			throws CoreException {
		Repository repository = getRepository(project);
		Assert.isLegal(repository != null, "Cannot commit project to repository. ");
		return commit(project, commitMessage, repository, monitor);
	}

	public static RevCommit commit(IProject project, IProgressMonitor monitor) throws CoreException {
		return commit(project, "Commit from JBoss Tools", monitor);
	}

	private static RevCommit commit(IProject project, String commitMessage, Repository repository,
			IProgressMonitor monitor)
			throws CoreException {
		Assert.isLegal(project != null, "Could not commit project. No project provided");
		Assert.isLegal(
				repository != null,
				MessageFormat
						.format("Could not commit. Project \"{0}\" is not connected to a repository (call #connect(project, repository) first)",
								project.getName()));
		/**
		 * TODO: add capability to commit selectively
		 */
		UserConfig userConfig = getUserConfig(repository);
		CommitOperation op = new CommitOperation(
				null,
				null,
				null,
				getFormattedUser(userConfig.getAuthorName(), userConfig.getAuthorEmail()),
				getFormattedUser(userConfig.getCommitterName(), userConfig.getCommitterEmail()),
				commitMessage);
		op.setCommitAll(true);
		op.setRepository(repository);
		op.execute(monitor);
		return op.getCommit();
	}

	/**
	 * Pushes the current branch of the given repository to the remote
	 * repository that it originates from.
	 * 
	 * @param repository
	 *            the repository that shall be pushed
	 * @param monitor
	 *            the monitor to report progress to
	 * @throws CoreException
	 *             core exception is thrown if the push could not be executed
	 */
	public static PushOperationResult push(Repository repository, IProgressMonitor monitor)
			throws CoreException {
		return push(repository, getRemoteConfig(repository), false, monitor);
	}

	/**
	 * Pushes the given repository to the remote repo with the given name.
	 * 
	 * @param remote
	 * @param repository
	 * @param monitor
	 * @throws CoreException
	 * 
	 * @see git config file: "[remote..."
	 * @see #getAllRemoteConfigs(Repository)
	 * @see RemoteConfig#getName()
	 */
	public static PushOperationResult push(String remote, Repository repository, IProgressMonitor monitor)
			throws CoreException {
		RemoteConfig remoteConfig = getRemoteConfig(remote, repository);
		return push(repository, remoteConfig, false, monitor);
	}

	public static PushOperationResult pushForce(String remote, Repository repository, IProgressMonitor monitor)
			throws CoreException {
		RemoteConfig remoteConfig = getRemoteConfig(remote, repository);
		return push(repository, remoteConfig, true, monitor);
	}

	private static PushOperationResult push(Repository repository, RemoteConfig remoteConfig,
			boolean force, IProgressMonitor monitor) throws CoreException {
		try {
			if (remoteConfig == null) {
				throw new CoreException(createStatus(null, "Repository \"{0}\" has no remote repository configured",
						repository.toString()));
			}
			PushOperation op = createPushOperation(remoteConfig, repository, force);
			op.run(monitor);
			return op.getOperationResult();
		} catch (CoreException e) {
			throw e;
		} catch (Exception e) {
			throw new CoreException(createStatus(e, "Could not push repo {0}", repository.toString()));
		}
	}

	private static PushOperation createPushOperation(String remoteName, Repository repository) {
		return new PushOperation(repository, remoteName, false, PUSH_TIMEOUT);
	}

	private static PushOperation createPushOperation(RemoteConfig remoteConfig, Repository repository, boolean force)
			throws CoreException {

		PushOperationSpecification spec = new PushOperationSpecification();
		List<URIish> pushToUris = getPushURIs(remoteConfig);
		List<RefSpec> pushRefSpecs = getPushRefSpecs(remoteConfig);
		addURIRefToPushSpecification(pushToUris, pushRefSpecs, repository, spec);

		// return new PushOperation(repository, spec, false, PUSH_TIMEOUT);
		// TODO: fix pushoperation to really use the spec (currently seems like
		// it does not work so we push everything to the remote)

		// TODO ensure the 'force' is respected
		return new PushOperation(repository, remoteConfig.getName(), false, PUSH_TIMEOUT);
	}

	/**
	 * Adds the given push uris to the given push operation specification.
	 * 
	 * @param urisToPush
	 *            the uris to push
	 * @param pushRefSpecs
	 *            the push ref specs
	 * @param repository
	 *            the repository
	 * @param spec
	 *            the spec
	 * @throws CoreException
	 *             the core exception
	 */
	private static void addURIRefToPushSpecification(List<URIish> urisToPush, List<RefSpec> pushRefSpecs,
			Repository repository, PushOperationSpecification spec) throws CoreException {
		for (URIish uri : urisToPush) {
			try {
				spec.addURIRefUpdates(uri,
						Transport.open(repository, uri).findRemoteRefUpdatesFor(pushRefSpecs));
			} catch (NotSupportedException e) {
				throw new CoreException(createStatus(e, "Could not connect repository \"{0}\" to a remote",
						repository.toString()));
			} catch (IOException e) {
				throw new CoreException(createStatus(e,
						"Could not convert remote specifications for repository \"{0}\" to a remote",
						repository.toString()));
			}
		}
	}

	/**
	 * Gets the push uris from the given remoteConfig.
	 * 
	 * @param remoteConfig
	 *            the remote config
	 * @return the push ur is
	 */
	private static List<URIish> getPushURIs(RemoteConfig remoteConfig) {
		List<URIish> urisToPush = new ArrayList<URIish>();
		for (URIish uri : remoteConfig.getPushURIs())
			urisToPush.add(uri);
		if (urisToPush.isEmpty() && !remoteConfig.getURIs().isEmpty())
			urisToPush.add(remoteConfig.getURIs().get(0));
		return urisToPush;
	}

	/**
	 * Gets the push RefSpecs from the given remote configuration.
	 * 
	 * @param config
	 *            the config
	 * @return the push ref specs
	 */
	private static List<RefSpec> getPushRefSpecs(RemoteConfig config) {
		List<RefSpec> pushRefSpecs = new ArrayList<RefSpec>();
		List<RefSpec> remoteConfigPushRefSpecs = config.getPushRefSpecs();
		if (!remoteConfigPushRefSpecs.isEmpty()) {
			pushRefSpecs.addAll(remoteConfigPushRefSpecs);
		} else {
			// default is to push current HEAD to remote MASTER
			pushRefSpecs.add(new RefSpec()
					.setSource(DEFAULT_REFSPEC_SOURCE).setDestination(DEFAULT_REFSPEC_DESTINATION));
		}
		return pushRefSpecs;
	}

	/**
	 * Gets the repository that is configured to the given project.
	 * 
	 * @param project
	 *            the project
	 * @return the repository
	 */
	public static Repository getRepository(IProject project) {
		Assert.isLegal(project != null, "Could not get repository. No project provided");

		RepositoryMapping repositoryMapping = RepositoryMapping.getMapping(project);
		if (repositoryMapping == null) {
			return null;
		}
		return repositoryMapping.getRepository();
	}

	/**
	 * Gets the UserConfig from the given repository. The UserConfig of a repo
	 * holds the default author and committer.
	 * 
	 * @param repository
	 *            the repository
	 * @return the user configuration
	 * @throws CoreException
	 * 
	 * @see PersonIdent(Repository)
	 * @see CommittHelper#calculateCommitInfo
	 */
	private static UserConfig getUserConfig(Repository repository) throws CoreException {
		Assert.isLegal(repository != null, "Could not get user configuration. No repository provided.");

		if (repository.getConfig() == null) {
			throw new CoreException(createStatus(null,
					"no user configuration (author, committer) are present in repository \"{0}\"",
					repository.toString()));
		}
		return repository.getConfig().get(UserConfig.KEY);
	}

	private static String getFormattedUser(String name, String email) {
		return new StringBuilder().append(name).append(" <").append(email).append('>').toString();
	}

	/**
	 * Returns the configuration of the remote repository that is set to the
	 * given repository.
	 * <code>null</null> if none was configured or if there's no remote repo configured.
	 * 
	 * @param repository
	 *            the repository to get the remote repo configuration from
	 * @return the configurtion of the remote repository
	 * @throws CoreException
	 *             the core exception
	 */
	private static RemoteConfig getRemoteConfig(Repository repository) throws CoreException {
		Assert.isLegal(repository != null, "Could not get configuration. No repository provided.");

		String currentBranch = getCurrentBranch(repository);
		String remote = getRemoteName(currentBranch, repository);
		return getRemoteConfig(remote, repository);
	}

	/**
	 * Returns the remote config for the given remote in the given repository
	 * 
	 * @param remote
	 * @param repository
	 * @return
	 * @throws CoreException
	 */
	private static RemoteConfig getRemoteConfig(String remote, Repository repository) throws CoreException {
		Assert.isLegal(repository != null, "Could not get configuration. No repository provided.");

		List<RemoteConfig> allRemotes = getAllRemoteConfigs(repository);
		return getRemoteConfig(remote, allRemotes);
	}

	private static String getCurrentBranch(Repository repository) throws CoreException {
		String branch = null;
		try {
			branch = repository.getBranch();
		} catch (IOException e) {
			throw new CoreException(createStatus(e, "Could not get current branch on repository \"{0}\"",
					repository.toString()));
		}
		return branch;
	}

	/**
	 * Gets the remote config with the given name from the list of remote
	 * repositories. Returns <code>null</code> if it was not found.
	 * 
	 * @param remoteName
	 *            the remote name
	 * @param remoteRepositories
	 *            the remote repositories
	 * @return the remote config
	 */
	private static RemoteConfig getRemoteConfig(String remoteName, List<RemoteConfig> remoteRepositories) {
		RemoteConfig defaultConfig = null;
		RemoteConfig configuredConfig = null;
		for (RemoteConfig config : remoteRepositories) {
			// if (config.getName().equals(Constants.DEFAULT_REMOTE_NAME))
			// defaultConfig = config;
			if (remoteName != null && config.getName().equals(remoteName))
				configuredConfig = config;
		}

		if (configuredConfig == null) {
			return defaultConfig;
		}
		return configuredConfig;
	}

	/**
	 * Returns all the remote configs from the given repository.
	 * 
	 * @param repository
	 *            the repository to retrieve the remote configs of
	 * @return the remote configs that are available on the repository
	 * @throws CoreException
	 */
	public static List<RemoteConfig> getAllRemoteConfigs(Repository repository) throws CoreException {
		try {
			return RemoteConfig.getAllRemoteConfigs(repository.getConfig());
		} catch (URISyntaxException e) {
			throw new CoreException(createStatus(e, "Could not get all remote repositories for repository \"{0}\"",
					repository.toString()));
		}
	}

	/**
	 * Returns <code>true</code> if the given repository has several configured
	 * remotes
	 * 
	 * @param repository
	 * @return
	 * @throws CoreException
	 * 
	 * @see git config file: "[remote..."
	 * @see #getAllRemoteConfigs
	 * @see RemoteConfig#getAllRemoteConfigs
	 * 
	 */
	public static boolean hasMultipleRemotes(Repository repository) throws CoreException {
		return getAllRemoteConfigs(repository).size() > 1;
	}

	/**
	 * Returns the name of the remote repository for the given branch. If
	 * there's no current branch or no remote configured to it, the default
	 * remote is returned ("origin").
	 * 
	 * @param branch
	 *            the branch
	 * @param repository
	 *            the repository
	 * @return the remote name
	 */
	private static String getRemoteName(String branch, Repository repository) {
		String remoteName = null;
		if (ObjectId.isId(branch)) {
			remoteName = Constants.DEFAULT_REMOTE_NAME;
		} else {
			remoteName = repository.getConfig().getString(
					ConfigConstants.CONFIG_BRANCH_SECTION, branch,
					ConfigConstants.CONFIG_REMOTE_SECTION);
			if (remoteName == null) {
				remoteName = Constants.DEFAULT_REMOTE_NAME;
			}
		}

		return remoteName;
	}

	/**
	 * Adds the given uri of a remote repository to the given repository by the
	 * given name.
	 * 
	 * @param remoteName
	 *            the name to use for the remote repository
	 * @param uri
	 *            the uri of the remote repository
	 * @param repository
	 *            the repository to add the remote to
	 * @throws URISyntaxException
	 *             the uRI syntax exception
	 * @throws MalformedURLException
	 *             the malformed url exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void addRemoteTo(String remoteName, URIish uri, Repository repository)
			throws URISyntaxException, MalformedURLException,
			IOException {
		StoredConfig config = repository.getConfig();
		RemoteConfig remoteConfig = new RemoteConfig(config, remoteName);
		remoteConfig.addURI(uri);
		remoteConfig.update(config);
		config.save();
	}

	private static IStatus createStatus(Exception e, String message, String... arguments) throws CoreException {
		IStatus status = null;
		if (e == null) {
			status = new Status(IStatus.ERROR, EGitCoreActivator.PLUGIN_ID, NLS.bind(message, arguments));
		} else {
			status = new Status(IStatus.ERROR, EGitCoreActivator.PLUGIN_ID, NLS.bind(message, arguments), e);
		}
		return status;
	}

	public static int countCommitableChanges(IProject project, IServer server, IProgressMonitor monitor) {
		try {
			Set<String> commitable = getCommitableChanges(project, server, monitor);
			return commitable.size();
		} catch (IOException ioe) {
		}
		return -1;
	}

	private static Set<String> getCommitableChanges(IProject project, IServer server, IProgressMonitor monitor)
			throws IOException {
		Repository repo = getRepository(project);

		EclipseGitProgressTransformer jgitMonitor = new EclipseGitProgressTransformer(monitor);
		IndexDiff indexDiff = new IndexDiff(repo, Constants.HEAD,
				IteratorService.createInitialIterator(repo));
		indexDiff.diff(jgitMonitor, 0, 0, NLS.bind(
				UIText.CommitActionHandler_repository, repo.getDirectory().getPath()));
		Set<String> set = new HashSet<String>();
		if (commitAddedResources(server))
			set.addAll(indexDiff.getAdded());
		if (commitChangedResources(server))
			set.addAll(indexDiff.getChanged());
		if (commitConflictingResources(server))
			set.addAll(indexDiff.getConflicting());
		if (commitMissingResources(server))
			set.addAll(indexDiff.getMissing());
		if (commitModifiedResources(server))
			set.addAll(indexDiff.getModified());
		if (commitRemovedResources(server))
			set.addAll(indexDiff.getRemoved());
		if (commitUntrackedResources(server))
			set.addAll(indexDiff.getUntracked());

		return set;
	}

	/*
	 * Current behaviour is to commit only: added, changed, modified, removed
	 * 
	 * These can be customized as properties on the server one day, if we wish,
	 * such that each server can have custom settings, or, they can be global
	 * settings
	 */
	public static boolean commitAddedResources(IServer server) {
		return true;
	}

	public static boolean commitChangedResources(IServer server) {
		return true;
	}

	public static boolean commitConflictingResources(IServer server) {
		return false;
	}

	public static boolean commitMissingResources(IServer server) {
		return false;
	}

	public static boolean commitModifiedResources(IServer server) {
		return true;
	}

	public static boolean commitRemovedResources(IServer server) {
		return true;
	}

	public static boolean commitUntrackedResources(IServer server) {
		return false;
	}

}
