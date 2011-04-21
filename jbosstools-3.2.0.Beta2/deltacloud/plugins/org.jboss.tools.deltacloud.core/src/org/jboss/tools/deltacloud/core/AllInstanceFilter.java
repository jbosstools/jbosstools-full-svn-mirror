package org.jboss.tools.deltacloud.core;

public class AllInstanceFilter implements IInstanceFilter {

	private IFieldMatcher matcher = new AllFieldMatcher();
	
	@Override
	public boolean isVisible(DeltaCloudInstance instance) {
		return true;
	}

	@Override
	public void setRules(String ruleString) {
		// ignore, never set the rules for this filter
	}
	
	@Override
	public String toString() {
		return ALL_STRING;
	}
	
	@Override
	public IFieldMatcher getNameRule() {
		return matcher; //$NON-NLS-1$
	}
	
	@Override
	public IFieldMatcher getIdRule() {
		return matcher; //$NON-NLS-1$
	}
	
	@Override
	public IFieldMatcher getImageIdRule() {
		return matcher;
	}

	@Override
	public IFieldMatcher getKeyNameRule() {
		return matcher;
	}

	@Override
	public IFieldMatcher getOwnerIdRule() {
		return matcher;
	}

	@Override
	public IFieldMatcher getProfileRule() {
		return matcher;
	}

	@Override
	public IFieldMatcher getRealmRule() {
		return matcher;
	}

}
