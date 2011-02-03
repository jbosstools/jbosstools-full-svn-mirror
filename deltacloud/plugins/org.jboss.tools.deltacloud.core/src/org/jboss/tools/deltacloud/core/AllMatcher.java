package org.jboss.tools.deltacloud.core;

public class AllMatcher implements IFieldMatcher {

	@Override
	public boolean matches(String input) {
		return true;
	}
	
	@Override
	public String toString() {
		return "*"; //$NON-NLS-1$
	}

}
