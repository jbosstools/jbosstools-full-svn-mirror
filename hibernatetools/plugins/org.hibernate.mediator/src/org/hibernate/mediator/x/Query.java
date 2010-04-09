package org.hibernate.mediator.x;

import java.util.List;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.type.Type;
import org.hibernate.mediator.x.type.TypeFactory;

public class Query extends HObject {
	public static final String CL = "org.hibernate.Query"; //$NON-NLS-1$

	protected Query(Object query) {
		super(query, CL);
	}

	public String[] getReturnAliases() {
		return (String[])invoke(mn());
	}

	public Type[] getReturnTypes() {
		Object[] returnTypes = (Object[])invoke(mn());
		Type[] res = new Type[returnTypes.length];
		for (int i = 0; i < returnTypes.length; i++) {
			res[i] = TypeFactory.createTypeStub(returnTypes[i]);
		}
		return null;
	}

	public void setMaxResults(int intValue) {
		invoke(mn(), intValue);
	}

	public void setParameter(int position, Object val, Type type) {
		invoke(mn(), position, val, type);
	}

	public void setParameter(String name, Object val, Type type) {
		invoke(mn(), name, val, type);
	}

	@SuppressWarnings("unchecked")
	public List<Object> list() {
		return (List<Object>)invoke(mn());
	}

}
