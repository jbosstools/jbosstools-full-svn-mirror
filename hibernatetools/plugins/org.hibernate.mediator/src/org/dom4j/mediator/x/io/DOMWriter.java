package org.dom4j.mediator.x.io;

import org.dom4j.mediator.x.Document;
import org.hibernate.mediator.base.HObject;

public class DOMWriter extends HObject {
	public static final String CL = "org.dom4j.io.DOMWriter"; //$NON-NLS-1$

	protected DOMWriter(Object saxReader) {
		super(saxReader, CL);
	}

	public static DOMWriter newInstance() {
		return new DOMWriter(HObject.newInstance(CL));
	}

	public org.w3c.dom.Document write(Document document) {
		return (org.w3c.dom.Document)invoke(mn(), document);
	}
}
