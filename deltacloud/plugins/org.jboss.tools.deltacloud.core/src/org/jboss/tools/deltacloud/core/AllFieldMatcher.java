package org.jboss.tools.deltacloud.core;

public class AllFieldMatcher implements IFieldMatcher {

	@Override
	public boolean matches(String input) {
		return true;
	}
	
	@Override
	public String toString() {
		return "*"; //$NON-NLS-1$
	}

	public boolean isMatchesAll() {
		return true;
	}

}
