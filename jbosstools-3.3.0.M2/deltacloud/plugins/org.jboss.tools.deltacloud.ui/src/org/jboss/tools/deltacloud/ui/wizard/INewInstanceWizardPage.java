/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.wizard;

import org.eclipse.jface.wizard.IWizardPage;
import org.jboss.tools.common.jobs.ChainedJob;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

/**
 * @author Rob Stryker
 */
public interface INewInstanceWizardPage extends IWizardPage {

	public static final String NEW_INSTANCE_FAMILY = "newInstanceFamily";

	public ChainedJob createPerformFinishJob(DeltaCloudInstance addedInstance);
}
