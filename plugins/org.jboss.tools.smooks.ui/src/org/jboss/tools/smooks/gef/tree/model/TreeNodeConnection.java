/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.jboss.tools.smooks.configuration.editors.IFieldMarker;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.IValidatableModel;

/**
 * @author DartPeng
 * 
 */
public class TreeNodeConnection implements IValidatableModel{
	
	protected int severity = IFieldMarker.TYPE_NONE;
	
	private Object data;

	private AbstractSmooksGraphicalModel sourceNode;

	private AbstractSmooksGraphicalModel targetNode;
	
	protected List<String> markerMessages = null;
	
	protected PropertyChangeSupport support = new PropertyChangeSupport(this);

	public TreeNodeConnection() {

	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Object data) {
		this.data = data;
	}



	public TreeNodeConnection(AbstractSmooksGraphicalModel sourceNode, AbstractSmooksGraphicalModel targetNode) {
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
		Assert.isNotNull(sourceNode);
		Assert.isNotNull(targetNode);
	}

	public void connect() {
		connectSource();
		connectTarget();
	}

	public void disconnect() {
		disconnectSource();
		disconnectTarget();
	}

	public void connectSource() {
		sourceNode.addSourceConnection(this);
	}

	public void connectTarget() {
		targetNode.addTargetConnection(this, sourceNode);
	}

	public void disconnectSource() {
		sourceNode.removeSourceConnection(this);
	}

	public void disconnectTarget() {
		targetNode.removeTargetConnection(this);
	}

	public AbstractSmooksGraphicalModel getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(AbstractSmooksGraphicalModel sourceNode) {
		this.sourceNode = sourceNode;
	}

	public AbstractSmooksGraphicalModel getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(AbstractSmooksGraphicalModel targetNode) {
		this.targetNode = targetNode;
	}

	public void addMessage(String message) {
		this.getMessage().add(message);
	}

	public List<String> getMessage() {
		if(markerMessages == null){
			markerMessages = new ArrayList<String>();
		}
		return markerMessages;
	}

	/**
	 * @return the severity
	 */
	public int getSeverity() {
		return severity;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		support.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		support.removePropertyChangeListener(listener);
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(int severity) {
		int old = this.severity;
		if (severity == this.severity){
			old = -1;
		}
		this.severity = severity;
		support.firePropertyChange(AbstractSmooksGraphicalModel.PRO_SEVERITY_CHANGED, old, this.severity);
	}
	
	
}
