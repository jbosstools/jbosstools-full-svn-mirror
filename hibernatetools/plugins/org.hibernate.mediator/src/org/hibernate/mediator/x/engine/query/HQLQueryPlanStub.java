package org.hibernate.mediator.x.engine.query;

import java.util.Map;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.SessionFactoryStub;
import org.hibernate.mediator.x.hql.QueryTranslatorStub;

public class HQLQueryPlanStub extends HObject {
	public static final String CL = "org.hibernate.engine.query.HQLQueryPlan"; //$NON-NLS-1$

	protected HQLQueryPlanStub(Object hqlQueryPlan) {
		super(hqlQueryPlan, CL);
	}

	@SuppressWarnings("unchecked")
	public static HQLQueryPlanStub newInstance(String hql, boolean shallow, Map enabledFilters, SessionFactoryStub factory) {
		return new HQLQueryPlanStub(HObject.newInstance(CL, hql, shallow, enabledFilters, factory));
	}

	public QueryTranslatorStub[] getTranslators() {
		Object[] queryTranslators = (Object[])invoke(mn());
		QueryTranslatorStub[] res = new QueryTranslatorStub[queryTranslators.length];
		for (int i = 0; i < queryTranslators.length; i++) {
			res[i] = new QueryTranslatorStub(queryTranslators[i]);
		}
		return res;
	}
}
