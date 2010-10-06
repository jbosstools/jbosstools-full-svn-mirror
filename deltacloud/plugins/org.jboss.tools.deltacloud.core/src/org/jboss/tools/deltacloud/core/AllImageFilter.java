package org.jboss.tools.deltacloud.core;

public class AllImageFilter implements IImageFilter {

	private IFieldMatcher matcher = new AllFieldMatcher();
	
	@Override
	public boolean isVisible(DeltaCloudImage image) {
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
	
	public IFieldMatcher getNameRule() {
		return matcher; //$NON-NLS-1$
	}
	
	public IFieldMatcher getIdRule() {
		return matcher; //$NON-NLS-1$
	}
	
	public IFieldMatcher getArchRule() {
		return matcher; //$NON-NLS-1$
	}
	
	public IFieldMatcher getDescRule() {
		return matcher; //$NON-NLS-1$
	}

}
