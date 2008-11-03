package org.jboss.tools.smooks.ui.gef.commandprocessor;

import org.eclipse.gef.commands.Command;

/**
 * 
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public interface ICommandProcessor {
	public void processGEFCommand(Command gefCommand);

	public void processEMFCommand(
			org.eclipse.emf.common.command.Command emfCommand);
}
