/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dart
 *
 */
public class CompositeCommand extends Command {

	private List<ICommand> commandList = null;
	
	
	public List<ICommand> getCommandList() {
		if(commandList == null) commandList = new ArrayList<ICommand>();
		return commandList;
	}
	
	public void appendCommand(ICommand command){
		getCommandList().add(command);
	}
	
	public void removeCommand(ICommand command){
		getCommandList().remove(command);
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.model.command.ICommand#execute()
	 */
	public void execute() {
		List<ICommand> l = getCommandList();
		for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
			ICommand iCommand = (ICommand) iterator.next();
			iCommand.execute();
		}
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.model.command.ICommand#canExecute()
	 */
	public boolean canExecute() {
		List<ICommand> l = getCommandList();
		for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
			ICommand iCommand = (ICommand) iterator.next();
			if(!iCommand.canExecute()) return false; 
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.model.command.ICommand#canRedo()
	 */
	public boolean canRedo() {
		List<ICommand> l = getCommandList();
		for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
			ICommand iCommand = (ICommand) iterator.next();
			if(!iCommand.canRedo()) return false; 
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.model.command.ICommand#canUndo()
	 */
	public boolean canUndo() {
		List<ICommand> l = getCommandList();
		for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
			ICommand iCommand = (ICommand) iterator.next();
			if(!iCommand.canUndo()) return false; 
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.model.command.ICommand#redo()
	 */
	public void redo() {
		List<ICommand> l = getCommandList();
		for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
			ICommand iCommand = (ICommand) iterator.next();
			iCommand.redo();
		}

	}
	
	@Override
	public String getCommandLabel() {
		return "CompositeCommand";
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.model.command.ICommand#undo()
	 */
	public void undo() {
		List<ICommand> l = getCommandList();
		for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
			ICommand iCommand = (ICommand) iterator.next();
			iCommand.undo();
		}

	}

	public void dispose() {
		List<ICommand> l = getCommandList();
		for (Iterator<?> iterator = l.iterator(); iterator.hasNext();) {
			ICommand iCommand = (ICommand) iterator.next();
			iCommand.dispose();
		}
	}

}
