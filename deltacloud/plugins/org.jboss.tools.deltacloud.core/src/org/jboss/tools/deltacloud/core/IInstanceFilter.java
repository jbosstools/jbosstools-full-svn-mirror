package org.jboss.tools.deltacloud.core;

public interface IInstanceFilter {

	public final static String ALL_STRING = "*;*;*;*;*;*;*"; //$NON-NLS-1$
	
	public boolean isVisible(DeltaCloudInstance instance);
	public void setRules(String ruleString);
	public IFieldMatcher getNameRule();
	public IFieldMatcher getIdRule();
	public IFieldMatcher getImageIdRule();
	public IFieldMatcher getOwnerIdRule();
	public IFieldMatcher getKeyNameRule();
	public IFieldMatcher getRealmRule();
	public IFieldMatcher getProfileRule();
}
