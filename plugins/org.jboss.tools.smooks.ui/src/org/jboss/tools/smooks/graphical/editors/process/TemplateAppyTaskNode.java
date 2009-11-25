/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.graphical.editors.process;

import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;

/**
 * @author Dart
 * 
 */
public class TemplateAppyTaskNode extends TaskTypeImpl {

	public TemplateAppyTaskNode() {
		super();
		this.setId(TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE);
		this.setName(TaskTypeManager.getTaskLabel(TaskTypeManager.TASK_ID_FREEMARKER_TEMPLATE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jboss.tools.smooks.graphical.editors.process.TaskTypeImpl#inTheTask
	 * (java.lang.Object)
	 */
	@Override
	public boolean inTheTask(Object smooksModel) {
		if (this.getSmooksModel().contains(smooksModel)) {
			return true;
		}
		return false;
	}

}
