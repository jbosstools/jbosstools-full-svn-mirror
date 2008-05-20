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
package org.jboss.tools.seam.internal.core.validation;

import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.wst.validation.internal.core.Message;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;
import org.eclipse.wst.validation.internal.provisional.core.IValidator;
import org.jboss.tools.seam.core.ISeamProject;
import org.jboss.tools.seam.core.ISeamTextSourceReference;
import org.jboss.tools.seam.core.SeamCorePlugin;
import org.jboss.tools.seam.core.SeamPreferences;

/**
 * @author Alexey Kazakov
 */
public class ValidationErrorManager implements IValidationErrorManager {

	public static final String BASE_NAME = "org.jboss.tools.seam.internal.core.validation.messages"; //$NON-NLS-1$

	IStatus OK_STATUS = new Status(IStatus.OK,
			"org.eclipse.wst.validation", 0, "OK", null); //$NON-NLS-1$ //$NON-NLS-2$

	protected IValidator validationManager;
	protected SeamContextValidationHelper coreHelper;
	protected IReporter reporter;
	protected ISeamProject project;
	protected String markerId;

	/**
	 * Constructor
	 * @param validatorManager
	 * @param coreHelper can be null
	 * @param reporter
	 * @param project
	 */
	public ValidationErrorManager(IValidator validatorManager,
			SeamContextValidationHelper coreHelper, IReporter reporter,
			ISeamProject project, String markerId) {
		this.validationManager = validatorManager;
		this.coreHelper = coreHelper;
		this.project = project;
		this.reporter = reporter;
		this.markerId = markerId;
	}

	protected String getBaseName() {
		return BASE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String,
	 *      java.lang.String, java.lang.String[],
	 *      org.jboss.tools.seam.core.ISeamTextSourceReference,
	 *      org.eclipse.core.resources.IResource)
	 */
	public void addError(String messageId, String preferenceKey,
			String[] messageArguments, ISeamTextSourceReference location,
			IResource target) {
		addError(messageId, preferenceKey, messageArguments, location
				.getLength(), location.getStartPosition(), target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String,
	 *      java.lang.String,
	 *      org.jboss.tools.seam.core.ISeamTextSourceReference,
	 *      org.eclipse.core.resources.IResource)
	 */
	public void addError(String messageId, String preferenceKey,
			ISeamTextSourceReference location, IResource target) {
		addError(messageId, preferenceKey, new String[0], location, target);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String, java.lang.String, java.lang.String[], org.eclipse.core.resources.IResource)
	 */
	public void addError(String messageId, String preferenceKey,
			String[] messageArguments, IResource target) {
		addError(messageId, preferenceKey, messageArguments, 0, 0, target);
	}

	private String getMarkerId() {
		return markerId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String,
	 *      java.lang.String, java.lang.String[], int, int,
	 *      org.eclipse.core.resources.IResource)
	 */
	public void addError(String messageId, String preferenceKey,
			String[] messageArguments, int length, int offset, IResource target) {
		String preferenceValue = SeamPreferences.getProjectPreference(project,
				preferenceKey);
		boolean ignore = false;
		int messageSeverity = IMessage.HIGH_SEVERITY;
		if (SeamPreferences.WARNING.equals(preferenceValue)) {
			messageSeverity = IMessage.NORMAL_SEVERITY;
		} else if (SeamPreferences.IGNORE.equals(preferenceValue)) {
			ignore = true;
		}

		if (ignore) {
			return;
		}

		IMessage message = new Message(getBaseName(), messageSeverity,
				messageId, messageArguments, target,
				getMarkerId());
		message.setLength(length);
		message.setOffset(offset);
		try {
			if (coreHelper != null) {
				coreHelper.getDocumentProvider().connect(target);
				message.setLineNo(coreHelper.getDocumentProvider().getDocument(
						target).getLineOfOffset(offset) + 1);
			}
		} catch (BadLocationException e) {
			SeamCorePlugin.getPluginLog().logError(
					"Exception occurred during error line number calculation",
					e);
			return;
		} catch (CoreException e) {
			SeamCorePlugin.getPluginLog().logError(
					"Exception occurred during error line number calculation",
					e);
			return;
		} finally {
			if(coreHelper!=null) {
				coreHelper.getDocumentProvider().disconnect(target);
			}
		}

		reporter.addMessage(validationManager, message);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#addError(java.lang.String, int, java.lang.String[], int, int, org.eclipse.core.resources.IResource)
	 */
	public void addError(String messageId, int severity, String[] messageArguments, int length, int offset, IResource target) {
		IMessage message = new Message(getBaseName(), severity,
				messageId, messageArguments, target,
				getMarkerId());
		message.setLength(length);
		message.setOffset(offset);
		try {
			if (coreHelper != null) {
				coreHelper.getDocumentProvider().connect(target);
				message.setLineNo(coreHelper.getDocumentProvider().getDocument(
						target).getLineOfOffset(offset) + 1);
			}
		} catch (BadLocationException e) {
			SeamCorePlugin.getPluginLog().logError(
					"Exception occurred during error line number calculation",
					e);
			return;
		} catch (CoreException e) {
			SeamCorePlugin.getPluginLog().logError(
					"Exception occurred during error line number calculation",
					e);
			return;
		}
		reporter.addMessage(validationManager, message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#displaySubtask(java.lang.String)
	 */
	public void displaySubtask(String messageId) {
		displaySubtask(messageId, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#displaySubtask(java.lang.String,
	 *      java.lang.String[])
	 */
	public void displaySubtask(String messageId, String[] messageArguments) {
		IMessage message = new Message(getBaseName(), IMessage.NORMAL_SEVERITY,
				messageId, messageArguments);
		reporter.displaySubtask(validationManager, message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#removeMessagesFromResources(java.util.Set)
	 */
	public void removeMessagesFromResources(Set<IResource> resources) {
		for (IResource r : resources) {
			reporter.removeMessageSubset(validationManager, r, getMarkerId());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#removeAllMessagesFromResource(org.eclipse.core.resources.IResource)
	 */
	public void removeAllMessagesFromResource(IResource resource) {
		reporter.removeAllMessages(validationManager, resource);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.seam.internal.core.validation.IValidationErrorManager#setProject(org.jboss.tools.seam.core.ISeamProject)
	 */
	public void setProject(ISeamProject project) {
		this.project = project;
	}
}