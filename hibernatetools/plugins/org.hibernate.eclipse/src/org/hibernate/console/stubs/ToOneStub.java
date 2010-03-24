package org.hibernate.console.stubs;

import org.hibernate.FetchMode;
import org.hibernate.mapping.ToOne;

public class ToOneStub extends SimpleValueStub {
	protected ToOne toOne;

	protected ToOneStub(Object toOne) {
		super(toOne);
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
