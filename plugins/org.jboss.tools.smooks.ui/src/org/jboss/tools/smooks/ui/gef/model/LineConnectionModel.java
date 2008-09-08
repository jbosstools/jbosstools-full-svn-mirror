package org.jboss.tools.smooks.ui.gef.model;


public class LineConnectionModel extends AbstractStructuredDataConnectionModel {

	public LineConnectionModel(IConnectableModel source,
			IConnectableModel target) {
		super(source, target);
	}
	
	public LineConnectionModel(){
		super();
	}

	public void connect() {
		this.attachSource();
		this.attachTarget();
	}
}