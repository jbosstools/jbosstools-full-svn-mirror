/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.management.as7.deployment;

import java.text.MessageFormat;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.as.controller.client.helpers.standalone.DeploymentAction;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentActionResult;
import org.jboss.as.controller.client.helpers.standalone.ServerDeploymentPlanResult;
import org.jboss.ide.eclipse.as.core.server.internal.v7.JBoss7ManangerException;
import org.jboss.ide.eclipse.as.core.server.internal.v7.IJBoss7DeploymentResult;
import org.jboss.ide.eclipse.as.management.as7.Activator;

/**
 * A class that holds the status of a deployment operation.
 * 
 * @author André Dietisheim
 *
 */
public class DeploymentOperationResult implements IJBoss7DeploymentResult {

	private Future<ServerDeploymentPlanResult> planResult;
	private DeploymentAction action;

	DeploymentOperationResult(DeploymentAction action, Future<ServerDeploymentPlanResult> planResult) {
		Assert.isNotNull(action);
		this.action = action;
		Assert.isNotNull(planResult);
		this.planResult = planResult;
	}

	/* (non-Javadoc)
	 * @see org.jboss.ide.eclipse.as.management.as7.deployment.IDeploymentResult#getStatus()
	 */
	@Override
	public IStatus getStatus() throws JBoss7ManangerException {
		try {
			ServerDeploymentActionResult actionResult = planResult.get().getDeploymentActionResult(action.getId());
			return createStatus(action.getDeploymentUnitUniqueName(), action.getType().name(), actionResult);
		} catch (Exception e) {
			throw new JBoss7ManangerException(e);
		}
	}

	private IStatus createStatus(String deploymentName, String actionName, ServerDeploymentActionResult actionResult) {
		if (actionResult == null) {
			return null;
		}

		IStatus status = null;
		switch (actionResult.getResult()) {
		case NOT_EXECUTED:
			status = createStatus(IStatus.ERROR, "The operation {0} was not executed on unit {1}",
					actionName, deploymentName);
			break;
		case EXECUTED:
			status = Status.OK_STATUS;
			break;
		case FAILED:
			status = createStatus(IStatus.ERROR, "The operation {0} failed for unit {1}",
					actionName, deploymentName);
			break;
		case ROLLED_BACK:
			status = createStatus(IStatus.ERROR, "The operation {0} for unit {1} was rolled back",
					actionName, deploymentName);
			break;
		case CONFIGURATION_MODIFIED_REQUIRES_RESTART:
			status = createStatus(
					IStatus.WARNING,
					"The operation {0} was not executed on unit {1}. The server configuration was changed though and the server needs to be restarted",
					actionName, deploymentName);
			break;
		}
		return status;
	}

	private IStatus createStatus(int severity, String messagePattern, Object... messageArguments) {
		return new Status(severity, Activator.getContext().getBundle().getSymbolicName(), MessageFormat.format(
				messagePattern, messageArguments));
	}
}