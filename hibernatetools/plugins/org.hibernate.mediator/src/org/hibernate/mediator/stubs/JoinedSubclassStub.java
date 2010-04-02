package org.hibernate.mediator.stubs;

import org.hibernate.mapping.JoinedSubclass;
import org.hibernate.mediator.Messages;

public class JoinedSubclassStub extends SubclassStub {
	public static final String CL = "org.hibernate.mapping.JoinedSubclass"; //$NON-NLS-1$

	protected JoinedSubclass joinedSubclass;

	protected JoinedSubclassStub(Object joinedSubclass) {
		super(joinedSubclass);
		if (joinedSubclass == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.joinedSubclass = (JoinedSubclass)joinedSubclass;
	}
	
	public static JoinedSubclassStub newInstance(PersistentClassStub persistentClass) {
		return new JoinedSubclassStub(persistentClass);
	}
	
	public void setTable(TableStub table) {
		joinedSubclass.setTable(table.getTable());
	}
	
	public void setKey(KeyValueStub key) {
		joinedSubclass.setKey(key.keyValue);
	}

}
