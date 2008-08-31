package org.jboss.tools.portlet.operations;

import static org.eclipse.jst.j2ee.internal.web.operations.INewServletClassDataModelProperties.INIT_PARAM;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DESCRIPTION;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DISPLAY_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.ADD_JBOSS_APP;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.ADD_JBOSS_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.ADD_PORTLET;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.COPY_JSF_TEMPLATES;
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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jst.j2ee.internal.common.operations.NewJavaEEArtifactClassOperation;
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
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.PortletCoreActivator;
import org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties;
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
		boolean addJBossApp = model.getBooleanProperty(ADD_JBOSS_APP);
		if (addJBossApp) {
			updateJBossApp(aModel);
		}
		boolean addJBossPortlet = model.getBooleanProperty(ADD_JBOSS_PORTLET);
		if (addJBossPortlet) {
			updateJBossPortlet(aModel);
		}
		
		boolean copyJSFTemplates = model.getBooleanProperty(COPY_JSF_TEMPLATES);
		if (copyJSFTemplates) {
			try {
				copyJSFTemplates(aModel);
			} catch (Exception e) {
				PortletUIActivator.log(e);
			}
		}
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
			Element portlet = addNode(document,element,"portlet",null);
			addNode(document,portlet,"portlet-name",name);
			addNode(document,portlet,"header-content",null);
			
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
		IVirtualFolder jsfFolder = component.getRootFolder().getFolder("jsf");
		if (!jsfFolder.exists()) {
			jsfFolder.create(IResource.FORCE, new NullProgressMonitor());
		}
		IContainer folder = jsfFolder.getUnderlyingFolder();

		Bundle bundle = Platform.getBundle(PortletUIActivator.PLUGIN_ID);
		URL jsfURL = bundle.getEntry("/resources/jsf");
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
			
			NodeList appNameNodes = element.getElementsByTagName("app-name");
			if (appNameNodes.getLength() <= 0) {
				String appName = model.getStringProperty(JBOSS_APP);
				addNode(document, element, "app-name", appName);
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
			
			Element deployment = document.createElement("deployment");
			element.appendChild(deployment);
			
			addNode(document,deployment,"parent-ref",parent);
			addNode(document,deployment,"if-exists",ifExists);
			
			Element page = null;
			if (pageName != null && pageName.trim().length() > 0) {
				page = addNode(document,deployment,"page",null);
				addNode(document,page,"page-name",pageName);
			} else {
				page=deployment;
			}
			
			Element window = addNode(document,page,"window",null);
			
			addNode(document,window,"window-name",windowName);
			addNode(document,window,"instance-ref",instanceId);
			addNode(document,window,"region",region);
			addNode(document,window,"height",height);
			addNode(document,window,"initial-window-state",initialWindowState);
			
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
			
			Element deployment = document.createElement("deployment");
			element.appendChild(deployment);
			
			Element instance = addNode(document,deployment,"instance",null);
			addNode(document,instance,"instance-id",instanceId);
			addNode(document,instance,"portlet-ref",name);
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
			
			Element portlet = document.createElement("portlet");
			element.appendChild(portlet);
			// description
			if (description != null && description.trim().length() > 0) {
				addNode(document,portlet,"description",description);
			}
			
			// portlet-name
			addNode(document,portlet,"portlet-name",name);
			
			// display-name
			if (displayName != null && displayName.trim().length() > 0) {
				addNode(document,portlet,"display-name",displayName);
			}
			// portlet-class
			addNode(document,portlet,"portlet-class",className);
			
			// init-param
			List initParamList = (List) aModel.getProperty(INIT_PARAM);
			if (initParamList != null) {
				for (Iterator iterator = initParamList.iterator(); iterator
						.hasNext();) {
					String[] arrayString = (String[]) iterator.next();
					Element initParam = addNode(document,portlet,"init-param",null);
					addNode(document,initParam,"name",arrayString[0]);
					addNode(document,initParam,"value",arrayString[1]);
					if (arrayString[2] != null && arrayString[2].length() > 0) {
						addNode(document,initParam,"description",arrayString[2]);
					}
				}
			}
			// supports
			Element supports = addNode(document,portlet,"supports",null);
			
			addNode(document,supports,"mime-type","text/html");
			if (aModel.getBooleanProperty(VIEW_MODE)) {
				addNode(document,supports,"portlet-mode","VIEW");
			}
			if (aModel.getBooleanProperty(EDIT_MODE)) {
				addNode(document,supports,"portlet-mode","EDIT");
			}
			if (aModel.getBooleanProperty(HELP_MODE)) {
				addNode(document,supports,"portlet-mode","HELP");
			}
			
			// portlet-info
			Element portletInfo = addNode(document,portlet,"portlet-info",null);
			addNode(document,portletInfo,"title", title);
			
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
