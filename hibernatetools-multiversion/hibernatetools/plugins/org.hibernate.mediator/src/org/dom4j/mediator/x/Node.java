package org.dom4j.mediator.x;

import org.hibernate.mediator.base.HObject;

public class Node extends HObject {

	public static final String CL = "org.dom4j.Node"; //$NON-NLS-1$

	protected Node(Object node) {
		super(node, CL);
	}

	protected Node(Object node, String cn) {
		super(node, cn);
	}

	public static Node newInstance() {
		return new Node(HObject.newInstance(CL));
	}

	public Element getParent() {
    	return new Element(invoke(mn()));
    }

	public void accept(Visitor visitor) {
	}
}
