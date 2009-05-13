/**
 * 
 */
package org.jboss.tools.smooks.configuration.editors;


/**
 * @author Dart
 *
 */
public class SelectorAttributes {
	public static final String ONLY_NAME = "only_name";

	public static final String FULL_PATH = "full_path";

	public static final String IGNORE_ROOT = "ignore_root";

	public static final String INCLUDE_PARENT = "include_parent";
	private String selectorSperator = "/";
	private String selectorPolicy = SelectorAttributes.FULL_PATH;
	public String getSelectorSperator() {
		return selectorSperator;
	}
	public void setSelectorSperator(String selectorSperator) {
		this.selectorSperator = selectorSperator;
	}
	public String getSelectorPolicy() {
		return selectorPolicy;
	}
	public void setSelectorPolicy(String selectorPolicy) {
		this.selectorPolicy = selectorPolicy;
	}
	
	
}
