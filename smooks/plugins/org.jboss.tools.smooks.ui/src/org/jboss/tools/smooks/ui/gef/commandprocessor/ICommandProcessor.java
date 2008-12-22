package org.jboss.tools.smooks.ui.gef.commandprocessor;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * 
 * @author Dart Peng
 * 
 * @CreateTime Jul 22, 2008
 */
public interface ICommandProcessor {
	public void processGEFCommand(Command gefCommand , SmooksConfigurationFileGenerateContext context);

	public void processEMFCommand(org.eclipse.emf.common.command.Command emfCommand, SmooksConfigurationFileGenerateContext context);
}
