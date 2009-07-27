package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.javabean12.BeanType;

public class JavaBean12ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof BeanType) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return "Java Binding v1.2";
	}

}
