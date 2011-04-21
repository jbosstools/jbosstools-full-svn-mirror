package org.jboss.tools.smooks.ui.gef.commandprocessor;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.CreateRequest;

/**
 * 
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public interface ICommandProcessor {
	public Object getNewModel(CreateRequest request,
			GraphicalEditPart rootEditPart);
}
