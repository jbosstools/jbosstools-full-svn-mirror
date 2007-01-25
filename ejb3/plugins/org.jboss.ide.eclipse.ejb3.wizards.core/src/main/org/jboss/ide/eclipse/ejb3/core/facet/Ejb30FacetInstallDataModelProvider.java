package org.jboss.ide.eclipse.ejb3.core.facet;

import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jst.j2ee.internal.J2EEVersionConstants;
import org.eclipse.jst.j2ee.internal.common.CreationConstants;
import org.eclipse.jst.j2ee.internal.ejb.project.operations.IEjbFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.internal.plugin.IJ2EEModuleConstants;
import org.eclipse.jst.j2ee.project.facet.J2EEModuleFacetInstallDataModelProvider;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;

public class Ejb30FacetInstallDataModelProvider extends J2EEModuleFacetInstallDataModelProvider 
	implements IEjbFacetInstallDataModelProperties{

	public static final String EJB30_FACET_ID = "jbide.ejb30";
	public static final IProjectFacetVersion EJB_30 = ProjectFacetsManager.getProjectFacet(EJB30_FACET_ID).getVersion("1.0"); //$NON-NLS-1$

	public Set getPropertyNames() {
		Set names = super.getPropertyNames();
		names.add(CONFIG_FOLDER);
		return names;
	}

	public Object getDefaultProperty(String propertyName) {
		if(propertyName.equals(FACET_ID)){
			return EJB30_FACET_ID;
		} else if (propertyName.equals(CONFIG_FOLDER)){
			return CreationConstants.DEFAULT_EJB_SOURCE_FOLDER;
		} else if (propertyName.equals(MODULE_URI)) {
			String projectName = model.getStringProperty(FACET_PROJECT_NAME).replace(' ', '_');
			return projectName + IJ2EEModuleConstants.JAR_EXT; 
		}
		return super.getDefaultProperty(propertyName);
	}


	protected int convertFacetVersionToJ2EEVersion(IProjectFacetVersion version) {
		// I've only got one facet version so far, so woohoo
		return J2EEVersionConstants.J2EE_1_4_ID;
	}
		
	public boolean isPropertyEnabled(String propertyName) {
		return super.isPropertyEnabled(propertyName);
	}


	public boolean propertySet(String propertyName, Object propertyValue) {
		boolean status = super.propertySet(propertyName, propertyValue);
		return status;
	}	

	public IStatus validate(String propertyName) {
		return super.validate(propertyName);
	}

}
