/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class StringMatcher implements IFieldMatcher {

	private String rule;
	private Pattern pattern;

	public StringMatcher(String rule) throws PatternSyntaxException {
		this.rule = rule;
		String regexRule = transform(rule);
		pattern = Pattern.compile(regexRule, Pattern.CASE_INSENSITIVE);
	}

	private String transform(String rule) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < rule.length(); ++i) {
			char ch = rule.charAt(i);
			if (Character.isLetterOrDigit(ch))
				buffer.append(ch);
			else if (ch == '*') {
				buffer.append(".*?");
			} else {
				buffer.append('\\');
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}

	@Override
	public boolean matches(String input) {
		if (input == null) {
			return false;
		}
		Matcher m = pattern.matcher(input);
		return m.matches();
	}

	@Override
	public String toString() {
		return rule;
	}

	public boolean isMatchesAll() {
		return false;
	}
}
