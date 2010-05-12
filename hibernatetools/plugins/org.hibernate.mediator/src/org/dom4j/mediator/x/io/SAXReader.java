package org.dom4j.mediator.x.io;

import org.dom4j.mediator.x.Document;
import org.hibernate.mediator.base.HObject;
import org.xml.sax.InputSource;

public class SAXReader extends HObject {
	public static final String CL = "org.dom4j.io.SAXReader"; //$NON-NLS-1$

	protected SAXReader(Object saxReader) {
		super(saxReader, CL);
	}

	public static SAXReader newInstance() {
		return new SAXReader(HObject.newInstance(CL));
	}

	public static SAXReader create(Object obj) {
		return new SAXReader(obj);
	}

    public Document read(InputSource in) {
		Object obj = invoke(mn(), in);
		return Document.create(obj);
    }
}
