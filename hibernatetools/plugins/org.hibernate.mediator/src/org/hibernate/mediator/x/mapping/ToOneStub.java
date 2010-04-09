package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.x.FetchModeStub;

public class ToOneStub extends SimpleValueStub {
	public static final String CL = "org.hibernate.mapping.ToOne"; //$NON-NLS-1$

	protected ToOneStub(Object toOne) {
		super(toOne, CL);
	}

	protected ToOneStub(Object toOne, String cn) {
		super(toOne, cn);
	}

	public void setReferencedEntityName(String referencedEntityName) {
		invoke(mn(), referencedEntityName);
	}

	public void setReferencedPropertyName(String name) {
		invoke(mn(), name);
	}

	public void setFetchMode(FetchModeStub fetchMode) {
		invoke(mn(), fetchMode);
	}

	public boolean isEmbedded() {
		return (Boolean)invoke(mn());
	}
}
