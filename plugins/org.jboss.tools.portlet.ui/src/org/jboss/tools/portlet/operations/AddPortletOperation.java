package org.jboss.tools.portlet.operations;

import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DESCRIPTION;
import static org.eclipse.jst.j2ee.internal.web.operations.INewWebClassDataModelProperties.DISPLAY_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.TITLE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.VIEW_MODE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.EDIT_MODE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.HELP_MODE;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.INSTANCE_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.WINDOW_NAME;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.IF_EXISTS;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PAGE_REGION;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PARENT_PORTAL;
import static org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties.PORTLET_HEIGHT;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.j2ee.internal.common.operations.NewJavaEEArtifactClassOperation;
import org.eclipse.jst.j2ee.internal.web.operations.AddWebClassOperation;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.operation.ArtifactEditProviderOperation;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.format.FormatProcessorXML;
import org.jboss.tools.portlet.core.IPortletConstants;
import org.jboss.tools.portlet.core.PortletCoreActivator;
import org.jboss.tools.portlet.ui.INewPortletClassDataModelProperties;
import org.jboss.tools.portlet.ui.PortletUIActivator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * @author snjeza
 */
public class AddPortletOperation extends AddWebClassOperation {

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
		return new NewPortletClassOperation(getDataModel());
	}

	protected void generateMetaData(IDataModel aModel, String qualifiedClassName) {
		// update the portlet.xml file
		updatePortletXml(aModel); 

		// generate/update portlet-instances.xml
		updatePortletInstance(aModel);
		
		// generate/update *.object.xml
		updatePortletObject(aModel);
	}

	private void updatePortletObject(IDataModel model) {
		
		String name = model.getStringProperty(NAME);
		String instanceId = model.getStringProperty(INSTANCE_NAME);
		String windowName = model.getStringProperty(WINDOW_NAME);
		String ifExists = model.getStringProperty(IF_EXISTS);
		String parent = model.getStringProperty(PARENT_PORTAL);;
		String region = model.getStringProperty(PAGE_REGION);
		String height = model.getStringProperty(PORTLET_HEIGHT);
		
		String fileName = name.toLowerCase() + "-object.xml";
		
		IProject project = getTargetProject();
		IVirtualComponent component = ComponentCore.createComponent(project);
		IVirtualFile portletVirtualFile = component.getRootFolder().getFolder("WEB-INF").getFile(
				fileName);
		
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
			
			addNode(document,deployment,"if-exists",ifExists);
			addNode(document,deployment,"parent-ref",parent);
			
			Element window = addNode(document,deployment,"window",null);
			
			addNode(document,window,"window-name",windowName);
			addNode(document,window,"instance-ref",instanceId);
			addNode(document,window,"region",region);
			addNode(document,window,"height",height);
			
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
		IVirtualComponent component = ComponentCore.createComponent(project);
		IVirtualFile portletVirtualFile = component.getRootFolder().getFile(
				IPortletConstants.CONFIG_PATH);

		if (!portletVirtualFile.getUnderlyingFile().exists()) {
			PortletCoreActivator.getDefault().log(new RuntimeException("The portlet.xml file doesn't exist"));
			return;
		}

		IFile portletFile = portletVirtualFile.getUnderlyingFile();
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
