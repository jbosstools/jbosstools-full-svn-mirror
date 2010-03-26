package org.hibernate.console.stubs.util;

import java.io.Reader;

import org.hibernate.hql.antlr.HqlBaseLexer;

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

	// TODO: antlr.Token -> TokenStub?
	public Token nextToken() {
		try {
			return hqlBaseLexer.nextToken();
		} catch (TokenStreamException e) {
			return null;
		}
	}

}
