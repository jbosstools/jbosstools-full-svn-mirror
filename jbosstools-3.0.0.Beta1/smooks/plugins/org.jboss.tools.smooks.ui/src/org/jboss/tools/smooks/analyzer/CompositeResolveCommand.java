/**
 * 
 */
package org.jboss.tools.smooks.analyzer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart
 *
 */
public class CompositeResolveCommand extends ResolveCommand {

	private List<ResolveCommand> commandList = new ArrayList<ResolveCommand>();
	
	public CompositeResolveCommand(
			SmooksConfigurationFileGenerateContext context) {
		super(context);
	}
	
	
	public void addCommand(ResolveCommand command){
		commandList.add(command);
	}

	public void removeCommand(ResolveCommand command){
		commandList.remove(command);
	}

	public boolean isEmpty(){
		if(commandList == null) return true;
		return commandList.isEmpty();
	}

	@Override
	public void execute() throws Exception {
		if(commandList == null) return;
		for (Iterator<ResolveCommand> iterator = commandList.iterator(); iterator.hasNext();) {
			ResolveCommand command = (ResolveCommand) iterator.next();
			command.execute();
		}
	}
	
	
}
