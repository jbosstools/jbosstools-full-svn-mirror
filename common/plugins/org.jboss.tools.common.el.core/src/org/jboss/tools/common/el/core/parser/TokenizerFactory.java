/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.common.el.core.parser;

import java.util.List;

import org.jboss.tools.common.el.core.model.ELExpression;
import org.jboss.tools.common.el.core.model.ELInstance;
import org.jboss.tools.common.el.core.model.ELInvocationExpression;
import org.jboss.tools.common.el.core.model.ELModel;
import org.jboss.tools.common.el.core.model.ELUtil;
import org.jboss.tools.common.el.internal.core.parser.rule.ArgRule;
import org.jboss.tools.common.el.internal.core.parser.rule.CallRule;
import org.jboss.tools.common.el.internal.core.parser.rule.ErrorRecoveryRule;
import org.jboss.tools.common.el.internal.core.parser.rule.ExpressionRule;
import org.jboss.tools.common.el.internal.core.parser.rule.OperationRule;
import org.jboss.tools.common.el.internal.core.parser.token.ArgEndTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.ArgStartTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.CommaTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.DotTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.EndELTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.ExprEndTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.ExprStartTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.JavaNameTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.OperationTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.ParamEndTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.ParamStartTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.PrimitiveValueTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.StartELTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.StringTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.UnaryTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.WhiteSpaceTokenDescription;

/**
 * 
 * @author V. Kabanovich
 *
 */
public class TokenizerFactory {

	public static Tokenizer createDefaultTokenizer() {
		Tokenizer t = new Tokenizer();
		t.setTokenDescriptions(new ITokenDescription[]{
			ArgEndTokenDescription.INSTANCE,
			ArgStartTokenDescription.INSTANCE,
			DotTokenDescription.INSTANCE,
			EndELTokenDescription.INSTANCE,
			JavaNameTokenDescription.INSTANCE,
			OperationTokenDescription.INSTANCE,
			UnaryTokenDescription.INSTANCE,
			PrimitiveValueTokenDescription.INSTANCE,
			StartELTokenDescription.INSTANCE,
			StringTokenDescription.INSTANCE,
			WhiteSpaceTokenDescription.INSTANCE,			
		});
		t.setRules(new IRule[]{
			ExpressionRule.INSTANCE,
			ArgRule.INSTANCE,
			CallRule.INSTANCE,
			OperationRule.INSTANCE,
			ErrorRecoveryRule.INSTANCE,
		});
		return t;
	}

	public static Tokenizer createJbossTokenizer() {
		Tokenizer t = new Tokenizer();
		t.setTokenDescriptions(new ITokenDescription[]{
			ArgEndTokenDescription.INSTANCE,
			ArgStartTokenDescription.INSTANCE,
			CommaTokenDescription.INSTANCE,
			DotTokenDescription.INSTANCE,
			EndELTokenDescription.INSTANCE,
			JavaNameTokenDescription.INSTANCE,
			OperationTokenDescription.INSTANCE,
			ParamEndTokenDescription.INSTANCE,
			ParamStartTokenDescription.INSTANCE,
			ExprStartTokenDescription.INSTANCE,
			ExprEndTokenDescription.INSTANCE,
			UnaryTokenDescription.INSTANCE,
			PrimitiveValueTokenDescription.INSTANCE,
			StartELTokenDescription.INSTANCE,
			StringTokenDescription.INSTANCE,
			WhiteSpaceTokenDescription.INSTANCE,			
		});
		t.setRules(new IRule[]{
			ExpressionRule.INSTANCE,
			ArgRule.INSTANCE,
			CallRule.INSTANCE,
			OperationRule.INSTANCE,
			ErrorRecoveryRule.INSTANCE,
		});
		return t;
	}

	public static void main(String[] args) {
		String text = "#{.8 +(.9d / - (-.8))}";
//"#{g11.g12.y13} #{#{  #{a14.b15(x.t.u(uu.ii[9],  j)).b16(m17(v18(i19[2]).u20).)+ a21(c.).b.}";
//"#{not a.b(x,y) + s.h((6 != -8) & (7 + -iy88.g[9].h(7  div 8).i.j)+(8) ? 4 : 7,'p', a.b.c.d[null])}";
//"q82#{a(  g.h(7  +  8) + 8, g['h'].j(),'p')}k#{b}";
		Tokenizer t = createJbossTokenizer();
		LexicalToken token = t.parse(text);
		LexicalToken ti = token;
		
		while(ti != null) {
			int type = ti.getType();
			System.out.println(type + ":" + ti.getText() + ":");
			ti = ti.getNextToken();
		}
		List<SyntaxError> errors = t.getErrors();
		for (SyntaxError e: errors) {
			System.out.println("state=" + e.getState() + " position=" + e.getPosition());
		}
		ELParser parser = ELParserUtil.getJbossFactory().createParser();
		ELModel model = parser.parse(text, 0, 90);
		System.out.println(model);

		ELExpression expr = model.getInstances().get(0).getExpression();
		System.out.println("Expression=" + expr);
		List<ELInvocationExpression> is = expr.getInvocations();
		System.out.println("Invocations:");
		for (ELInvocationExpression i : is) {
			System.out.println(i);
		}

		int off = 2;
		ELExpression expr1 = ELUtil.findExpression(model, off);
		System.out.println("Expression at " + off + ": " + expr1);
		
	}

}
