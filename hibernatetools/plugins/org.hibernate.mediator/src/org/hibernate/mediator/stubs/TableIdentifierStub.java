package org.hibernate.mediator.stubs;

import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mediator.Messages;

public class TableIdentifierStub {
	protected TableIdentifier tableIdentifier;

	protected TableIdentifierStub(Object tableIdentifier) {
		if (tableIdentifier == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.tableIdentifier = (TableIdentifier)tableIdentifier;
	}

	public static TableIdentifierStub create(TableStub table) {
		return newInstance(table.getCatalog(), table.getSchema(), table.getName() );
	}
	
	public static TableIdentifierStub newInstance(String catalog, String schema, String name) {
		return new TableIdentifierStub(new TableIdentifier(catalog, schema, name));
	}

	public String getName() {
		return tableIdentifier.getName();
	}
}
