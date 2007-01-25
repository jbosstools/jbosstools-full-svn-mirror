package org.jboss.ide.eclipse.ejb3.core.facet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.common.project.facet.IJavaFacetInstallDataModelProperties;
import org.eclipse.jst.common.project.facet.JavaFacetInstallDataModelProvider;
import org.eclipse.jst.j2ee.internal.project.J2EEProjectUtilities;
import org.eclipse.jst.j2ee.project.facet.IJ2EEModuleFacetInstallDataModelProperties;
import org.eclipse.jst.j2ee.project.facet.J2EEFacetProjectCreationDataModelProvider;
import org.eclipse.wst.common.componentcore.datamodel.properties.IFacetDataModelProperties;
import org.eclipse.wst.common.frameworks.datamodel.DataModelEvent;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.DataModelPropertyDescriptor;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelListener;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.eclipse.wst.common.project.facet.core.runtime.IRuntime;
import org.eclipse.wst.server.core.ServerCore;
import org.jboss.ide.eclipse.as.core.runtime.server.AbstractJBossServerRuntime;
import org.jboss.ide.eclipse.ejb3.core.classpath.EJB3ClasspathContainer;

public class Ejb30FacetProjectCreationDataModelProvider extends J2EEFacetProjectCreationDataModelProvider {

	public static final String EJB30_FACET_ID = "jbide.ejb30";
	public static final IProjectFacet EJB_30_FACET_OBJECT = ProjectFacetsManager.getProjectFacet(EJB30_FACET_ID);

	public Ejb30FacetProjectCreationDataModelProvider() {
		super();
	}

	public void init() {
		super.init();
		FacetDataModelMap map = (FacetDataModelMap) getProperty(FACET_DM_MAP);
		IDataModel javaFacet = DataModelFactory.createDataModel(new JavaFacetInstallDataModelProvider());
		map.add(javaFacet);
		IDataModel ejbFacet = DataModelFactory.createDataModel(new Ejb30FacetInstallDataModelProvider());
		map.add(ejbFacet);
		javaFacet.setProperty(IJavaFacetInstallDataModelProperties.SOURCE_FOLDER_NAME,ejbFacet.getStringProperty(IJ2EEModuleFacetInstallDataModelProperties.CONFIG_FOLDER));
		ejbFacet.addListener(new IDataModelListener() {
			public void propertyChanged(DataModelEvent event) {
				if (IJ2EEModuleFacetInstallDataModelProperties.EAR_PROJECT_NAME.equals(event.getPropertyName())) {
					setProperty(EAR_PROJECT_NAME, event.getProperty());
				}else if (IJ2EEModuleFacetInstallDataModelProperties.ADD_TO_EAR.equals(event.getPropertyName())) {
					setProperty(ADD_TO_EAR, event.getProperty());
				}
			}
		});
		
		Collection requiredFacets = new ArrayList();
		requiredFacets.add(ProjectFacetsManager.getProjectFacet(javaFacet.getStringProperty(IFacetDataModelProperties.FACET_ID)));
		requiredFacets.add(ProjectFacetsManager.getProjectFacet(ejbFacet.getStringProperty(IFacetDataModelProperties.FACET_ID)));
		setProperty(REQUIRED_FACETS_COLLECTION, requiredFacets);
	}
	
	public boolean propertySet(String propertyName, Object propertyValue) {
		if( propertyName.equals( MODULE_URI )){
			FacetDataModelMap map = (FacetDataModelMap) getProperty(FACET_DM_MAP);
			IDataModel ejbFacet = map.getFacetDataModel( J2EEProjectUtilities.EJB );	
			ejbFacet.setProperty( IJ2EEModuleFacetInstallDataModelProperties.MODULE_URI, propertyValue );
		}
		return super.propertySet(propertyName, propertyValue);
	}	
	
	public DataModelPropertyDescriptor[] getValidPropertyDescriptors(String propertyName) {
		if (FACET_RUNTIME.equals(propertyName)) {
			DataModelPropertyDescriptor[] descriptors = super.getValidPropertyDescriptors(propertyName);
			List list = new ArrayList();
			for (int i = 0; i < descriptors.length; i++) {
				IRuntime rt = (IRuntime) descriptors[i].getPropertyValue();
				if( rt == null ) continue;
				Map properties = rt.getProperties();
				String id = (String)properties.get("id");
				org.eclipse.wst.server.core.IRuntime wstRuntime = ServerCore.findRuntime(id);
				try {
					AbstractJBossServerRuntime jbrt = (AbstractJBossServerRuntime) wstRuntime.getAdapter(AbstractJBossServerRuntime.class);
					if( hasEJB3(jbrt)) {
						list.add(descriptors[i]);
					}
				} catch( Exception e ) {
					e.printStackTrace();
				}
			}
			descriptors = new DataModelPropertyDescriptor[list.size()];
			for (int i = 0; i < descriptors.length; i++) {
				descriptors[i] = (DataModelPropertyDescriptor) list.get(i);
			}
			return descriptors;
		}
		return super.getValidPropertyDescriptors(propertyName);
	}
	
	protected boolean hasEJB3(AbstractJBossServerRuntime jbrt) {
	      IPath jarToCheck = EJB3ClasspathContainer.jbossConfigRelativeJarPaths[0];

	      String jbossBaseDir = jbrt.getRuntime().getLocation().toOSString();
	      String jbossConfigDir = jbrt.getJBossConfiguration();
	      IPath absoluteJarPath = new Path(jbossBaseDir).append("server").append(jbossConfigDir).append(jarToCheck);
	      return absoluteJarPath.toFile().exists();
	}

}


