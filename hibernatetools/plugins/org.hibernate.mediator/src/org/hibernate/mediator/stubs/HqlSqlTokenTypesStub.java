package org.hibernate.mediator.stubs;

import org.hibernate.mediator.base.HObject;

public class HqlSqlTokenTypesStub extends HObject {

	public static final String CL = "org.hibernate.hql.antlr.HqlSqlTokenTypes"; //$NON-NLS-1$

	public static final int IDENT = (Integer)HObject.readStaticFieldValue(CL, "IDENT"); //$NON-NLS-1$

	public HqlSqlTokenTypesStub(Object hqlSqlTokenTypes) {
		super(hqlSqlTokenTypes, CL);
	}
}
