/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.jst.web.kb.validation;

import org.eclipse.core.resources.IProject;

/**
 * Represents a delegate which is used be EL validator to collect all the
 * projects which should be validated by EL validator.
 * 
 * @author Alexey Kazakov
 */
public interface IELValidationDelegate {

	/**
	 * @param project
	 * @return a set of projects which should be validated together with the
	 *         given project.
	 */
	IValidatingProjectTree getValidatingProjects(IProject project);

	/**
	 * @param project
	 * @return true if the validator should validate the given project.
	 */
	boolean shouldValidate(IProject project);
}