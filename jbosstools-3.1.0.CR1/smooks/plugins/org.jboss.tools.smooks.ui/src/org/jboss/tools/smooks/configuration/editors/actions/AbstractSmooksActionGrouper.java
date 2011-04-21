package org.jboss.tools.smooks.configuration.editors.actions;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.action.MenuManager;

public abstract class AbstractSmooksActionGrouper implements ISmooksActionGrouper {

	public boolean belongsToGroup(Object descriptor) {
		if (descriptor instanceof CommandParameter) {
			CommandParameter parameter = (CommandParameter) descriptor;
			if (parameter.getValue() != null) {
				return canAdd(AdapterFactoryEditingDomain.unwrap(parameter.getValue()));
			}
		}
		return false;
	}
	
	abstract protected boolean canAdd(Object value);

	public void orderActions(MenuManager menuManager) {
		
	}

	public boolean isSeparator() {
		return false;
	}

}
