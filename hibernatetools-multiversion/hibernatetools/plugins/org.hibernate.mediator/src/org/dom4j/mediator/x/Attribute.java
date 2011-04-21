package org.dom4j.mediator.x;

import org.hibernate.mediator.base.HObject;

public class Attribute extends Node {

	public static final String CL = "org.dom4j.Attribute"; //$NON-NLS-1$

	protected Attribute(Object attribute) {
		super(attribute, CL);
	}

	public static Attribute newInstance() {
		return new Attribute(HObject.newInstance(CL));
	}

	public String getValue() {
		return (String)invoke(mn());
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
