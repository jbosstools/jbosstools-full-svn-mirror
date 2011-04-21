/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author Dart
 * 
 */
public class Command implements ICommand {

	protected Object host;

	protected String propertyName;

	protected Object value;

	protected Object oldValue;


	public Command() {

	}

	public Command(Object host, Object value, String propertyName) {
		this.host = host;
		this.propertyName = propertyName;
		this.value = value;
	}
	
	public String getCommandLabel(){
		return null;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getHost() {
		return host;
	}

	public void setHost(Object host) {
		this.host = host;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	protected Object getPropertyValue() throws IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		return PropertyUtils.getProperty(host, propertyName);
	}

	protected Method getSetterMethod() throws SecurityException,
			NoSuchMethodException {
		char sc = propertyName.toCharArray()[0];
		String us = new String(new char[] { sc });
		us = us.toUpperCase();
		String n = propertyName.substring(1);
		n = ("set" + us + n);
		if (value != null) {
			return host.getClass().getMethod(n, value.getClass());
		}else{
			Method[] ms =  host.getClass().getMethods();
			for (int i = 0; i < ms.length; i++) {
				Method m = ms[i];
				if(m.getName().equals(n)){
					return m;
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.model.command.ICommand#execute()
	 */
	public void execute() {
		// if(!canExecute()) return;
		try {
			oldValue = getPropertyValue();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.model.command.ICommand#canExecute()
	 */
	public boolean canExecute() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.model.command.ICommand#canRedo()
	 */
	public boolean canRedo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.model.command.ICommand#canUndo()
	 */
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.model.command.ICommand#redo()
	 */
	public void redo() {
		this.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.tools.smooks.model.command.ICommand#undo()
	 */
	public void undo() {
		// if(!canUndo()) return;
		try {
			value = getPropertyValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dispose() {
	}

}
