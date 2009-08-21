/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.editparts;

import org.eclipse.gef.commands.Command;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;

/**
 * @author DartPeng
 * 
 */
public class CreateConnectionCommand extends Command {


	private AbstractSmooksGraphicalModel source;

	private AbstractSmooksGraphicalModel target;

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
	public AbstractSmooksGraphicalModel getSource() {
		return source;
	}

	public void setSource(AbstractSmooksGraphicalModel source) {
		this.source = source;
	}

	public AbstractSmooksGraphicalModel getTarget() {
		return target;
	}

	public void setTarget(AbstractSmooksGraphicalModel target) {
		this.target = target;
	}
}
