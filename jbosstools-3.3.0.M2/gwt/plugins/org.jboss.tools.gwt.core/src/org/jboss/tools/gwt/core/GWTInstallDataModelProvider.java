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
package org.jboss.tools.gwt.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.project.facet.core.IActionConfigFactory;

/**
 * @author adietish
 */
public class GWTInstallDataModelProvider extends FacetInstallDataModelProvider {

	private boolean generateSampleCode;

	@Override
	public void init() {
		super.init();

		model.setProperty(FACET_ID, IJBossToolsGWTPluginConstants.FACET_ID);
	}

	public static class Factory implements IActionConfigFactory {
		public Object create() throws CoreException {
			return new GWTInstallDataModelProvider();
		}
	}

	public void setGenerateSampleCode(boolean generateSampleCode) {
		this.generateSampleCode = generateSampleCode;
	}

	/**
	 * Returns whether the sample code shall be generated.
	 *
	 * @return <tt>true</tt>, if the sample code shall be generated
	 */
	public boolean isGenerateSampleCode() {
		return generateSampleCode;
	}

}
