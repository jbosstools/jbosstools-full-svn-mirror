package org.jboss.tools.seam.pages.xml.model.impl;

import org.jboss.tools.common.model.impl.CustomizedObjectImpl;

public class SeamPageRedirectImpl extends CustomizedObjectImpl {
	private static final long serialVersionUID = 1L;

	public String getPresentationString() {
		String v1 = getAttributeValue("view id");
		if(v1 != null && v1.length() > 0) return v1;
		return getModelEntity().getXMLSubPath();
	}

}
