/**
 * 
 */
package org.jboss.tools.smooks.xml.model;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.jboss.tools.smooks.model.ResourceConfigType;
import org.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor;
import org.jboss.tools.smooks.ui.gef.commands.CreateConnectionCommand;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.GraphRootModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.utils.UIUtils;
import org.jboss.tools.smooks.xml.ui.XMLPropertiesSection;
import org.jboss.tools.smooks.xml2xml.XML2XMLGraphicalModelListener;

/**
 * @author Dart
 * 
 */
public class XMLConnectionCommandProcessor implements ICommandProcessor {
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor#
	 * processEMFCommand(org.eclipse.emf.common.command.Command,
	 * org.jboss.tools.
	 * smooks.ui.modelparser.SmooksConfigurationFileGenerateContext)
	 */
	public boolean processEMFCommand(Command emfCommand,
			SmooksConfigurationFileGenerateContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor#
	 * processGEFCommand(org.eclipse.gef.commands.Command,
	 * org.jboss.tools.smooks
	 * .ui.modelparser.SmooksConfigurationFileGenerateContext)
	 */
	public boolean processGEFCommand(
			org.eclipse.gef.commands.Command gefCommand,
			SmooksConfigurationFileGenerateContext context) {
		if (gefCommand instanceof CreateConnectionCommand) {
			CreateConnectionCommand command = (CreateConnectionCommand) gefCommand;
			IConnectableModel source = command.getSource();
			IConnectableModel target = command.getTarget();
			// allow only one connection
			if (!source.getModelSourceConnections().isEmpty()
					|| !target.getModelTargetConnections().isEmpty())
				return false;
			// only allow line to the Tag
			if (!(((AbstractStructuredDataModel) target)
					.getReferenceEntityModel() instanceof TagObject))
				return false;

			if (((AbstractStructuredDataModel) source)
					.getReferenceEntityModel() instanceof TagObject
					&& ((AbstractStructuredDataModel) target)
							.getReferenceEntityModel() instanceof TagObject) {
				PropertyModel property = new PropertyModel();
				property.setName(XMLPropertiesSection.MAPPING_TYPE);
				property.setValue(XMLPropertiesSection.MAPPING);
				command.addPropertyModel(property);
			}
			if (((AbstractStructuredDataModel) source)
					.getReferenceEntityModel() instanceof TagPropertyObject) {
				PropertyModel property = new PropertyModel();
				property.setName(XMLPropertiesSection.MAPPING_TYPE);
				property.setValue(XMLPropertiesSection.BINDING);
				command.addPropertyModel(property);
				ResourceConfigType resourceConfig = findFirstRelateResourceConfig(
						context.getGraphicalRootModel(),
						(AbstractStructuredDataModel) target);
				if (resourceConfig == null)
					return false;
				PropertyModel property1 = new PropertyModel();
				property1
						.setName(XML2XMLGraphicalModelListener.PRO_REFERENCE_RESOURCE_CONFIG);
				property1.setValue(resourceConfig);
				command.addPropertyModel(property1);
			}

		}
		return true;
	}

	private ResourceConfigType findFirstRelateResourceConfig(
			GraphRootModel root, AbstractStructuredDataModel target) {
		AbstractXMLObject model = (AbstractXMLObject) target
				.getReferenceEntityModel();
		AbstractXMLObject parent = model.getParent();
		IConnectableModel parentGraph = (IConnectableModel) UIUtils
				.findGraphModel(root, parent);
		List connections = parentGraph.getModelTargetConnections();
		ResourceConfigType resourceConfig = null;
		while (resourceConfig == null && parentGraph != null) {
			for (Iterator iterator = connections.iterator(); iterator.hasNext();) {
				LineConnectionModel connection = (LineConnectionModel) iterator
						.next();
				Object mt = connection
						.getProperty(XMLPropertiesSection.MAPPING_TYPE);
				if (XMLPropertiesSection.MAPPING.equals(mt)) {
					resourceConfig = (ResourceConfigType) connection
							.getProperty(XML2XMLGraphicalModelListener.PRO_REFERENCE_RESOURCE_CONFIG);
					break;
				}
			}
			parent = parent.getParent();
			parentGraph = (IConnectableModel) UIUtils.findGraphModel(root,
					parent);
			if (parentGraph != null) {
				connections = parentGraph.getModelTargetConnections();
			}
		}
		return resourceConfig;
	}

}
