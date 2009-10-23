 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.web.kb.validation;

import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.jboss.tools.common.model.project.ext.ITextSourceReference;

/**
 * @author Alexey Kazakov
 */
public interface IValidationErrorManager {

	/**
	 * Adds a marker to the resource
	 * @param message
	 * @param preferenceKey
	 * @param messageArguments
	 * @param location
	 * @param target
	 */
	IMarker addError(String message, String preferenceKey,
			String[] messageArguments, ITextSourceReference location,
			IResource target);

	/**
	 * Adds a marker to the resource
	 * @param message
	 * @param preferenceKey
	 * @param messageArguments
	 * @param target
	 */
	IMarker addError(String message, String preferenceKey,
			String[] messageArguments,
			IResource target);

	/**
	 * Adds a marker to the resource
	 * @param message
	 * @param preferenceKey
	 * @param location
	 * @param target
	 */
	IMarker addError(String message, String preferenceKey,
			ITextSourceReference location, IResource target);

	/**
	 * Adds a marker to the resource
	 * @param message
	 * @param preferenceKey
	 * @param messageArguments
	 * @param length
	 * @param offset
	 * @param target
	 */
	IMarker addError(String message, String preferenceKey,
			String[] messageArguments, int length, int offset, IResource target);

	/**
	 * Adds a marker to the resource
	 * @param message
	 * @param severity
	 * @param messageArguments
	 * @param length
	 * @param offset
	 * @param target
	 */
	IMarker addError(String message, int severity, String[] messageArguments, int length, int offset, IResource target);

	/**
	 * Displays a subtask in the progress view. 
	 * @param message
	 */
	void displaySubtask(String message);

	/**
	 * Displays a subtask in the progress view.
	 * @param message
	 * @param messageArguments
	 */
	void displaySubtask(String message, String[] messageArguments);

	/**
	 * Removes all markers for the resources
	 * @param resources
	 */
	void removeMessagesFromResources(Set<IResource> resources);

	/**
	 * Remove all validation messages for the resource.
	 * @param resource
	 */
	void removeAllMessagesFromResource(IResource resource);
}