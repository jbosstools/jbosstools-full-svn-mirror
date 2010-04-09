package org.hibernate.mediator.x.tool.hbm2ddl;

import java.util.List;

import org.hibernate.mediator.base.HObject;

public class SchemaExport extends HObject {
	public static final String CL = "org.hibernate.tool.hbm2ddl.SchemaExport"; //$NON-NLS-1$

	protected SchemaExport(Object exporter) {
		super(exporter, CL);
	}

	protected SchemaExport(Object exporter, String cn) {
		super(exporter, cn);
	}

	public static SchemaExport createSchemaExport(Object configuration) {
		return new SchemaExport(HObject.newInstance(CL, configuration));
	}

	public void create(boolean b, boolean c) {
		invoke(mn(), b, c );
	}

	@SuppressWarnings("unchecked")
	public List getExceptions() {
		return (List)invoke(mn());
	}
}
