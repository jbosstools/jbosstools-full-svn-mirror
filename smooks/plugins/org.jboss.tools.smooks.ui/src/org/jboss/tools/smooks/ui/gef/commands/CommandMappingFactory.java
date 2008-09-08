/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.commands;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.jboss.tools.smooks.ui.gef.commandprocessor.ICommandProcessor;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.modelparser.IStructuredModelParser;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public class CommandMappingFactory implements ICommandMappingFactory {
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jboss.tools.smooks.ui.gef.commands.ICommandMappingFactory#
	 * createCreationStructuredModelCommand(java.lang.Object, java.lang.Object,
	 * org.eclipse.gef.GraphicalEditPart)
	 */
	public Command createCreationStructuredModelCommand(CreateRequest request,
			GraphicalEditPart rootEditPart) {
		try {
			Object modelType = request.getNewObjectType();
			Object newObject = request.getNewObject();
			ICommandProcessor processor = this.getCommandProcessor(request,
					rootEditPart);
			IStructuredModelParser parser = this.getStructuredModelParser(
					modelType, newObject, rootEditPart);
			CreateStructuredDataModelCommand command = new CreateStructuredDataModelCommand();
			command.setProcessor(processor);
			command.setParser(parser);
			command.setParent((AbstractStructuredDataModel) rootEditPart
					.getModel());
			command.setHostEditPart(rootEditPart);
			command.setRequest(request);
			return command;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new NullCommand();
	}

	protected IStructuredModelParser getStructuredModelParser(Object modelType,
			Object newObject, GraphicalEditPart rootEditPart) {
//		if (modelType == JavaBeanModel.class) {
//			return new JavaBeanParser();
//		}
		return null;
	}

	protected ICommandProcessor getCommandProcessor(CreateRequest request,
			GraphicalEditPart rootEditPart) {
//		if (request.getNewObjectType() == JavaBeanModel.class) {
//			return new JavaBeanModelCommandProcessor();
//		}
		return null;
	}

	private class NullCommand extends Command {

	}
}
