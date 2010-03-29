package org.hibernate.mediator.stubs.util;

import java.io.Reader;

import org.hibernate.hql.antlr.HqlBaseLexer;
import org.hibernate.mediator.stubs.HqlSqlTokenTypesStub;

import antlr.Token;
import antlr.TokenStreamException;

public class HqlBaseLexerStub {
	
	protected HqlBaseLexer hqlBaseLexer;
	
	protected HqlBaseLexerStub(HqlBaseLexer hqlBaseLexer) {
		this.hqlBaseLexer = hqlBaseLexer;
	}
	
	public static HqlBaseLexerStub newInstance(Reader in) {
		return new HqlBaseLexerStub(new HqlBaseLexer(in));
	}

	private Token nextToken() {
		try {
			return hqlBaseLexer.nextToken();
		} catch (TokenStreamException e) {
			return null;
		}
	}
	
	public boolean isNextTokenIdent() {
		Token token = nextToken();
		if (token != null && token.getType() == HqlSqlTokenTypesStub.IDENT) {
			return true;
		} 
		return false;
	}

}
