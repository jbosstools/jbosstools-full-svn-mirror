package org.hibernate.mediator.x.cfg.reveng;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.mapping.TableStub;

public class TableIdentifierStub extends HObject {
	public static final String CL = "org.hibernate.cfg.reveng.TableIdentifier"; //$NON-NLS-1$

	protected TableIdentifierStub(Object tableIdentifier) {
		super(tableIdentifier, CL);
	}

	public static TableIdentifierStub create(TableStub table) {
		return newInstance(table.getCatalog(), table.getSchema(), table.getName() );
	}
	
	public static TableIdentifierStub newInstance(String catalog, String schema, String name) {
		return new TableIdentifierStub(HObject.newInstance(CL, catalog, schema, name));
	}

	public String getName() {
		return (String)invoke(mn());
	}
}
