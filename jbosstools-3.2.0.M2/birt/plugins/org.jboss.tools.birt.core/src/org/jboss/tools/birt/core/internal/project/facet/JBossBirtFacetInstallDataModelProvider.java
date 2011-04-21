package org.jboss.tools.birt.core.internal.project.facet;

import org.eclipse.birt.integration.wtp.ui.internal.wizards.BirtWizardUtil;
import org.eclipse.birt.integration.wtp.ui.project.facet.BirtFacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.jboss.tools.birt.core.BirtCoreActivator;

public class JBossBirtFacetInstallDataModelProvider extends
		BirtFacetInstallDataModelProvider {

	@Override
	public Object create( )
	{
		IDataModel dataModel = (IDataModel) super.create( );
		dataModel.setProperty( FACET_ID, BirtCoreActivator.JBOSS_BIRT__FACET_ID );
		dataModel.setProperty( BIRT_CONFIG, BirtWizardUtil.initWebapp( null ) );
		// TODO: define all the birt properties as nested data models
		return dataModel;
	}
}
