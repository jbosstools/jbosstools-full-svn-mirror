package org.jboss.tools.smooks.model.command;

public interface ICommand {
	void execute();
	
	boolean canExecute();
	
	boolean canRedo();
	
	boolean canUndo();
	
	void redo();
	
	void undo();
	
	void dispose();
	
	String getCommandLabel();
}
