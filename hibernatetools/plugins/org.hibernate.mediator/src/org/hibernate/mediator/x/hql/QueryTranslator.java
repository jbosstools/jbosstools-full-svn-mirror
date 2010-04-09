package org.hibernate.mediator.x.hql;

import java.util.List;
import java.util.Set;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.Type;
import org.hibernate.mediator.x.type.TypeFactory;

public class QueryTranslator extends HObject {
	public static final String CL = "org.hibernate.hql.QueryTranslator"; //$NON-NLS-1$

	public QueryTranslator(Object queryTranslator) {
		super(queryTranslator, CL);
	}

	public boolean isManipulationStatement() {
		return (Boolean)invoke(mn());
	}

	@SuppressWarnings("unchecked")
	public Set<Object> getQuerySpaces() {
		return (Set<Object>)invoke(mn());
	}

	public Type[] getReturnTypes() {
		Object[] returnTypes = (Object[])invoke(mn());
		Type[] res = new Type[returnTypes.length];
		for (int i = 0; i < returnTypes.length; i++) {
			res[i] = TypeFactory.createTypeStub(returnTypes[i]);
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public List<String> collectSqlStrings() {
		return (List<String>)invoke(mn());
	}
}
