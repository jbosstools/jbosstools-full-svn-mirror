package org.hibernate.mediator.x.hql.antlr;

import org.hibernate.mediator.base.HObject;

public class HqlSqlTokenTypes extends HObject {

	public static final String CL = "org.hibernate.hql.antlr.HqlSqlTokenTypes"; //$NON-NLS-1$

	public static final int IDENT = (Integer)HObject.readStaticFieldValue(CL, "IDENT"); //$NON-NLS-1$

	public HqlSqlTokenTypes(Object hqlSqlTokenTypes) {
		super(hqlSqlTokenTypes, CL);
	}
}
