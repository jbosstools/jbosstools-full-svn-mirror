/**
 * 
 */
package org.jboss.tools.smooks.javabean.model;

import org.jboss.tools.smooks.javabean.ui.BeanPopulatorMappingAnalyzer;

/**
 * @author Dart
 *
 */
public class SelectorAttributes {
	private String selectorSperator = " ";
	private String selectorPolicy = BeanPopulatorMappingAnalyzer.FULL_PATH;
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
