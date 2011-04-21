/*******************************************************************************
 * Copyright (c) 2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors.input;

import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.EditorPart;
import org.jboss.tools.smooks.model.SmooksModel;
import org.milyn.javabean.dynamic.Model;

/**
 * Input task panel contributor Factory.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public interface InputTaskPanelContributorFactory {

	/**
	 * Set the {@link InputSourceType} associated with the factory instance.
	 * @param inputSourceType The {@link InputSourceType} associated with the factory instance.
	 */
	void setInputSourceType(InputSourceType inputSourceType);
	
	/**
	 * Create a new contributor instance.
	 * @param toolkit The associate Form Toolkit instance.
	 * @param editorPart Editor part.
	 * @param configModel The Smooks configuration model.
	 * @return The contributor instance.
	 */
	InputTaskPanelContributor newInstance(FormToolkit toolkit, EditorPart editorPart, Model<SmooksModel> configModel);
}
