package org.hibernate.mediator.stubs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.hibernate.tool.hbm2x.XMLPrettyPrinter;

public class XMLPrettyPrinterStub {
	public static final String CL = "org.hibernate.tool.hbm2x.XMLPrettyPrinter"; //$NON-NLS-1$

	public static void prettyPrint(InputStream in, OutputStream writer) throws IOException {
		XMLPrettyPrinter.prettyPrint(in, writer);
	}

}
