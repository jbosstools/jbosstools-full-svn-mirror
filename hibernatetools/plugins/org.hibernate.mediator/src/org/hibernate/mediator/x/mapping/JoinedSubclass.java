package org.hibernate.mediator.x.mapping;


public class JoinedSubclass extends Subclass {
	public static final String CL = "org.hibernate.mapping.JoinedSubclass"; //$NON-NLS-1$

	protected JoinedSubclass(Object joinedSubclass) {
		super(joinedSubclass, CL);
	}
	
	public static JoinedSubclass newInstance(PersistentClass persistentClass) {
		return new JoinedSubclass(newInstance(CL, persistentClass));
	}
	
	public void setTable(TableStub table) {
		invoke(mn(), table);
	}
	
	public void setKey(KeyValue key) {
		invoke(mn(), key);
	}

}
