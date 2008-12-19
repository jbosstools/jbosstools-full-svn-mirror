package org.jboss.tools.smooks.ui.gef.commands;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * 
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public interface ICommandMappingFactory {

	/**
	 * 
	 * @param modelType
	 * @param newObject
	 * @param rootEditPart
	 * @return
	 */
	public abstract Command createCreationStructuredModelCommand(CreateRequest request, GraphicalEditPart rootEditPart);

}