package org.hibernate.mediator.x.tool.hbm2x;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.hibernate.mediator.base.HObject;

public class XMLPrettyPrinterStub {
	public static final String CL = "org.hibernate.tool.hbm2x.XMLPrettyPrinter"; //$NON-NLS-1$

	public static void prettyPrint(InputStream in, OutputStream writer) throws IOException {
		HObject.invokeStaticMethod(CL, "prettyPrint", in, writer); //$NON-NLS-1$
	}

}
