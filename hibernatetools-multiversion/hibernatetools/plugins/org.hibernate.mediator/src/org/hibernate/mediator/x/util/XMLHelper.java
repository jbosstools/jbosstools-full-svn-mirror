package org.hibernate.mediator.x.util;

import java.util.List;

import org.dom4j.mediator.x.io.SAXReader;
import org.hibernate.mediator.base.HObject;
import org.xml.sax.EntityResolver;

public class XMLHelper extends HObject {
	
	public static final String CL = "org.hibernate.util.XMLHelper"; //$NON-NLS-1$

	public static final EntityResolver DEFAULT_DTD_RESOLVER = (EntityResolver)HObject.readStaticFieldValue(CL, "DEFAULT_DTD_RESOLVER"); //$NON-NLS-1$

	protected XMLHelper(Object xmlHelper) {
		super(xmlHelper, CL);
	}

	public static XMLHelper newInstance() {
		return new XMLHelper(HObject.newInstance(CL));
	}

	@SuppressWarnings("unchecked")
	public SAXReader createSAXReader(String file, List errorsList, EntityResolver entityResolver) {
		Object obj = invoke(mn(), file, errorsList, entityResolver);
		return SAXReader.create(obj);
	}
}
