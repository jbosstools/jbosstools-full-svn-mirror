/**
 * 
 */
package org.jboss.tools.smooks.xml.model;

import org.eclipse.emf.common.command.Command;
import org.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor;
import org.jboss.tools.smooks.ui.gef.commands.CreateConnectionCommand;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;
import org.jboss.tools.smooks.xml.ui.XMLPropertiesSection;

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
			if(!source.getModelSourceConnections().isEmpty() || !target.getModelTargetConnections().isEmpty()) return false;
			if (((AbstractStructuredDataModel) source)
					.getReferenceEntityModel() instanceof TagObject
					&& ((AbstractStructuredDataModel)target).getReferenceEntityModel() instanceof TagObject) {
				PropertyModel property = new PropertyModel();
				property.setName(XMLPropertiesSection.MAPPING_TYPE);
				property.setValue(XMLPropertiesSection.MAPPING);
				command.addPropertyModel(property);
			}
			if (((AbstractStructuredDataModel) source).getReferenceEntityModel() instanceof TagPropertyObject
					|| ((AbstractStructuredDataModel) source).getReferenceEntityModel() instanceof TagPropertyObject) {
				PropertyModel property = new PropertyModel();
				property.setName(XMLPropertiesSection.MAPPING_TYPE);
				property.setValue(XMLPropertiesSection.BINDING);
				command.addPropertyModel(property);
			}

		}
		return true;
	}

}
