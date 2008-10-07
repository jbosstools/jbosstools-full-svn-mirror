package org.jboss.tools.smooks.ui.gef.commands;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;

/**
 *
 */
public class CreateConnectionCommand extends Command {

	
	
	private IConnectableModel source;
	private IConnectableModel target;
	
	/**
	 */
	private LineConnectionModel connection;
	
	/**
	 */
	public boolean canExecute() {
		if ( !validate() ) {
			return false;
		}
		return true;
	}
	
	public void execute() {
		connection = new LineConnectionModel();
		connection.setSource(source);
		connection.setTarget(target);
		connection.connect();
	}

	public void setConnection(Object model) {
		this.connection = (LineConnectionModel)model;
	}

	public void setSource(Object model) {
		this.source = (IConnectableModel)model;
	}

	public void setTarget(Object model) {
		this.target = (IConnectableModel)model;
	}
	
	public void undo() {
		connection.detachSource();
		connection.detachTarget();
	}

	/**
	 */
	public boolean validate() {
		return true;
	}
	
	/**
	 */
	public IConnectableModel getSource() {
		return source;
	}

	/**
	 */
	public IConnectableModel getTarget() {
		return target;
	}
}