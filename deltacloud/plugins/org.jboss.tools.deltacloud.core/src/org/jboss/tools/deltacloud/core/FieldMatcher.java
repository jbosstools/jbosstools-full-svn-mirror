package org.jboss.tools.deltacloud.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FieldMatcher implements IFieldMatcher {

	private String rule;
	private Pattern pattern;
	
	public FieldMatcher(String rule) {
		this.rule = rule;
		pattern = Pattern.compile(rule);
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
