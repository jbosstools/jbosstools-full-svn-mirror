package org.jboss.tools.portlet.operations;

import static org.eclipse.jst.j2ee.internal.web.operations.INewServletClassDataModelProperties.INIT_PARAM;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DESCRIPTION;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DISPLAY_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.ADD_JBOSS_APP;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.ADD_JBOSS_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.ADD_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.COPY_JSF_TEMPLATES;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.CONFIGURE_GATEIN_PARAMETERS;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.EDIT_MODE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.HELP_MODE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.IF_EXISTS;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.INITIAL_WINDOW_STATE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.INSTANCE_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.IS_JSF_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.IS_SEAM_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.JBOSS_APP;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PAGE_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PAGE_REGION;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PARENT_PORTAL;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PORTLET_HEIGHT;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.TITLE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.VIEW_MODE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.WINDOW_NAME;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jst.j2ee.internal.common.operations.NewJavaEEArtifactClassOperation;
import org.eclipse.jst.j2ee.model.IModelProvider;
import org.eclipse.jst.javaee.web.WebApp;
import org.eclipse.jst.javaee.web.WebAppVersionType;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.FileSystemStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.operation.ArtifactEditProviderOperation;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.common.frameworks.internal.enablement.nonui.WFTWrappedException;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.jboss.tools.portlet.core.IJBossWebUtil;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.JBossWebUtil;
import org.jboss.tools.portlet.core.JBossWebUtil25;
import org.jboss.tools.portlet.core.PortletCoreActivator;
import org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties;
import org.jboss.tools.portlet.ui.Messages;
import org.jboss.tools.portlet.ui.PortletUIActivator;
import org.jboss.tools.portlet.ui.internal.wizard.action.xpl.AddWebClassOperationEx;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author snjeza
 */
public class AddPortletOperation extends AddWebClassOperationEx {

	public static final IOverwriteQuery OVERWRITE_NO_QUERY = new IOverwriteQuery()
    {
      public String queryOverwrite(String pathString)
      {
        return IOverwriteQuery.NO_ALL;
      }
    };
	/**
	 * This is the constructor which should be used when creating the operation.
	 * It will not accept null parameter. It will not return null.
	 * 
	 * @see ArtifactEditProviderOperation#ArtifactEditProviderOperation(IDataModel)
	 * 
	 * @param dataModel
	 * @return AddPortletOperation
	 */
	public AddPortletOperation(IDataModel dataModel) {
		super(dataModel);
	}

	@Override
	protected NewJavaEEArtifactClassOperation getNewClassOperation() {
		boolean isJSFPortlet = model.getBooleanProperty(IS_JSF_PORTLET);
		boolean isSeamPortlet = model.getBooleanProperty(IS_SEAM_PORTLET);
		
		if (!isJSFPortlet && !isSeamPortlet) {
			return new NewPortletClassOperation(getDataModel());
		}
		NewJavaEEArtifactClassOperation op = new NewJavaEEArtifactClassOperation(getDataModel()) {

			@Override
			protected void generateUsingTemplates(IProgressMonitor monitor,
					IPackageFragment fragment) throws WFTWrappedException,
					CoreException {
				
			}

			@Override
			public IStatus doExecute(IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				return Status.OK_STATUS;
			}
			
		};
		return op;
	}

	protected void generateMetaData(IDataModel aModel, String qualifiedClassName) {
		// update the portlet.xml file
		boolean isPortletProject = PortletUIActivator.isPortletProject(aModel);
		if (!isPortletProject) {
			return;
		}
		updatePortletXml(aModel);

		boolean addPortlet = model.getBooleanProperty(ADD_PORTLET);
		if (addPortlet) {
			// generate/update portlet-instances.xml
			updatePortletInstance(aModel);

			// generate/update *.object.xml
			updatePortletObject(aModel);
		}
		boolean isJSFPortlet = model.getBooleanProperty(IS_JSF_PORTLET);
		boolean isSeamPortlet = model.getBooleanProperty(IS_SEAM_PORTLET);
		if (!isJSFPortlet && !isSeamPortlet) {
			return;
		}
		if (addPortlet) {
			boolean addJBossApp = model.getBooleanProperty(ADD_JBOSS_APP);
			if (addJBossApp) {
				updateJBossApp(aModel);
			}
			boolean addJBossPortlet = model
					.getBooleanProperty(ADD_JBOSS_PORTLET);
			if (addJBossPortlet) {
				updateJBossPortlet(aModel);
			}
		}
		
		boolean copyJSFTemplates = model.getBooleanProperty(COPY_JSF_TEMPLATES);
		if (copyJSFTemplates) {
			try {
				copyJSFTemplates(aModel);
			} catch (Exception e) {
				PortletUIActivator.log(e);
			}
		}
		boolean configureGateIn = model.getBooleanProperty(CONFIGURE_GATEIN_PARAMETERS);
		if (configureGateIn) {
			configureGateInParameters(aModel);
		}
	}

	private void configureGateInParameters(IDataModel aModel) {
		final IProject project = getTargetProject();
		final IModelProvider provider = PortletCoreActivator
		.getModelProvider(project);
		IPath modelPath = new Path("WEB-INF").append("web.xml"); //$NON-NLS-1$ //$NON-NLS-2$
		boolean exists = project.getProjectRelativePath().append(modelPath).toFile().exists();
		if (isWebApp25(provider.getModelObject()) && !exists) {
			modelPath = IModelProvider.FORCESAVE;
		}
		provider.modify(new Runnable() {
			public void run() {
				IJBossWebUtil util = null;

				if (isWebApp25(provider.getModelObject())) {
					util = new JBossWebUtil25();
				} else {
					util = new JBossWebUtil();
				}
				String name = "org.jboss.portletbridge.WRAP_SCRIPTS"; //$NON-NLS-1$
				String value = "true"; //$NON-NLS-1$
				String description = null;
				util.configureContextParam(project, new NullProgressMonitor(), name, value, description);

			}
		}, modelPath);
	}

	private boolean isWebApp25(final Object webApp) {
		if (webApp instanceof WebApp
				&& ((WebApp) webApp).getVersion() == WebAppVersionType._25_LITERAL)
			return true;
		return false;
	}
	
	private void updateJBossPortlet(IDataModel model) {
		IProject project = getTargetProject();
		IVirtualComponent component = ComponentCore.createComponent(project);
		IVirtualFile portletVirtualFile = component.getRootFolder().getFile(
				IPortletConstants.JBOSS_PORTLET_FILE);
		
		if (!portletVirtualFile.getUnderlyingFile().exists()) {
			try {
				PortletCoreActivator.createJBossPortlet(project,portletVirtualFile.getUnderlyingFile());
			} catch (Exception e) {
				PortletCoreActivator.log(e);
				return;
			}
		}
		
		IFile portletFile = portletVirtualFile.getUnderlyingFile();
		IDOMModel domModel = null;
		try {
			domModel = (IDOMModel) StructuredModelManager.getModelManager()
					.getModelForEdit(portletFile);
			Document document = domModel.getDocument();
			Element element = document.getDocumentElement();
			
			String name = model.getStringProperty(NAME);
			Element portlet = addNode(document,element,"portlet",null); //$NON-NLS-1$
			addNode(document,portlet,"portlet-name",name); //$NON-NLS-1$
			addNode(document,portlet,"header-content",null); //$NON-NLS-1$
			
			domModel.save();
		} catch (Exception e) {
			PortletCoreActivator.getDefault().log(e);
		} finally {
			if (domModel != null) {
				domModel.releaseFromEdit();
			}
		}
		
		try {
			new FormatProcessorXML().formatFile(portletFile);
		} catch (Exception e) {
			// ignore
		}
	}

	private void copyJSFTemplates(IDataModel model) throws Exception  {
		IProject project = getTargetProject();
		IVirtualComponent component = ComponentCore.createComponent(project);
		IVirtualFolder jsfFolder = component.getRootFolder().getFolder("jsf"); //$NON-NLS-1$
		if (!jsfFolder.exists()) {
			jsfFolder.create(IResource.FORCE, new NullProgressMonitor());
		}
		IContainer folder = jsfFolder.getUnderlyingFolder();

		Bundle bundle = Platform.getBundle(PortletUIActivator.PLUGIN_ID);
		URL jsfURL = bundle.getEntry("/resources/jsf"); //$NON-NLS-1$
		String jsfFolderName = FileLocator.toFileURL(jsfURL).getFile();
		File source = new File(jsfFolderName);
		File[] files = source.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.isFile();
			}

		});
		List<File> filesToImport = Arrays.asList(files);
		ImportOperation importOperation = new ImportOperation(folder
				.getFullPath(), source, FileSystemStructureProvider.INSTANCE,
				OVERWRITE_NO_QUERY, filesToImport);
		importOperation.setCreateContainerStructure(false);
		IProgressMonitor monitor = new NullProgressMonitor();
		importOperation.run(monitor);
	}

	private void updateJBossApp(IDataModel model) {
		IProject project = getTargetProject();
		IVirtualComponent component = ComponentCore.createComponent(project);
		IVirtualFile portletVirtualFile = component.getRootFolder().getFile(
				IPortletConstants.JBOSS_APP_FILE);
		
		if (!portletVirtualFile.getUnderlyingFile().exists()) {
			try {
				PortletCoreActivator.createJBossApp(project,portletVirtualFile.getUnderlyingFile());
			} catch (Exception e) {
				PortletCoreActivator.log(e);
				return;
			}
		}
		
		IFile portletFile = portletVirtualFile.getUnderlyingFile();
		IDOMModel domModel = null;
		try {
			domModel = (IDOMModel) StructuredModelManager.getModelManager()
					.getModelForEdit(portletFile);
			Document document = domModel.getDocument();
			Element element = document.getDocumentElement();
			
			NodeList appNameNodes = element.getElementsByTagName("app-name"); //$NON-NLS-1$
			if (appNameNodes.getLength() <= 0) {
				String appName = model.getStringProperty(JBOSS_APP);
				addNode(document, element, "app-name", appName); //$NON-NLS-1$
				domModel.save();
			}
		} catch (Exception e) {
			PortletCoreActivator.getDefault().log(e);
		} finally {
			if (domModel != null) {
				domModel.releaseFromEdit();
			}
		}
		
		try {
			new FormatProcessorXML().formatFile(portletFile);
		} catch (Exception e) {
			// ignore
		}
	}

	private void updatePortletObject(IDataModel model) {
		
		String instanceId = model.getStringProperty(INSTANCE_NAME);
		String windowName = model.getStringProperty(WINDOW_NAME);
		String pageName = model.getStringProperty(PAGE_NAME);
		String ifExists = model.getStringProperty(IF_EXISTS);
		String parent = model.getStringProperty(PARENT_PORTAL);;
		String region = model.getStringProperty(PAGE_REGION);
		String height = model.getStringProperty(PORTLET_HEIGHT);
		String initialWindowState = model.getStringProperty(INITIAL_WINDOW_STATE);
		
		IProject project = getTargetProject();
		IVirtualComponent component = ComponentCore.createComponent(project);
		IVirtualFile portletVirtualFile = component.getRootFolder().getFile(
				IPortletConstants.PORTLET_OBJECT_FILE);
		
		if (!portletVirtualFile.getUnderlyingFile().exists()) {
			try {
				PortletCoreActivator.createPortletObject(project,portletVirtualFile.getUnderlyingFile());
			} catch (Exception e) {
				PortletCoreActivator.log(e);
				return;
			}
		}
		
		IFile portletFile = portletVirtualFile.getUnderlyingFile();
		IDOMModel domModel = null;
		try {
			domModel = (IDOMModel) StructuredModelManager.getModelManager()
					.getModelForEdit(portletFile);
			Document document = domModel.getDocument();
			Element element = document.getDocumentElement();
			
			Element deployment = document.createElement("deployment"); //$NON-NLS-1$
			element.appendChild(deployment);
			
			addNode(document,deployment,"parent-ref",parent); //$NON-NLS-1$
			addNode(document,deployment,"if-exists",ifExists); //$NON-NLS-1$
			
			Element page = null;
			if (pageName != null && pageName.trim().length() > 0) {
				page = addNode(document,deployment,"page",null); //$NON-NLS-1$
				addNode(document,page,"page-name",pageName); //$NON-NLS-1$
			} else {
				page=deployment;
			}
			
			Element window = addNode(document,page,"window",null); //$NON-NLS-1$
			
			addNode(document,window,"window-name",windowName); //$NON-NLS-1$
			addNode(document,window,"instance-ref",instanceId); //$NON-NLS-1$
			addNode(document,window,"region",region); //$NON-NLS-1$
			addNode(document,window,"height",height); //$NON-NLS-1$
			addNode(document,window,"initial-window-state",initialWindowState); //$NON-NLS-1$
			
			domModel.save();
		} catch (Exception e) {
			PortletCoreActivator.getDefault().log(e);
		} finally {
			if (domModel != null) {
				domModel.releaseFromEdit();
			}
		}
		
		try {
			new FormatProcessorXML().formatFile(portletFile);
		} catch (Exception e) {
			// ignore
		}
	}
	private void updatePortletInstance(IDataModel model) {
		
		
		String name = model.getStringProperty(NAME);
		String instanceId = model.getStringProperty(INSTANCE_NAME);
		
		IProject project = getTargetProject();
		IVirtualComponent component = ComponentCore.createComponent(project);
		IVirtualFile portletVirtualFile = component.getRootFolder().getFile(
				IPortletConstants.PORTLET_INSTANCES_FILE);
		
		if (!portletVirtualFile.getUnderlyingFile().exists()) {
			try {
				PortletCoreActivator.createPortletInstances(project,portletVirtualFile.getUnderlyingFile());
			} catch (Exception e) {
				PortletCoreActivator.log(e);
				return;
			}
		}
		
		IFile portletFile = portletVirtualFile.getUnderlyingFile();
		IDOMModel domModel = null;
		try {
			domModel = (IDOMModel) StructuredModelManager.getModelManager()
					.getModelForEdit(portletFile);
			Document document = domModel.getDocument();
			Element element = document.getDocumentElement();
			
			Element deployment = document.createElement("deployment"); //$NON-NLS-1$
			element.appendChild(deployment);
			
			Element instance = addNode(document,deployment,"instance",null); //$NON-NLS-1$
			addNode(document,instance,"instance-id",instanceId); //$NON-NLS-1$
			addNode(document,instance,"portlet-ref",name); //$NON-NLS-1$
			domModel.save();
		} catch (Exception e) {
			PortletCoreActivator.getDefault().log(e);
		} finally {
			if (domModel != null) {
				domModel.releaseFromEdit();
			}
		}
		
		try {
			new FormatProcessorXML().formatFile(portletFile);
		} catch (Exception e) {
			// ignore
		}
	}

	private void updatePortletXml(IDataModel aModel) {
		String displayName = aModel.getStringProperty(DISPLAY_NAME);
		String name = aModel.getStringProperty(NAME);
		String title = aModel.getStringProperty(TITLE);
		String description = aModel.getStringProperty(DESCRIPTION);
		String className = aModel.getStringProperty(INewPortletClassDataModelProperties.QUALIFIED_CLASS_NAME);

		IProject project = getTargetProject();
		IFile portletFile = PortletUIActivator.getPortletXmlFile(project);
		if (portletFile == null) {
			return;
		}
		IDOMModel domModel = null;
		try {
			domModel = (IDOMModel) StructuredModelManager.getModelManager()
					.getModelForEdit(portletFile);
			Document document = domModel.getDocument();
			Element element = document.getDocumentElement();
			
			Element portlet = document.createElement("portlet"); //$NON-NLS-1$
			element.appendChild(portlet);
			// description
			if (description != null && description.trim().length() > 0) {
				addNode(document,portlet,"description",description); //$NON-NLS-1$
			}
			
			// portlet-name
			addNode(document,portlet,"portlet-name",name); //$NON-NLS-1$
			
			// display-name
			if (displayName != null && displayName.trim().length() > 0) {
				addNode(document,portlet,"display-name",displayName); //$NON-NLS-1$
			}
			// portlet-class
			addNode(document,portlet,"portlet-class",className); //$NON-NLS-1$
			
			// init-param
			List initParamList = (List) aModel.getProperty(INIT_PARAM);
			if (initParamList != null) {
				for (Iterator iterator = initParamList.iterator(); iterator
						.hasNext();) {
					String[] arrayString = (String[]) iterator.next();
					Element initParam = addNode(document,portlet,"init-param",null); //$NON-NLS-1$
					if (arrayString[2] != null && arrayString[2].length() > 0) {
						addNode(document,initParam,"description",arrayString[2]); //$NON-NLS-1$
					}
					addNode(document,initParam,"name",arrayString[0]); //$NON-NLS-1$
					addNode(document,initParam,"value",arrayString[1]); //$NON-NLS-1$
				}
			}
			// supports
			Element supports = addNode(document,portlet,"supports",null); //$NON-NLS-1$
			
			addNode(document,supports,"mime-type","text/html"); //$NON-NLS-1$ //$NON-NLS-2$
			if (aModel.getBooleanProperty(VIEW_MODE)) {
				addNode(document,supports,"portlet-mode","VIEW"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (aModel.getBooleanProperty(EDIT_MODE)) {
				addNode(document,supports,"portlet-mode","EDIT"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (aModel.getBooleanProperty(HELP_MODE)) {
				addNode(document,supports,"portlet-mode","HELP"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			
			// portlet-info
			Element portletInfo = addNode(document,portlet,"portlet-info",null); //$NON-NLS-1$
			addNode(document,portletInfo,"title", title); //$NON-NLS-1$
			
			domModel.save();
			
		} catch (Exception e) {
			PortletCoreActivator.getDefault().log(e);
		} finally {
			if (domModel != null) {
				domModel.releaseFromEdit();
			}
		}
		
		try {
			new FormatProcessorXML().formatFile(portletFile);
		} catch (Exception e) {
			// ignore
		}
	}

	private Element addNode(Document document,Element element, String tagName, String value) {
		Element node = document.createElement(tagName);
		if (value != null) {
			Text text = document.createTextNode(value);
			node.appendChild(text);
		}
		element.appendChild(node);
		return node;
	}

}
