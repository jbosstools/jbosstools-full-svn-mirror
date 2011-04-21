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

import org.eclipse.jdt.core.IJavaProject;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.templating.model.ModelBuilderException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Input Source Model Factory.
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public interface InputModelFactory {

	/**
	 * Get the input message model for the specified {@link SmooksModel} instance.
	 * @param smooksConfigModel The Smooks model instance.
	 * @param project The Eclipse project.
	 * @return The model.
	 * @throws ModelBuilderException Error building model.
	 */
	Document getModel(SmooksModel smooksConfigModel, IJavaProject project) throws ModelBuilderException;
}
