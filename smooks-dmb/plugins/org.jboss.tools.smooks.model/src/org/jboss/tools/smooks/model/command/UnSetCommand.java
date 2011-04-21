/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import java.lang.reflect.Method;

/**
 * @author Dart
 *
 */
public class UnSetCommand extends Command {
	
	private boolean executed = false;

	public UnSetCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UnSetCommand(Object host, String propertyName) {
		super(host, null, propertyName);
		// TODO Auto-generated constructor stub
	}
	
	public void execute(Object host , Object value , String propertyName){
		this.host = host;
		this.value = value;
		this.propertyName = propertyName;
		execute();
	}
	
	@Override
	public boolean canExecute() {
		return (host != null && !executed && propertyName != null);
	}

	@Override
	public boolean canRedo() {
		return super.canExecute();
	}

	@Override
	public boolean canUndo() {
		return (host != null && executed && propertyName != null);
	}
	
	@Override
	public void execute() {
		if(!canExecute()) return;
		super.execute();
		try {
			Method setter = getSetterMethod();
			setter.invoke(host, new Object[]{null});
//			PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(host, propertyName);
//			pd.getWriteMethod().invoke(host, value);
			executed = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	@Override
	public String getCommandLabel() {
		return "UnSetCommand";
	}

	@Override
	public void undo() {
		if(!canUndo()) return;
		super.undo();
		try {
			Method setter = getSetterMethod();
			setter.invoke(host, oldValue);
//			PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(host, propertyName);
//			pd.setValue(propertyName, oldValue);
			executed = false;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
