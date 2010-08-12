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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.editor.FormPage;

/**
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public interface InputReaderConfigurationContributor {

	/**
	 * Add the input configuration controls to the Input Configuration area
	 * of the input task panel.
	 * @param formPage The composite parent {@link FormPage}.
	 * @param configComposite The task panel Composite.
	 */
	void addInputConfigControls(FormPage formPage, Composite configComposite);
}
