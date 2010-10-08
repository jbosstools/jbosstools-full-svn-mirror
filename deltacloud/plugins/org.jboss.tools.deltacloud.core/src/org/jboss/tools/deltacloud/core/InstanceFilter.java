package org.jboss.tools.deltacloud.core;

import java.util.regex.PatternSyntaxException;

public class InstanceFilter implements IInstanceFilter {

	private IFieldMatcher nameRule;
	private IFieldMatcher idRule;
	private IFieldMatcher imageIdRule;
	private IFieldMatcher realmRule;
	private IFieldMatcher profileRule;
	private IFieldMatcher ownerIdRule;
	private IFieldMatcher keyNameRule;
	
	@Override
	public boolean isVisible(DeltaCloudInstance instance) {
		return nameRule.matches(instance.getName()) &&
		idRule.matches(instance.getId()) &&
		imageIdRule.matches(instance.getImageId()) &&
		ownerIdRule.matches(instance.getOwnerId()) &&
		keyNameRule.matches(instance.getKey()) &&
		realmRule.matches(instance.getRealmId()) &&
		profileRule.matches(instance.getProfileId());
	}

	@Override
	public void setRules(String ruleString) throws PatternSyntaxException {
		String[] tokens = ruleString.split(";");
		if (tokens[0].equals("*")) { //$NON-NLS-1$
			nameRule = new AllFieldMatcher();
		} else {
			nameRule = new FieldMatcher(tokens[0]);
		}
		if (tokens[1].equals("*")) { //$NON-NLS-1$
			idRule = new AllFieldMatcher();
		} else {
			idRule = new FieldMatcher(tokens[1]);
		}
		if (tokens[2].equals("*")) { //$NON-NLS-1$
			imageIdRule = new AllFieldMatcher();
		} else {
			imageIdRule = new FieldMatcher(tokens[2]);
		}
		if (tokens[3].equals("*")) { //$NON-NLS-1$
			ownerIdRule = new AllFieldMatcher();
		} else {
			ownerIdRule = new FieldMatcher(tokens[3]);
		}
		if (tokens[4].equals("*")) { //$NON-NLS-1$
			keyNameRule = new AllFieldMatcher();
		} else {
			keyNameRule = new FieldMatcher(tokens[4]);
		}
		if (tokens[5].equals("*")) { //$NON-NLS-1$
			realmRule = new AllFieldMatcher();
		} else {
			realmRule = new FieldMatcher(tokens[5]);
		}
		if (tokens[6].equals("*")) { //$NON-NLS-1$
			profileRule = new AllFieldMatcher();
		} else {
			profileRule = new FieldMatcher(tokens[6]);
		}
	}
	
	@Override
	public String toString() {
		return nameRule + ";" //$NON-NLS-1$ 
		+ idRule + ";"  //$NON-NLS-1$
		+ imageIdRule + ";" //$NON-NLS-1$
		+ ownerIdRule + ";" //$NON-NLS-1$
		+ keyNameRule + ";" //$NON-NLS-1$
		+ realmRule + ";" //$NON-NLS-1$
		+ profileRule; //$NON-NLS-1$
	}

	@Override
	public IFieldMatcher getNameRule() {
		return nameRule;
	}
	
	@Override
	public IFieldMatcher getIdRule() {
		return idRule;
	}
	

	@Override
	public IFieldMatcher getImageIdRule() {
		return imageIdRule;
	}

	@Override
	public IFieldMatcher getKeyNameRule() {
		return keyNameRule;
	}

	@Override
	public IFieldMatcher getOwnerIdRule() {
		return ownerIdRule;
	}

	@Override
	public IFieldMatcher getProfileRule() {
		return profileRule;
	}

	@Override
	public IFieldMatcher getRealmRule() {
		return realmRule;
	}
}
