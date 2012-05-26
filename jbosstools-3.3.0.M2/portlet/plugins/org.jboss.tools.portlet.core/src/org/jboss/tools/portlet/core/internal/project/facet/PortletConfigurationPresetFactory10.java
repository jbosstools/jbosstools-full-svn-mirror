/******************************************************************************
 * Copyright (c) 2008 BEA Systems, Inc. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Konstantin Komissarchik
 ******************************************************************************/

package org.jboss.tools.portlet.core.internal.project.facet;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.common.project.facet.JavaFacetUtils;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetUtils;
import org.eclipse.wst.common.project.facet.core.IDynamicPreset;
import org.eclipse.wst.common.project.facet.core.IFacetedProjectBase;
import org.eclipse.wst.common.project.facet.core.IPresetFactory;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.PresetDefinition;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.Messages;

/**
 * @author <a href="mailto:kosta@bea.com">Konstantin Komissarchik</a>
 * @author snjeza
 */

public final class PortletConfigurationPresetFactory10

implements IPresetFactory {
	public PresetDefinition createPreset(final String presetId,
			final Map<String, Object> context)

	throws CoreException {
		final IFacetedProjectBase fproj = (IFacetedProjectBase) context
				.get(IDynamicPreset.CONTEXT_KEY_FACETED_PROJECT);
		final IProjectFacetVersion webFacetVersion = fproj
				.getProjectFacetVersion(WebFacetUtils.WEB_FACET);
		if (webFacetVersion != null
				&& webFacetVersion.compareTo(WebFacetUtils.WEB_23) >= 0) {
			final Set<IProjectFacetVersion> facets = new HashSet<IProjectFacetVersion>();
			final IProjectFacet portletFacet = ProjectFacetsManager
					.getProjectFacet(IPortletConstants.PORTLET_FACET_ID);
			final IProjectFacetVersion portletFacetVersion10 = portletFacet
					.getVersion(IPortletConstants.PORTLET_FACET_VERSION_10);
			facets.add(portletFacetVersion10);
			facets.add(webFacetVersion);
			facets.add(JavaFacetUtils.JAVA_50);
			return new PresetDefinition(Messages.PortletConfigurationPresetFactory10_Portlet_Project_v1_0,
					Messages.PortletConfigurationPresetFactory10_Portlet_Project_v1_0, facets);
		}
		return null;
	}
}
