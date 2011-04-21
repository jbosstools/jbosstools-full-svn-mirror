package org.jboss.tools.bpel.runtimes.ui.wizards;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jst.common.project.facet.IJavaFacetInstallDataModelProperties;
import org.eclipse.jst.common.project.facet.JavaFacetUtils;
import org.eclipse.wst.common.componentcore.datamodel.FacetProjectCreationDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;

public class BPELFacetProjectCreationDataModelProvider extends
		FacetProjectCreationDataModelProvider {

	@Override
	public void init() {
		super.init();
		
        Collection<IProjectFacet> requiredFacets = new ArrayList<IProjectFacet>();
        requiredFacets.add(JavaFacetUtils.JAVA_FACET);
//        requiredFacets.add(IJ2EEFacetConstants.UTILITY_FACET);
        setProperty(REQUIRED_FACETS_COLLECTION, requiredFacets);
		
		FacetDataModelMap map = (FacetDataModelMap) getProperty(FACET_DM_MAP);
		IDataModel javaFacet = map.getFacetDataModel(JavaFacetUtils.JAVA_FACET.getId());
		javaFacet.setStringProperty(IJavaFacetInstallDataModelProperties.SOURCE_FOLDER_NAME, "bpel");
	}
	

}
