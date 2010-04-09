package org.hibernate.mediator.x.hql;

import java.util.List;
import java.util.Set;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.TypeStub;
import org.hibernate.mediator.x.type.TypeStubFactory;

public class QueryTranslatorStub extends HObject {
	public static final String CL = "org.hibernate.hql.QueryTranslator"; //$NON-NLS-1$

	public QueryTranslatorStub(Object queryTranslator) {
		super(queryTranslator, CL);
	}

	public boolean isManipulationStatement() {
		return (Boolean)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public Set<Object> getQuerySpaces() {
		return (Set<Object>)invoke(mn());
	}

	public TypeStub[] getReturnTypes() {
		Object[] returnTypes = (Object[])invoke(mn());
		TypeStub[] res = new TypeStub[returnTypes.length];
		for (int i = 0; i < returnTypes.length; i++) {
			res[i] = TypeStubFactory.createTypeStub(returnTypes[i]);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<String> collectSqlStrings() {
		return (List<String>)invoke(mn());
	}
}
