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

public final class JSFPortletConfigurationPresetFactory10

implements IPresetFactory {
	public PresetDefinition createPreset(final String presetId,
			final Map<String, Object> context)

	throws CoreException {
		final IFacetedProjectBase fproj = (IFacetedProjectBase) context
				.get(IDynamicPreset.CONTEXT_KEY_FACETED_PROJECT);
		final IProjectFacetVersion webFacetVersion = fproj
				.getProjectFacetVersion(WebFacetUtils.WEB_FACET);
		final IProjectFacet PORTLET_FACET = ProjectFacetsManager.getProjectFacet(IPortletConstants.PORTLET_FACET_ID);
		final IProjectFacetVersion portletFacetVersion = PORTLET_FACET.getVersion(IPortletConstants.PORTLET_FACET_VERSION_10);
		final IProjectFacet JSF_FACET = ProjectFacetsManager.getProjectFacet("jst.jsf"); //$NON-NLS-1$
		final IProjectFacetVersion jsfFacetVersion = JSF_FACET.getVersion("1.2"); //$NON-NLS-1$
		if (webFacetVersion != null
				&& webFacetVersion.compareTo(WebFacetUtils.WEB_23) >= 0 &&
				portletFacetVersion != null) {
			final Set<IProjectFacetVersion> facets = new HashSet<IProjectFacetVersion>();
			final IProjectFacet jsfPortletFacet = ProjectFacetsManager
					.getProjectFacet(IPortletConstants.JSFPORTLET_FACET_ID);
			final IProjectFacetVersion jsfPortletVersion = jsfPortletFacet
					.getVersion(IPortletConstants.JSFPORTLET_FACET_VERSION_10);
			facets.add(jsfPortletVersion);
			facets.add(webFacetVersion);
			facets.add(JavaFacetUtils.JAVA_50);
			facets.add(portletFacetVersion);
			facets.add(jsfFacetVersion);
			return new PresetDefinition(Messages.JSFPortletConfigurationPresetFactory10_JBoss_JSF_Portlet_Project_v1_0,
					Messages.JSFPortletConfigurationPresetFactory10_JBoss_JSF_Portlet_Project_v1_0, facets);
		}
		return null;
	}
}
