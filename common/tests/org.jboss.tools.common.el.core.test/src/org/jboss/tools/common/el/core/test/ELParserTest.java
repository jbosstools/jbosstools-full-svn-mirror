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
		checkIncorrectEL(t, "#{a.b + -c.d + g}#{hh.vv..m()}", 16);
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
