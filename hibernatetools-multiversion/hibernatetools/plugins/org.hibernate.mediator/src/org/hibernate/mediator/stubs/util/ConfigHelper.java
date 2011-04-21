package org.hibernate.mediator.stubs.util;

import java.io.InputStream;

import org.hibernate.mediator.base.HObject;

public class ConfigHelper extends HObject {

	public static final String CL = "org.hibernate.util.ConfigHelper"; //$NON-NLS-1$
	
	protected ConfigHelper(Object configHelper) {
		super(configHelper, CL);
	}

	public static InputStream getResourceAsStream(String resource) {
		return (InputStream)invokeStaticMethod(CL, "getResourceAsStream", resource); //$NON-NLS-1$
	}
	
	public static InputStream getUserResourceAsStream(String resource) {
		return (InputStream)invokeStaticMethod(CL, "getUserResourceAsStream", resource); //$NON-NLS-1$
	}

}
