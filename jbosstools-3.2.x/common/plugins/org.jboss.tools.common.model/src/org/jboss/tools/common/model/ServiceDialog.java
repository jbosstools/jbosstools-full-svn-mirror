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
package org.jboss.tools.common.model;

import java.util.*;
import org.jboss.tools.common.meta.action.XEntityData;
import org.jboss.tools.common.meta.action.impl.SpecialWizardSupport;

public interface ServiceDialog { 

    public int QUESTION = 0;
    public int ERROR = 1;
    public int WARNING = 2;
    public int MESSAGE = 3;

	public void setModel(XModel model);
    public int showDialog(String title, String message,
                          String[] options, XEntityData data, int type);
                          
	public void showDialog(SpecialWizardSupport support);

	public String DIALOG_MESSAGE = "message"; //$NON-NLS-1$
	public String CHECKBOX_MESSAGE = "checkboxMessage"; //$NON-NLS-1$
	public String CHECKED = "checked"; //$NON-NLS-1$
	public String SEPARATOR = "separator"; //$NON-NLS-1$
	public String BUTTONS = "buttons"; //$NON-NLS-1$
	public static String RETURN_CODE = "returnCode"; //$NON-NLS-1$
	
	public boolean openConfirm(Properties p);
	
}
