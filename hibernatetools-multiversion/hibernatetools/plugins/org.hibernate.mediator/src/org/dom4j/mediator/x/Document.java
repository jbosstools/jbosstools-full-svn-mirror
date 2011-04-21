package org.dom4j.mediator.x;

import org.hibernate.mediator.base.HObject;

public class Document extends Node {
	public static final String CL = "org.dom4j.Document"; //$NON-NLS-1$

	protected Document(Object document) {
		super(document, CL);
	}

	public static Document newInstance() {
		return new Document(HObject.newInstance(CL));
	}

	public static Document create(Object obj) {
		return new Document(obj);
	}
	
	public Element getRootElement() {
		return new Element(invoke(mn()));
	}

	@Override
	public void accept(Visitor visitor) {
		invoke(mn(), visitor.Obj());
	}
}
