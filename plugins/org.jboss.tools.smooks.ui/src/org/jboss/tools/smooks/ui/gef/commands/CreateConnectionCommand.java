package org.jboss.tools.smooks.ui.gef.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.LineConnectionModel;
import org.jboss.tools.smooks.ui.gef.model.PropertyModel;

/**
 *
 */
public class CreateConnectionCommand extends Command {

	
	private List<PropertyModel> propertyList = new ArrayList<PropertyModel>();
	
	
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
	
	public LineConnectionModel getConnection() {
		return connection;
	}
	
	public void addPropertyModel(PropertyModel model){
		for (Iterator<PropertyModel> iterator = propertyList.iterator(); iterator.hasNext();) {
			PropertyModel pro = (PropertyModel) iterator.next();
			if(pro.isSameProperty(model)){
				return;
			}
		}
		propertyList.add(model);
	}

	public void setConnection(LineConnectionModel connection) {
		this.connection = connection;
	}

	public void setSource(IConnectableModel source) {
		this.source = source;
	}

	public void setTarget(IConnectableModel target) {
		this.target = target;
	}
	
	public void execute() {
		connection = new LineConnectionModel();
		for (Iterator<PropertyModel> iterator = this.propertyList.iterator(); iterator.hasNext();) {
			PropertyModel property = (PropertyModel) iterator.next();
			connection.addPropertyModel(property);
		}
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