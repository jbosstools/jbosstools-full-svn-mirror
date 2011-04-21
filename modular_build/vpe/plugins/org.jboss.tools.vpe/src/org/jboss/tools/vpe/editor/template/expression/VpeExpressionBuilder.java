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
package org.jboss.tools.vpe.editor.template.expression;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.tools.vpe.messages.VpeUIMessages;

public class VpeExpressionBuilder {
	public static final char ATTR_PREFIX = '@';
	public static final String ATTR_PREFIX_S = "" + '@'; //$NON-NLS-1$
	public static final String SIGNATURE_ANY_ATTR = ATTR_PREFIX + "*"; //$NON-NLS-1$
	public static final String SIGNATURE_JSF_VALUE = "jsfvalue()"; //$NON-NLS-1$

	private static final char COMPL_EXPR_LEFT_BRACKET = '{';
	private static final char COMPL_EXPR_RIGHT_BRACKET = '}';

	private static final char OPER_BRACKET_LEFT = '(';
	private static final char OPER_BRACKET_RIGHT = ')';
	private static final char OPER_STRING = '\'';
	private static final char OPER_EQUAL = '=';
	private static final char OPER_PLUS = '+';
	private static final char OPER_OR = '|';
	private static final String OPER_AND = "and"; //$NON-NLS-1$
	private static final char FUNC_BRACKET_LEFT = OPER_BRACKET_LEFT;
	private static final char FUNC_BRACKET_RIGHT = OPER_BRACKET_RIGHT;
	private static final char PARAM_SEPARATOR = ',';
	private static final char[] SEPARATORS = new char[] { 
			OPER_BRACKET_LEFT, OPER_BRACKET_RIGHT, OPER_STRING, PARAM_SEPARATOR,
			OPER_EQUAL, OPER_OR, OPER_PLUS, ' ', '\t', '\n', '\r'}; 

	private String originalText;
	private String text;
	private boolean caseSensitive;
	private Set<String> dependencySet;
	
	public VpeExpressionInfo buildPlainExpression(String text, boolean caseSensitive) throws VpeExpressionBuilderException {
		if (text == null) {
			return new VpeExpressionInfo();
		}
		originalText = text.trim();
		this.text = originalText;
		if (end()) {
			return new VpeExpressionInfo();
		}
		this.caseSensitive = caseSensitive;
		dependencySet = new HashSet<String>();
		return new VpeExpressionInfo(build(), dependencySet.size() > 0 ? dependencySet : null); 
	}
	
	public static VpeExpressionInfo buildCompletedExpression(String text, boolean caseSensitive) throws VpeExpressionBuilderException {
		if (text == null) {
			return new VpeExpressionInfo();
		}
		VpeExpressionBuilder builder = new VpeExpressionBuilder();
		Set<String> dependencySet = new HashSet<String>();
		List<VpeExpression> expressions = new ArrayList<VpeExpression>();
		int len = text.length();
		int startIndex = 0;
		while (startIndex < len) {
			int endIndex = text.indexOf(COMPL_EXPR_LEFT_BRACKET, startIndex);
			if (endIndex < 0) {
				endIndex = len;
			}
			String subText = text.substring(startIndex, endIndex);
			startIndex = endIndex + 1;
			if (subText.length() > 0) {
				expressions.add(new VpeTextExpression(subText));
			}
			if (startIndex < len) {
				endIndex = text.indexOf(COMPL_EXPR_RIGHT_BRACKET, startIndex);
				if (endIndex < 0) {
					endIndex = len;
				}
				subText = text.substring(startIndex, endIndex);
				startIndex = endIndex + 1;
				
				VpeExpressionInfo info = builder.buildPlainExpression(subText, caseSensitive);
				if (info.getExpression() != null) {
					if (info.getDependencySet() != null) {
						dependencySet.addAll(info.getDependencySet());
					}
					expressions.add(info.getExpression());
				}
			}
		}
		if (expressions.size() <= 0) {
			return new VpeExpressionInfo();
		}
		if (dependencySet.size() <= 0) {
			dependencySet = null;
		}
		VpeExpression expression;
		if (expressions.size() == 1) {
			expression = expressions.get(0);
		} else {
			expression = new VpeCompletedExpression(expressions.toArray(new VpeExpression[expressions.size()]));
		}
		return new VpeExpressionInfo(expression, dependencySet);
	}

	public static String attrSignature(String attrName, boolean caseSensitive) {
		return ATTR_PREFIX + (caseSensitive ? attrName : attrName.toLowerCase());
	}
	
	private VpeOperand build() throws VpeExpressionBuilderException {
		VpeOperand topOperand = buildOperand();
		VpeOperation lastOperation = null;
		while (!end() && nextChar() != OPER_BRACKET_RIGHT && nextChar() != PARAM_SEPARATOR) {
			VpeOperation operation = buildOperation();
			topOperand = operation.intoExpression(topOperand, lastOperation);
			operation.setRightOperand(buildOperand());
			lastOperation = operation;
		}
		return topOperand;
	}
	
	private VpeOperand buildOperand() throws VpeExpressionBuilderException {
		VpeOperand operand = null;
		int startPos = currentPosition(); 
		char c = nextChar();

		if (c == ATTR_PREFIX) {
			operand = buildAttribute();
		} else if (c == OPER_STRING) {
			operand = buildString();
		} else if (c == OPER_BRACKET_LEFT) {
			operand = buildBrackets();
		} else if (alpha(c)) {
			int pos = nextSepPos();
			String name = getToken(pos);
			text = text.trim();
			if (nextChar() == FUNC_BRACKET_LEFT) {
				operand = buildFunction(name, startPos);
			} else {
				undefinedName(name, startPos);
			}
		} else {
			undefinedCharacter(startPos);
		}
		text = text.trim();
		return operand;
	}
	
	private VpeOperand buildAttribute() throws VpeExpressionBuilderException {
		text = text.substring(1);
		if (!alpha()) {
			undefinedCharacter();
		}
		int pos = nextSepPos();
		String name = getToken(pos);
		dependencySet.add(attrSignature(name));
		return new VpeAttributeOperand(name, caseSensitive);
	}
	
	private VpeOperand buildString() throws VpeExpressionBuilderException {
		int startPos = currentPosition(); 
		text = text.substring(1);
		int pos = text.indexOf(OPER_STRING);
		if (pos == -1) {
			error("Closing apostrophe is not found", VpeUIMessages.VpeExpressionBuilder_ClosingApostropheNotFound, startPos); //$NON-NLS-1$
		}
		VpeOperand operand = new VpeStringOperand(getToken(pos));
		text = text.substring(1);
		return operand;
	}
	
	private VpeOperand buildBrackets() throws VpeExpressionBuilderException {
		int startPos = currentPosition(); 
		text = text.substring(1);
		text = text.trim();
		VpeOperand operand = build();
		if (text.length() > 0 && text.charAt(0) == OPER_BRACKET_RIGHT) {
			text = text.substring(1);
		} else {
			bracketNotFound(startPos);
		}
		return operand;
	}
	
	private VpeOperand buildFunction(String name, int namePos) throws VpeExpressionBuilderException {
		VpeFunction function = VpeFunctionFactory.getFunction(name);
		if (function == null) {
			error(MessageFormat.format("Function \''{0}\'' is not found", name), MessageFormat.format(VpeUIMessages.VpeExpressionBuilder_FunctionNotFound, name), namePos); //$NON-NLS-1$
		}
		int bracketPos = currentPosition();
		List<VpeOperand> params = new ArrayList<VpeOperand>();
		text = text.substring(1);
		text = text.trim();
		while (!end() && nextChar() != FUNC_BRACKET_RIGHT) {
			VpeOperand param = null;
			if (nextChar() != PARAM_SEPARATOR) {
				param = build();
			}
			params.add(param);
			if (nextChar() == PARAM_SEPARATOR) {
				text = text.substring(1);
				text = text.trim();
			}
		}
		if (nextChar() != FUNC_BRACKET_RIGHT) {
			bracketNotFound(bracketPos);
		}
		text = text.substring(1);
		if (params.size() > 0) {
			function.setParameters(params.toArray(new VpeOperand[params.size()]));
		}
		String[] signatures = function.getSignatures();
		if (signatures != null) {
			for (int i = 0; i < signatures.length; i++) {
				dependencySet.add(signatures[i]);
			}
		}
		return function;
	}
	
	private VpeOperation buildOperation() throws VpeExpressionBuilderException {
		VpeOperation operation = null;
		int startPos = currentPosition(); 
		char c = nextChar();

		if (c == OPER_EQUAL) {
			operation = new VpeEqualOperation();
			text = text.substring(1);
		} else if (c == OPER_PLUS) {
			operation = new VpePlusOperation();
			text = text.substring(1);
		} else if (c == OPER_OR) {
			operation = new VpeOrOperation();
			text = text.substring(1);
		} else if (text.startsWith(OPER_AND)) {
			String oldText = text;
			text = text.substring(OPER_AND.length());
			int pos = nextSepPos();
			if (end() || pos == 0) {
				operation = new VpeAndOperation();
			} else {
				text = oldText;
				undefinedCharacter(startPos);
			}
		} else {
			undefinedCharacter(startPos);
		}
		text = text.trim();
		return operation;
	}
	
	private String getToken(int pos) {
		String token;
		if (pos == -1) {
			token = text;
			text = ""; //$NON-NLS-1$
		} else {
			token = text.substring(0, pos);
			text = text.substring(pos);
		}
		return token;
	}

	private int currentPosition() {
		return originalText.length() - text.length();
	}

	private int nextSepPos() {
		int pos = text.indexOf(SEPARATORS[0]);
		for (int i = 1; i < SEPARATORS.length; i++) {
			pos = minPosition(pos, SEPARATORS[i]);
		}
		return pos;
	}

	private int minPosition(int pos1, char c2) {
		int pos2 = text.indexOf(c2);
		if (pos1 == -1) {
			return pos2;
		} else if (pos2 == -1){
			return pos1;
		} else {
			return Math.min(pos1, pos2);
		}
	}
	
	private boolean end() {
		return text.length() <= 0;
	}
	
	private boolean alpha(char c) {
		return c >= 'A' && c<= 'Z' || c >= 'a' && c<= 'z';
	}
	
	private boolean alpha() {
		if (end()) return false;
		return alpha(text.charAt(0));
	}
	
	private char nextChar() {
		if (end()) return 0;
		return text.charAt(0);
	}

	private String attrSignature(String attrName) {
		return attrSignature(attrName, caseSensitive);
	}
	
	private void error(String errorText, String localizedErrorText, int pos) throws VpeExpressionBuilderException {
		throw new VpeExpressionBuilderException(originalText, errorText, pos, localizedErrorText);
	}
	
	private void undefinedCharacter(int pos) throws VpeExpressionBuilderException {
		error(MessageFormat.format("Undefined character \''{0}\''", originalText.charAt(pos)), MessageFormat.format(VpeUIMessages.VpeExpressionBuilder_UndefinedCharacter, originalText.charAt(pos)), pos); //$NON-NLS-1$
	}
	
	private void undefinedCharacter() throws VpeExpressionBuilderException {
		undefinedCharacter(currentPosition());
	}
	
	private void undefinedName(String name, int pos) throws VpeExpressionBuilderException {
		error(MessageFormat.format("Undefined name \"{0}\"", name), MessageFormat.format(VpeUIMessages.VpeExpressionBuilder_UndefinedName, name), pos); //$NON-NLS-1$
	}
	
	private void bracketNotFound(int pos) throws VpeExpressionBuilderException {
		error("Closing bracket is not found", VpeUIMessages.VpeExpressionBuilder_ClosingBracketNotFound, pos); //$NON-NLS-1$
	}

	public static String getOutputAttrName(String value) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.length() <= 1) {
			return null;
		}
		boolean exprBracket;
		if (value.charAt(0) == COMPL_EXPR_LEFT_BRACKET) {
			if (value.charAt(value.length() - 1) == COMPL_EXPR_RIGHT_BRACKET) {
				value = value.substring(1, value.length() - 1);
				if (value.length() <= 1) {
					return null;
				}
			} else {
				return null;
			}
		}
		if (value.startsWith("jsfvalue(")) { //$NON-NLS-1$
			if (value.charAt(value.length() - 1) == OPER_BRACKET_RIGHT) {
				value = value.substring(9, value.length() - 1);
				if (value.length() <= 1) {
					return null;
				}
			} else {
				return null;
			}
		}
		if (value.charAt(0) == ATTR_PREFIX) {
			return value.substring(1);
		}
		return null;
	}
}
