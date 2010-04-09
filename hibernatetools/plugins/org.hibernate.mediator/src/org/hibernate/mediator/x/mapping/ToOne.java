package org.hibernate.mediator.x.mapping;

import org.hibernate.mediator.x.FetchMode;

public class ToOne extends SimpleValue {
	public static final String CL = "org.hibernate.mapping.ToOne"; //$NON-NLS-1$

	protected ToOne(Object toOne) {
		super(toOne, CL);
	}

	protected ToOne(Object toOne, String cn) {
		super(toOne, cn);
	}

	public void setReferencedEntityName(String referencedEntityName) {
		invoke(mn(), referencedEntityName);
	}

	public void setReferencedPropertyName(String name) {
		invoke(mn(), name);
	}

	public void setFetchMode(FetchMode fetchMode) {
		invoke(mn(), fetchMode);
	}

	public boolean isEmbedded() {
		return (Boolean)invoke(mn());
	}
}
