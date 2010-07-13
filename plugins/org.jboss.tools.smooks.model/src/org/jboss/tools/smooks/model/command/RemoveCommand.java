/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import java.util.Collection;

/**
 * @author Dart
 *
 */
public class RemoveCommand extends Command {
	
	private boolean removed=false;

	public RemoveCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RemoveCommand(Object host, Object value, String propertyName) {
		super(host, value, propertyName);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void execute() {
		if(!canExecute()) return;
		try {
			Object v = getPropertyValue();
			if(!(v instanceof Collection)){
				throw new RuntimeException("Value's type isn't Collection  , please check the " + "'" + propertyName + "'");
			}
			((Collection<?>)v).remove(value);
			removed = true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	
	@Override
	public boolean canExecute() {
		return (host != null && !removed && propertyName != null);
	}

	@Override
	public boolean canRedo() {
		return canExecute();
	}

	@Override
	public boolean canUndo() {
		return (host != null && removed && propertyName != null);
	}

	public void undo() {
		if(!canUndo()) return;
		try {
			Object v = getPropertyValue();
			if(!(v instanceof Collection)){
				throw new RuntimeException("Value's type isn't Collection  , please check the " + "'" + propertyName + "'");
			}
			((Collection)v).add(value);
			removed = false;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public String getCommandLabel() {
		return "RemoveCommand";
	}
}
