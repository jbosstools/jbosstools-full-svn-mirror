package org.jboss.tools.deltacloud.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class FieldMatcher implements IFieldMatcher {

	private String rule;
	private Pattern pattern;
	
	public FieldMatcher(String rule) throws PatternSyntaxException {
		this.rule = rule;
		String regexRule = transform(rule);
		pattern = Pattern.compile(regexRule);
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
		Matcher m = pattern.matcher(input);
		return m.matches();
	}
	
	@Override
	public String toString() {
		return rule;
	}

}
