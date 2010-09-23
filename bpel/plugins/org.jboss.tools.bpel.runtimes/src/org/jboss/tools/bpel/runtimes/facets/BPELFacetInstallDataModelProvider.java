package org.jboss.tools.bpel.runtimes.facets;

import java.util.Set;

import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.project.facet.core.IActionConfigFactory;
import org.jboss.tools.bpel.runtimes.IBPELModuleFacetConstants;

public class BPELFacetInstallDataModelProvider extends
		FacetInstallDataModelProvider implements IActionConfigFactory, IFacetDataModelProperties, IBPELModuleFacetConstants {
	
	@Override
	public Set getPropertyNames() {
		Set names = super.getPropertyNames();
		names.add(FACET_PROJECT_NAME);
		names.add(FACET_ID);
		names.add(BPEL_CONTENT_FOLDER);
		return names;
	}

	@Override
	public Object getDefaultProperty(String propertyName) {
		if (propertyName.equals(FACET_ID)) {
			return IBPELModuleFacetConstants.BPEL_PROJECT_FACET;
		}
		return super.getDefaultProperty(propertyName);
	}
}
