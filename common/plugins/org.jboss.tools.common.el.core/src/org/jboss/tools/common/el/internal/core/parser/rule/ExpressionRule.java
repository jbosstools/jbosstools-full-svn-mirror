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
package org.jboss.tools.common.el.internal.core.parser.rule;

import org.jboss.tools.common.el.core.parser.IRule;
import org.jboss.tools.common.el.internal.core.parser.token.EndELTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.ExprStartTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.JavaNameTokenDescription;
import org.jboss.tools.common.el.internal.core.parser.token.ParamEndTokenDescription;
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
public class ExpressionRule implements IRule, BasicStates {

	public static ExpressionRule INSTANCE = new ExpressionRule();

	public int[] getStartStates() {
		return new int[] {
			STATE_EXPECTING_EL,

			STATE_EXPECTING_EXPRESSION,
			STATE_EXPECTING_NAME,
			STATE_EXPECTING_PARAM,
			STATE_EXPECTING_OPERAND
		};
	}

	public int getFinalState(int state, int token) {
		switch (token) {
			case StartELTokenDescription.START_EL:
					return STATE_EXPECTING_EXPRESSION;

			case WhiteSpaceTokenDescription.WHITESPACE: 
					return state;
			case EndELTokenDescription.END_EL:
					return STATE_EXPECTING_EL;
			case JavaNameTokenDescription.JAVA_NAME:
					return STATE_EXPECTING_CALL;
			case StringTokenDescription.STRING:
			case PrimitiveValueTokenDescription.PRIMITIVE_VALUE:
					return STATE_EXPECTING_OPERATION;
			case ParamEndTokenDescription.PARAM_END:
					return STATE_EXPECTING_CALL_AFTER_METHOD;
			case ExprStartTokenDescription.EXPR_START:
			case UnaryTokenDescription.UNARY:
					return STATE_EXPECTING_OPERAND;
		}

		return 0;
	}

	public int[] getTokenTypes(int state) {
		switch(state) {
			case STATE_EXPECTING_EL:
				return new int[]{
					StartELTokenDescription.START_EL
				};

			case STATE_EXPECTING_EXPRESSION:
				return new int[] {
					WhiteSpaceTokenDescription.WHITESPACE,
					EndELTokenDescription.END_EL,
					PrimitiveValueTokenDescription.PRIMITIVE_VALUE,					
					JavaNameTokenDescription.JAVA_NAME,
					StringTokenDescription.STRING,
					ExprStartTokenDescription.EXPR_START,
					UnaryTokenDescription.UNARY,
				};
			case STATE_EXPECTING_NAME:
				return new int[] {
					WhiteSpaceTokenDescription.WHITESPACE,
					JavaNameTokenDescription.JAVA_NAME,
				};
			case STATE_EXPECTING_PARAM:
				return new int[] {
					WhiteSpaceTokenDescription.WHITESPACE,
					PrimitiveValueTokenDescription.PRIMITIVE_VALUE,
					JavaNameTokenDescription.JAVA_NAME,
					StringTokenDescription.STRING,
					ExprStartTokenDescription.EXPR_START,
					UnaryTokenDescription.UNARY,
					ParamEndTokenDescription.PARAM_END
				};
			case STATE_EXPECTING_OPERAND:
				return new int[] {
					WhiteSpaceTokenDescription.WHITESPACE,
					PrimitiveValueTokenDescription.PRIMITIVE_VALUE,
					StringTokenDescription.STRING,
					ExprStartTokenDescription.EXPR_START,
					UnaryTokenDescription.UNARY,
					JavaNameTokenDescription.JAVA_NAME,
				};
				
		}
		return new int[0];
	}

}
