package org.jboss.tools.deltacloud.core;

import java.util.regex.PatternSyntaxException;

/**
 * A filter for instances that matches on all elements (no criteria checked)
 * 
 * @see IInstanceFilter
 * @see DeltaCloud#getInstanceFilter()
 * 
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class AllInstanceFilter extends AbstractCloudElementFilter<DeltaCloudInstance> implements IInstanceFilter {

	public AllInstanceFilter(DeltaCloud cloud) {
		super(cloud);
	}

	private IFieldMatcher matcher = new AllFieldMatcher();

	@Override
	public boolean matches(DeltaCloudInstance instance) {
		return true;
	}

	@Override
	public void setRules(String rulesString) throws PatternSyntaxException {
		// ignore, never set the rules for this filter
	}
	
	@Override
	public IFieldMatcher getNameRule() {
		return matcher;
	}

	@Override
	public IFieldMatcher getIdRule() {
		return matcher;
	}

	@Override
	public IFieldMatcher getAliasRule() {
		return matcher;
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

	public boolean isFiltering() {
		return false;
	}

	@Override
	public String toString() {
		return ALL_STRING;
	}
	

}
