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
package org.jboss.tools.maven.profiles.core.profiles;

import java.util.List;
import java.util.Map;

import org.apache.maven.model.Profile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.project.IMavenProjectFacade;

/**
 * Retrieves and updates Maven profile informations for Maven projects  
 * 
 * @author Fred Bricon
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IProfileManager {
	
	/**
	 * Returns a List of ProfileStatus for the given mavenProjectFacade  
	 * @param mavenProjectFacade a facade of the maven project
	 * @param monitor a progress monitor
	 * @return a List of ProfileStatus for the given mavenProjectFacade. 
	 * @throws CoreException
	 */
	List<ProfileStatus> getProfilesStatuses(IMavenProjectFacade mavenProjectFacade, IProgressMonitor monitor) throws CoreException;

	/**
	 * Returns an unmodifiable Map of all the available profiles defined in the
	 * Maven settings.xml file.<br/>
	 * The value of each Map.Entry indicates if the profile is active.
	 * @return an unmodifiable Map of all the available profiles for a given project.
	 * @throws CoreException
	 */
	Map<Profile, Boolean> getAvailableSettingProfiles() throws CoreException;

	/**
	 * Update the profiles of the resolver configuration of a IMavenProjectFacade synchronously.
	 * @param mavenProjectFacade a facade of the maven project
	 * @param profiles the profile ids to use in the project's resolver configuration
	 * @param isOffline indicates if the maven request must be executed offline
	 * @param isForceUpdate indicates if a check for updated releases and snapshots on remote repositories must be forced.
	 * @param monitor a progress monitor
	 * @throws CoreException
	 */
	void updateActiveProfiles(IMavenProjectFacade mavenProjectFacade,
			List<String> profiles, boolean isOffline, boolean isForceUpdate, IProgressMonitor monitor)
			throws CoreException; 
}
