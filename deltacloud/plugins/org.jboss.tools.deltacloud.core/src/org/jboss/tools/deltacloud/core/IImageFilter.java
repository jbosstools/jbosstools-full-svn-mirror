package org.jboss.tools.deltacloud.core;

public interface IImageFilter {

	public final static String ALL_STRING = ".*;.*;.*;.*"; //$NON-NLS-1$
	
	public boolean isVisible(DeltaCloudImage image);
	public void setRules(String ruleString);
	public IFieldMatcher getNameRule();
	public IFieldMatcher getIdRule();
	public IFieldMatcher getArchRule();
	public IFieldMatcher getDescRule();
}
