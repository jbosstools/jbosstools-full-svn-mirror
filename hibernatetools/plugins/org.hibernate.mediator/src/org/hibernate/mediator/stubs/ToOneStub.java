package org.hibernate.mediator.stubs;

import org.hibernate.FetchMode;
import org.hibernate.mapping.ToOne;
import org.hibernate.mediator.Messages;

public class ToOneStub extends SimpleValueStub {
	public static final String CL = "org.hibernate.mapping.ToOne"; //$NON-NLS-1$

	protected ToOne toOne;

	protected ToOneStub(Object toOne) {
		super(toOne);
		if (toOne == null) {
			throw new HibernateConsoleRuntimeException(Messages.Stub_create_null_stub_prohibit);
		}
		this.toOne = (ToOne)toOne;
	}

	public void setReferencedEntityName(String referencedEntityName) {
		toOne.setReferencedEntityName(referencedEntityName);
	}

	public void setReferencedPropertyName(String name) {
		toOne.setReferencedPropertyName(name);
	}

	public void setFetchMode(FetchModeStub fetchMode) {
		if (FetchModeStub.DEFAULT.equals(fetchMode)) {
			toOne.setFetchMode(FetchMode.DEFAULT);
		} else if (FetchModeStub.SELECT.equals(fetchMode)) {
			toOne.setFetchMode(FetchMode.SELECT);
		} else if (FetchModeStub.JOIN.equals(fetchMode)) {
			toOne.setFetchMode(FetchMode.JOIN);
		} else if (FetchModeStub.SUBSELECT.equals(fetchMode)) {
		}
	}

	public boolean isEmbedded() {
		return toOne.isEmbedded();
	}
}
