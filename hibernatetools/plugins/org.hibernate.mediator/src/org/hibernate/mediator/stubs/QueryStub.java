package org.hibernate.mediator.stubs;

import java.util.List;

import org.hibernate.mediator.base.HObject;

public class QueryStub extends HObject {
	public static final String CL = "org.hibernate.Query"; //$NON-NLS-1$

	protected QueryStub(Object query) {
		super(query, CL);
	}

	public String[] getReturnAliases() {
		return (String[])invoke(mn());
	}

	public TypeStub[] getReturnTypes() {
		Object[] returnTypes = (Object[])invoke(mn());
		TypeStub[] res = new TypeStub[returnTypes.length];
		for (int i = 0; i < returnTypes.length; i++) {
			res[i] = TypeStubFactory.createTypeStub(returnTypes[i]);
		}
		return null;
	}

	public void setMaxResults(int intValue) {
		invoke(mn(), intValue);
	}

	public void setParameter(int position, Object val, TypeStub type) {
		invoke(mn(), position, val, type);
	}

	public void setParameter(String name, Object val, TypeStub type) {
		invoke(mn(), name, val, type);
	}

	@SuppressWarnings("unchecked")
	public List<Object> list() {
		return (List<Object>)invoke(mn());
	}

}
