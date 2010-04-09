package org.hibernate.mediator.x.engine.query;

import java.util.Map;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.SessionFactory;
import org.hibernate.mediator.x.hql.QueryTranslator;

public class HQLQueryPlan extends HObject {
	public static final String CL = "org.hibernate.engine.query.HQLQueryPlan"; //$NON-NLS-1$

	protected HQLQueryPlan(Object hqlQueryPlan) {
		super(hqlQueryPlan, CL);
	}

	@SuppressWarnings("unchecked")
	public static HQLQueryPlan newInstance(String hql, boolean shallow, Map enabledFilters, SessionFactory factory) {
		return new HQLQueryPlan(HObject.newInstance(CL, hql, shallow, enabledFilters, factory));
	}

	public QueryTranslator[] getTranslators() {
		Object[] queryTranslators = (Object[])invoke(mn());
		QueryTranslator[] res = new QueryTranslator[queryTranslators.length];
		for (int i = 0; i < queryTranslators.length; i++) {
			res[i] = new QueryTranslator(queryTranslators[i]);
		}
		return res;
	}
}
