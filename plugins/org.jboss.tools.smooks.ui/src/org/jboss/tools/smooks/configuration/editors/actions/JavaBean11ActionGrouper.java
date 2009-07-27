package org.jboss.tools.smooks.configuration.editors.actions;

import org.jboss.tools.smooks.model.javabean.BindingsType;

public class JavaBean11ActionGrouper extends AbstractSmooksActionGrouper {

	@Override
	protected boolean canAdd(Object value) {
		if (value instanceof BindingsType) {
			return true;
		}
		return false;
	}

	public String getGroupName() {
		return "Java Binding v1.1";
	}

}
