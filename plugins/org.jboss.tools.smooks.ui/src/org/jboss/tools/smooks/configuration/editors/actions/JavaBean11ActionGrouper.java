package org.jboss.tools.smooks.configuration.editors.actions;


import org.jboss.tools.smooks.model.javabean.BindingsType;
import org.jboss.tools.smooks.model.javabean12.BeanType;

public class JavaBean11ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof BindingsType) {
			return true;
		}
		if (value instanceof BeanType) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return Messages.JavaBean11ActionGrouper_GroupName;
	}

}
