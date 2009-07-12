/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.editparts;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;

/**
 * @author DartPeng
 * 
 */
public class CreateConnectionCommand extends Command {


	private TreeNodeModel source;

	private TreeNodeModel target;

	private TreeNodeConnection tempConnectionHandle = null;

	@Override
	public void execute() {
		if (source != null && target != null) {
			TreeNodeConnection connection = new TreeNodeConnection(source,
					target);
			connection.connect();
			tempConnectionHandle = connection;
		}
	}

	@Override
	public void redo() {
		if(tempConnectionHandle != null){
			tempConnectionHandle.connect();
		}else{
			execute();
		}
	}

	@Override
	public void undo() {
		if(tempConnectionHandle != null){
			tempConnectionHandle.disconnect();
		}
	}
	public TreeNodeModel getSource() {
		return source;
	}

	public void setSource(TreeNodeModel source) {
		this.source = source;
	}

	public TreeNodeModel getTarget() {
		return target;
	}

	public void setTarget(TreeNodeModel target) {
		this.target = target;
	}
}
