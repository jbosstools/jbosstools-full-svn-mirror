/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.el.core.test;

import java.util.List;

import org.jboss.tools.common.el.core.parser.LexicalToken;
import org.jboss.tools.common.el.core.parser.SyntaxError;
import org.jboss.tools.common.el.core.parser.Tokenizer;
import org.jboss.tools.common.el.core.parser.TokenizerFactory;

import junit.framework.TestCase;

public class ELParserTest extends TestCase {
	
	public ELParserTest() {}
	
	protected void setUp() throws Exception {
	}
	
	public void testTokenizerOnCorrectEL() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		//1. One variable
		checkCorrectEL(t, "#{a}");
		//2. Two EL instances
		checkCorrectEL(t, "#{a}#{b}");
		//3. Property invocation
		checkCorrectEL(t, "#{a.b}");
		//4. Argument invocation
		checkCorrectEL(t, "#{a.b['xxx']}");
		//5a. Method invocation
		checkCorrectEL(t, "#{a.b()}");
		//5b. Method invocation with one parameter
		checkCorrectEL(t, "#{a.b(c)}");
		//5b. Method invocation with two parameters
		checkCorrectEL(t, "#{a.b(c.d , e['u'])}");
		//6. Numeric
		checkCorrectEL(t, "#{a.b(16.900)}");
		//7. Boolean
		checkCorrectEL(t, "#{a.b(false)}");
		//8. Operators
		checkCorrectEL(t, "#{a.b(7 + 8) * 4 / 2 - 1}");
		//9. Complex expressions
		checkCorrectEL(t, "#{a.b(7 + 8) * (4 / 2 - 1)/c.d}");
		//10. Complex expressions
		checkCorrectEL(t, "#{a.b(7 + 8) * (4 / 2 - 1)/c.d}");		
	}

	public void testElEmptyOperator() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t, "#{empty a}");	
	}
	
	public void testElLogicalNotOperators() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t, "#{not a == null}");
		checkCorrectEL(t, "#{!a eq null}");
	}
	
	public void testElLogicalAndOperators() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t, "#{a!=null and a!=1}");
		checkCorrectEL(t, "#{a!=null && a!=1}");
	}
	
	public void testElLogicalOrOperators() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t, "#{a!=null or a!=1}");
		checkCorrectEL(t, "#{a!=null || a!=1}");
	}
	
	public void testElConditionalOperator() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t, "#{a?1:2}");
	}
	
	public void testElRelationalOperator() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t, "#{a == b}");
		checkCorrectEL(t, "#{a eq b}");
		checkCorrectEL(t, "#{a != b}");
		checkCorrectEL(t, "#{a ne b}");
		checkCorrectEL(t, "#{a < b}");
		checkCorrectEL(t, "#{a lt b}");
		checkCorrectEL(t, "#{a > b}");
		checkCorrectEL(t, "#{a < b}");
		checkCorrectEL(t, "#{a <= b}");
		checkCorrectEL(t, "#{a ge b}");
		checkCorrectEL(t, "#{a >= b}");
		checkCorrectEL(t, "#{a le b}");
		checkCorrectEL(t, "#{'a' < 'b'}");
		
	}
	
	public void testElArithmeticOperators() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t, "#{a*b}");
		checkCorrectEL(t, "#{a/b}");
		checkCorrectEL(t, "#{a div b}");
		checkCorrectEL(t, "#{a%b}");
		checkCorrectEL(t, "#{a mod b}");
		checkCorrectEL(t, "#{a-b}");
		checkCorrectEL(t, "#{a+b}");
		checkCorrectEL(t, "#{-b+1}");
		checkCorrectEL(t, "#{-b}");
	}	
	
	public void testElLiteralExpressions() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t,"#{\"#{\"}");
		checkCorrectEL(t,"\\#{exprA}");
		checkCorrectEL(t,"#{\"\\\"exprA\\\"\"}");
		checkCorrectEL(t,"#{\"\\\"#\\\"\"}");
		checkCorrectEL(t,"#{’#’}"); //Why this is correct?
	}
	
	public void testElReferencesObjectProperties() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t,"#{customer.address[\"street\"]}");
		checkCorrectEL(t,"#{customer.address['street']}");
		checkCorrectEL(t,"#{planets[object.counter].mass}");
	}
	
	public void testElSimpleTypes() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		checkCorrectEL(t,"#{\"string\"}");
		checkCorrectEL(t,"#{'string'}");
		checkCorrectEL(t,"#{11.0}");
		checkCorrectEL(t,"#{1.2E4}");
		checkCorrectEL(t,"#{null}");
		checkCorrectEL(t,"#{1}");
		checkCorrectEL(t,"#{true}");
		checkCorrectEL(t,"#{false}");
	}
	
	private void checkCorrectEL(Tokenizer t, String test) {
		LexicalToken token = t.parse(test);
		assertEquals(test, restore(token));
		List<SyntaxError> errors = t.getErrors();
		assertEquals("EL '" + test + "' has no syntax problems.", 0, errors.size());
		System.out.println("Passed correct EL '" + test + "'");
	}

	public void testTokenizerOnIncorrectEL() {
		Tokenizer t = TokenizerFactory.createJbossTokenizer();
		//1. Dot unfollowed by name
		checkIncorrectEL(t, "#{a.}", 4);
		//2. Incorrect use of ')'
		checkIncorrectEL(t, "#{a.b + -c.d + g)}", 16);
		//2. Incorrect use of ')' in second EL instance
		checkIncorrectEL(t, "#{a.b + -c.d + g}#{hh.vv..m()}", 25);
	}

	private void checkIncorrectEL(Tokenizer t, String test, int expectedErrorPosition) {
		LexicalToken token = t.parse(test);
		List<SyntaxError> errors = t.getErrors();
		assertTrue("EL '" + test + "' has syntax problems. ", errors.size() > 0);
		assertEquals(expectedErrorPosition, errors.get(0).getPosition());
		String correctPart = test.substring(0, expectedErrorPosition);
		String parsed = restore(token);
		assertTrue("Parsed value should be identical to source at least until first problem.", parsed.startsWith(correctPart));
		System.out.println("Passed incorrect EL '" + test + "'");
	}

	private String restore(LexicalToken token) {
		StringBuffer sb = new StringBuffer();
		while(token != null) {
			sb.append(token.getText());
			token = token.getNextToken();
		}
		return sb.toString();
	}

}
