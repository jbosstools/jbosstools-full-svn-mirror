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
package org.jboss.tools.common.mylyn.test;

import junit.framework.TestCase;

import org.eclipse.mylyn.internal.tasks.ui.TasksUiPlugin;
import org.eclipse.mylyn.tasks.core.TaskRepository;

/**
 * @author Alexey Kazakov
 */
public class RepositoryTest extends TestCase {

	public void testBugzilla() {
		TaskRepository repo = TasksUiPlugin.getRepositoryManager().getRepository("bugzilla", "https://bugzilla.redhat.com");
		assertNotNull(repo);
	}

	public void testJIRA() {
		TaskRepository repo = TasksUiPlugin.getRepositoryManager().getRepository("jira", "https://issues.jboss.org");
		assertNotNull(repo);
	}
}