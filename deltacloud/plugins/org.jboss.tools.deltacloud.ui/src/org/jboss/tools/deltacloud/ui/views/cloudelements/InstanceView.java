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
package org.jboss.tools.deltacloud.ui.views.cloudelements;

import java.beans.PropertyChangeEvent;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.IEvaluationService;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;

/**
 * A view that displays instances of a DeltaCloud
 * 
 * @see DeltaCloud#getInstances()
 * 
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class InstanceView extends AbstractCloudElementTableView<DeltaCloudInstance> {

	protected String getSelectedCloudPrefsKey() {
		return IDeltaCloudPreferenceConstants.LAST_CLOUD_INSTANCE_VIEW;
	}

	@Override
	protected String getViewID() {
		return "org.jboss.tools.deltacloud.ui.views.InstanceView";
	}

	@Override
	protected ITableContentAndLabelProvider<DeltaCloudInstance> getContentAndLabelProvider() {
		return new InstanceViewLabelAndContentProvider();
	}

	@Override
	protected void refreshToolbarCommandStates() {
		IEvaluationService evaluationService = (IEvaluationService) PlatformUI.getWorkbench().getService(
				IEvaluationService.class);
		evaluationService.requestEvaluation("org.jboss.tools.deltacloud.ui.commands.canStart");
		evaluationService.requestEvaluation("org.jboss.tools.deltacloud.ui.commands.canStop");
		evaluationService.requestEvaluation("org.jboss.tools.deltacloud.ui.commands.canReboot");
		evaluationService.requestEvaluation("org.jboss.tools.deltacloud.ui.commands.canDestroy");
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		if (DeltaCloud.PROP_INSTANCES.equals(event.getPropertyName())) {
			updateFilteredLabel();
			refreshToolbarCommandStates();
		}
	}

	@Override
	protected ICloudElementFilter<DeltaCloudInstance> getFilter(DeltaCloud cloud) {
		return cloud.getInstanceFilter();
	}
}
