/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import java.util.Collection;

/**
 * @author Dart
 *
 */
public class AddCommand extends Command {
	
	private boolean added = false;

	public AddCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AddCommand(Object host, Object value, String propertyName) {
		super(host, value, propertyName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		try {
			Object v = getPropertyValue();
			if(!(v instanceof Collection)){
				throw new RuntimeException("Value's type isn't Collection  , please check the " + "'" + propertyName + "'");
			}
			((Collection)v).add(value);
			added = true;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public boolean canExecute() {
		return  ( host!=null && value!=null && !added && propertyName != null);
	}

	@Override
	public boolean canRedo() {
		return super.canRedo();
	}

	@Override
	public boolean canUndo() {
		return  ( host!=null && value!=null && added && propertyName != null);
	}

	@Override
	public void undo() {
		try {
			Object v = getPropertyValue();
			if(!(v instanceof Collection)){
				throw new RuntimeException("Value's type isn't Collection  , please check the " + "'" + propertyName + "'");
			}
			((Collection)v).remove(value);
			added = false;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}