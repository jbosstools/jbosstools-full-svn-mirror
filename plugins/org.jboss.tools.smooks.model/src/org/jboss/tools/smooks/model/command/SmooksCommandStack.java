/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Dart
 *
 */
public class SmooksCommandStack {
	private List<ICommand> executedCommands = null;
	
	private List<ICommand> undoedCommands = null;
	
	private ICommand currentExecuteCommand = null;

	public List<ICommand> getExecutedCommands() {
		
		if(executedCommands == null){
			executedCommands = new ArrayList<ICommand>();
		}
		return executedCommands;
	}
	
	
	public List<ICommand> getUndoedCommands() {
		if(undoedCommands == null){
			undoedCommands = new ArrayList<ICommand>();
		}
		return undoedCommands;
	}


	public void execute(ICommand c){
		c.execute();
		addExecutedCommand(c);
	}
	
	public void undo(){
		if(getExecutedCommands().size() > 0){
			
		}
	}
	
	public void addExecutedCommand(ICommand c){
		getExecutedCommands().add(c);
	}
	
	public void removeCommand(ICommand c){
		getExecutedCommands().remove(c);
	}
	
//	public boolean isDirty(){
//		
//	}
	
	public void cleanStack(){
		getExecutedCommands().clear();
		currentExecuteCommand = null;
	}
}
