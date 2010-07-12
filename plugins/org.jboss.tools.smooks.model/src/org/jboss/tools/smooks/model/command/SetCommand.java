/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import java.lang.reflect.Method;

/**
 * @author Dart
 *
 */
public class SetCommand extends Command {
	
	public SetCommand() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SetCommand(Object host, Object value, String propertyName) {
		super(host, value, propertyName);
		// TODO Auto-generated constructor stub
	}

	public void execute(Object host , Object value , String propertyName){
		this.host = host;
		this.value = value;
		this.propertyName = propertyName;
		execute();
	}

	@Override
	public void execute() {
		super.execute();
		try {
			Method setter = getSetterMethod();
			setter.invoke(host, value);
//			PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(host, propertyName);
//			pd.getWriteMethod().invoke(host, value);
			value = null;
			execued = true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public void undo() {
		super.undo();
		try {
			Method setter = getSetterMethod();
			setter.invoke(host, oldValue);
//			PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(host, propertyName);
//			pd.setValue(propertyName, oldValue);
			oldValue = null;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
