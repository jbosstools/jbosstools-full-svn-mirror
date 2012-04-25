/*************************************************************************************
 * Copyright (c) 2008-2011 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     JBoss by Red Hat - Initial implementation.
 ************************************************************************************/
package org.jboss.tools.central.jobs;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.jboss.tools.central.JBossCentralActivator;
import org.jboss.tools.project.examples.ProjectExamplesActivator;

/**
 * 
 * @author snjeza
 *
 */
public class RefreshBlogsJob extends AbstractRefreshJob {

	private static final String CACHE_FILE = "blogs.xml";
	private static final String VALID_CACHE_FILE = "valid_blogs.xml";
	public static RefreshBlogsJob INSTANCE = new RefreshBlogsJob();
	
	private RefreshBlogsJob() {
		super("Refreshing JBoss Blogs...", ProjectExamplesActivator.getDefault().getConfigurator().getBlogsUrl());
	}

	@Override
	public File getCacheFile() {
		return getFile(CACHE_FILE);
	}

	protected File getFile(String name) {
		IPath location = JBossCentralActivator.getDefault().getStateLocation();
		File file = new File(location.toFile(), name);
		return file;
	}
	
	@Override
	public File getValidCacheFile() {
		return getFile(VALID_CACHE_FILE);
	}
}
