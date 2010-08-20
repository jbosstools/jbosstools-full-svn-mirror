/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.struts.validator.ui.formset;

import org.jboss.tools.common.model.ui.objecteditor.XChildrenEditor;
import org.jboss.tools.struts.validator.ui.Messages;

public interface ActionNames {
	public String ADD = XChildrenEditor.ADD;
	public String EDIT = XChildrenEditor.EDIT;
	public String OVERWRITE = Messages.ActionNames_Overwrite;
	public String DELETE = XChildrenEditor.DELETE;
	public String DEFAULT = Messages.ActionNames_Default;
}
