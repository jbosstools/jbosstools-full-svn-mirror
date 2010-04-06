package org.hibernate.mediator.stubs;

import java.util.List;

import org.hibernate.mediator.base.HObject;

public class SchemaExportStub extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2ddl.SchemaExport"; //$NON-NLS-1$

	protected SchemaExportStub(Object exporter) {
		super(exporter, CL);
	}

	protected SchemaExportStub(Object exporter, String cn) {
		super(exporter, cn);
	}

	public static SchemaExportStub createSchemaExport(Object configuration) {
		return new SchemaExportStub(HObject.newInstance(CL, configuration));
	}

	public void create(boolean b, boolean c) {
		invoke(mn(), b, c );
	}

	@SuppressWarnings("unchecked")
	public List getExceptions() {
		return (List)invoke(mn());
	}
}
