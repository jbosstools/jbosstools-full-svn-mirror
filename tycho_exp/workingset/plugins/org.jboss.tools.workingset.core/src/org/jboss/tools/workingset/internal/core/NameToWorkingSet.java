package org.jboss.tools.workingset.internal.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameToWorkingSet {

	final private List<PatternInfo> patterns = new ArrayList<PatternInfo>();
	
	static class PatternInfo {
		
		PatternInfo(String patternString, String replacePattern, boolean exclusive) {
			this.patternString = patternString;
			this.replacePattern = replacePattern;
			this.exclusive = exclusive;
			this.pattern = Pattern.compile(patternString);
		}
		
		public String getPatternString() {
			return patternString;
		}
		public String getReplacePattern() {
			return replacePattern;
		}
		public boolean isExclusive() {
			return exclusive;
		}
		public Pattern getPattern() {
			return pattern;
		}
		String patternString;
		String replacePattern;
		boolean exclusive;		
		Pattern pattern;
	}
	public NameToWorkingSet() {
		
	}
	
	public void add(String pattern, String replacePattern, boolean exclusive) {
		patterns.add(new PatternInfo(pattern, replacePattern, exclusive));
	}
	
	public void clear() {
		patterns.clear();
	}
	
	public String[] getWorkingSetNames(String name) {

		Set<String> names = new HashSet<String>();
		
		for (PatternInfo pattern : patterns) {
			Matcher matcher = pattern.pattern.matcher(name);
			boolean matchFound = matcher.find();

			if (matchFound) {
				names.add(matcher.replaceFirst(pattern.replacePattern));
				if(pattern.exclusive) { break; }
			} 
		}
		
		return names.toArray(new String[names.size()]);
	}

	
}
