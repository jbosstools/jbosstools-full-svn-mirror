package org.jboss.tools.birt.core.internal.project.facet;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.internal.webapplication.WebAppBean;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.BirtWizardUtil;
import org.eclipse.birt.integration.wtp.ui.internal.wizards.SimpleImportOverwriteQuery;
import org.eclipse.birt.integration.wtp.ui.project.facet.BirtFacetInstallDelegate;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.j2ee.model.ModelProviderManager;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebAppVersionType;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.jboss.tools.birt.core.BirtCoreActivator;
import org.jboss.tools.birt.core.Messages;
import org.osgi.framework.Bundle;

public class JBossBirtFacetInstallDelegate extends BirtFacetInstallDelegate {

	@Override
	public void execute(IProject project, IProjectFacetVersion fv,
			Object config, IProgressMonitor monitor) throws CoreException {
		super.execute(project, fv, config, monitor);
		IDataModel facetDataModel = (IDataModel) config;
		IDataModel masterDataModel = (IDataModel) facetDataModel
				.getProperty(FacetInstallDataModelProvider.MASTER_PROJECT_DM);
		String configFolder = BirtWizardUtil.getConfigFolder(masterDataModel);
		String platformFolder = configFolder + "/WEB-INF/platform/plugins"; //$NON-NLS-1$
		BirtCoreActivator.copyPlugin(project,"org.jboss.tools.birt.oda",platformFolder,monitor); //$NON-NLS-1$
		//copyPlugin(project,"org.hibernate.eclipse",platformFolder,monitor);
		//copyPlugin(project,"org.eclipse.ui.console",platformFolder,monitor);
		//copyPlugin(project,"org.eclipse.jface",platformFolder,monitor);
		
		
	}

	@Override
	protected void processConfiguration(final IProject project,
			final Map birtProperties, final IProgressMonitor monitor)
			throws CoreException {
		// Simple OverwriteQuery
		final SimpleImportOverwriteQuery query = new SimpleImportOverwriteQuery();
		IModelProvider modelProvider = ModelProviderManager
				.getModelProvider(project);
		IPath modelPath = new Path("WEB-INF").append("web.xml"); //$NON-NLS-1$ //$NON-NLS-2$
		boolean exists = project.getProjectRelativePath().append(modelPath)
				.toFile().exists();
		if (isWebApp25(modelProvider.getModelObject()) && !exists) {
			modelPath = IModelProvider.FORCESAVE;
		}
		final IBirtUtil util = createBirtUtil(modelProvider.getModelObject());
		modelProvider.modify(new Runnable() {
			public void run() {
				util.configureWebApp((WebAppBean) birtProperties
						.get(EXT_WEBAPP), project, query, monitor);
				util.configureContextParam((Map) birtProperties
						.get(EXT_CONTEXT_PARAM), project, query, monitor);
				util.configureListener((Map) birtProperties.get(EXT_LISTENER),
						project, query, monitor);

				util.configureServlet((Map) birtProperties.get(EXT_SERVLET),
						project, query, monitor);

				util.configureServletMapping((Map) birtProperties
						.get(EXT_SERVLET_MAPPING), project, query, monitor);

				util.configureFilter((Map) birtProperties.get(EXT_FILTER),
						project, query, monitor);

				util.configureFilterMapping((Map) birtProperties
						.get(EXT_FILTER_MAPPING), project, query, monitor);

				util.configureTaglib((Map) birtProperties.get(EXT_TAGLIB),
						project, query, monitor);
			}
		}, modelPath);

	}

	private IBirtUtil createBirtUtil(Object webApp) {
		if (isWebApp25(webApp)) {
			return new JBossBirtUtil25();
		}
		return new JBossBirtUtil();
	}

	private boolean isWebApp25(final Object webApp) {
		if (webApp instanceof WebApp
				&& ((WebApp) webApp).getVersion() == WebAppVersionType._25_LITERAL)
			return true;
		return false;
	}
}
