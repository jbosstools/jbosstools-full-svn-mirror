package org.hibernate.mediator.x.mapping;


public class JoinedSubclassStub extends SubclassStub {
	public static final String CL = "org.hibernate.mapping.JoinedSubclass"; //$NON-NLS-1$

	protected JoinedSubclassStub(Object joinedSubclass) {
		super(joinedSubclass, CL);
	}
	
	public static JoinedSubclassStub newInstance(PersistentClassStub persistentClass) {
		return new JoinedSubclassStub(newInstance(CL, persistentClass));
	}
	
	public void setTable(TableStub table) {
		invoke(mn(), table);
	}
	
	public void setKey(KeyValueStub key) {
		invoke(mn(), key);
	}

}
