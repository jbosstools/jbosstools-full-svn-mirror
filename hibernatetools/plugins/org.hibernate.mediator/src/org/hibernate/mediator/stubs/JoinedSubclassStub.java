package org.hibernate.mediator.stubs;

public class JoinedSubclassStub extends SubclassStub {
	public static final String CL = "org.hibernate.mapping.JoinedSubclass"; //$NON-NLS-1$

	protected JoinedSubclassStub(Object joinedSubclass) {
		super(joinedSubclass, CL);
	}
	
	public static JoinedSubclassStub newInstance(PersistentClassStub persistentClass) {
		return new JoinedSubclassStub(newInstance(CL, persistentClass));
	}
	
	public void setTable(TableStub table) {
		invoke("setTable", table); //$NON-NLS-1$
	}
	
	public void setKey(KeyValueStub key) {
		invoke("ssetKey", key); //$NON-NLS-1$
	}

}
