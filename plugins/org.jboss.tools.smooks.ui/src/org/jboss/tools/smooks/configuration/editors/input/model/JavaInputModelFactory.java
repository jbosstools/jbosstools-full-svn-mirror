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
package org.jboss.tools.smooks.configuration.editors.input.model;

import org.eclipse.jdt.core.IJavaProject;
import org.jboss.tools.smooks.configuration.SmooksModelUtils;
import org.jboss.tools.smooks.model.SmooksModel;
import org.jboss.tools.smooks.model.core.IParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Java Source Input Model Factory.
 * 
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class JavaInputModelFactory extends AbstractInputModelFactory {

	public Document getModel(SmooksModel smooksConfigModel, IJavaProject project) {
		IParam inputSourceParam = getInputSourceParam(SmooksModelUtils.INPUT_TYPE_JAVA, smooksConfigModel);
		
		
		
		return null;
	}

}
