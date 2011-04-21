package org.hibernate.mediator.x.hql.antlr;

import java.io.Reader;

import org.hibernate.mediator.base.HObject;
import org.hibernate.mediator.x.hql.antlr.HqlSqlTokenTypes;

import antlr.Token;

public class HqlBaseLexer extends HObject {

	public static final String CL = "org.hibernate.hql.antlr.HqlBaseLexer"; //$NON-NLS-1$
	
	public HqlBaseLexer(Object hqlBaseLexer) {
		super(hqlBaseLexer, CL);
	}

	public static HqlBaseLexer newInstance(Reader in) {
		return new HqlBaseLexer(HObject.newInstance(CL, in));
	}

	private Token nextToken() {
		try {
			return (Token)invoke(mn());
		} catch (Exception e) {
			//if (e.getClass().getName().contains("TokenStreamException")) { //$NON-NLS-1$
			//}
		}
		return null;
	}
	
	public boolean isNextTokenIdent() {
		Token token = nextToken();
		if (token != null && token.getType() == HqlSqlTokenTypes.IDENT) {
			return true;
		} 
		return false;
	}

}
